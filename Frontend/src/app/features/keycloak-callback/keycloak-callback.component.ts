import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakAuthService } from '../../core/services/keycloak-auth.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-keycloak-callback',
  standalone: true,
  template: `
    <div class="keycloak-callback-container">
      <div class="loading-card">
        <div class="spinner"></div>
        <h3>Procesando solicitud...</h3>
        <p>Estamos verificando tu autenticación</p>
      </div>
    </div>
  `,
  styles: [`
    .keycloak-callback-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    
    .loading-card {
      background: white;
      padding: 3rem;
      border-radius: 20px;
      box-shadow: 0 20px 40px rgba(0,0,0,0.1);
      text-align: center;
      max-width: 400px;
    }
    
    .spinner {
      width: 50px;
      height: 50px;
      border: 4px solid #f3f3f3;
      border-top: 4px solid #667eea;
      border-radius: 50%;
      animation: spin 1s linear infinite;
      margin: 0 auto 1.5rem;
    }
    
    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }
    
    h3 {
      color: #333;
      margin-bottom: 0.5rem;
      font-weight: 600;
    }
    
    p {
      color: #666;
      margin: 0;
    }
  `]
})
export class KeycloakCallbackComponent implements OnInit {
  constructor(
    private router: Router,
    private keycloakAuth: KeycloakAuthService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.handleKeycloakResponse();
  }

  private handleKeycloakResponse(): void {
    // Obtener parámetros de la URL
    const urlParams = new URLSearchParams(window.location.search);
    const success = urlParams.get('success') === 'true';
    const estudianteData = urlParams.get('estudiante');
    const message = urlParams.get('message');

    if (success && estudianteData) {
      try {
        const estudiante = JSON.parse(decodeURIComponent(estudianteData));
        
        // Guardar sesión
        this.authService.guardarSesion(estudiante);
        
        // Determinar tipo de estudiante y redirigir
        setTimeout(() => {
          if (estudiante.codigoEstudiante) {
            this.router.navigate(['/matricula-regular']);
          } else {
            this.router.navigate(['/matricula-ingresante']);
          }
        }, 1500);
        
      } catch (error) {
        console.error('Error parsing estudiante data:', error);
        this.router.navigate(['/inicio']);
      }
    } else {
      console.error('Authentication failed:', message);
      this.router.navigate(['/inicio']);
    }
  }
}
