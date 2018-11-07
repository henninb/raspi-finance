import React, { Component } from 'react';
import DropdownMenu from './DropdownMenu';
import TransactionTable from './TransactionTable';
import SimpleSelect from './SimpleSelect';
import AppHeader from './AppHeader';
import { withStyles } from '@material-ui/core/styles'

class TransactionList extends Component {

  render() {
    const { accountNameOwner } = this.props.match.params
    return (
      <div>
        <DropdownMenu />
        <AppHeader title="Finance App" />
        <SimpleSelect accountNameOwner={accountNameOwner} />
        <TransactionTable accountNameOwner={accountNameOwner} />
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
