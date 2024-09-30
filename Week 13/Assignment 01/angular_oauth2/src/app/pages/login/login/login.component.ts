import { Component, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth/auth.service';
import { User } from '../../../models/user.model';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  standalone: true,  // Important! This marks the component as standalone.
  templateUrl: './login.component.html',
  styleUrls: [],
  providers: [
    AuthService
  ], // Services can be provided directly in the component
  imports: [FormsModule, CommonModule] // Import necessary modules directly into the component
})
export class LoginComponent {
  // constants
  mainbg: string = '../assets/img/background.jpg';
  user: User = { username: '', password: '' };

  @ViewChild(ToastContainerDirective, { static: true })
  toastContainer!: ToastContainerDirective;

  constructor(private authService: AuthService, private router: Router, private toastrService: ToastrService) {}

  ngOnInit() {
    this.toastrService.overlayContainer = this.toastContainer;
    this.processLogin();
  }

  login() {
    this.authService.login(this.user).subscribe({
      next: (response) => {
        if (response.token) {
          this.authService.setToken(response.token);
          this.toastrService.success('Login successful!');
          this.router.navigate(['/']);
        } else {
          this.toastrService.warning(response.error || 'Unexpected error occurred.');
        }
      },
      error: (error) => {
        if (error.status === 401) {
          this.toastrService.error(error.error.error);
        }
      }
    });
  }
  
  loginwithOauth() {
    this.authService.loginWithOAuth();
  }

  private processLogin(): void {
    const url = new URL(window.location.href);
    const code = url.searchParams.get('code');
    
    if (code) {
      this.authService.getTokenFromBackend(code).subscribe({
        next: (response) => {
          this.authService.handleLogin(response.token);
          this.toastrService.success('Login successful!');
        },
        error: (err) => {
          this.toastrService.error('Error during authentication: ' + err);
          this.router.navigate(['/login']);
        }
      });
    } else if (this.authService.isLoggedIn()) {
        this.router.navigate(['/']);
    } else {
      this.router.navigate(['/login']);
    }
  }
}