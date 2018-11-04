import React, { Component } from 'react';
import { BrowserRouter, Route, Link } from 'react-router-dom'
import TransactionList from './components/TransactionList';


const welcome = "Welcome to React-Finance";

/*
        <Route path="/" component={TransactionList} />
        <Route path={`/new1/:accountNameOwner`} component={TransactionList} />
*/

class App extends Component {

  render() {
    return (
      <BrowserRouter>
       <Route path="/" component={TransactionList} />

      </BrowserRouter>
    );
  }
}

export default App;
