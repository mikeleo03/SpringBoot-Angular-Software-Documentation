import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-login',
  standalone: true,  // Important! This marks the component as standalone.
  templateUrl: './login.component.html',
  styleUrls: [],
  providers: [AuthService], // Services can be provided directly in the component
  imports: [FormsModule, CommonModule] // Import necessary modules directly into the component
})
export class LoginComponent {
  user: User = { id: 0, username: '', password: '' };

  constructor(private authService: AuthService) {}

  mainbg: string = 'background.jpg';

  login() {
    this.authService.login(this.user).subscribe(response => {
      console.log('Login successful', response);
    });
  }
}
