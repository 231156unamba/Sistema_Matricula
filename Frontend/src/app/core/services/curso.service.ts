import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Curso } from '../models';

@Injectable({
  providedIn: 'root'
})
export class CursoService {
  private apiUrl = 'http://localhost:8080/api/cursos';

  constructor(private http: HttpClient) { }

  obtenerTodos(): Observable<Curso[]> {
    return this.http.get<Curso[]>(this.apiUrl);
  }

  obtenerPorSemestre(semestre: number): Observable<Curso[]> {
    return this.http.get<Curso[]>(`${this.apiUrl}/semestre/${semestre}`);
  }

  obtenerDisponibles(idEstudiante: number, semestre: number): Observable<Curso[]> {
    return this.http.get<Curso[]>(`${this.apiUrl}/disponibles/${idEstudiante}/${semestre}`);
  }

  obtenerPorId(id: number): Observable<Curso> {
    return this.http.get<Curso>(`${this.apiUrl}/${id}`);
  }

  crear(curso: Curso): Observable<Curso> {
    return this.http.post<Curso>(this.apiUrl, curso);
  }

  actualizar(id: number, curso: Curso): Observable<Curso> {
    return this.http.put<Curso>(`${this.apiUrl}/${id}`, curso);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
