package com.epam.gymApp.service;

import com.epam.gymApp.model.Trainer;
import com.epam.gymApp.model.Training;
import com.epam.gymApp.model.TrainingType;
import com.epam.gymApp.model.User;
import com.epam.gymApp.model.dto.ChangeStatusDto;
import com.epam.gymApp.model.dto.TrainerRegistrationDto;
import com.epam.gymApp.model.dto.TrainerTrainingDto;
import com.epam.gymApp.model.dto.TrainingInfoDto;
import com.epam.gymApp.model.dto.TrainingInfoDto2;
import com.epam.gymApp.model.dto.UpdateTrainerDto;
import com.epam.gymApp.model.dto.UserDto;
import com.epam.gymApp.model.dto.UserRegistrationResponse;
import com.epam.gymApp.repository.TrainerRepository;
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
public class TrainerService {

  private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);
  private final TrainerRepository trainerRepository;

  private final UserService userService;

  @Autowired
  private TrainingTypeService trainingTypeService;

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  public TrainerService(UserService userService, TrainerRepository trainerRepository) {
    this.userService = userService;
    this.trainerRepository = trainerRepository;
  }

  @Transactional
  public void createTrainerProfile(Trainer trainer) {
    try {
      logger.info("Creating trainer profile for username: {}", trainer.getUser().getUsername());
      if (userService.findByUsername(trainer.getUser().getUsername()) != null) {
        userService.save(trainer.getUser());
        trainerRepository.save(trainer);
        logger.info("Trainer profile created successfully.");
      } else {
        logger.warn("Username already exists. Trainer profile creation ignored.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while creating trainer profile.", e);
    }
  }

  @Transactional(readOnly = true)
  public boolean isTrainerCredentialsValid(String username, String password) {
    try {
      logger.info("Validating trainer credentials for username: {}", username);
      User user = userService.findByUsername(username);
      if (user != null && user.getPassword().equals(password)) {
        Trainer trainer = trainerRepository.findByUser(user);
        boolean isValid = trainer != null;
        logger.info("Trainer credentials validation result: {}", isValid);
        return isValid;
      }
      logger.info("Trainer credentials validation result: false");
      return false;
    } catch (Exception e) {
      logger.error("Error occurred while validating trainer credentials.", e);
      throw new RuntimeException("Failed to validate trainer credentials.", e);
    }
  }

  @Transactional
  public void changeTrainerPassword(Long trainerId, String newPassword) {
    try {
      logger.info("Changing password for trainer with ID: {}", trainerId);
      Trainer trainer = trainerRepository.findById(trainerId);
      if (trainer != null) {
        User user = trainer.getUser();
        user.setPassword(newPassword);
        userService.update(user);
        logger.info("Password changed successfully.");
      } else {
        logger.warn("Trainer not found. Password change ignored.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while changing trainer password.", e);
      throw new RuntimeException("Failed to change trainer password.", e);
    }
  }

  @Transactional
  public void updateTrainerProfile(Long trainerId, String newFirstName, String newLastName) {
    try {
      logger.info("Updating trainer profile for trainer with ID: {}", trainerId);
      Trainer trainer = trainerRepository.findById(trainerId);
      if (trainer != null) {
        trainer.getUser().setFirstName(newFirstName);
        trainer.getUser().setLastName(newLastName);
        trainerRepository.update(trainer);
        logger.info("Trainer profile updated successfully.");
      } else {
        logger.warn("Trainer not found. Profile update ignored.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while updating trainer profile.", e);
      throw new RuntimeException("Failed to update trainer profile.", e);
    }
  }

  public Trainer updateTrainerProfile(UpdateTrainerDto trainerDto) {
    try {
      logger.info("Updating trainer profile for username: {}", trainerDto.getUsername());
      Trainer trainer = trainerRepository.findTrainerByUsername(trainerDto.getUsername());
      if (trainer != null) {
        trainer.getUser().setPassword(passwordEncoder.encode(trainerDto.getPassword()));
        trainer.getUser().setFirstName(trainerDto.getFirstName());
        trainer.getUser().setLastName(trainerDto.getLastName());
        trainer.getSpecialization().setName(trainerDto.getSpecialization());
        trainer.getUser().setActive(trainerDto.isActive());
        trainerRepository.update(trainer);
        logger.info("Trainer profile updated successfully.");
        return trainer;
      } else {
        logger.warn("Trainer not found. Profile update ignored.");
        return null;
      }
    } catch (Exception e) {
      logger.error("Error occurred while updating trainer profile.", e);
      throw new RuntimeException("Failed to update trainer profile.", e);
    }
  }

  @Transactional
  public void activateTrainer(Long trainerId) {
    try {
      logger.info("Activating trainer with ID: {}", trainerId);
      Trainer trainer = trainerRepository.findById(trainerId);
      if (trainer != null) {
        trainer.getUser().setActive(true);
        trainerRepository.update(trainer);
        logger.info("Trainer activated successfully.");
      } else {
        logger.warn("Trainer not found. Activation ignored.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while activating trainer.", e);
      throw new RuntimeException("Failed to activate trainer.", e);
    }
  }

  @Transactional
  public void deactivateTrainer(Long trainerId) {
    try {
      logger.info("Deactivating trainer with ID: {}", trainerId);
      Trainer trainer = trainerRepository.findById(trainerId);
      if (trainer != null) {
        trainer.getUser().setActive(false);
        trainerRepository.update(trainer);
        logger.info("Trainer deactivated successfully.");
      } else {
        logger.warn("Trainer not found. Deactivation ignored.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while deactivating trainer.", e);
      throw new RuntimeException("Failed to deactivate trainer.", e);
    }
  }

  @Transactional
  public UserRegistrationResponse register(TrainerRegistrationDto trainerDto) {
    try {
      logger.info("Registering new trainer: {}",
          trainerDto.getFirstName() + " " + trainerDto.getLastName());

      String username = userService.generateUsername(trainerDto.getFirstName(),
          trainerDto.getLastName());
      if (userService.findUserByUsername(username) != null) {
        username = userService.generateUsername(trainerDto.getFirstName(),
            trainerDto.getLastName());
      }
      String password = userService.generatePassword();
      User user = User.builder()
          .firstName(trainerDto.getFirstName())
          .lastName(trainerDto.getLastName())
          .username(username)
          .password(passwordEncoder.encode(password))
          .isActive(true)
          .build();

      String token = authenticationService.register(user);
      if (token == null) {
        logger.warn("Registration failed. Token is null.");
        return null;
      }

      TrainingType trainingType = trainingTypeService.findTrainingTypeByName(
          trainerDto.getSpecialization());
      if (trainingType == null) {
        trainingType = TrainingType.builder()
            .name(trainerDto.getSpecialization())
            .build();
        trainingTypeService.save(trainingType);
      }

      Trainer newTrainer = Trainer.builder()
          .specialization(trainingType)
          .user(user)
          .build();
      trainerRepository.save(newTrainer);

      logger.info("Trainer registered successfully.");
      return new UserRegistrationResponse(
          username,
          password,
          token
      );
    } catch (Exception e) {
      logger.error("Error occurred while registering a trainer.", e);
      throw new RuntimeException("Failed to register a trainer.", e);
    }
  }

  @Transactional
  public void updateTraineeStatus(ChangeStatusDto trainerDto) {
    try {
      logger.info("Updating trainer status for username: {}", trainerDto.getUsername());
      Trainer trainer = trainerRepository.findTrainerByUsername(trainerDto.getUsername());
      boolean status = Boolean.parseBoolean(trainerDto.getIsActive());
      if (trainer != null) {
        trainer.getUser().setActive(status);
        trainerRepository.update(trainer);
        logger.info("Trainer status updated successfully.");
      } else {
        logger.warn("Trainer not found. Status update ignored.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while updating trainer status.", e);
      throw new RuntimeException("Failed to update trainer status.", e);
    }
  }

  @Transactional(readOnly = true)
  public Trainer getTraineeProfileByUsername(String username) {
    try {
      logger.info("Fetching trainer profile by username: {}", username);
      User user = trainerRepository.findUserByUsername(username);
      if (user != null) {
        Trainer trainer = trainerRepository.findByUser(user);
        if (trainer != null) {
          logger.info("Trainer profile found successfully.");
          return trainer;
        }
      }
      logger.warn("Trainer not found.");
      return null;
    } catch (Exception e) {
      logger.error("Error occurred while fetching trainer profile.", e);
      throw new RuntimeException("Failed to fetch trainer profile.", e);
    }
  }

  public List<Trainer> findAllNotAssignedTrainers(String username) {
    try {
      logger.info("Finding not assigned trainers for trainee: {}", username);
      List<Trainer> trainers = trainerRepository.findNotAssignedTrainersForTrainee(username);
      logger.info("Found {} not assigned trainers.", trainers.size());
      return trainers;
    } catch (Exception e) {
      logger.error("Error occurred while finding not assigned trainers.", e);
      throw new RuntimeException("Failed to find not assigned trainers.", e);
    }
  }

  public Trainer findTrainerByUsername(String username) {
    try {
      logger.info("Finding trainer by username: {}", username);
      Trainer trainer = trainerRepository.findTrainerByUsername(username);
      if (trainer != null) {
        logger.info("Trainer found successfully.");
      } else {
        logger.warn("Trainer not found.");
      }
      return trainer;
    } catch (Exception e) {
      logger.error("Error occurred while finding trainer by username.", e);
      throw new RuntimeException("Failed to find trainer by username.", e);
    }
  }

  public List<TrainingInfoDto> getTrainerTrainings(TrainerTrainingDto trainerTrainingsDto) {
    try {
      logger.info("Fetching trainer trainings for username: {}", trainerTrainingsDto.getUsername());
      List<TrainingInfoDto> trainingDtos = new ArrayList<>();
      Trainer trainer = trainerRepository.findTrainerByUsername(trainerTrainingsDto.getUsername());
      if (trainer == null) {
        logger.warn("Trainer not found. Cannot fetch trainer trainings.");
        return null;
      }
      if (!Objects.equals(trainer.getSpecialization().getName(),
          trainerTrainingsDto.getTrainingType())) {
        logger.warn("Trainer specialization does not match the specified training type.");
        return null;
      }
      for (Training training : trainer.getTrainings()) {
        LocalDate endDate = DateUtil.calculateEndDate(training.getDate(), training.getDuration());
        if (training.getDate().isAfter(trainerTrainingsDto.getPeriodFrom()) && endDate.isBefore(
            trainerTrainingsDto.getPeriodTo()) && Objects.equals(
            trainerTrainingsDto.getTrainingType(),
            training.getTrainingType().getName())) {
          TrainingInfoDto trainingInfoDto = TrainingInfoDto.builder()
              .trainingName(training.getName())
              .trainingDate(training.getDate())
              .trainingType(training.getTrainingType().getName())
              .trainingDuration(training.getDuration())
              .trainerName(training.getTrainer().getUser().getUsername())
              .build();
          trainingDtos.add(trainingInfoDto);
        }
      }
      logger.info("Fetched {} trainer trainings.", trainingDtos.size());
      return trainingDtos;
    } catch (Exception e) {
      logger.error("Error occurred while fetching trainer trainings.", e);
      throw new RuntimeException("Failed to fetch trainer trainings.", e);
    }
  }

  public List<TrainingInfoDto2> getTraineeAllTrainings(UserDto userDto) {
    try {
      logger.info("Retrieving trainee's trainings.");
      Trainer trainer = trainerRepository.findTrainerByUsername(userDto.getUsername());
      if (trainer == null) {
        logger.warn("Trainer not found. Cannot fetch trainer trainings.");
        return null;
      }
      List<TrainingInfoDto2> trainingInfoDtos = new ArrayList<>();
      List<Training> trainerTrainings = trainer.getTrainings();
      if (trainerTrainings == null) {
        return null;
      }
      for (Training training : trainerTrainings) {
        TrainingInfoDto2 trainingInfoDto = TrainingInfoDto2.builder()
            .trainingName(training.getName())
            .trainingDate(training.getDate())
            .trainingType(training.getTrainingType().getName())
            .trainingDuration(training.getDuration())
            .traineeName(training.getTrainee().getUser().getUsername())
            .build();
        trainingInfoDtos.add(trainingInfoDto);
      }
      if (trainingInfoDtos.size() == 0) {
        return null;
      }
      logger.info("Trainer's trainings retrieved successfully.");
      return trainingInfoDtos;
    } catch (Exception e) {
      logger.error("Error occurred while retrieving trainer's trainings.", e);
      throw new RuntimeException("Failed to retrieve trainer's trainings.", e);
    }
  }
}
