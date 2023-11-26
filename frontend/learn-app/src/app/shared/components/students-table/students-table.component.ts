import {Component, Input} from '@angular/core';
import {TraineeForTable} from "../../interfaces";

@Component({
  selector: 'app-students-table',
  templateUrl: './students-table.component.html',
  styleUrls: ['./students-table.component.scss']
})
export class StudentsTableComponent {
  @Input() trainees: TraineeForTable[] = [];
}
