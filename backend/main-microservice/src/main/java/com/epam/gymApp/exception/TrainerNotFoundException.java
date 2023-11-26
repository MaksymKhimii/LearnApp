package com.epam.gymApp.exception;

public class TrainerNotFoundException extends RuntimeException {

  public TrainerNotFoundException(String message) {
    super(message);
  }
}
