import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../environment";

export type regUser = {
  firstName: string,
  lastName: string,
  email: string;
  username: string;
  password: string;
}

export type User = {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  createdAt: string;
} | null

export type ChangeUserPassword = {
  oldPassword: string;
  newPassword: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);

  getAuthenticatedUserDetails(): Observable<any> {
    return this.http.get(environment.apiUrl + '/user/authenticated', {withCredentials: true, observe: 'response'});
  }

  createUser(user: regUser): Observable<any> {
    return this.http.post(environment.apiUrl + '/user', user, {observe: 'response'});
  }

  changeUserPassword(body: ChangeUserPassword): Observable<any> {
    return this.http.put(environment.apiUrl + '/user/password', body, {withCredentials: true, observe: 'response'});
  }

  deleteUser(): Observable<any> {
    return this.http.delete(environment.apiUrl + '/user', {withCredentials: true});
  }

  getContacts(): Observable<any> {
    return this.http.get(environment.apiUrl + '/user/contacts', {withCredentials: true, observe: 'response'});
  }

  addContact(usernameContact: string): Observable<any> {
    return this.http.post(
      environment.apiUrl + '/user/contact/' + usernameContact,
      '',
      {
        withCredentials: true,
        observe: 'response'
      }
    );
  }

  deleteContact(userContactId: string): Observable<any> {
    return this.http.delete(
      environment.apiUrl + '/user/contact/' + userContactId,
      {
        withCredentials: true,
        observe: 'response'
      }
    );
  }
}
