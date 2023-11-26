import {Component, OnInit} from '@angular/core';
import {UserService} from "../../shared/services/user.service";

@Component({
  selector: 'app-my-account-page',
  templateUrl: './my-account-page.component.html',
  styleUrls: ['./my-account-page.component.scss']
})
export class MyAccountPageComponent {

  constructor(private userService: UserService) {
  }

  isStudent(): boolean {
    return this.userService.getRole() === 'trainee';
  }
}
