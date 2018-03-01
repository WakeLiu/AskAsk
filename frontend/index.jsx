if (location.hostname === 'localhost') {
    require('./scss/index.scss');
}

import React from 'react';
import ReactDOM from 'react-dom';
import { createStore, combineReducers, applyMiddleware, compose } from 'redux';
import { Provider } from 'react-redux';
import thunkMiddleware from 'redux-thunk';
import { Router, Route, IndexRoute, browserHistory } from 'react-router'

import App from './components/App.jsx';
import Notification from './components/Notification.jsx';
import StudentList from './components/StudentList.jsx';
import Student from './components/Student.jsx';
import AskList from './components/AskList.jsx';
import Login from './containers/Login.jsx';

import reducers from './reducers/index.jsx'

const createStoreWithMiddleware = applyMiddleware(thunkMiddleware);

const store = createStore(
    combineReducers(reducers),
    {},
    compose(createStoreWithMiddleware)
);

ReactDOM.render(
    <Provider store={ store }>
        <Router history={ browserHistory }>
            <Route path="/" component={ App } />
            <Route path="login" component={ Login } />
            <Route path="notification" component={ Notification } />
            <Route path="students" component={ StudentList } />
            <Route path="students/:id" component={ Student } />
            <Route path="asks" component={ AskList } />
        </Router>
    </Provider>,
    document.getElementById('root')
);
