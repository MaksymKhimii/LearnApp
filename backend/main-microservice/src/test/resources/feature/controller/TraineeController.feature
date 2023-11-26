Feature: Testing Trainee Profile Controller
  Background: Authenticate user before tests
    Given the trainee calls endpoint "/gym-app/auth/authenticate" with the following JSON request body:
      """
      {
        "username": "Maks_Khimii422",
        "password": "BKv2oNRA"
      }
      """
    When the trainee sends a POST request in order to authenticate
    Then the authentication response status code should be 200

  Scenario: Retrieve Trainee Profile
    Given the user sends a request to "/gym-app/trainees/profile/get" with the following JSON request:
      """
      {
        "username": "Maks_Khimii422",
        "password": "BKv2oNRA"
      }
      """
    When the user performs a GET request
    Then the response status code should be 200

  Scenario: Trainee profile not found
    Given the user sends a request to "/gym-app/trainees/profile/get" with the following JSON request:
      """
      {
        "username": "NonExistentUser",
        "password": "NonExistentPassword"
      }
      """
    When the user performs a GET request
    Then the response status code should be 403

  Scenario: Successful trainee training retrieval
    Given the user sends a request to "/gym-app/trainees/profile/trainings/get" with the following JSON request:
    """
    {
        "username": "Maks_Khimii422",
        "password": "BKv2oNRA",
        "periodFrom": "08/11/2022",
        "periodTo": "08/12/2023",
        "trainerName": "Dmytro_Yurikof607",
        "trainingType": "crossfit"
    }
    """
    When the user performs a GET request
    Then the response status code should be 200

  Scenario: Trainee has no training history
    Given the user sends a request to "/gym-app/trainees/profile/trainings/get" with the following JSON request:
    """
    {
        "username": "TraineeWithNoTraining",
        "password": "password",
        "periodFrom": "08/11/2022",
        "periodTo": "08/12/2023",
        "trainerName": "Dmytro_Yurikof607",
        "trainingType": "crossfit"
    }
    """
    When the user performs a GET request
    Then the response status code should be 404

  Scenario: Successful updating trainee's trainers list
    Given the user sends a request to "/gym-app/trainees/profile/trainers/update" with the following JSON request:
    """
    {
      "traineeUsername": "Maks_Khimii422",
      "trainerUsernameList": ["Jane_Smith915"]
    }
    """
    When the user performs a PUT request
    Then the response status code should be 200

  Scenario: Failure updating trainee's trainers list
    Given the user sends a request to "/gym-app/trainees/profile/trainers/update" with the following JSON request:
    """
    {
      "traineeUsername": "Maks_Khimii422",
      "trainerUsernameList": []
    }
    """
    When the user performs a PUT request
    Then the response status code should be 200

  Scenario: Successful changing trainee status
    Given the user sends a request to "/gym-app/trainees/profile/status/update" with the following JSON request:
    """
    {
        "username": "Maks_Khimii422",
        "password": "BKv2oNRA",
        "isActive": true
    }
    """
    When the user performs a PATCH request
    Then the response status code should be 200

  Scenario: Failure changing trainee status
    Given the user sends a request to "/gym-app/trainees/profile/status/update" with the following JSON request:
    """
    {
        "username": "Maks_Khimii422",
        "password": "BKv2oNRA",
        "isActive": null
    }
    """
    When the user performs a PATCH request
    Then the response status code should be 400

  Scenario: Successful deleting trainee profile
    Given the user sends a request to "/gym-app/trainees/profile/delete" with the following JSON request:
    """
    {
        "username": "Maks_Khimiy444",
        "password": "BKv2oNRA"
    }
    """
    When the user performs a DELETE request
    Then the response status code should be 200
