package com.epam.gymApp.exception;

public class TraineeNotFoundException extends RuntimeException {

  private static final String FORMAT_MESSAGE = "Trainee with ID %d is not found!";

  public TraineeNotFoundException(Number id) {
    super(String.format(FORMAT_MESSAGE, id.intValue()));
  }

  public TraineeNotFoundException(String message) {
    super(message);
  }
}
