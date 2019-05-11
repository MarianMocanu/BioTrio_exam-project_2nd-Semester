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
  private SeatRepository seatRepo;



  @GetMapping("/manage/screening/{screeningId}/ticketing")
  public String screeningTicketing(@PathVariable(name = "screeningId") int id, Model model) {
    SeatData data = new SeatData();
    data.setSeats(seatRepo.getSeatStatusForScreening(id));
    data.setSubmittedData(new ArrayList<>());
    for (Seat seat : data.getSeats()) {
      if (seat.isSold()) {
        data.getSubmittedData().add("" + seat.getRowNo() + "_" + seat.getSeatNo());
      } else {
        data.getSubmittedData().add("");
      }
    }
    model.addAttribute("data", data);
    model.addAttribute("screening", screeningRepo.findById(id)); 
    return "ticketing/screeningID-ticketing";
  }

  @PostMapping("/manage/screening/{screeningId}/ticketing")
  public String screeningTicketing(@PathVariable(name = "screeningId") int id,
                                   @ModelAttribute SeatData data) {
    for (Seat seat:seatRepo.getSeatsInfo(data.getSubmittedData())) {
      Ticket ticket = new Ticket();
      ticket.setScreening(screeningRepo.findById(id));
      ticket.setSeat(seat);
      ticketRepo.addTicket(ticket);
      //TODO SomeClass.print(ticketRepo.addTicket(soldTicket));
    }
    return "redirect:/manage/screening/" + id + "/ticketing";
  }

  @GetMapping("/manage/screening/{screeningId}/ticketing/void")
  public String deleteTicket(@PathVariable(name = "screeningId") int id, Model model) {
    SeatData data = new SeatData();
    data.setSeats(seatRepo.getSeatStatusForScreening(id));
    data.setSubmittedData(new ArrayList<>());
    model.addAttribute("data", data);
    model.addAttribute("screening", screeningRepo.findById(id));
    return "ticketing/delete-ticket";
  }

  @PostMapping("/manage/screening/{screeningId}/ticketing/void")
  public String deleteTicket(@PathVariable(name = "screeningId") int id,
                             @ModelAttribute SeatData data) {
    for (Seat seat : seatRepo.getSeatsInfo(data.getSubmittedData())) {
      Ticket ticket = new Ticket();
      ticket.setScreening(screeningRepo.findById(id));
      ticket.setSeat(seat);
      ticketRepo.deleteTicket(ticket);
    }
    return "redirect:/manage/screening/" + id + "/ticketing";
  }



}
