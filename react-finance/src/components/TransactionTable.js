import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

const styles = theme => ({
  root: {
    width: '100%',
    marginTop: theme.spacing.unit * 3,
    overflowX: 'auto',
  },
  table: {
    minWidth: 700,
  },
});

let id = 0;
function createData(date, description, category, amount, cleared, notes) {
  id += 1;
  return { id, date, description, category, amount, cleared, notes };
}

const rows = [
  createData('11/3/2018', 'McDonalds', 'restaurant', 19.72, 1, 'notes'),
  createData('11/4/2018', 'Menards', '', 43.23, 1, 'notes'),
];

function SimpleTable(props) {
  const { classes } = props;

  return (
    <Paper className={classes.root}>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell date>date</TableCell>
            <TableCell>description</TableCell>
            <TableCell>category</TableCell>
            <TableCell numeric>amount</TableCell>
            <TableCell currency>cleared</TableCell>
            <TableCell>notes</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map(row => {
            return (
              <TableRow key={row.id}>
                <TableCell date>{row.date}</TableCell>
                <TableCell numeric>{row.description}</TableCell>
                <TableCell numeric>{row.category}</TableCell>
                <TableCell numeric>{row.amount}</TableCell>
                <TableCell currency>{row.cleared}</TableCell>
                <TableCell currency>{row.notes}</TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </Paper>
  );
}

SimpleTable.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SimpleTable);
