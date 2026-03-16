import { Estudiante } from './estudiante.model';
import { Administrador } from './administrador.model';

export interface AuthResponse {
  success: boolean;
  message: string;
  estudiante?: Estudiante;
  administrador?: Administrador;
  token?: string;
}
