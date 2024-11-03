import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";
import {environment} from "../environment";

export type login = {
  email: string,
  password: string,
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient)

  login(email: string, password: string): Observable<any> {
    return this.http.post(
      environment.apiUrl + '/auth/login',
      {email: email, password: password},
      {withCredentials: true, observe: 'response'}
    );
  }

  logout(): Observable<any> {
    return this.http.post(environment.apiUrl + '/auth/logout', null, {withCredentials: true});
  }

  isAuthenticated(): Observable<any> {
    return this.http.get(environment.apiUrl + '/auth/validate-token', {withCredentials: true}).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }
}
