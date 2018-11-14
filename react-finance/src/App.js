import React, { Component } from 'react'
import TransactionList from './components/TransactionList'
import TransactionTable from './components/TransactionTable'
import TrasactionPage from './components/TrasactionPage'
import LoadingData from './components/LoadingData'
import TransactionAdd from './components/TransactionAdd'
import { BrowserRouter, Route, Switch } from 'react-router-dom'

class App extends Component {

  render() {
    return (

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
  }
}

export default App;
