package com.movies.watched.service;

import com.movies.watched.api.v1.model.MovieDTO;

import java.util.List;

public interface MovieService {
  MovieDTO addMovieSeen(MovieDTO movieTitle);

  MovieDTO getMovie(String movieTitle);

  MovieDTO updateMovieSeen(MovieDTO movieDTO);

  List<MovieDTO> getAllMovies();

  void deleteMovie(String movieTitle);
}
