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

  @GetMapping("manage/screening/{screening_id}/ticketing")
  public String screeningTicketing(@PathVariable(name = "screening_id") int id, Model model) {
    Screening screening = screeningRepo.findById(id);
    List<Ticket> screeningTickets = ticketRepo.findTicketsForScreening(id);
    List<Booking> screeningBookings = bookingRepo.findBookingsForScreening(id);

    //constructing a bidimensional array of Seat objects based on the screening's theater
    List<Seat> theaterSeats = new ArrayList<>();

//    for (Seat[] rows : theaterSeats) {
//      for (Seat seat : rows) {
//        seat = new Seat();
//      }
//    }
    //List<List<Seat>> theaterSeats = new ArrayList<>();

    //iterating through all seats of the screening's theater
    for (int i = 0; i < screening.getTheater().getNoOfRows(); i++) {
      for (int j = 0; j < screening.getTheater().getSeatsPerRow(); j++) {
        Seat newSeat = new Seat();
        newSeat.setRowNo(i + 1);
        newSeat.setSeatNo(j + 1);
        newSeat.setAvailable(true);
        newSeat.setSold(false);

        //setting the availability of a seat based on the sold tickets
        for (Ticket ticket : screeningTickets) {
          if (newSeat.getRowNo() == ticket.getSeat().getRowNo() &&
              newSeat.getSeatNo() == ticket.getSeat().getSeatNo()) {
            newSeat.setAvailable(false);
            newSeat.setSold(true);
          }
        }
        //setting the availability of a seat based on the booked seats
        for (Booking booking : screeningBookings) {
          for (Seat bookedSeat : booking.getSeats()) {
            if (newSeat.getRowNo() == bookedSeat.getRowNo() &&
                newSeat.getSeatNo() == bookedSeat.getSeatNo()) {
              newSeat.setAvailable(false);
              newSeat.setSold(false);
            }
          }
        }
        theaterSeats.add(newSeat);
      }
    }
//    Integer screeningId = id;
    List<String> seats = new ArrayList<>();
    for (Seat seat : theaterSeats) {
      seats.add(new String());
    }

    model.addAttribute("theaterSeats", theaterSeats);
    model.addAttribute("screeningId", id);
    model.addAttribute("stringSeats", seats);
    System.out.println(theaterSeats.size());
    return "ticketing/screeningID-ticketing";
  }

  @PostMapping("manage/screening/{screening_id}/ticketing")
  public String screeningTicketing(@PathVariable(name = "screening_id") int id,
                                   @ModelAttribute boolean[][] theaterSeats) {

    return "redirect:/manage/screenings/ticketing";
  }

}
