import React, { Component } from 'react';
import { BrowserRouter, Route, Link } from 'react-router-dom'
import TransactionList from './components/TransactionList';
import TransactionTable from './components/TransactionTable';
import TrasactionPage from './components/TrasactionPage';

/*
        <Route path="/" component={TransactionList} />
        <Route path={`/new1/:accountNameOwner`} component={TransactionList} />
      <Route path='/list' component={TransactionList} />       
	   <Route path="/list/:accountNameOwner" component={TransactionList} />
	         <Route path='/table/:accountNameOwner' component={TransactionTable} />
		*/

class App extends Component {

  render() {
    return (
      <div>
      <Route path="/list/:accountNameOwner?" component={TransactionList} />
      <Route path='/table/:accountNameOwner' component={TransactionTable} />
      <Route path='/page' component={TrasactionPage} />
      </div>
    );
  }
}

export default App;
