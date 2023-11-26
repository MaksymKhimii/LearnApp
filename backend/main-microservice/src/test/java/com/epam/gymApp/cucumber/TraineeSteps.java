package com.epam.gymApp.cucumber;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymApp.model.Trainee;
import com.epam.gymApp.model.User;
import com.epam.gymApp.repository.TraineeRepository;
import com.epam.gymApp.service.TraineeService;
import com.epam.gymApp.service.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TraineeSteps {

  private TraineeService traineeService;
  private TraineeRepository traineeRepository;
  private User mockUser;
  private Trainee mockTrainee;
  private long traineeId;

  @Given("a trainee with ID {long} exists")
  public void a_trainee_with_id_exists(long traineeId) {
    this.traineeId = traineeId;
    traineeRepository = mock(TraineeRepository.class);
    UserService userService = mock(UserService.class);
    traineeService = new TraineeService(traineeRepository, userService);

    mockUser = mock(User.class);
    mockTrainee = mock(Trainee.class);

    when(traineeRepository.findById(eq(traineeId))).thenReturn(mockTrainee);
    when(mockTrainee.getUser()).thenReturn(mockUser);
  }

  @When("the trainee changes their password to {string}")
  public void the_trainee_changes_their_password_to(String newPassword) {
    traineeService.changeTraineePassword(traineeId, newPassword);
  }

  @Then("the trainee's password should be {string}")
  public void the_trainee_password_should_be(String expectedPassword) {
    verify(mockUser, times(1)).setPassword(expectedPassword);
    verify(traineeRepository, times(1)).updatePassword(mockUser);
  }

  @When("the trainee updates their profile with new first name {string} and new last name {string}")
  public void the_trainee_updates_their_profile_with_new_first_name_and_new_last_name(
      String newFirstName, String newLastName) {
    traineeService.updateTraineeProfile(traineeId, newFirstName, newLastName);
  }

  @Then("the trainee's first name should be {string} and the last name should be {string}")
  public void the_trainee_first_name_should_be_and_the_last_name_should_be(String expectedFirstName,
      String expectedLastName) {
    verify(mockUser, times(1)).setFirstName(expectedFirstName);
    verify(mockUser, times(1)).setLastName(expectedLastName);
    verify(traineeRepository, times(1)).update(mockTrainee);
  }

  @Given("there is no trainee with ID {long}")
  public void there_is_no_trainee_with_id(long traineeId) {
    this.traineeId = traineeId;
    traineeRepository = mock(TraineeRepository.class);
    UserService userService = mock(UserService.class);
    traineeService = new TraineeService(traineeRepository, userService);

    when(traineeRepository.findById(eq(traineeId))).thenReturn(null);
  }

  @Then("the trainee profile should not be updated")
  public void the_trainee_profile_should_not_be_updated() {
    verify(traineeRepository, never()).update(any(Trainee.class));
  }

  @Given("there is a trainee with ID {long}")
  public void there_is_a_trainee_with_id(long traineeId) {
    this.traineeId = traineeId;
    traineeRepository = mock(TraineeRepository.class);
    UserService userService = mock(UserService.class);
    traineeService = new TraineeService(traineeRepository, userService);

    mockTrainee = mock(Trainee.class);
    mockUser = mock(User.class);

    when(traineeRepository.findById(eq(traineeId))).thenReturn(mockTrainee);
    when(mockTrainee.getUser()).thenReturn(mockUser);
  }

  @When("the trainee is activated")
  public void the_trainee_is_activated() {
    traineeService.activateTrainee(traineeId);
  }

  @Then("the trainee should be marked as active")
  public void the_trainee_should_be_marked_as_active() {
    verify(mockUser, times(1)).setActive(true);
    verify(traineeRepository, times(1)).update(mockTrainee);
  }

  @When("the trainee is deactivated")
  public void the_trainee_is_deactivated() {
    traineeService.deactivateTrainee(traineeId);
  }

  @Then("the trainee should be marked as inactive")
  public void the_trainee_should_be_marked_as_inactive() {
    verify(mockUser, times(1)).setActive(false);
    verify(traineeRepository, times(1)).update(mockTrainee);
  }
}
