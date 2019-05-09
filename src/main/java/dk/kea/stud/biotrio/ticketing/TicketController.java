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



  @GetMapping("/manage/screening/{screening_id}/ticketing")
  public String screeningTicketing(@PathVariable(name = "screening_id") int id, Model model) {
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

  @PostMapping("/manage/screening/{screening_id}/ticketing")
  public String screeningTicketing(@PathVariable(name = "screening_id") int id, @ModelAttribute SeatData data) {
    List<String> updateSeats = data.getSubmittedData();
    for (String seat : updateSeats) {
      Ticket soldTicket = new Ticket();
      soldTicket.setScreening(screeningRepo.findById(id));
      Seat soldSeat = new Seat();
      String[] seatPostion = seat.split("_");
      soldSeat.setRowNo(Integer.valueOf(seatPostion[0]));
      soldSeat.setSeatNo(Integer.valueOf(seatPostion[1]));
      soldTicket.setSeat(soldSeat);
      ticketRepo.addTicket(soldTicket);
      //TODO SomeClass.print(ticketRepo.addTicket(soldTicket));
    }
    return "redirect:/manage/screening/" + id + "/ticketing";
  }

  @GetMapping("/manage/screening/{screening_id}/ticketing/void")
  public String deleteTicket(@PathVariable(name = "screening_id") int id, Model model) {
    SeatData data = new SeatData();
    data.setSeats(seatRepo.getSeatStatusForScreening(id));
    data.setSubmittedData(new ArrayList<>());
    model.addAttribute("data", data);
    model.addAttribute("screening", screeningRepo.findById(id));
    return "ticketing/delete-ticket";
  }

  @PostMapping("/manage/screening/{screening_id}/ticketing/void")
  public String deleteTicket(@PathVariable(name = "screening_id") int id, @ModelAttribute SeatData data) {
    for (String rowSeatNo : data.getSubmittedData()) {
      String[] seatPosition = rowSeatNo.split("_");
      ticketRepo.deleteTicket(id, Integer.valueOf(seatPosition[0]), Integer.valueOf(seatPosition[1]));
    }
    return "redirect:/manage/screening/" + id + "/ticketing";
  }

}
