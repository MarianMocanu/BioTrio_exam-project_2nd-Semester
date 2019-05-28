package dk.kea.stud.biotrio.ticketing;

import java.util.List;

/**
 * A helper class to facilitate sending and returning {@link Seat}
 * objects to and from forms
 */
public class SeatData {
  /**
   * A list of {@link Seat} objects to use for sending data to a form
   */
  private List<Seat> seats;
  /**
   * A list of {@link String} objects to use for retrieving data from a form
   */
  private List<String> submittedData;

  /**
   * @return A list of {@link Seat} objects
   */
  public List<Seat> getSeats() {
    return seats;
  }

  /**
   * @param seats A list of {@link Seat} objects
   */
  public void setSeats(List<Seat> seats) {
    this.seats = seats;
  }

  /**
   * @return A list of {@link String} objects
   */
  public List<String> getSubmittedData() {
    return submittedData;
  }

  /**
   * @param submittedData A list of {@link String} objects
   */
  public void setSubmittedData(List<String> submittedData) {
    this.submittedData = submittedData;
  }
}
