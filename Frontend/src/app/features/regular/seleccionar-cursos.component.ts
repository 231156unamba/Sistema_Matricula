import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { MatriculaService, Curso } from '../../core/services/matricula.service';
import { AuthService } from '../../core/services/auth.service';
import { ModalService } from '../../shared/services/modal.service';

@Component({
  selector: 'app-seleccionar-cursos',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './seleccionar-cursos.component.html',
  styleUrls: ['./seleccionar-cursos.component.css']
})
export class SeleccionarCursosComponent implements OnInit {
  cursosDisponibles: Curso[] = [];
  cursosSeleccionados: Curso[] = [];
  estudiante: any = null;
  loading = false;
  creditosMaximos = 23;

  constructor(
    private matriculaService: MatriculaService,
    private authService: AuthService,
    private modalService: ModalService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
    if (typeof window === 'undefined') return;

    const sesion = this.authService.obtenerSesion();
    if (!sesion || !sesion.idEstudiante) {
      this.router.navigate(['/login-regular']);
      return;
    }
    this.estudiante = sesion;
    this.creditosMaximos = this.estudiante.creditosMaximos || 23;
    
    this.cargarCursos();
  }

  cargarCursos() {
    if (!this.estudiante?.idEstudiante) {
      console.warn('CargarCursos: No hay ID de estudiante');
      return;
    }

    console.log('Iniciando carga de cursos para estudiante:', this.estudiante.idEstudiante);
    this.loading = true;
    this.cdr.detectChanges();

    this.matriculaService.obtenerCursosDisponibles(this.estudiante.idEstudiante)
      .subscribe({
        next: (cursos) => {
          console.log('Cursos recibidos del servidor:', cursos);
          this.ngZone.run(() => {
            this.cursosDisponibles = cursos || [];
            this.loading = false;
            // Forzar detección de cambios después de actualizar el estado
            this.cdr.markForCheck();
            this.cdr.detectChanges();
            console.log('Estado actualizado: loading=false, cursos count=', this.cursosDisponibles.length);
          });
        },
        error: (err) => {
          this.ngZone.run(() => {
            console.error('Error al cargar cursos:', err);
            this.loading = false;
            this.modalService.showError('Error', 'No se pudieron cargar los cursos disponibles.');
            this.cdr.detectChanges();
          });
        }
      });
  }

  toggleCurso(curso: Curso) {
    const index = this.cursosSeleccionados.findIndex(c => c.idCurso === curso.idCurso);
    if (index > -1) {
      this.cursosSeleccionados.splice(index, 1);
    } else {
      if (this.totalCreditos + curso.creditos > this.creditosMaximos) {
        this.modalService.showWarning('Límite de créditos', `No puedes superar los ${this.creditosMaximos} créditos.`);
        return;
      }
      this.cursosSeleccionados.push(curso);
    }
    this.cdr.detectChanges();
  }

  isCursoSeleccionado(idCurso: number): boolean {
    return this.cursosSeleccionados.some(c => c.idCurso === idCurso);
  }

  get totalCreditos(): number {
    return this.cursosSeleccionados.reduce((sum, c) => sum + c.creditos, 0);
  }

  confirmarMatricula() {
    if (this.cursosSeleccionados.length === 0) {
      this.modalService.showWarning('Atención', 'Debes seleccionar al menos un curso.');
      return;
    }

    this.modalService.showConfirmation(
      'Confirmar Matrícula',
      `¿Estás seguro de matricularte en ${this.cursosSeleccionados.length} cursos con un total de ${this.totalCreditos} créditos?`,
      'Confirmar',
      'Cancelar'
    ).subscribe((result: boolean) => {
      if (result) {
        this.registrar();
      }
    });
  }

  registrar() {
    const voucher = localStorage.getItem('matricula_voucher') || 'VCH-VALIDADO';

    const request = {
      idEstudiante: this.estudiante.idEstudiante,
      idCursos: this.cursosSeleccionados.map(c => c.idCurso),
      voucher: voucher
    };

    this.loading = true;
    this.cdr.detectChanges();

    this.matriculaService.registrarMatricula(request)
      .subscribe({
        next: (res: any) => {
          this.ngZone.run(() => {
            this.loading = false;
            if (res.success) {
              localStorage.removeItem('matricula_voucher');
              this.modalService.showSuccess('¡Éxito!', 'Tu matrícula ha sido registrada correctamente.');
              setTimeout(() => this.router.navigate(['/regular']), 2000);
            } else {
              this.modalService.showError('Error', res.message);
            }
            this.cdr.detectChanges();
          });
        },
        error: (err) => {
          this.ngZone.run(() => {
            this.loading = false;
            this.modalService.showError('Error', err.error?.message || 'Error al registrar la matrícula.');
            this.cdr.detectChanges();
          });
        }
      });
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
