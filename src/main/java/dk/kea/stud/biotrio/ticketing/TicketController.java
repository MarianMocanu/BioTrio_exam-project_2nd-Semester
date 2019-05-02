package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.cinema.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TicketController {
  @Autowired
  private TicketRepository ticketRepo;
  @Autowired
  private ScreeningRepository screeningRepo;


  @GetMapping("manage/screenings/ticketing")
  public String showScreenings(Model model){
    model.addAttribute("screeningsList", screeningRepo.findUpcomingScreenings());
    return "ticketing/screenings-ticketing";
  }

  @GetMapping("manage/screening/{screening_id}/ticketing")
  public String screeningTicketing(){
    return ""
  }

}
