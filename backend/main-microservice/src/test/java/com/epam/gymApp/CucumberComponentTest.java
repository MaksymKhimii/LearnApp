package com.epam.gymApp;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:feature/service/",
    glue = {"com.epam.gymApp.cucumber"})
public class CucumberComponentTest {

}
