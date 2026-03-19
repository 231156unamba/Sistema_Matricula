import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  // Rutas que manejan sesión/localStorage deben renderizarse en el cliente
  { path: 'matricula-regular', renderMode: RenderMode.Client },
  { path: 'login-matricula-regular', renderMode: RenderMode.Client },
  { path: 'regular', renderMode: RenderMode.Client },
  { path: 'admin', renderMode: RenderMode.Client },
  { path: 'admin/dashboard', renderMode: RenderMode.Client },
  // El resto puede usar SSR
  { path: '**', renderMode: RenderMode.Server }
];
