package com.movies.watched.constant;

public enum ErrorMessage {
  UNKNOWN_MOVIE("Movie not found"),
  ALREADY_EXISTS("Movie already exists"),
  COUNT_REDUCTION("A movie's seen count cannot be reduced"),
  GENERIC_ISSUE("Internal issue - please contact system administrator");

  private final String message;

  private ErrorMessage(String message){
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
