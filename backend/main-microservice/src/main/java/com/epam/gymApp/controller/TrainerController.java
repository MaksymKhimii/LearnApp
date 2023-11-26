package com.epam.gymApp.controller;

import com.epam.gymApp.error.GymAppError;
import com.epam.gymApp.model.Trainer;
import com.epam.gymApp.model.dto.ChangeStatusDto;
import com.epam.gymApp.model.dto.NotAssignedTrainersDto;
import com.epam.gymApp.model.dto.TrainerProfileDto;
import com.epam.gymApp.model.dto.TrainerRegistrationDto;
import com.epam.gymApp.model.dto.TrainerTrainingDto;
import com.epam.gymApp.model.dto.TrainingInfoDto;
import com.epam.gymApp.model.dto.TrainingInfoDto2;
import com.epam.gymApp.model.dto.UpdateTrainerDto;
import com.epam.gymApp.model.dto.UserDto;
import com.epam.gymApp.service.TrainerService;
import com.epam.gymApp.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for managing trainer-related actions.
 * <p>
 * This controller handles operations such as trainer registration, profile management, and status
 * updates.
 *
 * @see TrainerRegistrationDto
 * @see ChangeStatusDto
 * @see UpdateTrainerDto
 * @see TrainerService
 * @see UserService
 */
@RestController
@RequestMapping("/gym-app/trainers")
@NoArgsConstructor
public class TrainerController {

  private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);

  @Autowired
  private TrainerService trainerService;

  /**
   * Change Trainer's status.
   * <p>
   * This method allows for changing the status of a Trainer based on the provided ChangeStatusDto.
   * If the status change is successful, returns ResponseEntity with HttpStatus OK. If the status
   * change fails, returns ResponseEntity with HttpStatus BAD_REQUEST.
   *
   * @param traineeDto Data transfer object containing Trainer status change details.
   * @return ResponseEntity with HttpStatus OK on successful status change, or BAD_REQUEST on
   * failure.
   */
  @PatchMapping("/profile/status/update")
  public ResponseEntity<HttpStatus> changeTraineeStatus(@RequestBody ChangeStatusDto traineeDto) {
    try {
      logger.info("Changing status for trainee with username: {}", traineeDto.getUsername());

      if (traineeDto == null) {
        logger.warn("Invalid request to change status for trainee.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      if (traineeDto.getUsername() == null || traineeDto.getIsActive() == null) {
        logger.warn("Invalid request to change status for trainee.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      trainerService.updateTraineeStatus(traineeDto);

      logger.info("Status changed successfully for trainee with username: {}",
          traineeDto.getUsername());
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      logger.error("Failed to change trainee status.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Update Trainer's profile information.
   * <p>
   * This method allows for updating a Trainer's profile information based on the provided
   * UpdateTrainerDto. If the profile update is successful, returns ResponseEntity with updated
   * TrainerProfileDto and HttpStatus OK. If the profile update fails, returns ResponseEntity with
   * HttpStatus BAD_REQUEST.
   *
   * @param updateTrainerDto Data transfer object containing updated Trainer profile information.
   * @return ResponseEntity containing updated TrainerProfileDto on successful update, or
   * BAD_REQUEST on failure.
   */
  @PutMapping("/profile/update")
  public ResponseEntity<TrainerProfileDto> updateTrainerProfile(
      @RequestBody UpdateTrainerDto updateTrainerDto) {
    try {
      logger.info("Updating profile for trainer with username: {}", updateTrainerDto.getUsername());

      Trainer trainer = trainerService.updateTrainerProfile(updateTrainerDto);
      if (trainer == null) {
        logger.warn("No profile found for trainer with username: {}",
            updateTrainerDto.getUsername());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      TrainerProfileDto dto = convertToTrainerProfileDto(trainer);

      logger.info("Profile updated successfully for trainer with username: {}",
          updateTrainerDto.getUsername());
      return new ResponseEntity<>(dto, HttpStatus.OK);
    } catch (Exception e) {
      logger.error("Failed to update trainer profile.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Retrieve a list of not assigned trainers.
   * <p>
   * This method retrieves a list of trainers who are not currently assigned, based on the provided
   * UserDto. If not assigned trainers are found, returns ResponseEntity with list of
   * NotAssignedTrainersDto objects and HttpStatus OK. If no trainers are found, returns
   * ResponseEntity with GymAppError and HttpStatus NOT_FOUND.
   *
   * @param dto Data transfer object containing user details for retrieval of not assigned
   *            trainers.
   * @return ResponseEntity with list of NotAssignedTrainersDto on successful retrieval, or
   * GymAppError with HttpStatus NOT_FOUND.
   */
  @PostMapping("/available/get")
  public ResponseEntity<?> getNotAssignedTrainers(
      @RequestBody UserDto dto) {
    try {
      logger.info("Fetching not assigned trainers for user with username: {}", dto.getUsername());

      List<Trainer> trainers = trainerService.findAllNotAssignedTrainers(dto.getUsername());
      if (trainers != null) {
        List<NotAssignedTrainersDto> listDto = new ArrayList<>();
        for (Trainer trainer : trainers) {
          NotAssignedTrainersDto trainerDto = NotAssignedTrainersDto.builder()
              .username(trainer.getUser().getUsername())
              .firstName(trainer.getUser().getFirstName())
              .lastName(trainer.getUser().getLastName())
              .specialization(trainer.getSpecialization().getName())
              .build();
          listDto.add(trainerDto);
        }

        logger.info("Not assigned trainers fetched successfully for user with username: {}",
            dto.getUsername());
        return new ResponseEntity<>(listDto, HttpStatus.OK);
      }

      logger.warn("No available trainers found for user with username: {}", dto.getUsername());
      return new ResponseEntity<>(new GymAppError(HttpStatus.NOT_FOUND.value(),
          "Available trainers for user with username " + dto.getUsername() + " was not found"),
          HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      logger.error("Failed to fetch not assigned trainers.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Retrieve a trainer's profile information.
   * <p>
   * This method retrieves the profile information of a trainer based on the provided UserDto. If
   * the trainer's profile is found, returns ResponseEntity with TrainerProfileDto and HttpStatus
   * OK. If the trainer's profile is not found, returns ResponseEntity with GymAppError and
   * HttpStatus NOT_FOUND.
   *
   * @param userDto Data transfer object containing user details for retrieval of trainer's
   *                profile.
   * @return ResponseEntity with TrainerProfileDto on successful retrieval, or GymAppError with
   * HttpStatus NOT_FOUND.
   */
  @PostMapping("/profile/get")
  public ResponseEntity<?> getTrainerProfile(@RequestBody UserDto userDto) {
    try {
      logger.info("Fetching trainer profile for username: {}", userDto.getUsername());

      Trainer trainer = trainerService.getTraineeProfileByUsername(userDto.getUsername());
      if (trainer != null) {
        TrainerProfileDto dto = convertToTrainerProfileDto(trainer);

        logger.info("Trainer profile fetched successfully for username: {}", userDto.getUsername());
        return new ResponseEntity<>(dto, HttpStatus.OK);
      }

      logger.warn("No trainer found with username: {}", userDto.getUsername());
      return new ResponseEntity<>(new GymAppError(HttpStatus.NOT_FOUND.value(),
          "Trainer with username " + userDto.getUsername() + " was not found"),
          HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      logger.error("Failed to fetch trainer profile.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private TrainerProfileDto convertToTrainerProfileDto(Trainer trainer) {
    return TrainerProfileDto.builder()
        .firstName(trainer.getUser().getFirstName())
        .lastName(trainer.getUser().getLastName())
        .specialization(trainer.getSpecialization().getName())
        .isActive(trainer.getUser().isActive())
        .traineesList(trainer.getTrainees())
        .build();
  }

  /**
   * Retrieve trainer's training information.
   * <p>
   * This method retrieves training information for a trainer based on the provided
   * TrainerTrainingDto. If the training information is found, returns ResponseEntity with list of
   * TrainingInfoDto objects and HttpStatus OK. If no training information is found, returns
   * ResponseEntity with HttpStatus NOT_FOUND.
   *
   * @param trainerTrainingsDto Data transfer object containing details for retrieval of trainer's
   *                            training information.
   * @return ResponseEntity with list of TrainingInfoDto on successful retrieval, or HttpStatus
   * NOT_FOUND.
   */
  @PostMapping("/profile/trainings/get")
  public ResponseEntity<List<TrainingInfoDto>> getTrainerTrainings(
      @RequestBody TrainerTrainingDto trainerTrainingsDto) {
    try {
      logger.info("Fetching trainer trainings for trainer with username: {}",
          trainerTrainingsDto.getUsername());

      List<TrainingInfoDto> trainings = trainerService.getTrainerTrainings(trainerTrainingsDto);
      if (trainings == null) {
        logger.info("Trainer trainings fetched successfully for trainer with username: {}",
            trainerTrainingsDto.getUsername());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      logger.warn("No trainer trainings found for trainer with username: {}",
          trainerTrainingsDto.getUsername());
      return ResponseEntity.ok(trainings);
    } catch (Exception e) {
      logger.error("Failed to fetch trainer trainings.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/profile/trainings/all/get")
  public ResponseEntity<List<TrainingInfoDto2>> getTrainerAllTrainings(
      @RequestBody UserDto userDto) {
    try {
      logger.info("Getting trainings for trainer with username: {}",
          userDto.getUsername());
      List<TrainingInfoDto2> trainings = trainerService.getTraineeAllTrainings(userDto);
      if (trainings == null) {
        logger.warn("No trainings found for trainer with username: {}",
            userDto.getUsername());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      logger.info("Trainings retrieved successfully for trainer with username: {}",
          userDto.getUsername());
      return ResponseEntity.ok(trainings);
    } catch (Exception e) {
      logger.error("Failed to get trainer trainings.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
