import React, { Component } from 'react'
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Dialog from '@material-ui/core/Dialog';
//import blue from '@material-ui/core/colors/blue';
import axios from 'axios'

const styles = {
  //avatar: {
  //  backgroundColor: blue[50],
  //  color: blue[400],
  //},
};

class DialogDeleteConfirm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      guid: null,
    };
    this.handleClose.bind(this)
    this.handleListItemClick.bind(this)
  }

  handleClose = () => {
    this.props.onClose(this.props.selectedValue);
  };

  handleListItemClick = (value, guid) => {
    // alert('got here')
    if( value === true ) {
      let transactionTable = document.getElementById('transactionTable')
      let row = document.getElementById(guid)
      if( transactionTable !== undefined && transactionTable !== null) {
      if( row !== undefined && row !== null) {
        //row remove causes problems
        ////////////////////////////////row.remove()
        //row.parentNode.removeChild(guid)
        //alert('got here')
        //transactionTable.removeChild(guid)
      }
//var element = document.getElementById(elementId);
//element.parentNode.removeChild(guid);

      axios.delete('http://localhost:8080/delete/' + guid)
      .then(function(response){
        console.log(response);
        //alert(response);
      })
      .catch(function(error){
        console.log(error);
        alert(error);
      });
      } else {
        alert(guid + 'is returning null from the table.');
      }
    }
    if( this.props !== null ) {
      this.props.onClose(value)
    }
    //return false
    //this.props.onClose(value)
  };

  render() {
    const { classes, onClose, selectedValue, ...other } = this.props;

    return (
      <Dialog onClose={this.handleClose} {...other}>
        <DialogTitle id="title">Ok to delete {this.props.guid}</DialogTitle>
        <div>
{JSON.stringify(this.props)}
          <List>
            <ListItem button onClick={() => this.handleListItemClick(true, this.props.guid)}>
              <ListItemText primary="Yes" />
            </ListItem>

            <ListItem button onClick={() => this.handleListItemClick(false, this.props.guid)}>
              <ListItemText primary="Cancel" />
            </ListItem>
          </List>
        </div>
      </Dialog>
    );
  }
}

DialogDeleteConfirm.propTypes = {
  classes: PropTypes.object.isRequired,
  onClose: PropTypes.func,
  selectedValue: PropTypes.string,
};

export default withStyles(styles)(DialogDeleteConfirm);
