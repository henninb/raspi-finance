import React from 'react'
import { BrowserRouter, Route, Switch } from 'react-router-dom'
import TransactionList from './TransactionList'
import TransactionTable from './TransactionTable'
import TrasactionPage from './TrasactionPage'
import LoadingData from './LoadingData'
import TransactionAdd from './TransactionAdd'

const Routes = () => (
    <BrowserRouter>
      <Switch>
        <Route path="/loading" component={LoadingData} />
        <Route path="/list/:accountNameOwner?" component={TransactionList} />
        <Route path="/table/:accountNameOwner" component={TransactionTable} />
        <Route path="/page" component={TrasactionPage} />
        <Route path="/add" component={TransactionAdd} />
      </Switch>
    </BrowserRouter>
);

export default Routes;
