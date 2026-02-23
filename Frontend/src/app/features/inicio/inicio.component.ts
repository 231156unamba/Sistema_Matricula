import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AnuncioService } from '../../core/services/anuncio.service';

@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.css']
})
export class InicioComponent implements OnInit {
  anuncios: any[] = [];
  loading = true;

  constructor(private anuncioService: AnuncioService) {}

  ngOnInit() {
    this.cargarAnuncios();
  }

  cargarAnuncios() {
    // Cargar datos de ejemplo inmediatamente
    this.anuncios = this.obtenerAnunciosEjemplo();
    this.loading = false;
    
    // Intentar cargar desde el backend en segundo plano (opcional)
    this.anuncioService.obtenerActivos().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.anuncios = data;
        }
      },
      error: (error) => {
        console.log('Backend no disponible, usando datos de ejemplo');
      }
    });
  }

  obtenerAnunciosEjemplo() {
    return [
      {
        idAnuncio: 1,
        titulo: 'Matrícula 2024-I',
        contenido: 'Inicio de matrícula para el semestre 2024-I del 15 al 30 de enero. Todos los estudiantes deben realizar su matrícula en las fechas establecidas.',
        tipo: 'MATRICULA',
        fechaInicio: '2024-01-15T08:00:00',
        fechaFin: '2024-01-30T18:00:00',
        activo: true
      },
      {
        idAnuncio: 2,
        titulo: 'Horario de Atención',
        contenido: 'Oficina de Registros Académicos: Lunes a Viernes de 8:00 AM a 5:00 PM. Sábados de 9:00 AM a 1:00 PM.',
        tipo: 'HORARIO',
        activo: true
      },
      {
        idAnuncio: 3,
        titulo: 'Examen de Admisión 2024',
        contenido: 'Examen de admisión programado para el 10 de febrero 2024. Inscripciones abiertas hasta el 5 de febrero.',
        tipo: 'EXAMEN',
        fechaInicio: '2024-02-10T09:00:00',
        fechaFin: '2024-02-10T13:00:00',
        activo: true
      },
      {
        idAnuncio: 4,
        titulo: 'Ceremonia de Graduación',
        contenido: 'Se invita a todos los egresados a la ceremonia de graduación que se realizará el 20 de marzo en el auditorio principal.',
        tipo: 'EVENTO',
        fechaInicio: '2024-03-20T10:00:00',
        activo: true
      },
      {
        idAnuncio: 5,
        titulo: 'Comunicado Importante',
        contenido: 'Se informa a la comunidad universitaria que el día 15 de febrero no habrá atención por feriado nacional.',
        tipo: 'COMUNICADO',
        activo: true
      }
    ];
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
