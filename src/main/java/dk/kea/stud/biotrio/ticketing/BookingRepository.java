package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.cinema.Screening;
import dk.kea.stud.biotrio.cinema.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class that is responsible with managing {@link Booking} data within the database
 */
@Repository
public class BookingRepository {

  @Autowired
  private JdbcTemplate jdbc;
  @Autowired
  private ScreeningRepository screeningRepo;


  /**
   * Finds a particular {@link Booking} in the database based on the booking id
   *
   * @param id An integer representing the id to look up in the database
   * @return A {@link Booking} object on success, otherwise null if nothing is found
   */
  public Booking findBookingById(int id) {
    Booking booking = new Booking();
    String query = "SELECT * FROM bookings WHERE id = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, id);
    if (rs.first()) {
      booking.setId(rs.getInt("id"));
      booking.setScreening(screeningRepo.findById(rs.getInt("screening_id")));
      booking.setCode(rs.getString("code"));
      booking.setPhoneNo(rs.getString("phone_no"));

      List<Seat> bookingSeats = new ArrayList<>();

      String queryForSeats = "SELECT * FROM booked_seats WHERE booking_id = ?";
      SqlRowSet seatsRS = jdbc.queryForRowSet(queryForSeats, booking.getId());

      //iteration for setting the seats received from database
      while (seatsRS.next()) {
        Seat seat = new Seat();
        seat.setRowNo(seatsRS.getInt("row_no"));
        seat.setSeatNo(seatsRS.getInt("seat_no"));
        seat.setAvailable(false);
        seat.setSold(false);

        //adding the booked seat in the array
        bookingSeats.add(seat);
      }

      //setting the last attribute of the Booking object
      booking.setSeats(bookingSeats);
    }
    return booking;
  }

  /**
   * Gets the full list of bookings for a particular screening
   *
   * @param id An integer representing the screening id to look up in the database
   * @return A {@link List} of {@link Booking} objects of a particular screening
   */
  public List<Booking> findBookingsForScreening(int id) {
    Booking booking = null;
    List<Booking> screeningBookings = new ArrayList<>();
    Screening screening = screeningRepo.findById(id);

    String query = "SELECT * FROM bookings WHERE screening_id = ?";
    SqlRowSet bookingsRS = jdbc.queryForRowSet(query, id);

    //iteration for setting the bookings received from the database
    while (bookingsRS.next()) {
      booking = new Booking();
      booking.setId(bookingsRS.getInt("id"));
      booking.setPhoneNo(bookingsRS.getString("phone_no"));
      booking.setCode(bookingsRS.getString("code"));
      booking.setScreening(screening);

      //Booking object has an array of Seat objects
      List<Seat> bookingSeats = new ArrayList<>();

      String queryForSeats = "SELECT * FROM booked_seats WHERE booking_id = ?";
      SqlRowSet seatsRS = jdbc.queryForRowSet(queryForSeats, booking.getId());

      //iteration for setting the seats received from database
      while (seatsRS.next()) {
        Seat seat = new Seat();
        seat.setRowNo(seatsRS.getInt("row_no"));
        seat.setSeatNo(seatsRS.getInt("seat_no"));
        seat.setAvailable(false);
        seat.setSold(false);

        //adding the booked seat in the array
        bookingSeats.add(seat);
      }

      //setting the last attribute of the Booking object
      booking.setSeats(bookingSeats);

      //adding the Booking object in the screenings Array
      screeningBookings.add(booking);
    }
    return screeningBookings;
  }

  //
//  public List<Booking> findBookingByPhoneNo(String phoneNo) {
//    String query = "SELECT * FROM bookings WHERE phone_no = ?";
//    SqlRowSet rs = jdbc.queryForRowSet(query, phoneNo);
//    List<Booking> result = new ArrayList<>();
//    while (rs.next()) {
//      Booking booking = new Booking();
//      booking.setId(rs.getInt("id"));
//      booking.setPhoneNo(rs.getString("phone_no"));
//      booking.setCode(rs.getString("code"));
//      booking.setScreening(screeningRepo.findById(rs.getInt("screening_id")));
//      List<Seat> seats = new ArrayList<>();
//      String queryForSeats = "SELECT * FROM booked_seats WHERE booking_id = ?";
//      SqlRowSet seatsRS = jdbc.queryForRowSet(queryForSeats, booking.getId());
//
//      //iteration for setting the seats received from database
//      while (seatsRS.next()) {
//        Seat seat = new Seat();
//        seat.setRowNo(seatsRS.getInt("row_no"));
//        seat.setSeatNo(seatsRS.getInt("seat_no"));
//        seat.setAvailable(false);
//        seat.setSold(false);
//
//        //adding the booked seat in the array
//        seats.add(seat);
//      }
//
//      //setting the last attribute of the Booking object
//      booking.setSeats(seats);
//
//      //adding the Booking object in the screenings Array
//      result.add(booking);
//    }
//    return result;
//  }
  ///////////////////////////////////////////////////////////////////////////////////////////////
  public List<Booking> findBookingByPhoneNo(String phoneNo) {
    String query = "SELECT * FROM bookings WHERE phone_no = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, phoneNo);
    List<Booking> result = new ArrayList<>();
    while (rs.next()) {

      Booking booking = new Booking();
      booking.setId(rs.getInt("id"));
      booking.setPhoneNo(rs.getString("phone_no"));
      booking.setCode(rs.getString("code"));
      int screeningId = rs.getInt("screening_id");
      booking.setScreening(screeningRepo.findById(screeningId));
      List<Seat> seats = new ArrayList<>();
      String queryForSeats = "SELECT * FROM booked_seats WHERE booking_id = ?";
      SqlRowSet seatsRS = jdbc.queryForRowSet(queryForSeats, booking.getId());

      //iteration for setting the seats received from database
      while (seatsRS.next()) {
        Seat seat = new Seat();
        seat.setRowNo(seatsRS.getInt("row_no"));
        seat.setSeatNo(seatsRS.getInt("seat_no"));
        seat.setAvailable(false);
        seat.setSold(false);

        //adding the booked seat in the array
        seats.add(seat);
      }

      //setting the last attribute of the Booking object
      booking.setSeats(seats);

      //adding the Booking object in the screenings Array
      result.add(booking);
    }
    return result;
  }

  /**
   * Verifies if there is any booking with a particular code
   *
   * @param code A {@link String} object representing the auto-generated
   *             code of a particular booking
   * @return A boolean value that is true if the booking is found, otherwise false
   */
  public boolean isCodeTaken(String code) {
    String query = "SELECT * FROM bookings WHERE code = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, code);

    // .first() returns true if the RowSet contains at least one row, therefore the code is taken
    return rs.first();
  }

  /**
   * Saves the data of a {@link Booking} object to the database as a new entry
   *
   * @param booking The {@link Booking} object containing the data
   * @return The updates {@link Booking} object also containing id that was just
   * generated for the newly inserted entry
   */
  public Booking addBooking(Booking booking) {
    PreparedStatementCreator psc = new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO bookings (phone_no, code, screening_id) " +
                "VALUES (?, ?, ?)", new String[]{"id"}
        );
        ps.setString(1, booking.getPhoneNo());
        ps.setString(2, booking.getCode());
        ps.setInt(3, booking.getScreening().getId());
        return ps;
      }
    };

    KeyHolder key = new GeneratedKeyHolder();
    jdbc.update(psc, key);

    booking.setId(key.getKey().intValue());
    addBookedSeats(booking);
    return booking;
  }

  /**
   * Saves the data of a {@link Booking}'s list of {@link Seat} objects
   * to the database os new entries
   *
   * @param booking The {@link Booking} object containing the data
   */
  private void addBookedSeats(Booking booking) {
    String query = "INSERT INTO booked_seats VALUES (?, ?, ?);";
    for (Seat seat : booking.getSeats()) {
      jdbc.update(query, booking.getId(), seat.getRowNo(), seat.getSeatNo());
    }
  }

  /**
   * Deletes a record from the database based on the auto-generated code
   *
   * @param code A {@link String} object containing the data by which to identify the record
   *             in the database
   * @return true if the record is found and deleted, false otherwise
   */
  public boolean deleteBookingByCode(String code) {
    SqlRowSet rs = jdbc.queryForRowSet("SELECT id FROM bookings WHERE code = ?;", code);
    int id;

    if (rs.first()) {
      id = rs.getInt("id");
      //deleteBookedSeats(id);
      jdbc.update("DELETE FROM bookings WHERE id = ?;", id);
      return true;
    }

    return false;
  }

  /**
   * Deletes a booking record from the database based on the booking id
   *
   * @param id An integer representing the id by which to
   *           identify the record within the database
   */
  public void deleteBookingById(int id) {
    //deleteBookedSeats(id);
    String query = "DELETE FROM bookings WHERE id = ?;";
    jdbc.update(query, id);
  }

  public void deleteBookingsForScreening(int screeningId) {
    String query = "DELETE FROM bookings WHERE screening_id = ?;";
    jdbc.update(query, screeningId);
  }
}
