package com.epam.gymApp.controller;

import com.epam.gymApp.model.dto.AuthenticationRequest;
import com.epam.gymApp.model.dto.AuthenticationResponse;
import com.epam.gymApp.model.dto.TraineeRegistrationDto;
import com.epam.gymApp.model.dto.TrainerRegistrationDto;
import com.epam.gymApp.model.dto.UserRegistrationResponse;
import com.epam.gymApp.service.AuthenticationService;
import com.epam.gymApp.service.TraineeService;
import com.epam.gymApp.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication-related actions.
 * <p>
 * This controller handles user authentication and registration operations.
 *
 * @see com.epam.gymApp.config.SecurityConfig
 * @see com.epam.gymApp.filter.JwtAuthenticationFilter
 * @see com.epam.gymApp.listener.AuthenticationFailureListener
 * @see AuthenticationService
 * @see AuthenticationResponse
 * @see UserRegistrationResponse
 */
@RestController
@RequestMapping("/gym-app/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

  private final AuthenticationService authenticationService;
  private final TraineeService traineeService;

  private final TrainerService trainerService;

  @CrossOrigin
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }

  /**
   * Register a new Trainee.
   *
   * @param traineeRegistrationDto Data transfer object containing trainee registration details.
   * @return ResponseEntity containing user credentials upon successful registration.
   */
  @CrossOrigin
  @PostMapping("/trainee/register")
  public ResponseEntity<UserRegistrationResponse> registerTrainee(
      @RequestBody TraineeRegistrationDto traineeRegistrationDto) {
    try {
      logger.info("Received request to register trainee: {}", traineeRegistrationDto);

      if (traineeRegistrationDto == null) {
        logger.error("Trainee registration request is null.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      UserRegistrationResponse response = traineeService.register(traineeRegistrationDto);
      if (response != null) {
        logger.info("Trainee registration successful: {}", response);
        return ResponseEntity.ok(response);
      } else {
        logger.error("Failed to register trainee.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      logger.error("An error occurred during trainee registration.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Register a new Trainer.
   * <p>
   * This method registers a new Trainer based on the provided TrainerRegistrationDto. If the
   * registration is successful, returns ResponseEntity with user credentials and HttpStatus OK. If
   * the registration fails, returns ResponseEntity with HttpStatus BAD_REQUEST.
   *
   * @param trainerRegistrationDto Data transfer object containing trainer registration details.
   * @return ResponseEntity containing user credentials on successful registration, or BAD_REQUEST
   * on failure.
   */

  @CrossOrigin
  @PostMapping("/trainer/register")
  public ResponseEntity<UserRegistrationResponse> registerTrainer(
      @RequestBody TrainerRegistrationDto trainerRegistrationDto) {
    try {
      logger.info("Received request to register trainer: {}", trainerRegistrationDto);

      if (trainerRegistrationDto == null) {
        logger.error("Trainer registration request is null.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      UserRegistrationResponse response = trainerService.register(trainerRegistrationDto);
      if (response != null) {
        logger.info("Trainer registration successful: {}", response);
        return ResponseEntity.ok(response);
      } else {
        logger.error("Failed to register trainer.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      logger.error("An error occurred during trainer registration.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/actuator/health")
  public String healthCheck() {
    return "Health check passed!";
  }
}
