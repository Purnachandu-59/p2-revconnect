import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://51.20.85.38:8080/api/auth';

  constructor(private http: HttpClient) {}

  register(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, data);
  }

  login(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, data);
  }

  getSecurityQuestion(username: string): Observable<string> {
    return this.http.post<string>(
      `${this.baseUrl}/security-question`,
      { username },
      { responseType: 'text' as 'json' }
    );
  }

  resetBySecurity(data: any): Observable<string> {
    return this.http.post<string>(
      `${this.baseUrl}/reset-by-security`,
      data,
      { responseType: 'text' as 'json' }
    );
  }
}