import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';

export const adminGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const adminSession = localStorage.getItem('adminSession');
  
  if (adminSession) {
    try {
      const admin = JSON.parse(adminSession);
      if (admin && admin.rol) {
        return true;
      }
    } catch (e) {
      console.error('Error parsing admin session', e);
    }
  }
  
  router.navigate(['/admin']);
  return false;
};
