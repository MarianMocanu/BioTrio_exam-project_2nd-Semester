package dk.kea.stud.biotrio.cinema;

import dk.kea.stud.biotrio.AppGlobals;

import java.time.LocalDateTime;

/**
 * The representation of a screening (a particular movie played in
 * a particular theater at a particular point in time)
 */
public class Screening {
  /**
   * An integer representing the screening's id within the database
   */
  private int id;
  /**
   * A {@link Movie} object representing the movie to be screened
   */
  private Movie movie;
  /**
   * A {@link Theater} object representing the movie theater in which
   * the screening will take place
   */
  private Theater theater;
  /**
   * A {@link java.time.LocalDateTime} object representing the point
   * in time when the screening will take place
   */
  private LocalDateTime startTime;
  /**
   * An integer representing the number of available seats for the screening
   */
  private int noAvailableSeats;

  /**
   * @return An integer representation of the {@link Screening}'s id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id An integer to set the {@link Screening}'s id to
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return A {@link Movie} object representing the movie to be screened
   */
  public Movie getMovie() {
    return movie;
  }

  /**
   * @param movie A {@link Movie} object to set the {@link Screening}'s
   *              movie to be played to
   */
  public void setMovie(Movie movie) {
    this.movie = movie;
  }

  /**
   * @return A {@link Theater} object representing the theater where the
   * screening will take place
   */
  public Theater getTheater() {
    return theater;
  }

  /**
   * @param theater A {@link Theater} object to set the theater where
   *                the {@link Screening} will take place in
   */
  public void setTheater(Theater theater) {
    this.theater = theater;
  }

  /**
   * @return A {@link LocalDateTime} object representing the point in
   * time when the screening is to take place
   */
  public LocalDateTime getStartTime() {
    return startTime;
  }

  /**
   * @return A {@link String} object containing a formatted representation
   * of the screening's starting time
   */
  public String getFormattedStartTime() {
    return startTime.format(AppGlobals.DATE_TIME_FORMAT);
  }

  /**
   * @param startTime A {@link LocalDateTime} to set the {@link Screening}'s
   *                  starting time point to
   */
  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  /**
   * @return An integer representing the number of seats available for the screening
   */
  public int getNoAvailableSeats() {
    return noAvailableSeats;
  }

  /**
   * @param noAvailableSeats An integer to set the {@link Screening}'s number
   *                         pf available seats to
   */
  public void setNoAvailableSeats(int noAvailableSeats) {
    this.noAvailableSeats = noAvailableSeats;
  }
}
