import React, { Component } from 'react';
import { withStyles } from '@material-ui/core/styles'
import axios from "axios";
//import SimpleSelect from './SimpleSelect';
import './DropdownMenu.css';

class DropdownMenu extends Component {  
  constructor(props) {
    super(props);
    this.state = {
      accounts: [],
    };
  }
  
  setAccountUrl(accountNameOwner) {
      var url = '/list/' + accountNameOwner;
      return url;
  }
  
  componentDidMount() {
    axios.get("http://localhost:8080/select_accounts").then(result => {
      this.setState({
        accounts:result.data,
      })
    }).catch(error => {
      console.log(error)
    })
  }

  render() {
    return (

<div>
<ul>
  <li><a href="/list">Home</a></li>
  <li><a href="#add_accounts">Add Account</a></li>
  <li><a href="#payments">Payments</a></li>
  <li className="dropdown">
    <a href="javascript:void(0)" className="dropbtn">Accounts</a>
    <div className="dropdown-content">
      {
        this.state.accounts.map(accounts => { 
        return <a href={this.setAccountUrl(accounts.accountNameOwner)}>{accounts.accountNameOwner}</a>})
      }
    </div>
  </li>
</ul>

</div>

    );
  }
}

const styles = theme => ({
  listItemText:{
    fontSize:'0.7em',
  }
});

export default withStyles(styles)(DropdownMenu);
