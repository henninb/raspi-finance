import { SET_ACCOUNT, SET_TRANSACTION, SET_TRANSACTION_LOAD_STATUS, SET_UPDATED_TRANSACTION } from './actionType'

export function setTransaction (viewStatus, transactions) {
  return {
    type: SET_TRANSACTION,
    payload: {
      viewStatus,
      transactions,
    },
  }
}

export function setTransactionLoadStatus (viewStatus) {
  return {
    type: SET_TRANSACTION_LOAD_STATUS,
    payload: {
      viewStatus,
    },
  }
}


export function setAccount (isShown, accountNameOwners) {
  return {
    type: SET_ACCOUNT,
    payload: {
      isShown,
      accountNameOwners,
    },
  }
}

export function setUpdatedTransaction (updatedTransaction) {
  return {
    type: SET_UPDATED_TRANSACTION,
    payload: {
      updatedTransaction,
    },
  }
}