import React, { Component } from 'react'
import { connect } from 'react-redux'

import { getVolunteerList } from '../actions/volunteer.jsx'

import Topbar from './Topbar.jsx'

export default connect (
    state => ({
        volunteer: state.volunteer
    }), {
        getVolunteerList
    }
)(class VolunteerList extends Component {
    constructor (props) {
        super(props)
    }

    componentDidMount () {
        this.props.getVolunteerList();
    }

    render () {
        console.log([...this.props.volunteer])
        return (
            <div>
                {
                   this.props.volunteer.map(
                       eachVolunteer => (
                           <li>
                               { eachVolunteer.name }
                           </li>
                       )
                   )
               }
            </div>
        );
    }
})
