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

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);

  createUser(user: regUser): Observable<any> {
    return this.http.post(environment.apiUrl + '/user', user, {observe: 'response'});
  }
}
