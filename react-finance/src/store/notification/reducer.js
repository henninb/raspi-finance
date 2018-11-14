import { SHOW_NOTIFICATION } from './actionType'

export const initialState = {
  isShown: false,
  message: '',
}

export default function notificationReducer (state = initialState, action = {}) {
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
    default:
      return state
  }
}
