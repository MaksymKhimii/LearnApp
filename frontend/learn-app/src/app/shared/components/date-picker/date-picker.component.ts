import {Component, Input} from '@angular/core';
import {BsDatepickerConfig} from "ngx-bootstrap/datepicker";

@Component({
  selector: 'app-date-picker',
  templateUrl: './date-picker.component.html',
  styleUrls: ['./date-picker.component.scss']
})
export class DatePickerComponent {
  @Input() singleInput: boolean = false
  bsConfig: Partial<BsDatepickerConfig>;
  fromDate: Date | undefined;
  toDate: Date | undefined;

  constructor() {
    this.bsConfig = Object.assign({}, {
      dateInputFormat: 'DD/MM/YYYY'
    });
  }

  onValueChange(value: Date | undefined, isFrom: boolean): void {
    if (isFrom) {
      this.fromDate = value;
      this.toDate = undefined;
    } else {
      if (this.fromDate) {
        this.toDate = value;
      }
    }
  }
}
