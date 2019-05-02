package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScreeningController {
  @Autowired
  private ScreeningRepository screeningRepo;
  @Autowired
  private MovieRepository movieRepo;
  @Autowired
  private TheaterRepository theaterRepo;

  // For the end-users
  @GetMapping("/screenings")
  public String screenings(Model model) {
    model.addAttribute("upcomingScreenings", screeningRepo.findUpcomingScreenings());
    return "screenings/screenings-view";
  }

  // For the employees
  @GetMapping("/manage/screenings/add")
  public String manageScreenings(Model model) {
    model.addAttribute("movies", movieRepo.findAllMovies());
    model.addAttribute("theaters", theaterRepo.findAllTheaters());
    model.addAttribute("screening", new Screening());
    return "screenings/screenings-add";
  }


}
