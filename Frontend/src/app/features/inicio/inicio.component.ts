import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnuncioService } from '../../core/services/anuncio.service';
import { ModalService } from '../../shared/services/modal.service';

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
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    // Evitar llamadas HTTP en el servidor (SSR) que pueden quedar colgadas
    if (typeof window === 'undefined') {
      this.loading = false;
      return;
    }
    this.cargarAnuncios();
  }

  cargarAnuncios() {
    this.loading = true;
    this.anuncioService.obtenerActivos().subscribe({
      next: (data) => {
        this.anuncios = data || [];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error al cargar anuncios', error);
        this.anuncios = [];
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModalMatricula() {
    this.modalService.showConfirmation(
      '¿Qué tipo de alumno eres?',
      'Selecciona si eres ingresante o alumno regular para continuar con tu matrícula.',
      'Ingresante',
      'Regular'
    );
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
