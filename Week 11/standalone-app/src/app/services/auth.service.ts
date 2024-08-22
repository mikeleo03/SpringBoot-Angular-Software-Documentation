import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/users';  // Replace with actual API URL

  constructor(private http: HttpClient) { }

  login(user: User): Observable<any> {
    // Send a POST request to the server with the user's credentials
    return this.http.post<any>(this.apiUrl, user);
  }
}