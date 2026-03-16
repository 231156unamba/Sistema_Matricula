import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ModalComponent } from '../../shared/components/modal/modal.component';

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
    private router: Router
  ) {}

  onSubmit() {
    if (!this.usuario || !this.password) {
      this.showModal('Error', 'Por favor complete todos los campos', 'error');
      return;
    }

    this.loading = true;
    
    // Simular autenticación administrativa
    setTimeout(() => {
      this.loading = false;
      if (this.usuario === 'admin' && this.password === 'admin123') {
        this.showModal('Éxito', 'Acceso administrativo concedido', 'success');
        setTimeout(() => {
          this.router.navigate(['/dashboard']);
        }, 1500);
      } else {
        this.showModal('Error', 'Credenciales incorrectas', 'error');
      }
    }, 1500);
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
