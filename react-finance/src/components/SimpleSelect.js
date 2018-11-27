import React, { Component } from 'react'
import Select from 'react-select'
import { withStyles } from '@material-ui/core/styles'
import { connect } from 'react-redux'
import { setAccount } from '../store/account/actionCreator'
import axios from 'axios';

class SimpleSelect extends Component {
  constructor(props) {
    super(props);
    this.state = {
      options: [],
      accountNameOwners: [],
    };
  }

  componentWillUnmount() {
  }

  componentDidMount () {

    let endpoint = 'http://localhost:8080/select_accounts'
    let payload = ''

    axios.get(endpoint, payload, {
    headers: {
        'Content-Type': 'application/json',
    }
    })
      .then(response => {
        this.setState({accountNameOwners: response.data}, () => {

        let joined = []
        this.state.accountNameOwners.forEach(element => {
          joined = joined.concat({ value:  element.accountNameOwner, label:  element.accountNameOwner })
        })
        this.setState({ options: joined })
        })
     })
     .catch(error => {
         console.log(error)
     });
  }

  handleChange = (selectedOption) => {
    if( selectedOption.value !== '' ) {
      this.props.setAccount(false, selectedOption.value);
    }
  }

  render() {
    return (
     <div>
      <Select
        placeholder="account name owner..."
        onChange={this.handleChange}
        options={this.state.options}
      />
    </div>
    )
  }
}

const styles = theme => ({
})

const mapDispatchToProps = {
  setAccount,
}

export default withStyles(styles) (connect(null, mapDispatchToProps) (SimpleSelect))
