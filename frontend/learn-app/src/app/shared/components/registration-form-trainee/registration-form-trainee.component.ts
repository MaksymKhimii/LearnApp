import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'registration-form-trainee',
  templateUrl: './registration-form-trainee.component.html',
  styleUrls: ['./registration-form-trainee.component.scss']
})
export class RegistrationFormTraineeComponent {
  traineeForm: FormGroup
  isFormConfirmed = false;
  placeholderColor: string = 'initial';
  username: string = 'username'
  password: string = 'password'
  firstName: string = ''
  lastName: string = ''
  dateOfBirth: string = ''
  address: string = ''

  constructor(private router: Router,
              private authService: AuthService,
              private userService: UserService) {
    this.traineeForm = new FormGroup({
      firstName: new FormControl('', [
        Validators.required
      ]),
      lastName: new FormControl('', [
        Validators.required
      ]),
      dateOfBirth: new FormControl('', []),
      address: new FormControl('', [])
    });
  }

  submitTraineeRegistration() {
    if (this.traineeForm.valid) {
      this.placeholderColor = 'initial';
    } else {
      this.placeholderColor = 'red';
      Object.keys(this.traineeForm.controls).forEach(key => {
        this.traineeForm.get(key)?.markAsTouched();
      });
    }

    this.firstName = this.traineeForm.get('firstName')?.value
    this.lastName = this.traineeForm.get('lastName')?.value
    this.dateOfBirth = this.traineeForm.get('dateOfBirth')?.value
    this.address = this.traineeForm.get('address')?.value

    if (this.dateOfBirth == null) {
      this.dateOfBirth = ''
    }
    if (this.address == null) {
      this.address = ''
    }

    this.authService.registerTrainee(
      this.firstName,
      this.lastName,
      this.dateOfBirth,
      this.address)
      .subscribe(isValidUser => {
        if (isValidUser) {
          this.username = this.userService.getUsername() || ''
          this.password = this.userService.getPassword() || ''
          this.isFormConfirmed = true
        } else {
          this.traineeForm.reset()
        }
      })
  }
}
