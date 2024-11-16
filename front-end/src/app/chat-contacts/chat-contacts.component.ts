import {Component, inject, OnInit} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {Store} from "@ngrx/store";
import {ChatState} from "../chat/chat.reducer";
import {updateVisibilityContacts} from "../chat/chat.actions";
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {User, UserService} from "../user.service";
import {MatTooltip} from "@angular/material/tooltip";
import {Router} from "@angular/router";

@Component({
  selector: 'app-chat-contacts',
  standalone: true,
  imports: [
    MatIcon,
    NgOptimizedImage,
    NgForOf,
    FormsModule,
    NgIf,
    ReactiveFormsModule,
    MatTooltip,
  ],
  templateUrl: './chat-contacts.component.html'
})
export class ChatContactsComponent implements OnInit {
  private readonly userService = inject(UserService);
  private readonly router = inject(Router);

  searchFormControl: FormControl = new FormControl("", []);
  userNameFormControl: FormControl = new FormControl("", [Validators.required]);

  contacts: User[] | null = null;
  contactsFiltered: User[] | null = null;

  constructor(private store: Store<{chat: ChatState}>) {}

  ngOnInit() {
    this.getOrReloadContacts()
  }

  private getOrReloadContacts() {
    this.userService.getContacts().subscribe({
      next: (res) => {
        this.contacts = res.body;
        this.contactsFiltered = res.body;
      },
      error: (err) => {
        this.contacts = null;
        console.error(err);
      }
    })
  }

  onCloseClick(): void {
    this.store.dispatch(updateVisibilityContacts({visible: false}));
  }

  onSearchContacts(): void {
    const searchTerm = this.searchFormControl.value.trim().toLowerCase();

    if (!searchTerm) {
      this.contactsFiltered = this.contacts;
    } else {
      const searchTerms = searchTerm.split(' ').map(
        (term: string) => term.trim().toLowerCase()
      ).filter((term: string) => term);

      this.contactsFiltered = this.contacts!.filter(user => {
        return searchTerms.every((term: string) =>
          user!.username.toLowerCase().includes(term) ||
          user!.firstName.toLowerCase().includes(term) ||
          user!.lastName.toLowerCase().includes(term)
        );
      });
    }
  }

  onAddContactSubmit(): void {
    if(this.userNameFormControl.valid) {
      this.userService.addContact(this.userNameFormControl.value).subscribe({
        next: () => {
          this.getOrReloadContacts();
          this.userNameFormControl.reset()
        },
        error: (err) => {
          console.error(err);
        }
      })
    }
  }

  onDeleteContact(userContactId: string): void {
    this.userService.deleteContact(userContactId).subscribe({
      next: () => {
        this.getOrReloadContacts();
      }
    })
  }

  onOpenChat(userId: string): void {
    this.onCloseClick();
    this.router.navigate([`/chat/${userId}`]);
  }
}
