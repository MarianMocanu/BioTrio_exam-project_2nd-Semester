package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MovieController {

  @Autowired
  private MovieRepository movieRepo;

  // For the end-users
  @GetMapping("/movie/{id}")
  public String findMovies(@PathVariable(name = "id") int id, Model model) {
    model.addAttribute("movies", movieRepo.findMovieById(id));
    return "movies/movies-view";
  }

  //TODO @GetMapping("/manage")

  // For the staff
  @GetMapping("/manage/movies")
  public String manageMovies(Model model) {
    model.addAttribute("movies", movieRepo.findAllMovies());
    return "movies/manage-movies";
  }

  @GetMapping("/manage/movies/add")
  public String addMovie(Model model) {
    model.addAttribute("movie", new Movie());
    return "movies/add-movie";
  }

  @PostMapping("/manage/movies/save")
  public String saveMovie(@ModelAttribute Movie movie) {
    movieRepo.addMovie(movie);
    return "redirect:/manage/movies";
  }

  @GetMapping("/manage/movies/edit/{id}")
  public String editMovie(@PathVariable("id") int id, Model model) {
    model.addAttribute("currentMovie", movieRepo.findMovieById(id));
    return "movies/edit-movie";
  }

  @PostMapping("/manage/movies/edit")
  public String updateMovie(@ModelAttribute Movie movie) {
    movieRepo.updateMovie(movie);
    return "redirect:/manage/movies";
  }

  @GetMapping("/manage/movies/delete/{id}")
  public String deleteMovie(@PathVariable("id") int id) {
    movieRepo.deleteMovie(id);
    return "redirect:/manage/movies";
  }

}
