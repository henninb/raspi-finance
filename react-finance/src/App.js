import React, { Component } from 'react';
import { Route } from 'react-router-dom'
import TransactionList from './components/TransactionList';
import TransactionTable from './components/TransactionTable';
import TrasactionPage from './components/TrasactionPage';
import SimpleSelect from './components/SimpleSelect';

class App extends Component {

  render() {
    return (
      <div>
      <Route path="/list/:accountNameOwner?" component={TransactionList} />
      <Route path='/table/:accountNameOwner' component={TransactionTable} />
      <Route path='/page' component={TrasactionPage} />
      <Route path='/select' component={SimpleSelect} />
      </div>
    );
  }
}

export default App;
