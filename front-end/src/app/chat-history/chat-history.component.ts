import { Component } from '@angular/core';
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-chat-history',
  standalone: true,
    imports: [
        NgForOf
    ],
  templateUrl: './chat-history.component.html'
})
export class ChatHistoryComponent {

}
