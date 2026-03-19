import { Component, OnInit, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ModalService } from '../../shared/services/modal.service';

@Component({
  selector: 'app-login-matricula-regular',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login-matricula-regular.component.html',
  styleUrls: ['./login-matricula-regular.component.css']
})
export class LoginMatriculaRegularComponent implements OnInit {
  dni = '';
  voucher = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private modalService: ModalService,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
    // Limpiar voucher anterior — siempre debe ingresar uno nuevo
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('matricula_voucher');
    }
    // Pre-llenar DNI si hay sesión activa (comodidad, pero voucher siempre vacío)
    const sesion = this.authService.obtenerSesion();
    if (sesion?.dni) {
      this.dni = sesion.dni;
    }
  }

  onSubmit(): void {
    if (!this.dni || !this.voucher) {
      this.modalService.showError('Error', 'Por favor complete todos los campos');
      return;
    }

    this.loading = true;
    this.authService.loginMatriculaRegular(this.dni, this.voucher).subscribe({
      next: (response) => {
        this.ngZone.run(() => {
          this.loading = false;
          if (response.success && response.estudiante) {
            this.authService.guardarSesion(response);
            localStorage.setItem('matricula_voucher', this.voucher);
            this.modalService.showSuccess('Validación Exitosa', 'Procede a seleccionar tus cursos.');
            setTimeout(() => this.router.navigate(['/matricula-regular']), 1500);
          } else {
            this.modalService.showError('Error', response.message || 'Voucher o DNI incorrectos.');
          }
        });
      },
      error: (error) => {
        this.ngZone.run(() => {
          this.loading = false;
          const backendMsg = error?.error?.message || 'Error al validar datos.';
          this.modalService.showError('Error', backendMsg);
        });
      }
    });
  }
}
