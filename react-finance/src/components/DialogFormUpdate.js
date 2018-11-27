import React, { Component } from 'react'
import Button from '@material-ui/core/Button'
import TextField from '@material-ui/core/TextField'
import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogContentText from '@material-ui/core/DialogContentText'
import DialogTitle from '@material-ui/core/DialogTitle'
import { withStyles } from '@material-ui/core/styles'
import { connect } from 'react-redux'
import { setUpdatedTransaction } from '../store/account/actionCreator'
import Select from 'react-select'
import edit_logo from '../images/edit-24px.svg'
import axios from 'axios'

const dateFormat = require('dateformat');

class DialogFormUpdate extends Component {
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

  fromEpochDate(utcSeconds) {
      var transactionDate = new Date(0);
      transactionDate.setUTCSeconds(utcSeconds);
      return transactionDate.toLocaleDateString("en-US");
  }

  toEochDate(transactionDate) {
    let date_val = new Date(transactionDate)
    let utc_val = new Date(date_val.getTime() + date_val.getTimezoneOffset() * 60000)

    return Math.round(utc_val.getTime() / 1000)
  }

  handleCloseUpdate = (value) => {
    let obj = {}
    let transactionDate = document.getElementById('transactionDate')
    let description = document.getElementById('description')
    let category = document.getElementById('category')
    let notes = document.getElementById('notes')
    let amount = document.getElementById('amount')
    let cleared = document.getElementById('cleared')
    let accountNameOwner = document.getElementById('accountNameOwner')

    obj['guid'] = this.props.transaction.guid

    if( this.toEochDate(transactionDate.value) !== this.props.transaction.transactionDate ) {
      obj['transactionDate'] = this.toEochDate(transactionDate.value)
    }
    if( description.value !== this.props.transaction.description ) {
      obj['description'] = description.value
    }
    if( category.value !== this.props.transaction.category ) {
      obj['category'] = category.value
    }
    if( notes.value !== this.props.transaction.notes ) {
      obj['notes'] = notes.value
    }
    if( amount.value !== this.props.transaction.amount ) {
      obj['amount'] = amount.value
    }
    if( accountNameOwner.textContent !== "account name owner..." && accountNameOwner.textContent !== this.props.transaction.accountNameOwner ) {
      obj['accountNameOwner'] = accountNameOwner.textContent

      this.props.accountNameOwnerList.forEach(element1 => {
        if( accountNameOwner.textContent === element1.accountNameOwner ) {
          obj['accountType'] = element1.accountType
        }
      })
    }
    if( cleared.textContent !== "transaction cleared..." && cleared.textContent !== this.props.transaction.cleared ) {
      obj['cleared'] = cleared.textContent
    }
    let payload = JSON.stringify(obj)
    let endpoint = 'http://localhost:8080/update/' + this.props.transaction.guid
    console.log(payload);
    alert(payload);

    axios.patch(endpoint, payload, {
    timeout: 0,
    headers: {
        'Content-Type': 'application/json-patch+json',
    }
    })
    .then(response => {
      console.log(response)

      this.props.setUpdatedTransaction(this.props.transaction.guid)
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

  componentDidMount () {
    let clearedList = []
    let accountNameOwnerList = []
    let clearedJoinedList = []
    let accountNameOwnerJoinedList = []
    clearedList.push(1)
    clearedList.push(0)

    //MenuItem vs option
    this.props.accountNameOwnerList.forEach(element1 => {
      accountNameOwnerJoinedList = accountNameOwnerJoinedList.concat({ value:  element1.accountNameOwner, label:  element1.accountNameOwner })
    })

    clearedList.forEach(element1 => {
      clearedJoinedList = clearedJoinedList.concat({ value:  element1, label:  element1 })
    })

    this.setState({ accountNameOwnerOptions: accountNameOwnerJoinedList, clearedOptions: clearedJoinedList })
    let accountNameOwner = document.getElementById('accountNameOwner')
  }

  render() {
    const { accountNameOwnerList, transaction } = this.props

    return (
      <div>
        <Button><img onClick={this.handleClickOpen} src={edit_logo} className="" alt="edit_logo" /></Button>

        <Dialog open={this.state.open} onClose={this.handleClose} aria-labelledby="form-dialog-title">
          <DialogTitle id="form-dialog-title">Update Transaction</DialogTitle>
          <DialogContent>
            <DialogContentText>Update transaction details.</DialogContentText>
           <div>
            <Select label="Account Name Owner" id="accountNameOwner" key="accountNameOwner" defaultValue="" placeholder="account name owner..." onChange={this.handleAccountNameOwnerChange} options={this.state.accountNameOwnerOptions} />
            <TextField autoFocus label="Transaction Date" id="transactionDate" type="date" key="transactionDate" defaultValue={dateFormat(new Date(transaction.transactionDate * 1000), 'yyyy-mm-dd')} margin="dense" fullWidth />
            <TextField autoFocus label="Description" required id="description" type="text" placeholder="transaction description..." defaultValue={transaction.description} onKeyDown={this.validateText} margin="dense" fullWidth />
            <TextField autoFocus label="Category" id="category" key="category" type="text" placeholder="transaction category..." defaultValue={transaction.category} margin="dense" fullWidth />
            <TextField autoFocus label="Amount" type="text" id="amount" key="amount" defaultValue={transaction.amount} autoComplete="on" margin="dense" />
            <Select autoFocus label="Cleared" placeholder="transaction cleared..." id="cleared" key="cleared" useDefault={true} defaultValue={transaction.cleared} margin="dense" onChange={this.handleClearedChange} options={this.state.clearedOptions} />
            <TextField autoFocus label="Notes" id="notes" type="text" key="notes" placeholder="transaction notes..." defaultValue={transaction.notes} autoComplete="on" margin="dense" fullWidth />
          </div>
          </DialogContent>
          <DialogActions>
            <Button onClick={this.handleClose} color="primary">
              Cancel
            </Button>
            <Button onClick={this.handleCloseUpdate} color="primary">
              Update
            </Button>
          </DialogActions>
        </Dialog>
      </div>
    )
  }
}

const styles = {
}

const mapDispatchToProps = {
  setUpdatedTransaction,
}

export default withStyles(styles) (connect(null, mapDispatchToProps) (DialogFormUpdate));
