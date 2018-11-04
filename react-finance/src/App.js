import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

import DropdownMenu from './components/DropdownMenu';
//import DownshiftMultiple from './components/DownshiftMultiple';
import TransactionTable from './components/TransactionTable';

const welcome = "Welcome to React-Finance";

class App extends Component {
  constructor() {
    super()
  }

  render() {
    return (
      
      <div className="App">
        <DropdownMenu />
          <header className="App-header">
          <p>
            {welcome}
          </p>
          <TransactionTable />
        </header>
      </div>
    );
  }
}

export default App;
