package dk.kea.stud.biotrio.cinema;

import dk.kea.stud.biotrio.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

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
    model.addAttribute("upcomingScreenings", screeningRepo.findUpcomingScreeningsAsMap());
    return "screenings/user/screenings-view";
  }

  // For the employees
  @GetMapping("/manage/screenings")
  public String screenings(Model model) {
    model.addAttribute("screenings", screeningRepo.findAllScreenings());
    return "screenings/screenings-view";
  }

  @GetMapping("/manage/ticketing")
  public String screeningsForBookingOrSale(Model model){
    model.addAttribute("screenings", screeningRepo.findAllScreenings());
    return "ticketing/ticketing";
  }

  @GetMapping("/manage/screenings/add")
  public String manageScreenings(@RequestParam(value = "error", required = false) String error,
                                 Model model) {
    if (error != null) {
      model.addAttribute("error", error);
    }
    model.addAttribute("movies", movieRepo.findAllMovies());
    model.addAttribute("theaters", theaterRepo.findAllTheaters());
    model.addAttribute("screeningForm", new ScreeningForm());
    return "screenings/screenings-add";
  }

  @PostMapping("/manage/screenings/save")
  public String addScreening(@ModelAttribute ScreeningForm screeningForm) {
    Screening screening = convertFormToScreening(screeningForm);
    if (screening == null) {
      return "redirect:/manage/screenings/add?error=start_time";
    }

    if (areTechnologicallyCompatible(screening.getMovie(), screening.getTheater())) {
      Screening conflict = checkForSchedulingConflicts(screening);
      if (conflict == null) {
        screeningRepo.addScreening(screening);
        return "redirect:/manage/screenings";
      } else {
        return "redirect:/manage/screenings/add?error=conflict";
      }
    } else {
      return "redirect:/manage/screenings/add?error=tech";
    }
  }

  @GetMapping("/manage/screenings/edit/{id}")
  public String editScreening(@RequestParam(value = "error", required = false) String error,
      @PathVariable("id") int id, Model model) {
    if (error != null) {
      model.addAttribute("error", error);
    }
    Screening screening = screeningRepo.findById(id);
    ScreeningForm formData = new ScreeningForm();
    formData.setId(id);
    formData.setMovieId(screening.getMovie().getId());
    formData.setTheaterId(screening.getTheater().getId());
    formData.setStartTime(screening.getStartTime().format(AppSettings.DTFormat));
    model.addAttribute("selectedScreening", formData);
    model.addAttribute("movies", movieRepo.findAllMovies());
    model.addAttribute("theaters", theaterRepo.findAllTheaters());
    return "screenings/screenings-edit";
  }

  @PostMapping("/manage/screenings/edit")
  public String commitEdit(@ModelAttribute ScreeningForm screeningForm) {
    Screening screening = convertFormToScreening(screeningForm);
    if (screening == null) {
      return "redirect:/manage/screenings/edit/" + screeningForm.getId() + "?error=start_time";
    }

    if (areTechnologicallyCompatible(screening.getMovie(), screening.getTheater())) {
      Screening conflict = checkForSchedulingConflicts(screening);
      if (conflict == null) {
        screeningRepo.updateScreening(screening);
        return "redirect:/manage/screenings";
      } else {
        return "redirect:/manage/screenings/edit/" + screeningForm.getId() + "?error=conflict";
      }
    } else {
      return "redirect:/manage/screenings/edit/" + screeningForm.getId() + "?error=tech";
    }
  }

  //From "delete" button goes to "Are you sure to delete(...)?"
  @GetMapping("/manage/screenings/delete/{id}")
  public String deleteScreening(@PathVariable(name = "id") int id, Model m) {
    Screening s = screeningRepo.findById(id);
    boolean canDelete = screeningRepo.canDelete(s);
    m.addAttribute("canDelete", canDelete);
    m.addAttribute("screening", s);

    return "screenings/screenings-delete";
  }

  //Deletes Movie and lists all the Movies
  @PostMapping("/manage/screenings/delete")
  public String deleteScreening(int id) {
    screeningRepo.deleteScreening(id);

    return "redirect:/manage/screenings";
  }

  private Screening convertFormToScreening(ScreeningForm formData) {
    Screening screening = new Screening();
    screening.setId(formData.getId());
    screening.setMovie(movieRepo.findMovieById(formData.getMovieId()));
    screening.setTheater(theaterRepo.findTheater(formData.getTheaterId()));
    LocalDateTime startTime;
    try {
      startTime = LocalDateTime.parse(formData.getStartTime(), AppSettings.DTFormat);
    } catch (DateTimeParseException e) {
      return null;
    }
    screening.setStartTime(startTime);

    return screening;
  }

  // returns null if there are no conflicts, otherwise will return the first conflicting screening found;
  private Screening checkForSchedulingConflicts(Screening screening) {
    List<Screening> potentialConflictingScreenings = screeningRepo
        .findScreeningsThatMightConflict(screening);

    if (potentialConflictingScreenings != null) {
      int screeningLength = screening.getMovie().getRuntime();

      for (Screening otherScreening: potentialConflictingScreenings) {
        int otherScreeningLength = otherScreening.getMovie().getRuntime();

        if (screening.getId() != otherScreening.getId() &&
            screening.getStartTime().isAfter(otherScreening.getStartTime()
            .minusMinutes(screeningLength + AppSettings.TIME_BUFFER_MINUTES_BETWEEN_SCREENINGS))
            && screening.getStartTime().isBefore(otherScreening.getStartTime()
            .plusMinutes(otherScreeningLength
                + AppSettings.TIME_BUFFER_MINUTES_BETWEEN_SCREENINGS))) {

          return otherScreening;
        }
      }
    }

    return null;
  }

  // Determine if a movie's technological requirements are compatible with
  // a theater's supported technologies
  private boolean areTechnologicallyCompatible(Movie movie, Theater theater) {

    List<Technology> requiredTechnologies = movie.getRequiredTechnologies();
    if (requiredTechnologies != null) {
      // Iterate over the movie's required technologies
      for (Technology requiredTechnology : requiredTechnologies) {
        boolean found = false;

        // For every required technology, iterate over the theater's supported technologies
        List<Technology> supportedTechnologies = theater.getSupportedTechnologies();
        if (supportedTechnologies != null) {
          for (Technology supportedTechnology : theater.getSupportedTechnologies()) {

            // Check if the required technology can be found among the supported ones
            if (requiredTechnology.equals(supportedTechnology)) {

              // If found, set the flag and break out of the loop
              found = true;
              break;
            }
          }
        }

        // If even a single required technology has not been found, it means
        // that the movie and theater are incompatible
        if (!found) {
          return false;
        }
      }
    }

    // If execution reaches this point, it means that either there are none, or all
    // the required technologies have been found among the supported technologies,
    // thus the movie and the theater are compatible
    return true;
  }
}