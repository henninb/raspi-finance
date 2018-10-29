import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Button from './components/Button';
import DropdownMultiple from './components/DropdownMultiple';
import Dropdown from './components/Dropdown';

const welcome = "Welcome to React-Finance";

class App extends Component {

    constructor(){
    super()
    this.state = {
      location: [
        {
          id: 0,
          title: 'New York',
          selected: false,
          key: 'location'
        },
        {
          id: 1,
          title: 'Dublin',
          selected: false,
          key: 'location'
        },
        {
          id: 2,
          title: 'California',
          selected: false,
          key: 'location'
        },
        {
          id: 3,
          title: 'Istanbul',
          selected: false,
          key: 'location'
        },
        {
          id: 4,
          title: 'Izmir',
          selected: false,
          key: 'location'
        },
        {
          id: 5,
          title: 'Oslo',
          selected: false,
          key: 'location'
        },
        {
          id: 6,
          title: 'Zurich',
          selected: false,
          key: 'location'
        }
      ],
      fruit: [
        {
          id: 0,
          title: 'Apple',
          selected: false,
          key: 'fruit'
        },
        {
          id: 1,
          title: 'Orange',
          selected: false,
          key: 'fruit'
        },
        {
          id: 2,
          title: 'Grape',
          selected: false,
          key: 'fruit'
        },
        {
          id: 3,
          title: 'Pomegranate',
          selected: false,
          key: 'fruit'
        },
        {
          id: 4,
          title: 'Strawberry',
          selected: false,
          key: 'fruit'
        }
      ]
    }
  }
  
  toggleSelected = (id, key) => {
    let temp = JSON.parse(JSON.stringify(this.state[key]))
    temp[id].selected = !temp[id].selected
    this.setState({
      [key]: temp
    })
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
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <p>
            <Welcome text="my text" />
          </p>
          <p>
            <Button text="mybutton" />
          </p>
          <p>
            {welcome}
          </p>

          <p>
          <Dropdown
            title="Select fruit"
            list={this.state.fruit}
            resetThenSet={this.resetThenSet}
          />
        </p>
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
