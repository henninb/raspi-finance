import React, { Component } from 'react'
//import { object, func } from 'prop-types'
import { withStyles } from '@material-ui/core/styles'
import { connect } from 'react-redux'
import axios from 'axios'
import LoadingData from './LoadingData'
import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
//import Paper from '@material-ui/core/Paper'
import Button from '@material-ui/core/Button'
import DialogDeleteConfirm from './DialogDeleteConfirm'
import DialogUpdate from './DialogUpdate'
import delete_logo from '../images/delete-24px.svg'
import edit_logo from '../images/edit-24px.svg'
import { showNotification } from '../store/notification/actionCreator'
import MUIDataTable from 'mui-datatables'
//import { FilterDrawer, filterSelectors, filterActions } from 'material-ui-filter'

export class TransactionTable extends Component {
  constructor (props) {
      super(props)
      this.state = {
        rows:[],
        clickOpenDelete: false,
        columns: [ 'action', 'date', 'description', 'category', 'amount', 'cleared', 'notes' ],
        toggleView: 'none',
        guidToDelete: null,
        guidToUpdate: null,
        loading: false,
      }
  }

  handleClickDelete = (guid) => {
    this.setState({
      clickOpenDelete: true,
      guidToDelete: guid,
    });
  }

  handleClickUpdate = (guid) => {
    this.setState({
      clickOpenUpdate: true,
      guidToUpdate: guid,
    });
  }

  handleCloseUpdate = (value) => {
    this.setState({ selectedUpdateValue: value, clickOpenUpdate: false });
  }

  handleCloseDelete = (value) => {
    this.setState({ selectedDeleteValue: value, clickOpenDelete: false });
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
  
  viewTable() {
    //if( this.props.notificationIsShown === false ) {
      if( this.state.toggleView === 'spin' ) {
        return this.props.hideTable
      } else {
        return this.props.viewTable
      }
    //}
  }

  componentDidUpdate() {
    if( this.props.notificationIsShown === false ) {
      this.setState({ toggleView:'spin', });
      this.props.showNotification(true, this.props.accountNameOwner);
      axios.get('http://localhost:8080/get_by_account_name_owner/' + this.props.accountNameOwner)
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
    //const classes = this.props

    return(
    <div className="">
    

  <LoadingData 
      className=""
      type={this.state.toggleView} 
      open={this.state.toggleView}
      onClose={this.state.toggleView} />
      {/* <div className={this.props.notificationIsShown === true ? this.props.showTable: this.props.hideTable}> */}
<div className={this.props.hideTable}>
      <Table id="mytable">
          <TableHead>
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
          <TableRow className={this.props.classes.row} key={row.guid} id={row.guid} hover={true}>
              <TableCell>
                <div>
                  <Button className={this.props.classes.button} onClick={() => this.handleClickDelete(row.guid)}><img src={delete_logo} className="" alt="delete_logo" /></Button> 
                  <Button className={this.props.classes.button} onClick={() => this.handleClickUpdate(row.guid)}><img src={edit_logo} className="" alt="edit_logo" /></Button> 
                </div>
              </TableCell>
              <TableCell>{this.fromEpochDate(row.transactionDate)}</TableCell>
              <TableCell>{row.description}</TableCell>
              <TableCell>{row.category}</TableCell>
              <TableCell className={this.props.classes.currency}>{row.amount}</TableCell>
              <TableCell numeric>{row.cleared}</TableCell>
              <TableCell>{row.notes}</TableCell>
          </TableRow>
           )
          })}
        </TableBody>
      </Table>

    <DialogUpdate
      guid={this.state.guidToUpdate}
      selectedValue={this.state.selectedUpdateValue}
      open={this.state.clickOpenUpdate}
      onClose={this.handleCloseUpdate}
    />

    <DialogDeleteConfirm
      guid={this.state.guidToDelete}
      selectedValue={this.state.selectedDeleteValue}
      open={this.state.clickOpenDelete}
      onClose={this.handleCloseDelete}
    />
</div>
  </div>

    )}
}

const CustomTableCell = withStyles(theme => ({
  head: {
    backgroundColor: theme.palette.common.black,
    color: theme.palette.common.white,
    display: '',
    fontSize: 20,
  },
}))(TableCell);

const styles = (theme) => ({
  //root: {
  //  width: '100%',
  //  marginTop: theme.spacing.unit * 3,
  //  overflowX: 'auto',
  //},
  //table: {
  //  minWidth: 700,
  //  fontSize: 'x-small',
  //  //display: 'none',
  //},
  row: {
    //not working
    '&:nth-of-type(odd)': {
      //backgroundColor: theme.palette.background.default,
      backgroundColor: theme.palette.common.red,
    },
  },
  button: {
    //in use
    display: '',
    padding: 0,
    border: 'none',
    background: 'none',
    borderCollapse: 'collapse',
    backgroundColor: 'white', 
  },
  showTable: {
    //in use
    display: '',
  },
  hideTable: {
    //in use
    display: 'none',
  },
  head: {
    //not working
    backgroundColor: theme.palette.common.black,
    color: theme.palette.common.white,
    fontSize: 20,
    //color: 'white',
    //display: 'inline',
    //display: 'none',
    //display: '',
  },
  currency: {
    '&:after': {
      content: '.00', 
    },
    //not working
    //'&:hover:not($disabled):not($error):not($focused):before': {
    '&:before': {
      content:'$',
      textAlign: 'right',
      //display: 'none',
    },
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
