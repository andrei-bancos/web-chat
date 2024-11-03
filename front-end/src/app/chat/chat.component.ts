import {Component, inject, OnDestroy} from '@angular/core';
import {AsyncPipe, NgIf, NgOptimizedImage} from "@angular/common";
import {ChatHistoryComponent} from "../chat-history/chat-history.component";
import {ChatMessageComponent} from "../chat-message/chat-message.component";
import {ChatContactsComponent} from "../chat-contacts/chat-contacts.component";
import {Store} from "@ngrx/store";
import {ChatState} from "./chat.reducer";
import {Observable, Subscription} from "rxjs";
import {updateVisibilityContacts} from "./chat.actions";
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [
    NgOptimizedImage,
    ChatHistoryComponent,
    ChatMessageComponent,
    ChatContactsComponent,
    NgIf,
    AsyncPipe
  ],
  templateUrl: './chat.component.html'
})
export class ChatComponent implements OnDestroy {
  private readonly authService = inject(AuthService);

  chat$: Observable<ChatState>;
  chatSubscribe: Subscription | undefined;

  isContactsVisible: boolean = false;

  constructor(private store: Store<{ chat: ChatState }>) {
    this.chat$ = this.store.select(store => store.chat);
    this.chatSubscribe = this.chat$.subscribe(chat => {
      this.isContactsVisible = chat.isContactsVisible;
    })
  }

  logout(): void {
    this.authService.logout().subscribe();
  }

  onContactsClick(): void {
    this.store.dispatch(updateVisibilityContacts({visible: true}));
    console.log(this.isContactsVisible)
  }

  ngOnDestroy() {
    if(this.chatSubscribe) {
      this.chatSubscribe.unsubscribe();
    }
  }
}
