import axios from 'axios';
import Cookie from 'js-cookie';

const API_URL = 'https://data.junior-master.markchen.cc/api/v1'
const API_TOKEN = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2RhdGEuanVuaW9yLW1hc3Rlci5tYXJrY2hlbi5jYy9hcGkvdjEvbG9naW4iLCJpYXQiOjE0NzI1NzI0MjksImV4cCI6MTUwNDEwODQyOSwibmJmIjoxNDcyNTcyNDI5LCJqdGkiOiI2OWQwZDRlNWQwNDg0MzU4NzJhODBhOWIyMGY3YzE5NCIsInN1YiI6MTR9.ECZb1RPE88BYzreB3ktrQO0NvMvS9At-K9SI4YW2us0'

function isEmpty(val){
    return (val === undefined || val == null || val.length <= 0) ? true : false;
}

function encodeQueryData(data) {
   var ret = [];

   for (var key in data) {
      ret.push(encodeURIComponent(key) + "=" + encodeURIComponent(data[key]));
   }

   return ret.join("&");
}

export function doSearch (grade, has_digitalized_question, order_by) {
    return (dispatch) => {

        let pairs = {}

        if (!isEmpty(grade)) {
            pairs['grade'] = grade
        }

        if (!isEmpty(has_digitalized_question)) {
            pairs['has_digitalized_question'] = has_digitalized_question
        }

        if (!isEmpty(order_by)) {
            pairs['order_by'] = order_by
        }

        return axios.get(`${API_URL}/questions?` + encodeQueryData(pairs), {
                headers: {
                    'Authorization': `Bearer ${API_TOKEN}`
                }
            })
            .then(function (res) {
                //alert(res);
                console.log(res)
                return true
            })
            .catch(function (error) {
                console.log('fail!!')
            });
    }
}
