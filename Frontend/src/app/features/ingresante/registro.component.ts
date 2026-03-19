import { Component, OnInit, NgZone, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ModalService } from '../../shared/services/modal.service';

interface CursoIngresante {
  idCurso: number;
  codigoCurso: string;
  nombre: string;
  creditos: number;
  semestre: number;
}

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent implements OnInit {
  // Datos del formulario
  nombres = '';
  apellidos = '';
  declaracionJurada = '';
  certificadoEstudios = '';
  boletaMatricula = '';
  boletaExamen = '';
  pagoCentroMedico = '';
  hojaMatricula = '';

  loading = false;
  // Fase: 'formulario' | 'cursos'
  fase: 'formulario' | 'cursos' = 'formulario';
  cursosAsignados: CursoIngresante[] = [];
  estudiante: any = null;

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
      this.router.navigate(['/login-ingresante']);
      return;
    }
    this.estudiante = sesion;
    // Pre-llenar nombres y apellidos si ya vienen en la sesión
    if (sesion.nombres) this.nombres = sesion.nombres;
    if (sesion.apellidos) this.apellidos = sesion.apellidos;

    // Si ya tiene idEstudiante asignado (ya se matriculó antes), mostrar cursos directamente
    if (sesion.idEstudiante && sesion.tipo === 'INGRESANTE') {
      const voucher = localStorage.getItem('ingresante_voucher');
      // Si no hay voucher pendiente, es porque ya se matriculó — mostrar cursos
      if (!voucher) {
        this.cargarCursosIngresante(sesion.idEstudiante);
      }
    }
  }

  onSubmit(): void {
    if (!this.nombres || !this.apellidos) {
      this.modalService.showError('Error', 'Nombres y apellidos son obligatorios.');
      return;
    }
    if (!this.declaracionJurada || !this.certificadoEstudios || !this.boletaMatricula ||
        !this.boletaExamen || !this.pagoCentroMedico || !this.hojaMatricula) {
      this.modalService.showError('Error', 'Todos los documentos son obligatorios.');
      return;
    }

    const voucher = localStorage.getItem('ingresante_voucher') || '';
    if (!voucher) {
      this.modalService.showError('Error', 'No se encontró el voucher. Vuelve a iniciar sesión.');
      this.router.navigate(['/login-ingresante']);
      return;
    }

    const body = JSON.stringify({
      dni: this.estudiante.dni,
      nombres: this.nombres,
      apellidos: this.apellidos,
      voucher,
      declaracionJurada: this.declaracionJurada,
      certificadoEstudios: this.certificadoEstudios,
      boletaMatricula: this.boletaMatricula,
      boletaExamen: this.boletaExamen,
      pagoCentroMedico: this.pagoCentroMedico,
      hojaMatricula: this.hojaMatricula
    });

    const token = localStorage.getItem('token') || '';
    this.loading = true;
    this.cdr.detectChanges();

    fetch(`${this.API}/ingresante`, {
      method: 'POST',
      headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
      body
    })
      .then(async res => {
        const data = await res.json();
        this.ngZone.run(() => {
          this.loading = false;
          if (res.ok && data.success) {
            localStorage.removeItem('ingresante_voucher');
            // Cargar cursos del primer semestre
            this.cargarCursosIngresante(data.matricula?.estudiante?.idEstudiante || this.estudiante.idEstudiante);
          } else {
            const msg = data?.message || Object.values(data || {}).join(', ') || 'Error al registrar matrícula.';
            this.modalService.showError('Error', msg);
          }
          this.cdr.detectChanges();
        });
      })
      .catch(() => {
        this.ngZone.run(() => {
          this.loading = false;
          this.modalService.showError('Error', 'Error de conexión al registrar la matrícula.');
          this.cdr.detectChanges();
        });
      });
  }

  private cargarCursosIngresante(idEstudiante: number): void {
    const token = localStorage.getItem('token') || '';
    fetch(`${this.API}/cursos-ingresante/${idEstudiante}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    })
      .then(res => res.json())
      .then((cursos: CursoIngresante[]) => {
        this.ngZone.run(() => {
          this.cursosAsignados = cursos;
          this.fase = 'cursos';
          this.cdr.detectChanges();
        });
      })
      .catch(() => {
        this.ngZone.run(() => {
          // Aunque falle la carga de cursos, la matrícula fue exitosa
          this.fase = 'cursos';
          this.cdr.detectChanges();
        });
      });
  }

  get totalCreditos(): number {
    return this.cursosAsignados.reduce((s, c) => s + c.creditos, 0);
  }

  irAlInicio(): void {
    this.router.navigate(['/inicio']);
  }
}
