package com.epam.gymApp.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.gymApp.model.User;
import com.epam.gymApp.repository.UserRepository;
import com.epam.gymApp.service.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ComponentSteps {

  private UserService userService;
  private UserRepository userRepository;
  private User result;

  private List<User> resultList;

  private String username;

  private Throwable exception;

  @Given("a user with ID {long} exists")
  public void a_user_with_ID_exists(Long userId) {
    userRepository = mock(UserRepository.class);
    userService = new UserService(userRepository);

    User mockUser = mock(User.class);
    when(userRepository.findById(eq(userId))).thenReturn(mockUser);
  }

  @When("the user tries to find a user by ID")
  public void the_user_tries_to_find_a_user_by_ID() {
    result = userService.findUserById(1L);
  }

  @Then("the user should get the expected user")
  public void the_user_should_get_the_expected_user() {
    assertNotNull(result);
  }

  @Given("there are multiple users in the system")
  public void there_are_multiple_users_in_the_system() {
    userRepository = mock(UserRepository.class);
    userService = new UserService(userRepository);

    List<User> mockUsers = new ArrayList<>();
    when(userRepository.findAll()).thenReturn(mockUsers);
  }

  @When("the user tries to find all users")
  public void the_user_tries_to_find_all_users() {
    resultList = userService.findAllUsers();
  }

  @Then("the user should get a list of all users")
  public void the_user_should_get_a_list_of_all_users() {
    assertNotNull(resultList);
  }

  @Given("there is an existing user with username {string}")
  public void there_is_an_existing_user_with_username(String username) {
    this.username = username;
    userRepository = mock(UserRepository.class);
    userService = new UserService(userRepository);

    User existingUser = User.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .username(username)
        .password("password")
        .isActive(true)
        .build();
    when(userRepository.findByUsername(username)).thenReturn(existingUser);
  }

  @When("the user tries to find the user by username {string}")
  public void the_user_tries_to_find_the_user_by_username(String username) {
    result = userService.findUserByUsername(username);
  }

  @Then("the user should get the existing user")
  public void the_user_should_get_the_existing_user() {
    assertNotNull(result);
    assertEquals(username, result.getUsername());
  }

  @Given("there is no user with username {string}")
  public void there_is_no_user_with_username(String username) {
    this.username = username;
    userRepository = mock(UserRepository.class);
    userService = new UserService(userRepository);

    when(userRepository.findByUsername(username)).thenReturn(null);
  }

  @When("the user tries to find the user by not existing username {string}")
  public void the_user_tries_to_find_the_user_by_not_existing_username(String username) {
    try {
      result = userService.findUserByUsername(username);
    } catch (Throwable e) {
      exception = e;
    }
  }

  @Then("the user should get a UserNotFoundException")
  public void the_user_should_get_a_UserNotFoundException() {
    assertNull(exception);
    assertNull(result);
  }
}
