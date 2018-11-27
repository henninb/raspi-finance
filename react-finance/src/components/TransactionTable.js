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
import DialogFormUpdate from './DialogFormUpdate'
import delete_logo from '../images/delete-24px.svg'
import edit_logo from '../images/edit-24px.svg'
import { setAccount, setTransaction, setTransactionLoadStatus } from '../store/account/actionCreator'
//import MUIDataTable from 'mui-datatables'
import DialogFormAdd from './DialogFormAdd'
//import { FilterDrawer, filterSelectors, filterActions } from 'material-ui-filter'
//https://stackoverflow.com/questions/35537229/how-to-update-parents-state-in-react

export class TransactionTable extends Component {
  constructor (props) {
      super(props)
      this.state = {
        rows:[],
        totals_cleared: 0.00,
        clickOpenDelete: false,
        columns: [ 'action', 'date', 'description', 'category', 'amount', 'cleared', 'notes' ],
        guidToDelete: null,
        guidToUpdate: null,
        accountNameOwnerList: null,
      }
    this.handleClickDelete.bind(this)
    this.handleClickUpdate.bind(this)
    this.handler = this.handler.bind(this)
  }

  handleClickDelete = (guid) => {
    this.setState({
      clickOpenDelete: true,
      guidToDelete: guid,
    });
  }

  handleClickUpdate = (guid) => {
    this.setState({  clickOpenUpdate: true, guidToUpdate: guid, })
  }

  handler = (e) => {
    //e.preventDefault()
    if( this.props.accountNameOwner !== '' && this.props.accountNameOwner !== null && this.props.accountNameOwner.length != 0 ) {
      this.props.setAccount(false, this.props.accountNameOwner)
    }
  }

  handleCloseUpdate = (value) => {
    this.setState({ selectedUpdateValue: value, clickOpenUpdate: false, })
  }

  handleCloseDelete = (value) => {
    this.setState({ selectedDeleteValue: value, clickOpenDelete: false, })
  }

  fromEpochDate(utcSeconds) {
      var transactionDate = new Date(0);
      transactionDate.setUTCSeconds(utcSeconds);
      return transactionDate.toLocaleDateString("en-US");
  }

  componentWillUnmount() {
  }

  componentWillReceiveProps = (nextProps, nextState) => {
  }

  componentDidUpdate( prevProps, prevState ) {
    if( this.props.notificationIsShown === false ) {
      this.props.setTransactionLoadStatus('spin')
      this.props.setAccount(true, this.props.accountNameOwner)

      let endpoint = 'http://localhost:8080/get_by_account_name_owner/' + this.props.accountNameOwner
      let payload = ''

      axios.get(endpoint, payload, {
      timeout: 0,
      headers: {
          'Content-Type': 'application/json',
      }
      })
      .then(response => {

        this.props.setTransaction('none', response.data)
        this.setState({ rows: response.data, })
      })
      .catch(error => {
        console.log(error);
        alert(error);
      })


      endpoint = 'http://localhost:8080/get_totals_cleared/' + this.props.accountNameOwner
      payload = ''

      axios.get(endpoint, payload, {
      timeout: 0,
      headers: {
          'Content-Type': 'application/json',
      }
      })
      .then(response => {
		  alert(response)
        this.setState({ totals_cleared: response.data, })
      })
      .catch(error => {
        console.log(error);
        alert(error);
      })


    }
  }

  componentDidMount () {
    //alert('accountNameOwner - componentDidMount=' + this.props.accountNameOwner)
    this.props.setAccount(true, []);
    this.props.setTransaction('none', [])

    let endpoint = 'http://localhost:8080/select_accounts'
    let payload = ''

    axios.get(endpoint, payload, {
    timeout: 0,
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

    return(
    <div>
    {/* JSON.stringify(this.state) */}
    {/* JSON.stringify(this.props.transactionList) */}
    {/* Array.from(this.props.transactionList).map(row => {})*/}
    {/* <LoadingData className=""  type={this.state.toggleView} */}

    <div className={this.props.classes.column}>
    <DialogFormAdd handler={this.handler} accountNameOwnerList={this.state.accountNameOwnerList} />
    </div>
    <div className={this.props.classes.column}>{this.state.totals_cleared}</div>
    <LoadingData className="" type={this.props.loadingStatus} />

    {/* <div className={this.props.notificationIsShown === false ? this.props.classes.showTable: this.props.classes.hideTable}> */}
    <div className={this.props.classes.showTable}>
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
                  {/*<Button className={this.props.classes.button} onClick={() => this.handleClickUpdate(row.guid)}><img src={edit_logo} className="" alt="edit_logo" /></Button> */}
                <DialogFormUpdate handler={this.handler} transaction={row} accountNameOwnerList={this.state.accountNameOwnerList} />
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

    <DialogDeleteConfirm
      guid={this.state.guidToDelete}
      selectedValue={this.state.selectedDeleteValue}
      open={this.state.clickOpenDelete}
      onClose={this.handleCloseDelete}
      handler={this.handler}
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
  column: {
    float: 'left',
    padding: '5px',
	bottom: 0,
  },
});

const mapStateToProps = state => {
  const { account } = state
  const { isShown, accountNameOwners, transactions, viewStatus, updatedTransaction } = account

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
