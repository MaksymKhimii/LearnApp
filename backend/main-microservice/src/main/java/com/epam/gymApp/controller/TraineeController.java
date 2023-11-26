package com.epam.gymApp.controller;

import com.epam.gymApp.error.GymAppError;
import com.epam.gymApp.model.Trainee;
import com.epam.gymApp.model.Trainer;
import com.epam.gymApp.model.dto.ChangeStatusDto;
import com.epam.gymApp.model.dto.TraineeProfileDto;
import com.epam.gymApp.model.dto.TraineeTrainingDto;
import com.epam.gymApp.model.dto.TrainerDto;
import com.epam.gymApp.model.dto.TrainersListDto;
import com.epam.gymApp.model.dto.TrainingInfoDto;
import com.epam.gymApp.model.dto.UpdateTraineeDto;
import com.epam.gymApp.model.dto.UserDto;
import com.epam.gymApp.service.TraineeService;
import com.epam.gymApp.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing actions related to Trainees.
 * <p>
 * This controller handles various operations such as trainee registration, profile management,
 * status updates, and retrieval of trainings and profile information.
 *
 * @see Trainee
 * @see TraineeService
 * @see UserService
 */
@RestController
@RequestMapping("/gym-app/trainees")
@NoArgsConstructor
public class TraineeController {

  private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);
  @Autowired
  private TraineeService traineeService;

  /**
   * Update Trainee profile information.
   *
   * @param updateTraineeDto Data transfer object containing updated trainee profile information.
   * @return ResponseEntity containing updated TraineeProfileDto upon successful update.
   */
  @PutMapping("/profile/update")
  public ResponseEntity<?> updateTraineeProfile(
      @RequestBody UpdateTraineeDto updateTraineeDto) {
    try {
      logger.info("Updating trainee profile for username: {}", updateTraineeDto.getUsername());

      Trainee trainee = traineeService.updateTraineeProfile(updateTraineeDto);
      if (trainee == null) {
        logger.warn("Trainee profile not found for username: {}", updateTraineeDto.getUsername());
        return new ResponseEntity<>(new GymAppError(HttpStatus.NOT_FOUND.value(),
            "Cannot update profile for trainee with username: " + updateTraineeDto.getUsername()),
            HttpStatus.NOT_FOUND);
      }

      logger.info("Trainee profile updated successfully for username: {}",
          updateTraineeDto.getUsername());
      return new ResponseEntity<>(convertToTraineeProfileDto(trainee), HttpStatus.OK);
    } catch (Exception e) {
      logger.error("Failed to update trainee profile.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Deletes a trainee's profile based on the provided user data.
   *
   * @param userDto User data containing information for profile deletion.
   * @return ResponseEntity with HTTP status OK upon successful deletion or with HTTP status
   * BAD_REQUEST and a GymAppError object in case of an error.
   */
  @DeleteMapping("/profile/delete")
  public ResponseEntity<?> deleteTraineeProfile(@RequestBody UserDto userDto) {
    try {
      logger.info("Deleting trainee profile for username: {}", userDto.getUsername());
      if (userDto == null) {
        logger.warn("UserDto is null, cannot delete trainee profile.");
        return new ResponseEntity<>(new GymAppError(HttpStatus.NOT_FOUND.value(),
            "Cannot delete this trainee's profile"),
            HttpStatus.BAD_REQUEST);
      }

      traineeService.deleteTraineeProfileByUsername(userDto.getUsername());

      logger.info("Trainee profile deleted successfully for username: {}", userDto.getUsername());

      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      logger.error("Failed to delete trainee profile.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Changes a trainee's status based on the provided data.
   *
   * @param traineeDto Data for changing the trainee's status.
   * @return ResponseEntity with HTTP status OK upon successful status change or with HTTP status
   * BAD_REQUEST and a GymAppError object in case of an error.
   */
  @PatchMapping("/profile/status/update")
  public ResponseEntity<?> changeTraineeStatus(@RequestBody ChangeStatusDto traineeDto) {
    try {
      logger.info("Changing status for trainee with username: {}", traineeDto.getUsername());

      if (traineeDto == null || traineeDto.getIsActive() == null) {
        logger.warn("TraineeDto is null or IsActive is null, cannot change status.");

        return new ResponseEntity<>(new GymAppError(HttpStatus.NOT_FOUND.value(),
            "Cannot change status for this trainee"),
            HttpStatus.BAD_REQUEST);
      }

      traineeService.updateTraineeStatus(traineeDto);

      logger.info("Trainee status changed successfully for username: {}", traineeDto.getUsername());
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      logger.error("Failed to change trainee status.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Updates a trainee's list of trainers based on the provided data.
   *
   * @param trainersListDto Data for updating the trainee's list of trainers.
   * @return ResponseEntity with a list of TrainerDto objects and HTTP status OK upon successful
   * update, or ResponseEntity with HTTP status BAD_REQUEST in case of an error.
   */
  @PutMapping("profile/trainers/update")
  public ResponseEntity<List<TrainerDto>> updateTraineesTrainersList(
      @RequestBody TrainersListDto trainersListDto) {
    try {
      logger.info("Updating trainers list for trainee with username: {}",
          trainersListDto.getTraineeUsername());

      List<Trainer> trainers = traineeService.updateTraineeTrainersList(trainersListDto);
      if (trainers == null) {
        logger.warn("Trainers list is null, cannot update.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      List<TrainerDto> trainerDtoList = convertTrainersToTrainerDto(trainers);

      logger.info("Trainers list updated successfully for trainee with username: {}",
          trainersListDto.getTraineeUsername());
      return new ResponseEntity<>(trainerDtoList, HttpStatus.OK);
    } catch (Exception e) {
      logger.error("Failed to update trainee trainers list.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private List<TrainerDto> convertTrainersToTrainerDto(List<Trainer> trainers) {
    List<TrainerDto> trainerDtoList = new ArrayList<>();
    for (Trainer trainer : trainers) {
      TrainerDto trainerDto = TrainerDto.builder()
          .username(trainer.getUser().getUsername())
          .firstName(trainer.getUser().getFirstName())
          .lastName(trainer.getUser().getLastName())
          .specialization(trainer.getSpecialization().getName())
          .build();
      trainerDtoList.add(trainerDto);
    }
    return trainerDtoList;
  }

  /**
   * Method to retrieve a list of TrainingInfoDto for a specific Trainee.
   * <p>
   * This method retrieves a list of training information for a given Trainee based on the provided
   * TraineeTrainingDto. The Trainee's training details include training name, type, date, and
   * duration.
   *
   * @param traineeTrainingDto Data transfer object containing details for retrieving Trainee's
   *                           trainings.
   * @return ResponseEntity containing a list of TrainingInfoDto objects on successful retrieval.
   * Returns NOT_FOUND status if no trainings are found.
   */
  @PostMapping("/profile/trainings/get")
  public ResponseEntity<List<TrainingInfoDto>> getTraineeTrainings(
      @RequestBody TraineeTrainingDto traineeTrainingDto) {
    try {
      logger.info("Getting trainings for trainee with username: {}",
          traineeTrainingDto.getUsername());

      List<TrainingInfoDto> trainings = traineeService.getTraineeTrainings(traineeTrainingDto);

      if (trainings == null) {
        logger.warn("No trainings found for trainee with username: {}",
            traineeTrainingDto.getUsername());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      logger.info("Trainings retrieved successfully for trainee with username: {}",
          traineeTrainingDto.getUsername());
      return ResponseEntity.ok(trainings);
    } catch (Exception e) {
      logger.error("Failed to get trainee trainings.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/profile/trainings/all/get")
  public ResponseEntity<List<TrainingInfoDto>> getTraineeAllTrainings(
      @RequestBody UserDto userDto) {
    try {
      logger.info("Getting trainings for trainee with username: {}",
          userDto.getUsername());
      List<TrainingInfoDto> trainings = traineeService.getTraineeAllTrainings(userDto);
      if (trainings == null) {
        logger.warn("No trainings found for trainee with username: {}",
            userDto.getUsername());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      logger.info("Trainings retrieved successfully for trainee with username: {}",
          userDto.getUsername());
      return ResponseEntity.ok(trainings);
    } catch (Exception e) {
      logger.error("Failed to get trainee trainings.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Controller method to retrieve the profile information of a Trainee.
   * <p>
   * This method retrieves the profile information of a Trainee based on the provided UserDto, which
   * contains the username of the Trainee. If the provided username is valid and the Trainee is
   * authenticated, the method returns the Trainee's profile information in a TraineeProfileDto.
   *
   * @param userDto Data transfer object containing the username for profile retrieval.
   * @return ResponseEntity containing TraineeProfileDto with Trainee's profile information on
   * successful retrieval. Returns NOT_FOUND status if the username is invalid or authentication
   * fails.
   */
  @PostMapping("/profile/get")
  public ResponseEntity<TraineeProfileDto> getTraineeProfile(@RequestBody UserDto userDto) {
    try {
      logger.info("Getting profile for trainee with username: {}", userDto.getUsername());

      Trainee trainee = traineeService.getTraineeProfileByUsername(userDto.getUsername());
      TraineeProfileDto dto = convertToTraineeProfileDto(trainee);

      if (dto == null) {
        logger.warn("No profile found for trainee with username: {}", userDto.getUsername());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      logger.info("Profile retrieved successfully for trainee with username: {}",
          userDto.getUsername());
      return new ResponseEntity<>(dto, HttpStatus.OK);
    } catch (Exception e) {
      logger.error("Failed to get trainee profile.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private TraineeProfileDto convertToTraineeProfileDto(Trainee trainee) {
    return TraineeProfileDto.builder()
        .firstName(trainee.getUser().getFirstName())
        .lastName(trainee.getUser().getLastName())
        .dateOfBirth(trainee.getDateOfBirth())
        .address(trainee.getAddress())
        .isActive(trainee.getUser().isActive())
        .trainersList(trainee.getTrainers())
        .build();
  }
}
