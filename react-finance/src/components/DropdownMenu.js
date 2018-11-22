import React, { Component } from 'react'
import { withStyles } from '@material-ui/core/styles'
import './DropdownMenu.css'

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

  render() {
    return (
      <div>
      <ul>
        <li><a href="/list">Home</a></li>
        <li><a href="/add">Add Transaction</a></li>
        <li><a href="#payments">Payments</a></li>
{/*
        <li className="dropdown">
          <a href="javascript:void(0)" className="dropbtn">Accounts</a>
          <div className="dropdown-content">
            {
              this.state.accounts.map(accounts => { 
              return <a href={this.setAccountUrl(accounts.accountNameOwner)}>{accounts.accountNameOwner}</a>})
            }
          </div>
        </li>
*/}
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
