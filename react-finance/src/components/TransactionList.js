import React, { Component } from 'react'
import DropdownMenu from './DropdownMenu'
import SimpleSelect from './SimpleSelect'
import AppHeader from './AppHeader'
import TransactionTable from './TransactionTable'
import ReactLoading from 'react-loading'
import { connect } from 'react-redux'
import { withStyles } from '@material-ui/core/styles'

class TransactionList extends Component {

  render() {
    const { accountNameOwner } = this.props.match.params
    const { notificationMessage } = this.props
    //alert("bruh: " + this.props.notificationMessage)
    //alert("bruh: " + JSON.stringify(this.props))
    return (
      <div>
        <DropdownMenu />
        <AppHeader title="Finance App" />
        {JSON.stringify(this.props)}
        <SimpleSelect accountNameOwner={this.props.notificationMessage} />
        <TransactionTable accountNameOwner={this.props.notificationMessage} />
        {/* <ReactLoading type="bars" color="red" height="20%" width="20%" /> */}
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

const mapStateToProps = state => {
  const { notification } = state
  const { isShown, message } = notification

  return {
    notificationIsShown: isShown,
    notificationMessage: message,
  }
}

export default withStyles(styles) (connect(mapStateToProps) (TransactionList));
