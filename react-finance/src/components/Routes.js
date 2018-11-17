import React from 'react'
import { BrowserRouter, Route, Switch } from 'react-router-dom'
import TransactionList from './TransactionList'
//import TransactionTable from './TransactionTable'
//import TrasactionPage from './TrasactionPage'
import LoadingData from './LoadingData'
import TransactionAdd from './TransactionAdd'
//import TransactionUpdate from './TransactionUpdate'

const Routes = () => (
    <BrowserRouter>
      <Switch>
        <Route path="/loading" component={LoadingData} />
       {/* <Route path="/list/:accountNameOwner?" component={TransactionList} /> */}
        <Route path="/list" component={TransactionList} />
        {/* <Route path="/table/:accountNameOwner" component={TransactionTable} /> */ }
        {/*  <Route path="/page" component={TrasactionPage} /> */ }
        <Route path="/add" component={TransactionAdd} />
        <Route path="/" component={TransactionList} />
      </Switch>
    </BrowserRouter>
);

export default Routes;
