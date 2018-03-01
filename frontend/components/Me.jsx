import React, { Component } from 'react'
import { connect } from 'react-redux'

import { getMeData } from '../actions/me.jsx'

export default connect (
    state => ({
        me: state.me
    }), {
        getMeData
    }
)(class MeData extends Component {
    constructor (props) {
        super(props)
    }

    componentDidMount () {
        this.props.getMeData();
    }

    render () {
        if (!this.props.me) {
            return <div>no data</div>;
        }
        console.log([...this.props.me])
        return (
            <div>
            {
                  <li>{this.props.me.name}</li>
            }
            </div>            
        );
    }
})
