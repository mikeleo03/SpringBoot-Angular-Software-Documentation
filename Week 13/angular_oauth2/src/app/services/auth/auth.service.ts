import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/oauth2/callback'; // Endpoint to handle OAuth callback
  private jwtHelper = new JwtHelperService(); // Ensure JwtHelperService is correctly imported

  constructor(private http: HttpClient, private router: Router) {
    this.checkToken(); // Check token on initialization
  }

  // Redirect to Google OAuth login page
  login(): void {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  }

  // Store the token after successful login and validate it
  handleLogin(token: string): void {
    localStorage.setItem('token', token);
    if (this.isLoggedIn()) { // Check if token is valid right after login
      this.router.navigate(['/']); // Redirect to home page after login
    } else {
      this.logout(); // If token is invalid, force logout
    }
  }

  // Fetch token from backend after OAuth2 callback
  getTokenFromBackend(code: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}?code=${code}`);
  }

  // Remove token on logout and update login state
  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']); // Redirect to login page
  }

  // Check if user is logged in by verifying token existence and validity
  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    return token != null && !this.jwtHelper.isTokenExpired(token); // Return true if token exists and is valid
  }

  // Check the presence and validity of the token on startup
  private checkToken(): void {
    if (this.isLoggedIn()) {
      // User is logged in
      console.log('User is logged in');
      this.router.navigate(['/']);
    } else {
      // Not logged in, redirect to login page if necessary
      console.log('User is not logged in');
      this.router.navigate(['/login']);
    }
  }
}