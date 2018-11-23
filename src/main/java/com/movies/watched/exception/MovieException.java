package com.movies.watched.exception;

public abstract class MovieException extends RuntimeException {

  MovieException(String message){
    super(message);
  }

  MovieException(){
  }
}
