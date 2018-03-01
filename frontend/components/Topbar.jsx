import React, { Component } from 'react'
import Cookie from 'js-cookie';

export default class Topbar extends Component {
    constructor (props) {
        super(props);

        this.logout = this.logout.bind(this);
    }

    render () {
        if (this.props.isLogin) {
            return (
                <header className="header-bar">
                    <div className="container--wide">
                        <h1>Junior Master</h1>
                    </div>
                </header>
            );
        }

        return (
            <header className="header-bar">
                <div className="container--wide">
                    <h1>Junior Master</h1>
                    <div className="header-menu">
                        <nav className="header-nav">
                            <ul>
                                <li><a href="/notification">通知</a></li>
                                <li><a href="/students">學生列表</a></li>
                                <li><a href="#">志工列表</a></li>
                                <li><a href="#">問題列表</a></li>
                                <li><a href="#">任務列表</a></li>
                                <li><a href="#">個人檔案</a></li>
                            </ul>
                        </nav>
                        <span className="header-btn--right" onClick={ this.logout }>
                            登出
                        </span>
                    </div>
                </div>
            </header>
        );
    }

    logout () {
        Cookie.remove('jm-token');
        location.href = '/login';
    }
}
