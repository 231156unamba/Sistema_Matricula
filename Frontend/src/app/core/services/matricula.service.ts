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

@Injectable({
  providedIn: 'root'
})
export class MatriculaService {
  private apiUrl = 'http://localhost:8080/api/matriculas';

  constructor(private http: HttpClient) { }

  obtenerCursosDisponibles(idEstudiante: number): Observable<Curso[]> {
    return this.http.get<Curso[]>(`${this.apiUrl}/cursos-disponibles/${idEstudiante}`);
  }

  registrarMatricula(request: { idEstudiante: number, idCursos: number[], voucher: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/regular`, request);
  }
}
