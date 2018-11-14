import { SHOW_NOTIFICATION } from './actionType'

export function showNotification (isShown, message) {
  return {
    type: SHOW_NOTIFICATION,
    payload: {
      isShown,
      message,
    },
  }
}
