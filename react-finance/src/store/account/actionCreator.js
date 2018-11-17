import { SHOW_NOTIFICATION, SET_ACCOUNT } from './actionType'

export function showNotification (isShown, message) {
   let x = {
    type: SHOW_NOTIFICATION,
    payload: {
      isShown,
      message,
    },
   }

  return x
}

export function setAccount (isShown, message) {
  return {
    type: SET_ACCOUNT,
    payload: {
      isShown,
      message,
    },
  }
}
