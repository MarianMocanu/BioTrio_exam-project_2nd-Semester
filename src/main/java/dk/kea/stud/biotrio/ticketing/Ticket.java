package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.cinema.Screening;

/**
 * The representation of a ticket within the system
 */
public class Ticket {
  /**
   * An integer representing the movie's id within the database
   */
  private int id;
  /**
   * A {@link Screening} object representing the screening
   * for which the ticket is sold
   */
  private Screening screening;
  /**
   * A {@link Seat} object representing a seat within the
   * {@link Screening}'s theater
   */
  private Seat seat;

  /**
   * @return An integer representation of the {@link Ticket}'s id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id An integer to set the {@link Ticket}'s id to
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return A {@link Screening} object representing the screening
   * for which the ticket is sold
   */
  public Screening getScreening() {
    return screening;
  }

  /**
   * @param screening A {@link Screening} object to set the {@link Ticket}'s
   *                  screening for which the ticket is sold
   */
  public void setScreening(Screening screening) {
    this.screening = screening;
  }

  /**
   * @return A {@link Seat} object representing a seat within the
   * {@link Screening}'s {@link dk.kea.stud.biotrio.cinema.Theater}
   */
  public Seat getSeat() {
    return seat;
  }

  /**
   * @param seat A {@link Seat} object to set a seat within
   *             {@link Screening}'s {@link dk.kea.stud.biotrio.cinema.Theater}
   */
  public void setSeat(Seat seat) {
    this.seat = seat;
  }
}
