import {
  Component,
  ElementRef,
  inject,
  Input,
  OnChanges,
  ViewChild,
} from '@angular/core';
import {DatePipe, NgClass, NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {MatIcon} from "@angular/material/icon";
import {User, UserService} from "../user.service";
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {SendMessage, MessageService, DisplayMessage} from "../message.service";

@Component({
  selector: 'app-chat-message',
  standalone: true,
  imports: [
    NgOptimizedImage,
    MatIcon,
    NgIf,
    NgForOf,
    FormsModule,
    ReactiveFormsModule,
    DatePipe,
    NgClass
  ],
  templateUrl: './chat-message.component.html'
})
export class ChatMessageComponent implements OnChanges {
  private readonly userService = inject(UserService);
  private readonly messageService = inject(MessageService);

  @Input() chatUserId: string | null = null;
  @ViewChild('messagesScroll') private messagesScroll: ElementRef | undefined;

  receivedUser: User | null = null;
  senderUser: User | null = null;

  messages: DisplayMessage[] = [];

  messageFormControl: FormControl = new FormControl("", [Validators.required]);

  constructor() {
    this.userService.getAuthenticatedUserDetails().subscribe({
      next: res => {
        this.senderUser = res.body;
      },
      error: (err) => {
        console.error(err);
      }
    })
    this.loadChat();
  }

  ngOnChanges() {
    this.loadChat();
  }

  private scrollToBottom(): void {
    setTimeout(() => {
      const container = this.messagesScroll!.nativeElement;
      container.scrollTop = container.scrollHeight;
    }, 0)
  }

  private loadChat(): void {
    if(this.chatUserId !== null) {
      this.userService.getContact(this.chatUserId).subscribe({
        next: res => {
          this.receivedUser = res.body;
          this.loadMessages()
        },
        error: (err) => {
          console.error(err);
        }
      })
    }
    this.scrollToBottom();
  }

  private loadMessages(): void {
    this.messageService.getChatMessages(this.senderUser!.id, this.chatUserId!).subscribe({
      next: (res) => {
        this.messages = res.body;
      },
      error: (err) => {
        console.log(err)
      },
      complete: () => {
        if(this.messages.length > 0) {
          this.scrollToBottom()
        }
      }
    })
  }

  onMessageSubmit() {
    if(this.messageFormControl.valid && this.senderUser !== null && this.receivedUser !== null) {
      const message: SendMessage = {
        senderId: this.senderUser.id,
        content: this.messageFormControl.value
      }

      this.messageService.sendMessage(this.receivedUser.id, message).subscribe({
        next: () => {
          this.messageFormControl.reset();
          this.loadChat()
        },
        error: (err) => {
          console.log(err);
        }
      })
    }
  }
}