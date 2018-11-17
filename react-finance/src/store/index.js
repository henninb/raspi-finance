import { combineReducers } from 'redux'
import notification from './account/reducer'

const rootReducer = combineReducers({
  notification,
})

export default rootReducer
