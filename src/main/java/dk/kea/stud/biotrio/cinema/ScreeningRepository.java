package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class ScreeningRepository {
  @Autowired
  private JdbcTemplate jdbc;
  @Autowired
  private MovieRepository movieRepo;

  public Screening findById(int id) {
    Screening result = null;
    String query = "SELECT * FROM screenings WHERE id = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, id);

    if(rs.first()) {
      result = new Screening();
      result.setId(id);
      result.setMovie(null/*TODO movieRepo.findMovieById(rs.getInt("movie_id"))*/);
      result.setTheater(null/*TODO theaterRepo.findTheaterById(rs.getInt("theater_id"))*/);
      Timestamp ts = rs.getTimestamp("start_time");
      result.setStartTime(ts == null ? null : ts.toLocalDateTime());
    }

    return result;
  }
}
