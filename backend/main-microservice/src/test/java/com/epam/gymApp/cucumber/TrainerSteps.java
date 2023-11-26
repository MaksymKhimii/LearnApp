package com.epam.gymApp.cucumber;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymApp.model.Trainer;
import com.epam.gymApp.model.User;
import com.epam.gymApp.repository.TrainerRepository;
import com.epam.gymApp.repository.UserRepository;
import com.epam.gymApp.service.TrainerService;
import com.epam.gymApp.service.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TrainerSteps {

  private TrainerRepository trainerRepository;
  private TrainerService trainerService;
  private UserRepository userRepository;
  private boolean isCredentialsValidResult;
  private User mockUser;
  private long trainerId;
  private Trainer mockTrainer;

  @Given("a trainer with username {string} exists")
  public void a_trainer_with_username_exists(String username) {
    userRepository = mock(UserRepository.class);
    trainerRepository = mock(TrainerRepository.class);
    UserService userService = mock(UserService.class);
    trainerService = new TrainerService(userService, trainerRepository);
    mockUser = mock(User.class);
    when(userRepository.findByUsername(username)).thenReturn(mockUser);
  }

  @Given("the trainer's password is {string}")
  public void the_trainer_password_is(String password) {
    User mockUser = userRepository.findByUsername("johndoe");
    when(mockUser.getPassword()).thenReturn(password);
  }

  @When("the trainer checks if the credentials are valid for username {string} and password {string}")
  public void the_trainer_checks_if_the_credentials_are_valid_for_username_and_password(
      String username, String password) {
    isCredentialsValidResult = trainerService.isTrainerCredentialsValid(username, password);
  }

  @Then("the result should be false")
  public void the_result_should_be_false() {
    assertFalse(isCredentialsValidResult);
  }

  @Given("a trainer with ID {long} exists")
  public void a_trainer_with_id_exists(long trainerId) {
    userRepository = mock(UserRepository.class);
    UserService userService = mock(UserService.class);
    trainerService = new TrainerService(userService, trainerRepository);

    this.trainerId = trainerId;
    mockUser = mock(User.class);
    mockTrainer = mock(Trainer.class);

    when(trainerRepository.findById(eq(trainerId))).thenReturn(mockTrainer);
    when(mockTrainer.getUser()).thenReturn(mockUser);
  }

  @When("the trainer updates their profile with new first name {string} and new last name {string}")
  public void the_trainer_updates_their_profile_with_new_first_name_and_new_last_name(
      String newFirstName, String newLastName) {
    trainerService.updateTrainerProfile(trainerId, newFirstName, newLastName);
  }

  @Then("the trainer's first name should be {string} and the last name should be {string}")
  public void the_trainer_first_name_should_be_and_the_last_name_should_be(String expectedFirstName,
      String expectedLastName) {
    verify(mockUser).setFirstName(expectedFirstName);
    verify(mockUser).setLastName(expectedLastName);
  }

  @Given("there is a trainer with ID {long}")
  public void there_is_a_trainer_with_id(long trainerId) {
    this.trainerId = trainerId;
    trainerRepository = mock(TrainerRepository.class);
    UserService userService = mock(UserService.class);
    trainerService = new TrainerService(userService, trainerRepository);

    mockUser = mock(User.class);
    mockTrainer = mock(Trainer.class);

    when(trainerRepository.findById(eq(trainerId))).thenReturn(mockTrainer);
    when(mockTrainer.getUser()).thenReturn(mockUser);
  }

  @When("the trainer is activated")
  public void the_trainer_is_activated() {
    trainerService.activateTrainer(trainerId);
  }

  @Then("the trainer should be marked as active")
  public void the_trainer_should_be_marked_as_active() {
    verify(mockUser, times(1)).setActive(true);
    verify(trainerRepository, times(1)).update(mockTrainer);
  }

  @When("the trainer is deactivated")
  public void the_trainer_is_deactivated() {
    trainerService.deactivateTrainer(trainerId);
  }

  @Then("the trainer should be marked as inactive")
  public void the_trainer_should_be_marked_as_inactive() {
    verify(mockUser, times(1)).setActive(false);
    verify(trainerRepository, times(1)).update(mockTrainer);
  }
}
