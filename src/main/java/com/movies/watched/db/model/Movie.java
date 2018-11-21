package com.movies.watched.db.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull
  private String title;

  @NotNull
  @Column(name="number_of_times_seen")
  private Integer numberOfTimesSeen;
}
