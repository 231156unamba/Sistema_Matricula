import { Routes } from '@angular/router';

export const routes: Routes = [
  { 
    path: '', 
    redirectTo: '/inicio', 
    pathMatch: 'full' 
  },
  { 
    path: 'inicio', 
    loadComponent: () => import('./features/inicio/inicio.component').then(m => m.InicioComponent)
  },
  { 
    path: 'login-ingresante', 
    loadComponent: () => import('./features/login/login.component').then(m => m.LoginComponent)
  },
  { 
    path: 'login-regular', 
    loadComponent: () => import('./features/login-regular/login-regular.component').then(m => m.LoginRegularComponent)
  },
  { 
    path: 'registro-ingresante', 
    loadComponent: () => import('./features/ingresante/registro.component').then(m => m.RegistroComponent)
  },
  { 
    path: 'matricula-regular', 
    loadComponent: () => import('./features/regular/seleccionar-cursos.component').then(m => m.SeleccionarCursosComponent)
  },
  { 
    path: 'admin', 
    loadComponent: () => import('./features/admin/admin.component').then(m => m.AdminComponent)
  },
  { 
    path: '**', 
    redirectTo: '/inicio' 
  }
];
