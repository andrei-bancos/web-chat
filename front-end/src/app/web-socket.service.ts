import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private client: Client;
  private messageSubjectWS: Subject<any> = new Subject<any>(); // Centralized Subject
  public messagesWS$ = this.messageSubjectWS.asObservable();    // Observable for components

  constructor() {
    this.client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      reconnectDelay: 5000,
      debug: (str) => console.log(str),
    });

    this.client.onConnect = () => {
      console.log('Connected');
      this.client.subscribe('/user/queue/messages', (msg) => {
        this.messageSubjectWS.next(JSON.parse(msg.body));
      });
    };

    this.client.activate();
  }

  disconnect(): void {
    if (this.client) {
      this.client.deactivate();
    }
  }
}
