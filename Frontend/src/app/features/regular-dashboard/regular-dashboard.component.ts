import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Estudiante } from '../../core/models/estudiante.model';
import { EstudianteService } from '../../core/services/estudiante.service';
import { ModalService } from '../../shared/services/modal.service';
import { RecursoService } from '../../core/services/recurso.service';
import { ModalComponent } from '../../shared/components/modal/modal.component';
import { finalize, forkJoin } from 'rxjs';

type ResumenAcademico = {
  idEstudiante: number;
  cursosAprobados: number;
  creditosAprobados: number;
  creditosMaximos: number;
};

@Component({
  selector: 'app-regular-dashboard',
  standalone: true,
  imports: [CommonModule, ModalComponent],
  templateUrl: './regular-dashboard.component.html',
  styleUrls: ['./regular-dashboard.component.css']
})
export class RegularDashboardComponent implements OnInit {
  estudiante: Estudiante | null = null;
  resumen: ResumenAcademico | null = null;
  loading = true;
  showHorarioModal = false;
  showMallaModal = false;
  showReglamentoModal = false;

  horarioUrl: SafeResourceUrl | null = null;
  mallaUrl: SafeResourceUrl | null = null;
  reglamentoUrl: SafeResourceUrl | null = null;

  imageError = false;
  mallaError = false;
  reglamentoError = false;

  menu = [
    { label: 'Historial', icon: 'bi-clock-history' },
    { label: 'Matrícula', icon: 'bi-journal-check' },
    { label: 'Reglamento', icon: 'bi-book' },
    { label: 'Malla curricular', icon: 'bi-diagram-3' },
    { label: 'Horario', icon: 'bi-calendar-week' }
  ];

  constructor(
    private estudianteService: EstudianteService,
    private modalService: ModalService,
    private recursoService: RecursoService,
    private router: Router,
    private sanitizer: DomSanitizer,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (typeof window === 'undefined') {
      this.loading = false;
      return;
    }

    const raw = window.localStorage.getItem('estudiante');
    if (!raw) {
      this.loading = false;
      this.modalService.showError('Sesión', 'No hay sesión activa. Inicia sesión nuevamente.');
      this.router.navigate(['/login-regular']);
      return;
    }

    try {
      this.estudiante = JSON.parse(raw) as Estudiante;
    } catch {
      this.loading = false;
      window.localStorage.removeItem('estudiante');
      this.modalService.showError('Sesión', 'Sesión inválida. Inicia sesión nuevamente.');
      this.router.navigate(['/login-regular']);
      return;
    }

    const id = this.estudiante?.idEstudiante;
    if (!id) {
      this.loading = false;
      this.modalService.showError('Sesión', 'Falta el ID del estudiante. Inicia sesión nuevamente.');
      this.router.navigate(['/login-regular']);
      return;
    }

    // Refrescamos datos desde BD (evita depender solo del localStorage)
    this.loading = true;
    
    forkJoin({
      estudiante: this.estudianteService.obtenerPorId(id),
      resumen: this.estudianteService.obtenerResumenAcademico(id)
    }).pipe(
      finalize(() => { 
        console.log('Finalizando loading. Estudiante:', this.estudiante, 'Resumen:', this.resumen);
        this.loading = false; 
        if (typeof window !== 'undefined') {
          this.cdr.detectChanges();
        }
      })
    ).subscribe({
      next: (result) => {
        console.log('Datos recibidos:', result);
        this.estudiante = result.estudiante;
        this.resumen = result.resumen as ResumenAcademico;
        console.log('Después de asignar - Estudiante:', this.estudiante, 'Resumen:', this.resumen);
        if (typeof window !== 'undefined') {
          this.cdr.detectChanges();
        }
      },
      error: (error) => {
        console.error('Error cargando datos:', error);
        // Si falla, al menos mostramos la data de sesión que ya tenemos
        if (typeof window !== 'undefined') {
          this.cdr.detectChanges();
        }
      }
    });
  }

  onMenuClick(label: string): void {
    if (label === 'Horario') {
      this.verHorario();
    } else if (label === 'Matrícula') {
      this.router.navigate(['/login-matricula-regular']);
    } else if (label === 'Malla curricular') {
      this.cargarMalla();
    } else if (label === 'Reglamento') {
      this.cargarReglamento();
    } else {
      // Otros menús no implementados aún o con otra lógica
      console.log('Menú clickeado:', label);
    }
  }

  verHorario(): void {
    if (!this.estudiante?.carrera) {
      this.modalService.showError('Horario', 'No se encontró información de tu carrera.');
      return;
    }

    this.imageError = false;
    const carreraNormalizada = this.normalizarTexto(this.estudiante.carrera);
    const url = `http://localhost:8080/horarios/${carreraNormalizada}.pdf`;
    this.horarioUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
    this.showHorarioModal = true;
  }

  cargarMalla() {
    if (!this.estudiante?.carrera) {
      this.modalService.showError('Malla', 'No se encontró información de tu carrera.');
      return;
    }
    this.mallaError = false;
    const carreraNormalizada = this.normalizarTexto(this.estudiante.carrera);
    const url = `http://localhost:8080/mallas/${carreraNormalizada}.pdf`;
    this.mallaUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
    this.showMallaModal = true;
  }

  cargarReglamento() {
    this.reglamentoError = false;
    const url = `http://localhost:8080/reglamento/reglamento_general.pdf`;
    this.reglamentoUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
    this.showReglamentoModal = true;
  }

  private normalizarTexto(texto: string): string {
    return texto
      .toLowerCase()
      .trim()
      .replace(/\s+/g, '_')
      .normalize("NFD")
      .replace(/[\u0300-\u036f]/g, ""); // Quitar tildes
  }
 
   onPdfError(event: any): void {
     this.imageError = true;
     console.error('No se pudo cargar el PDF del horario:', this.horarioUrl);
     this.cdr.detectChanges();
   }

  get nombreCompleto(): string {
    const n = this.estudiante?.nombres || '';
    const a = this.estudiante?.apellidos || '';
    return `${n} ${a}`.trim();
  }

  get anioIngreso(): string {
    const codigo = this.estudiante?.codigoEstudiante || '';
    // Si es formato AASNNNN (ej 231156), AA -> 20AA
    if (codigo.length >= 2) {
      const aa = codigo.substring(0, 2);
      if (/^\d{2}$/.test(aa)) return `20${aa}`;
    }
    if (this.estudiante?.fechaRegistro) {
      const d = new Date(this.estudiante.fechaRegistro as any);
      if (!Number.isNaN(d.getTime())) return String(d.getFullYear());
    }
    return '—';
  }

  logout() {
    if (typeof window !== 'undefined') {
      window.localStorage.removeItem('estudiante');
      window.localStorage.removeItem('token');
      window.localStorage.removeItem('matricula_voucher');
    }
    this.router.navigate(['/inicio']);
  }
}

