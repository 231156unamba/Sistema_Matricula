import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MatriculaService {
  private apiUrl = 'http://localhost:8080/api/matriculas';

  constructor(private http: HttpClient) { }

  matricularIngresante(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/ingresante`, data);
  }

  matricularRegular(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/regular`, data);
  }
}
