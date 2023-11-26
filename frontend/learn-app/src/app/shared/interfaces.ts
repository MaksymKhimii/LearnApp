export interface User {
  username: string,
  password: string,
  firstName: string,
  lastName: string,
  active: boolean,
  dateOfBirth?: string,
  address?: string,
  specialization?: string,
  token?: string
  trainersList?: Trainer[]
  traineesList?: Trainee[]
}

export interface Trainee {
  username?: string,
  firstName?: string,
  lastName?: string,
  active?: boolean,
  dateOfBirth?: string,
  address?: string,
}

export interface Trainer {
  username?: string,
  firstName?: string,
  lastName?: string,
  active?: boolean,
  specialization?: string
}

export interface Training {
  trainingName?: string,
  trainingDate?: string,
  trainingType?: string,
  trainingDuration?: string,
  trainerName?: string
}

export interface TrainingInfoDto {
  trainingName: string,
  trainingDate: string,
  trainingType: string,
  trainingDuration: string,
  trainerName: string
}

export interface TrainerTrainingInfo {
  trainingName: string,
  trainingDate: string,
  trainingType: string,
  trainingDuration: string,
  traineeName: string
}

export interface TraineeTrainingDto {
  username: string,
  password: string,
  periodFrom: string,
  periodTo: string,
  trainerName: string,
  trainingType: string
}

export interface TrainerForTable {
  trainerName: string,
  trainingType: string
}

export interface TraineeForTable {
  traineeName: string,
  status: boolean
}

export interface NotAssignedTrainer {
  username: string,
  firstName: string,
  lastName: string,
  specialization: string
}
