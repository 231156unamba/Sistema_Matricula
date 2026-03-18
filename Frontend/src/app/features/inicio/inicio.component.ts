import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AnuncioService } from '../../core/services/anuncio.service';
import { ModalService } from '../../shared/services/modal.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.css']
})
export class InicioComponent implements OnInit {
  anuncios: any[] = [];
  loading = true;

  constructor(
    private anuncioService: AnuncioService,
    private modalService: ModalService,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.cargarAnuncios();
  }

  cargarAnuncios() {
    // Evitar llamadas HTTP en el servidor (SSR) si es necesario, 
    // pero para anuncios queremos que carguen de inmediato.
    // Si loading se queda en true, es porque la petición no termina o el cambio no se detecta.
    this.loading = true;
    this.anuncioService.obtenerActivos().subscribe({
      next: (data) => {
        this.anuncios = data || [];
        this.loading = false;
        // Forzamos la detección de cambios para que la UI se actualice inmediatamente
        if (typeof window !== 'undefined') {
          this.cdr.detectChanges();
        }
      },
      error: (error) => {
        console.error('Error al cargar anuncios', error);
        this.anuncios = [];
        this.loading = false;
        if (typeof window !== 'undefined') {
          this.cdr.detectChanges();
        }
      }
    });
  }

  abrirModalMatricula() {
    this.modalService.showConfirmation(
      '¿Qué tipo de alumno eres?',
      'Selecciona si eres ingresante o alumno regular para continuar con tu matrícula.',
      'Ingresante',
      'Regular'
    ).subscribe((esIngresante: boolean) => {
      if (esIngresante) {
        this.router.navigate(['/login-ingresante']);
      } else {
        // Enviar directamente a la validación de matrícula (DNI + Voucher)
        this.router.navigate(['/login-matricula-regular']);
      }
    });
  }

  getTipoClass(tipo: string): string {
    const classes: any = {
      'MATRICULA': 'tipo-matricula',
      'EXAMEN': 'tipo-examen',
      'EVENTO': 'tipo-evento',
      'COMUNICADO': 'tipo-comunicado',
      'HORARIO': 'tipo-horario'
    };
    return classes[tipo] || '';
  }

  getTipoIcon(tipo: string): string {
    const icons: any = {
      'MATRICULA': '📝',
      'EXAMEN': '📋',
      'EVENTO': '🎉',
      'COMUNICADO': '📢',
      'HORARIO': '🕐'
    };
    return icons[tipo] || '📌';
  }
}
