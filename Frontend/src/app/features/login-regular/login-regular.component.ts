import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ModalService } from '../../shared/services/modal.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-login-regular',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login-regular.component.html',
  styleUrls: ['./login-regular.component.css']
})
export class LoginRegularComponent {
  dni = '';
  codigo = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private modalService: ModalService
  ) {}

  onSubmit() {
    if (!this.dni || !this.codigo) {
      this.modalService.showError('Error', 'Por favor complete todos los campos');
      return;
    }

    this.loading = true;
    this.authService.loginRegular(this.dni, this.codigo)
      .pipe(finalize(() => { this.loading = false; }))
      .subscribe({
      next: (response) => {
        if (response.success) {
          this.authService.guardarSesion(response);
          const nombres = (response.estudiante as any)?.nombres || '';
          const apellidos = (response.estudiante as any)?.apellidos || '';
          const nombreCompleto = `${nombres} ${apellidos}`.trim();
          this.modalService.showSuccess('¡Bienvenid@!', nombreCompleto ? `Hola, ${nombreCompleto}` : 'Login exitoso');
          setTimeout(() => {
            this.router.navigate(['/matricula-regular']);
          }, 1500);
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
}
