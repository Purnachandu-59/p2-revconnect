import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Comment {
  id: string;
  content: string;
  authorId: number;
  authorName: string;
  authorAvatar: string;
  createdAt: string;
}

export interface Post {
  id: string;
  content: string;
  imageUrl: string;
  authorId: number;
  authorName: string;
  authorAvatar: string;
  likes: number;
  createdAt: string;

  likedByCurrentUser: boolean;  

  comments?: Comment[];
}

export interface FeedResponse {
  content: Post[];
  totalPages: number;
  totalElements: number;
  last: boolean;
}

@Injectable({ providedIn: 'root' })
export class PostService {

  private baseUrl = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) {}

  
  getFeed(page: number, size: number): Observable<FeedResponse> {
    return this.http.get<FeedResponse>(
      `${this.baseUrl}/feed?page=${page}&size=${size}`
    );
  }

 
  createPost(data: { content: string; imageUrl?: string }): Observable<Post> {
    return this.http.post<Post>(this.baseUrl, data);
  }

  toggleLike(postId: string): Observable<Post> {
    return this.http.post<Post>(
      `${this.baseUrl}/${postId}/like`,
      {}
    );
  }

  
  addComment(postId: string, content: string): Observable<Comment> {
    return this.http.post<Comment>(
      `${this.baseUrl}/${postId}/comment`,
      { content }
    );
  }

  
  getComments(postId: string): Observable<Comment[]> {
    return this.http.get<Comment[]>(
      `${this.baseUrl}/${postId}/comments`
    );
  }

 
  deletePost(postId: string): Observable<void> {
    return this.http.delete<void>(
      `${this.baseUrl}/${postId}`
    );
  }

  getMyPosts(page: number, size: number) {
  return this.http.get<any>(
    `${this.baseUrl}/me?page=${page}&size=${size}`
  );
}

getPostsByUser(userId: number, page: number, size: number) {
  return this.http.get<any>(
    `http://localhost:8080/api/posts/user/${userId}?page=${page}&size=${size}`
  );
}
}