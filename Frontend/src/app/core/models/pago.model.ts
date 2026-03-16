import { Estudiante } from './estudiante.model';

export interface Pago {
  idPago: number;
  estudiante: Estudiante;
  tipoPago: 'MATRICULA' | 'EXAMEN' | 'CERTIFICADO' | 'OTRO';
  voucher: string;
  monto: number;
  fechaPago: Date;
  validado: boolean;
}
