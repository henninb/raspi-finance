import React, { Component } from 'react'
import TextField from '@material-ui/core/TextField'
import axios from 'axios'
import CurrencyInput from 'react-currency-input'
import DropdownMenu from './DropdownMenu'
import SimpleSelect from './SimpleSelect'
import AppHeader from './AppHeader'
import { withStyles } from '@material-ui/core/styles'
import { showNotification } from '../store/notification/actionCreator'
import uuid from 'uuidv4'
import { connect } from 'react-redux'
import './TransactionPage.css'

const dateFormat = require('dateformat');

class TransactionAdd extends Component {
  constructor(props) {
    super(props);
    this.state = {
      accounts: [],
      options: [],
      accountNameOwners: [],
    };
  }

  submitHandler(payload) {
   axios.post('http://localhost:8080/insert', payload)
     .then(function(response){
       console.log(response);
       alert(response);
   })
     .catch(function(error){
       console.log(error);
       alert(error);
     });
  }

  submitit() {
    var obj = {};
    var form1 = document.getElementById("myform");
    var elements1 = form1.querySelectorAll( "input, select" );

    elements1.forEach(item => {
      obj[item.id] = item.value;
    });

    let date_val = new Date(obj['transactionDate']);
    let utc_val = new Date(date_val.getTime() + date_val.getTimezoneOffset() * 60000);
    obj['transactionDate'] = Math.round(utc_val.getTime() / 1000);
    obj['amount'] = obj['amount'].replace("$", "");

    var payload = JSON.stringify(obj);

    console.log(payload);
    alert(payload);

    var endpoint = "http://localhost:8080/insert";
    let request = new XMLHttpRequest();
    request.open('POST', endpoint, true);
    request.setRequestHeader("Content-Type", "application/json");
    //request.setRequestHeader("Access-Control-Allow-Origin", "*");
    //request.setRequestHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
    //request.setRequestHeader("Access-Control-Allow-Headers", "accept, content-type");
    request.send(payload);
  }

  createSelectItems(elements1) {
    let items = [];

    elements1.forEach(item => {
      items.push(<option key={item} value={item}>{item}</option>);
    });

    return items;
  }

  //onChange(e) {
  //    this.setState({
  //    });
  //}

  componentDidMount() {
    //this.props.showNotification(true, 'blah123')
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
  <div id="" className="" >
  <DropdownMenu />
  <AppHeader title="Finance App" />
  <SimpleSelect />

    {/*<form onSubmit={this.submitit} name="myform" id="myform" method="post"> */}
    <form onSubmit={this.submitit} name="myform" id="myform">
      <label>guid</label>
      <TextField required id="guid" type="text" value={uuid()} key="guid" disabled={true} />

      <label>Transaction Date</label>
      <TextField id="transactionDate" type="date" key="transactionDate" defaultValue={dateFormat(new Date(), 'yyyy-mm-dd')} />

      <label>Account Name Owner</label>

      <input required type="search" id="accountNameOwner" key="accountNameOwner" list="accounts" placeholder=" pick an account name owner..." autocomplete="off" />
      
      <datalist id="accounts">
        {  this.state.accounts.map(accounts => {
            return <option key={accounts.accountNameOwner} value={accounts.accountNameOwner} />
          }) 
        }
      </datalist>

{/*
<Select id="accountNameOwner" key="accountNameOwner" options={this.state.options} onChange={this.handleChange} placeholder="account name owner...">

  this.state.accounts.map(accounts => {
    return  <option key={accounts.accountNameOwner} value={accounts.accountNameOwner}>{accounts.accountNameOwner}</option>
  }
) 
</Select>
*/}

      <label>Account Type</label>
      <select id="accountType" key="accountType" >
        <option value="credit">credit</option>
        <option value="debit">debit</option>
      </select>

      <label>Description</label>
      <TextField required id="description" label="*Required" type="text" placeholder="transaction description..." autoComplete="off" onkeydown="" defaultValue="" />

      <label>Category</label>
      {/* <TextField id="category" key="category" type="text" placeholder="transaction category..." defaultValue="" /> */}
      <TextField id="category" key="category" type="text" placeholder="transaction category..." defaultValue="" autoComplete="off"/>

      <label>Amount</label>
      <CurrencyInput id="amount" key="amount" prefix="$" precision="2" placeholder="dollar amount..." />

      <label>Cleared</label>
      <select id="cleared" key="cleared">
        <option value="0">0</option>
        <option value="1">1</option>
        <option value="-1">-1</option>
        <option value="-2">-2</option>
        <option value="-3">-3</option>
      </select>

      <label>Notes</label>
      <TextField id="notes" type="text" key="notes" placeholder="transaction notes..." defaultValue="" autoComplete="off" />
      <button id="submit">Submit</button>
  </form>
</div>
    );
  }
}

const styles = theme => ({
  root:{
  },
  blah: {
    'hidden' : '',
  },
});

const mapStateToProps = state => {
  const { notification } = state
  const { isShown, message } = notification

  return {
    notificationIsShown: isShown,
    notificationMessage: message,
  }
}

const mapDispatchToProps = {
  showNotification,
}

//export default compose(withStyles(styles), connect(mapStateToProps, mapDispatchToProps))(TransactionAdd)
export default withStyles(styles) (connect(mapStateToProps, mapDispatchToProps) (TransactionAdd))
