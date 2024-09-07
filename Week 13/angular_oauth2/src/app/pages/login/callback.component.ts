import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-callback',
  template: `<p>Processing login...</p>`,
})
export class CallbackComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.processLogin();
  }

  private processLogin(): void {
    const url = new URL(window.location.href);
    const code = url.searchParams.get('code');
    
    if (code) {
      this.authService.getTokenFromBackend(code).subscribe({
        next: (response) => {
          this.authService.handleLogin(response.token);
        },
        error: (err) => {
          console.error('Error during authentication', err);
          this.router.navigate(['/login']);
        }
      });
    } else {
      this.router.navigate(['/login']);
    }
  }
}
