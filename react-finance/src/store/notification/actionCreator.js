import { SHOW_NOTIFICATION, SHOW_ACCOUNTS } from './actionType'

export function showNotification (isShown, message) {
  //alert('message got here:' + message);
  //alert('message got here:' + SHOW_NOTIFICATION);

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