import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { withStyles } from '@material-ui/core/styles'
import CurrencyInput from 'react-currency-input'
import DialogTitle from '@material-ui/core/DialogTitle'
import Dialog from '@material-ui/core/Dialog'
import blue from '@material-ui/core/colors/blue'
import TextField from '@material-ui/core/TextField'
import NumberFormat from 'react-number-format'
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
      openStatus: false,
    };
    //this.submitit.bind(this)
  }

  handleClose = () => {
    this.props.onClose(this.props.selectedValue);
  }

  componentDidUpdate() {
    if ( this.props.guid !== null ) {
      let endpoint = 'http://localhost:8080/select/' + this.props.guid
      let payload = ''
      
      axios.get(endpoint, payload, {
      headers: {
          'Content-Type': 'application/json',
      }
      })
      .then(response => {
        this.setState({
          transaction: response.data,
        })
        let transactionDate = document.getElementById('transactionDate')
        let accountNameOwner = document.getElementById('accountNameOwner')
        let accountType = document.getElementById('accountType')
        let description = document.getElementById('description')
        let category = document.getElementById('category')
        let amount = document.getElementById('amount')
        let cleared = document.getElementById('cleared')
        let notes = document.getElementById('notes')
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

    let endpoint = 'http://localhost:8080/select_accounts'
    let payload = ''

    axios.get(endpoint, payload, {
    headers: {
        'Content-Type': 'application/json',
    }
    })
    .then(response => {
      this.setState({
        accounts: response.data,
      })
    }).catch(error => {
      console.log(error)
    })
  }
  
  handleAccountChange() {
    let account_name_owner = document.getElementById('accountNameOwner')
    let account_type = document.getElementById('accountType')

    this.state.accounts.map(accounts => {
      if( account_name_owner.value === accounts.accountNameOwner ) {
        account_type.value = accounts.accountType
      }
    })
  }
  
  fromEpochDate(utcSeconds) {
      var transactionDate = new Date(0);
      transactionDate.setUTCSeconds(utcSeconds);
      return transactionDate.toLocaleDateString("en-US");
  }
  
  toEochDate(transactionDate) {
    let date_val = new Date(transactionDate)
    let utc_val = new Date(date_val.getTime() + date_val.getTimezoneOffset() * 60000)
    //alert(transactionDate)

    return Math.round(utc_val.getTime() / 1000)
  }

  submitit(transaction) {
    let obj = {}
    let transactionUpdateForm = document.getElementById('transactionUpdateForm')
    let elements1 = transactionUpdateForm.querySelectorAll("input, select")

	//alert(this.toEochDate(transaction.transactionDate))
    elements1.forEach(item => {
      if( item.id === 'guid' ) { 
        obj[item.id] = item.value;
      }

      if( item.id === 'accountNameOwner' && item.value !== transaction.accountNameOwner ) {
        obj[item.id] = item.value;
      }
      if( item.id === 'accountType' && item.value !== transaction.accountType ) {
        obj[item.id] = item.value;
      }
      if( item.id === 'transactionDate' && this.toEochDate(item.value) !== this.toEochDate(transaction.transactionDate) ) {
        obj[item.id] = this.toEochDate(item.value)
      }
      if( item.id === 'description' && item.value != transaction.description ) {
        obj[item.id] = item.value;
      }
      if( item.id === 'category' && item.value != transaction.category ) {
        obj[item.id] = item.value;
      }
      if( item.id === 'cleared' && item.value != transaction.cleared ) {
        obj[item.id] = item.value;
      }
      if( item.id === 'amount' && item.value != transaction.amount ) {
        obj[item.id] = item.value;
      }
      if( item.id === 'notes' && item.value != transaction.notes ) {
        obj[item.id] = item.value;
      }
    })

    let payload = JSON.stringify(obj);
    let endpoint = 'http://localhost:8080/update/' + obj['guid']
    console.log(payload);
    alert(payload);

    axios.patch(endpoint, payload, {
    headers: {
        'Content-Type': 'application/json-patch+json',
    }
    })
    .then(response => {
      console.log(response)
      alert(JSON.stringify(response))
    })
    .catch(error => {
      console.log(error);
      alert(error);
    })
  }

  render() {
    const { classes, onClose, selectedValue, ...other } = this.props

  return (
    <Dialog open={true} onClose={this.handleClose} {...other}>
    {/* <DialogTitle id="title">Update {this.props.guid}</DialogTitle> */}
    <div>
    <form onSubmit={() => this.submitit(this.state.transaction)} name="transactionUpdateForm" id="transactionUpdateForm">
      <label>guid</label>
      <TextField required id="guid" type="text" value={this.props.guid} key="guid" disabled={true} />
    
      <label>Transaction Date</label>
      <TextField id="transactionDate" type="date" key="transactionDate" defaultValue="" />
    
      <label>Account Name Owner</label>
    
      <input required type="search" id="accountNameOwner" key="accountNameOwner" list="accounts" placeholder=" pick an account name owner..." autoComplete="off" onChange={this.handleAccountChange.bind(this)} />
      
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
