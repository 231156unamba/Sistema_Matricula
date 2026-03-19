import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Curso {
  idCurso: number;
  codigoCurso: string;
  nombre: string;
  creditos: number;
  semestre: number;
}

export interface CursoDisponible extends Curso {
  vecesDesaprobado: number;
  esRepeticion: boolean;
}

export interface InfoMatricula {
  cursosDisponibles: CursoDisponible[];
  creditosMinimos: number;
  creditosMaximos: number;
  promedioSemestreAnterior: number | null;
  tieneJaladoDosVeces: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class MatriculaService {
  private apiUrl = 'http://localhost:8080/api/matriculas';

  constructor(private http: HttpClient) { }

  obtenerInfoMatricula(idEstudiante: number): Observable<InfoMatricula> {
    return this.http.get<InfoMatricula>(`${this.apiUrl}/info-matricula/${idEstudiante}`);
  }

  obtenerCursosDisponibles(idEstudiante: number): Observable<Curso[]> {
    return this.http.get<Curso[]>(`${this.apiUrl}/cursos-disponibles/${idEstudiante}`);
  }

  registrarMatricula(request: { idEstudiante: number, idCursos: number[], voucher: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/regular`, request);
  }
}
