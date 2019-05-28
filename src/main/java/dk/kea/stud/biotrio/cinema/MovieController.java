package dk.kea.stud.biotrio.cinema;

import dk.kea.stud.biotrio.AppGlobals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the routes related to {@link Movie} management
 */
@Controller
public class MovieController {

  @Autowired
  private MovieRepository movieRepo;
  @Autowired
  private ScreeningRepository screeningRepo;
  @Autowired
  private TechnologyRepository technologyRepo;

  /**
   * Displays the movie detail view. This is meant mainly for the customers
   */
  @GetMapping("/movie/{id}")
  public String findMovies(@PathVariable(name = "id") int id, Model model) {
    model.addAttribute("movie", movieRepo.findMovieById(id));
    model.addAttribute("upcomingMovieScreenings", screeningRepo.findUpcomingScreeningsForMovieAsMap(id));
    return "movies/user/movies-detail-view";
  }

  /**
   * Displays the movie list view for movie management
   */
  @GetMapping("/manage/movies")
  public String manageMovies(Model model) {
    model.addAttribute("movies", movieRepo.findAllMovies());
    return "movies/movies-view";
  }

  /**
   * Displays the add movie view
   */
  @GetMapping("/manage/movies/add")
  public String addMovie(Model model) {
    model.addAttribute("technologies", technologyRepo.getAllTechnologies());
    model.addAttribute("selectedTechnologies", new ArrayList<Integer>());
    model.addAttribute("movie", new Movie());
    return "movies/movies-add";
  }

  /**
   * Saves the data from the add movie view to the database, then redirects the
   * user to the movie list view
   */
  @PostMapping("/manage/movies/save")
  public String saveMovie(@ModelAttribute Movie movie,
                          @RequestParam String releaseDateString,
                          @RequestParam(value = "selectedTechnologies", required = false)
                              List<Integer> selectedTechnologies) {
    // If the input release date fails to parse to a LocalDate object,
    // set the movie's release date attribute to null
    LocalDate releaseDate;
    try {
      releaseDate = LocalDate.parse(releaseDateString, AppGlobals.DATE_FORMAT);
    } catch (DateTimeParseException e) {
      releaseDate = null;
    }

    movie.setReleaseDate(releaseDate);
    movie.setRequiredTechnologies(technologyRepo.convertFromIdList(selectedTechnologies));
    movieRepo.addMovie(movie);

    return "redirect:/manage/movies";
  }

  /**
   * Displays the edit movie view
   */
  @GetMapping("/manage/movies/edit/{id}")
  public String editMovie(@PathVariable("id") int id, Model model) {
    Movie selectedMovie = movieRepo.findMovieById(id);
    model.addAttribute("technologies", technologyRepo.getAllTechnologies());
    model.addAttribute("currentMovie", selectedMovie);
    model.addAttribute("selectedTechnologies", technologyRepo.
        convertToIdList(selectedMovie.getRequiredTechnologies()));
    return "movies/movies-edit";
  }

  /**
   * Saves the submitted movie data from the edit movie view to the database
   * and redirects the user to the movie list view
   */
  @PostMapping("/manage/movies/edit")
  public String updateMovie(@ModelAttribute Movie movie,
                            @RequestParam String releaseDateString,
                            @RequestParam(value = "selectedTechnologies", required = false)
                                List<Integer> selectedTechnologies) {

    // If the input release date fails to parse, set the field to null
    LocalDate releaseDate;
    try {
      releaseDate = LocalDate.parse(releaseDateString, AppGlobals.DATE_FORMAT);
    } catch (DateTimeParseException e) {
      releaseDate = null;
    }

    movie.setReleaseDate(releaseDate);
    movie.setRequiredTechnologies(technologyRepo.convertFromIdList(selectedTechnologies));
    movieRepo.updateMovie(movie);

    return "redirect:/manage/movies";
  }

  /**
   * Checks if the movie has associated screenings, and if not, deletes it from the
   * database, and redirects the user back to the movie list view. Otherwise
   * displays an error screen informing that user that the movie can't be deleted
   */
  @PostMapping("/manage/movies/delete")
  public String deleteMovie(@RequestParam(name = "movieId") int id, Model m) {
    Movie movie = movieRepo.findMovieById(id);
    if (movieRepo.canDelete(movie)) {
      movieRepo.deleteMovie(id);
      return "redirect:/manage/movies";
    } else {
      m.addAttribute("movieTitle", movieRepo.findMovieById(id).getTitle());
      return "movies/movies-delete";
    }
  }

  /**
   * Displays the upcoming movies list view
   */
  @GetMapping("/manage/upcoming")
  public String showUpcomingMovies(Model model) {
    model.addAttribute("upcomingMovies", movieRepo.getUpcomingMovies());
    return "movies/upcoming-movies-view";
  }

  /**
   * Displays the add to upcoming movies list view
   */
  @GetMapping("/manage/upcoming/add")
  public String addToUpcomingMovies(Model model,
                                    @RequestParam(value = "error", required = false) String error) {
    if (error != null) {
      model.addAttribute("error", "date");
    }

    // Populate the selection box with only the movies that aren't
    // already in the upcoming movies list
    model.addAttribute("movies", movieRepo.getMoviesThatArentUpcoming());
    return "movies/upcoming-movies-add";
  }

  /**
   * Attempt to add a movie to the upcoming movies list, then redirect the user to
   * the upcoming movies list view. In case of failing to parse the estimated
   * screening date, an error message will be displayed and the movie is
   * not added to the list
   */
  @PostMapping("/manage/upcoming/add")
  public String saveInUpcomingList(@ModelAttribute("selectedMovie") int movieId,
                                   @ModelAttribute("estDate") String estDate) {
    try {
      LocalDate estimatedDate = LocalDate.parse(estDate, AppGlobals.DATE_FORMAT);
      movieRepo.addMovieToUpcomingList(movieRepo.findMovieById(movieId), estimatedDate);
      return "redirect:/manage/upcoming";
    } catch (DateTimeParseException e) {
      return "redirect:/manage/upcoming/add?error=date";
    }
  }

  /**
   * Remove a movie from the upcoming movies list, and redirect the user
   * to the upcoming movies list view
   */
  @PostMapping("/manage/upcoming/delete")
  public String removeMovieFromList(@RequestParam(name = "movieId") int id) {
    movieRepo.removeMovieFromUpcomingList(id);
    return "redirect:/manage/upcoming";
  }

}
