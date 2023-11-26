Feature: Authentication Controller

  Scenario: Successful authentication
    Given the client calls endpoint "/gym-app/auth/authenticate" with the following JSON request:
      """
      {
        "username": "Maks_Khimii422",
        "password": "BKv2oNRA"
      }
      """
    When the client sends a POST request
    Then the response status code is 200
    And the response body should contain a token

  Scenario: Invalid authentication
    Given the client calls endpoint "/gym-app/auth/authenticate" with the following JSON request:
      """
      {
        "username": "Maks_Khimii422",
        "password": "BKv2oNRf"
      }
      """
    When the client sends a POST request
    Then the response status code is 403


  Scenario: Successful trainee registration
    Given the client calls endpoint "/main-microservice/gym-app/auth/trainee/register" with the following JSON request:
      """
      {
        "firstName": "John99",
        "lastName": "Doe99",
        "dateOfBirth": "10/11/2023",
        "address": "123 Main St"
      }
      """
    When the client sends a POST request in order to register
    Then the registration response status code is 200

  Scenario: Invalid trainee registration request
    Given the client calls endpoint "/main-microservice/gym-app/auth/trainee/register" with the following JSON request:
      """
      {
        "firstName": null,
        "lastName": "Doe",
        "dateOfBirth": "10/11/2023",
        "address": "123 Main St"
      }
      """
    When the client sends a POST request
    Then the response status code is 403

  Scenario: Successful trainer registration
    Given the client calls endpoint "/main-microservice/gym-app/auth/trainer/register" with the following JSON request:
    """
    {
      "firstName": "Jane",
      "lastName": "Smith",
      "specialization": "crossfit"
    }
    """
    When the client sends a POST request in order to register
    Then the registration response status code is 200

  Scenario: Invalid trainer registration request
    Given the client calls endpoint "/main-microservice/gym-app/auth/trainer/register" with the following JSON request:
    """
    {
      "firstName": "Jane",
      "lastName": null,
      "specialization": "crossfit"
    }
    """
    When the client sends a POST request
    Then the response status code is 403

