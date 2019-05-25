package dk.kea.stud.biotrio;

import dk.kea.stud.biotrio.ticketing.Booking;
import dk.kea.stud.biotrio.ticketing.Ticket;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Static class that holds ceretain application-wide settings and helper methods
 */
public class AppGlobals {
  /**
   * A minimum time buffer between two consecutive screenings, allowing
   * enough time for the employees to clean the theater
   */
  public static final int TIME_BUFFER_MINUTES_BETWEEN_SCREENINGS = 15;
  /**
   * The maximum number of seats a customer can select per booking
   */
  public static final int MAX_NUMBER_OF_SEATS_PER_BOOKING = 4;
  /**
   * The default format for inputting / outputting date & time data
   */
  public static final DateTimeFormatter DTFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  /**
   * The default format for inputting / outputting date data
   */
  public static final DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Prints ticket data to a text file
   *
   * @param ticket the ticket data to print
   */
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

  /**
   * Prints booking data to a text file
   *
   * @param booking the booking data to print
   */
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
