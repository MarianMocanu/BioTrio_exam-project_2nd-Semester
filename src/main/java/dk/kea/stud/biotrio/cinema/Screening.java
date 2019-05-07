package dk.kea.stud.biotrio.cinema;

import java.time.LocalDateTime;

public class Screening {
  private int id;
  private Movie movie;
  private Theater theater;
  private LocalDateTime startTime;
  private int noAvailableSeats;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Movie getMovie() {
    return movie;
  }

  public void setMovie(Movie movie) {
    this.movie = movie;
  }

  public Theater getTheater() {
    return theater;
  }

  public void setTheater(Theater theater) {
    this.theater = theater;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public int getNoAvailableSeats() {
    return noAvailableSeats;
  }

  public void setNoAvailableSeats(int noAvailableSeats) {
    this.noAvailableSeats = noAvailableSeats;
  }

  @Override
  public String toString() {
    return "Screening{" +
        "id=" + id +
        ", movie=" + movie +
        ", theater=" + theater +
        ", startTime=" + startTime +
        '}';
  }
}
