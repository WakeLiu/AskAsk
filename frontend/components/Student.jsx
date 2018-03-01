import React, { Component } from 'react'
import { connect } from 'react-redux'

import { getStudentData, changeStudentLevel } from '../actions/student.jsx'

import Topbar from './Topbar.jsx'

export default connect (
    state => ({
        student: state.student
    }), {
        getStudentData,
        changeStudentLevel
    }
)(class Student extends Component {
    constructor (props) {
        super(props)

        this.changeLevel = this.changeLevel.bind(this)
    }

    componentDidMount () {
        this.props.getStudentData(this.props.params.id);
    }

    render () {
        return (
            <div>
                <Topbar />
                <div className="container--wide">
                    {
                        this.props.student.data ?
                        this.getStudentHtml() : 'No data found.'
                    }
                </div>
            </div>
        );
    }

    getStudentHtml () {
        let student = this.props.student.data,
            avatar = student.photo ? student.photo.filename : 'http://25.media.tumblr.com/tumblr_mcz1a4WCjw1rd0rbzo1_400.gif';

        return (
            <main className="student-data">
                <div className="student__row">
                    <div className="student-data__col">
                        <img className="student-img" src={avatar} alt={`${student.nickname}的大頭照`} />
                    </div>
                    <div className="student-data__col">
                        <p>暱稱：{ student.nickname }</p>
                        <p>學校：{ student.school.fullname }</p>
                        <p>年級：{ student.grade }</p>
                        <p>性別：{ student.gender }</p>
                    </div>
                    <div className="student-data__col">
                        <p>真實名稱：{ student.name }</p>
                        <p>信箱位址：{ student.user.email || '( 目前沒有信箱 )' }</p>
                        <p>電話號碼：{ student.user.phone || '( 目前沒有電話號碼 )' }</p>
                        <p>創立時間：{ student.created_at }</p>
                        <p>上次更新時間：{ student.updated_at }</p>
                    </div>
                </div>

                <hr className="hr--theme"/>

                <div className="student__row">
                    <p>
                        <span className="lineheight--40">權限：</span>
                        <select className="select--theme" name="level-select"
                                defaultValue={student.status} onChange={ evt => this.changeLevel(evt.target) }>
                            <option value="0">0 - 停權中</option>
                            <option value="1">1 - 可瀏覽</option>
                            <option value="2">2 - 審核中</option>
                            <option value="3">3 - 可發問</option>
                            <option value="4">4 - 可以自其他裝置登入</option>
                        </select>
                    </p>
                </div>
            </main>
        );
    }

    changeLevel (target) {
        const { id } = this.props.params;
        this.props.changeStudentLevel(id, target.value);
    }
})
