import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss']
})
export class AlertComponent implements OnInit {
  @Input() backgroundColor: string = '#60CFA5'
  @Input() message: string = ''
  @Input() leftSymbol: boolean = false
  hideAlert: boolean = false;

  @Input() set color(value: string) {
    if (value === 'red') {
      this.backgroundColor = 'red'
    }
    if (value === 'yellow') {
      this.backgroundColor = 'yellow'
    }
  }

  ngOnInit(): void {
    setTimeout(() => {
      this.hideAlert = true;
    }, 10000);
  }
}
