package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.AppGlobals;
import dk.kea.stud.biotrio.cinema.Screening;
import dk.kea.stud.biotrio.cinema.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Defines the routes related to {@link Booking} management
 */
@Controller
public class BookingController {
  @Autowired
  private BookingRepository bookingRepo;
  @Autowired
  private TicketRepository ticketRepo;
  @Autowired
  private ScreeningRepository screeningRepo;
  @Autowired
  private SeatRepository seatRepo;


  /**
   * Displays the make-booking view for selected screening
   */
  @GetMapping("/booking/{id}")
  public String showBookingsView(@PathVariable("id") int screeningId,
                                 Model model) {
    SeatData data = new SeatData();
    data.setSeats(seatRepo.getSeatStatusForScreening(screeningId));
    data.setSubmittedData(new ArrayList<>());

    model.addAttribute("maxSeatsPerBooking", AppGlobals.MAX_NUMBER_OF_SEATS_PER_BOOKING);
    model.addAttribute("screening", screeningRepo.findById(screeningId));
    model.addAttribute("seatData", data);
    return "bookings/user/make-booking";
  }

  /**
   * Converts the data received from the make-booking view and adds the booking data
   * to the database, then displays the booking-confirmed view. If there's an error
   * on input validation (e.g. no seats selected) sends the user to the booking-error view
   */
  @PostMapping("/booking/save")
  public String saveBooking(@ModelAttribute SeatData seatData,
                            @RequestParam int screeningId,
                            @RequestParam String phoneNo,
                            Model model) {
    if (seatData.getSubmittedData().size() > 0 && seatData.getSubmittedData().size()
        <= AppGlobals.MAX_NUMBER_OF_SEATS_PER_BOOKING) {
      List<Seat> seats = new ArrayList<>();
      for (String bookedSeatString : seatData.getSubmittedData()) {
        Seat currentSeat = new Seat();
        String[] seatLocation = bookedSeatString.split("_");
        currentSeat.setRowNo(Integer.valueOf(seatLocation[0]));
        currentSeat.setSeatNo(Integer.valueOf(seatLocation[1]));
        seats.add(currentSeat);
      }

      Booking booking = new Booking();
      booking.setSeats(seats);
      booking.setScreening(screeningRepo.findById(screeningId));
      booking.setCode(generateUniqueCode());
      booking.setPhoneNo(phoneNo);
      bookingRepo.addBooking(booking);
      model.addAttribute("booking", booking);
      AppGlobals.printBooking(booking);
      return "bookings/user/booking-confirmed";
    } else {
      return "bookings/user/booking-error";
    }
  }

  /**
   * Displays the view for cancelling a booking
   */
  @GetMapping("/booking/cancel")
  public String cancelBooking() {
    return "bookings/user/booking-cancel";
  }

  /**
   * Deletes the booking from the database and displays the confirmation view
   */
  @PostMapping("/booking/cancel")
  public String bookingCancelled(@RequestParam String bookingCode,
                                 Model model) {
    boolean success = bookingRepo.deleteBookingByCode(bookingCode.toLowerCase());
    model.addAttribute("success", success);
    return "bookings/user/booking-cancelled";
  }

  /**
   * Generates an unique code that the customer can use to cancel their booking
   *
   * @return A {@link String} object representing the code
   */
  private String generateUniqueCode() {
    Random random = new SecureRandom();
    String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
    int length = 8;
    char[] charArr = characters.toCharArray();
    StringBuilder result = null;

    boolean isUnique = false;
    while (!isUnique) {
      result = new StringBuilder();
      for (int i = 0; i < length; i++) {
        result.append(charArr[random.nextInt(charArr.length)]);
      }
      isUnique = !bookingRepo.isCodeTaken(result.toString());
    }

    return result.toString();
  }

  /**
   * Displays the none booking view if there is not booking found for the phone number
   * Displays booking's seats overview if there is only one booking found for the phone number
   * Displays a list of bookings view if there are 2 or more bookings found
   */
  @PostMapping("/manage/bookings/")
  public String listBookingsForPhoneNo(@RequestParam(name = "bookingPhoneNo") String phoneNo,
                                       Model model) {
    List<Booking> bookingList = bookingRepo.findBookingByPhoneNo(phoneNo);
    model.addAttribute("bookingList", bookingList);
    model.addAttribute("phoneNo", phoneNo);
    switch (bookingList.size()) {
      case 0:
        return "ticketing/booking-none";
      case 1:
        SeatData bookingData = new SeatData();
        bookingData.setSeats(bookingList.get(0).getSeats());
        bookingData.setSubmittedData(new ArrayList<>());
        model.addAttribute("bookedSeats", bookingData);
        model.addAttribute("bookingId", bookingList.get(0).getId());
        return "ticketing/booking-redeem-seats";
      default:
        model.addAttribute("bookingList", bookingList);
        return "ticketing/list-of-bookings-phoneNo";
    }
  }


  /**
   * Displays the screening's bookings list view
   */
  @GetMapping("/manage/bookings/{screeningId}/list")
  public String showBookings(@PathVariable(name = "screeningId") int screeningId, Model model) {
    List<Booking> bookingList = bookingRepo.findBookingsForScreening(screeningId);
    model.addAttribute("bookingList", bookingList);
    switch (bookingList.size()) {
      case 0:
        model.addAttribute("screeningId", screeningId);
        return "ticketing/booking-none-screening";
      default:
        model.addAttribute("bookingList", bookingList);
        return "ticketing/list-of-bookings-screening";
    }
  }

  /**
   * Displays the booking's seats view
   */
  @GetMapping("/manage/bookings/redeem/{bookingId}")
  public String showBookedSeatsForBooking(Model model,
                                          @PathVariable(name = "bookingId") int bookingId) {
    Booking booking = bookingRepo.findBookingById(bookingId);
    SeatData bookingData = new SeatData();
    bookingData.setSeats(booking.getSeats());
    bookingData.setSubmittedData(new ArrayList<>());
    model.addAttribute("bookedSeats", bookingData);
    model.addAttribute("bookingId", bookingId);
    return "ticketing/booking-redeem-seats";
  }

  /**
   * Converts the data received from the booking's seats view, saves the ticket data
   * to the database and deletes the booking data from the database,
   * then redirects to manage upcoming screenings list view
   */
  @PostMapping("/manage/bookings/redeem/{bookingId}")
  public String showBookedSeats(@ModelAttribute SeatData data,
                                @PathVariable(name = "bookingId") int bookingId) {

    List<Ticket> ticketsList = new ArrayList<>();
    int screeningId = bookingRepo.findBookingById(bookingId).getScreening().getId();
    if (data != null) {
      for (Seat seat : seatRepo.convertStringSeatData(data.getSubmittedData())) {
        Ticket ticket = new Ticket();
        ticket.setScreening(screeningRepo.findById(screeningId));
        ticket.setSeat(seat);
        ticketsList.add(ticket);
      }
      ticketRepo.addTickets(ticketsList);
      bookingRepo.deleteBookingById(bookingId);
    }
    return "redirect:/manage/ticketing/";
  }

}
