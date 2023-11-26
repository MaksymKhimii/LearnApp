import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";

@Component({
  selector: 'app-registration-page',
  templateUrl: './registration-page.component.html',
  styleUrls: ['./registration-page.component.scss']
})
export class RegistrationPageComponent implements OnInit{
  isStudent: boolean =false

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params=> {
      const userType = params.get('userType');
        console.log("userType: " + userType)
        if (userType==='student') {
          this.isStudent = true
        }
      }
    )
  }
}
