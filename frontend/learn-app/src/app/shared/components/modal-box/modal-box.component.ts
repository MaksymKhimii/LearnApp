import {Component} from '@angular/core';
import {UserService} from "../../services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-modal-box',
  templateUrl: './modal-box.component.html',
  styleUrls: ['./modal-box.component.scss']
})
export class ModalBoxComponent {

  constructor(private router: Router,
              private userService: UserService) {
  }

  deleteUserProfile() {
    const modalElement = document.getElementById('deleteConfirmationModal');
    if (modalElement) {
      modalElement.style.display = 'none';
      const modalBackdrop = document.querySelector('.modal-backdrop');
      if (modalBackdrop) {
        modalBackdrop.remove();
      }
    }
    if (this.userService.getRole() === 'trainee') {
      this.userService.deleteTraineeProfile()
      this.router.navigate(['/'])
    }
  }
}
