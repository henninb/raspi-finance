import React from 'react'
//import { BrowserRouter as Router, Route, Link } from 'react-router-dom';
import { Route, IndexRoute } from 'react-router-dom'
import TransactionList from './TransactionList'
import TransactionTable from './TransactionTable'
import TrasactionPage from './TrasactionPage'
import SimpleSelect from './SimpleSelect'
import LoadingData from './LoadingData'
import TransactionAdd from './TransactionAdd'

const Routes = () => (
    <Router>
        <Route path="/loading" component={LoadingData} />
        <Route path="/list/:accountNameOwner?" component={TransactionList} />
        <Route path="/table/:accountNameOwner" component={TransactionTable} />
        <Route path="/page" component={TrasactionPage} />
        <Route path="/add" component={TransactionAdd} />
    </Router>
);

export default Routes;
