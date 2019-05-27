package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.cinema.Screening;

import java.util.List;

/**
 * The representation of a booking (an arrangement made by the customer
 * to have some particular seats for a particular screening)
 */

public class Booking {
  /**
   * An integer representing the booking's id within a database
   */
  private int id;
  /**
   * An integer representing the phone number of the customer that made the booking
   */
  private String phoneNo;
  /**
   * A {@link String} representing the unique code that was provided for the
   * ability to cancel the booking
   */
  private String code;
  /**
   * A {@link Screening} object representing  the screening for which the booking is made
   */
  private Screening screening;
  /**
   * A  list of {@link Seat}  objects representing the booked seats for this particular booking
   */
  private List<Seat> seats;

  /**
   * @return An integer representation of the {@link Booking}'s id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id An integer to set the {@link Booking}'s id to
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return A {@link String} representation of the phone number of the customer
   * that made the booking
   */
  public String getPhoneNo() {
    return phoneNo;
  }

  /**
   * @param phoneNo A {@link String} object to set the {@link Booking}'s phone
   *                number of the customer that made the booking
   */
  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }

  /**
   * @return A {@link String} object representing the {@link Booking}'s unique code
   * that was provided for the ability to cancel the booking
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code A {@link String} object to set the unique code
   *             that was provided for the ability to cancel the booking
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return A {@link Screening} object representing the screening for which the booking is made
   */
  public Screening getScreening() {
    return screening;
  }

  /**
   * @param screening A {@link Screening} object to set the screening
   *                  for which the {@link Booking} is made
   */
  public void setScreening(Screening screening) {
    this.screening = screening;
  }

  /**
   * @return A {@link List} of {@link Seat} objects representing
   * the booked seats for the {@link Booking}
   */
  public List<Seat> getSeats() {
    return seats;
  }

  /**
   * @param seats A {@link List} of {@link Seat} objects to set the
   *              the booked seats for the {@link Booking}
   */
  public void setSeats(List<Seat> seats) {
    this.seats = seats;
  }
}
