import { HttpInterceptorFn } from '@angular/common/http';

// URLs que NO deben llevar token (endpoints públicos)
const PUBLIC_URLS = [
  '/api/auth/',
  '/api/anuncios/activos',
  '/api/recursos/horario/',
  '/api/recursos/malla/',
  '/api/recursos/reglamento',
  '/api/recursos/carreras',
];

function esPublico(url: string): boolean {
  return PUBLIC_URLS.some(pub => url.includes(pub));
}

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  if (typeof window === 'undefined') return next(req);

  // No agregar token a endpoints públicos
  if (esPublico(req.url)) return next(req);

  const token = window.localStorage.getItem('token');
  if (token) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(req);
};
