import { SET_ACCOUNT, SET_TRANSACTION, SET_TRANSACTION_LOAD_STATUS } from './actionType'

export const initialState = {
  isShown: false,
  accountNameOwners: '',
  viewStatus: 'none',
  transactions: '',
}

export default function accountReducer (state = initialState, action = {}) {
  switch (action.type) {
    case SET_ACCOUNT: {
      const { payload } = action
      const { isShown, accountNameOwners } = payload
      const newState = {
        ...state,
        isShown,
        accountNameOwners,
      }
      return newState
    }
    case SET_TRANSACTION: {
      const { payload } = action
      const { viewStatus, transactions } = payload
      const newState = {
        ...state,
        viewStatus,
        transactions,
      }
      return newState
    }
    case SET_TRANSACTION_LOAD_STATUS: {
      const { payload } = action
      const { viewStatus } = payload
      const newState = {
        ...state,
        viewStatus,
      }
      return newState
    }
    default:
      return state
  }
}
