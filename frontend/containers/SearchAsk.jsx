import React, { Component } from 'react'
import { connect } from 'react-redux'

import { doSearch } from '../actions/search_ask.jsx'

export default connect (
    state => state, {
        doSearch
    }
)(class SearchAsk extends Component {
    constructor (props) {
        super(props)
    }

    render () {
        return (
            <form onSubmit={ (e) => { this._doSearch(e) } }>
                年級：
                <select ref="grade">
                    <option value="">不拘</option>
                    <option value="一年級">一年級</option>
                    <option value="二年級">二年級</option>
                    <option value="三年級">三年級</option>
                </select> <br/>
            
                是否已經被數位化？
                <select ref="has_digitalized_question">
                    <option value="">不拘</option>
                    <option value="1">是</option>
                    <option value="0">否</option>
                </select> <br/>

                排序方式
                <select ref="order_by">
                    <option value="">不拘</option>
                    <option value="created_at">發問時間</option>
                    <option value="updated_at">更新時間</option>
                </select> <br/>

                <button>搜尋</button>
            </form>
        );
    }

    _doSearch (e) {
        e.preventDefault();

        let grade = this.refs.grade.value,
            has_digitalized_question = this.refs.has_digitalized_question.value,
            order_by = this.refs.order_by.value
        
        this.props.doSearch(grade, has_digitalized_question, order_by)
            .then(isSuccess => {
                if (isSuccess) {
                    //alert('sucess~!!!!')
                } else {
                    alert('fail~!!!!')
                }
            })
    }
})
