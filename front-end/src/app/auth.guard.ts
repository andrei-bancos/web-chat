import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "./auth.service";
import {map} from "rxjs";

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const authService = inject(AuthService);
  return authService.isAuthenticated().pipe(
    map(isAuthenticated => isAuthenticated || router.navigate(["/"]))
  )
};
