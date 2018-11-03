import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

import ExampleForms from './components/ExampleForms';
import DropdownMenu from './components/DropdownMenu';
import MyDropDown from './components/MyDropDown';
import DownshiftMultiple from './components/DownshiftMultiple';

const welcome = "Welcome to React-Finance";

class App extends Component {

    constructor(){
    super()
    this.state = {
      location: [],
      fruit: []
    }
  }
  
  toggleSelected = (id, key) => {
    let temp = JSON.parse(JSON.stringify(this.state[key]))
  }

  resetThenSet = (id, key) => {
    let temp = JSON.parse(JSON.stringify(this.state[key]))
    temp.forEach(item => item.selected = false);
    temp[id].selected = true;
    this.setState({
      [key]: temp
    })
  }
  
  render() {
    return (
      
      <div className="App">
        <DropdownMenu />
          <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <p>
            <ExampleForms />
          </p>
          <p>
            {welcome}
          </p>
          <DownshiftMultiple />
        </header>
      </div>
    );
  }
}

class Welcome extends Component {
  render() {
    console.log('hi');
    const {text} = this.props;
    return (
    <div>
      <b>{this.props.text}</b>
      <b>{text}</b>
    </div>
    );
  }
}

export default App;
