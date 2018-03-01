import React, { Component } from 'react'
import { connect } from 'react-redux'

import { doLogin } from '../actions/member.jsx'

import Topbar from "../components/Topbar.jsx";

export default connect (
    state => state, {
        doLogin
    }
)(class Login extends Component {
    constructor (props) {
        super(props)
    }

    render () {
        return (
            <div>
                <Topbar isLogin={ true } />
                <form className="container--wide" onSubmit={ (e) => { this._doLogin(e) } }>
                    <p>
                        帳號：<br/><br/>
                        <input className="input--theme"
                               ref="email" type="email" name="email" required />
                    </p>
                    <p>
                        密碼：<br/><br/>
                        <input className="input--theme"
                               ref="password" type="password" name="password" required />
                    </p>
                    <button className="btn--theme">登入</button>
                </form>
            </div>
        );
    }

    _doLogin (e) {
        e.preventDefault();

        let email = this.refs.email.value,
            password = this.refs.password.value

        this.props.doLogin(email, password)
            .then(isSuccess => {
                if (isSuccess) {
                    location.href = '/students'
                } else {
                    alert('fail~!!!!')
                }
            })
    }
})
