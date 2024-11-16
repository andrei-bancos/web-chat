import {Component, inject} from '@angular/core';
import {NgForOf} from "@angular/common";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {MatTooltip} from "@angular/material/tooltip";
import {MatIcon} from "@angular/material/icon";
import {LastChats, MessageService} from "../message.service";

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
export class ChatHistoryComponent {
  private readonly messageService = inject(MessageService);

  lastChats: LastChats[] = [];

  constructor() {
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
