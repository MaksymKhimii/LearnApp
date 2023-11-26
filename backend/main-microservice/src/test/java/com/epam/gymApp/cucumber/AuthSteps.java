package com.epam.gymApp.cucumber;

import com.epam.gymApp.model.dto.UserRegistrationResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthSteps {

  @Value("${app.test.auth.base-path}")
  private String baseUrl;
  private ResponseEntity<String> lastResponse;
  private String requestBody;
  private String fullPath;
  private ResponseEntity<UserRegistrationResponse> userRegistrationResponse;

  @Given("the client calls endpoint {string} with the following JSON request:")
  public void givenClientCallsEndpointWithRequestBody(String endpoint, String jsonRequest) {
    this.fullPath = baseUrl + endpoint;
    this.requestBody = jsonRequest;
  }

  @When("the client sends a POST request")
  public void whenClientSendsPostRequest() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
    RestTemplate restTemplate = new RestTemplate();
    try {
      lastResponse = restTemplate.postForEntity(fullPath, entity, String.class);
      String token = extractTokenFromResponse(lastResponse.getBody());
    } catch (HttpClientErrorException e) {
      lastResponse = new ResponseEntity<>(e.getStatusCode());
    }
  }

  @When("the client sends a POST request in order to register")
  public void whenClientSendsPostRequestInOrderToRegisterTrainee() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
    RestTemplate restTemplate = new RestTemplate();
    try {
      userRegistrationResponse = restTemplate.postForEntity(fullPath, entity,
          UserRegistrationResponse.class);
    } catch (HttpClientErrorException e) {
      userRegistrationResponse = new ResponseEntity<>(e.getStatusCode());
    }
  }

  @Then("the registration response status code is {int}")
  public void thenTheRegistrationResponseStatusCodeIs(int expectedStatusCode) {
    Assertions.assertNotNull(userRegistrationResponse);
    Assertions.assertEquals(expectedStatusCode, userRegistrationResponse.getStatusCodeValue());
  }


  @Then("the response status code is {int}")
  public void thenTheResponseStatusCodeIs(int expectedStatusCode) {
    Assertions.assertNotNull(lastResponse);
    Assertions.assertEquals(expectedStatusCode, lastResponse.getStatusCodeValue());
  }

  @Then("the response body should contain a token")
  public void thenTheResponseBodyShouldContainToken() {
    Assertions.assertNotNull(lastResponse);
    Assertions.assertTrue(lastResponse.getBody().contains("token"));
  }

  @Then("the response body should be empty")
  public void thenTheResponseBodyShouldBeEmpty() {
    Assertions.assertNotNull(lastResponse);
    Assertions.assertEquals("", lastResponse.getBody());
  }

  private String extractTokenFromResponse(String authResponse) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(authResponse);
      if (jsonNode.has("token")) {
        return jsonNode.get("token").asText();
      } else {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }
}
