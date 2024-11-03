import { Component } from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-chat-message',
  standalone: true,
  imports: [
    NgOptimizedImage,
    MatIcon
  ],
  templateUrl: './chat-message.component.html'
})
export class ChatMessageComponent {

}
