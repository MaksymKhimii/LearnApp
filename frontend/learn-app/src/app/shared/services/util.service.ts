import {Injectable} from '@angular/core';
import {TrainerTrainingInfo, TrainingInfoDto} from "../interfaces";

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor() {
  }

  /*  convertTrainingsDurationToDays(trainings: TrainingInfoDto[]): TrainingInfoDto[] {
      return trainings.map((training) => {
        return {
          ...training,
          trainingDuration: this.convertDurationToDays(training.trainingDuration),
        };
      });
    }

    convertDurationToDays(duration: string): string {
      const durationObject = this.parseDuration(duration);
      return durationObject ? durationObject.days.toString() +' d' : '0 d';
    }

    parseDuration(duration: string): { days: number } | null {
      const match = duration.match(/P(\d+)D/);
      if (match && match[1]) {
        return { days: parseInt(match[1], 10) };
      }
      return null;
    }*/

  convertTrainingsDurationToDays(trainings: TrainingInfoDto[]): TrainingInfoDto[] {
    return trainings.map((training) => {
      return {
        ...training,
        trainingDuration: this.convertDurationToDays(training.trainingDuration),
      };
    });
  }

  convertDurationToDays(duration: string): string {
    const durationValue = this.extractNumberFromDuration(duration);
    return durationValue ? `${durationValue} d` : '0 d';
  }

  extractNumberFromDuration(duration: string): number | null {
    const match = duration.match(/T(\d+)S/);
    return match ? parseInt(match[1], 10) : null;
  }

  convertDateFormat(inputDate: string): string {
    const [day, month, year] = inputDate.split('/');
    return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
  }

  convertTrainerTrainingsDurationToDays(trainings: TrainerTrainingInfo[]): TrainerTrainingInfo[] {
    return trainings.map((training) => {
      return {
        ...training,
        trainingDuration: this.convertDurationToDays(training.trainingDuration),
      };
    });
  }
}
