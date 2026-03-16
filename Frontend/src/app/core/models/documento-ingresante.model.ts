import { Estudiante } from './estudiante.model';

export interface DocumentoIngresante {
  idDocumento: number;
  estudiante: Estudiante;
  declaracionJurada: string;
  certificadoEstudios: string;
  boletaMatricula: string;
  boletaExamen: string;
  pagoCentroMedico: string;
  hojaMatricula: string;
  verificado: boolean;
}
