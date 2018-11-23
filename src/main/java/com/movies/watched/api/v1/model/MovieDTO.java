package com.movies.watched.api.v1.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDTO {

  private String title;

  private Integer numberOfTimesSeen;
}
