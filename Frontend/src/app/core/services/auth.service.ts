import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthResponse } from '../models/auth-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  loginIngresante(dni: string, voucher: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login/ingresante`, {
      identificador: dni,
      voucher: voucher
    });
  }

  loginRegular(dni: string, codigo: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login/regular`, {
      identificador: dni,
      voucher: codigo
    });
  }

  guardarSesion(response: AuthResponse) {
    if (response.estudiante) {
      localStorage.setItem('estudiante', JSON.stringify(response.estudiante));
    }
    if (response.token) {
      localStorage.setItem('token', response.token);
    }
  }

  obtenerSesion() {
    const data = localStorage.getItem('estudiante');
    return data ? JSON.parse(data) : null;
  }

  cerrarSesion() {
    localStorage.removeItem('estudiante');
    localStorage.removeItem('token');
  }
}
