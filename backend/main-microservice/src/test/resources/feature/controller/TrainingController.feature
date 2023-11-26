Feature: Testing Training Controller

  Scenario: Add training successfully
    Given the trainee calls endpoint "/gym-app/auth/authenticate" with the following JSON request body:
      """
      {
        "username": "Maks_Khimii422",
        "password": "BKv2oNRA"
      }
      """
    When the trainee sends a POST request in order to authenticate
    Then the authentication response status code should be 200

    Given the user sends a request to "/gym-app/training/add" with the following JSON request:
    """
    {
        "traineeUsername": "Maks_Khimii422",
        "trainerUsername": "Dmytro_Yurikof607",
        "trainingName": "crossfit basic3",
        "date": "21/11/2023",
        "duration": 7.0,
        "trainingType": "crossfit"
    }
    """
    When the user performs a POST request
    Then the response status code should be 200

  Scenario: Add training failure
    Given the trainee calls endpoint "/gym-app/auth/authenticate" with the following JSON request body:
      """
      {
        "username": "Maks_Khimii422",
        "password": "BKv2oNRA"
      }
      """
    When the trainee sends a POST request in order to authenticate
    Then the authentication response status code should be 200

    Given the user sends a request to "/gym-app/training/add" with the following JSON request:
    """
    {
    }
    """
    When the user performs a POST request
    Then the response status code should be 400

  Scenario: Delete training successfully
    Given the trainee calls endpoint "/gym-app/auth/authenticate" with the following JSON request body:
      """
      {
        "username": "Maks_Khimii422",
        "password": "BKv2oNRA"
      }
      """
    When the trainee sends a POST request in order to authenticate
    Then the authentication response status code should be 200

    Given the user sends a request to "/gym-app/training/delete" with the following JSON request:
    """
    {
        "traineeUsername": "Maks_Khimii422",
        "trainerUsername": "Dmytro_Yurikof607",
        "trainingName": "crossfit basic3",
        "date": "21/11/2023",
        "duration": 7.0,
        "trainingType": "crossfit"
    }
    """
    When the user performs a DELETE request
    Then the response status code should be 200

  Scenario: Delete training failure
    Given the trainee calls endpoint "/gym-app/auth/authenticate" with the following JSON request body:
      """
      {
        "username": "Maks_Khimii422",
        "password": "BKv2oNRA"
      }
      """
    When the trainee sends a POST request in order to authenticate
    Then the authentication response status code should be 200

    Given the user sends a request to "/gym-app/training/delete" with the following JSON request:
    """
    {
    }
    """
    When the user performs a DELETE request
    Then the response status code should be 400