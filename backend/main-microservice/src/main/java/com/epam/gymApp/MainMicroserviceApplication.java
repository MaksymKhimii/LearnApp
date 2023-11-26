package com.epam.gymApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {
    HibernateJpaAutoConfiguration.class
})
public class MainMicroserviceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MainMicroserviceApplication.class, args);
  }
}
