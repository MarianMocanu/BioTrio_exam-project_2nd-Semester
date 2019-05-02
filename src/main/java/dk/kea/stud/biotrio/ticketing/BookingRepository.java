package dk.kea.stud.biotrio.ticketing;

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

@Repository
public class BookingRepository {

  @Autowired
  private JdbcTemplate jdbc;
  @Autowired
  private ScreeningRepository screeningRepo;

  public List<Booking> findBookingsForScreening(int id) {
    Booking booking = null;
    List<Booking> screeningBookings = new ArrayList<>();

    String query = "SELECT * FROM bookings WHERE screening_id = ?";
    SqlRowSet bookingsRS = jdbc.queryForRowSet(query, id);

    //iteration for setting the bookings received from the database
    while (bookingsRS.next()) {
      booking = new Booking();
      booking.setId(bookingsRS.getInt("id"));
      booking.setPhoneNo(bookingsRS.getString("phone_no"));
      booking.setCode(bookingsRS.getString("code"));
      booking.setScreening(screeningRepo.findById(bookingsRS.getInt("screening_id")));

      //Booking object has an array of Seat objects
      List<Seat> bookingSeats = new ArrayList<>();

      String queryForSeats = "SELECT * FROM booked_seats WHERE booking_id = ?";
      SqlRowSet seatsRS = jdbc.queryForRowSet(queryForSeats, booking.getId());

      //iteration for setting the seats received from database
      while (seatsRS.next()) {
        Seat seat = new Seat();
        seat.setRowNo(seatsRS.getInt("row_no"));
        seat.setSeatNo(seatsRS.getInt("seat_no"));

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
    return booking;
  }

  //TODO public void addBookedSeats(Booking booking){}

  public void updateBooking(Booking booking) {
    String query = "UPDATE bookings SET " +
        "phone_no = ?, " +
        "code = ?, " +
        "screening_id = ? " +
        "WHERE id = ?;";
    jdbc.update(query,
        booking.getPhoneNo(),
        booking.getCode(),
        booking.getScreening().getId(),
        booking.getId());
  }

  //TODO public void updateBookedSeats(Booking booking){}

  public void deleteBooking(int id) {
    jdbc.update("DELETE FROM bookings WHERE id = ?;", id);
  }

  //TODO public void deleteBookedSeats(int bookingId){}
}
