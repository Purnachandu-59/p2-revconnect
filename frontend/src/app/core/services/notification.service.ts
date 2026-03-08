import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private baseUrl = 'http://51.20.85.38:8080/api/notifications';

  constructor(private http: HttpClient) {}

  getNotifications() {
    return this.http.get(`${this.baseUrl}`);
  }

  getUnreadCount() {
    return this.http.get(`${this.baseUrl}/unread-count`);
  }

  markAsRead(id: number) {
    return this.http.put(`${this.baseUrl}/${id}/read`, {});
  }
}