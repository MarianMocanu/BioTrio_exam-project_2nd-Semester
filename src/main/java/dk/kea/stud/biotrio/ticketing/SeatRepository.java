package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.cinema.Screening;
import dk.kea.stud.biotrio.cinema.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class that is responsible with managing {@link Seat} data within the database
 */
@Repository
public class SeatRepository {
  @Autowired
  private ScreeningRepository screeningRepo;
  @Autowired
  private TicketRepository ticketRepo;
  @Autowired
  private BookingRepository bookingRepo;

  /**
   * Gets all the seat information for a particular screening
   *
   * @param screeningId The integer id of the screening for which to get seat data
   * @return A list of {@link Seat} objects describing the seat information for the screening
   */
  public List<Seat> getSeatStatusForScreening(int screeningId) {
    Screening screening = screeningRepo.findById(screeningId);
    List<Ticket> screeningTickets = ticketRepo.findTicketsForScreening(screeningId);
    List<Booking> screeningBookings = bookingRepo.findBookingsForScreening(screeningId);

    List<Seat> theaterSeats = new ArrayList<>();

    // Iterate through all seats of the theater in which the screening is taking place
    for (int i = 0; i < screening.getTheater().getNoOfRows(); i++) {
      for (int j = 0; j < screening.getTheater().getSeatsPerRow(); j++) {
        // Generate the seat objects' row and seat number based on theater size
        Seat newSeat = new Seat();
        newSeat.setRowNo(i + 1);
        newSeat.setSeatNo(j + 1);
        // Initially assume that each seat is available
        newSeat.setAvailable(true);
        newSeat.setSold(false);

        for (Ticket ticket : screeningTickets) {
          if (newSeat.getRowNo() == ticket.getSeat().getRowNo() &&
              newSeat.getSeatNo() == ticket.getSeat().getSeatNo()) {
            // If a ticket is found for the seat, set is as not
            // available and as sold, and break out of the loop because
            // if it has been found once, it won't be found again
            newSeat.setAvailable(false);
            newSeat.setSold(true);
            break;
          }
        }

        // If the seat has already been marked as sold, no need to
        // check among the bookings too
        if (!newSeat.isSold()) {
          for (Booking booking : screeningBookings) {
            for (Seat bookedSeat : booking.getSeats()) {
              if (newSeat.getRowNo() == bookedSeat.getRowNo() &&
                  newSeat.getSeatNo() == bookedSeat.getSeatNo()) {
                // If the seat is found within the booked seats, set it as
                // unavailable, but not as sold. This state represents a
                // booked seat. Also, break out of the loop
                newSeat.setAvailable(false);
                newSeat.setSold(false);
                break;
              }
            }
            if (!newSeat.isAvailable()) {
              // This breaks execution out of the outer for loop, in case
              // a booking has already been found for the seat
              break;
            }
          }
        }
        theaterSeats.add(newSeat);
      }
    }

    return theaterSeats;
  }

  /**
   * Convert a list of {@link String} objects representing selected
   * seats return from a form, to a list of {@link Seat} objects
   * with their respective row number and seat number correctly set
   *
   * @param seatsInfo A list of {@link String} objects returned from a form
   * @return A list of {@link Seat} objects representing the seats that
   * have been selected in the form
   */
  public List<Seat> convertStringSeatData(List<String> seatsInfo) {
    List<Seat> seatsPositions = new ArrayList<>();
    if (seatsInfo != null) {
      for (String data : seatsInfo) {
        Seat seat = new Seat();
        String[] positions = data.split("_");
        seat.setRowNo(Integer.valueOf(positions[0]));
        seat.setSeatNo(Integer.valueOf(positions[1]));
        seatsPositions.add(seat);
      }
      return seatsPositions;
    }
    return null;
  }
}
