import {createAction, props} from "@ngrx/store";

export const updateVisibilityContacts = createAction("Update Visibility Contacts", props<{visible: boolean}>());
