import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, Subject} from "rxjs";
import {environment} from "../environment";

export type SendMessage = {
  senderId: string;
  content: string;
}

export type DisplayMessage = {
  id: string;
  senderId: string;
  receiverId: string;
  content: string;
  read: boolean;
  createdAt: string;
}

export type LastChats = {
  senderUserId: string;
  receivedUserId: string;
  firstName: string;
  lastName: string;
  lastMessage: string;
  read: boolean;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private readonly http = inject(HttpClient);

  private chatHistorySubject = new Subject<void>();
  chatHistoryUpdate$ = this.chatHistorySubject.asObservable();

  triggerChatHistoryUpdate$() {
    this.chatHistorySubject.next();
  }

  getChatMessages(senderId: string, receivedId: string): Observable<any> {
    return this.http.get(
      environment.apiUrl + '/messages/' + senderId + '/chat/' + receivedId,
      {
        withCredentials: true,
        observe: 'response'
      }
    );
  }

  getLastChats(): Observable<any> {
    return this.http.get(environment.apiUrl + '/messages/chats', {withCredentials: true, observe: 'response'});
  }

  sendMessage(receivedId: string, body: SendMessage): Observable<any> {
    return this.http.post(
      environment.apiUrl + '/messages/' + receivedId,
      body,
      {
        withCredentials: true,
        observe: 'response'
      }
    );
  }

  markAsRead(messageId: string): Observable<any> {
    return this.http.put(
      environment.apiUrl + '/messages/' + messageId + '/markAsRead',
      '',
      {
        withCredentials: true,
        observe: 'response'
      }
    );
  }
}
