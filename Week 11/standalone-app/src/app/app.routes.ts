import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component'; // Import the LoginComponent

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: '', redirectTo: '/login', pathMatch: 'full' }
];