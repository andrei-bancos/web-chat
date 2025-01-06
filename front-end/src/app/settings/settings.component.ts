import {Component, inject, OnInit} from '@angular/core';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {ChangeUserPassword, User, UserService} from "../user.service";
import {RouterLink} from "@angular/router";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [
    NgOptimizedImage,
    RouterLink,
    FormsModule,
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit {
  private readonly userService = inject(UserService);
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);

  formResponseMessage: string = "";

  changePasswordForm: FormGroup | undefined;

  user: User = null;
  contactsTotalNumber: number | null = null;

  ngOnInit() {
    this.userService.getAuthenticatedUserDetails().subscribe({
      next: res => {
        this.user = {
          id: res.body.id,
          firstName: res.body.firstName,
          lastName: res.body.lastName,
          email: res.body.email,
          username: res.body.username,
          createdAt: new Date(res.body.createdAt).toLocaleString()
        }
      },
      error: error => {
        console.error(error);
      }
    });

    this.userService.getContacts().subscribe({
      next: res => {
        this.contactsTotalNumber = res.body.length;
      },
      error: error => {
        console.error(error);
      }
    })

    this.changePasswordForm = this.fb.group({
      oldPassword: ['', [Validators.required]],
      newPassword: ['',   [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(20),
        Validators.pattern(/[A-Z]/),
        Validators.pattern(/[a-z]/),
        Validators.pattern(/[0-9]/),
        Validators.pattern(/[\W_]/)
      ]]
    })
  }

  onChangePasswordSubmit() {
    if(this.changePasswordForm!.valid) {
      const body: ChangeUserPassword = {
        oldPassword: this.changePasswordForm?.value.oldPassword,
        newPassword: this.changePasswordForm?.value.newPassword
      }

      this.userService.changeUserPassword(body).subscribe({
        next: (res) => {
          this.formResponseMessage = res.body.message;
          this.changePasswordForm?.reset()
          setTimeout(() => {
            this.formResponseMessage = "";
          }, 4000)
        },
        error: (res) => {
          this.formResponseMessage = res.error.message;
        }
      })
    }
  }

  onDelete() {
    this.userService.deleteUser().subscribe();
    this.authService.logout().subscribe();
  }
}
