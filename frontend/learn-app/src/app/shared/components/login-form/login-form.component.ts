import {Component, Input} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {Router} from "@angular/router";


@Component({
  selector: ' login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent {
  loginForm: FormGroup

  @Input() backgroundColor: string = '#F8F8F8';
  @Input() placeholder: string = 'Enter password';
  @Input() passwordType: string = 'password';
  @Input() formControlName: string = '';

  passwordVisible: boolean = false;
  lock: boolean = false;
  isUserValid: boolean = true

  constructor(private http: HttpClient,
              private router: Router,
              private authService: AuthService,
              private userService: UserService) {
    this.loginForm = new FormGroup({
      username: new FormControl('', [
        Validators.required
      ]),
      password: new FormControl('', [
        Validators.required
      ]),
    });
  }

  onAuthenticate() {
    const usernameValue = this.loginForm.get('username')?.value;
    const passwordValue = this.loginForm.get('password')?.value;
    this.authService.login(usernameValue, passwordValue)
      .subscribe(isValidUser => {
        if (isValidUser) {
          this.userService.saveUser(usernameValue, passwordValue);
          this.router.navigate(['/account']).then(r => window.location.reload())
        } else {
          this.isUserValid = false
          this.loginForm.reset()
        }
      })
  }

  togglePassword() {
    this.passwordType = this.passwordVisible ? 'password' : 'text';
    this.passwordVisible = !this.passwordVisible;
    this.lock = !this.lock
    if (this.formControlName) {
      const control = new FormControl('');
      control.setValue(this.passwordVisible ? control.value : '');
    }
  }
}
