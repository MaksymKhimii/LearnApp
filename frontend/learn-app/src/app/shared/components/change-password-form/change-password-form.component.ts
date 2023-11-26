import {Component} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-change-password-form',
  templateUrl: './change-password-form.component.html',
  styleUrls: ['./change-password-form.component.scss']
})
export class ChangePasswordFormComponent {
  changePasswordForm: FormGroup
  placeholderColor: string = 'initial'
  formConfirmed: boolean = false
  currentPasswordVisible: boolean = false
  newPasswordVisible: boolean = false
  confirmedNewPasswordVisible: boolean = false
  currentPassword: string = ''
  newPassword: string = ''
  confirmedNewPassword: string = ''

  constructor(private authService: AuthService) {
    this.changePasswordForm = new FormGroup({
      currentPassword: new FormControl('', [Validators.required]),
      newPassword: new FormControl('', [Validators.required]),
      confirmedNewPassword: new FormControl('', [Validators.required]),
    })
  }

  togglePassword(inputId: string): void {
    const input = document.getElementById(inputId) as HTMLInputElement;
    console.log(inputId)
    if (inputId === 'currentPassword' && input) {
      this.currentPasswordVisible = !this.currentPasswordVisible;
      input.type = this.currentPasswordVisible ? 'text' : 'password';
      return
    }

    if (inputId === 'newPassword' && input) {
      this.newPasswordVisible = !this.newPasswordVisible;
      input.type = this.newPasswordVisible ? 'text' : 'password';
      return
    }

    if (inputId === 'confirmedNewPassword' && input) {
      this.confirmedNewPasswordVisible = !this.confirmedNewPasswordVisible;
      input.type = this.confirmedNewPasswordVisible ? 'text' : 'password';
      return
    }
  }

  changePassword() {
    if (this.changePasswordForm.valid) {
      this.placeholderColor = 'initial'
    } else {
      this.placeholderColor = 'red'
      Object.keys(this.changePasswordForm.controls).forEach(key => {
        this.changePasswordForm.get(key)?.markAsTouched();
      });
    }
    this.currentPassword = this.changePasswordForm.get('currentPassword')?.value
    this.newPassword = this.changePasswordForm.get('newPassword')?.value
    this.confirmedNewPassword = this.changePasswordForm.get('confirmedNewPassword')?.value

    console.log("currentPassword: " + this.currentPassword)
    console.log("newPassword: " + this.newPassword)
    console.log("newPasswordConfirm: " + this.confirmedNewPassword)

    this.authService.changePassword(
      this.currentPassword,
      this.newPassword
    )
      .subscribe(isPasswordWasChanged => {
        if (isPasswordWasChanged) {
          this.formConfirmed = true;
        } else {
          this.changePasswordForm.reset()
        }
      })
  }
}
