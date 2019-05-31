package dk.kea.stud.biotrio;

import dk.kea.stud.biotrio.cinema.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller class for the routes that didn't fit in any of the other categories
 */
@Controller
public class MiscController {

  @Autowired
  private MovieRepository movieRepo;

  /**
   * Display the web application's index page
   */
  @GetMapping("/")
  public String showIndex(Model model) {
    model.addAttribute("nowPlaying", movieRepo.getMoviesCurrentlyPlaying());
    model.addAttribute("upcomingMovies", movieRepo.getUpcomingMovies());
    return "index";
  }

  /**
   * Display the management dashboard
   */
  @GetMapping("/manage")
  public String showManagementDashboard() {
    return "manage/dashboard";
  }

  /**
   * Displays the custom login form
   */
  @GetMapping(value = "/login")
  public String showLogin(@RequestParam(value = "error", required = false) String error, String logout, Model model) {

    if (logout != null) {
      model.addAttribute("logoutmsg", "You've been logged out Successfully");
    }

    if (error == null || !error.isEmpty()) {
      model.addAttribute("error", error);
    }

    return "security/login";
  }
}
