import axios from 'axios';
import Cookie from 'js-cookie';

const API_URL = 'https://data.junior-master.markchen.cc/api/v1'
const API_TOKEN = Cookie.get('jm-token')

function receiveStudentList (list) {
    return {
        type: 'RECEIVE_STUDENT_LIST',
        list: list
    }
}

export function getStudentList () {
    return (dispatch) => {
        return axios.get(`${API_URL}/students?show_all=1`, {
                headers: {
                    'Authorization': `Bearer ${API_TOKEN}`
                }
            })
            .then(function (res) {
                dispatch(receiveStudentList(res.data))
            })
            .catch(function (error) {
                console.log('fail')
            });
    }
}



function receiveStudentData (data) {
    return {
        type: 'RECEIVE_STUDENT_DATA',
        data: data
    }
}

export function getStudentData (studentId) {
    return (dispatch) => {
        return axios.get(`${API_URL}/students/${studentId}`, {
                headers: {
                    'Authorization': `Bearer ${API_TOKEN}`
                }
            })
            .then(function (res) {
                dispatch(receiveStudentData(res.data))
            })
            .catch(function (error) {
                console.log('fail')
            });
    }
}



export function changeStudentLevel (studentId, level) {
    return (dispatch) => {
        return axios.patch(`${API_URL}/students/${studentId}`, {
                status: parseInt(level)
            })
            .then(function (res) {
                console.log(res)
                return true
            })
            .catch(function (error) {
                console.log(error)
                console.log('fail!!')
            });
    }
}
