import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {TokenService} from "./token.service";
import {Router} from "@angular/router";
import {catchError, map, Observable, of} from "rxjs";
import {UserService} from "./user.service";
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = environment.apiUrl;
  token: string | null = ''


  constructor(private http: HttpClient,
              private router: Router,
              private tokenService: TokenService,
              private userService: UserService) {
    this.token = tokenService.getToken()
  }

  login(username: string, password: string): Observable<boolean> {
    return this.http.post(`${this.baseUrl}/auth/authenticate`,
      {
        username: username,
        password: password
      },
      {observe: 'response'})
      .pipe(
        map((response: HttpResponse<any>) => {
          const token = response.body?.token;
          if (token) {
            this.tokenService.removeToken()
            this.tokenService.setToken(token);
            console.log('New Token:', token);
            return true;
          } else {
            return false;
          }
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('Error during authentication:', error);
          return of(false);
        })
      );
  }

  logout(): void {
    const username = this.userService.getUsername()
    const password = this.userService.getPassword()

    if (username == null || password == null) {
      return
    }

    this.http.post(`${this.baseUrl}/auth/logout`, {
      username: username,
      password: password
    }, {
      observe: 'response',
      headers: {
        'Authorization': `Bearer ${this.token}`
      }
    })
      .pipe(
        map((response: HttpResponse<any>) => {
          if (response.status === 200) {
            console.log("User successfully logged out.");
          } else {
            console.error('Failure during logging out:', response.status);
          }
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('Failure during logging out:', error);
          return of(false);
        })
      ).subscribe()
    this.userService.removeRole()
    this.userService.removeUser()
    this.tokenService.removeToken()
  }

  registerTrainee(firstName: string,
                  lastName: string,
                  dateOfBirth?: string,
                  address?: string): Observable<boolean> {
    return this.http.post(`${this.baseUrl}/auth/trainee/register`, {
      firstName: firstName,
      lastName: lastName,
      dateOfBirth: dateOfBirth,
      address: address
    }, {observe: 'response'})
      .pipe(
        map((response: HttpResponse<any>) => {
          const username = response.body?.username
          const password = response.body?.password
          const token = response.body?.token
          if (token) {
            this.userService.removeUser()
            this.userService.saveUser(username, password)
            this.tokenService.removeToken()
            this.tokenService.setToken(token)
            console.log("New Username: " + username)
            console.log("New Password: " + password)
            console.log('New Token:', token)
            this.userService.setRole('trainee')
            return true;
          } else {
            return false;
          }
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('Error during trainee registration:', error);
          return of(false);
        })
      );
  }

  registerTrainer(firstName: string,
                  lastName: string,
                  specialization?: string): Observable<boolean> {
    console.log("specialization: (" + specialization + ")")
    return this.http.post(`${this.baseUrl}/auth/trainer/register`, {
      firstName: firstName,
      lastName: lastName,
      specialization: specialization
    }, {observe: 'response'})
      .pipe(
        map((response: HttpResponse<any>) => {
          const username = response.body?.username
          const password = response.body?.password
          const token = response.body?.token
          console.log(1)
          if (token) {
            if (this.userService.getUsername() != null) {
              this.userService.removeUser()
              this.tokenService.removeToken()
            }
            console.log(2)
            this.userService.saveUser(username, password)

            this.tokenService.setToken(token)
            console.log("New Username: " + username)
            console.log("New Password: " + password)
            console.log('New Token:', token)
            this.userService.setRole('trainer')
            return true;
          } else {
            return false;
          }
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('Error during trainer registration:', error);
          return of(false);
        })
      );
  }

  changePassword(currentPassword: string,
                 newPassword: string): Observable<boolean> {
    const username: string | null = this.userService.getUsername()
    const token: string | null = this.tokenService.getToken();

    return this.http.put(`${this.baseUrl}/users/password/update`, {
      username: username,
      oldPassword: currentPassword,
      newPassword: newPassword
    }, {
      observe: 'response',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
      .pipe(
        map((response: HttpResponse<any>) => {
          if (response.status === 200) {
            this.userService.saveUser(username, newPassword)
            console.log("Password changed successfully.");
            return true;
          } else {
            console.error('Password change failed with status:', response.status);
            return false;
          }
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('Error during password change:', error);
          return of(false);
        })
      );
  }
}
