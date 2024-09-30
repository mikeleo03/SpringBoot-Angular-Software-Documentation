import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  user: User = { id: 0, username: '', password: '' };

  constructor(private authService: AuthService) {}

  login() {
      this.authService.login(this.user).subscribe(response => {
          console.log('Login successful', response);
      });
  }
}
