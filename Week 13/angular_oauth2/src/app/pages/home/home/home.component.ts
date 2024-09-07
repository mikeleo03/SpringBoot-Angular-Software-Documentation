import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  template: `
    <h1>Welcome Home!</h1>
    <button (click)="logout()">Logout</button>
  `
})
export class HomeComponent {
  constructor(private authService: AuthService) { }

  logout(): void {
    this.authService.logout();
  }
}