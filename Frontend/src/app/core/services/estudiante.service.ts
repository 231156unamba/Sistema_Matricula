import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Estudiante } from '../models';

@Injectable({
  providedIn: 'root'
})
export class EstudianteService {
  private apiUrl = 'http://localhost:8080/api/estudiantes';

  constructor(private http: HttpClient) { }

  obtenerTodos(): Observable<Estudiante[]> {
    return this.http.get<Estudiante[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Estudiante> {
    return this.http.get<Estudiante>(`${this.apiUrl}/${id}`);
  }

  obtenerResumenAcademico(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}/resumen-academico`);
  }

  buscarPorDni(dni: string): Observable<Estudiante> {
    return this.http.get<Estudiante>(`${this.apiUrl}/dni/${dni}`);
  }

  crear(estudiante: Estudiante): Observable<Estudiante> {
    return this.http.post<Estudiante>(this.apiUrl, estudiante);
  }

  actualizar(id: number, estudiante: Estudiante): Observable<Estudiante> {
    return this.http.put<Estudiante>(`${this.apiUrl}/${id}`, estudiante);
  }

  actualizarEstado(id: number, estado: Estudiante['estado']): Observable<Estudiante> {
    return this.http.patch<Estudiante>(`${this.apiUrl}/${id}/estado`, { estado });
  }

  verificarPagoMatricula(id: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${id}/pago-matricula-validado`);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
