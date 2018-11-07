import React from 'react';
import Select from 'react-select';
import { withStyles } from '@material-ui/core/styles';
import axios from 'axios';

class SimpleSelect extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedOption: null,
      options: [],
      accountNameOwners: [],
    };
    this.getData()
  }

  //componentDidMount () {
  //  axios.get("http://localhost:8080/select_accounts").then(result => {
  //    this.setState({
  //      accountNameOwners:result.data,
  //    })
  //  }).catch(error => {
  //    console.log(error)
  //  })
  //}

  handleChange = (selectedOption) => {
    this.setState({ selectedOption });

    window.location.href = 'http://localhost:3000/list/' + selectedOption.value;
  }
  
  getData = () => {
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

  render() {
    const { selectedOption } = this.state;

    return (
      <Select
        value={selectedOption}
        onChange={this.handleChange}
        options={this.state.options}
      />
    );
  }
}

const styles = theme => ({
});

export default withStyles(styles)(SimpleSelect);
