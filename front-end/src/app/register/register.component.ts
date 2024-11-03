import {Component, EventEmitter, inject, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";
import {regUser, UserService} from "../user.service";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly userService = inject(UserService);

  registerForm: FormGroup | undefined;
  errorMessage: string = "";

  @Output() newUserAccountCreated : EventEmitter<void> = new EventEmitter();

  ngOnInit() {
    this.registerForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required, Validators.minLength(2)]],
      password: ['',   [
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

  onSubmit() {
    if(this.registerForm?.valid) {
      const user: regUser = this.registerForm!.value
      this.userService.createUser(user).subscribe({
        next: () => {
          this.registerForm?.reset()
          this.newUserAccountCreated.emit()
        },
        error: (res) => {
          this.errorMessage = res.error.message;
        }
      });
    }
  }
}
