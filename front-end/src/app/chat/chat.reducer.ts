import {createReducer, on} from "@ngrx/store";
import {updateVisibilityContacts} from "./chat.actions";

export interface ChatState {
  isContactsVisible: boolean;
}

export const initialState: ChatState = {
  isContactsVisible: false
}

export const chatReducer = createReducer(
  initialState,
  on(updateVisibilityContacts, (state, {visible}) => ({
    ...state,
    isContactsVisible: visible
  })),
)
