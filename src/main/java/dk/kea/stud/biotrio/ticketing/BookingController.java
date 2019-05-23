package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.AppSettings;
import dk.kea.stud.biotrio.cinema.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

  @GetMapping("/booking/{id}")
  public String showBookingsView(@PathVariable("id") int screeningId,
                                 Model model) {
    SeatData data = new SeatData();
    data.setSeats(seatRepo.getSeatStatusForScreening(screeningId));
    data.setSubmittedData(new ArrayList<>());

    model.addAttribute("maxSeatsPerBooking", AppSettings.MAX_NUMBER_OF_SEATS_PER_BOOKING);
    model.addAttribute("screening", screeningRepo.findById(screeningId));
    model.addAttribute("seatData", data);
    return "bookings/user/make-booking";
  }

  @PostMapping("/booking/save")
  public String saveBooking(@ModelAttribute SeatData seatData,
                            @RequestParam int screeningId,
                            @RequestParam String phoneNo,
                            Model model) {
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
    Helper.printBooking(booking);
    return "bookings/user/booking-confirmed";
  }

  @GetMapping("/booking/cancel")
  public String cancelBooking() {
    return "bookings/user/booking-cancel";
  }

  @PostMapping("/booking/cancel")
  public String bookingCancelled(@RequestParam String bookingCode,
                                 Model model) {
    boolean success = bookingRepo.deleteBookingByCode(bookingCode.toLowerCase());
    model.addAttribute("success", success);
    return "bookings/user/booking-cancelled";
  }

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

  @GetMapping("/manage/screening/{screeningId}/bookings")
  public String getPhoneNo(@PathVariable(name = "screeningId") int id,
                           Model model) {
    model.addAttribute("screeningId", id);
    return "ticketing/get-phone-no";
  }

  @PostMapping("/manage/screening/{screeningId}/bookings")
  public String showBooking(@RequestParam String bookingPhoneNo,
                            @PathVariable(name = "screeningId") int id,
                            Model model) {
    List<Booking> bookingList = bookingRepo.findBookingByPhoneNo(bookingPhoneNo, id);
    model.addAttribute("screeningId", id);
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
        return "ticketing/list-of-bookings";
    }
  }

  @GetMapping("/manage/screening/{screeningId}/bookings/{bookingId}")
  public String sellBookedSeats(Model model,
                                @PathVariable(name = "screeningId") int screeningId,
                                @PathVariable(name = "bookingId") int bookingId) {
    Booking booking = bookingRepo.findBookingById(bookingId);
    SeatData bookingData = new SeatData();
    bookingData.setSeats(booking.getSeats());
    bookingData.setSubmittedData(new ArrayList<>());
    model.addAttribute("screeningID", screeningId);
    model.addAttribute("bookedSeats", bookingData);
    model.addAttribute("bookingId", bookingId);
    return "ticketing/booking-redeem-seats";
  }

  @PostMapping("/manage/screening/{screeningId}/booking/redeem/{bookingId}")
  public String sellBookedSeats(@PathVariable(name = "screeningId") int screeningId,
                                @ModelAttribute SeatData data,
                                @PathVariable(name = "bookingId") int bookingId) {
    List<Ticket> ticketsList = new ArrayList<>();
    for (Seat seat : seatRepo.getSeatsInfo(data.getSubmittedData())) {
      Ticket ticket = new Ticket();
      ticket.setScreening(screeningRepo.findById(screeningId));
      ticket.setSeat(seat);
      ticketsList.add(ticket);
    }
    ticketRepo.addTickets(ticketsList);
    bookingRepo.deleteBookingById(bookingId);
    return "redirect:/manage/screenings";
  }

}
