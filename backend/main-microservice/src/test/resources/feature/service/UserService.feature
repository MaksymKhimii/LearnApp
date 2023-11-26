Feature: Testing UserService

  Scenario: Find a user by ID
    Given a user with ID 1 exists
    When the user tries to find a user by ID
    Then the user should get the expected user

  Scenario: Find all users
    Given there are multiple users in the system
    When the user tries to find all users
    Then the user should get a list of all users

  Scenario: Find a user by username when the user exists
    Given there is an existing user with username "existingUser"
    When the user tries to find the user by username "existingUser"
    Then the user should get the existing user

  Scenario: Find a user by username when the user does not exist
    Given there is no user with username "nonExistingUser"
    When the user tries to find the user by not existing username "nonExistingUser"
    Then the user should get a UserNotFoundException
