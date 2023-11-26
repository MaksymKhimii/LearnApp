import {Component, Input, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {TrainingInfoDto} from "../../interfaces";
import {UtilService} from "../../services/util.service";

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnInit {
  @Input() trainings: TrainingInfoDto[] = [];

  constructor(private userService: UserService,
              private utilService: UtilService) {
  }

  ngOnInit(): void {
    this.userService.getTraineeTrainings()
      .subscribe((trainings) => {
          this.trainings = this.utilService.convertTrainingsDurationToDays(trainings)
        }
      )
  }

  isStudent(): boolean {
    return this.userService.getRole() === 'trainee';
  }
}
