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
    model.addAttribute("movie", movieRepo.findMovieById(id));
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
    model.addAttribute("movie", new Movie());
    return "movies/movies-add";
  }

  @PostMapping("/manage/movies/save")
  public String saveMovie(@ModelAttribute Movie movie) {
    movieRepo.addMovie(movie);
    return "redirect:/manage/movies";
  }

  @GetMapping("/manage/movies/edit/{id}")
  public String editMovie(@PathVariable("id") int id, Model model) {
    model.addAttribute("currentMovie", movieRepo.findMovieById(id));
    return "movies/movies-edit";
  }

  @PostMapping("/manage/movies/edit")
  public String updateMovie(@ModelAttribute Movie movie) {
    movieRepo.updateMovie(movie);
    return "redirect:/manage/movies";
  }

  //From "delete" button goes to "Are you sure to delete(...)?" or "Theater can't be deleted"
  @GetMapping("/manage/movies/delete/{id}")
  public String deleteMovie(@PathVariable(name = "id") int id, Model m) {
    Movie movie = movieRepo.findMovieById(id);
    boolean canDelete = movieRepo.canDelete(movie);
    m.addAttribute("canDelete", canDelete);
    m.addAttribute("movie", movie);
    return "movies/movies-delete";
  }

  //Deletes Movie and lists all the Movies
  @PostMapping("/manage/movies/delete")
  public String deleteMovie(int id) {
    movieRepo.deleteMovie(id);

    return "redirect:/manage/movies";
  }

}
