package com.epam.gymApp.controller;

import com.epam.gymApp.model.ActionType;
import com.epam.gymApp.model.Training;
import com.epam.microservice.model.TrainerWorkloadDto;
import com.epam.gymApp.model.dto.TrainerWorkloadRequest;
import com.epam.gymApp.model.dto.TrainingDto;
import com.epam.gymApp.service.TrainingService;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing training-related actions.
 * <p>
 * This controller handles the addition of new training sessions.
 *
 * @see TrainingDto
 * @see TrainerWorkloadRequest
 * @see TrainingService
 */
@RestController
@RequestMapping("/gym-app/training")
@RequiredArgsConstructor
public class TrainingController {

  private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);

  private final TrainingService trainingService;

  private final SqsTemplate sqsTemplate;

  @Value("${cloud.aws.end-point.uri}")
  private String endpoint;

  /**
   * Add a new training session.
   * <p>
   * This method allows for the addition of a new training session based on the provided
   * TrainingDto. If the training session is successfully added, an OK status (HttpStatus.OK) is
   * returned. If the addition fails, a BAD_REQUEST status (HttpStatus.BAD_REQUEST) is returned. The
   * ReportMicroservice is also called here to calculate the general training duration
   *
   * @param trainingDto Data transfer object containing details of the training session to be
   *                    added.
   * @return ResponseEntity with HttpStatus OK on successful addition, or BAD_REQUEST on failure.
   */
  @PostMapping("/add")
  public ResponseEntity<HttpStatus> addTraining(@RequestBody TrainingDto trainingDto) {
    try {
      logger.info("Adding a new training.");

      Training newTraining = trainingService.addTraining(trainingDto);

      if (newTraining == null) {
        logger.warn("Failed to add new training to db.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      TrainerWorkloadRequest request = convertToTrainerWorkloadRequest(newTraining);
      request.setActionType(ActionType.ADD.toString());
      sqsTemplate.send(endpoint,
          new TrainerWorkloadDto(
              request.getTrainerUsername(),
              request.getTrainerFirstName(),
              request.getTrainerLastName(),
              request.isActive(),
              request.getTrainingDate().toString(),
              (double) request.getTrainingDuration().getSeconds(),
              request.getActionType()
          ));
      logger.info("New training added successfully.");
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (MessageDeliveryException e) {
      logger.error("Failed to send message to SQS queue: " + e.getMessage(), e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      logger.error("Failed to add training.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Deletes a training session based on the provided TrainingDto.
   * <p>
   * This method deletes the specified training session using the data in the provided TrainingDto.
   * If the training session is successfully deleted, an OK status (HttpStatus.OK) is returned. If
   * the deletion fails or the training session does not exist, a BAD_REQUEST status
   * (HttpStatus.BAD_REQUEST) is returned.
   * <p>
   * Additionally, this method notifies the report microservice about the deleted training session
   * by sending a TrainerWorkloadRequest with ActionType set to DELETE.
   *
   * @param trainingDto Data transfer object containing details of the training session to be
   *                    deleted.
   * @return ResponseEntity with HttpStatus OK on successful deletion, or BAD_REQUEST on failure.
   */
  @DeleteMapping("/delete")
  public ResponseEntity<HttpStatus> deleteTraining(@RequestBody TrainingDto trainingDto) {
    try {
      logger.info("Deleting training.");

      Training newTraining = trainingService.deleteTraining(trainingDto);

      if (newTraining == null) {
        logger.warn("Failed to delete training.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      TrainerWorkloadRequest request = convertToTrainerWorkloadRequest(newTraining);
      request.setActionType(ActionType.DELETE.toString());
      sqsTemplate.send(endpoint,
          new TrainerWorkloadDto(
              request.getTrainerUsername(),
              request.getTrainerFirstName(),
              request.getTrainerLastName(),
              request.isActive(),
              request.getTrainingDate().toString(),
              (double) request.getTrainingDuration().getSeconds(),
              request.getActionType()
          )
      );
      logger.info("Training deleted successfully.");
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      logger.error("Failed to delete training.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Converts a Training object into a TrainerWorkloadRequest object.
   *
   * @param training The Training object to be converted.
   * @return A TrainerWorkloadRequest object representing the training details.
   */
  private TrainerWorkloadRequest convertToTrainerWorkloadRequest(Training training) {
    return TrainerWorkloadRequest.builder()
        .trainerUsername(training.getTrainer().getUser().getUsername())
        .trainerFirstName(training.getTrainer().getUser().getFirstName())
        .trainerLastName(training.getTrainer().getUser().getLastName())
        .isActive(training.getTrainer().getUser().isActive())
        .trainingDate(training.getDate())
        .trainingDuration(training.getDuration())
        .build();
  }
}
