import axios from 'axios';
import Cookie from 'js-cookie';

const API_URL = 'https://data.junior-master.markchen.cc/api/v1'

export function doLogin (email, password) {
    return (dispatch) => {
        return axios.post(`${API_URL}/login`, {
                email: email,
                password: password
            })
            .then(function (res) {
                Cookie.set('jm-token', res.data.token)
                return true;
            })
            .catch(function (error) {
                console.log('fail!!')
            });
    }
}
