package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.cinema.Screening;
import dk.kea.stud.biotrio.cinema.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SeatRepository {
  @Autowired
  private ScreeningRepository screeningRepo;
  @Autowired
  private TicketRepository ticketRepo;
  @Autowired
  private BookingRepository bookingRepo;

  public List<Seat> getSeatStatusForScreening(int screeningId) {
    Screening screening = screeningRepo.findById(screeningId);
    List<Ticket> screeningTickets = ticketRepo.findTicketsForScreening(screeningId);
    List<Booking> screeningBookings = bookingRepo.findBookingsForScreening(screeningId);

    List<Seat> theaterSeats = new ArrayList<>();

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

    return theaterSeats;
  }
}
