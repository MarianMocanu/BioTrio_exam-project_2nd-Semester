package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
  public String screeningsUsers(Model model) {
    model.addAttribute("upcomingScreenings", screeningRepo.findUpcomingScreenings());
    return "screenings/user/screenings-view";
  }

  // For the employees
  @GetMapping("/manage/screenings")
  public String screenings(Model model) {
    model.addAttribute("screenings", screeningRepo.findAllScreenings());
    return "screenings/screenings-view";
  }

  @GetMapping("/manage/screenings/add")
  public String manageScreenings(Model model) {
    model.addAttribute("movies", movieRepo.findAllMovies());
    model.addAttribute("theaters", theaterRepo.findAllTheaters());
    model.addAttribute("screening", new Screening());
    return "screenings/screenings-add";
  }

  @PostMapping("/manage/screenings/save")
  public String addScreening(@ModelAttribute Screening screening) {
//    Screening screening = new Screening();
//    screening.setMovie(movieRepo.findMovieById(screeningForm.getMovieId()));
//    screening.setTheater(theaterRepo.findTheater(screeningForm.getTheaterId()));
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//    LocalDateTime startTime;
//    try {
//       startTime = LocalDateTime.parse(screeningForm.getStartTime(), formatter);
//    } catch (Exception e) {
//      return "redirect:/manage/screenings/add?start_time_error";
//    }
//    screening.setStartTime(startTime);
    System.out.println(screening);
    screeningRepo.addScreening(screening);
    return "redirect:/manage/screenings";
  }

  @GetMapping("/manage/screenings/edit/{id}")
  public String editScreening(@PathVariable("id") int id, Model model) {
    model.addAttribute("selectedScreening", screeningRepo.findById(id));
    model.addAttribute("movies", movieRepo.findAllMovies());
    model.addAttribute("theaters", theaterRepo.findAllTheaters());
    return "screenings/screenings-edit";
  }

  @PostMapping("/manage/screenings/edit")
  public String commitEdit(@ModelAttribute Screening screening) {
    screeningRepo.updateScreening(screening);
    return "redirect:/manage/screenings";
  }

  @GetMapping("/manage/screenings/delete/{id}")
  public String deleteScreening(@PathVariable("id") int id) {
    screeningRepo.deleteScreening(id);
    return "redirect:/manage/screenings";
  }
}
