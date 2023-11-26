Feature: Trainee Service
  Scenario: Change Trainee Password
    Given a trainee with ID 1 exists
    When the trainee changes their password to "newPassword"
    Then the trainee's password should be "newPassword"

  Scenario: Update Trainee Profile
    Given a trainee with ID 1 exists
    When the trainee updates their profile with new first name "NewFirstName" and new last name "NewLastName"
    Then the trainee's first name should be "NewFirstName" and the last name should be "NewLastName"

  Scenario: Update Trainee Profile When Trainee Not Found
    Given there is no trainee with ID 1
    When the trainee updates their profile with new first name "NewFirstName" and new last name "NewLastName"
    Then the trainee profile should not be updated

  Scenario: Activate Trainee
    Given there is a trainee with ID 1
    When the trainee is activated
    Then the trainee should be marked as active

  Scenario: Deactivate Trainee
    Given there is a trainee with ID 1
    When the trainee is deactivated
    Then the trainee should be marked as inactive
