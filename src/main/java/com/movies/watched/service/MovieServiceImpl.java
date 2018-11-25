package com.movies.watched.service;

import com.movies.watched.api.v1.model.MovieDTO;
import com.movies.watched.constant.ErrorMessage;
import com.movies.watched.db.model.Movie;
import com.movies.watched.exception.MovieValidationException;
import com.movies.watched.exception.UnknownMovieException;
import com.movies.watched.persistence.MovieRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class MovieServiceImpl implements MovieService {

  private static final ModelMapper modelMapper = new ModelMapper();
  private final MovieRepository movieRepository;

  public MovieServiceImpl(final MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  @Override
  public MovieDTO addMovieSeen(final MovieDTO movieDTO) {
    //we store movie titles capitalised
    final String capitalisedTitle = movieDTO.getTitle().toUpperCase();
    if (Objects.nonNull(movieRepository.findByTitle(capitalisedTitle))) {
      throw new MovieValidationException(ErrorMessage.ALREADY_EXISTS);
    }

    final Movie movie = new Movie();
    movie.setTitle(capitalisedTitle);
    movie.setCreatedDateTime(LocalDateTime.now());
    movie.setUpdatedDateTime(LocalDateTime.now());
    movie.setReview(movieDTO.getReview());

    Integer numberOfTimesSeen = movieDTO.getNumberOfTimesSeen();
    if (Objects.isNull(numberOfTimesSeen) || numberOfTimesSeen == 0) {
      numberOfTimesSeen = 1;
    }

    movie.setNumberOfTimesSeen(numberOfTimesSeen);
    return modelMapper.map(movieRepository.save(movie), MovieDTO.class);
  }

  @Override
  public MovieDTO getMovie(final String movieTitle) {
    final String capitalisedTitle = movieTitle.toUpperCase();

    Movie movie = movieRepository.findByTitle(capitalisedTitle);

    if (Objects.isNull(movie)) {
      throw new UnknownMovieException();
    }

    return modelMapper.map(movie, MovieDTO.class);
  }

  @Override
  public MovieDTO updateMovieSeen(final MovieDTO movieDTO) {
    //we store movie titles capitalised
    final Movie movie = movieRepository.findByTitle(movieDTO.getTitle().toUpperCase());
    if (Objects.isNull(movie)) {
      throw new UnknownMovieException();
    }

    movie.setTitle(movieDTO.getTitle().toUpperCase());
    movie.setUpdatedDateTime(LocalDateTime.now());

    Integer numberOfTimesSeen = movieDTO.getNumberOfTimesSeen();
    if (Objects.nonNull(numberOfTimesSeen) && numberOfTimesSeen < movie.getNumberOfTimesSeen()) {
      throw new MovieValidationException(ErrorMessage.COUNT_REDUCTION);
    }
    //user is not required to pass these values
    movie.setNumberOfTimesSeen(Objects.isNull(numberOfTimesSeen) ? movie.getNumberOfTimesSeen() : numberOfTimesSeen);
    movie.setReview(Objects.isNull(movieDTO.getReview()) ? movie.getReview() : movieDTO.getReview());
    return modelMapper.map(movieRepository.save(movie), MovieDTO.class);
  }

  @Override
  public List<MovieDTO> getAllMovies() {
    Type targetListType = new TypeToken<List<MovieDTO>>() {
    }.getType();
    return modelMapper.map(movieRepository.findAll(), targetListType);
  }

  @Override
  public void deleteMovie(final String movieTitle) {
    final String capitalisedTitle = movieTitle.toUpperCase();

    Movie movie = movieRepository.findByTitle(capitalisedTitle);

    if (Objects.isNull(movie)) {
      throw new UnknownMovieException();
    }
    movieRepository.delete(movie);
  }
}
