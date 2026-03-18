import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ModalService } from '../../shared/services/modal.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-login-matricula-regular',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login-matricula-regular.component.html',
  styleUrls: ['./login-matricula-regular.component.css']
})
export class LoginMatriculaRegularComponent {
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
    this.authService.loginMatriculaRegular(this.dni, this.voucher)
      .pipe(finalize(() => { this.loading = false; }))
      .subscribe({
      next: (response) => {
        if (response.success && response.estudiante) {
          this.authService.guardarSesion(response);
          // Guardar el voucher temporalmente para la matrícula
          localStorage.setItem('matricula_voucher', this.voucher);
          this.modalService.showSuccess('Validación Exitosa', 'Procede a seleccionar tus cursos.');
          setTimeout(() => {
            this.router.navigate(['/matricula-regular']);
          }, 1500);
        } else {
          this.modalService.showError('Error', response.message || 'Voucher o DNI incorrectos.');
        }
      },
      error: (error) => {
        const backendMsg = error?.error?.message || 'Error al validar datos.';
        this.modalService.showError('Error', backendMsg);
      }
    });
  }
}
