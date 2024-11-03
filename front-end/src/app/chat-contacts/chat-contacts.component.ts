import { Component } from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {NgForOf, NgOptimizedImage} from "@angular/common";
import {Store} from "@ngrx/store";
import {ChatState} from "../chat/chat.reducer";
import {updateVisibilityContacts} from "../chat/chat.actions";

@Component({
  selector: 'app-chat-contacts',
  standalone: true,
  imports: [
    MatIcon,
    NgOptimizedImage,
    NgForOf
  ],
  templateUrl: './chat-contacts.component.html'
})
export class ChatContactsComponent {
  constructor(private store: Store<{chat: ChatState}>) {}

  onCloseClick(): void {
    this.store.dispatch(updateVisibilityContacts({visible: false}));
  }
}
