import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable } from 'rxjs';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/oauth2/callback'; // Endpoint to handle OAuth callback
  private jwtHelper = new JwtHelperService(); // Ensure JwtHelperService is correctly imported
  private authUrl = `${environment.apiUrl}/authentication`;
  private apiKey = environment.apiKey;

  constructor(private http: HttpClient, private router: Router) { }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'api-key': this.apiKey
    });
  }

  login(user: any): Observable<any> {
    const headers = this.getHeaders();
    return this.http.post<any>(this.authUrl, user, { headers });
  }

  // Redirect to Google OAuth login page
  loginWithOAuth(): void {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  }

  setToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // Store the token after successful login and validate it
  handleLogin(token: string): void {
    this.setToken(token);
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
  }

  // Check if user is logged in by verifying token existence and validity
  isLoggedIn(): boolean {
    const token = this.getToken();
    return token != null && !this.jwtHelper.isTokenExpired(token); // Return true if token exists and is valid
  }
}