import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';

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

  
  private baseUrl = 'http://localhost:8080/api';

  user = signal<User | null>(null);

  // USER 

  loadUser() {
    this.http.get<User>(`${this.baseUrl}/users/me`)
      .subscribe({
        next: (data) => this.user.set(data),
        error: (err) => console.error('User load failed', err)
      });
  }

  updateUser(updated: Partial<User>) {
    return this.http.put<User>(`${this.baseUrl}/users/me`, updated);
  }

  getUserById(id: number) {
    return this.http.get<User>(`${this.baseUrl}/users/${id}`);
  }

  

  searchUsers(query: string) {
    return this.http.get<User[]>(
      `${this.baseUrl}/users/search?query=${query}`
    );
  }



  discoverUsers() {
    return this.http.get<User[]>(`${this.baseUrl}/users/discover`);
  }

  

  sendRequest(receiverId: number) {
    return this.http.post(
      `${this.baseUrl}/connections/request/${receiverId}`,
      {}
    );
  }

  getConnections() {
    return this.http.get<User[]>(
      `${this.baseUrl}/connections`
    );
  }

  getPendingRequests() {
    return this.http.get<any[]>(
      `${this.baseUrl}/connections/pending`
    );
  }

  acceptRequest(id: number) {
    return this.http.put(
      `${this.baseUrl}/connections/accept/${id}`,
      {}
    );
  }

  rejectRequest(id: number) {
    return this.http.put(
      `${this.baseUrl}/connections/reject/${id}`,
      {}
    );
  }

}