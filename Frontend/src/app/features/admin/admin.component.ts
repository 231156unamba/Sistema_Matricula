import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ModalComponent } from '../../shared/components/modal/modal.component';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalComponent, RouterLink],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent {
  usuario = '';
  password = '';
  loading = false;
  
  modalOpen = false;
  modalTitle = '';
  modalMessage = '';
  modalType: 'success' | 'error' = 'success';

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  onSubmit() {
    if (!this.usuario || !this.password) {
      this.showModal('Error', 'Por favor complete todos los campos', 'error');
      return;
    }

    this.loading = true;
    
    this.authService.loginAdmin(this.usuario, this.password).subscribe({
      next: (response) => {
        this.loading = false;
        if (response.success) {
          this.authService.guardarSesion(response);
          this.showModal('Éxito', 'Acceso administrativo concedido', 'success');
          setTimeout(() => {
            this.router.navigate(['/admin/dashboard']);
          }, 1500);
        } else {
          this.showModal('Error', response.message || 'Credenciales incorrectas', 'error');
        }
      },
      error: (err) => {
        this.loading = false;
        this.showModal('Error', err.error?.message || 'Error en el servidor', 'error');
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
