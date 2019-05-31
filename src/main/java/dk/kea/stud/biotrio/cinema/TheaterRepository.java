package dk.kea.stud.biotrio.cinema;

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
 * Repository class that is responsible with managing {@link Theater} data within the database
 */
@Repository
public class TheaterRepository {
  @Autowired
  private JdbcTemplate jdbc;
  @Autowired
  private TechnologyRepository technologyRepo;

  /**
   * Gets a particular theater from the database based on an id
   *
   * @param id The integer id to look up in the database
   * @return A {@link Theater} object if found, otherwise null
   */
  public Theater findTheater(int id) {
    SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM theaters WHERE id = ?", id);
    Theater theater = null;
    //while (rs.next()) - reads all the results and then picks the one with the unique id.
    //since the id is unique we know that we get only one result
    // so we use conditioning to read the first (and only row)
    if (rs.first()) {
      theater = new Theater();
      theater.setId(rs.getInt("id"));
      theater.setName(rs.getString("name"));
      theater.setNoOfRows(rs.getInt("no_of_rows"));
      theater.setSeatsPerRow(rs.getInt("seats_per_row"));
      theater.setSupportedTechnologies(technologyRepo.getSupportedTechnologies(id));
    }
    return theater;
  }

  /**
   * Gets all the theater records from the database
   *
   * @return A list of {@link Theater} objects, or null if there are no records in the database
   */
  public List<Theater> findAllTheaters() {
    SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM theaters");
    List<Theater> theaterList = new ArrayList<>();
    while (rs.next()) {
      Theater theater = new Theater();
      theater.setId(rs.getInt("id"));
      theater.setName(rs.getString("name"));
      theater.setNoOfRows(rs.getInt("no_of_rows"));
      theater.setSeatsPerRow(rs.getInt("seats_per_row"));
      theater.setSupportedTechnologies(technologyRepo.getSupportedTechnologies(theater.getId()));

      theaterList.add(theater);
    }
    return theaterList;
  }

  /**
   * Saves the data of an {@link Theater} object to the database as a new entry
   *
   * @param theater The {@link Theater} object to save in the database
   */
  public void insert(Theater theater) {

    PreparedStatementCreator psc = new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO theaters(name, no_of_rows, seats_per_row) VALUES(?,?,?)",
            new String[]{"id"});
        ps.setString(1, theater.getName());
        ps.setInt(2, theater.getNoOfRows());
        ps.setInt(3, theater.getSeatsPerRow());
        return ps;
      }
    };

    KeyHolder id = new GeneratedKeyHolder();
    jdbc.update(psc, id);

    theater.setId(id.getKey().intValue());
    technologyRepo.setSupportedTechnologies(theater.getId(), theater.getSupportedTechnologies());
  }

  /**
   * Update an existing record in the database with the data of an {@link Theater} object
   *
   * @param theater A {@link Theater} object to update the database with
   */
  public void update(Theater theater) {

    jdbc.update("UPDATE theaters SET name =? ,no_of_rows =? , seats_per_row =?  WHERE id = ?",
        theater.getName(), theater.getNoOfRows(), theater.getSeatsPerRow(), theater.getId());
    technologyRepo.setSupportedTechnologies(theater.getId(), theater.getSupportedTechnologies());
  }

  /**
   * Deletes a theater entry from the database
   *
   * @param id An integer id by which to identify the entry to be deleted
   */
  public void deleteTheater(int id) {
    technologyRepo.setSupportedTechnologies(id, null);
    jdbc.update("DELETE FROM theaters WHERE id = ?", id);
  }

  /**
   * Checks whether a theater can be safely deleted. We want to disallow deleting theaters
   * that still have associated screenings within the database
   *
   * @param t A {@link Theater} object to check if it's associated record within
   *          the database can be safely deleted
   * @return true if it can be safely deleted, otherwise false
   */
  public boolean canDelete(Theater theater) {
    String query = ("SELECT COUNT(*) FROM screenings WHERE theater_id = ?;");
    SqlRowSet rs = jdbc.queryForRowSet(query, theater.getId());
    rs.first();
    int noScreenings = rs.getInt(1);

    // If the number of associated screenings is 0, the theater can be deleted
    // and thus the method will return true
    return noScreenings == 0;
  }

}
