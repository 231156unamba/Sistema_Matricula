import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Estudiante } from '../../core/models/estudiante.model';
import { EstudianteService } from '../../core/services/estudiante.service';
import { ModalService } from '../../shared/services/modal.service';
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
  loadingPdf = false;

  menu = [
    { label: 'Historial', icon: 'bi-clock-history' },
    { label: 'Matrícula', icon: 'bi-journal-check' },
    { label: 'Reglamento', icon: 'bi-book' },
    { label: 'Malla curricular', icon: 'bi-diagram-3' },
    { label: 'Horario', icon: 'bi-calendar-week' }
  ];

  private readonly API = 'http://localhost:8080/api/recursos';

  constructor(
    private estudianteService: EstudianteService,
    private modalService: ModalService,
    private router: Router,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
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
      this.router.navigate(['/login-regular']);
      return;
    }

    const id = this.estudiante?.idEstudiante;
    if (!id) {
      this.loading = false;
      this.router.navigate(['/login-regular']);
      return;
    }

    forkJoin({
      estudiante: this.estudianteService.obtenerPorId(id),
      resumen: this.estudianteService.obtenerResumenAcademico(id)
    }).pipe(
      finalize(() => { this.loading = false; this.cdr.detectChanges(); })
    ).subscribe({
      next: (result) => {
        this.estudiante = result.estudiante;
        this.resumen = result.resumen as ResumenAcademico;
        this.cdr.detectChanges();
      },
      error: () => this.cdr.detectChanges()
    });
  }

  onMenuClick(label: string): void {
    switch (label) {
      case 'Horario': this.abrirPdf('horario'); break;
      case 'Malla curricular': this.abrirPdf('malla'); break;
      case 'Reglamento': this.abrirPdf('reglamento'); break;
      case 'Matrícula': this.verificarYEntrarMatricula(); break;
    }
  }

  abrirPdf(tipo: 'horario' | 'malla' | 'reglamento'): void {
    if ((tipo === 'horario' || tipo === 'malla') && !this.estudiante?.carrera) {
      this.modalService.showError('Error', 'No se encontró información de tu carrera.');
      return;
    }

    const carrera = this.normalizarTexto(this.estudiante?.carrera || '');
    const url = tipo === 'reglamento'
      ? `${this.API}/reglamento`
      : `${this.API}/${tipo}/${carrera}`;

    this.loadingPdf = true;
    this.cdr.detectChanges();

    this.http.get(url, { responseType: 'blob' }).pipe(
      finalize(() => { this.loadingPdf = false; this.cdr.detectChanges(); })
    ).subscribe({
      next: (blob) => {
        const blobUrl = URL.createObjectURL(blob);
        window.open(blobUrl, '_blank');
        // Liberar memoria después de abrir
        setTimeout(() => URL.revokeObjectURL(blobUrl), 10000);
      },
      error: () => {
        this.modalService.showError('Archivo no disponible',
          'El administrador aún no ha subido este archivo.');
      }
    });
  }

  verificarYEntrarMatricula(): void {
    if (!this.estudiante?.idEstudiante) return;
    this.loading = true;
    this.estudianteService.verificarPagoMatricula(this.estudiante.idEstudiante)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (pagado) => this.router.navigate([pagado ? '/matricula-regular' : '/login-matricula-regular']),
        error: () => this.router.navigate(['/login-matricula-regular'])
      });
  }

  private normalizarTexto(texto: string): string {
    if (!texto) return '';
    return texto.toLowerCase().trim()
      .normalize('NFD').replace(/[\u0300-\u036f]/g, '')
      .replace(/[^a-z0-9\s]/g, '').replace(/\s+/g, '_');
  }

  get nombreCompleto(): string {
    return `${this.estudiante?.nombres || ''} ${this.estudiante?.apellidos || ''}`.trim();
  }

  get anioIngreso(): string {
    const codigo = this.estudiante?.codigoEstudiante || '';
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

  logout(): void {
    window.localStorage.removeItem('estudiante');
    window.localStorage.removeItem('token');
    window.localStorage.removeItem('matricula_voucher');
    this.router.navigate(['/inicio']);
  }
}
