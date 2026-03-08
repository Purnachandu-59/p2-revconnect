import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Navbar } from '../../../features/navbar/navbar';
import { Router } from '@angular/router';
import { UserService, User } from '../../../core/services/user.service';
import { PostCard } from '../../../features/post-card/post-card';
import { PostService, Post } from '../../../core/services/post.service';
import { FormsModule } from '@angular/forms';
import { ConnectionService, ConnectionUser } from '../../../core/services/connection.service';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, Navbar, PostCard],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class Dashboard implements OnInit {

  allTags: string[] = [
  '#AI',
  '#Angular',
  '#Startups',
  '#CyberSecurity',
  '#SpringBoot',
  '#FullStack',
  '#WebDevelopment',
  '#Cloud',
  '#DevOps',
  '#Java',
  '#TypeScript',
  '#MachineLearning',
  '#OpenSource',
  '#React',
  '#Microservices',
  '#Cognizant'
];


trendingTags: string[] = [];

private trendingSubscription!: Subscription;



  connectionService = inject(ConnectionService);
  userService = inject(UserService);
  router = inject(Router);
  postService = inject(PostService);

  connections: ConnectionUser[] = [];

  suggestedUsers: User[] = [];
  connectedUserIds: number[] = [];
  sentRequests: number[] = [];

  selectedImageFile: File | null = null;

  posts: Post[] = [];
  page = 0;
  size = 10;
  newPostContent = '';
  loading = false;
  lastPage = false;

  ngOnInit() {
    this.userService.loadUser();
    this.loadFeed();
    this.loadConnections();
    this.loadSuggestions();
    this.startTrendingRotation();
  }

  


  loadFeed() {
    if (this.loading || this.lastPage) return;

    this.loading = true;

    this.postService.getFeed(this.page, this.size)
      .subscribe({
        next: (res) => {
          this.posts = [...this.posts, ...res.content];
          this.lastPage = res.last;
          this.page++;
          this.loading = false;
        },
        error: () => this.loading = false
      });
  }

  

  loadConnections() {
    this.connectionService.getMyConnections()
      .subscribe(res => {
        this.connections = res;
      });
  }



  loadSuggestions() {

    const currentUserId = this.userService.user()?.id;

    this.connectionService.getMyConnections().subscribe(connections => {

      this.connectedUserIds = connections.map(c => c.userId);

      this.connectionService.getSentRequests().subscribe(sent => {

        this.sentRequests = sent;

        this.userService.discoverUsers().subscribe(users => {

          this.suggestedUsers = users
            .filter(user =>
              user.id !== currentUserId &&
              !this.connectedUserIds.includes(user.id) &&
              !this.sentRequests.includes(user.id)
            )
            .slice(0, 5);

        });

      });

    });
  }

  connectFromSuggestion(userId: number) {

    this.connectionService.sendRequest(userId).subscribe(() => {

      this.suggestedUsers =
        this.suggestedUsers.filter(u => u.id !== userId);

      this.loadSuggestions();
    });
  }

 

  onImageSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedImageFile = file;
    }
  }

  createPost() {

    if (!this.newPostContent.trim() && !this.selectedImageFile) {
      return;
    }

    if (this.selectedImageFile) {

      const reader = new FileReader();

      reader.onload = (e: any) => {

        const base64Image = e.target.result;

        this.postService.createPost({
          content: this.newPostContent || '',
          imageUrl: base64Image
        }).subscribe(created => {

          this.posts.unshift(created);
          this.newPostContent = '';
          this.selectedImageFile = null;
        });
      };

      reader.readAsDataURL(this.selectedImageFile);

    } else {

      this.postService.createPost({
        content: this.newPostContent
      }).subscribe(created => {

        this.posts.unshift(created);
        this.newPostContent = '';
      });
    }
  }

 

  like(postId: string) {
    const index = this.posts.findIndex(p => p.id === postId);
    if (index === -1) return;

    this.postService.toggleLike(postId).subscribe(updated => {
      this.posts[index] = updated;
    });
  }

  addComment(event: { postId: string, content: string }) {
    const post = this.posts.find(p => p.id === event.postId);
    if (!post) return;

    this.postService.addComment(event.postId, event.content)
      .subscribe(comment => {
        if (!post.comments) post.comments = [];
        post.comments.push(comment);
      });
  }

  deletePost(postId: string) {
    this.postService.deletePost(postId)
      .subscribe(() => {
        this.posts = this.posts.filter(p => p.id !== postId);
      });
  }

  goToFeed() {
  this.router.navigate(['/dashboard']);
}

goToNetwork() {
  this.router.navigate(['/network']);
}

goToAnalytics() {
  const role = localStorage.getItem('role');
  if (role === 'CREATOR') {

    this.router.navigate(['/analytics']);

  } else if (role === 'BUSINESS') {

    this.router.navigate(['/business-analytics']);

  }

}

isCreator(): boolean {

  if (typeof window !== 'undefined') {

    return localStorage.getItem('role') === 'CREATOR';

  }

  return false;
}

isBusiness(): boolean {

  if (typeof window !== 'undefined') {
    return localStorage.getItem('role') === 'BUSINESS';

  }

  return false;

}


goToCreatorAnalytics() {

  this.router.navigate(['/analytics']);

}

goToBusinessAnalytics() {

  this.router.navigate(['/business-analytics']);

}

startTrendingRotation() {

  this.generateRandomTags();

  this.trendingSubscription = interval(10000)
    .subscribe(() => {
      this.generateRandomTags();
    });
}

generateRandomTags() {

  const shuffled = [...this.allTags]
    .sort(() => 0.5 - Math.random());

  this.trendingTags = shuffled.slice(0, 4);
}


ngOnDestroy() {
  if (this.trendingSubscription) {
    this.trendingSubscription.unsubscribe();
  }
}
}