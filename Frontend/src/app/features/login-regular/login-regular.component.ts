import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ModalComponent } from '../../shared/components/modal/modal.component';

@Component({
  selector: 'app-login-regular',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalComponent, RouterLink],
  templateUrl: './login-regular.component.html',
  styleUrls: ['./login-regular.component.css']
})
export class LoginRegularComponent {
  codigo = '';
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
    if (!this.codigo || !this.voucher) {
      this.showModal('Error', 'Por favor complete todos los campos', 'error');
      return;
    }

    this.loading = true;
    this.authService.loginRegular(this.codigo, this.voucher).subscribe({
      next: (response) => {
        this.loading = false;
        if (response.success) {
          this.authService.guardarSesion(response.estudiante);
          this.showModal('Éxito', 'Login exitoso', 'success');
          setTimeout(() => {
            this.router.navigate(['/matricula-regular']);
          }, 1500);
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
