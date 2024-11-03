import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";
import {AuthService, login} from "../auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  private readonly fb = new FormBuilder()
  private readonly authService = inject(AuthService)
  private readonly router = inject(Router);

  loginForm: FormGroup | undefined;
  errorMessage: string = "";

  ngOnInit() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    })
  }

  onSubmit() {
    if(this.loginForm?.valid) {
      const user: login = this.loginForm.value;
      this.authService.login(user.email, user.password).subscribe({
        next: () => {
          this.loginForm!.reset();
          this.errorMessage = "";
          this.router.navigate(['/chat'])
        },
        error: (res) => {
          this.errorMessage = res.error.message;
        }
      })
    }
  }
}
