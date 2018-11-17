import React, { Component } from 'react'
import TextField from '@material-ui/core/TextField'
import axios from 'axios'
import CurrencyInput from 'react-currency-input'
import DropdownMenu from './DropdownMenu'
import SimpleSelect from './SimpleSelect'
import AppHeader from './AppHeader'
import { withStyles } from '@material-ui/core/styles'
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
    let form1 = document.getElementById("myform")
    let elements1 = form1.querySelectorAll("input, select")

    elements1.forEach(item => {
      obj[item.id] = item.value;
    });

    let date_val = new Date(obj['transactionDate']);
    let utc_val = new Date(date_val.getTime() + date_val.getTimezoneOffset() * 60000);
    obj['transactionDate'] = Math.round(utc_val.getTime() / 1000);
    obj['amount'] = obj['amount'].replace("$", "");

    let payload = JSON.stringify(obj);

    console.log(payload);
    alert(payload);

    let endpoint = 'http://localhost:8080/insert'
    let request = new XMLHttpRequest();
    request.open('POST', endpoint, true);
    request.setRequestHeader("Content-Type", "application/json");
    //request.setRequestHeader("Access-Control-Allow-Origin", "*");
    //request.setRequestHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
    //request.setRequestHeader("Access-Control-Allow-Headers", "accept, content-type");
    request.send(payload);
  }
  
  handleAccountChange() {
    let account_name_owner = document.getElementById("accountNameOwner")
    let account_type = document.getElementById("accountType")

    this.state.accounts.map(accounts => {
      if( account_name_owner.value === accounts.accountNameOwner ) {
        account_type.value = accounts.accountType
      }
    })
  }

  post() {
    let endpoint = 'http://localhost:8080/insert'
    let payload = ''
        return axios({
          method: 'POST',
          url: endpoint,
        })
          .then((response) => {
            this.setState({ output: response.data })
          })
  }

  createSelectItems(elements1) {
    let items = [];

    elements1.forEach(item => {
      items.push(<option key={item} value={item}>{item}</option>);
    });

    return items;
  }

  componentDidMount() {
    axios.get('http://localhost:8080/select_accounts')
    .then(result => {
      this.setState({
        accounts: result.data,
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
  {JSON.stringify(this.state)}
    {/*<form onSubmit={this.submitit} name="myform" id="myform" method="post"> */}
    <form onSubmit={this.submitit} name="myform" id="myform">
      <label>guid</label>
      <TextField required id="guid" type="text" value={uuid()} key="guid" disabled={true} />

      <label>Transaction Date</label>
      <TextField id="transactionDate" type="date" key="transactionDate" defaultValue={dateFormat(new Date(), 'yyyy-mm-dd')} />

      <label>Account Name Owner</label>

      <input required type="search" id="accountNameOwner" key="accountNameOwner" list="accounts" placeholder=" pick an account name owner..." autocomplete="off" onChange={this.handleAccountChange.bind(this)} />
      
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
      <TextField required id="accountType" type="text" value="" key="accountType" disabled={true}  />

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

//const mapStateToProps = state => {
//  const { account } = state
//  const { isShown, message } = account
//
//  return {
//    notificationIsShown: isShown,
//    notificationMessage: message,
//  }
//}

//export default compose(withStyles(styles), connect(mapStateToProps, mapDispatchToProps))(TransactionAdd)
export default withStyles(styles) (connect(null, null) (TransactionAdd))
