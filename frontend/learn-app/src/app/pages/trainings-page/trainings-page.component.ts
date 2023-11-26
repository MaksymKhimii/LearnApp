import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../shared/services/user.service";
import {TrainerTrainingInfo, TrainingInfoDto} from "../../shared/interfaces";
import {TrainingService} from "../../shared/services/training.service";
import {UtilService} from "../../shared/services/util.service";

@Component({
  selector: 'app-trainings-page',
  templateUrl: './trainings-page.component.html',
  styleUrls: ['./trainings-page.component.scss']
})
export class TrainingsPageComponent {
  isTrainingWasAdded: boolean = false
  searchFormTrainee: FormGroup
  searchFormTrainer: FormGroup
  loadedTrainings: TrainingInfoDto[] = []
  loadedTrainerTrainings: TrainerTrainingInfo[] = []
  trainerName: string = ''
  traineeName: string = ''
  trainingType: string = ''
  periodFrom: string = ''
  periodTo: string = ''
  isTrainingsWasNotFound: boolean = false
  isSearch: boolean = false


  constructor(private userService: UserService,
              private trainingService: TrainingService,
              private utilService: UtilService) {
    this.searchFormTrainee = new FormGroup({
      trainerName: new FormControl('', [
        Validators.required
      ]),
      trainingType: new FormControl('', [
        Validators.required
      ])
    })

    this.searchFormTrainer = new FormGroup({
      traineeName: new FormControl('', [
        Validators.required
      ]),
    })

    this.trainingService.isTrainingWasAdded$.subscribe((value) => {
      this.isTrainingWasAdded = value;
    });
  }

  searchTraineeTrainings() {
    this.trainerName = this.searchFormTrainee.get('trainerName')?.value
    this.trainingType = this.searchFormTrainee.get('trainingType')?.value
    this.periodFrom = (<HTMLInputElement>document.getElementById("periodFrom"))?.value;
    this.periodTo = (<HTMLInputElement>document.getElementById("periodTo"))?.value;

    this.userService.searchTraineeTrainings(
      this.trainerName,
      this.trainingType,
      this.periodFrom,
      this.periodTo
    ).subscribe((trainings) => {
      if (trainings.length == 0) {
        this.isTrainingsWasNotFound = true
        return
      }
      this.loadedTrainings = trainings
    })
  }

  clearAfterSearch() {
    this.searchFormTrainee.reset()
    const periodFromElement = document.getElementById('periodFrom') as HTMLInputElement;
    const periodToElement = document.getElementById('periodTo') as HTMLInputElement;

    if (periodFromElement) {
      periodFromElement.value = '';
    }

    if (periodToElement) {
      periodToElement.value = '';
    }
    this.isSearch = false
    window.location.reload()
  }

  isStudent(): boolean {
    return this.userService.getRole() === 'trainee';
  }

  searchTrainerTrainings() {
    this.isSearch = true
    this.traineeName = this.searchFormTrainee.get('traineeName')?.value
    this.periodFrom = (<HTMLInputElement>document.getElementById("periodFrom"))?.value;
    this.periodTo = (<HTMLInputElement>document.getElementById("periodTo"))?.value;
    const trainerTrainingType = this.userService.getTrainingType()

    if (trainerTrainingType != null) {
      this.trainingType = trainerTrainingType
    }

    this.userService.getTrainerTrainings()
      .subscribe((trainings) => {
        const filteredTrainings = this.userService.searchTrainerTrainings(
          this.traineeName,
          this.trainingType,
          this.utilService.convertDateFormat(this.periodFrom),
          this.utilService.convertDateFormat(this.periodTo),
          trainings
        )
        this.loadedTrainerTrainings = this.utilService.convertTrainerTrainingsDurationToDays(filteredTrainings)
      })
  }

  getAllTrainerTrainings() {
    this.isSearch = true
    this.userService.getTrainerTrainings()
      .subscribe(
        (trainings) => {
          this.loadedTrainerTrainings = this.utilService.convertTrainerTrainingsDurationToDays(trainings)
        }
      )
  }
}
