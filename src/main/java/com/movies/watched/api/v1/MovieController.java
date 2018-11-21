package com.movies.watched.api.v1;

import com.movies.watched.service.MovieService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {

  public final static String MOVIE_API = "movie/{movieTitle}";

  private final MovieService movieService;

  public MovieController(final MovieService movieService) {
    this.movieService = movieService;
  }

  @RequestMapping(value = MOVIE_API)
  public String addMovie(@PathVariable final String movieTitle) {

    movieService.addMovieSeen(movieTitle);
    return "Movie added";
  }

}
