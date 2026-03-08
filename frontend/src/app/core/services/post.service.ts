import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
@Injectable({ providedIn: 'root' })
export class PostService {

  private baseUrl = 'http://51.20.85.38:8080/api/posts';

  constructor(private http: HttpClient) {}

  getFeed(page: number, size: number) {
    return this.http.get(`${this.baseUrl}/feed?page=${page}&size=${size}`);
  }

  createPost(data: { content: string; imageUrl?: string }) {
    return this.http.post(`${this.baseUrl}`, data);
  }

  toggleLike(postId: string) {
    return this.http.post(`${this.baseUrl}/${postId}/like`, {});
  }

  addComment(postId: string, content: string) {
    return this.http.post(`${this.baseUrl}/${postId}/comment`, { content });
  }

  getComments(postId: string) {
    return this.http.get(`${this.baseUrl}/${postId}/comments`);
  }

  deletePost(postId: string) {
    return this.http.delete(`${this.baseUrl}/${postId}`);
  }

  getMyPosts(page: number, size: number) {
    return this.http.get(`${this.baseUrl}/me?page=${page}&size=${size}`);
  }

  getPostsByUser(userId: number, page: number, size: number) {
    return this.http.get(
      `${this.baseUrl}/user/${userId}?page=${page}&size=${size}`
    );
  }
}