package com.epam.gymApp;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:feature/controller/",
    glue = {"com.epam.gymApp.cucumber"})
public class CucumberIntegrationTest {

}
