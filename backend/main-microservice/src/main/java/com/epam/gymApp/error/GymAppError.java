package com.epam.gymApp.error;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an error response in the Gym App.
 * <p>
 * This class encapsulates information about an error that occurred during an operation in the Gym
 * App. It includes a status code and a message describing the error.
 */
@Getter
@Setter
public class GymAppError {

  private int statusCode;
  private String message;

  /**
   * Constructor for GymAppError with status code and message.
   *
   * @param statusCode The HTTP status code indicating the type of error.
   * @param message    A message providing more details about the error.
   */
  public GymAppError(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }
}
