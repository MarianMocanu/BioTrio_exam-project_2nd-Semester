package dk.kea.stud.biotrio.ticketing;

import java.util.List;

public class SeatData {
  private List<Seat> seats;
  private List<String> submittedData;

  public List<Seat> getSeats() {
    return seats;
  }

  public void setSeats(List<Seat> seats) {
    this.seats = seats;
  }

  public List<String> getSubmittedData() {
    return submittedData;
  }

  public void setSubmittedData(List<String> submittedData) {
    this.submittedData = submittedData;
  }
}
