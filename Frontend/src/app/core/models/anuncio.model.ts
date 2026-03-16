export interface Anuncio {
  idAnuncio: number;
  titulo: string;
  contenido: string;
  tipo: 'MATRICULA' | 'EXAMEN' | 'EVENTO' | 'COMUNICADO' | 'HORARIO';
  fechaInicio?: string;
  fechaFin?: string;
  activo: boolean;
  fechaCreacion?: string;
}
