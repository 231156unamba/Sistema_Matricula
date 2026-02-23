import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ModalComponent } from '../../shared/components/modal/modal.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalComponent],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  dni = '';
  voucher = '';
  loading = false;
  
  modalOpen = false;
  modalTitle = '';
  modalMessage = '';
  modalType: 'success' | 'error' = 'success';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit() {
    if (!this.dni || !this.voucher) {
      this.showModal('Error', 'Por favor complete todos los campos', 'error');
      return;
    }

    this.loading = true;
    this.authService.loginIngresante(this.dni, this.voucher).subscribe({
      next: (response) => {
        this.loading = false;
        if (response.success) {
          if (response.estudiante) {
            this.authService.guardarSesion(response.estudiante);
            this.showModal('Éxito', 'Login exitoso', 'success');
            setTimeout(() => {
              this.router.navigate(['/matricula-ingresante']);
            }, 1500);
          } else {
            this.router.navigate(['/registro-ingresante'], {
              queryParams: { dni: this.dni, voucher: this.voucher }
            });
          }
        } else {
          this.showModal('Error', response.message, 'error');
        }
      },
      error: (error) => {
        this.loading = false;
        this.showModal('Error', 'Error al iniciar sesión. Verifique sus datos.', 'error');
      }
    });
  }

  showModal(title: string, message: string, type: 'success' | 'error') {
    this.modalTitle = title;
    this.modalMessage = message;
    this.modalType = type;
    this.modalOpen = true;
  }

  closeModal() {
    this.modalOpen = false;
  }
}

