import requests
from config import config

def main():
    lines = open("data/add_students.csv").readlines()
    students = [[term.strip() for term in line.split(",")] for line in lines]

    r = requests.post(config.URL_ADDRESS + "login", 
        params={"email": "mark86092@gmail.com", "password": "admin"})
    if r.status_code != 200:
        print("failed to receive admin token. details:" + r.text); return

    auth = "Bearer " + r.json()["token"]
    r = requests.get(config.URL_ADDRESS + "students", 
        headers={"Authorization": auth})
    if r.status_code != 200 or r.json()["total"] != 0:
        print("exited because the student is not empty."); return

    r = requests.get(config.URL_ADDRESS + "schools", 
        headers={"Authorization": auth})
    if r.status_code != 200:
        print("failed to receive school mapping. details:" + r.text); return
    mapping = dict()
    for school in r.json()['data']:
        mapping[school["edu_id"]] = school["id"]
        
    for student in students:
        r = requests.post(config.URL_ADDRESS + "students", 
            headers={"Authorization": auth},
            params={
                "name": student[0], 
                "gender": student[1], 
                "grade": student[2],
                "school_id": mapping[student[3]] },
            files={"photo_file": open(student[4], "rb")})
        print(student)
        print(r.status_code)
        print(r.text)

if __name__ == '__main__':
    main()
