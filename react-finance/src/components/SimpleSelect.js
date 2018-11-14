import React, { Component } from 'react'
import Select from 'react-select'
import { withStyles } from '@material-ui/core/styles';
import axios from 'axios';

class SimpleSelect extends Component {
  constructor(props) {
    super(props);
    this.state = {
      //selectedOption: null,
      options: [],
      accountNameOwners: [],
    };
  }

  componentDidMount () {
    axios.get("http://localhost:8080/select_accounts")
      .then((response) => {
        this.setState({accountNameOwners: response.data}, () => {
   
        var joined = [];
        this.state.accountNameOwners.forEach(element => {
          joined = joined.concat({ value:  element.accountNameOwner, label:  element.accountNameOwner });
        });
        this.setState({ options: joined });
        });
     })
     .catch(function (error) {
         console.log(error);
     });
  }

  handleChange = (selectedOption) => {
    //this.setState({ selectedOption });

    window.location.href = 'http://localhost:3000/list/' + selectedOption.value;
  }

  render() {
    //const { selectedOption } = this.state;

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

export default withStyles(styles)(SimpleSelect);
