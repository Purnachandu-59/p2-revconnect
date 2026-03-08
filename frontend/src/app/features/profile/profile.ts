import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { UserService } from '../../core/services/user.service';
import { PostService, Post } from '../../core/services/post.service';
import { Navbar } from '../navbar/navbar';
import { PostCard } from '../post-card/post-card';
import { ConnectionService, ConnectionUser } from '../../core/services/connection.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, Navbar, PostCard, RouterModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.css'],
})
export class Profile implements OnInit {

  connectionService = inject(ConnectionService);
  userService = inject(UserService);
  postService = inject(PostService);
  router = inject(Router);
  route = inject(ActivatedRoute);

  profileUser: any = null;

  editMode = signal(false);

  activeTab: 'posts' | 'about' | 'connections' = 'posts';

  posts: Post[] = [];
  connections: ConnectionUser[] = [];

  sentRequests: number[] = [];
  connectedUserIds: number[] = [];

  page = 0;

  viewingOtherUser = false;
  viewedUserId: number | null = null;

  ngOnInit() {

    
    this.connectionService.getMyConnections().subscribe(res => {
      this.connectedUserIds = res.map(u => u.userId);
    });

    this.connectionService.getSentRequests().subscribe(res => {
      this.sentRequests = res;
    });

    this.route.paramMap.subscribe(params => {

      const paramId = params.get('id');

      if (paramId) {

        this.viewingOtherUser = true;
        this.viewedUserId = Number(paramId);

        this.userService.getUserById(this.viewedUserId)
          .subscribe(user => this.profileUser = user);

        this.loadUserPosts(this.viewedUserId);
        this.loadUserConnections(this.viewedUserId);

      } else {

        this.viewingOtherUser = false;

        this.userService.loadUser();
        const user = this.userService.user();
        if (user) this.profileUser = user;

        this.loadMyPosts();
        this.loadMyConnections();
      }
    });
  }

  

  loadMyPosts() {
    this.postService.getMyPosts(this.page, 10)
      .subscribe(res => this.posts = res.content);
  }

  loadUserPosts(userId: number) {
    this.postService.getPostsByUser(userId, this.page, 10)
      .subscribe(res => this.posts = res.content);
  }

  

  loadMyConnections() {
    this.connectionService.getMyConnections()
      .subscribe(data => this.connections = data);
  }

  loadUserConnections(userId: number) {
    this.connectionService.getConnectionsByUser(userId)
      .subscribe(data => this.connections = data);
  }



  like(postId: string) {
    this.postService.toggleLike(postId).subscribe(updated => {
      const index = this.posts.findIndex(p => p.id === postId);
      if (index !== -1) this.posts[index] = updated;
    });
  }

  addComment(event: { postId: string; content: string }) {
    this.postService.addComment(event.postId, event.content)
      .subscribe(comment => {
        const post = this.posts.find(p => p.id === event.postId);
        if (post) post.comments = [...(post.comments || []), comment];
      });
  }

  deletePost(postId: string) {
    if (this.viewingOtherUser) return;

    this.postService.deletePost(postId)
      .subscribe(() => {
        this.posts = this.posts.filter(p => p.id !== postId);
      });
  }

 

  enableEdit() {
    if (this.viewingOtherUser) return;
    this.editMode.set(true);
  }

  save() {
    if (this.viewingOtherUser) return;

    this.userService.updateUser(this.profileUser)
      .subscribe(updatedUser => {
        this.userService.user.set(updatedUser);
        this.editMode.set(false);
      });
  }

  cancel() {
    this.editMode.set(false);
  }

  

  isConnected(userId: number): boolean {
    return this.connectedUserIds.includes(userId);
  }

  isRequestSent(userId: number): boolean {
    return this.sentRequests.includes(userId);
  }

  sendConnectionRequest() {
    if (!this.viewedUserId) return;

    this.connectionService.sendRequest(this.viewedUserId)
      .subscribe(() => {
        this.sentRequests.push(this.viewedUserId!);
      });
  }
}