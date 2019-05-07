package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.cinema.Screening;
import dk.kea.stud.biotrio.cinema.ScreeningRepository;
import dk.kea.stud.biotrio.cinema.Theater;
import dk.kea.stud.biotrio.cinema.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TicketController {
  @Autowired
  private TicketRepository ticketRepo;
  @Autowired
  private ScreeningRepository screeningRepo;
  @Autowired
  private TheaterRepository theaterRepo;
  @Autowired
  private BookingRepository bookingRepo;
  @Autowired
  private SeatRepository seatRepo;

  @GetMapping("manage/screening/{screening_id}/ticketing")
  public String screeningTicketing(@PathVariable(name = "screening_id") int id, Model model) {
    SeatData data = new SeatData();
    data.setSeats(seatRepo.getSeatStatusForScreening(id));
    data.setSubmittedData(new ArrayList<>());
    for (Seat seat: data.getSeats()) {
      if (seat.isSold()) {
        data.getSubmittedData().add("" + seat.getRowNo() + "_" + seat.getSeatNo());
      } else {
        data.getSubmittedData().add("");
      }
    }

    model.addAttribute("data", data);
    model.addAttribute("screeningId", id); // ii nevoie?

    return "ticketing/screeningID-ticketing";
  }

  @PostMapping("manage/screening/{screening_id}/ticketing")
  public String screeningTicketing(@PathVariable(name = "screening_id") int id,
                                   @ModelAttribute boolean[][] theaterSeats) {

    return "redirect:/manage/screenings/ticketing";
  }

}
