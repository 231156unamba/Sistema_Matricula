import { Component, OnInit, NgZone, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { CursoDisponible, InfoMatricula } from '../../core/services/matricula.service';
import { AuthService } from '../../core/services/auth.service';
import { ModalService } from '../../shared/services/modal.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-seleccionar-cursos',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './seleccionar-cursos.component.html',
  styleUrls: ['./seleccionar-cursos.component.css']
})
export class SeleccionarCursosComponent implements OnInit {
  info: InfoMatricula | null = null;
  cursosSeleccionados: CursoDisponible[] = [];
  estudiante: any = null;
  loading = true;
  error = false;

  private readonly API = 'http://localhost:8080/api/matriculas';

  constructor(
    private authService: AuthService,
    private modalService: ModalService,
    private router: Router,
    private ngZone: NgZone,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const sesion = this.authService.obtenerSesion();
    if (!sesion?.idEstudiante) {
      this.loading = false;
      this.router.navigate(['/login-matricula-regular']);
      return;
    }
    this.estudiante = sesion;
    this.cargarInfo();
  }

  cargarInfo(): void {
    this.loading = true;
    this.error = false;

    const token = localStorage.getItem('token') || '';
    const url = `${this.API}/info-matricula/${this.estudiante.idEstudiante}`;

    // Usar fetch nativo para evitar problemas de SSR transfer state con HttpClient
    fetch(url, {
      headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' }
    })
      .then(res => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json();
      })
      .then((data: InfoMatricula) => {
        this.ngZone.run(() => {
          this.info = data;
          this.loading = false;
          this.error = false;
          this.cdr.detectChanges();
        });
      })
      .catch(() => {
        this.ngZone.run(() => {
          this.loading = false;
          this.error = true;
          this.cdr.detectChanges();
          this.modalService.showError('Error', 'No se pudieron cargar los cursos.');
        });
      });
  }

  get cursosDisponibles(): CursoDisponible[] {
    return this.info?.cursosDisponibles || [];
  }

  get creditosMaximos(): number {
    return this.info?.creditosMaximos ?? 23;
  }

  get creditosMinimos(): number {
    return this.info?.creditosMinimos ?? 12;
  }

  get totalCreditos(): number {
    return this.cursosSeleccionados.reduce((s, c) => s + c.creditos, 0);
  }

  get promedio(): number | null {
    return this.info?.promedioSemestreAnterior ?? null;
  }

  isCursoSeleccionado(idCurso: number): boolean {
    return this.cursosSeleccionados.some(c => c.idCurso === idCurso);
  }

  toggleCurso(curso: CursoDisponible): void {
    const idx = this.cursosSeleccionados.findIndex(c => c.idCurso === curso.idCurso);
    if (idx > -1) {
      // Reasignar para que Angular detecte el cambio
      this.cursosSeleccionados = this.cursosSeleccionados.filter(c => c.idCurso !== curso.idCurso);
    } else {
      if (this.totalCreditos + curso.creditos > this.creditosMaximos) {
        this.modalService.showWarning('Límite de créditos',
          `No puedes superar los ${this.creditosMaximos} créditos permitidos.`);
        return;
      }
      this.cursosSeleccionados = [...this.cursosSeleccionados, curso];
    }
    this.cdr.detectChanges();
  }

  confirmarMatricula(): void {
    if (this.cursosSeleccionados.length === 0) {
      this.modalService.showWarning('Atención', 'Debes seleccionar al menos un curso.');
      return;
    }
    if (this.totalCreditos < this.creditosMinimos) {
      this.modalService.showWarning('Mínimo de créditos',
        `Debes seleccionar al menos ${this.creditosMinimos} créditos.`);
      return;
    }
    this.modalService.showConfirmation(
      'Confirmar Matrícula',
      `¿Confirmas tu matrícula en ${this.cursosSeleccionados.length} cursos (${this.totalCreditos} créditos)?`,
      'Confirmar', 'Cancelar'
    ).subscribe((ok: boolean) => { if (ok) this.registrar(); });
  }

  registrar(): void {
    const voucher = localStorage.getItem('matricula_voucher') || '';
    if (!voucher) {
      this.modalService.showError('Error', 'No se encontró el voucher de matrícula. Vuelve a iniciar sesión.');
      this.router.navigate(['/login-matricula-regular']);
      return;
    }

    const body = JSON.stringify({
      idEstudiante: this.estudiante.idEstudiante,
      idCursos: this.cursosSeleccionados.map(c => c.idCurso),
      voucher
    });

    const token = localStorage.getItem('token') || '';
    this.loading = true;
    this.cdr.detectChanges();

    fetch(`${this.API}/regular`, {
      method: 'POST',
      headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
      body
    })
      .then(async res => {
        const data = await res.json();
        this.ngZone.run(() => {
          this.loading = false;
          if (res.ok) {
            localStorage.removeItem('matricula_voucher');
            this.modalService.showSuccess('¡Éxito!', 'Tu matrícula fue registrada correctamente.');
            setTimeout(() => this.router.navigate(['/regular']), 2000);
          } else {
            // Extraer mensaje de error del backend
            let msg = 'No se pudo registrar la matrícula.';
            if (data?.message) {
              msg = data.message;
            } else if (typeof data === 'object') {
              // Puede ser un mapa de errores de validación: { campo: "mensaje" }
              msg = Object.values(data).join(', ');
            }
            this.modalService.showError('Error', msg);
          }
          this.cdr.detectChanges();
        });
      })
      .catch(() => {
        this.ngZone.run(() => {
          this.loading = false;
          this.modalService.showError('Error', 'Error al registrar la matrícula.');
          this.cdr.detectChanges();
        });
      });
  }

  logout(): void {
    localStorage.removeItem('estudiante');
    localStorage.removeItem('token');
    localStorage.removeItem('matricula_voucher');
    this.router.navigate(['/inicio']);
  }
}
