import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HorarioService } from '../../core/services/horario.service';
import { RecursoService } from '../../core/services/recurso.service';
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
  activeTab: 'horarios' | 'mallas' | 'reglamento' = 'horarios';

  constructor(
    private horarioService: HorarioService,
    private recursoService: RecursoService,
    private modalService: ModalService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.horarioService.getCarreras().subscribe(carreras => {
      this.carreras = carreras;
      if (this.carreras.length > 0) {
        this.selectedCarrera = this.carreras[0];
      }
      this.cdr.detectChanges();
    });
  }

  setActiveTab(tab: 'horarios' | 'mallas' | 'reglamento') {
    this.activeTab = tab;
    this.selectedFile = null;
    this.cdr.detectChanges();
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file && file.type === 'application/pdf') {
      this.selectedFile = file;
    } else {
      this.modalService.showError('Error', 'Por favor selecciona un archivo PDF válido.');
      event.target.value = '';
    }
  }

  onUpload() {
    if (!this.selectedFile) return;

    this.loading = true;
    this.cdr.detectChanges();

    const upload$ = this.activeTab === 'horarios'
      ? this.horarioService.uploadHorario(this.selectedCarrera, this.selectedFile!)
      : this.activeTab === 'mallas'
      ? this.recursoService.uploadMalla(this.selectedCarrera, this.selectedFile!)
      : this.recursoService.uploadReglamento(this.selectedFile!);

    upload$.subscribe({
      next: () => {
        this.loading = false;
        const message = this.activeTab === 'horarios'
          ? `Horario de ${this.selectedCarrera} actualizado.`
          : this.activeTab === 'mallas'
          ? `Malla curricular de ${this.selectedCarrera} actualizada.`
          : 'Reglamento general actualizado.';
        this.modalService.showSuccess('Éxito', message);
        this.selectedFile = null;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        const errorMsg = this.activeTab === 'horarios'
          ? 'No se pudo actualizar el horario.'
          : this.activeTab === 'mallas'
          ? 'No se pudo actualizar la malla curricular.'
          : 'No se pudo actualizar el reglamento.';
        this.modalService.showError('Error', errorMsg);
        this.cdr.detectChanges();
      }
    });
  }

  logout() {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('estudiante');
      localStorage.removeItem('token');
    }
    this.router.navigate(['/inicio']);
  }
}