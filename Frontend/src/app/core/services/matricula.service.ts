import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Matricula, MatriculaIngresanteRequest, MatriculaRequest, MatriculaResponse } from '../models';

@Injectable({
  providedIn: 'root'
})
export class MatriculaService {
  private apiUrl = 'http://localhost:8080/api/matriculas';

  constructor(private http: HttpClient) { }

  matricularIngresante(data: MatriculaIngresanteRequest): Observable<MatriculaResponse> {
    return this.http.post<MatriculaResponse>(`${this.apiUrl}/ingresante`, data);
  }

  matricularRegular(data: MatriculaRequest): Observable<MatriculaResponse> {
    return this.http.post<MatriculaResponse>(`${this.apiUrl}/regular`, data);
  }

  obtenerTodos(): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Matricula> {
    return this.http.get<Matricula>(`${this.apiUrl}/${id}`);
  }

  obtenerPorEstudiante(idEstudiante: number): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(`${this.apiUrl}/estudiante/${idEstudiante}`);
  }

  obtenerPorPeriodo(idPeriodo: number): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(`${this.apiUrl}/periodo/${idPeriodo}`);
  }
}
