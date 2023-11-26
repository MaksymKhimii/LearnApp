package com.epam.gymApp.exception;

public class UserNotFoundException extends RuntimeException {

  private static final String FORMAT_MESSAGE_USERNAME = "User Trainer with username %s is not found!";

  public UserNotFoundException(String username) {
    super(String.format(FORMAT_MESSAGE_USERNAME, username));
  }
}

