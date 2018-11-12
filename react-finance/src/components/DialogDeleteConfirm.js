import React, { Component } from 'react'
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Dialog from '@material-ui/core/Dialog';
import blue from '@material-ui/core/colors/blue';

const styles = {
  avatar: {
    backgroundColor: blue[100],
    color: blue[400],
  },
};

class DialogDeleteConfirm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      guid: null,
    };
  }

  handleClose = () => {
    this.props.onClose(this.props.selectedValue);
  };

  handleListItemClick = (value, guid) => {
    if( value === true ) {
      var row = document.getElementById(guid);
      if( row !== null ) {
        row.remove();
        
        fetch('http://localhost:8080/delete/' + guid)
        .then(function(response) {
          return response.json();
        })
        .then(function(myJson) {
          console.log(JSON.stringify(myJson));
        });
      } else {
        alert(guid + 'is returning null from the table.');
      }
    }
    this.props.onClose(value);
  };

  render() {
    const { classes, onClose, selectedValue, ...other } = this.props;

    return (
      <Dialog onClose={this.handleClose} aria-labelledby="simple-dialog-title" {...other}>
        <DialogTitle id="title">Ok to delete?</DialogTitle>
        <div>
          <List>
            <ListItem button onClick={() => this.handleListItemClick(true, this.props.guid)}>
              <ListItemText primary="Yes" />
            </ListItem>

            <ListItem button onClick={() => this.handleListItemClick(false, this.props.guid)}>
              <ListItemText primary="No" />
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
