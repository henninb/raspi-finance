import React from 'react';
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
    color: blue[500],
  },
};

class DialogDeleteConfirm extends React.Component {
  handleClose = () => {
    this.props.onClose(this.props.selectedValue);
  };

  handleListItemClick = value => {
    if( value === 'true' ) {
      //var table = document.getElementById("blah");

      var row = document.getElementById(this.props.guid);
      row.remove();
      
      fetch('http://localhost:8080/delete/' + this.props.guid)
      .then(function(response) {
        return response.json();
      })
      .then(function(myJson) {
        console.log(JSON.stringify(myJson));
      });

      //Object.values(table).forEach(element1 => {
      //  //joined = joined.concat({ value:  element.accountNameOwner, label:  element.accountNameOwner });
      //  //var json_data = JSON.stringify(element);
      //  //alert(element1.innerHTML);
      //});
      
      //Array.from(table.rows).forEach(element1 => {
      //  //alert(element1);
      //});

    }
    this.props.onClose(value);
  };

  render() {
    const { classes, onClose, selectedValue, ...other } = this.props;

    return (
      <Dialog onClose={this.handleClose} aria-labelledby="simple-dialog-title" {...other}>
        <DialogTitle id="title">Delete Record - {this.props.guid}</DialogTitle>
        <div>
          <List>

            <ListItem button onClick={() => this.handleListItemClick('true')}>
              <ListItemText primary="Yes" />
            </ListItem>

            <ListItem button onClick={() => this.handleListItemClick('false')}>
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
