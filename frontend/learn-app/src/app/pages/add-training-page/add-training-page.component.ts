import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../shared/services/user.service";
import {NotAssignedTrainer} from "../../shared/interfaces";
import {Router} from "@angular/router";
import {TrainingService} from "../../shared/services/training.service";

@Component({
  selector: 'app-add-training-page',
  templateUrl: './add-training-page.component.html',
  styleUrls: ['./add-training-page.component.scss']
})
export class AddTrainingPageComponent implements OnInit {
  addTrainingForm: FormGroup
  trainingName: string = ''
  trainerUsername: string = ''
  date: string = ''
  duration: number = 1.0
  trainingType: string = ''
  trainers: NotAssignedTrainer[] = []
  isNewTrainingValid: boolean = true
  isTrainingWasAdded: boolean = true

  constructor(private router: Router,
              private userService: UserService,
              private trainingService: TrainingService) {
    this.addTrainingForm = new FormGroup({
      trainingName: new FormControl('', [Validators.required]),
      trainerUsername: new FormControl('', [Validators.required]),
      duration: new FormControl('', [Validators.required]),
      trainingType: new FormControl('', [Validators.required]),
    })
  }

  /**
   * loading trainers for select options
   */
  ngOnInit(): void {
    this.userService.getNotAssignedTrainers()
      .subscribe((trainers) => {
        if (trainers.length == 0) {
          this.trainers = []
          return
        }
        this.trainers = trainers
      })
  }


  addTraining() {
    this.trainingName = this.addTrainingForm.get('trainingName')?.value
    this.date = (<HTMLInputElement>document.getElementById("singleDate"))?.value;
    const durationStr = parseFloat(this.addTrainingForm.get('duration')?.value);
    this.trainingType = this.addTrainingForm.get('trainingType')?.value
    this.trainerUsername = this.addTrainingForm.get('trainerUsername')?.value

    if (isNaN(durationStr) || durationStr <= 0
      || !this.trainingName || !this.trainerUsername
      || !this.date || !this.trainingType) {
      this.cleanInputs()
      return
    }

    this.userService.addTraining(
      this.trainingName,
      this.trainerUsername,
      this.date,
      durationStr,
      this.trainingType
    ).subscribe((isTrainingWasAdded) => {
        console.log("isTrainingWasAdded subscribe: " + isTrainingWasAdded)
        if (isTrainingWasAdded) {
          this.isTrainingWasAdded = true
          this.router.navigate(['/trainings'])
          this.setTrainingWasAdded()
        } else {
          this.cleanInputs()
        }
      }
    )
  }

  cleanInputs() {
    this.addTrainingForm.reset()
    const date = document.getElementById('singleDate') as HTMLInputElement;
    if (date) {
      date.value = ''
    }
    this.isNewTrainingValid = false
  }

  /**
   * this flag call alert displaying
   */
  setTrainingWasAdded() {
    this.trainingService.setTrainingWasAdded(true);
  }
}
