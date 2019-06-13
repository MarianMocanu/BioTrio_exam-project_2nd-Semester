package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.AppGlobals;
import dk.kea.stud.biotrio.cinema.Screening;
import dk.kea.stud.biotrio.cinema.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Defines the routes related to {@link Ticket} management
 */
@Controller
public class TicketController {
  @Autowired
  private TicketRepository ticketRepo;
  @Autowired
  private ScreeningRepository screeningRepo;
  @Autowired
  private SeatRepository seatRepo;
  @Autowired
  private BookingRepository bookingRepo;


  /**
   * Displays the manage upcoming screenings list view
   */
  @GetMapping("/manage/ticketing")
  public String screeningsForBookingOrSale(@RequestParam(value = "error", required = false) String error, Model model) {
    if(error != null) {
      model.addAttribute("error", error);
    }
    model.addAttribute("upcomingScreenings", screeningRepo.findUpcomingScreeningsAsMap());
    return "ticketing/ticketing";
  }

  @GetMapping("/manage/ticketing/{id}")
  public String screeningDetailView(@PathVariable int id,
                                    Model model) {
    model.addAttribute("screening", screeningRepo.findById(id));
    return "ticketing/screening-detail-view";
  }

  /**
   * Displays the sell ticket view for selected screening
   */
  @GetMapping("/manage/screening/{screeningId}/ticketing")
  public String screeningTicketing(@PathVariable(name = "screeningId") int id, Model model) {
    // The object that will populate the view and return the data related to which
    // seats the user has selected
    SeatData data = new SeatData();
    // If we're X or fewer minutes away from the screening's start time, we can go ahead and delete
    // all the bookings, thus opening up the previously booked seats for sale
    if (screeningRepo.findById(id).getStartTime().isBefore(LocalDateTime.now()
        .plusMinutes(AppGlobals.BOOKINGS_GO_ON_SALE_BEFORE_SCREENING_MINUTES))) {
      bookingRepo.deleteBookingsForScreening(id);
    }
    // Gets the tickets and bookings data from the database for a particular screening
    data.setSeats(seatRepo.getSeatStatusForScreening(id));
    data.setSubmittedData(new ArrayList<>());
    for (Seat seat : data.getSeats()) {
      if (seat.isSold()) {
        // If a seat is sold, we set the value of the String that will be bound to its respective
        // checkbox to the same value as that checkbox will have in its value attribute. This will
        // make Thymeleaf set that checkbox as checked when generating the view.
        data.getSubmittedData().add("" + seat.getRowNo() + "_" + seat.getSeatNo());
      } else
        // Setting the String to an empty value, or as a matter of fact any other value except
        // the same as the checkbox's value attribute, will create it in an unchecked state
        data.getSubmittedData().add("");
    }
    // Finally add the data to the model and render the template
    model.addAttribute("data", data);
    model.addAttribute("screening", screeningRepo.findById(id));
    return "ticketing/ticketing-id-add";
  }

  /**
   * Converts the data received from the add ticket view and adds the tickets data
   * to the database, then redirects the user to the add ticket view
   */
  @PostMapping("/manage/screening/{screeningId}/ticketing")
  public String screeningTicketing(@PathVariable(name = "screeningId") int id,
                                   @ModelAttribute SeatData data) {
    Screening currentScreening = screeningRepo.findById(id);
    for (Seat seat : seatRepo.convertStringSeatData(data.getSubmittedData())) {
      Ticket ticket = new Ticket();
      ticket.setScreening(currentScreening);
      ticket.setSeat(seat);
      ticketRepo.addTicket(ticket);
      AppGlobals.printTicket(ticket);
    }
    return "redirect:/manage/ticketing/" + id;
  }

  /**
   * Displays the void ticket view for selected screening
   */
  @GetMapping("/manage/screening/{screeningId}/ticketing/void")
  public String deleteTicket(@PathVariable(name = "screeningId") int id, Model model) {
    SeatData data = new SeatData();
    data.setSeats(seatRepo.getSeatStatusForScreening(id));
    data.setSubmittedData(new ArrayList<>());
    model.addAttribute("data", data);
    model.addAttribute("screening", screeningRepo.findById(id));
    return "ticketing/ticketing-void";
  }

  /**
   * Converts the data received from the void ticket view and deletes the tickets data
   * from the database, then redirects the user the add ticket view
   */
  @PostMapping("/manage/screening/{screeningId}/ticketing/void")
  public String deleteTicket(@PathVariable(name = "screeningId") int id,
                             @ModelAttribute SeatData data) {

    if (data.getSubmittedData() != null) {
      for (Seat seat : seatRepo.convertStringSeatData(data.getSubmittedData())) {
        Ticket ticket = new Ticket();
        ticket.setScreening(screeningRepo.findById(id));
        ticket.setSeat(seat);
        ticketRepo.deleteTicket(ticket);
      }
    }
    return "redirect:/manage/ticketing/" + id;
  }


}
