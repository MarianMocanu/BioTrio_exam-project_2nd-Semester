package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TechnologyRepository {
  @Autowired
  private JdbcTemplate jdbc;

  public List<Technology> getAllTechnologies() {
    String query = "SELECT * FROM technologies ORDER BY id;";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    List<Technology> result = new ArrayList<>();
    while (rs.next()) {
      Technology current = new Technology();
      current.setId(rs.getInt("id"));
      current.setName(rs.getString("name"));

      result.add(current);
    }

    return result;
  }

  public Technology getTechnologyById(int id) {
    String query = "SELECT * FROM technologies WHERE id = ?;";
    SqlRowSet rs = jdbc.queryForRowSet(query, id);

    Technology result = null;
    if (rs.first()) {
      result = new Technology();
      result.setId(rs.getInt("id"));
      result.setName(rs.getString("name"));
    }

    return result;
  }

  public List<String> getSupportedTechnologies(int theaterId) {
    String query = "SELECT technologies.name FROM technologies_to_theaters " +
        "INNER JOIN technologies ON technologies_to_theaters.technology_id = technologies.id " +
        "WHERE technologies_to_theaters.theater_id = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, theaterId);

    return extractStringsFromRowSet(rs);
  }

  public List<String> getRequiredTechnologies(int movieId) {
    String query = "SELECT technologies.name FROM technologies_to_movies " +
        "INNER JOIN technologies ON technologies_to_movies.technology_id = technologies.id " +
        "WHERE technologies_to_movies.movie_id = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, movieId);

    return extractStringsFromRowSet(rs);
  }

  private List<String> extractStringsFromRowSet(SqlRowSet rs) {
    List<String> result = rs.isBeforeFirst() ? new ArrayList<>() : null;
    while (rs.next()) {
      result.add(rs.getString("name"));
    }

    return result;
  }

  public void addTechnology(String name) {
    jdbc.update("INSERT INTO technologies(name) VALUE (?);", name);
  }

  public void deleteTechnology(int id) {
    jdbc.update("DELETE FROM technologies_to_movies WHERE technology_id = ?;", id);
    jdbc.update("DELETE FROM technologies_to_theaters WHERE technology_id = ?;", id);
    jdbc.update("DELETE FROM technologies WHERE id = ?;", id);
  }
}
