import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CursoService {
  private apiUrl = 'http://localhost:8080/api/cursos';

  constructor(private http: HttpClient) { }

  obtenerTodos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  obtenerPorSemestre(semestre: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/semestre/${semestre}`);
  }

  obtenerDisponibles(idEstudiante: number, semestre: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/disponibles/${idEstudiante}/${semestre}`);
  }
}
