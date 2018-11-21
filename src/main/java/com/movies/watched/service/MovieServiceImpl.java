package com.movies.watched.service;

import com.movies.watched.db.model.Movie;
import com.movies.watched.persistence.MovieRepository;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {

  private final MovieRepository movieRepository;

  public MovieServiceImpl(final MovieRepository movieRepository){
    this.movieRepository = movieRepository;
  }

  @Override
  public void addMovieSeen(final String movieTitle){
      final Movie movie = new Movie();
      movie.setTitle(movieTitle);
      movie.setNumberOfTimesSeen(1);

      movieRepository.save(movie);
  }

}
