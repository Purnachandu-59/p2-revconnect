import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

export interface ConnectionUser {
  userId: number;
  username: string;
  bio: string;
  profileImage: string;
}

export interface PendingRequest {
  connectionId: number;
  senderId: number;
  senderUsername: string;
  senderProfileImage: string;
}

@Injectable({
  providedIn: 'root'
})
export class ConnectionService {

  private baseUrl = 'http://51.20.85.38:8080/api/connections';

  constructor(private http: HttpClient) {}

  sendRequest(receiverId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/request/${receiverId}`, {});
  }

  acceptRequest(connectionId: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/accept/${connectionId}`, {});
  }

  rejectRequest(connectionId: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/reject/${connectionId}`, {});
  }

  getPending(): Observable<PendingRequest[]> {
    return this.http.get<PendingRequest[]>(`${this.baseUrl}/pending`);
  }

  getMyConnections(): Observable<ConnectionUser[]> {
    return this.http.get<ConnectionUser[]>(`${this.baseUrl}/my`);
  }

  getSentRequests(): Observable<number[]> {
    return this.http.get<number[]>(`${this.baseUrl}/sent`);
  }

  getConnectionsByUser(userId: number): Observable<ConnectionUser[]> {
    return this.http.get<ConnectionUser[]>(`${this.baseUrl}/user/${userId}`);
  }
}