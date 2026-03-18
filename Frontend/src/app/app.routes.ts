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
    path: 'login-matricula-regular', 
    loadComponent: () => import('./features/login-matricula-regular/login-matricula-regular.component').then(m => m.LoginMatriculaRegularComponent)
  },
  { 
    path: 'matricula-ingresante', 
    loadComponent: () => import('./features/ingresante/registro.component').then(m => m.RegistroComponent)
  },
  { 
    path: 'keycloak-callback', 
    loadComponent: () => import('./features/keycloak-callback/keycloak-callback.component').then(m => m.KeycloakCallbackComponent)
  },
  { 
    path: 'matricula-regular', 
    loadComponent: () => import('./features/regular/seleccionar-cursos.component').then(m => m.SeleccionarCursosComponent)
  },
  {
    path: 'regular',
    loadComponent: () => import('./features/regular-dashboard/regular-dashboard.component').then(m => m.RegularDashboardComponent)
  },
  { 
    path: 'admin', 
    loadComponent: () => import('./features/admin/admin.component').then(m => m.AdminComponent)
  },
  {
    path: 'admin/dashboard',
    loadComponent: () => import('./features/admin-dashboard/admin-dashboard.component').then(m => m.AdminDashboardComponent)
  },
  { 
    path: '**', 
    redirectTo: '/inicio' 
  }
];
