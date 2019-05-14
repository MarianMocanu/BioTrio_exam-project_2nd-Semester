package dk.kea.stud.biotrio.cinema;

import dk.kea.stud.biotrio.ticketing.BookingRepository;
import dk.kea.stud.biotrio.ticketing.TicketRepository;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ScreeningRepository {
  @Autowired
  private JdbcTemplate jdbc;
  @Autowired
  private MovieRepository movieRepo;
  @Autowired
  private TheaterRepository theaterRepo;


  public Screening findById(int id) {
    Screening result = null;
    String query = "SELECT * FROM screenings WHERE id = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, id);

    if (rs.first()) {
      result = new Screening();
      result.setId(id);
      result.setMovie(movieRepo.findMovieById(rs.getInt("movie_id")));
      result.setTheater(theaterRepo.findTheater(rs.getInt("theater_id")));
      Timestamp ts = rs.getTimestamp("start_time");
      result.setStartTime(ts == null ? null : ts.toLocalDateTime());
      result.setNoAvailableSeats(getAvailableSeatsForScreening(result.getTheater().getId()));
    }

    return result;
  }

  private int getAvailableSeatsForScreening(int id) {
    Theater currentTheater = theaterRepo.findTheater(id);
    int totalSeats = currentTheater.getNoOfRows() * currentTheater.getSeatsPerRow();
    int occupiedSeats;

//  calculating the number of booked seats based on 2 tables from the database
    int bookedSeats = 0;
    String query = "SELECT COUNT(*) FROM bookings INNER JOIN booked_seats ON " +
        "booked_seats.booking_id = bookings.id WHERE bookings.screening_id = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, id);
    if (rs.first()) {
      bookedSeats = rs.getInt(1);
    }

//  calculating the number of sold seats based on 1 table from the database
    int soldSeats = 0;
    query = "SELECT COUNT(*) FROM tickets WHERE screening_id = ?";
    rs = jdbc.queryForRowSet(query, id);
    if (rs.first()) {
      soldSeats = rs.getInt(1);
    }

    occupiedSeats = soldSeats + bookedSeats;
    return (totalSeats - occupiedSeats);
  }

  public List<Screening> findUpcomingScreenings() {
    String query = "SELECT * FROM screenings WHERE start_time >= CURDATE() ORDER BY start_time";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    return getScreeningsListFromRowSet(rs);
  }

  private String convertToStringLabel(LocalDateTime startTime){
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd MMM");
    return startTime.format(timeFormatter);
  }

  public Map<String,List<Screening>> findUpcomingSreeningsForMap() {
    List<Screening> screeningList = findUpcomingScreenings();
    Map<String,List<Screening>> screenings = new HashMap<>();
    for(Screening screening:screeningList){
      String screeningDate = convertToStringLabel(screening.getStartTime());
      if(!screenings.containsKey(screeningDate)) {
        screenings.put(screeningDate, new ArrayList<>());
      }
      screenings.get(screeningDate).add(screening);
    }
    return screenings;
  }

  public List<Screening> findAllScreenings() {
    String query = "SELECT * FROM screenings;";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    return getScreeningsListFromRowSet(rs);
  }

  public List<Screening> findTodaysScreenings() {
    String query = "SELECT * FROM screenings WHERE DATE(start_time) = CURDATE() ORDER BY start_time";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    return getScreeningsListFromRowSet(rs);
  }

  /**
   *
   * @param rowSet jdkjkd
   * @return
   */
  private List<Screening> getScreeningsListFromRowSet(SqlRowSet rowSet) {
    List<Screening> result = new ArrayList<>();

    while (rowSet.next()) {
      Screening screening = new Screening();
      screening.setId(rowSet.getInt("id"));
      screening.setMovie(movieRepo.findMovieById(rowSet.getInt("movie_id")));
      screening.setTheater(theaterRepo.findTheater(rowSet.getInt("theater_id")));
      Timestamp start = rowSet.getTimestamp("start_time");
      screening.setStartTime(start == null ? null : start.toLocalDateTime());
      screening.setNoAvailableSeats(getAvailableSeatsForScreening(screening.getTheater().getId()));
      result.add(screening);
    }

    return result;
  }

  public Screening addScreening(Screening screening) {
    PreparedStatementCreator psc = new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO screenings (movie_id, theater_id, start_time) " +
                "VALUES (?, ?, ?)", new String[]{"id"}
        );
        ps.setInt(1, screening.getMovie().getId());
        ps.setInt(2, screening.getTheater().getId());
        ps.setTimestamp(3, Timestamp.valueOf(screening.getStartTime()));
        return ps;
      }
    };
    KeyHolder key = new GeneratedKeyHolder();
    jdbc.update(psc, key);

    screening.setId(key.getKey().intValue());
    return screening;
  }

  public void updateScreening(Screening screening) {
    String query = "UPDATE screenings SET " +
        "movie_id = ?, " +
        "theater_id = ?, " +
        "start_time = ? " +
        "WHERE id = ?;";
    jdbc.update(query,
        screening.getMovie().getId(),
        screening.getTheater().getId(),
        screening.getStartTime() == null ? null : Timestamp.valueOf(screening.getStartTime()),
        screening.getId());
  }

  public void deleteScreening(int id) {jdbc.update("DELETE FROM screenings WHERE id = ?;", id); }
}
