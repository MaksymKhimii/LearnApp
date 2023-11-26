import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TrainingService {
  private isTrainingWasAddedSubject = new BehaviorSubject<boolean>(false);
  isTrainingWasAdded$ = this.isTrainingWasAddedSubject.asObservable();

  setTrainingWasAdded(value: boolean) {
    this.isTrainingWasAddedSubject.next(value);
  }
}
