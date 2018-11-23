package com.movies.watched.api;

import com.movies.watched.constant.ErrorMessage;
import com.movies.watched.exception.MovieException;
import com.movies.watched.exception.MovieValidationException;
import com.movies.watched.exception.UnknownMovieException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MovieWatchedErrorHandler {

  @ExceptionHandler(UnknownMovieException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleUnknownMovieException() {
    return ErrorMessage.UNKNOWN_MOVIE.getMessage();
  }

  @ExceptionHandler(MovieValidationException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleMovieValidationException(MovieValidationException exception) {
    return exception.getMessage();
  }

  @ExceptionHandler(MovieException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public String handleMovieException() {
    return ErrorMessage.GENERIC_ISSUE.getMessage();
  }
}
