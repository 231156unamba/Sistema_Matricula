import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Estudiante, Curso, Matricula } from '../models';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiUrl = 'http://localhost:8080/api/reportes';

  constructor(private http: HttpClient) { }

  // Estadísticas
  obtenerEstadisticasEstudiantes(): Observable<Map<string, number>> {
    return this.http.get<Map<string, number>>(`${this.apiUrl}/estudiantes/estadisticas`);
  }

  obtenerEstadisticasMatriculas(): Observable<Map<string, number>> {
    return this.http.get<Map<string, number>>(`${this.apiUrl}/matriculas/estadisticas`);
  }

  obtenerEstadisticasCursos(): Observable<Map<string, number>> {
    return this.http.get<Map<string, number>>(`${this.apiUrl}/cursos/estadisticas`);
  }

  obtenerEstadisticasPagos(): Observable<Map<string, number>> {
    return this.http.get<Map<string, number>>(`${this.apiUrl}/pagos/estadisticas`);
  }

  obtenerEstadisticasAcademicas(): Observable<Map<string, number>> {
    return this.http.get<Map<string, number>>(`${this.apiUrl}/academicas/estadisticas`);
  }

  // Listados específicos
  obtenerEstudiantesPorEstado(): Observable<Estudiante[]> {
    return this.http.get<Estudiante[]>(`${this.apiUrl}/estudiantes/por-estado`);
  }

  obtenerCursosPorSemestre(): Observable<Curso[]> {
    return this.http.get<Curso[]>(`${this.apiUrl}/cursos/por-semestre`);
  }

  obtenerCursosMasDemandados(limite: number = 10): Observable<Curso[]> {
    return this.http.get<Curso[]>(`${this.apiUrl}/cursos/mas-demandados?limite=${limite}`);
  }

  obtenerCursosConMasDesaprobados(limite: number = 10): Observable<Curso[]> {
    return this.http.get<Curso[]>(`${this.apiUrl}/cursos/mas-desaprobados?limite=${limite}`);
  }

  obtenerEstudiantesConMasDesaprobados(limite: number = 10): Observable<Estudiante[]> {
    return this.http.get<Estudiante[]>(`${this.apiUrl}/estudiantes/mas-desaprobados?limite=${limite}`);
  }

  // Reportes por período
  obtenerMatriculasPorPeriodo(): Observable<Map<string, number>> {
    return this.http.get<Map<string, number>>(`${this.apiUrl}/matriculas/por-periodo`);
  }

  obtenerRendimientoAcademico(): Observable<Map<string, any>> {
    return this.http.get<Map<string, any>>(`${this.apiUrl}/rendimiento/academico`);
  }

  obtenerReportePeriodoActual(): Observable<Map<string, any>> {
    return this.http.get<Map<string, any>>(`${this.apiUrl}/periodo-actual`);
  }

  // Dashboard completo
  obtenerDashboard(): Observable<Map<string, any>> {
    return this.http.get<Map<string, any>>(`${this.apiUrl}/dashboard`);
  }
}
