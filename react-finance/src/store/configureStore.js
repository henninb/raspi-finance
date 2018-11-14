import { createStore, applyMiddleware } from 'redux'
import rootReducer from './index'

export const middleware = []

export default function configureStore () {
  return createStore(
    rootReducer, 
    window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
  )
}
