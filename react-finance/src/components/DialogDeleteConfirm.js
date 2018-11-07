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
    color: blue[600],
  },
};

class DialogDeleteConfirm extends React.Component {
  handleClose = () => {
    this.props.onClose(this.props.selectedValue);
  };

  deleteRow(rowid) {
      var row = document.getElementById(rowid);
      row.parentNode.removeChild(row);
  }


 deleteRow1(rowid) {
    var row = document.getElementById(rowid);
    var table = row.parentNode;
    while ( table && table.tagName !== 'blah' )
        table = table.parentNode;
    if ( !table )
        return;
    table.deleteRow(row.rowIndex);
}
  
  
  handleListItemClick = value => {
    if( value === 'true' ) {
        //alert(this.props.guid);
		
		    //var i = r.parentNode.parentNode.rowIndex;
        //document.getElementById("myTable").deleteRow(i);
		
		//this.deleteRow1(this.props.guid)
        //document.getElementById('blah').deleteRow('#' + this.props.guid);
        //alert('deleted');
        fetch('http://localhost:8080/delete/' + this.props.guid);
        //this.forceUpdate();
        this.setState({ refresh: Math.random() });
        //this.state.refresh = Math.random();
        //this.props.key = Math.random();
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
