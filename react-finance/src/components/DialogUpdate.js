import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { withStyles } from '@material-ui/core/styles'
import CurrencyInput from 'react-currency-input'
import DialogTitle from '@material-ui/core/DialogTitle'
import Dialog from '@material-ui/core/Dialog'
import blue from '@material-ui/core/colors/blue'
import TextField from '@material-ui/core/TextField'
import NumberFormat from 'react-number-format';
import axios from 'axios'

const dateFormat = require('dateformat');

const styles = {
}

class DialogUpdate extends Component {
  constructor(props) {
    super(props);
    this.state = {
      accounts: [],
      transaction: {},
      loaded: false,
    };
  }

  handleClose = () => {
    this.props.onClose(this.props.selectedValue);
  }

  componentDidUpdate() {
    if ( this.props.guid !== null ) {
      axios.get('http://localhost:8080/select/' + this.props.guid)
      .then(result => {
        this.setState({
          transaction: result.data,
        })
        let transactionDate = document.getElementById("transactionDate")
        let accountNameOwner = document.getElementById("accountNameOwner")
        let accountType = document.getElementById("accountType")
        let description = document.getElementById("description")
        let category = document.getElementById("category")
        let amount = document.getElementById("amount")
        let cleared = document.getElementById("cleared")
        let notes = document.getElementById("notes")
        transactionDate.defaultValue = dateFormat(new Date(this.state.transaction.transactionDate * 1000), 'yyyy-mm-dd')
        accountNameOwner.defaultValue = this.state.transaction.accountNameOwner
        accountType.defaultValue = this.state.transaction.accountType
        description.defaultValue = this.state.transaction.description
        category.defaultValue = this.state.transaction.category
        amount.defaultValue = this.state.transaction.amount
        cleared.value = this.state.transaction.cleared
        notes.defaultValue = this.state.transaction.notes
      }).catch(error => {
        console.log(error)
      })
    }
  }
  
  componentDidMount = () => {
    // var localThis = this
    axios.get('http://localhost:8080/select_accounts')
    .then(result => {
      this.setState({
        accounts: result.data,
      })
    }).catch(error => {
      console.log(error)
    })
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

    let endpoint = 'http://localhost:8080/update/' + obj['guid']
    let request = new XMLHttpRequest();
    request.open('PATCH', endpoint, true);
    request.setRequestHeader("Content-Type", "application/json");
    //request.setRequestHeader("Access-Control-Allow-Origin", "*");
    //request.setRequestHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
    //request.setRequestHeader("Access-Control-Allow-Headers", "accept, content-type");
    request.send(payload);
  }

  render() {
    const { classes, onClose, selectedValue, ...other } = this.props;

  return (
    <Dialog onClose={this.handleClose} {...other}>
    {/* <DialogTitle id="title">Update {this.props.guid}</DialogTitle> */}
    <div>
    <form onSubmit={this.submitit} name="myform" id="myform">
      <label>guid</label>
      <TextField required id="guid" type="text" value={this.props.guid} key="guid" disabled={true} />
    
      <label>Transaction Date</label>
      <TextField id="transactionDate" type="date" key="transactionDate" defaultValue="" />
    
      <label>Account Name Owner</label>
    
      <input required type="search" id="accountNameOwner" key="accountNameOwner" list="accounts" placeholder=" pick an account name owner..." autocomplete="off" onChange={this.handleAccountChange.bind(this)} />
      
      <datalist id="accounts">
        {
          this.state.accounts.map(accounts => {
            return <option key={accounts.accountNameOwner} value={accounts.accountNameOwner} />
          }) 
        }
      </datalist>
    
      {/* <label>Account Type</label> */}
      <label></label>
      <TextField required id="accountType" type="hidden" defaultValue="" key="accountType" disabled={true} />
    
      <label>Description</label>
      <TextField required id="description" type="text" placeholder="transaction description..." onkeydown="" />
    
      <label>Category</label>
      <TextField id="category" key="category" type="text" placeholder="transaction category..." defaultValue="" />
    
      <label>Amount</label>
      {/* <CurrencyInput id="amount" key="amount" prefix="$" precision="2" placeholder="dollar amount..." /> */}
      <TextField type="text" id="amount" key="amount" defaultValue="" autoComplete="on" />
      {/* <NumberFormat type="text" id="amount" allowNegative="true" prefix="$" key="amount" defaultValue="" /> */}
    
      <label>Cleared</label>
      <select id="cleared" key="cleared">
        <option value="0">0</option>
        <option value="1">1</option>
        <option value="-1">-1</option>
        <option value="-2">-2</option>
        <option value="-3">-3</option>
      </select>
    
      <label>Notes</label>
      <TextField id="notes" type="text" key="notes" placeholder="transaction notes..." defaultValue="" autoComplete="on" />
      <button id="submit">Submit</button>
    </form>
    </div>
  </Dialog>
    );
  }
}

DialogUpdate.propTypes = {
  classes: PropTypes.object.isRequired,
  onClose: PropTypes.func,
  selectedValue: PropTypes.string,
};

export default withStyles(styles)(DialogUpdate);
