import {AfterViewInit, Component, inject} from '@angular/core';
import {NgForOf} from "@angular/common";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {MatTooltip} from "@angular/material/tooltip";
import {MatIcon} from "@angular/material/icon";
import {LastChats, MessageService} from "../message.service";
import {WebSocketService} from "../web-socket.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-chat-history',
  standalone: true,
  imports: [
    NgForOf,
    RouterLink,
    MatTooltip,
    MatIcon,
    RouterLinkActive
  ],
  templateUrl: './chat-history.component.html'
})
export class ChatHistoryComponent implements AfterViewInit {
  private readonly messageService = inject(MessageService);
  private readonly webSocketService = inject(WebSocketService);

  private chatHistoryUpdate: Subscription | undefined;
  private messagesWsSubscription: Subscription | undefined;

  lastChats: LastChats[] = [];

  constructor() {
    this.loadChatHistory()

    this.chatHistoryUpdate = this.messageService.chatHistoryUpdate$.subscribe(() => {
      this.loadChatHistory();
    })
  }

  ngAfterViewInit() {
    this.messagesWsSubscription = this.webSocketService.messagesWS$.subscribe(() => {
      this.loadChatHistory();
    });
  }

  private loadChatHistory(): void {
    this.messageService.getLastChats().subscribe({
      next: (res) => {
        this.lastChats = res.body;
      },
      error: (err) => {
        console.log(err);
      }
    })
  }
}
