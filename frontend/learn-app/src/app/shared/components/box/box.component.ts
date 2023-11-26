import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-box',
  templateUrl: './box.component.html',
  styleUrls: ['./box.component.scss']
})
export class BoxComponent {
  @Input() tag: string = 'Tag'
  @Input() title: string = 'Title'
  @Input() date: string = 'Date'
  @Input() img: string = '../../../assets/pictures/register-as-2.png'
}
