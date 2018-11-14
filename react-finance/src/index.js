import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom'
import './index.css'
import App from './App'
import * as serviceWorker from './serviceWorker'
import { combineReducers, createStore } from 'redux'
import { Provider } from 'react-redux'
import configureStore from './store/configureStore'

/*
function reducer(state, action) {
  if( action.type === 'chooseState' ) {
    return action.payload.newState;
  }

  return 'State';
}
*/

function productsReducer(state = [], action) {
  return [{name: 'iPhone'}];
}

function userReducer(state = '', action) {
  
  return 'brain';
}

const allReducers = combineReducers({
  products: productsReducer, 
  user: userReducer,
});

//const store = createStore(
//  allReducers, 
//  {
//    products: [{name: 'iPhone'}], 
//    user: 'brian',
//  },
//  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
//);


const store = configureStore()

/*
const action = {
   type: 'chooseState',
   payload: {
     newState: 'New state',
   }
};
*/

const updateUserAction = {
  type: 'updateUser',
  payload: {
    user: 'John',
  }
}

//  {/* <Provider store={store}> <App /> </Provider> */}
//store.dispatch(action);

//alert(store.getState().user);

ReactDOM.render((
  <Provider store={store} >
    <App />
  </Provider>
), document.getElementById('root'))

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.unregister();
