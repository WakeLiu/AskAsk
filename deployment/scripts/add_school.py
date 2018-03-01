import requests
from config import config

def main():
    lines = open("data/add_school.csv").readlines()
    schools = [[term.strip() for term in line.split(",")] for line in lines]

    r = requests.post(config.URL_ADDRESS + "login", 
        params={"email": "mark86092@gmail.com", "password": "admin"})
    if r.status_code != 200:
        print("failed to receive admin token. details:" + r.text); return

    auth = "Bearer " + r.json()["token"]
    r = requests.get(config.URL_ADDRESS + "schools", 
        headers={"Authorization": auth})
    if r.status_code != 200 or r.json()["total"] != 0:
        print("exited because the schools is not empty."); return


    for school in schools:
        r = requests.post(config.URL_ADDRESS + "schools", 
            headers={"Authorization": auth},
            params={
                "name": school[0], 
                "fullname": school[1], 
                "edu_id": school[2],
                "district": school[3] })
        print(school[0])
        print(r.status_code)
        print(r.text)

if __name__ == '__main__':
    main()
