import { Routes } from '@angular/router';
import {MainComponent} from "./main/main.component";
import {ChatComponent} from "./chat/chat.component";
import {authGuard} from "./auth.guard";
import {SettingsComponent} from "./settings/settings.component";
import {PageNotFoundComponent} from "./page-not-found/page-not-found.component";

export const routes: Routes = [
  {path: '', component: MainComponent, title: 'Web Chat'},
  {path: 'chat', component: ChatComponent, title: 'Web Chat', canActivate: [authGuard]},
  {path: 'chat/:userId', component: ChatComponent, title: 'Web Chat', canActivate: [authGuard]},
  {path: 'settings', component: SettingsComponent, title: 'Settings', canActivate: [authGuard]},
  {path: '**', component: PageNotFoundComponent, title: 'Page not found'}
];
