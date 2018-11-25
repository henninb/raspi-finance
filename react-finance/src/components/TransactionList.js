import React, { Component } from 'react'
import SimpleSelect from './SimpleSelect'
import AppHeader from './AppHeader'
import TransactionTable from './TransactionTable'
import { connect } from 'react-redux'
import { withStyles } from '@material-ui/core/styles'

class TransactionList extends Component {

  componentWillReceiveProps( nextProps, nextState ) {
  }

  render() {

    return (
      <div>
        <AppHeader title="Finance App" />
        <SimpleSelect accountNameOwner={this.props.accountNameOwnersList} />
        <TransactionTable accountNameOwner={this.props.accountNameOwnersList} />
      </div>
    )
  }
}

const styles = theme => ({
  root: {
    width: '100%',
    marginTop: theme.spacing.unit * 3,
    overflowX: 'auto',
  },
});

const mapStateToProps = state => {
  const { account } = state
  const { accountNameOwners } = account

  return {
    accountNameOwnersList: accountNameOwners,
  }
}

export default withStyles(styles) (connect(mapStateToProps) (TransactionList));
