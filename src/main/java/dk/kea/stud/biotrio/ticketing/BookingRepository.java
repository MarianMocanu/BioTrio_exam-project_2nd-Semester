package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.cinema.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

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
        SqlRowSet bookingsRS = jdbc.queryForRowSet( query, id );

        //iteration for setting the bookings received from the database
        while (bookingsRS.next()) {
            booking = new Booking();
            booking.setId( bookingsRS.getInt( "id" ) );
            booking.setPhoneNo( bookingsRS.getString( "phone_no" ) );
            booking.setCode( bookingsRS.getString( "code" ) );
            booking.setScreening( screeningRepo.findById( bookingsRS.getInt( "screening_id" ) ) );

            //Booking object has an array of Seat objects
            List<Seat> bookingSeats = new ArrayList<>();

            String queryForSeats = "SELECT * FROM booked_seats WHERE booking_id = ?";
            SqlRowSet seatsRS = jdbc.queryForRowSet( queryForSeats, booking.getId() );

            //iteration for setting the seats received from database
            while (seatsRS.next()) {
                Seat seat = new Seat();
                seat.setRowNo( seatsRS.getInt( "row_no" ) );
                seat.setSeatNo( seatsRS.getInt( "seat_no" ) );

                //adding the booked seat in the array
                bookingSeats.add( seat );
            }

            //setting the last attribute of the Booking object
            booking.setSeats( bookingSeats );

            //adding the Booking object in the screenings Array
            screeningBookings.add( booking );
        }
        return screeningBookings;
    }

    //TODO public Booking addBooking(Booking booking) {}

    //TODO public void updateBooking(Booking booking) {}

    //TODO public void deleteBooking(int id) {}

}
