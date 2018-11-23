import React, { Component } from 'react'
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
import DialogFormUpdate from './DialogFormUpdate'
import delete_logo from '../images/delete-24px.svg'
import edit_logo from '../images/edit-24px.svg'
import { setAccount, setTransaction, setTransactionLoadStatus } from '../store/account/actionCreator'
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
        accountNameOwnerList: null,
        myRows: [],
      }
    this.handleClickDelete.bind(this)
    this.handleClickUpdate.bind(this)
    //this.viewTable.bind(this)
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

  componentWillReceiveProps( nextProps, nextState ) {
  }

  componentDidUpdate( prevProps, prevState ) {
    if( this.props.notificationIsShown === false ) {
      this.props.setTransactionLoadStatus('spin')
      this.props.setAccount(true, this.props.accountNameOwner)
	  
    let endpoint = 'http://localhost:8080/get_by_account_name_owner/' + this.props.accountNameOwner
    let payload = ''

    axios.get(endpoint, payload, {
    headers: {
        'Content-Type': 'application/json',
    }
    })
    .then(response => {

      this.props.setTransaction('none', response.data)

      var newRows = []
      
      response.data.forEach(function (element) {
        var newRow = []
        newRow.push('amount')
        newRow.push(element.transactionDate)
        newRow.push(element.description)
        newRow.push(element.category)
        newRow.push(element.amount)
        newRow.push(element.cleared)
        newRow.push(element.notes)
        newRows.push(newRow)
      })

      this.setState({ myRows: newRows, })
      this.setState({ rows: response.data, })
    })
    .catch(error => {
      console.log(error);
      alert(error);
    })
	  
      //axios.get('http://localhost:8080/get_by_account_name_owner/' + this.props.accountNameOwner)
      //.then(result => {
      //    this.props.setTransaction('none', result.data)
      //
      //    var newRows = []
      //    
      //    result.data.forEach(function (element) {
      //      var newRow = []
      //      newRow.push('amount')
      //      newRow.push(element.transactionDate)
      //      newRow.push(element.description)
      //      newRow.push(element.category)
      //      newRow.push(element.amount)
      //      newRow.push(element.cleared)
      //      newRow.push(element.notes)
      //      newRows.push(newRow)
      //    })
      //
      //    this.setState({ myRows: newRows, })
      //    this.setState({ rows: result.data, })
      //})
      //.catch(error => {
      //  console.log(error)
      //})
    }
  }

  componentDidMount () {
    //alert('accountNameOwner - componentDidMount=' + this.props.accountNameOwner)
    this.props.setAccount(true, []);
    this.props.setTransaction('none', [{"guid":"e85890bb-ff14-4fa3-a23b-db59e323b0c1","sha256":"","accountType":"credit","accountNameOwner":"discover_brian","description":"none","category":"","notes":"","cleared":0,"reoccurring":false,"amount":"0.0","transactionDate":0,"dateUpdated":0,"dateAdded":0}])

    let endpoint = 'http://localhost:8080/select_accounts'
    let payload = ''

    axios.get(endpoint, payload, {
    headers: {
        'Content-Type': 'application/json',
    }
    })
    .then(response => {
      this.setState({ accountNameOwnerList: response.data, })
    }).catch(error => {
      console.log(error)
    })
  }

  render() {
    //const classes = this.props

    let content = <Table title={'List'} data={this.state.myRows} columns={this.state.columns} />

    return(
    <div className="">
    {/* JSON.stringify(this.state) */}
    {/* JSON.stringify(this.props.transactionList) */}
    {/* Array.from(this.props.transactionList).map(row => {})*/}

    {/* <LoadingData className=""  type={this.state.toggleView} */}
    <LoadingData className="" type={this.props.loadingStatus} />

      {/* <div className={this.props.notificationIsShown === true ? this.props.showTable: this.props.hideTable}> */}
    <div className={this.props.hideTable}>
      <Table id="transactionTable" key="transactionTable">
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
                <DialogFormUpdate transaction={row} accountNameOwnerList={this.state.accountNameOwnerList} />
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
  const { account } = state
  const { isShown, accountNameOwners, transactions, viewStatus } = account

  return {
    notificationIsShown: isShown,
    transactionList: transactions,
    loadingStatus: viewStatus,
  }
}

const mapDispatchToProps = {
  setAccount,
  setTransaction,
  setTransactionLoadStatus,
}

export default withStyles(styles) (connect(mapStateToProps, mapDispatchToProps) (TransactionTable));
