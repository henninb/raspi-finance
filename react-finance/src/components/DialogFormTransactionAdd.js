import React, { Component } from 'react'
import Button from '@material-ui/core/Button'
import TextField from '@material-ui/core/TextField'
import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogContentText from '@material-ui/core/DialogContentText'
import DialogTitle from '@material-ui/core/DialogTitle'
import Select from 'react-select'
import add_logo from '../images/add.svg'
import uuid from 'uuidv4'
import axios from 'axios'

const dateFormat = require('dateformat');

export default class DialogFormTransactionAdd extends Component {
  state = {
    open: false,
    clearedOptions: [],
    accountNameOwnerOptions: [],
    accountNameOwnerList: []
  };

  handleClickOpen = () => {
    this.setState({ open: true })
  }

  handleClose = (value) => {
    this.setState({ open: false })
  }

  validateText = () => {
  }

  fromEpochDate = (utcSeconds) => {
      var transactionDate = new Date(0);
      transactionDate.setUTCSeconds(utcSeconds);
      return transactionDate.toLocaleDateString("en-US");
  }

  toEochDate = (transactionDate) => {
    let date_val = new Date(transactionDate)
    let utc_val = new Date(date_val.getTime() + date_val.getTimezoneOffset() * 60000)

    return Math.round(utc_val.getTime() / 1000)
  }

  handleCloseAdd = (value) => {
    let obj = {}
    let transactionDate = document.getElementById('transactionDate')
    let description = document.getElementById('description')
    let category = document.getElementById('category')
    let notes = document.getElementById('notes')
    let amount = document.getElementById('amount')
    let cleared = document.getElementById('cleared')
    let accountNameOwner = document.getElementById('accountNameOwner')

    obj['guid'] = uuid()
    obj['transactionDate'] = this.toEochDate(transactionDate.value)
    obj['description'] = description.value
    obj['category'] = category.value
    obj['notes'] = notes.value
    obj['amount'] = amount.value
    obj['cleared'] = cleared.textContent
    obj['accountNameOwner'] = accountNameOwner.textContent

    this.state.accountNameOwnerList.forEach(element1 => {
      if( accountNameOwner.textContent === element1.accountNameOwner ) {
        obj['accountType'] = element1.accountType
      }
    })

    let payload = JSON.stringify(obj)
    let endpoint = 'http://localhost:8080/insert/'
    console.log(payload);
    alert(payload);

    axios.post(endpoint, payload, {
    timeout: 0,
    headers: {
        'Content-Type': 'application/json',
    }
    })
    .then(response => {
      console.log(response)
      alert(JSON.stringify(response))
      this.props.handler()
    })
    .catch(error => {
      console.log(error);
      alert(error);
    })

    this.setState({ open: false })
  }

  handleAccountNameOwnerChange = (selectedAccountNameOwner) => {
    if( selectedAccountNameOwner.value !== '' ) {
    }
  }

  handleClearedChange = (selectedCleared) => {
    if( selectedCleared.value !== '' ) {
    }
  }

  componentDidMount = () => {
    let clearedList = []
    let accountNameOwnerList = []
    let clearedJoinedList = []
    let accountNameOwnerJoinedList = []
    clearedList.push(1)
    clearedList.push(0)

    let endpoint = 'http://localhost:8080/select_accounts'
    let payload = ''

    axios.get(endpoint, payload, {
    timeout: 0,
    headers: {
        'Content-Type': 'application/json',
    }
    })
    .then(response => {
      this.setState({ accountNameOwnerList: response.data, })
      //accountNameOwnerList = response.data
      this.state.accountNameOwnerList.forEach(element1 => {
        //alert(element1.accountNameOwner)
        accountNameOwnerJoinedList = accountNameOwnerJoinedList.concat({ value:  element1.accountNameOwner, label:  element1.accountNameOwner })
        clearedList.forEach(element1 => {
        clearedJoinedList = clearedJoinedList.concat({ value:  element1, label:  element1 })
      })

      this.setState({ accountNameOwnerOptions: accountNameOwnerJoinedList, clearedOptions: clearedJoinedList })
    })
    }).catch(error => {
      console.log(error)
    })
  }

  render() {
    return (
      <div>
        <Button><img onClick={this.handleClickOpen} src={add_logo} className="" alt="add_logo" /></Button>

        <Dialog open={this.state.open} onClose={this.handleClose} aria-labelledby="form-dialog-title">
          <DialogTitle id="form-dialog-title">Add Transaction</DialogTitle>
          <DialogContent>
            <DialogContentText>Add transaction details.</DialogContentText>
          <div>
            <Select label="Account Name Owner" id="accountNameOwner" key="accountNameOwner" defaultValue="" placeholder="account name owner..." onChange={this.handleAccountNameOwnerChange} options={this.state.accountNameOwnerOptions} />
            <TextField autoFocus label="Transaction Date" id="transactionDate" type="date" key="transactionDate" defaultValue={dateFormat(new Date(), 'yyyy-mm-dd')} margin="dense" fullWidth />
            {/* <TextField autoFocus label="Account Type" required id="accountType" type="text" defaultValue="" key="accountType" disabled={false} margin="dense" fullWidth /> */}
            <TextField autoFocus label="Description" required id="description" type="text" placeholder="transaction description..." defaultValue="" onKeyDown={this.validateText} margin="dense" fullWidth />
            <TextField autoFocus label="Category" id="category" key="category" type="text" placeholder="transaction category..." defaultValue="" margin="dense" fullWidth />
            <TextField autoFocus label="Amount" type="text" id="amount" key="amount" defaultValue="" autoComplete="on" margin="dense" />
            <Select autoFocus label="Cleared" placeholder="transaction cleared..." id="cleared" key="cleared" useDefault={true} defaultValue="" margin="dense" onChange={this.handleClearedChange} options={this.state.clearedOptions} />
            <TextField autoFocus label="Notes" id="notes" type="text" key="notes" placeholder="transaction notes..." defaultValue="" autoComplete="on" margin="dense" fullWidth />
          </div>
     </DialogContent>
          <DialogActions>
            <Button onClick={this.handleClose} color="primary">
              Cancel
            </Button>
            <Button onClick={this.handleCloseAdd} color="primary">
              Add
            </Button>
          </DialogActions>
        </Dialog>
      </div>
    )
  }
}
