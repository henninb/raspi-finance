import React, { Component } from 'react';
import './DropdownMenu.css';

class DropdownMenu extends React.Component {  
  render() {
    return (

<div>
<ul>
  <li><a href="#home">Home</a></li>
  <li><a href="#news">News</a></li>
  <li class="dropdown">
    <a href="javascript:void(0)" class="dropbtn">Dropdown</a>
    <div class="dropdown-content">
      <a href="#">Link 1</a>
      <a href="#">Link 2</a>
      <a href="#">Link 3</a>
    </div>
  </li>
  <li><a href="#next">Next</a></li>
</ul>

</div>

    );
  }
}

export default DropdownMenu;

