import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class ConnectionService {

  private baseUrl = 'http://51.20.85.38:8080/api/connections';

  constructor(private http: HttpClient) {}

  sendRequest(receiverId: number) {
    return this.http.post(`${this.baseUrl}/request/${receiverId}`, {});
  }

  acceptRequest(connectionId: number) {
    return this.http.put(`${this.baseUrl}/accept/${connectionId}`, {});
  }

  rejectRequest(connectionId: number) {
    return this.http.put(`${this.baseUrl}/reject/${connectionId}`, {});
  }

  getPending() {
    return this.http.get(`${this.baseUrl}/pending`);
  }

  getMyConnections() {
    return this.http.get(`${this.baseUrl}/my`);
  }

  getSentRequests() {
    return this.http.get(`${this.baseUrl}/sent`);
  }

  getConnectionsByUser(userId: number) {
    return this.http.get(`${this.baseUrl}/user/${userId}`);
  }
}