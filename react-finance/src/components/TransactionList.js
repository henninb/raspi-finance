import React, { Component } from 'react';
import DropdownMenu from './DropdownMenu';
import TransactionTable from './TransactionTable';
import { withStyles } from '@material-ui/core/styles'

const welcome = "Welcome to App Finance";

class TransactionList extends Component {

  render() {
    return (
      <div>
        <DropdownMenu />
          <div>
            {welcome}
          </div>
          <TransactionTable />
      </div>
    );
  }
}

const styles = theme => ({
  root: {
    width: '100%',
    marginTop: theme.spacing.unit * 3,
    overflowX: 'auto',
  },
});

export default withStyles(styles)(TransactionList);