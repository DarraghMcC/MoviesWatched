package com.movies.watched.db.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull
  private String title;

  @NotNull
  private Integer numberOfTimesSeen;

  @NotNull
  private LocalDateTime createdDateTime;

  @NotNull
  private LocalDateTime updatedDateTime;

  private String review;

}
