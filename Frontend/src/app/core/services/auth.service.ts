import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  loginIngresante(dni: string, voucher: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login/ingresante`, {
      identificador: dni,
      voucher: voucher
    });
  }

  loginRegular(codigo: string, voucher: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login/regular`, {
      identificador: codigo,
      voucher: voucher
    });
  }

  guardarSesion(estudiante: any) {
    localStorage.setItem('estudiante', JSON.stringify(estudiante));
  }

  obtenerSesion() {
    const data = localStorage.getItem('estudiante');
    return data ? JSON.parse(data) : null;
  }

  cerrarSesion() {
    localStorage.removeItem('estudiante');
  }
}
