import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'registration-form-trainer',
  templateUrl: './registration-form-trainer.component.html',
  styleUrls: ['./registration-form-trainer.component.scss']
})
export class RegistrationFormTrainerComponent {
  trainerForm: FormGroup
  placeholderColor: string = 'initial'
  isFormConfirmed: boolean = false
  username: string = 'username'
  password: string = 'password'
  firstName: string = ''
  lastName: string = ''
  specialization: string = ''

  constructor(private authService: AuthService,
              private userService: UserService) {
    this.trainerForm = new FormGroup({
      firstName: new FormControl('', [
        Validators.required
      ]),
      lastName: new FormControl('', [
        Validators.required
      ]),
      specialization: new FormControl(null, [
        Validators.required
      ])
    });
  }

  submitTrainerRegistration() {
    if (this.trainerForm.valid) {
      this.placeholderColor = 'initial'
    } else {
      this.placeholderColor = 'red'
      Object.keys(this.trainerForm.controls).forEach(key => {
        this.trainerForm.get(key)?.markAsTouched();
      });
    }

    this.firstName = this.trainerForm.get('firstName')?.value
    this.lastName = this.trainerForm.get('lastName')?.value
    this.specialization = this.trainerForm.get('specialization')?.value

    if (this.specialization == null) {
      this.specialization = ''
    }

    this.authService.registerTrainer(
      this.firstName,
      this.lastName,
      this.specialization)
      .subscribe(isValidUser => {
        if (isValidUser) {
          this.username = this.userService.getUsername() || ''
          this.password = this.userService.getPassword() || ''
          this.isFormConfirmed = true
        } else {
          this.trainerForm.reset()
        }
      })
  }
}
