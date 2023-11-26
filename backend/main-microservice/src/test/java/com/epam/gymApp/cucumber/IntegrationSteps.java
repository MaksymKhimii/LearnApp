package com.epam.gymApp.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationSteps {

  @Value("${app.test.base-path}")
  private String basePath;

  @Autowired
  private TestRestTemplate restTemplate;
  private ResponseEntity<String> response;
  private String requestJson;
  private String url;
  private String fullPath;
  private String requestBody;
  private String newToken;

  @Given("the user sends a request to {string} with the following JSON request:")
  public void theUserSendsAGETRequestToWithTheFollowingJSONRequest(String path,
      String jsonRequest) {
    this.url = basePath + path;
    requestJson = jsonRequest;
  }

  @When("the user performs a GET request")
  public void theUserPerformsAGETRequest() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.set("Authorization", "Bearer " + newToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);
    response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
  }

  @When("the user performs a PUT request")
  public void theUserPerformsAPUTRequest() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.set("Authorization", "Bearer " + newToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);
    response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
  }

  @When("the user performs a PATCH request")
  public void theUserPerformsAPATCHRequest() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.set("Authorization", "Bearer " + newToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);
    response = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, String.class);
  }

  @When("the user performs a DELETE request")
  public void theUserPerformsADELETERequest() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.set("Authorization", "Bearer " + newToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);
    response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);
  }

  @When("the user performs a POST request")
  public void theUserPerformsAPOSTRequest() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.set("Authorization", "Bearer " + newToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);
    response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
  }

  @Then("the response status code should be {int}")
  public void theResponseStatusCodeShouldBe(int statusCode) {
    System.out.println(response.toString());
    assertThat(response.getStatusCodeValue()).isEqualTo(statusCode);
  }

  @Given("the trainee calls endpoint {string} with the following JSON request body:")
  public void givenClientCallsEndpointWithRequestBody(String endpoint, String jsonRequest) {
    this.fullPath = "http://localhost:8083/main-microservice" + endpoint;
    this.requestBody = jsonRequest;
  }


  @When("the trainee sends a POST request in order to authenticate")
  public void theTraineeSendsAPOSTRequestInOrderToAuthenticate() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
    try {
      RestTemplate restTemplate = new RestTemplate();
      response = restTemplate.postForEntity(fullPath,
          entity, String.class);
      newToken = extractTokenFromResponse(response.getBody());
    } catch (HttpClientErrorException e) {
      response = new ResponseEntity<>(e.getStatusCode());
    }
  }

  @Then("the authentication response status code should be {int}")
  public void checkAuthResponseStatusCode(int expectedStatusCode) {
    assertEquals(expectedStatusCode, response.getStatusCodeValue());
  }

  private String extractTokenFromResponse(String responseBody) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new InputSource(new StringReader(responseBody)));

      NodeList nodeList = doc.getElementsByTagName("token");
      if (nodeList.getLength() > 0) {
        Node tokenNode = nodeList.item(0);
        if (tokenNode.getNodeType() == Node.ELEMENT_NODE) {
          Element tokenElement = (Element) tokenNode;
          return tokenElement.getTextContent();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
