package dk.kea.stud.biotrio.cinema;

import dk.kea.stud.biotrio.AppGlobals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Defines the routes related to {@link Screening} management
 */
@Controller
public class ScreeningController {
  @Autowired
  private ScreeningRepository screeningRepo;
  @Autowired
  private MovieRepository movieRepo;
  @Autowired
  private TheaterRepository theaterRepo;


  /**
   * Displays the upcoming screenings list view, meant for the customers
   */
  @GetMapping("/screenings")
  public String screeningsUsers(Model model) {
    model.addAttribute("upcomingScreenings", screeningRepo.findUpcomingScreeningsAsMap());
    return "screenings/user/screenings-view";
  }

  /**
   * Displays the screening management list view
   */
  @GetMapping("/manage/screenings")
  public String screenings(Model model) {
    model.addAttribute("screenings", screeningRepo.findAllScreeningsAsMap());
    model.addAttribute("pastScreenings", screeningRepo.findPastScreenings());
    return "screenings/screenings-view";
  }

  /**
   * Displays the add screening view
   */
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

  /**
   * If valid, saves the data received from the add screening view to the
   * database and redirects the user back to the manage screening list view.
   * Otherwise it redirects the user to the add screening view and displays
   * an appropriate error message
   */
  @PostMapping("/manage/screenings/save")
  public String addScreening(@ModelAttribute ScreeningForm screeningForm) {
    // This method return null if the date conversion fails
    Screening screening = convertFormToScreening(screeningForm);
    if (screening == null) {
      return "redirect:/manage/screenings/add?error=start_time";
    }

    // Check if the selected theater provides all the technologies the movie requires
    if (areTechnologicallyCompatible(screening.getMovie(), screening.getTheater())) {
      // Check if the screening's starting time conflicts with other existing screenings
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

  /**
   * Display the edit screening view
   */
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
    formData.setStartTime(screening.getStartTime().format(AppGlobals.DATE_TIME_FORMAT));
    model.addAttribute("selectedScreening", formData);
    model.addAttribute("movies", movieRepo.findAllMovies());
    model.addAttribute("theaters", theaterRepo.findAllTheaters());
    return "screenings/screenings-edit";
  }

  /**
   * Saves the updated screening information from input in the edit screening
   * view if valid, and redirects the user to the manage screening list view.
   * Otherwise redirects the user to the edit screening view and displays
   * an appropriate error message
   */
  @PostMapping("/manage/screenings/edit")
  public String commitEdit(@ModelAttribute ScreeningForm screeningForm) {
    // Extracts the screening data from the ScreeningForm object.
    // The method returns null if the date conversion is invalid
    Screening screening = convertFormToScreening(screeningForm);
    if (screening == null) {
      return "redirect:/manage/screenings/edit/" + screeningForm.getId() + "?error=start_time";
    }

    // Check if the selected theater provides all the technologies the movie requires
    if (areTechnologicallyCompatible(screening.getMovie(), screening.getTheater())) {
      // Check if the screening's starting time conflicts with the other existing screenings
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

  /**
   * Deletes a screening from the database. If successful, redirects the user to
   * the manage screenings list view, or if the screening cannot be deleted
   * because of associated tickets or bookings, an error message will be displayed
   */
  @PostMapping("/manage/screenings/delete")
  public String deleteScreening(@RequestParam(name = "screeningId") int id, Model m) {
    Screening screening = screeningRepo.findById(id);
    boolean canDelete = screeningRepo.canDelete(screening);
    if (canDelete) {
      screeningRepo.deleteScreening(id);
      return "redirect:/manage/screenings";
    } else {
      m.addAttribute("movieTitle", screening.getMovie().getTitle());
      return "screenings/screenings-delete";
    }
  }

  /**
   * Deletes the past screenings and all associated bookings and tickets
   */
  @PostMapping("/manage/screenings/delete/past/screenings")
  public String deletePastScreenings() {
    screeningRepo.deletePastScreenings();
    return "redirect:/manage/screenings";
  }

  /**
   * Extracts the data from a ScreeningForm object and returns a screening
   * object. If the date conversion fails, the method returns null
   */
  private Screening convertFormToScreening(ScreeningForm formData) {
    Screening screening = new Screening();
    screening.setId(formData.getId());
    screening.setMovie(movieRepo.findMovieById(formData.getMovieId()));
    screening.setTheater(theaterRepo.findTheater(formData.getTheaterId()));
    LocalDateTime startTime;
    try {
      startTime = LocalDateTime.parse(formData.getStartTime(), AppGlobals.DATE_TIME_FORMAT);
    } catch (DateTimeParseException e) {
      return null;
    }
    screening.setStartTime(startTime);

    return screening;
  }

  /**
   * Check if there are any screenings that conflict with the schedule
   * of the one provided as a parameter
   *
   * @param screening The {@link Screening} object to check against
   * @return The first conflicting {@link Screening} found, or null if
   * no conflict is found
   */
  private Screening checkForSchedulingConflicts(Screening screening) {
    // Get a list that could potentially conflict with the one provided
    List<Screening> potentialConflictingScreenings = screeningRepo
        .findScreeningsThatMightConflict(screening);

    if (potentialConflictingScreenings != null) {
      int screeningLength = screening.getMovie().getRuntime();

      // If there are any potentially conflicting screenings, iterate over them
      for (Screening otherScreening : potentialConflictingScreenings) {
        int otherScreeningLength = otherScreening.getMovie().getRuntime();


        if (screening.getId() != otherScreening.getId() &&
            screening.getStartTime().isAfter(otherScreening.getStartTime()
                .minusMinutes(screeningLength
                    + AppGlobals.TIME_BUFFER_MINUTES_BETWEEN_SCREENINGS))
            && screening.getStartTime().isBefore(otherScreening.getStartTime()
            .plusMinutes(otherScreeningLength
                + AppGlobals.TIME_BUFFER_MINUTES_BETWEEN_SCREENINGS))) {

          // If a conflict is found, return the respective screening
          return otherScreening;
        }
      }
    }

    // If no conflict is found, just return null
    return null;
  }

  /**
   * Determine if a movie's technological requirements are compatible with
   * a theater's supported technologies
   *
   * @param movie   The {@link Movie} object to check
   * @param theater The {@link Theater} object to check
   * @return true if they are compatible, false otherwise
   */
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