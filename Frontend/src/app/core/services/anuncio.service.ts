import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AnuncioService {
  private apiUrl = 'http://localhost:8080/api/anuncios';

  constructor(private http: HttpClient) { }

  obtenerActivos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/activos`);
  }

  obtenerTodos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  crear(anuncio: any): Observable<any> {
    return this.http.post(this.apiUrl, anuncio);
  }

  actualizar(id: number, anuncio: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, anuncio);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
