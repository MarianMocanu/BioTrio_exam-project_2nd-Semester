package dk.kea.stud.biotrio.cinema;

public class ScreeningForm {
  private int id;
  private int movieId;
  private int theaterId;
  private String startTime;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getMovieId() {
    return movieId;
  }

  public void setMovieId(int movieId) {
    this.movieId = movieId;
  }

  public int getTheaterId() {
    return theaterId;
  }

  public void setTheaterId(int theaterId) {
    this.theaterId = theaterId;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  @Override
  public String toString() {
    return "ScreeningForm{" +
        "id=" + id +
        ", movieId=" + movieId +
        ", theaterId=" + theaterId +
        ", startTime=" + startTime +
        '}';
  }
}
