import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HorarioService {
  private apiUrl = 'http://localhost:8080/api/horarios';

  constructor(private http: HttpClient) { }

  getCarreras(): Observable<string[]> {
    return this.http.get<string[]>('http://localhost:8080/carreras.json');
  }

  uploadHorario(carrera: string, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('carrera', carrera);
    formData.append('file', file);

    return this.http.post(`${this.apiUrl}/upload`, formData, {
      responseType: 'text'
    });
  }
}
