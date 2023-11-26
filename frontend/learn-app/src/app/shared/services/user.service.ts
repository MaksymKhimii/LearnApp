import {Injectable} from '@angular/core';
import {TokenService} from "./token.service";
import {
  NotAssignedTrainer,
  TraineeForTable,
  TraineeTrainingDto,
  TrainerForTable,
  TrainerTrainingInfo,
  TrainingInfoDto,
  User
} from "../interfaces";
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = environment.apiUrl;
  token: string | null = ''
  username: string | null = ''
  password: string | null = ''
  private readonly USERNAME = 'username'
  private readonly PASSWORD = 'password'
  private readonly ROLE = 'role'
  private readonly TRAINING_TYPE = 'trainingType'

  constructor(private tokenService: TokenService, private http: HttpClient) {
    this.token = tokenService.getToken()
  }

  saveUser(username: string | null, password: string) {
    if (typeof username === "string") {
      localStorage.setItem(this.USERNAME, username)
    }
    localStorage.setItem(this.PASSWORD, password)
  }

  removeUser() {
    localStorage.removeItem(this.USERNAME)
    localStorage.removeItem(this.PASSWORD)
  }

  getUsername() {
    return localStorage.getItem(this.USERNAME)
  }

  getPassword() {
    return localStorage.getItem(this.PASSWORD)
  }

  setRole(role: string) {
    localStorage.setItem(this.ROLE, role)
  }

  getRole(): string | null {
    const role = localStorage.getItem(this.ROLE)
    if (role == null || role === '') {
      return null
    }
    return role
  }

  removeRole() {
    localStorage.removeItem(this.ROLE)
  }

  setTrainingType(trainingType: string) {
    localStorage.setItem(this.TRAINING_TYPE, trainingType)
  }

  getTrainingType(): string | null {
    return localStorage.getItem(this.TRAINING_TYPE);
  }


  getUserProfile(): Observable<User | null> {
    const username = localStorage.getItem(this.USERNAME)
    const password = localStorage.getItem(this.PASSWORD)
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    })
    if (!username || !password) {
      console.error('Username or password is missing.');
      return of(null);
    }
    return this.http.post(`${this.baseUrl}/trainees/profile/get`, {
      username: username,
      password: password
    }, {
      headers,
      observe: 'response'
    })
      .pipe(
        map((response: HttpResponse<any>) => {
          const user: User = {
            username: username,
            password: password,
            firstName: response.body?.firstName,
            lastName: response.body?.lastName,
            dateOfBirth: response.body?.dateOfBirth,
            address: response.body?.address,
            active: response.body?.active,
            trainersList: response.body?.trainersList
          }
          this.setRole('trainee')
          return user;
        }),
        catchError(() => {
          return this.http.post(`${this.baseUrl}/trainers/profile/get`, {
            username: username,
            password: password
          }, {
            headers,
            observe: 'response'
          })
            .pipe(
              map((response: HttpResponse<any>) => {
                const user: User = {
                  username: username,
                  password: password,
                  firstName: response.body?.firstName,
                  lastName: response.body?.lastName,
                  active: response.body?.active,
                  specialization: response.body?.specialization,
                  traineesList: response.body?.traineesList
                }
                this.setRole('trainer')
                return user;
              }),
              catchError((error: HttpErrorResponse) => {
                console.error('Error during getting user profile: ', error);
                return of(null);
              })
            );
        })
      );
  }

  updateTraineeProfile(user: User): Observable<User | null> {
    const username = localStorage.getItem(this.USERNAME)
    const password = localStorage.getItem(this.PASSWORD)

    if (username == null || password == null) {
      return of(null)
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    })
    return this.http.put(`${this.baseUrl}/trainees/profile/update`,
      {
        username: username,
        password: password,
        firstName: user.firstName,
        lastName: user.lastName,
        dateOfBirth: user.dateOfBirth,
        address: user.address,
        isActive: user.active
      },
      {
        headers,
        observe: 'response'
      })
      .pipe(
        map((response: HttpResponse<any>) => {
          const user: User = {
            username: username,
            password: password,
            firstName: response.body?.firstName,
            lastName: response.body?.lastName,
            dateOfBirth: response.body?.dateOfBirth,
            address: response.body?.address,
            active: response.body?.active,
            trainersList: response.body?.trainersList
          }
          console.log("Trainee`s profile has been updated")
          return user
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('Error during updating trainee`s profile: ', error);
          return of(null);
        })
      )
  }

  updateTraineeStatus(status: boolean): void {
    const username = localStorage.getItem(this.USERNAME)
    const password = localStorage.getItem(this.PASSWORD)

    if (username == null || password == null) {
      return
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    })
    this.http.patch(`${this.baseUrl}/trainees/profile/status/update`,
      {
        username: username,
        password: password,
        isActive: status
      },
      {
        headers,
        observe: 'response'
      })
      .pipe(
        map((response: HttpResponse<any>) => {
          console.log(" Trainee`s status has been updated")
        }),
        catchError((error: HttpErrorResponse): Observable<any> => {
          console.error('Error during updating trainee status: ', error);
          return of(null);
        })
      ).subscribe()
  }

  updateTrainerProfile(user: User): Observable<User | null> {
    const username = localStorage.getItem(this.USERNAME)
    const password = localStorage.getItem(this.PASSWORD)

    if (username == null || password == null) {
      return of(null)
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    })
    return this.http.put(`${this.baseUrl}/trainers/profile/update`,
      {
        username: username,
        password: password,
        firstName: user.firstName,
        lastName: user.lastName,
        specialization: user.specialization,
        isActive: user.active
      },
      {
        headers,
        observe: 'response'
      })
      .pipe(
        map((response: HttpResponse<any>) => {
          const user: User = {
            username: username,
            password: password,
            firstName: response.body?.firstName,
            lastName: response.body?.lastName,
            specialization: response.body?.specialization,
            active: response.body?.active,
            traineesList: response.body?.traineesList
          }
          console.log("Trainer`s profile has been updated")
          return user
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('Error during updating trainer`s profile: ', error);
          return of(null);
        })
      )
  }

  updateTrainerStatus(status: boolean): void {
    const username = localStorage.getItem(this.USERNAME)
    const password = localStorage.getItem(this.PASSWORD)

    if (username == null || password == null) {
      return
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    })
    this.http.patch(`${this.baseUrl}/trainers/profile/status/update`,
      {
        username: username,
        password: password,
        isActive: status
      },
      {
        headers,
        observe: 'response'
      })
      .pipe(
        map((response: HttpResponse<any>) => {
          console.log(" Trainer`s status has been updated")
        }),
        catchError((error: HttpErrorResponse): Observable<any> => {
          console.error('Error during updating trainer status: ', error);
          return of(null);
        })
      ).subscribe()
  }

  deleteTraineeProfile(): void {
    const username = localStorage.getItem(this.USERNAME)
    const password = localStorage.getItem(this.PASSWORD)

    if (username == null || password == null) {
      return
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    })

    const options = {
      headers: headers,
      body: {
        username: username,
        password: password,
      }
    }

    this.http.delete(`${this.baseUrl}/trainees/profile/delete`, options)
      .subscribe({
        next: () => {
          console.log('Profile deletion successful');
        },
        error: (error) => {
          console.error('Error during profile deletion:', error);
        }
      });
    this.removeUser()
    this.removeRole()
    this.tokenService.removeToken()
  }

  getTraineeTrainings(): Observable<TrainingInfoDto[]> {
    const username = localStorage.getItem(this.USERNAME)
    const password = localStorage.getItem(this.PASSWORD)

    if (username == null || password == null) {
      return new Observable<TrainingInfoDto[]>()
    }
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    })
    return this.http
      .post<TrainingInfoDto[]>(`${this.baseUrl}/trainees/profile/trainings/all/get`,
        {
          username: username,
          password: password,
        },
        {headers})
      .pipe(catchError((error) => {
        console.error('An error occurred during receiving trainee trainings', error);
        return [];
      }));
  }

  getTraineeTrainersList(trainings: TrainingInfoDto[]): TrainerForTable[] {
    return trainings.reduce((accumulator, currentTraining) => {
      const existingItem = accumulator.find(
        (item) =>
          item.trainerName === currentTraining.trainerName && item.trainingType === currentTraining.trainingType
      );

      if (!existingItem) {
        accumulator.push({
          trainerName: currentTraining.trainerName,
          trainingType: currentTraining.trainingType,
        });
      }

      return accumulator;
    }, [] as TrainerForTable[]);
  }

  searchTraineeTrainings(trainerName: string,
                         trainingType: string,
                         periodFrom: string,
                         periodTo: string): Observable<TrainingInfoDto[]> {
    const username = localStorage.getItem(this.USERNAME)
    const password = localStorage.getItem(this.PASSWORD)

    if (username == null || password == null) {
      return new Observable<TrainingInfoDto[]>()
    }
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    })
    const traineeTrainingDto: TraineeTrainingDto = {
      username: username,
      password: password,
      periodFrom: periodFrom,
      periodTo: periodTo,
      trainerName: trainerName,
      trainingType: trainingType
    }
    return this.http
      .post<TrainingInfoDto[]>(`${this.baseUrl}/trainees/profile/trainings/get`,
        traineeTrainingDto,
        {headers})
      .pipe(catchError((error) => {
        console.error('An error occurred during receiving trainee trainings', error);
        return [];
      }));
  }

  getNotAssignedTrainers(): Observable<NotAssignedTrainer[]> {
    const username = localStorage.getItem(this.USERNAME)
    const password = localStorage.getItem(this.PASSWORD)

    if (username == null || password == null) {
      return new Observable<NotAssignedTrainer[]>()
    }
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    })
    return this.http
      .post<NotAssignedTrainer[]>(`${this.baseUrl}/trainers/available/get`,
        {
          username: username,
          password: password
        },
        {headers})
      .pipe(catchError((error) => {
        console.error('An error occurred during receiving trainee trainings', error);
        return [];
      }));
  }

  addTraining(trainingName: string,
              trainerUsername: string,
              date: string,
              duration: number,
              trainingType: string): Observable<boolean> {
    const username = localStorage.getItem(this.USERNAME)

    if (username == null) {
      return new Observable<false>()
    }
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    })
    return this.http.post(`${this.baseUrl}/training/add`, {
        traineeUsername: username,
        trainerUsername: trainerUsername,
        trainingName: trainingName,
        date: date,
        duration: duration,
        trainingType: trainingType
      },
      {
        headers,
        observe: 'response'
      }).pipe(
      map((response: HttpResponse<any>) => {
        if (response.status === 200) {
          console.log("New Training has been added")
          return true
        } else {
          return false
        }
      }),
      catchError((error: HttpErrorResponse): Observable<boolean> => {
        console.error('Error during adding training: ', error);
        return new Observable<false>();
      })
    )
  }

  getTrainerTrainings(): Observable<TrainerTrainingInfo[]> {
    const username = localStorage.getItem(this.USERNAME)
    const password = localStorage.getItem(this.PASSWORD)

    if (username == null || password == null) {
      return new Observable<TrainerTrainingInfo[]>()
    }
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    })
    return this.http
      .post<TrainerTrainingInfo[]>(`${this.baseUrl}/trainers/profile/trainings/all/get`,
        {
          username: username,
          password: password,
        },
        {headers})
      .pipe(catchError((error) => {
        console.error('An error occurred during receiving trainee trainings', error);
        return [];
      }));
  }

  getTrainerTrainingsList(trainings: TrainerTrainingInfo[]): TraineeForTable[] {
    return trainings.reduce((accumulator, currentTraining) => {
      const existingItem = accumulator.find(
        (item) => item.traineeName === currentTraining.traineeName
      );

      if (!existingItem) {
        accumulator.push({
          traineeName: currentTraining.traineeName,
          status: true
        });
      }

      return accumulator;
    }, [] as TraineeForTable[]);
  }

  searchTrainerTrainings(
    traineeName: string,
    trainingType: string,
    periodFrom: string,
    periodTo: string,
    trainings: TrainerTrainingInfo[]
  ): TrainerTrainingInfo[] {
    return trainings.filter((training) => {
      const isTraineeNameMatch = !traineeName || training.traineeName.toLowerCase().includes(traineeName.toLowerCase());
      const isTrainingTypeMatch = !trainingType || training.trainingType.toLowerCase() === trainingType.toLowerCase();
      const isDateInRange =
        (!periodFrom || training.trainingDate >= periodFrom) &&
        (!periodTo || training.trainingDate <= periodTo);

      return isTraineeNameMatch && isTrainingTypeMatch && isDateInRange;
    });
  }
}
