package dk.kea.stud.biotrio;

import dk.kea.stud.biotrio.ticketing.Booking;
import dk.kea.stud.biotrio.ticketing.Ticket;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppGlobals {
  public static final int TIME_BUFFER_MINUTES_BETWEEN_SCREENINGS = 15;
  public static final int MAX_NUMBER_OF_SEATS_PER_BOOKING = 4;
  public static final DateTimeFormatter DTFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  public static final DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static void printTicket(Ticket ticket) {
    BufferedWriter writer = null;
    LocalDateTime movieTime = ticket.getScreening().getStartTime();
    String rowNo = Integer.toString(ticket.getSeat().getRowNo());
    String seatNo = Integer.toString(ticket.getSeat().getSeatNo());
    String movieTitle = ticket.getScreening().getMovie().getTitle();
    String theaterName = ticket.getScreening().getTheater().getName();
    try {
      String fileSeparator = System.getProperty("file.separator");
      File folder = new File("tickets");
      folder.mkdir();
      String absoluteFilePath = "tickets" + fileSeparator + ticket.getScreening().getId()
          + "_" + ticket.getId() + ".txt";
      File logFile = new File(absoluteFilePath);
      writer = new BufferedWriter(new FileWriter(logFile));
      writer.write("Bio Trio presents");
      writer.newLine();
      writer.write(movieTitle);
      writer.newLine();
      writer.write(movieTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
      writer.write("\n at " + movieTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
      writer.newLine();
      writer.write("Theater:" + theaterName);
      writer.write("\t Row:" + rowNo);
      writer.write("\t Seat:" + seatNo);
      writer.newLine();
      writer.write("Thank you for choosing BioTrio!");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        writer.close();
      } catch (Exception e) {
      }
    }
  }

  public static void printBooking(Booking booking) {
    BufferedWriter writer = null;
    LocalDateTime movieTime = booking.getScreening().getStartTime();
    String noOfSeats = Integer.toString(booking.getSeats().size());
    String movieTitle = booking.getScreening().getMovie().getTitle();
    String theaterName = booking.getScreening().getTheater().getName();
    try {
      String fileSeparator = System.getProperty("file.separator");
      File folder = new File("bookings");
      folder.mkdir();
      String absoluteFilePath = "bookings" + fileSeparator + booking.getScreening().getId()
          + "_" + booking.getId() + ".txt";
      File logFile = new File(absoluteFilePath);
      writer = new BufferedWriter(new FileWriter(logFile));
      writer.write("You booked seats for");
      writer.newLine();
      writer.write(movieTitle);
      writer.newLine();
      writer.write(movieTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
      writer.write("\n at " + movieTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
      writer.newLine();
      writer.write("Theater:" + theaterName);
      writer.write("\t Number of booked seats:" + noOfSeats);
      writer.newLine();
      writer.write("Thank you for choosing BioTrio!");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        writer.close();
      } catch (Exception e) {
      }
    }
  }
}
