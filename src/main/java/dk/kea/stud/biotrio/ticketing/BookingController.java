package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.cinema.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BookingController {
  @Autowired
  private BookingRepository bookingRepo;
  @Autowired
  private TicketRepository ticketRepo;
  @Autowired
  private ScreeningRepository screeningRepo;

  @GetMapping("/booking/{id}")
  public String showBookingsView(@PathVariable("id") int screeningId, Model model) {
    model.addAttribute("screening", screeningRepo.findById(screeningId));
    return "bookings/user/make-booking";
  }
}
