import React, { Component } from 'react'
import DropdownMenu from './DropdownMenu'
import SimpleSelect from './SimpleSelect'
import AppHeader from './AppHeader'
import TransactionTable from './TransactionTable'
import { connect } from 'react-redux'
import { withStyles } from '@material-ui/core/styles'

class TransactionList extends Component {

  render() {
    //const { accountNameOwner } = this.props.match.params
    //const { notificationMessage } = this.props

    return (
      <div>
        <DropdownMenu />
        <AppHeader title="Finance App" />
        {/* JSON.stringify(this.props) */}
        <SimpleSelect accountNameOwner={this.props.notificationMessage} />
        <TransactionTable accountNameOwner={this.props.notificationMessage} />
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
  const { isShown, message } = account

  return {
    notificationIsShown: isShown,
    notificationMessage: message,
  }
}

export default withStyles(styles) (connect(mapStateToProps) (TransactionList));
