import React, { Component } from 'react';
//import axios from "axios";
import './DropdownMenu.css';



class DropdownMenu extends Component {  
  constructor(props) {
    super(props);
    this.state = {
      accounts: [],
    };
  }

  /*
  componentDidMount() {
    axios
      .get("https://jsonplaceholder.typicode.com/users")
      .then(response => {

        // create an array of contacts only with relevant data
        const newContacts = response.data.map(c => {
          return {
            id: c.id,
            name: c.name
          };
        });

        // create a new "State" object without mutating 
        // the original State object. 
        const newState = Object.assign({}, this.state, {
          contacts: newContacts
        });

        // store the new state object in the component's state
        this.setState(newState);
      })
      .catch(error => console.log(error));
  }
  */
  
  /*
  componentDidMount() {
    fetch('http://localhost:8080/select_accounts')
    .then(results => {
      return results.json();
    }).then(data => {
      let accounts = data.results.map((accountNameOwner) => {
      return (
      <a href="#">Link blah</a>
      )
    })
    this.setState({accounts, accounts});
    console.log("state", this.state.accounts)
    })
  }
  
  
  
        .then(data => this.setState({ accounts }));
  */
  setAccountUrl(accountNameOwner) {
      var url = 'http://localhost:8080/get_by_account_name_owner/' + accountNameOwner;
      return url;
  }
  componentDidMount() {
    fetch('http://localhost:8080/select_accounts')
      .then(response => response.json())
      .then(data => this.setState({ accounts: data }));
  }

  render() {
    return (

<div>
<ul>
  <li><a href="#home">Home</a></li>
  <li><a href="#news">News</a></li>
  <li className="dropdown">
    <a href="javascript:void(0)" className="dropbtn">Dropdown</a>
    <div className="dropdown-content">
      {

        this.state.accounts.map(accounts => { 
        return <a href={this.setAccountUrl(accounts.accountNameOwner)}>{accounts.accountNameOwner}</a>})
      }
    </div>
  </li>
  <li><a href="#next">Next</a></li>
</ul>

</div>

    );
  }
}

export default DropdownMenu;

