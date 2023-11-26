import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {User} from "../../interfaces";

@Component({
  selector: 'app-my-account',
  templateUrl: './my-account.component.html',
  styleUrls: ['./my-account.component.scss']
})
export class MyAccountComponent implements OnInit {
  @Input() firstName: string = 'Marta'
  @Input() lastName: string = 'Black'
  @Input() username: string = 'Marta_st'
  @Input() dateOfBirth: string | undefined = '01.01.2001'
  @Input() address: string | undefined = '123 Main StreetBoston, MA 02108United States'
  @Input() active: boolean = true
  @Input() specialization: string | undefined = ''
  isEditing = false

  editForm: FormGroup

  constructor(private userService: UserService) {
    this.editForm = new FormGroup<any>({})
  }

  isStudent(): boolean {
    return this.userService.getRole() === 'trainee';
  }

  ngOnInit(): void {
    this.loadUserProfile()
  }


  toggleEdit(): void {
    this.isEditing = !this.isEditing;
    if (this.isStudent()) {
      this.editForm = new FormGroup({
        firstName: new FormControl(this.firstName),
        lastName: new FormControl(this.lastName),
        username: new FormControl(this.username),
        dateOfBirth: new FormControl(this.dateOfBirth),
        address: new FormControl(this.address),
        active: new FormControl(this.active)
      })
    } else {
      this.editForm = new FormGroup({
        firstName: new FormControl(this.firstName),
        lastName: new FormControl(this.lastName),
        username: new FormControl(this.username),
        specialization: new FormControl(this.specialization),
        active: new FormControl(this.active)
      })
    }
  }

  updateUserProfile() {
    if (this.isStudent()) {
      const firstName = this.editForm.get('firstName')?.value;
      const lastName = this.editForm.get('lastName')?.value;
      const username = this.editForm.get('username')?.value;
      const active = this.editForm.get('active')?.value;
      let dateOfBirth = this.editForm.get('dateOfBirth')?.value;
      let address = this.editForm.get('address')?.value;

      if (address === '') {
        address = 'not specified'
        this.address = 'not specified'
      }

      if (dateOfBirth == null || dateOfBirth === 'not specified') {
        dateOfBirth = null
        this.dateOfBirth = 'not specified'
      }
      const user: User = {
        username: username,
        password: '',
        firstName: firstName,
        lastName: lastName,
        active: active,
        dateOfBirth: dateOfBirth,
        address: address
      }

      this.userService.updateTraineeProfile(user)
        .subscribe({
          next: (user: User | null) => {
            console.log("Updated user: " + user)
          },
          error: (error) => {
            console.error('Error:', error);
          }
        })

      this.userService.updateTraineeStatus(active)

      this.firstName = firstName
      this.lastName = lastName
      this.username = username
      this.active = active
      this.dateOfBirth = dateOfBirth
      this.address = address

      if (this.address === '') {
        this.address = 'not specified'
      }

      if (this.dateOfBirth == null || this.dateOfBirth === 'not specified') {
        this.dateOfBirth = 'not specified'
      }

      this.toggleEdit()
    } else {
      const firstName = this.editForm.get('firstName')?.value;
      const lastName = this.editForm.get('lastName')?.value;
      const username = this.editForm.get('username')?.value;
      const active = this.editForm.get('active')?.value;
      let specialization = this.editForm.get('specialization')?.value;

      if (specialization === '') {
        specialization = 'java'
        this.specialization = 'not specified'
      }

      const user: User = {
        username: username,
        password: '',
        firstName: firstName,
        lastName: lastName,
        active: active,
        specialization: specialization,
      }
      this.userService.updateTrainerProfile(user)
        .subscribe({
          next: (user: User | null) => {
            console.log("Updated trainer: " + user)
          },
          error: (error) => {
            console.error('Error:', error);
          }
        })

      this.firstName = firstName
      this.lastName = lastName
      this.username = username
      this.active = active
      this.specialization = specialization
      this.toggleEdit()
    }
  }

  loadUserProfile() {
    this.userService.getUserProfile().subscribe({
      next: (user: User | null) => {
        if (user !== null) {
          this.firstName = user.firstName
          this.lastName = user.lastName
          this.username = user.username
          this.active = user.active

          if (this.isStudent()) {

            if (user.address === '') {
              this.address = 'not specified'
            } else {
              this.address = user.address
            }

            if (user.dateOfBirth == null) {
              this.dateOfBirth = 'not specified'
            } else {
              console.log(4)
              this.dateOfBirth = user.dateOfBirth
            }

          } else {
            this.specialization = user.specialization
            if (user.specialization != null) {
              this.userService.setTrainingType(user.specialization)
            }
          }
          console.log('User Profile:', user);
          console.log("Role: " + this.userService.getRole());
        } else {
          console.log('Unable to fetch trainer profile.');
        }
      },
      error: (error) => {
        console.error('Error:', error);
      },
    });
  }
}
