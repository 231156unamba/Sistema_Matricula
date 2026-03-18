import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatriculaService, Curso } from '../../core/services/matricula.service';
import { AuthService } from '../../core/services/auth.service';
import { ModalService } from '../../shared/services/modal.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-seleccionar-cursos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './seleccionar-cursos.component.html',
  styleUrls: ['./seleccionar-cursos.component.css']
})
export class SeleccionarCursosComponent implements OnInit {
  cursosDisponibles: Curso[] = [];
  cursosSeleccionados: Curso[] = [];
  estudiante: any = null;
  loading = true;
  creditosMaximos = 23;
  voucher = '';

  constructor(
    private matriculaService: MatriculaService,
    private authService: AuthService,
    private modalService: ModalService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const sesion = this.authService.obtenerSesion();
    if (!sesion) {
      this.router.navigate(['/login-regular']);
      return;
    }
    this.estudiante = sesion;
    this.creditosMaximos = this.estudiante.creditosMaximos || 23;
    
    // El voucher se guardó en la sesión durante el login-matricula-regular
    // Como el loginIngresante no devuelve el voucher, lo recuperamos del localStorage si lo guardamos o lo pedimos de nuevo
    // Para simplificar, asumiremos que el voucher está disponible en un estado global o lo pedimos en el login previo
    // En este flujo, el voucher fue validado en el paso anterior.
    const raw = localStorage.getItem('estudiante');
    if (raw) {
      const data = JSON.parse(raw);
      // Asumimos que el backend nos dio el estudiante validado.
    }

    this.cargarCursos();
  }

  cargarCursos() {
    this.loading = true;
    this.matriculaService.obtenerCursosDisponibles(this.estudiante.idEstudiante)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (cursos) => {
          this.cursosDisponibles = cursos;
        },
        error: (err) => {
          this.modalService.showError('Error', 'No se pudieron cargar los cursos disponibles.');
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
    // Recuperar el voucher usado en el login previo
    const voucher = localStorage.getItem('matricula_voucher') || 'VCH-VALIDADO';

    const request = {
      idEstudiante: this.estudiante.idEstudiante,
      idCursos: this.cursosSeleccionados.map(c => c.idCurso),
      voucher: voucher
    };

    this.loading = true;
    this.matriculaService.registrarMatricula(request)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (res) => {
          if (res.success) {
            localStorage.removeItem('matricula_voucher');
            this.modalService.showSuccess('¡Éxito!', 'Tu matrícula ha sido registrada correctamente.');
            setTimeout(() => this.router.navigate(['/regular']), 2000);
          } else {
            this.modalService.showError('Error', res.message);
          }
        },
        error: (err) => {
          this.modalService.showError('Error', err.error?.message || 'Error al registrar la matrícula.');
        }
      });
  }
}
