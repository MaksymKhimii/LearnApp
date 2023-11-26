import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent {
  @Input() color: string = '#6355D8FF'
  @Input() width: string = '88px'
  @Input() text: string = ''
  @Input() type: string = 'button'
}
