import axios from 'axios';
import Cookie from 'js-cookie';

const API_URL = 'https://data.junior-master.markchen.cc/api/v1'
const API_TOKEN = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2RhdGEuanVuaW9yLW1hc3Rlci5tYXJrY2hlbi5jYy9hcGkvdjEvbG9naW4iLCJpYXQiOjE0NjgwNDYxNjcsImV4cCI6MTQ5OTU4MjE2NywibmJmIjoxNDY4MDQ2MTY3LCJqdGkiOiI3YWFlNzQ5ZjlhNTNiMjA1ODhhOWU1YzFmMDAxNjcwMyIsInN1YiI6MX0.Gb-WAxAyVK1W6u9majgY0amqEIj-5DUDz-VjLHuIFP0'

function receiveVolunteerList (data) {
    return {
        type: 'RECEIVE_VOLUNTEER_LIST',
        list: data.data
    }
}

export function getVolunteerList () {
    return (dispatch) => {
        return axios.get(`${API_URL}/volunteers`, {
                headers: {
                    'Authorization': `Bearer ${API_TOKEN}`
                }
            })
            .then(function (res) {
                dispatch(receiveVolunteerList(res.data))
            })
            .catch(function (error) {
                console.log('fail')
            });
    }
}

export function getVolunteerData (volunteer_id) {
    // backend not ready
}

export function updateVolunteerData (volunteer_id, data) {
    // backend not ready
}
