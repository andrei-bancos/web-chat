import { Routes } from '@angular/router';
import {MainComponent} from "./main/main.component";
import {ChatComponent} from "./chat/chat.component";
import {authGuard} from "./auth.guard";

export const routes: Routes = [
  {path: '', component: MainComponent, title: 'Web Chat'},
  {path: 'chat', component: ChatComponent, title: 'Web Chat', canActivate: [authGuard]},
];
