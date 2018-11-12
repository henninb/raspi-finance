import React, { Component } from 'react'
import TextField from '@material-ui/core/TextField'
import axios from 'axios'
import CurrencyInput from 'react-currency-input'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import DropdownMenu from './DropdownMenu'
import SimpleSelect from './SimpleSelect'
import AppHeader from './AppHeader'
import { withStyles } from '@material-ui/core/styles'
import { ValidatorForm, TextValidator} from 'react-material-ui-form-validator'
import './TransactionPage.css'

const uuid = require('uuidv4');
const dateFormat = require('dateformat');

class TransactionAdd extends Component {
  constructor(props) {
    super(props);
    this.state = {
      accounts: [],
      options: [],
      accountNameOwners: [],
    };
    this.getData();
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

  getData = () => {
    axios.get("http://localhost:8080/select_accounts")
      .then((response) => {
        this.setState({accountNameOwners: response.data}, () => {

        var joined = [];
        this.state.accountNameOwners.forEach(element => {
          joined = joined.concat({ value: element.accountNameOwner, label:  element.accountNameOwner });
        });
        this.setState({ options: joined });
        });
     })
     .catch(function (error) {
         console.log(error);
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

    //var d = new Date(0); // The 0 there is the key, which sets the date to the epoch
    //alert(d.setUTCSeconds(utcSeconds));
    //obj['transactionDate'] = ((date_val.getTime() - date_val.getMilliseconds())/1000);
    //obj['transactionDate'] = date_val.valueOf();
    //TODO: fix the date issue
    obj['transactionDate'] = date_val.getTime()/1000|0;
    obj['amount'] = obj['amount'].replace("$", "");

    var payload = JSON.stringify(obj);

    console.log(payload);
    alert(payload);

    var endpoint = "http://localhost:8080/insert";

    //var headers = {
    //headers: {
    //  'Content-Type': 'application/json',
    //  'Accept': 'application/json',
    //},
    //}

    //return axios.post(endpoint, json_indata, {
    //    headers: {
    //        'Content-Type': 'application/json',
    //    }
    //})
  //axios.defaults.headers.common['Content-Type'] = 'application/json'
  //axios.defaults.headers.post['Content-Type'] = 'application/json';
    //let headers = {
    //  'Content-Type': 'application/json;charset=utf-8',
    //  'Accept': 'application/json;charset=utf-8',
    //}
          //'Content-Type': 'application/json',


    //let headers = {
    //  headers: {
    //      'Content-Type': 'application/json;charset=UTF-8',
    //      //"Access-Control-Allow-Origin": "*",
    //  }
    //};

    //return axios.post(endpoint, payload, headers)
    //.then((res) => {
    //  console.log("RESPONSE RECEIVED: ", res);
    //})
    //.catch((err) => {
    //  console.log("AXIOS ERROR: ", err);
    //})

    //alert(dateFormat(new Date(), 'yyyy-mm-dd'));

    ////sends a POST, no header changes
    //let headers = {
    //  'Accept': 'application/json',
    //  'Content-Type': 'application/json;charset=UTF-8',
    //  "Access-Control-Allow-Origin": "*",
    //}
    //return axios.post(endpoint, payload, headers)
    //$("#myform").attr("method", "post");
    //form1.attr("method", "post");
    //this.submitHandler(payload);
    let request = new XMLHttpRequest();
    request.open('POST', endpoint, true);
    request.setRequestHeader("Content-Type", "application/json");
    //request.setRequestHeader("Access-Control-Allow-Origin", "*");
    //request.setRequestHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
    //request.setRequestHeader("Access-Control-Allow-Headers", "accept, content-type");
    request.send(payload);
    //this.transitionTo(endpoint);
    //window.location.href = 'http://localhost:3000/add';
    //return <Redirect to='/add' />
    ////send the OPTIONS message
    //return axios({
    //  method: 'POST',
    //  url: endpoint,
    //  mode: 'no-cors',
    //  headers: {
    //    'Content-Type': 'application/json',
    //  },
    //  data: payload,
    //}).then(response => {
    //   alert(response.data);
    //   return response.data;
    //})

    //return axios({
    //  method:'POST',
    //  url: endpoint,
    //  data: payload,
    //})
    //
    //.then((response) => {
    //  this.setState({ output: response.data});
    //  alert(this.state.output);
    //}
    //);
  }

  createSelectItems(elements1) {
    let items = [];

    elements1.forEach(item => {
      items.push(<option key={item} value={item}>{item}</option>);
    });

    return items;
  }

  onChange(e) {
      this.setState({
      });
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
    const { classes } = this.props;

    return (
<div>
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

      <input required type="search" id="accountNameOwner" key="accountNameOwner" list="accounts" placeholder=" pick an account name owner..." />
      
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
      <TextField required id="description" label="Description Required" type="text" placeholder="transaction description..." autoComplete="on" defaultValue="" />

      <label>Category</label>
      {/* <TextField id="category" key="category" type="text" placeholder="transaction category..." defaultValue="" /> */}
      <TextField id="category" key="category" type="text" placeholder="transaction category..." defaultValue="" />

      <label>Amount</label>
      {/* <TextField id="amount" key="amount" type="number" step="0.01" placeholder="dollar amount..." /> */}
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
      <TextField id="notes" type="text" key="notes" placeholder="transaction notes..." defaultValue="" />
      <button id="submit">Submit</button>
  </form>
</div>
    );
  }
}

TransactionAdd.propTypes = {
  classes: PropTypes.object.isRequired,
};

const styles = theme => ({
  root:{
  }
});

export default withStyles(styles)(TransactionAdd);
