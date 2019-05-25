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

@Controller
public class MovieController {

  @Autowired
  private MovieRepository movieRepo;
  @Autowired
  private ScreeningRepository screeningRepo;
  @Autowired
  private TechnologyRepository technologyRepo;

  // For the end-users
  @GetMapping("/movie/{id}")
  public String findMovies(@PathVariable(name = "id") int id, Model model) {
    model.addAttribute("movie", movieRepo.findMovieById(id));
    model.addAttribute("upcomingMovieScreenings", screeningRepo.findUpcomingScreeningsForMovieAsMap(id));
    return "movies/user/movies-detail-view";
  }

  // For the staff
  @GetMapping("/manage/movies")
  public String manageMovies(Model model) {
    model.addAttribute("movies", movieRepo.findAllMovies());
    return "movies/movies-view";
  }

  @GetMapping("/manage/movies/add")
  public String addMovie(Model model) {
    model.addAttribute("technologies", technologyRepo.getAllTechnologies());
    model.addAttribute("selectedTechnologies", new ArrayList<Integer>());
    model.addAttribute("movie", new Movie());
    return "movies/movies-add";
  }

  @PostMapping("/manage/movies/save")
  public String saveMovie(@ModelAttribute Movie movie,
                          @RequestParam String releaseDateString,
                          @RequestParam(value = "selectedTechnologies", required = false)
                              List<Integer> selectedTechnologies) {
    LocalDate releaseDate;
    try {
      releaseDate = LocalDate.parse(releaseDateString, AppGlobals.DateFormat);
    } catch (DateTimeParseException e) {
      releaseDate = null;
    }
    movie.setReleaseDate(releaseDate);
    movie.setRequiredTechnologies(technologyRepo.convertFromIdList(selectedTechnologies));
    movieRepo.addMovie(movie);
    return "redirect:/manage/movies";
  }

  @GetMapping("/manage/movies/edit/{id}")
  public String editMovie(@PathVariable("id") int id, Model model) {
    Movie selectedMovie = movieRepo.findMovieById(id);
    model.addAttribute("technologies", technologyRepo.getAllTechnologies());
    model.addAttribute("currentMovie", selectedMovie);
    model.addAttribute("selectedTechnologies", technologyRepo.
        convertToIdList(selectedMovie.getRequiredTechnologies()));
    return "movies/movies-edit";
  }

  @PostMapping("/manage/movies/edit")
  public String updateMovie(@ModelAttribute Movie movie,
                            @RequestParam String releaseDateString,
                            @RequestParam(value = "selectedTechnologies", required = false)
                                List<Integer> selectedTechnologies) {
    LocalDate releaseDate;
    try {
      releaseDate = LocalDate.parse(releaseDateString, AppGlobals.DateFormat);
    } catch (DateTimeParseException e) {
      releaseDate = null;
    }
    movie.setReleaseDate(releaseDate);
    movie.setRequiredTechnologies(technologyRepo.convertFromIdList(selectedTechnologies));
    movieRepo.updateMovie(movie);
    return "redirect:/manage/movies";
  }

  //From "delete" button goes to "Are you sure to delete(...)?" or "Theater can't be deleted"
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

  @GetMapping("/manage/upcoming")
  public String showUpcomingMovies(Model model) {
    model.addAttribute("upcomingMovies", movieRepo.getUpcomingMovies());
    return "movies/upcoming-movies-view";
  }

  @GetMapping("/manage/upcoming/add")
  public String addToUpcomingMovies(Model model,
                                    @RequestParam(value = "error", required = false) String error) {
    if (error != null) {
      model.addAttribute("error", "date");
    }
    model.addAttribute("movies", movieRepo.getMoviesThatArentUpcoming());
    return "movies/upcoming-movies-add";
  }

  @PostMapping("/manage/upcoming/add")
  public String saveInUpcomingList(@ModelAttribute("selectedMovie") int movieId,
                                   @ModelAttribute("estDate") String estDate) {
    try {
      LocalDate estimatedDate = LocalDate.parse(estDate, AppGlobals.DateFormat);
      movieRepo.addMovieToUpcomingList(movieRepo.findMovieById(movieId), estimatedDate);
      return "redirect:/manage/upcoming";
    } catch (DateTimeParseException e) {
      return "redirect:/manage/upcoming/add?error=date";
    }
  }

  @PostMapping("/manage/upcoming/delete")
  public String removeMovieFromList(@RequestParam (name = "movieId") int id) {
    movieRepo.removeMovieFromUpcomingList(id);
    return "redirect:/manage/upcoming";
  }

}
