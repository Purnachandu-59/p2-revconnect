import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.services';

@Component({
  selector: 'app-forgot-security',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule], 
  templateUrl: './forgot-security.html',
  styleUrls: ['./forgot-security.css']
})
export class ForgotSecurity {

  username = '';
  securityQuestion = '';
  answer = '';
  newPassword = '';

  step = 1;

  message = '';
  error = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef 
  ) {}

  fetchQuestion() {

  if (!this.username.trim()) {
    this.error = "Username is required.";
    return;
  }

  this.error = '';
  this.message = '';

  this.authService.getSecurityQuestion(this.username.trim())
    .subscribe({
      next: (res: string) => {

        console.log("Received question:", res);

        this.securityQuestion = res;
        this.step = 2;   

      },
     error: (err) => {

          console.log("Username error:", err);

          if (err.status === 404) {
            this.error = "User not found.";
          } else {
            this.error =
              err?.error?.message || "Wrong Username..";
          }

          this.cdr.detectChanges();
        }
    });
}

  resetPassword() {

  if (!this.answer.trim() || !this.newPassword.trim()) {
    this.error = "All fields are required.";
    return;
  }

  this.error = '';
  this.message = '';

  this.authService.resetBySecurity({
    username: this.username.trim(),
    answer: this.answer.trim(),
    newPassword: this.newPassword.trim()
  }).subscribe({
    next: (res: string) => {

      console.log("Reset Success:", res);

      this.error = '';
      this.message = res; 

    
    setTimeout(() => {
  this.router.navigate(['/'], {
    queryParams: { reset: 'success' }
  });
}, 2000);
    },
   error: (err) => {

  console.log("Reset error:", err);

  this.error = err?.error?.message || "Incorrect security answer.";

  this.cdr.detectChanges();  
}
  });
}
}