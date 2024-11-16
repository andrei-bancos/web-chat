import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {ChatHistoryComponent} from "../chat-history/chat-history.component";
import {ChatMessageComponent} from "../chat-message/chat-message.component";
import {ChatContactsComponent} from "../chat-contacts/chat-contacts.component";
import {Store} from "@ngrx/store";
import {ChatState} from "./chat.reducer";
import {Observable, Subscription} from "rxjs";
import {updateVisibilityContacts} from "./chat.actions";
import {AuthService} from "../auth.service";
import {User, UserService} from "../user.service";
import {ActivatedRoute, RouterLink} from "@angular/router";

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [
    NgOptimizedImage,
    ChatHistoryComponent,
    ChatMessageComponent,
    ChatContactsComponent,
    NgIf,
    RouterLink,
  ],
  templateUrl: './chat.component.html'
})
export class ChatComponent implements OnInit, OnDestroy {
  private readonly authService = inject(AuthService);
  private readonly userService = inject(UserService);
  private readonly activatedRouter = inject(ActivatedRoute);

  user: User = null;

  chat$: Observable<ChatState>;
  chatSubscribe: Subscription | undefined;

  isContactsVisible: boolean = false;

  chatUserId: string | null = null;

  constructor(private store: Store<{ chat: ChatState }>) {
    this.chat$ = this.store.select(store => store.chat);
    this.chatSubscribe = this.chat$.subscribe(chat => {
      this.isContactsVisible = chat.isContactsVisible;
    })

    this.activatedRouter.paramMap.subscribe(paramMap => {
      this.chatUserId = paramMap.get("userId");
    })
  }

  ngOnInit() {
    this.userService.getAuthenticatedUserDetails().subscribe({
      next: res => {
        this.user = {
          id: res.body.id,
          firstName: res.body.firstName,
          lastName: res.body.lastName,
          email: res.body.email,
          username: res.body.username,
          createdAt: res.body.createdAt
        }
      },
      error: error => {
        console.error(error);
      }
    });
  }

  logout(): void {
    this.authService.logout().subscribe();
  }

  onContactsClick(): void {
    this.store.dispatch(updateVisibilityContacts({visible: true}));
  }

  ngOnDestroy() {
    if(this.chatSubscribe) {
      this.chatSubscribe.unsubscribe();
    }
  }
}
