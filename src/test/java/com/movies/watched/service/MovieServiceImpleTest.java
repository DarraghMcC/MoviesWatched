package com.movies.watched.service;

import com.movies.watched.api.v1.model.MovieDTO;
import com.movies.watched.constant.ErrorMessage;
import com.movies.watched.db.model.Movie;
import com.movies.watched.exception.MovieValidationException;
import com.movies.watched.exception.UnknownMovieException;
import com.movies.watched.persistence.MovieRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MovieServiceImpleTest {

  @Mock
  private MovieRepository movieRepository;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  private MovieService sut;

  @Before
  public void setUp() {
    sut = new MovieServiceImpl(movieRepository);
  }

  @Test
  public void testAddSeenMovie() {
    final MovieDTO testMovie = createTestMovie();
    when(movieRepository.findByTitle(anyString())).thenReturn(null);
    when(movieRepository.save(any())).thenReturn(new Movie());

    ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
    sut.addMovieSeen(testMovie);

    verify(movieRepository).save(movieCaptor.capture());
    verify(movieRepository).findByTitle(testMovie.getTitle().toUpperCase());
    assertThat(movieCaptor.getValue().getTitle()).isEqualTo(testMovie.getTitle().toUpperCase());
    assertThat(movieCaptor.getValue().getNumberOfTimesSeen()).isEqualTo(1);
    assertThat(movieCaptor.getValue().getReview()).isEqualTo(testMovie.getReview());
    assertFalse("Date should be before now",
      movieCaptor.getValue().getCreatedDateTime().isAfter(LocalDateTime.now()));
    assertFalse("Date should be before now",
      movieCaptor.getValue().getUpdatedDateTime().isAfter(LocalDateTime.now()));
  }

  @Test
  public void testAddSeenMovie_noReview() {
    final MovieDTO testMovie = createTestMovie();
    testMovie.setReview(null);
    when(movieRepository.findByTitle(anyString())).thenReturn(null);
    when(movieRepository.save(any())).thenReturn(new Movie());

    ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
    sut.addMovieSeen(testMovie);

    verify(movieRepository).save(movieCaptor.capture());
    verify(movieRepository).findByTitle(testMovie.getTitle().toUpperCase());
    assertThat(movieCaptor.getValue().getReview()).isNull();
  }

  @Test
  public void testAddSeenMovie_nullCount() {
    final MovieDTO testMovie = createTestMovie();
    testMovie.setNumberOfTimesSeen(null);
    when(movieRepository.findByTitle(anyString())).thenReturn(null);
    when(movieRepository.save(any())).thenReturn(new Movie());

    ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
    sut.addMovieSeen(testMovie);

    verify(movieRepository).save(movieCaptor.capture());
    assertThat(movieCaptor.getValue().getNumberOfTimesSeen()).isEqualTo(1);
  }

  @Test
  public void testAddSeenMovie_prePopulatedCount() {
    final MovieDTO testMovie = createTestMovie();
    testMovie.setNumberOfTimesSeen(10);
    when(movieRepository.findByTitle(anyString())).thenReturn(null);
    when(movieRepository.save(any())).thenReturn(new Movie());

    ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
    sut.addMovieSeen(testMovie);

    verify(movieRepository).save(movieCaptor.capture());
    assertThat(movieCaptor.getValue().getNumberOfTimesSeen()).isEqualTo(10);
  }

  @Test
  public void testAddSeenMovie_movieAlreadyExists() {
    final MovieDTO testMovie = createTestMovie();
    testMovie.setNumberOfTimesSeen(10);
    when(movieRepository.findByTitle(anyString())).thenReturn(new Movie());

    expectedException.expect(MovieValidationException.class);
    expectedException.expectMessage(ErrorMessage.ALREADY_EXISTS.getMessage());
    sut.addMovieSeen(testMovie);
  }

  @Test
  public void testGetMovie_unknownMovie() {
    final Movie movie = new Movie();
    movie.setNumberOfTimesSeen(5);

    when(movieRepository.findByTitle(anyString())).thenReturn(null);
    expectedException.expect(UnknownMovieException.class);
    sut.getMovie("tesMovie");
  }

  @Test
  public void testGetMovie() {
    final Movie movie = new Movie();
    movie.setNumberOfTimesSeen(5);

    when(movieRepository.findByTitle(anyString())).thenReturn(movie);
    MovieDTO movieDTO = sut.getMovie("testMovie");

    verify(movieRepository).findByTitle("TESTMOVIE");
    assertThat(movieDTO.getNumberOfTimesSeen()).isEqualTo(movie.getNumberOfTimesSeen());
  }

  @Test
  public void testUpdateMovie_unknownMovie() {
    final MovieDTO testMovie = createTestMovie();
    when(movieRepository.findByTitle(anyString())).thenReturn(null);
    expectedException.expect(UnknownMovieException.class);
    sut.updateMovieSeen(testMovie);
  }

  @Test
  public void testUpdateMovie() {
    final Movie movie = new Movie();
    final LocalDateTime createdDate = LocalDateTime.now().minusDays(2);

    movie.setNumberOfTimesSeen(5);
    movie.setCreatedDateTime(createdDate);
    movie.setUpdatedDateTime(createdDate);
    movie.setTitle("testTitle");

    final MovieDTO movieDTO = createTestMovie();
    movieDTO.setNumberOfTimesSeen(movie.getNumberOfTimesSeen() + 1);

    when(movieRepository.findByTitle(anyString())).thenReturn(movie);
    when(movieRepository.save(any())).thenReturn(new Movie());
    sut.updateMovieSeen(movieDTO);

    ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
    verify(movieRepository).save(movieCaptor.capture());
    verify(movieRepository).findByTitle(movie.getTitle());

    assertThat(movieCaptor.getValue().getNumberOfTimesSeen()).isEqualTo(movieDTO.getNumberOfTimesSeen());
    assertThat(movieCaptor.getValue().getCreatedDateTime()).isEqualTo(createdDate);
    assertThat(movieCaptor.getValue().getUpdatedDateTime()).isNotEqualTo(createdDate);
    assertThat(movieCaptor.getValue().getReview()).isEqualTo(movieDTO.getReview());
  }

  @Test
  public void testUpdateMovie_updateReview() {
    final Movie movie = new Movie();
    final LocalDateTime createdDate = LocalDateTime.now().minusDays(2);

    movie.setNumberOfTimesSeen(5);
    movie.setCreatedDateTime(createdDate);
    movie.setUpdatedDateTime(createdDate);
    movie.setTitle("testTitle");
    movie.setReview("original review");

    final MovieDTO movieDTO = createTestMovie();
    movieDTO.setNumberOfTimesSeen(movie.getNumberOfTimesSeen() + 1);

    when(movieRepository.findByTitle(anyString())).thenReturn(movie);
    when(movieRepository.save(any())).thenReturn(new Movie());
    sut.updateMovieSeen(movieDTO);

    ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
    verify(movieRepository).save(movieCaptor.capture());
    verify(movieRepository).findByTitle(movie.getTitle());

    assertThat(movieCaptor.getValue().getReview()).isEqualTo(movieDTO.getReview());
  }

  @Test
  public void testUpdateMovie_noUpdateReview() {
    final Movie movie = new Movie();
    final LocalDateTime createdDate = LocalDateTime.now().minusDays(2);

    movie.setNumberOfTimesSeen(5);
    movie.setCreatedDateTime(createdDate);
    movie.setUpdatedDateTime(createdDate);
    movie.setTitle("testTitle");
    movie.setReview("original review");

    final MovieDTO movieDTO = createTestMovie();
    movieDTO.setReview(null);
    movieDTO.setNumberOfTimesSeen(movie.getNumberOfTimesSeen() + 1);

    when(movieRepository.findByTitle(anyString())).thenReturn(movie);
    when(movieRepository.save(any())).thenReturn(new Movie());
    sut.updateMovieSeen(movieDTO);

    ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
    verify(movieRepository).save(movieCaptor.capture());
    verify(movieRepository).findByTitle(movie.getTitle());

    assertThat(movieCaptor.getValue().getReview()).isEqualTo("original review");
  }

  @Test
  public void testUpdateMovie_noCountUpdate() {
    final Movie movie = new Movie();

    movie.setNumberOfTimesSeen(5);
    movie.setTitle("testTitle");

    final MovieDTO movieDTO = createTestMovie();
    movieDTO.setNumberOfTimesSeen(null);

    when(movieRepository.findByTitle(anyString())).thenReturn(movie);
    when(movieRepository.save(any())).thenReturn(new Movie());
    sut.updateMovieSeen(movieDTO);

    ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
    verify(movieRepository).save(movieCaptor.capture());
    verify(movieRepository).findByTitle(movie.getTitle());

    assertThat(movieCaptor.getValue().getNumberOfTimesSeen()).isEqualTo(5);
  }

  @Test
  public void testUpdateMovie_countDecrease() {
    final Movie movie = new Movie();

    movie.setNumberOfTimesSeen(5);
    movie.setTitle("testTitle");

    final MovieDTO movieDTO = createTestMovie();
    movieDTO.setNumberOfTimesSeen(1);

    when(movieRepository.findByTitle(anyString())).thenReturn(movie);
    expectedException.expect(MovieValidationException.class);
    expectedException.expectMessage(ErrorMessage.COUNT_REDUCTION.getMessage());
    sut.updateMovieSeen(movieDTO);
  }

  @Test
  public void testGetAllMovies() {
    Movie movie = new Movie();
    movie.setTitle("testTitle");

    when(movieRepository.findAll()).thenReturn(Collections.singletonList(movie));
    List<MovieDTO> movieList = sut.getAllMovies();

    verify(movieRepository).findAll();
    assertThat(movieList).hasSize(1);
    assertThat(movieList).extracting("title").contains("testTitle");
  }

  @Test
  public void testDeleteMovie_unknownMovie() {
    when(movieRepository.findByTitle("TESTMOVIE")).thenReturn(null);
    expectedException.expect(UnknownMovieException.class);
    sut.deleteMovie("testMovie");

    verify(movieRepository, never()).delete(any());
  }

  @Test
  public void testDeleteMovie() {
    Movie movie = new Movie();
    when(movieRepository.findByTitle("TESTMOVIE")).thenReturn(movie);

    sut.deleteMovie("testMovie");
    verify(movieRepository).delete(movie);
  }


  private MovieDTO createTestMovie() {
    MovieDTO movieDTO = new MovieDTO();
    movieDTO.setNumberOfTimesSeen(0);
    movieDTO.setTitle("TestMovie");
    movieDTO.setReview("This is a review.");
    return movieDTO;
  }

}
