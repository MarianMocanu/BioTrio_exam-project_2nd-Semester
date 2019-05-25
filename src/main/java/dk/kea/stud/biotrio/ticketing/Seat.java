package dk.kea.stud.biotrio.ticketing;

/**
 * The representation of a theater seat
 */
public class Seat {
  /**
   * An integer representing the seat's row number
   */
  private int rowNo;
  /**
   * An integer representing the seat's position on its row
   */
  private int seatNo;
  /**
   * A boolean representing whether the seat is available (i.e. whether
   * there are neither tickets or bookings associated with it)
   */
  private boolean isAvailable;
  /**
   * A boolean representing whether the seat has a ticket associated
   */
  private boolean isSold;

  /**
   * @param sold A boolean to set the {@link Seat}'s isSold attribute
   */
  public void setSold(boolean sold) {
    isSold = sold;
  }

  /**
   * @return A boolean denoting whether the {@link Seat} has been sold
   */
  public boolean isSold() {
    return isSold;
  }

  /**
   * @return A boolean denoting whether the {@link Seat} is available
   */
  public boolean isAvailable() {
    return isAvailable;
  }

  /**
   * @param available A boolean to set the {@link Seat}'s isAvailable attribute
   */
  public void setAvailable(boolean available) {
    isAvailable = available;
  }

  /**
   * @return An integer representation of the {@link Seat}'s row number
   */
  public int getRowNo() {
    return rowNo;
  }

  /**
   * @param rowNo An integer to set the {@link Seat}'s row number to
   */
  public void setRowNo(int rowNo) {
    this.rowNo = rowNo;
  }

  /**
   * @return An integer representation of the {@link Seat}'s seat number
   */
  public int getSeatNo() {
    return seatNo;
  }

  /**
   * @param seatNo An integer to set the {@link Seat}'s seat number to
   */
  public void setSeatNo(int seatNo) {
    this.seatNo = seatNo;
  }
}
