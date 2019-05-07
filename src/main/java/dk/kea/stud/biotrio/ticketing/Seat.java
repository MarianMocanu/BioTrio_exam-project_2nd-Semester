package dk.kea.stud.biotrio.ticketing;

public class Seat {
  private int rowNo;
  private int seatNo;
  private boolean isAvailable;
  private boolean isSold;

  public void setSold(boolean sold) {
    isSold = sold;
  }

  public boolean isSold() {
    return isSold;
  }

  public void setAvailalble(boolean available) {
    this.isAvailable = available;
  }

  public boolean isAvailable() {
    return isAvailable;
  }

  public void setAvailable(boolean available) {
    isAvailable = available;
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

  @Override
  public String toString() {
    return "Seat{" +
        "rowNo=" + rowNo +
        ", seatNo=" + seatNo +
        ", isAvailable=" + isAvailable +
        ", isSold=" + isSold +
        '}';
  }
}
