import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Navbar } from '../navbar/navbar';
import { UserService, User } from '../../core/services/user.service';
import {
  ConnectionService,
  PendingRequest,
  ConnectionUser
} from '../../core/services/connection.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-network',
  standalone: true,
  imports: [CommonModule, Navbar, RouterModule],
  templateUrl: './network.html',
  styleUrls: ['./network.css']
})
export class Network implements OnInit {

  private userService = inject(UserService);
  private connectionService = inject(ConnectionService);

  activeTab: 'connections' | 'followers' | 'following' | 'pending' | 'discover' = 'connections';

  discoverUsers: User[] = [];
  connections: ConnectionUser[] = [];
  pending: PendingRequest[] = [];

  sentRequests: number[] = [];
  connectedUserIds: number[] = [];

  defaultImage = 'https://i.pravatar.cc/150?img=1';

  ngOnInit(): void {
    this.loadDiscoverUsers();
    this.loadConnections();
    this.loadPending();
    this.loadSentRequests();
  }

  loadDiscoverUsers(): void {
    this.userService.discoverUsers().subscribe({
      next: res => {
        const currentUserId = Number(localStorage.getItem('userId'));
        this.discoverUsers = res.filter(user => user.id !== currentUserId);
      },
      error: err => console.error('Discover error', err)
    });
  }

  loadConnections(): void {
    this.connectionService.getMyConnections().subscribe({
      next: res => {
        this.connections = res;
        this.connectedUserIds = res.map(u => u.userId);
      },
      error: err => console.error('Connections error', err)
    });
  }

  loadPending(): void {
    this.connectionService.getPending().subscribe({
      next: res => this.pending = res,
      error: err => console.error('Pending error', err)
    });
  }

  loadSentRequests(): void {
    this.connectionService.getSentRequests().subscribe({
      next: res => this.sentRequests = res,
      error: err => console.error('Sent requests error', err)
    });
  }

  connect(userId: number): void {
    this.connectionService.sendRequest(userId).subscribe({
      next: () => {
        this.sentRequests.push(userId);
      },
      error: err => console.error('Send request error', err)
    });
  }

  accept(connectionId: number): void {
    this.connectionService.acceptRequest(connectionId).subscribe({
      next: () => {
        this.loadPending();
        this.loadConnections();
        this.loadSentRequests();
      },
      error: err => console.error('Accept error', err)
    });
  }

  reject(connectionId: number): void {
    this.connectionService.rejectRequest(connectionId).subscribe({
      next: () => this.loadPending(),
      error: err => console.error('Reject error', err)
    });
  }

  setTab(tab: typeof this.activeTab): void {
    this.activeTab = tab;
  }

  isConnected(userId: number): boolean {
    return this.connectedUserIds.includes(userId);
  }

  isRequestSent(userId: number): boolean {
    return this.sentRequests.includes(userId);
  }
}