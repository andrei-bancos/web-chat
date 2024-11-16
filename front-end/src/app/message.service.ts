import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../environment";

export type SendMessage = {
  senderId: string;
  content: string;
}

export type DisplayMessage = {
  senderId: string;
  receiverId: string;
  content: string;
  createdAt: string;
}

export type LastChats = {
  userId: string;
  firstName: string;
  lastName: string;
  lastMessage: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private readonly http = inject(HttpClient);

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
}
