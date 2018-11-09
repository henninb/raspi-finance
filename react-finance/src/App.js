import React, { Component } from 'react'
import { Route } from 'react-router-dom'
import TransactionList from './components/TransactionList'
import TransactionTable from './components/TransactionTable'
import TrasactionPage from './components/TrasactionPage'
//import SimpleSelect from './components/SimpleSelect'
import LoadingData from './components/LoadingData'
import TransactionAdd from './components/TransactionAdd'

class App extends Component {

  render() {
    return (
      <div>
      {/* <IndexRoute component={TransactionList}/> */}
        <Route path="/loading" component={LoadingData} />
        <Route path="/list/:accountNameOwner?" component={TransactionList} />
        <Route path="/table/:accountNameOwner" component={TransactionTable} />
        <Route path="/page" component={TrasactionPage} />
        <Route path="/add" component={TransactionAdd} />
        
        {/*<Route path="*" component={TransactionList} /> */}
      </div>
    );
  }
}

export default App;
