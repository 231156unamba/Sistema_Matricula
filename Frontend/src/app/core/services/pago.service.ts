import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pago } from '../models';

@Injectable({
  providedIn: 'root'
})
export class PagoService {
  private apiUrl = 'http://localhost:8080/api/pagos';

  constructor(private http: HttpClient) { }

  obtenerTodos(): Observable<Pago[]> {
    return this.http.get<Pago[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Pago> {
    return this.http.get<Pago>(`${this.apiUrl}/${id}`);
  }

  obtenerPorEstudiante(idEstudiante: number): Observable<Pago[]> {
    return this.http.get<Pago[]>(`${this.apiUrl}/estudiante/${idEstudiante}`);
  }

  obtenerPorVoucher(voucher: string): Observable<Pago> {
    return this.http.get<Pago>(`${this.apiUrl}/voucher/${voucher}`);
  }

  obtenerPorValidacion(validado: boolean): Observable<Pago[]> {
    return this.http.get<Pago[]>(`${this.apiUrl}/validados/${validado}`);
  }

  crear(pago: Pago): Observable<Pago> {
    return this.http.post<Pago>(this.apiUrl, pago);
  }

  validarPago(id: number): Observable<Pago> {
    return this.http.patch<Pago>(`${this.apiUrl}/${id}/validar`, {});
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
