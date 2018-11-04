import React from 'react'
import { object, string, func } from 'prop-types'
import { withStyles } from '@material-ui/core/styles'
import axios from 'axios'

import PropTypes from 'prop-types';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

export class TransactionTable extends React.Component {
    constructor (props) {
        super(props)
        this.state = {
        rows:[],
        }
    }

    fromEpochDate(utcSeconds) {
        //var dateFormat = require('dateformat');
        //var now = new Date();
        //dateFormat(now, "mm-dd-yyyy");

        var transactionDate = new Date(0);
        transactionDate.setUTCSeconds(utcSeconds);
        return transactionDate.toLocaleDateString("en-US");
    }

    createDeleteUrl(guid) {
        var url = 'http://localhost:8080/delete/' + guid;
        return url;
    }

    componentDidMount () {
        axios.get("http://localhost:8080/get_by_account_name_owner/chase_brian").then(result => {
            this.setState({
                rows:result.data,
            })

        }).catch(error => {
          console.log(error)
        })
    }

      render () {
        console.log(this.state)
        const classes = this.props
        return(
        <div className={classes.TransactionTable}>
         <Paper className={classes.root}>
          <Table className={classes.table}>
            <TableHead>
              <TableRow>
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
                  <TableRow key={row.transactionId}>
                  <TableCell><a href={this.createDeleteUrl(row.guid)}>delete</a></TableCell>
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
});

export default withStyles(styles)(TransactionTable)
