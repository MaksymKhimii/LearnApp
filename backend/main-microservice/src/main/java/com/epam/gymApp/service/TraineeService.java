package com.epam.gymApp.service;

import com.epam.gymApp.exception.TraineeNotFoundException;
import com.epam.gymApp.model.Trainee;
import com.epam.gymApp.model.Trainer;
import com.epam.gymApp.model.Training;
import com.epam.gymApp.model.User;
import com.epam.gymApp.model.dto.ChangeStatusDto;
import com.epam.gymApp.model.dto.TraineeRegistrationDto;
import com.epam.gymApp.model.dto.TraineeTrainingDto;
import com.epam.gymApp.model.dto.TrainersListDto;
import com.epam.gymApp.model.dto.TrainingInfoDto;
import com.epam.gymApp.model.dto.UpdateTraineeDto;
import com.epam.gymApp.model.dto.UserDto;
import com.epam.gymApp.model.dto.UserRegistrationResponse;
import com.epam.gymApp.repository.TraineeRepository;
import com.epam.gymApp.util.DateUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TraineeService {

  private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

  private final TraineeRepository traineeRepository;
  private final UserService userService;

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private TrainerService trainerService;

  @Autowired
  public TraineeService(TraineeRepository traineeRepository, UserService userService) {
    this.traineeRepository = traineeRepository;
    this.userService = userService;
  }

  @Transactional
  public UserRegistrationResponse register(TraineeRegistrationDto traineeDto) {
    try {
      logger.info("Registering a new trainee.");
      String username = userService.generateUsername(traineeDto.getFirstName(),
          traineeDto.getLastName());
      if (userService.findUserByUsername(username) != null) {
        username = userService.generateUsername(traineeDto.getFirstName(),
            traineeDto.getLastName());
      }
      String password = userService.generatePassword();
      User user = User.builder()
          .firstName(traineeDto.getFirstName())
          .lastName(traineeDto.getLastName())
          .username(username)
          .password(passwordEncoder.encode(password))
          .isActive(true)
          .build();
      String token = authenticationService.register(user);
      if (token == null) {
        logger.warn("Failed to register trainee. Token not generated.");
        return null;
      }
      Trainee newTrainee = Trainee.builder()
          .user(user)
          .dateOfBirth(traineeDto.getDateOfBirth())
          .address(traineeDto.getAddress())
          .build();
      traineeRepository.save(newTrainee);
      logger.info("Trainee registered successfully.");
      return new UserRegistrationResponse(
          username,
          password,
          token
      );
    } catch (Exception e) {
      logger.error("Error occurred during trainee registration.", e);
      return null;
    }
  }

  @Transactional(readOnly = true)
  public Trainee getTraineeProfileByUsername(String username) {
    try {
      logger.info("Getting trainee profile by username.");

      User user = traineeRepository.findUserByUsername(username);
      if (user == null) {
        logger.warn("Trainee with username {} not found.", username);
        return null;
      }

      Trainee trainee = traineeRepository.findByUser(user);

      logger.info("Trainee profile retrieved successfully.");
      logger.info("Trainee trainers: " + trainee.getTrainers());
      return trainee;
    } catch (Exception e) {
      logger.error("Error occurred while retrieving trainee profile.", e);
      return null;
    }
  }

  @Transactional
  public void changeTraineePassword(Long traineeId, String newPassword) {
    try {
      logger.info("Changing trainee's password.");
      Trainee trainee = traineeRepository.findById(traineeId);
      if (trainee != null) {
        User user = trainee.getUser();
        user.setPassword(newPassword);
        traineeRepository.updatePassword(user);
        logger.info("Trainee's password changed successfully.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while changing trainee's password.", e);
    }
  }


  @Transactional
  public void updateTraineeProfile(Long traineeId, String newFirstName, String newLastName) {
    try {
      logger.info("Updating trainee's profile.");

      Trainee trainee = traineeRepository.findById(traineeId);
      if (trainee != null) {
        trainee.getUser().setFirstName(newFirstName);
        trainee.getUser().setLastName(newLastName);
        traineeRepository.update(trainee);
        logger.info("Trainee's profile updated successfully.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while updating trainee's profile.", e);
    }
  }

  @Transactional
  public Trainee updateTraineeProfile(UpdateTraineeDto traineeDto) {
    try {
      logger.info("Updating trainee's profile using DTO.");

      Trainee trainee = traineeRepository.findTraineeByUsername(traineeDto.getUsername());
      if (trainee == null) {
        logger.warn("Trainee not found with username {}.", traineeDto.getUsername());
        return null;
      }
      trainee.getUser().setPassword(passwordEncoder.encode(traineeDto.getPassword()));
      trainee.getUser().setFirstName(traineeDto.getFirstName());
      trainee.getUser().setLastName(traineeDto.getLastName());
      trainee.getUser().setActive(traineeDto.isActive());
      trainee.setAddress(traineeDto.getAddress());
      trainee.setDateOfBirth(traineeDto.getDateOfBirth());
      traineeRepository.update(trainee);
      logger.info("Trainee's profile updated successfully.");
      return trainee;
    } catch (Exception e) {
      logger.error("Error occurred while updating trainee's profile using DTO.", e);
      return null;
    }
  }

  @Transactional
  public void activateTrainee(Long traineeId) {
    try {
      logger.info("Activating trainee.");
      Trainee trainee = traineeRepository.findById(traineeId);
      if (trainee != null) {
        trainee.getUser().setActive(true);
        traineeRepository.update(trainee);
        logger.info("Trainee activated successfully.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while activating trainee.", e);
    }
  }

  @Transactional
  public void deactivateTrainee(Long traineeId) {
    try {
      logger.info("Deactivating trainee.");

      Trainee trainee = traineeRepository.findById(traineeId);
      if (trainee != null) {
        trainee.getUser().setActive(false);
        traineeRepository.update(trainee);
        logger.info("Trainee deactivated successfully.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while deactivating trainee.", e);
    }
  }

  @Transactional
  public void deleteTraineeProfileByUsername(String username) {
    try {
      logger.info("Deleting trainee profile by username: {}", username);

      Trainee trainee = traineeRepository.findTraineeByUsername(username);
      if (trainee != null) {
        traineeRepository.delete(trainee);
        logger.info("Trainee profile deleted successfully.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while deleting trainee profile.", e);
    }
  }

  @Transactional(readOnly = true)
  public List<Training> getTraineeTrainingsByUsernameAndSpecialization(String username,
      String specialization) {
    Trainee trainee = traineeRepository.findTraineeByUsername(username);
    if (trainee != null) {
      return traineeRepository.findTrainingsByTraineeAndSpecialization(trainee, specialization);
    }
    return new ArrayList<>();
  }

  @Transactional
  public void updateTraineeTrainersList(Long traineeId, List<Trainer> trainers) {
    Trainee trainee = traineeRepository.findById(traineeId);
    if (trainee != null) {
      trainee.setTrainers(trainers);
      traineeRepository.update(trainee);
      return;
    }
    throw new TraineeNotFoundException(traineeId);
  }

  @Transactional
  public List<Trainer> updateTraineeTrainersList(TrainersListDto trainersListDto) {
    try {
      logger.info("Updating trainee's trainers list.");

      if (trainersListDto.getTrainerUsernameList() == null) {
        return null;
      }
      Trainee trainee = traineeRepository.findTraineeByUsername(
          trainersListDto.getTraineeUsername());
      List<Trainer> trainers = new ArrayList<>();
      for (String trainerUsername : trainersListDto.getTrainerUsernameList()) {
        Trainer trainer = trainerService.findTrainerByUsername(trainerUsername);
        if (trainee != null) {
          trainers.add(trainer);
        }
      }
      if (trainee != null) {
        trainee.setTrainers(trainers);
        traineeRepository.update(trainee);
        logger.info("Trainee's trainers list updated successfully.");
        return trainers;
      }
      throw new TraineeNotFoundException(trainersListDto.getTraineeUsername());
    } catch (Exception e) {
      logger.error("Error occurred while updating trainee's trainers list.", e);
      throw new RuntimeException("Failed to update trainee's trainers list.", e);
    }
  }

  @Transactional
  public void updateTraineeStatus(ChangeStatusDto traineeDto) {
    try {
      logger.info("Updating trainee's status.");
      Trainee trainee = traineeRepository.findTraineeByUsername(traineeDto.getUsername());
      boolean status = Boolean.parseBoolean(traineeDto.getIsActive());
      if (trainee != null) {
        trainee.getUser().setActive(status);
        traineeRepository.update(trainee);
        logger.info("Trainee's status updated successfully.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while updating trainee's status.", e);
      throw new RuntimeException("Failed to update trainee's status.", e);
    }
  }

  public Trainee findTraineeByUsername(String username) {
    return traineeRepository.findTraineeByUsername(username);
  }

  public List<TrainingInfoDto> getTraineeTrainings(TraineeTrainingDto trainingDto) {
    try {
      logger.info("Retrieving trainee's trainings.");
      Trainee trainee = getTraineeProfileByUsername(trainingDto.getUsername());
      if (trainee == null) {
        return null;
      }
      List<TrainingInfoDto> trainingInfoDtos = new ArrayList<>();
      List<Training> traineeTrainings = trainee.getTrainings();
      for (Training training : traineeTrainings) {
        LocalDate endDate = DateUtil.calculateEndDate(training.getDate(), training.getDuration());
        if (training.getDate().isAfter(trainingDto.getPeriodFrom()) && endDate.isBefore(
            trainingDto.getPeriodTo()) && Objects.equals(training.getTrainingType().getName(),
            trainingDto.getTrainingType())) {
          TrainingInfoDto trainingInfoDto = TrainingInfoDto.builder()
              .trainingName(training.getName())
              .trainingDate(training.getDate())
              .trainingType(training.getTrainingType().getName())
              .trainingDuration(training.getDuration())
              .trainerName(training.getTrainer().getUser().getUsername())
              .build();
          trainingInfoDtos.add(trainingInfoDto);
        }
      }
      if (trainingInfoDtos.size() == 0) {
        return null;
      }
      logger.info("Trainee's trainings retrieved successfully.");
      return trainingInfoDtos;
    } catch (Exception e) {
      logger.error("Error occurred while retrieving trainee's trainings.", e);
      throw new RuntimeException("Failed to retrieve trainee's trainings.", e);
    }
  }

  public List<TrainingInfoDto> getTraineeAllTrainings(UserDto userDto) {
    try {
      logger.info("Retrieving trainee's trainings.");
      Trainee trainee = getTraineeProfileByUsername(userDto.getUsername());
      if (trainee == null) {
        return null;
      }
      List<TrainingInfoDto> trainingInfoDtos = new ArrayList<>();
      List<Training> traineeTrainings = trainee.getTrainings();
      if (traineeTrainings == null) {
        return null;
      }
      for (Training training : traineeTrainings) {
        TrainingInfoDto trainingInfoDto = TrainingInfoDto.builder()
            .trainingName(training.getName())
            .trainingDate(training.getDate())
            .trainingType(training.getTrainingType().getName())
            .trainingDuration(training.getDuration())
            .trainerName(training.getTrainer().getUser().getUsername())
            .build();
        trainingInfoDtos.add(trainingInfoDto);
      }
      if (trainingInfoDtos.size() == 0) {
        return null;
      }
      logger.info("Trainee's trainings retrieved successfully.");
      return trainingInfoDtos;
    } catch (Exception e) {
      logger.error("Error occurred while retrieving trainee's trainings.", e);
      throw new RuntimeException("Failed to retrieve trainee's trainings.", e);
    }
  }
}
