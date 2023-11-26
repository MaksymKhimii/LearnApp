import {Component, Input, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {TrainerForTable} from "../../interfaces";

@Component({
  selector: 'app-trainers-table',
  templateUrl: './trainers-table.component.html',
  styleUrls: ['./trainers-table.component.scss']
})
export class TrainersTableComponent implements OnInit {
  @Input() trainers: TrainerForTable[] = [];

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.getTraineeTrainings()
      .subscribe((trainings) => {
        this.trainers = this.userService.getTraineeTrainersList(trainings)
      })
  }
}
