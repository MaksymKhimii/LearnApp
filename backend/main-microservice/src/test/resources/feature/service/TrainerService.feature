Feature: Testing Trainer Service

  Scenario: Invalid trainer credentials
    Given a trainer with username "johndoe" exists
    And the trainer's password is "password123"
    When the trainer checks if the credentials are valid for username "johndoe" and password "password"
    Then the result should be false

  Scenario: Activate Trainer
    Given there is a trainer with ID 1
    When the trainer is activated
    Then the trainer should be marked as active

  Scenario: Deactivate a trainer
    Given there is a trainer with ID 1
    When the trainer is deactivated
    Then the trainer should be marked as inactive

  Scenario: Update trainer's profile
    Given a trainer with ID 1 exists
    When the trainer updates their profile with new first name "NewFirstName" and new last name "NewLastName"
    Then the trainer's first name should be "NewFirstName" and the last name should be "NewLastName"
