import React, { Component } from 'react'
import { connect } from 'react-redux'

import { getAskList } from '../actions/ask.jsx'

export default connect (
    state => ({
        ask: state.ask
    }), {
        getAskList
    }
)(class AskList extends Component {
    constructor (props) {
        super(props)
    }

    componentDidMount () {
        this.props.getAskList();
    }

    render () {
        // 沒有資料的情形，顯示 "no ask" 字樣
        if (!this.props.ask) {
            return <div>no ask</div>;
        }

        return (
            <ul>
            {
                this.props.ask.map(
                    eachAsk => (
                        <li key={`ask--${eachAsk.id}`}>
                            { eachAsk.content }
                        </li>
                    )
                )
            }
            </ul>
        );
    }
})
