import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  if (typeof window === 'undefined') return next(req);

  const router = inject(Router);
  
  return next(req).pipe(
    catchError(error => {
      if (error.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('estudiante');
        localStorage.removeItem('adminSession');
        router.navigate(['/inicio']);
      }
      
      if (error.status === 403) {
        console.error('Acceso denegado');
      }
      
      return throwError(() => error);
    })
  );
};
