import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  username: string;
  email: string;
  bio: string;
  profileImage: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private http = inject(HttpClient);
  private baseUrl = 'http://51.20.85.38:8080/api';

  user = signal<User | null>(null);

  loadUser() {
    this.http.get<User>(`${this.baseUrl}/users/me`)
      .subscribe({
        next: (data) => this.user.set(data),
        error: (err) => console.error('User load failed', err)
      });
  }

  updateUser(updated: Partial<User>): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/users/me`, updated);
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/users/${id}`);
  }

  searchUsers(query: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/users/search?query=${query}`);
  }

  discoverUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/users/discover`);
  }

  sendRequest(receiverId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/connections/request/${receiverId}`, {});
  }

  getConnections(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/connections`);
  }

  getPendingRequests(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/connections/pending`);
  }

  acceptRequest(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/connections/accept/${id}`, {});
  }

  rejectRequest(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/connections/reject/${id}`, {});
  }
}