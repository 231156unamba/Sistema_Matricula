// Exportar todos los modelos del sistema
export type { Estudiante } from './estudiante.model';
export type { Curso } from './curso.model';
export type { Matricula, DetalleMatricula, PeriodoAcademico } from './matricula.model';
export type { Pago } from './pago.model';
export type { Prerrequisito } from './prerrequisito.model';
export type { DocumentoIngresante } from './documento-ingresante.model';
export type { Administrador } from './administrador.model';
export type { Anuncio } from './anuncio.model';
export type { AuthResponse } from './auth-response.model';

import type { Estudiante } from './estudiante.model';
import type { Matricula } from './matricula.model';

// Tipos para requests y responses
export interface LoginRequest {
  identificador: string; // DNI o código de estudiante
  voucher: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  estudiante?: Estudiante;
}

export interface MatriculaRequest {
  idEstudiante: number;
  idCursos: number[];
  voucher: string;
}

export interface MatriculaIngresanteRequest {
  voucher: string;
  dni: string;
  nombres: string;
  apellidos: string;
  declaracionJurada: string;
  certificadoEstudios: string;
  boletaMatricula: string;
  boletaExamen: string;
  pagoCentroMedico: string;
  hojaMatricula: string;
}

export interface MatriculaResponse {
  success: boolean;
  message: string;
  matricula?: Matricula;
}
