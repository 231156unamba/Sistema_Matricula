import { Estudiante } from './estudiante.model';
import { Curso } from './curso.model';

export interface Matricula {
  idMatricula: number;
  estudiante: Estudiante;
  periodo: PeriodoAcademico;
  tipo: 'INGRESANTE' | 'REGULAR' | 'EXTRAORDINARIA';
  fecha: Date;
}

export interface DetalleMatricula {
  idDetalle: number;
  matricula: Matricula;
  curso: Curso;
  vecesLlevado: number;
  notaFinal?: number;
  estado: 'CURSANDO' | 'APROBADO' | 'DESAPROBADO' | 'RETIRADO';
}

export interface PeriodoAcademico {
  idPeriodo: number;
  anio: number;
  semestre: 'PRIMERO' | 'SEGUNDO';
  estado: 'ACTIVO' | 'INACTIVO';
}
