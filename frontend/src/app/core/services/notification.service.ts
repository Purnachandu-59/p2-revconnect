import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

export interface Notification {
  id: number;
  message: string;
  isRead: boolean;
  createdAt: string;
  type: 'LIKE' | 'COMMENT' | 'CONNECTION_REQUEST' | 'CONNECTION_ACCEPTED';
  referenceId: number;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private baseUrl = 'http://51.20.85.38:8080/api/notifications';

  constructor(private http: HttpClient) {}

  getNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.baseUrl}`);
  }

  getUnreadCount(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/unread-count`);
  }

  markAsRead(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/read`, {});
  }
}