package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.cinema.Screening;
import dk.kea.stud.biotrio.cinema.ScreeningRepository;
import dk.kea.stud.biotrio.cinema.Theater;
import dk.kea.stud.biotrio.cinema.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TicketController {
  @Autowired
  private TicketRepository ticketRepo;
  @Autowired
  private ScreeningRepository screeningRepo;
  @Autowired
  private TheaterRepository theaterRepo;


  @GetMapping("manage/screenings/ticketing")
  public String showScreenings(Model model) {
    model.addAttribute("screeningsList", screeningRepo.findUpcomingScreenings());
    return "ticketing/screenings-ticketing";
  }

  @GetMapping("manage/screening/{screening_id}/ticketing")
  public String screeningTicketing(@PathVariable(name = "screening_id") int id, Model model) {
    Screening screening = screeningRepo.findById(id);
    Theater theater = screening.getTheater();
    Seat[][] theaterSeats = new Seat[theater.getNoOfRows()][theater.getSeatsPerRow()];
    model.addAttribute("theaterSeats", theaterSeats);

    return "ticketing/screeningID-ticketing";
  }

  @PostMapping("manage/screening/{screening_id}/ticketing")
  public String screeningTicketing(@PathVariable(name = "screening_id") int id, @ModelAttribute boolean[][] theaterSeats) {


    return "redirect:/manage/screenings/ticketing";
  }

}
