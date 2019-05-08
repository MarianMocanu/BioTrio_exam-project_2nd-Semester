package dk.kea.stud.biotrio.ticketing;

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
  public String showBookingsView(@PathVariable("id") int screeningId, Model model) {
    SeatData data = new SeatData();
    data.setSeats(seatRepo.getSeatStatusForScreening(screeningId));
    data.setSubmittedData(new ArrayList<>());
    for (Seat seat: data.getSeats()) {
      if (seat.isSold()) {
        data.getSubmittedData().add("" + seat.getRowNo() + "_" + seat.getSeatNo());
      } else {
        data.getSubmittedData().add("");
      }
    }

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
    for (String bookedSeatString: seatData.getSubmittedData()) {
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

    return "bookings/user/booking-confirmed";
  }

  @GetMapping("/booking/cancel")
  public String cancelBooking() {
    return "bookings/user/booking-cancel";
  }

  @PostMapping("/booking/cancel")
  public String bookingCancelled(@RequestParam String bookingCode, Model model) {
    boolean success = bookingRepo.deleteBooking(bookingCode.toLowerCase());
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

  //for employee use
  @GetMapping("/manage/screening/{screening_id}/bookings")
  public String findBooking(@PathVariable (name = "screening_id") int id, Model model){
    model.addAttribute("screeningId", id);
    return "ticketing/select-booking";
  }

  //for employee use
  @PostMapping("/manage/screening/{screening_id}/bookings")
  public String selectBooking(@RequestParam String phoneNumber, Model model,
                              @PathVariable(name = "screening_id") int id) {
    List<Booking> bookingList = bookingRepo.findBookingsByPhoneNo(phoneNumber, id);
    model.addAttribute("bookedSeats", bookingList);
    return "ticketing/booking-view";
  }

  @PostMapping("manage/screening/{screening_id}/booking/{phoneNo}")
  public String sellBookedSeats(){

    return "redirect:/manage/screenings";
  }

}
