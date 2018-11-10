import React from 'react'
import { object, func } from 'prop-types'
import { withStyles } from '@material-ui/core/styles'
import axios from 'axios'
import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import Paper from '@material-ui/core/Paper'
import Button from '@material-ui/core/Button'
import DialogDeleteConfirm from './DialogDeleteConfirm'
import delete_logo from '../images/delete-24px.svg'

export class TransactionTable extends React.Component {
    constructor (props) {
        super(props)
        this.state = {
          rows:[],
          open: false,
          toggleView: false
          //refresh: Math.random(),
        }
    }

    handleClickOpen = () => {
      this.setState({
        open: true,
      });
    };

    handleClose = value => {
      this.setState({ selectedValue: value, open: false });
    };

    fromEpochDate(utcSeconds) {
        //var dateFormat = require('dateformat');
        //var now = new Date();
        //dateFormat(now, "mm-dd-yyyy");

        var transactionDate = new Date(0);
        transactionDate.setUTCSeconds(utcSeconds);
        return transactionDate.toLocaleDateString("en-US");
    };

    createDeleteUrl(guid) {
        var url = 'http://localhost:8080/delete/' + guid;
        return url;
    };

    componentDidMount () {
        //alert(this.props.accountNameOwner)
        axios.get("http://localhost:8080/get_by_account_name_owner/" + this.props.accountNameOwner).then(result => {
            this.setState({ toggleView:true, });
            this.setState({ rows:result.data, });
            this.setState({ toggleView:false, });
            alert('completed');
        }).catch(error => {
          console.log(error)
        })
    }

      render () {
        //console.log(this.state)
        const classes = this.props
        return(
        <div className={classes.TransactionTable}>
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
                    <Button onClick={this.handleClickOpen}><img src={delete_logo} className="" alt="delete_logo" /></Button>
                      <DialogDeleteConfirm
                        guid={row.guid}
                        selectedValue={this.state.selectedValue}
                        open={this.state.open}
                        onClose={this.handleClose}
                      />
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
