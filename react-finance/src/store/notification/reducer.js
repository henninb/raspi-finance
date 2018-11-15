import { SHOW_NOTIFICATION, SHOW_ACCOUNTS } from './actionType'

export const initialNotificationState = {
  isShown: false,
  message: '',
}

//export default function notificationReducer (state = initialNotificationState, action = {}) {
export default function notificationReducer (state = initialNotificationState, action = {}) {
//export default function notificationReducer (state, action) {
    //if (state === undefined || state === null) {
    //  state = {}
    //  const newState1 = {
    //    ...state,
    //    isShown : false,
    //    message: '',
    //  }
	//  return newState1;
    //}
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
    case SHOW_ACCOUNTS: {
      alert('SHOW_ACCOUNTS');
    }
    default:
      return state
  }
}
