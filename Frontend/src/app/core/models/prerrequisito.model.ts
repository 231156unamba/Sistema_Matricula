import { Curso } from './curso.model';

export interface Prerrequisito {
  id: number;
  curso: Curso;
  cursoPrerrequisito: Curso;
}
