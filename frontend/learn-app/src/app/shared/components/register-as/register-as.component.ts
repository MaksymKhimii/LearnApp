import {Component, Input} from '@angular/core';

@Component({
  selector: 'register-as',
  templateUrl: './register-as.component.html',
  styleUrls: ['./register-as.component.scss']
})
export class RegisterAsComponent {
  @Input() role: string = 'Student';
  @Input() pngPath: string = '../../../assets/pictures/register-as-2.png';
}
