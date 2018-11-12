import React, { Component } from 'react'
import { object, func } from 'prop-types'
import { withStyles } from '@material-ui/core/styles'
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
//import { FilterDrawer, filterSelectors, filterActions } from 'material-ui-filter'

export class TransactionTable extends Component {
    constructor (props) {
        super(props)
        this.state = {
          rows:[],
          clickOpen: false,
          toggleView: 'spin',
          guidToDelete: null,
        }
    }

    handleClickOpen = (guid) => {
      this.setState({
        clickOpen: true,
        guidToDelete: guid,
      });
    };

    handleClose = (value) => {
      this.setState({ selectedValue: value, clickOpen: false });
    };

    fromEpochDate(utcSeconds) {
        //var dateFormat = require('dateformat');
        //var now = new Date();
        //dateFormat(now, "mm-dd-yyyy");

        var transactionDate = new Date(0);
        transactionDate.setUTCSeconds(utcSeconds);
        return transactionDate.toLocaleDateString("en-US");
    };

    createDeleteUrl( guid ) {
        var url = 'http://localhost:8080/delete/' + guid;
        return url;
    };

    componentDidMount () {
        axios.get("http://localhost:8080/get_by_account_name_owner/" + this.props.accountNameOwner).then(result => {
            this.setState({ toggleView:'spin', });
            this.setState({ rows:result.data, });
            this.setState({ toggleView:'none', });
            //alert('completed');
        }).catch(error => {
          console.log(error)
        })
    }

      render() {
        //console.log(this.state)
        const classes = this.props
        return(
        <div className={classes.TransactionTable}>
        <LoadingData type={this.state.toggleView} 
          open={this.state.toggleView}
          onClose={this.state.toggleView} />
         <Paper className={classes.root}>
          <Table className={classes.table} id='blah'>
            <TableHead>
              <TableRow header={true}>
                <TableCell>Action</TableCell>
                <TableCell date>date</TableCell>
                <TableCell>description</TableCell>
                <TableCell>category</TableCell>
                <TableCell currency="true">amount</TableCell>
                <TableCell numeric>cleared</TableCell>
                <TableCell>notes</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {this.state.rows.map(row => {
                  return (
              <TableRow key={row.guid} id={row.guid} hover={true}>
                  <TableCell>
                    <div>

					{/* <a href={() => this.handleClickOpen(row.guid)}> <img border="0" alt="delete" src={delete_logo} /> </a> */}

			   <Button className={classes.button} onClick={() => this.handleClickOpen(row.guid)}><img src={delete_logo} className="" alt="delete_logo" /></Button> 
			   <Button className={classes.button} ><img src={edit_logo} className="" alt="edit_logo" /></Button> 
                    </div>
                  </TableCell>
                  <TableCell date>{this.fromEpochDate(row.transactionDate)}</TableCell>
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

const styles = theme => ({
  root: {
    width: '100%',
    marginTop: theme.spacing.unit * 3,
    overflowX: 'auto',
  },
  table: {
    minWidth: 700,
    fontSize: 'x-small',
  },
  button: {
  //z-index: -1,
  outline: 'none',
  //width:1000,
  //height:1,
  //backgroundColor: '#4CAF50',
  },
});

export default withStyles(styles)(TransactionTable)
