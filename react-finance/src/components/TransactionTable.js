import React, { Component } from 'react'
import { object, func } from 'prop-types'
import { withStyles } from '@material-ui/core/styles'
import { connect } from 'react-redux'
import axios from 'axios'
import LoadingData from './LoadingData'
import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import Paper from '@material-ui/core/Paper'
import Button from '@material-ui/core/Button'
import DialogDeleteConfirm from './DialogDeleteConfirm'
import delete_logo from '../images/delete-24px.svg'
import edit_logo from '../images/edit-24px.svg'
import { showNotification } from '../store/notification/actionCreator'
//import { FilterDrawer, filterSelectors, filterActions } from 'material-ui-filter'

export class TransactionTable extends Component {
  constructor (props) {
      super(props)
      this.state = {
        rows:[],
        clickOpen: false,
        toggleView: 'none',
        guidToDelete: null,
        loading: false,
      }
  }

  handleClickOpen = (guid) => {
    this.setState({
      clickOpen: true,
      guidToDelete: guid,
    });
  }

  handleClose = (value) => {
    this.setState({ selectedValue: value, clickOpen: false });
  }

  fromEpochDate(utcSeconds) {
      var transactionDate = new Date(0);
      transactionDate.setUTCSeconds(utcSeconds);
      return transactionDate.toLocaleDateString("en-US");
  }

  componentWillUnmount() {
  }

  componentWillReceiveProps(nextProps) {
  }

  componentDidUpdate() {
    if( this.props.notificationIsShown === false ) {
      this.setState({ toggleView:'spin', });
      this.props.showNotification(true, this.props.accountNameOwner);
      axios.get("http://localhost:8080/get_by_account_name_owner/" + this.props.accountNameOwner)
      .then(result => {
          this.setState({ rows:result.data, });
          this.setState({ toggleView:'none', });
      })
      .catch(error => {
        console.log(error)
      })
    }
  }

  componentDidMount () {
  }

  render() {
    const classes = this.props

    return(
    <div className={classes.TransactionTable}>
  
  <LoadingData 
      className=""
      type={this.state.toggleView} 
      open={this.state.toggleView}
      onClose={this.state.toggleView} />

     <Paper className={''}>
      <Table className={this.props.classes.view} id="blah">
          <TableHead className={''}>
          <TableRow>
            <CustomTableCell>action</CustomTableCell>
            <CustomTableCell>date</CustomTableCell>
            <CustomTableCell>description</CustomTableCell>
            <CustomTableCell>category</CustomTableCell>
            <CustomTableCell>amount</CustomTableCell>
            <CustomTableCell>cleared</CustomTableCell>
            <CustomTableCell>notes</CustomTableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {this.state.rows.map(row => {
              return (
          <TableRow className={classes.row} key={row.guid} id={row.guid} hover={true}>
              <TableCell>
                <div>
                  <Button className={classes.button} onClick={() => this.handleClickOpen(row.guid)}><img src={delete_logo} className="" alt="delete_logo" /></Button> 
                  <Button className={classes.button} ><img src={edit_logo} className="" alt="edit_logo" /></Button> 
                </div>
              </TableCell>
              <TableCell>{this.fromEpochDate(row.transactionDate)}</TableCell>
              <TableCell>{row.description}</TableCell>
              <TableCell>{row.category}</TableCell>
              <TableCell currency="true">{row.amount}</TableCell>
              <TableCell numeric>{row.cleared}</TableCell>
              <TableCell>{row.notes}</TableCell>
          </TableRow>
           )
          })}
        </TableBody>
      </Table>
    </Paper>
  
    <DialogDeleteConfirm
      guid={this.state.guidToDelete}
      selectedValue={this.state.selectedValue}
      open={this.state.clickOpen}
      onClose={this.handleClose}
    />
  </div>

    )}
}

TransactionTable.propTypes = {
  classes: object,
  menuAction: func,
}

TransactionTable.defaultProps = {
  classes: {},
}


const CustomTableCell = withStyles(theme => ({
  head: {
    backgroundColor: theme.palette.common.black,
    color: theme.palette.common.white,
    //display: 'inline',
    //display: 'none',
    display: '',
  },
  body: {
    fontSize: 10,
  },
}))(TableCell);

const styles = (theme) => ({
  root: {
    width: '100%',
    marginTop: theme.spacing.unit * 3,
    overflowX: 'auto',
  },
  table: {
    minWidth: 700,
    fontSize: 'x-small',
    //display: 'none',
  },
  row: {
    '&:nth-of-type(odd)': {
      backgroundColor: theme.palette.background.default,
    },
  },
  button: {
    display: 'none',
  },
  view: {
    //display: 'none',
    display: '',
  },
  head: {
    backgroundColor: theme.palette.common.black,
    color: theme.palette.common.white,
    //color: 'white',
    //display: 'inline',
    //display: 'none',
    //display: '',
  },
  body: {
    fontSize: 10,
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

const mapDispatchToProps = {
  showNotification,
}

export default withStyles(styles) (connect(mapStateToProps, mapDispatchToProps) (TransactionTable));
