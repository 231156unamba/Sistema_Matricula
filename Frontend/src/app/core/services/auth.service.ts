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

  loginMatriculaRegular(dni: string, voucher: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login/matricula-regular`, {
      identificador: dni,
      voucher: voucher
    });
  }

  loginAdmin(usuario: string, clave: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login/admin`, {
      identificador: usuario,
      voucher: clave
    });
  }

  guardarSesion(response: AuthResponse) {
    if (response.estudiante) {
      localStorage.setItem('estudiante', JSON.stringify(response.estudiante));
    }
    if (response.administrador) {
      localStorage.setItem('adminSession', JSON.stringify(response.administrador));
    }
    if (response.token) {
      localStorage.setItem('token', response.token);
    }
  }

  obtenerSesion() {
    if (typeof localStorage === 'undefined') return null;
    const data = localStorage.getItem('estudiante');
    return data ? JSON.parse(data) : null;
  }

  isLoggedIn(): boolean {
    if (typeof localStorage === 'undefined') return false;
    return !!localStorage.getItem('token');
  }

  isRegularStudent(): boolean {
    const sesion = this.obtenerSesion();
    return sesion && sesion.tipo === 'REGULAR';
  }

  cerrarSesion() {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('estudiante');
      localStorage.removeItem('adminSession');
      localStorage.removeItem('token');
    }
  }
}
