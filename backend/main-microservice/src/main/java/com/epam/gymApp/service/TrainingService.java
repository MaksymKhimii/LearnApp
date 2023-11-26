package com.epam.gymApp.service;

import com.epam.gymApp.model.Trainee;
import com.epam.gymApp.model.Trainer;
import com.epam.gymApp.model.Training;
import com.epam.gymApp.model.TrainingType;
import com.epam.gymApp.model.dto.TrainingDto;
import com.epam.gymApp.repository.TrainingRepository;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainingService {

  private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

  private final TrainingRepository trainingRepository;

  @Autowired
  private TraineeService traineeService;

  @Autowired
  private TrainerService trainerService;

  @Autowired
  private TrainingTypeService trainingTypeService;

  @Autowired
  public TrainingService(TrainingRepository trainingRepository) {
    this.trainingRepository = trainingRepository;
  }

  @Transactional
  public Training addTraining(TrainingDto trainingDto) {
    try {
      logger.info("Adding training: {}", trainingDto.getTrainingName());
      Trainer trainer = trainerService.findTrainerByUsername(trainingDto.getTrainerUsername());
      Trainee trainee = traineeService.findTraineeByUsername(trainingDto.getTraineeUsername());
      if (trainingDto.getTrainingName() == null) {
        logger.warn("Training name is missing. Cannot add training.");
        return null;
      }
      TrainingType trainingType = trainingTypeService.findTrainingTypeByName(
          trainingDto.getTrainingType());
      if (trainingType == null || (!Objects.equals(trainingType.getName(),
          trainer.getSpecialization().getName()))) {
        logger.warn("Training type is invalid. Cannot add training.");
        return null;
      }
      Training training = Training.builder()
          .name(trainingDto.getTrainingName())
          .trainee(trainee)
          .trainer(trainer)
          .trainingType(trainingType)
          .date(trainingDto.getDate())
          .duration(trainingDto.getDuration()
          )
          .build();
            /*logger.info("Training saved");
      List<Trainer> trainerList = trainee.getTrainers();
      logger.info("Trainers list has been got");
      boolean isTrainerAddedToList = false;
      if(trainerList.size()!=0) {
        for (Trainer t : trainerList) {
          if (Objects.equals(t.getUser().getUsername(), trainer.getUser().getUsername())) {
            logger.info("Trainer was found in list");
            isTrainerAddedToList = true;
            break;
          }
        }
      }
      if(!isTrainerAddedToList){
        trainerList.add(trainer);
        logger.info("trainer added to list");
      }
      List<String> usernameList = new ArrayList<>();
      usernameList.add(trainer.getUser().getUsername());
      TrainersListDto trainersListDto = new TrainersListDto(
        trainee.getUser().getUsername(),
          usernameList
      );
      traineeService.updateTraineeTrainersList(trainersListDto);
    //  traineeService.updateTraineeTrainersList(trainee.getId(), trainerList);
      logger.info("Trainer's list was updated");
      */
      trainingRepository.save(training);
      logger.info("Training added successfully.");
      return training;
    } catch (Exception e) {
      logger.error("Error occurred while adding training.", e);
      throw new RuntimeException("Failed to add training.", e);
    }
  }

  @Transactional
  public Training deleteTraining(TrainingDto trainingDto) {
    try {
      logger.info("Deleting training: {}", trainingDto.getTrainingName());
      if (trainingDto.getTrainingName() == null) {
        logger.warn("Training name is missing. Cannot delete training.");
        return null;
      }
      Training training = trainingRepository.findByTrainingName(trainingDto.getTrainingName());
      if (training == null) {
        logger.warn("Training not found. Cannot delete.");
        return null;
      }
      trainingRepository.delete(training);
      logger.info("Training deleted successfully.");
      return training;
    } catch (Exception e) {
      logger.error("Error occurred while deleting training.", e);
      throw new RuntimeException("Failed to delete training.", e);
    }
  }
}
