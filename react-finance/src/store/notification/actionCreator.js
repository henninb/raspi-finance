import { SHOW_NOTIFICATION, SHOW_ACCOUNTS } from './actionType'

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

export function showAccounts (isShown, message) {
  return {
    type: SHOW_ACCOUNTS,
    payload: {
      isShown,
      message,
    },
  }
}