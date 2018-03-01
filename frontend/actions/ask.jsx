import axios from 'axios';

const API_URL = 'https://data.junior-master.markchen.cc/api/v1'
const API_TOKEN = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2RhdGEuanVuaW9yLW1hc3Rlci5tYXJrY2hlbi5jYy9hcGkvdjEvbG9naW4iLCJpYXQiOjE0NjgxNTI3NDMsImV4cCI6MTQ5OTY4ODc0MywibmJmIjoxNDY4MTUyNzQzLCJqdGkiOiJmODdiZmYyNDc3NzgzYWVmOTg0YmI5MDA5ZGZmMGM2NiIsInN1YiI6MjB9.RWjWYaGOrXPdkTOFbyb_aCsde_ORZyYgsenhn1-DQmk'

function receiveAskList (data) {

    return {
        type: 'RECEIVE_ASK_LIST',
        list: data.data
    }
}

export function getAskList () {
    return (dispatch) => {
        return axios.get(`${API_URL}/questions`, {
                headers: {
                    'Authorization': `Bearer ${API_TOKEN}`
                }
            })
            .then(function (res) {
            	console.log(res.data);
                dispatch(receiveAskList(res.data))
            })
            .catch(function (error) {
                console.log('fail')
            });
    }
}

