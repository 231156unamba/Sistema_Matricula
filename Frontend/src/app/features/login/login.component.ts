import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ModalService } from '../../shared/services/modal.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  dni = '';
  voucher = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private modalService: ModalService
  ) {}

  onSubmit() {
    if (!this.dni || !this.voucher) {
      this.modalService.showError('Error', 'Por favor complete todos los campos');
      return;
    }

    this.loading = true;
    this.authService.loginIngresante(this.dni, this.voucher)
      .pipe(finalize(() => { this.loading = false; }))
      .subscribe({
      next: (response) => {
        if (response.success) {
          if (response.estudiante) {
            this.authService.guardarSesion(response);
            const nombres = (response.estudiante as any)?.nombres || '';
            const apellidos = (response.estudiante as any)?.apellidos || '';
            const nombreCompleto = `${nombres} ${apellidos}`.trim();
            this.modalService.showSuccess('¡Bienvenid@!', nombreCompleto ? `Hola, ${nombreCompleto}` : 'Login exitoso');
            setTimeout(() => {
              this.router.navigate(['/matricula-ingresante']);
            }, 1500);
          } else {
            this.router.navigate(['/registro-ingresante'], {
              queryParams: { dni: this.dni, voucher: this.voucher }
            });
          }
        } else {
          this.modalService.showError('Error', response.message);
        }
      },
      error: (error) => {
        const backendMsg = error?.error?.message || error?.error?.error || error?.message;
        this.modalService.showError('Error', backendMsg || 'Credenciales incorrectas.');
      }
    });
  }

  loginWithKeycloak() {
    this.loading = true;
    // Redirigir a Keycloak o abrir popup
    const keycloakUrl = 'http://localhost:8180/auth/realms/unamba/protocol/openid-connect/auth?' +
      'client_id=frontend-app&' +
      'redirect_uri=' + encodeURIComponent(window.location.origin + '/keycloak-callback') + '&' +
      'response_type=code&' +
      'scope=openid profile email';
    
    window.location.href = keycloakUrl;
  }
}

