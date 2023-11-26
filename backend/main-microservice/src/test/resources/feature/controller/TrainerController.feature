Feature: Testing Trainer Profile Controller
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

  Scenario: Retrieve Trainer Profile
    Given the user sends a request to "/gym-app/trainers/profile/get" with the following JSON request:
      """
      {
      "username": "Dmytro_Yurikof607",
      "password": "80W8lZ71"
      }
      """
    When the user performs a GET request
    Then the response status code should be 200

  Scenario: Trainer profile not found
    Given the user sends a request to "/gym-app/trainers/profile/get" with the following JSON request:
      """
      {
        "username": "NonExistentUser",
        "password": "NonExistentPassword"
      }
      """
    When the user performs a GET request
    Then the response status code should be 404

  Scenario: Successful trainer training retrieval
    Given the user sends a request to "/gym-app/trainers/profile/trainings/get" with the following JSON request:
    """
    {
        "username": "Dmytro_Yurikof607",
        "password": "80W8lZ71",
        "periodFrom": "08/11/2022",
        "periodTo": "08/12/2023",
        "traineeName": "Maks_Khimii422",
        "trainingType": "crossfit"
    }
    """
    When the user performs a GET request
    Then the response status code should be 200

  Scenario: Trainer has no training history
    Given the user sends a request to "/gym-app/trainers/profile/trainings/get" with the following JSON request:
    """
    {
        "username": "TraineeWithNoTraining",
        "password": "80W8lZ71",
        "periodFrom": "08/11/2022",
        "periodTo": "08/12/2023",
        "traineeName": "Maks_Khimii422",
        "trainingType": "crossfit"
    }
    """
    When the user performs a GET request
    Then the response status code should be 404

  Scenario: Successful changing trainer's status
    Given the user sends a request to "/gym-app/trainers/profile/status/update" with the following JSON request:
    """
    {
        "username": "Dmytro_Yurikof607",
        "password": "80W8lZ71",
        "isActive": true
    }
    """
    When the user performs a PATCH request
    Then the response status code should be 200

  Scenario: Failure changing trainer's status
    Given the user sends a request to "/gym-app/trainers/profile/status/update" with the following JSON request:
    """
    {
        "username": "Dmytro_Yurikof607",
        "password": "80W8lZ71",
        "isActive": null
    }
    """
    When the user performs a PATCH request
    Then the response status code should be 400

  Scenario: Successful getting not assigned trainers
    Given the user sends a request to "/gym-app/trainers/available/get" with the following JSON request:
            """
      {
      "username": "Maks_Khimii422",
      "password": "BKv2oNRA"
      }
      """
    When the user performs a GET request
    Then the response status code should be 200

  Scenario: Successful updating trainer's profile
    Given the user sends a request to "/gym-app/trainers/profile/update" with the following JSON request:
       """
    {
        "username": "Dmytro_Yurikof607",
        "password": "80W8lZ71",
        "firstName": "Dmytro",
        "lastName": "Yurikof",
        "specialization": "crossfit",
        "isActive": true
    }
    """
    When the user performs a PUT request
    Then the response status code should be 200
