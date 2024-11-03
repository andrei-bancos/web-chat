import {Component, inject, OnInit} from '@angular/core';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {Router} from "@angular/router";
import {LoginComponent} from "../login/login.component";
import {RegisterComponent} from "../register/register.component";
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgIf,
    LoginComponent,
    RegisterComponent
  ],
  templateUrl: './main.component.html'
})
export class MainComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);

  imageBgUrls: string[] = [
    '/bg1.png',
    '/bg2.png',
    '/bg3.png'
  ];

  imageBgUrl: string = '';

  showLogin: boolean = false;
  showRegister: boolean = false;

  ngOnInit() {
    this.getRandomImageUrl()
    this.authService.isAuthenticated().subscribe(isAuthenticated => {
      if(isAuthenticated) this.router.navigate(['/chat']);
    })
  }

  getRandomImageUrl(): void {
    const randomIndex = Math.floor(Math.random() * this.imageBgUrls.length);
    this.imageBgUrl = this.imageBgUrls[randomIndex];
  }

  onClickGoogleLoginBtn() {
    this.router.navigate(['/']);
  }

  onClickLogo() {
    this.showLogin = false;
    this.showRegister = false;
  }

  displayLogin() {
    this.showLogin = true;
    this.showRegister = false;
  }

  displayRegister() {
    this.showLogin = false;
    this.showRegister = true;
  }
}
