export interface Administrador {
  idAdmin: number;
  usuario: string;
  nombres: string;
  apellidos: string;
  email?: string;
  rol: 'SUPER_ADMIN' | 'ADMIN' | 'REGISTRADOR';
  activo: boolean;
  fechaCreacion?: Date;
}
