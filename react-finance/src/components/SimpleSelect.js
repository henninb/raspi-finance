import React, { Component } from 'react'
import Select from 'react-select'
import { withStyles } from '@material-ui/core/styles'
import { connect } from 'react-redux'
//import { bindActionCreators } from 'redux'
import { showNotification, showAccounts } from '../store/notification/actionCreator'
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
    axios.get('http://localhost:8080/select_accounts')
      .then((response) => {
        this.setState({accountNameOwners: response.data}, () => {
   
        var joined = []
        this.state.accountNameOwners.forEach(element => {
          joined = joined.concat({ value:  element.accountNameOwner, label:  element.accountNameOwner })
        });
        this.setState({ options: joined })
        });
     })
     .catch(function (error) {
         console.log(error)
     });
  }

  handleChange = (selectedOption) => {
    if( selectedOption.value !== '' ) {
      this.props.showNotification(false, selectedOption.value);
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
    );
  }
}

const styles = theme => ({
});

const mapDispatchToProps = {
  showNotification,
  showAccounts,
}

export default withStyles(styles) (connect(null, mapDispatchToProps) (SimpleSelect))
