package dk.kea.stud.biotrio;

import dk.kea.stud.biotrio.cinema.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MiscController {

  @Autowired
  private MovieRepository movieRepo;

  @GetMapping("/")
  public String showIndex(Model model) {
    model.addAttribute("nowPlaying", movieRepo.getMoviesCurrentlyPlaying());
    model.addAttribute("upcomingMovies", movieRepo.getUpcomingMovies());
    return "index";
  }

  @GetMapping("/manage")
  public String showManagementDashboard() {
    return "manage/dashboard";
  }
}
