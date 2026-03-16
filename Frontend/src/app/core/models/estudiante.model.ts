export interface Estudiante {
  idEstudiante: number;
  codigoEstudiante?: string;
  dni: string;
  nombres: string;
  apellidos: string;
  tipo: 'INGRESANTE' | 'REGULAR';
  estado: 'ACTIVO' | 'INHABILITADO' | 'RETIRADO';
  creditosMaximos: number;
  fechaRegistro?: Date;
}
