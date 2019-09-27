import React, {Component} from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Select from 'react-select';
import add_logo from '../images/add.svg';
import uuid from 'uuidv4';
import axios from 'axios';

const dateFormat = require('dateformat');

export default class DialogFormAccountAdd extends Component {
  state = {
    open: false,
  };

  handleClickOpen = () => {
    this.setState({open: true});
  };

  handleClose = value => {
    this.setState({open: false});
  };

  validateText = () => {};

  handleCloseAdd = value => {
    let obj = {};
    let accountNameOwner = document.getElementById('accountNameOwner');
    let accountType = document.getElementById('accountType');
    let moniker = document.getElementById('moniker');
    let activeStatus = document.getElementById('activeStatus');

    obj['accountNameOwner'] = accountNameOwner.value;
    obj['accountType'] = accountType.value;
    obj['moniker'] = moniker.value;
    obj['activeStatus'] = activeStatus.value;

    let payload = JSON.stringify(obj);
    let endpoint = 'http://localhost:8080/insert_account';
    console.log(payload);
    alert(payload);

    axios
      .post(endpoint, payload, {
        timeout: 0,
        headers: {
          'Content-Type': 'application/json',
        },
      })
      .then(response => {
        console.log(response);
        alert(JSON.stringify(response));
        this.props.handler();
      })
      .catch(error => {
        console.log(error);
        alert(error);
      });

    this.setState({open: false});
  };

  handleAccountNameOwnerChange = selectedAccountNameOwner => {
    if (selectedAccountNameOwner.value !== '') {
    }
  };

  handleClearedChange = selectedCleared => {
    if (selectedCleared.value !== '') {
    }
  };

  componentDidMount = () => {};

  render() {
    return (
      <div>
        <Button>
          <img
            onClick={this.handleClickOpen}
            src={add_logo}
            className=""
            alt="add_logo"
          />
        </Button>

        <Dialog
          open={this.state.open}
          onClose={this.handleClose}
          aria-labelledby="form-dialog-title">
          <DialogTitle id="form-dialog-title">Add Account</DialogTitle>
          <DialogContent>
            <DialogContentText>Add account details.</DialogContentText>
            <div>
              <TextField
                autoFocus
                label="accountNameOwner"
                required
                id="accountNameOwner"
                type="text"
                placeholder="account accountNameOwner..."
                defaultValue=""
                onKeyDown={this.validateText}
                margin="dense"
                fullWidth
              />
              <TextField
                autoFocus
                label="accountType"
                required
                id="accountType"
                type="text"
                placeholder="account accountType..."
                defaultValue="credit"
                onKeyDown={this.validateText}
                margin="dense"
                fullWidth
              />
              <TextField
                autoFocus
                label="activeStatus"
                required
                id="activeStatus"
                type="text"
                placeholder="account activeStatus..."
                defaultValue="true"
                onKeyDown={this.validateText}
                margin="dense"
                fullWidth
              />
              <TextField
                autoFocus
                label="moniker"
                required
                id="moniker"
                type="text"
                placeholder="account moniker..."
                defaultValue="0000"
                onKeyDown={this.validateText}
                margin="dense"
                fullWidth
              />
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
    );
  }
}
