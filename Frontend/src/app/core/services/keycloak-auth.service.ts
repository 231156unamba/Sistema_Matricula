import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class KeycloakAuthService {
  private loading = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  // Método para manejar la respuesta de Keycloak después del login exitoso
  handleKeycloakCallback(): void {
    this.loading = true;
    
    // Escuchar el mensaje de la ventana principal con los datos de autenticación
    window.addEventListener('message', (event) => {
      // Verificar origen de seguridad
      if (event.origin !== window.location.origin) {
        console.warn('Origen no permitido:', event.origin);
        this.loading = false;
        return;
      }

      const { success, estudiante, message } = event.data;
      
      if (success && estudiante) {
        // Guardar sesión y redirigir
        this.authService.guardarSesion(estudiante);
        this.loading = false;
        
        // Determinar tipo de estudiante y redirigir
        if (estudiante.codigoEstudiante) {
          this.router.navigate(['/matricula-regular']);
        } else {
          this.router.navigate(['/matricula-ingresante']);
        }
      } else {
        console.error('Error en autenticación:', message);
        this.loading = false;
        this.router.navigate(['/inicio']);
      }
    });

    // Timeout por si no hay respuesta
    setTimeout(() => {
      if (this.loading) {
        this.loading = false;
        console.error('Timeout esperando respuesta de Keycloak');
        this.router.navigate(['/inicio']);
      }
    }, 10000);
  }

  // Método para verificar si hay una sesión activa
  checkSession(): boolean {
    const estudiante = this.authService.obtenerSesion();
    return !!estudiante;
  }

  // Método para obtener el estado de carga
  isLoading(): boolean {
    return this.loading;
  }

  // Método para iniciar el proceso de login con Keycloak
  initiateKeycloakLogin(): void {
    this.loading = true;
    
    // Abrir popup de Keycloak o redirigir
    const keycloakUrl = 'http://localhost:8080/auth'; // URL de Keycloak
    const popup = window.open(
      keycloakUrl,
      'keycloak-login',
      'width=500,height=600,scrollbars=yes,resizable=yes'
    );

    // Verificar si el popup fue cerrado
    const checkClosed = setInterval(() => {
      if (popup?.closed) {
        clearInterval(checkClosed);
        this.loading = false;
      }
    }, 1000);
  }
}
