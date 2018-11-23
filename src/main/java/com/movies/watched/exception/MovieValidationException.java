package com.movies.watched.exception;

import com.movies.watched.constant.ErrorMessage;

public class MovieValidationException extends MovieException {

  public MovieValidationException(ErrorMessage errorMessage){
    super(errorMessage.getMessage());
  }
}
