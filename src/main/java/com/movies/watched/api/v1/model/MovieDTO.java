package com.movies.watched.api.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieDTO {

  private String title;

  private Integer numberOfTimesSeen;

  private String review;
}
