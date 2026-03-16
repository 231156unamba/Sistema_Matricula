import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { ModalService } from './shared/services/modal.service';
import { GlobalModalComponent } from './shared/components/global-modal/global-modal.component';

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule, RouterLink, RouterOutlet, GlobalModalComponent],
  templateUrl: './app.html',
  styles: []
})
export class App {
  title = 'Sistema de Matrículas UNAMBA';
  
  constructor(
    private router: Router,
    private modalService: ModalService
  ) {}
  
  // Estado de la aplicación
  currentPage = 'inicio';
  currentUser = null;
  loading = false;
  
  // Formularios
  ingresanteForm = {
    dni: '',
    voucher: '',
    nombres: '',
    apellidos: '',
    email: ''
  };
  
  regularForm = {
    dni: '',
    codigo: ''
  };
  
  adminForm = {
    usuario: '',
    password: ''
  };
  
  // Estado para el modal
  showMatriculaModalFlag = false;
  
  // Métodos de navegación
  isActiveRoute(route: string): boolean {
    return this.router.url === route;
  }
  
  showMatriculaModal() {
    this.showMatriculaModalFlag = true;
  }
  
  selectMatriculaType(type: 'ingresante' | 'regular') {
    this.showMatriculaModalFlag = false;
    if (type === 'ingresante') {
      this.router.navigate(['/login-ingresante']);
    } else {
      this.router.navigate(['/login-regular']);
    }
  }
  
  logout() {
    this.currentUser = null;
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('estudiante');
      localStorage.removeItem('token');
    }
    this.router.navigate(['/inicio']);
    this.showNotification('Sesión cerrada correctamente', 'info');
  }
  
  showNotification(message: string, type: 'success' | 'error' | 'warning' | 'info' = 'info') {
    this.modalService.show({
      title: type.charAt(0).toUpperCase() + type.slice(1),
      message: message,
      type: type,
      showCancel: false,
      confirmText: 'OK'
    });
  }
}
