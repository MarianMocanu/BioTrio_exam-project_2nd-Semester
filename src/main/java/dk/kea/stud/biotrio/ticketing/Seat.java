package dk.kea.stud.biotrio.ticketing;

public class Seat {
  private int rowNo;
  private int seatNo;
  private boolean isAvailalble;

  public boolean isAvailalble() {
    return isAvailalble;
  }

  public void setAvailalble(boolean availalble) {
    isAvailalble = availalble;
  }

  public int getRowNo() {
    return rowNo;
  }

  public void setRowNo(int rowNo) {
    this.rowNo = rowNo;
  }

  public int getSeatNo() {
    return seatNo;
  }

  public void setSeatNo(int seatNo) {
    this.seatNo = seatNo;
  }
}
