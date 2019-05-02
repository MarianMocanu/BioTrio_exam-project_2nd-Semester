package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.cinema.Screening;

public class Ticket {
  private int id;
  private Screening screening;
  private Seat seat;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Screening getScreening() {
    return screening;
  }

  public void setScreening(Screening screening) {
    this.screening = screening;
  }

  public Seat getSeat() {
    return seat;
  }

  public void setSeat(Seat seat) {
    this.seat = seat;
  }
}
