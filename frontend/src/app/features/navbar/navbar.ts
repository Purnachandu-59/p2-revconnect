import {
  Component,
  signal,
  inject,
  HostListener,
  OnInit,
  OnDestroy
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { debounceTime, Subject, interval, Subscription } from 'rxjs';
import { UserService } from '../../core/services/user.service';
import { NotificationService, Notification } from '../../core/services/notification.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule
  ],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class Navbar implements OnInit, OnDestroy {

  router = inject(Router);
  userService = inject(UserService);
  notificationService = inject(NotificationService);

  

 

  role = signal<string | null>(null);

  isCreator = signal(false);

  isBusiness = signal(false);

 

  darkMode = signal(false);
  notifications = signal(0);

  showDropdown = false;
  showNotif = false;
  isScrolled = false;

  notificationsList: Notification[] = [];

  private pollingSub?: Subscription;

  

  searchTerm = '';
  searchResults: any[] = [];
  showSearchResults = false;

  private searchSubject = new Subject<string>();

  constructor() {

    this.searchSubject
      .pipe(debounceTime(300))
      .subscribe(value => {

        if (value.trim().length > 0) {
          this.userService.searchUsers(value)
            .subscribe(res => {
              this.searchResults = res;
              this.showSearchResults = true;
            });
        } else {
          this.searchResults = [];
          this.showSearchResults = false;
        }
      });
  }

  ngOnInit(): void {

    

    const storedRole = localStorage.getItem('role');

    this.role.set(storedRole);
    this.isCreator.set(storedRole === 'CREATOR');

    this.isBusiness.set(storedRole === 'BUSINESS');
    this.loadNotifications();

  
    this.pollingSub = interval(5000).subscribe(() => {
      this.loadNotifications();
    });
  }

  ngOnDestroy(): void {
    this.pollingSub?.unsubscribe();
  }

  isCreatorOrBusiness(): boolean {
  if (typeof window !== 'undefined') {
    const role = localStorage.getItem('role');
    return role === 'CREATOR' || role === 'BUSINESS';
  }
  return false;
}

 

  loadNotifications() {
    this.notificationService.getNotifications()
      .subscribe(data => {
        this.notificationsList = data;
      });

    this.notificationService.getUnreadCount()
      .subscribe(count => {
        this.notifications.set(count);
      });
  }

  toggleNotif() {
    this.showNotif = !this.showNotif;
    this.showDropdown = false;

    if (this.showNotif) {
      this.loadNotifications();
    }
  }

  handleNotificationClick(notification: Notification) {

    if (!notification.isRead) {
      this.notificationService.markAsRead(notification.id)
        .subscribe(() => {
          notification.isRead = true;
          this.notifications.update(n => n > 0 ? n - 1 : 0);
        });
    }

    this.showNotif = false;

    
    if (notification.type === 'LIKE' || notification.type === 'COMMENT') {
      this.router.navigate(['/dashboard']);
    }

    if (notification.type === 'CONNECTION_REQUEST'
        || notification.type === 'CONNECTION_ACCEPTED') {
      this.router.navigate(['/network']);
    }
  }

  

  onSearchChange(value: string) {
    if (!value.trim()) {
      this.searchResults = [];
      this.showSearchResults = false;
      return;
    }
    this.searchSubject.next(value);
  }

  goToUser(id: number) {
    this.router.navigate(['/profile', id]);
    this.showSearchResults = false;
    this.searchTerm = '';
  }

  

  toggleDarkMode() {
    this.darkMode.update(v => !v);
    document.body.classList.toggle('dark');
  }

 

  goToProfile() {
    this.router.navigate(['/profile']);
    this.showDropdown = false;
  }

  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
    this.showNotif = false;
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/']);
  }


  @HostListener('window:scroll')
  onScroll() {
    this.isScrolled = window.scrollY > 10;
  }

 

  @HostListener('document:click', ['$event'])
  handleClickOutside(event: Event) {

    const target = event.target as HTMLElement;

    if (!target.closest('.profile-menu')) {
      this.showDropdown = false;
    }

    if (!target.closest('.notification-wrapper')) {
      this.showNotif = false;
    }

    if (!target.closest('.search-box')) {
      this.showSearchResults = false;
    }
  }
}