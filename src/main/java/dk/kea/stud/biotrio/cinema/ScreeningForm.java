package dk.kea.stud.biotrio.cinema;

/**
 * A helper class that maps exactly to the add and edit screening forms
 */
public class ScreeningForm {
  /**
   * An integer that represents the input screening's id
   */
  private int id;
  /**
   * An integer that represents the screening's associated movie's id
   */
  private int movieId;
  /**
   * An integer that represents the screening's associated theater's id
   */
  private int theaterId;
  /**
   * A {@link String} that represents the screening's start time
   */
  private String startTime;

  /**
   * @return An integer representation of the {@link ScreeningForm}'s id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id An integer to set the {@link ScreeningForm}'s id to
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return An integer representation of the {@link ScreeningForm}'s associated movie id
   */
  public int getMovieId() {
    return movieId;
  }

  /**
   * @param movieId An integer to set the {@link ScreeningForm}'s associated movie id to
   */
  public void setMovieId(int movieId) {
    this.movieId = movieId;
  }

  /**
   * @return An integer representation of the {@link ScreeningForm}'s associated theater id
   */
  public int getTheaterId() {
    return theaterId;
  }

  /**
   * @param theaterId An integer to set the {@link ScreeningForm}'s associated theater id to
   */
  public void setTheaterId(int theaterId) {
    this.theaterId = theaterId;
  }

  /**
   * @return An {@link String} representation of the {@link ScreeningForm}'s start time
   */
  public String getStartTime() {
    return startTime;
  }

  /**
   * @param startTime A {@link String} to set the {@link ScreeningForm}'s starting time to
   */
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }
}
