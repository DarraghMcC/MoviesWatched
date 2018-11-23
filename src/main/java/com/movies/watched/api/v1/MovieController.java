package com.movies.watched.api.v1;

import com.movies.watched.api.v1.model.MovieDTO;
import com.movies.watched.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.movies.watched.api.v1.MovieController.MOVIE_API;

@RestController
@RequestMapping(value = MOVIE_API)
public class MovieController {

  public final static String MOVIE_API = "movie";
  public final static String MOVIE_DELETE_API = "delete/{movieTitle}";
  public final static String MOVIE_TITLE_API = "/{movieTitle}";

  private final MovieService movieService;

  public MovieController(final MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping
  @RequestMapping(MOVIE_TITLE_API)
  public MovieDTO getMovie(@PathVariable final String movieTitle) {
    return movieService.getMovie(movieTitle);
  }

  @GetMapping
  public List<MovieDTO> getMovies() {
    return movieService.getAllMovies();
  }

  @PostMapping
  public MovieDTO addMovie(final MovieDTO movieDTO) {
    return movieService.addMovieSeen(movieDTO);
  }

  @PutMapping
  public MovieDTO updateMovie(final MovieDTO movieDTO) {
    return movieService.updateMovieSeen(movieDTO);
  }

  @DeleteMapping
  @RequestMapping(MOVIE_DELETE_API)
  public void deleteMovie(@PathVariable final String movieTitle) {
    movieService.deleteMovie(movieTitle);
  }

}
