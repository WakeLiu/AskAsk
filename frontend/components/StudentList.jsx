import React, { Component } from 'react'
import { connect } from 'react-redux'

import { getStudentList } from '../actions/student.jsx'

import Topbar from './Topbar.jsx'

class StudentLi extends Component {
    render () {
        var { student } = this.props,
            avatar = student.photo ? student.photo.filename : 'http://25.media.tumblr.com/tumblr_mcz1a4WCjw1rd0rbzo1_400.gif';

        return (
            <li className="student-li">
                <a className="student-link" href={`/students/${student.id}`}>
                    <img className="student-img" src={avatar} alt={`${student.nickname}的大頭照`} />
                    <div className="student-li__info">
                        <p>{student.nickname} ( {student.name} )</p>
                        <p>{student.school.fullname} {student.grade} ( {student.gender} )</p>
                        { student.user.email ? <p>信箱：{student.user.email}</p> : null }
                        <p>級別：{student.status}</p>
                        <p>創立時間：{student.created_at} / 上次更新時間：{student.updated_at}</p>
                    </div>
                </a>
            </li>
        );
    }
}

export default connect (
    state => ({
        student: state.student
    }), {
        getStudentList
    }
)(class StudentList extends Component {
    constructor (props) {
        super(props)
    }

    componentDidMount () {
        this.props.getStudentList();
    }

    render () {
        return (
            <div>
                <Topbar />
                <div className="container--wide">
                    {
                        (this.props.student.list && this.props.student.list.length) ?
                        this.getListHtml() : 'No students found.'
                    }
                </div>
            </div>
        );
    }

    getListHtml () {
        return (
            <div className="container--wide">
                <ul className="ul--no-padding">
                    {
                        this.props.student.list.map(eachStudent =>
                            <StudentLi key={`student--${eachStudent.id}`} student={eachStudent} />
                        )
                    }
                </ul>
            </div>
        );
    }
})
