import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HlmAvatarImageDirective, HlmAvatarComponent, HlmAvatarFallbackDirective } from '@spartan-ng/ui-avatar-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule, 
    HlmAvatarImageDirective, HlmAvatarComponent, HlmAvatarFallbackDirective,
    HlmButtonDirective
  ], // Import necessary modules
  templateUrl: './header.component.html',
  styleUrls: []
})
export class HeaderComponent implements OnInit {
  username = '';
  showLogout = false;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.username = 'Mr. Lorem Ipsum';
  }

  handleLogout() {
    this.router.navigate(['/login']); // Redirect to home after logout
  }
}
