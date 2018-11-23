import React, { Component } from 'react'
import Button from '@material-ui/core/Button'
import TextField from '@material-ui/core/TextField'
import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogContentText from '@material-ui/core/DialogContentText'
import DialogTitle from '@material-ui/core/DialogTitle'
import Select from 'react-select'
import edit_logo from '../images/edit-24px.svg'
import axios from 'axios'

const dateFormat = require('dateformat');

export default class DialogFormUpdate extends Component {
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
    //alert('Cancel Button')
    this.setState({ open: false })
  }

  handleCloseUpdate = (value) => {
    let obj = {}
    let transactionDate = document.getElementById('transactionDate')
    let description = document.getElementById('description')
    let category = document.getElementById('category')
    let notes = document.getElementById('notes')

	obj['guid'] = this.props.transaction.guid
	alert(transactionDate.value + ' - ' + this.props.transaction.transactionDate)
    if( description.value !== this.props.transaction.description ) {
      alert('description changed')
      obj['description'] = description.value
    }
    if( category.value !== this.props.transaction.category ) {
      obj['category'] = category.value
    }
    if( notes.value !== this.props.transaction.notes ) {
      obj['notes'] = notes.value
    }
    let payload = JSON.stringify(obj)
    let endpoint = 'http://localhost:8080/update/' + this.props.transaction.guid
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

    this.setState({ open: false })
  }
  
  handleAccountNameOwnerChange = (selectedAccountNameOwner) => {
    if( selectedAccountNameOwner.value !== '' ) {
      //alert(selectedAccountNameOwner)
      //this.props.setAccount(false, selectedAccountNameOwner.value);
    }
  }

  handleClearedChange = (selectedCleared) => {
    if( selectedCleared.value !== '' ) {
      //alert(selectedCleared)
      //this.props.setAccount(false, selectedAccountNameOwner.value);
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
  }
  
  
  render() {
    const { accountNameOwnerList, transaction } = this.props
	
    return (
      <div>
	  {/* <Button onClick={this.handleClickOpen}>Update</Button> */}
        <Button><img onClick={this.handleClickOpen} src={edit_logo} className="" alt="edit_logo" /></Button>
  
        <Dialog open={this.state.open} onClose={this.handleClose} aria-labelledby="form-dialog-title">
          <DialogTitle id="form-dialog-title">Update Transaction</DialogTitle>
          <DialogContent>
            <DialogContentText>Update transaction details.</DialogContentText>

            <Select label="Account Name Owner" id="accountNameOwner" key="accountNameOwner" defaultValue="" placeholder="account name owner..." onChange={this.handleAccountNameOwnerChange} options={this.state.accountNameOwnerOptions} />
            <TextField autoFocus label="Transaction Date" id="transactionDate" type="date" key="transactionDate" defaultValue={dateFormat(new Date(transaction.transactionDate * 1000), 'yyyy-mm-dd')} margin="dense" fullWidth />
            {/* <TextField autoFocus label="Account Type" required id="accountType" type="text" defaultValue="" key="accountType" disabled={false} margin="dense" fullWidth /> */}
            <TextField autoFocus label="Description" required id="description" type="text" placeholder="transaction description..." defaultValue={transaction.description} onkeydown="" margin="dense" fullWidth />
            <TextField autoFocus label="Category" id="category" key="category" type="text" placeholder="transaction category..." defaultValue={transaction.category} margin="dense" fullWidth />
            <TextField autoFocus label="Amount" type="text" id="amount" key="amount" defaultValue={transaction.amount} autoComplete="on" margin="dense" />
            <Select autoFocus label="Cleared" placeholder="transaction cleared..." id="cleared" key="cleared" useDefault={true} defaultValue={transaction.cleared} margin="dense" onChange={this.handleClearedChange} options={this.state.clearedOptions} />
            <TextField autoFocus label="Notes" id="notes" type="text" key="notes" placeholder="transaction notes..." defaultValue={transaction.notes} autoComplete="on" margin="dense" fullWidth />
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
    );
  }
}
