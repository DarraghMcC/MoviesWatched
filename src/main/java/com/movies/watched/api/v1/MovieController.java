package com.movies.watched.api.v1;

import com.movies.watched.api.v1.model.MovieDTO;
import com.movies.watched.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.movies.watched.api.v1.MovieController.MOVIE_API;

@RestController
@RequestMapping(value = MOVIE_API)
@Api(value="movie", description="Operations pertaining to the details of watched movies")
public class MovieController {

  public final static String MOVIE_API = "movie";
  public final static String MOVIE_DELETE_API = "delete/{movieTitle}";
  public final static String MOVIE_TITLE_API = "/{movieTitle}";

  private final MovieService movieService;

  public MovieController(final MovieService movieService) {
    this.movieService = movieService;
  }

  @ApiOperation(value = "Get the details for one movie based on title", response = MovieDTO.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully retrieved list"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @RequestMapping(value = MOVIE_TITLE_API, method = RequestMethod.GET)
  public MovieDTO getMovie(@PathVariable final String movieTitle) {
    return movieService.getMovie(movieTitle);
  }

  @ApiOperation(value = "Get all movies currently stored", response = List.class)
  @GetMapping
  public List<MovieDTO> getMovies() {
    return movieService.getAllMovies();
  }


  @ApiOperation(value = "Add a movie's details", response = MovieDTO.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully added movie"),
    @ApiResponse(code = 400, message = "Validation issue occurred")
  })
  @PostMapping
  public MovieDTO addMovie(final MovieDTO movieDTO) {
    return movieService.addMovieSeen(movieDTO);
  }

  @ApiOperation(value = "Update a movie's details", response = MovieDTO.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully updated movie"),
    @ApiResponse(code = 400, message = "Validation issue occurred")
  })
  @PutMapping
  public MovieDTO updateMovie(final MovieDTO movieDTO) {
    return movieService.updateMovieSeen(movieDTO);
  }

  @ApiOperation(value = "Remove movie's details", response = MovieDTO.class)
  @RequestMapping(value = MOVIE_DELETE_API, method = RequestMethod.DELETE)
  public void deleteMovie(@PathVariable final String movieTitle) {
    movieService.deleteMovie(movieTitle);
  }

}
