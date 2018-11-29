import React, { Component } from 'react'
import { withStyles } from '@material-ui/core/styles'

class AppHeader extends Component {

  render() {
    return (
      <div style={Div}>
            <h2 style={Header}>Welcome to App Finance</h2>

      </div>
    )
  }
}

const Header = {
  padding: "10px 20px",
  textAlign: "center",
  color: "black",
  fontSize: "22px"
}

const Div = {
    width: '100%',
    height: '100%',
    backgroundColor: 'grey'
}

const styles = theme => ({
  root: {
    textAlign: 'center',
    width: '100%',
    marginTop: theme.spacing.unit * 3,
    overflowX: 'auto',
    backgroundColor: 'yellow',
  },
  div: {
    textAlign: 'center',
    fontWeight: 'bold',
    fontSize: 18,
    marginTop: 0,
    width: 200,
    backgroundColor: 'yellow',
  }
});

export default withStyles(styles)(AppHeader);
