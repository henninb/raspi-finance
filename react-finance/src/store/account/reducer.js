import { SHOW_NOTIFICATION, SET_ACCOUNT } from './actionType'

export const initialNotificationState = {
  isShown: false,
  message: '',
}

export default function accountReducer (state = initialNotificationState, action = {}) {
  switch (action.type) {
    case SHOW_NOTIFICATION: {
      const { payload } = action
      const { isShown, message } = payload
      const newState = {
        ...state,
        isShown,
        message,
      }
      return newState
    }
    case SET_ACCOUNT: {
      const { payload } = action
      const { isShown, message } = payload
      const newState = {
        ...state,
        isShown,
        message,
      }
      return newState
    }
    default:
      return state
  }
}
