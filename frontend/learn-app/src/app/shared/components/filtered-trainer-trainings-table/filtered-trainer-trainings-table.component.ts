import {Component, Input} from '@angular/core';
import {TrainerTrainingInfo} from "../../interfaces";

@Component({
  selector: 'app-filtered-trainer-trainings-table',
  templateUrl: './filtered-trainer-trainings-table.component.html',
  styleUrls: ['./filtered-trainer-trainings-table.component.scss']
})
export class FilteredTrainerTrainingsTableComponent {
  @Input() trainings: TrainerTrainingInfo[] = [];
}
