export interface Estudiante {
  idEstudiante: number;
  codigoEstudiante?: string;
  dni: string;
  nombres: string;
  apellidos: string;
  carrera?: string;
  tipo: 'INGRESANTE' | 'REGULAR';
  estado: 'ACTIVO' | 'INHABILITADO' | 'RETIRADO';
  creditosMaximos: number;
  fechaRegistro?: Date;
}
