import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HorarioService } from '../../core/services/horario.service';
import { ModalService } from '../../shared/services/modal.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  carreras: string[] = [];
  selectedCarrera = '';
  selectedFile: File | null = null;
  loading = false;

  constructor(
    private horarioService: HorarioService,
    private modalService: ModalService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.horarioService.getCarreras().subscribe(carreras => {
      this.carreras = carreras;
      if (this.carreras.length > 0) {
        this.selectedCarrera = this.carreras[0];
      }
    });
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0] ?? null;
  }

  onUpload(): void {
    if (!this.selectedCarrera || !this.selectedFile) {
      this.modalService.showError('Error', 'Por favor, selecciona una carrera y un archivo.');
      return;
    }

    this.loading = true;
    this.horarioService.uploadHorario(this.selectedCarrera, this.selectedFile).subscribe({
      next: (response) => {
        this.loading = false;
        this.modalService.showSuccess('Éxito', response);
        this.selectedFile = null;
        // Reset file input
        const fileInput = document.getElementById('horario-upload') as HTMLInputElement;
        if (fileInput) {
          fileInput.value = '';
        }
      },
      error: (error) => {
        this.loading = false;
        this.modalService.showError('Error', `Error al subir el archivo: ${error.error}`);
      }
    });
  }

  logout(): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('estudiante');
      localStorage.removeItem('token');
    }
    this.router.navigate(['/inicio']);
  }
}