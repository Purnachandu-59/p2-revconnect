import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Post } from '../../core/services/post.service';

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './post-card.html',
  styleUrls: ['./post-card.css']
})
export class PostCard {

  @Input() post!: Post;
  @Input() currentUserId?: number;

  @Output() like = new EventEmitter<string>();
  @Output() commentAdded = new EventEmitter<{ postId: string, content: string }>();
  @Output() postDeleted = new EventEmitter<string>();

  showComments = false;
  newComment = '';
  showMenu = false;

  onLike() {
    this.like.emit(this.post.id);
  }

  toggleComments() {
    this.showComments = !this.showComments;
  }

  submitComment() {
    if (!this.newComment.trim()) return;

    this.commentAdded.emit({
      postId: this.post.id,
      content: this.newComment
    });

    this.newComment = '';
  }

  toggleMenu() {
    this.showMenu = !this.showMenu;
  }

  deletePost() {
    const confirmDelete = confirm("Are you sure you want to delete this post?");
    if (!confirmDelete) return;

    this.postDeleted.emit(this.post.id);
    this.showMenu = false;
  }

 isOwner(): boolean {
  if (!this.currentUserId) return false;
  return Number(this.post.authorId) === Number(this.currentUserId);
}
}