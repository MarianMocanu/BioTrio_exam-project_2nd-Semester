package dk.kea.stud.biotrio.cinema;

import dk.kea.stud.biotrio.AppGlobals;
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
import java.util.*;

/**
 * Repository class that is responsible with managing {@link Screening} data within the database
 */
@Repository
public class ScreeningRepository {
  @Autowired
  private JdbcTemplate jdbc;
  @Autowired
  private MovieRepository movieRepo;
  @Autowired
  private TheaterRepository theaterRepo;

  /**
   * Gets a list of {@link Screening} objects which have their start time
   * in the past. Used for easy removal by the Delete Past Screenings use case
   *
   * @return The list of {@link Screening} objects
   */
  public List<Screening> findPastScreenings() {
    String query = "SELECT * FROM screenings WHERE start_time < CURDATE() ORDER BY start_time;";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    return extractScreeningsListFromRowSet(rs);
  }

  /**
   * Gets a list of {@link Screening} objects for a particular movie id, which have
   * their respective starting times in the future
   *
   * @param movieId An integer movie id by which to filter the screenings
   * @return The list of {@link Screening} objects
   */
  public List<Screening> findUpcomingScreeningsForMovieById(int movieId) {
    String query = "SELECT * FROM screenings WHERE start_time >= CURDATE()" +
        " AND movie_id = ? ORDER BY start_time;";
    SqlRowSet rs = jdbc.queryForRowSet(query, movieId);

    return extractScreeningsListFromRowSet(rs);
  }

  /**
   * Gets a {@link Map} of upcoming screenings for a particular movie, where the
   * key is a {@link String} representing a particular date, and the value is a
   * {@link List} of all {@link Screening} objects whose start time fall on that day
   *
   * @param movieId An integer movie id by which to filter the screenings
   * @return The map of {@link Screening} objects
   */
  public Map<String, List<Screening>> findUpcomingScreeningsForMovieAsMap(int movieId) {
    List<Screening> screeningList = findUpcomingScreeningsForMovieById(movieId);
    return convertScreeningsListToMap(screeningList);
  }

  /**
   * Helper function that converts a {@link List} of {@link Screening} objects into
   * a {@link Map} where the key is a {@link String} that represents a particular date,
   * and the value is a {@link List} of all the {@link Screening} objects whose start time
   * falls on that day
   *
   * @param screeningsList The {@link List} of {@link Screening} objects to convert
   * @return The map of {@link Screening} objects
   */
  private Map<String, List<Screening>> convertScreeningsListToMap(List<Screening> screeningsList) {
    Map<String, List<Screening>> screenings = new LinkedHashMap<>();
    for (Screening screening : screeningsList) {

      String screeningDate = screening.getStartTime().format(AppGlobals.TAB_LABEL_FORMAT);
      if (!screenings.containsKey(screeningDate)) {
        screenings.put(screeningDate, new ArrayList<>());
      }
      screenings.get(screeningDate).add(screening);
    }
    return screenings;
  }

  /**
   * Finds a particular {@link Screening} record in the database by a given id
   *
   * @param id An integer that represents the {@link Screening}'s id
   * @return A {@link Screening} object if the record is found, null otherwise
   */
  public Screening findById(int id) {
    Screening result = null;
    String query = "SELECT * FROM screenings WHERE id = ?;";
    SqlRowSet rs = jdbc.queryForRowSet(query, id);

    if (rs.first()) {
      result = extractNextScreeningFromRowSet(rs);
    }

    return result;
  }

  /**
   * Helper method to get the count of available seats for a particular screening
   *
   * @param id         An integer that represents the {@link Screening}'s id
   * @param theater_id An integer that represents the {@link Theater}'s id
   * @return An integer representing the number of available seats for the screening
   */
  private int getAvailableSeatsForScreening(int id, int theater_id) {
    Theater currentTheater = theaterRepo.findTheater(theater_id);
    int totalSeats = currentTheater.getNoOfRows() * currentTheater.getSeatsPerRow();
    int occupiedSeats;

    // Count the number of booked seats for the screening
    int bookedSeats = 0;
    String query = "SELECT COUNT(*) FROM bookings INNER JOIN booked_seats ON " +
        "booked_seats.booking_id = bookings.id WHERE bookings.screening_id = ?;";
    SqlRowSet rs = jdbc.queryForRowSet(query, id);
    if (rs.first()) {
      bookedSeats = rs.getInt(1);
    }

    // Count the number of tickets for the screening
    int soldSeats = 0;
    query = "SELECT COUNT(*) FROM tickets WHERE screening_id = ?;";
    rs = jdbc.queryForRowSet(query, id);
    if (rs.first()) {
      soldSeats = rs.getInt(1);
    }

    occupiedSeats = soldSeats + bookedSeats;
    return (totalSeats - occupiedSeats);
  }

  /**
   * Gets a {@link List} of screenings that have their start
   * time half an hour or more into the future
   *
   * @return The {@link List} of {@link Screening} objects
   */
  public List<Screening> findUpcomingScreenings() {
    String query = "SELECT * FROM screenings WHERE start_time >= " +
        "ADDTIME(UTC_TIMESTAMP(), TIME('00:" +
        AppGlobals.BOOKINGS_GO_ON_SALE_BEFORE_SCREENING_MINUTES + "')) ORDER BY start_time;";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    return extractScreeningsListFromRowSet(rs);
  }

  /**
   * Gets a map of upcoming screenings grouped by date
   *
   * @return The {@link Map} of {@link Screening} objects
   */
  public Map<String, List<Screening>> findUpcomingScreeningsAsMap() {
    List<Screening> screeningsList = findUpcomingScreenings();
    return convertScreeningsListToMap(screeningsList);
  }

  /**
   * Gets a map of all screenings grouped by date
   *
   * @return The {@link Map} of {@link Screening} objects
   */
  public Map<String, List<Screening>> findAllScreeningsAsMap() {
    List<Screening> screeningsList = findAllScreenings();
    return convertScreeningsListToMap(screeningsList);
  }

  /**
   * Gets a list of all the screenings in the database
   *
   * @return The {@link List} of {@link Screening} objects
   */
  public List<Screening> findAllScreenings() {
    String query = "SELECT * FROM screenings ORDER BY start_time;";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    return extractScreeningsListFromRowSet(rs);
  }

  /**
   * Helper method that extracts all screenings from a rowset
   *
   * @param rowSet The {@link SqlRowSet} object that contains the data
   * @return A {@link List} of {@link Screening} objects
   */
  private List<Screening> extractScreeningsListFromRowSet(SqlRowSet rowSet) {
    List<Screening> result = new ArrayList<>();

    while (rowSet.next()) {
      result.add(extractNextScreeningFromRowSet(rowSet));
    }

    return result;
  }

  /**
   * Helper method which extracts the screening from a rowset,
   * which it is currently pointing to
   *
   * @param rowSet The {@link SqlRowSet} object containing the data
   * @return A {@link Screening} object
   */
  private Screening extractNextScreeningFromRowSet(SqlRowSet rowSet) {
    Screening result = new Screening();
    result.setId(rowSet.getInt("id"));
    result.setMovie(movieRepo.findMovieById(rowSet.getInt("movie_id")));
    result.setTheater(theaterRepo.findTheater(rowSet.getInt("theater_id")));
    Timestamp start = rowSet.getTimestamp("start_time");
    result.setStartTime(start == null ? null : start.toLocalDateTime());
    result.setNoAvailableSeats(getAvailableSeatsForScreening(result.getId(), result.getTheater().getId()));
    return result;
  }

  /**
   * Saves the data of a screening to the database as a new entry, and then
   * returns the updated object
   *
   * @param screening The {@link Screening} object which contains the data
   * @return The updated {@link Screening} which now also contains the correct
   * id, of the newly added entry to the database
   */
  public Screening addScreening(Screening screening) {
    PreparedStatementCreator psc = new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO screenings (movie_id, theater_id, start_time) " +
                "VALUES (?, ?, ?);", new String[]{"id"}
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

  /**
   * Updates an entry in the database based on the data of a screening object
   *
   * @param screening The {@link Screening} object to update
   */
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

  /**
   * Deletes a particular {@link Screening} based on a given id
   *
   * @param id An integer by which to find the screening to be deleted
   */
  public void deleteScreening(int id) {
    jdbc.update("DELETE FROM screenings WHERE id = ?;", id);
  }

  /**
   * Finds all past screenings and deletes them from the database
   */
  public void deletePastScreenings() {
    for (Screening screening : findPastScreenings()) {
      deleteScreening(screening.getId());
    }
  }

  /**
   * Gets all screenings that are scheduled in the same theater 12 hours before
   * and after a given screening.
   *
   * @param screening A {@link Screening} object by which to look up other screenings
   * @return A {@link List} of {@link Screening} objects that match the criteria
   */
  public List<Screening> findScreeningsThatMightConflict(Screening screening) {
    String query = "SELECT * FROM screenings " +
        "WHERE start_time > ? - INTERVAL 8 HOUR " +
        "AND start_time < ? + INTERVAL 8 HOUR " +
        "AND theater_id = ? " +
        "ORDER BY start_time;";
    Timestamp startTimeAsTimestamp = Timestamp.valueOf(screening.getStartTime());
    SqlRowSet rs = jdbc.queryForRowSet(query, startTimeAsTimestamp,
        startTimeAsTimestamp, screening.getTheater().getId());

    // isBeforeFirst() returns true only if the cursor is before the first record,
    // but false if it's not, or if there are no records at all
    List<Screening> result = rs.isBeforeFirst() ? new ArrayList<>() : null;
    while (rs.next()) {
      Screening scr = extractNextScreeningFromRowSet(rs);
      result.add(scr);
    }

    return result;
  }

  /**
   * Checks if a particular screening can be safely deleted, meaning
   * that it has no associated bookings and/or tickets
   *
   * @param s The {@link Screening} to check
   * @return A true if the screening can be safely deleted, false otherwise
   */
  public boolean canDelete(Screening s) {
    if (s.getStartTime().isBefore(LocalDateTime.now())) {
      return true;
    }
    String query = ("SELECT COUNT(*) FROM (SELECT id FROM bookings " +
        "WHERE screening_id = ? " +
        "UNION " +
        "SELECT id FROM tickets " +
        "WHERE screening_id = ?) AS x;");
    SqlRowSet rs = jdbc.queryForRowSet(query, s.getId(), s.getId());
    rs.first();
    int noSeats = rs.getInt(1);
    return noSeats == 0;
  }
}
