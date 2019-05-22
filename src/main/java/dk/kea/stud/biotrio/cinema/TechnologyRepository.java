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

  public List<Technology> getSupportedTechnologies(int theaterId) {
    String query = "SELECT technologies.name FROM technologies_to_theaters " +
        "INNER JOIN technologies ON technologies_to_theaters.technology_id = technologies.id " +
        "WHERE technologies_to_theaters.theater_id = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, theaterId);

    return extractTechnologiesFromRowSet(rs);
  }

  public List<Technology> getRequiredTechnologies(int movieId) {
    String query = "SELECT technologies.id, technologies.name FROM technologies_to_movies " +
        "INNER JOIN technologies ON technologies_to_movies.technology_id = technologies.id " +
        "WHERE technologies_to_movies.movie_id = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, movieId);

    return extractTechnologiesFromRowSet(rs);
  }

  public List<Integer> convertToIdList(List<Technology> technologies) {
    if (technologies != null) {
      List<Integer> result = new ArrayList<>();

      for (Technology technology : technologies) {
        result.add(technology.getId());
      }

      return result;
    } else {
      return null;
    }
  }

  public List<Technology> convertFromIdList(List<Integer> techIds) {
    List<Technology> result = new ArrayList<>();
    for (int id : techIds) {
      result.add(getTechnologyById(id));
    }
    return result;
  }

  private List<Technology> extractTechnologiesFromRowSet(SqlRowSet rs) {
    List<Technology> result = rs.isBeforeFirst() ? new ArrayList<>() : null;
    while (rs.next()) {
      Technology newTech = new Technology();
      newTech.setId(rs.getInt("id"));
      newTech.setName(rs.getString("name"));

      result.add(newTech);
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

  // This method, and the next one, also work as delete methods just by passing a null
  // or even an empty list, as the second parameter
  public void setRequiredTechnologies(int movieId, List<Technology> technologies) {
    // First clear the old associated technologies
    jdbc.update("DELETE FROM technologies_to_movies WHERE movie_id = ?;", movieId);

    // If there are any, set the new ones
    if (technologies != null && !technologies.isEmpty()) {
      StringBuilder query = new StringBuilder();
      query.append("INSERT INTO technologies_to_movies VALUES ");

      for (Technology technology : technologies) {
        query.append("(").append(technology.getId()).append(", ").append(movieId).append("), ");
      }

      // Need to replace the last hanging comma with a semicolon to avoid an SQL error
      query.replace(query.lastIndexOf(","), query.length(), ";");

      jdbc.update(query.toString());
    }
  }

  public void setAvailableTechnologies(int theaterId, List<Technology> technologies) {
    // First clear the old associated technologies
    jdbc.update("DELETE FROM technologies_to_theaters WHERE theater_id = ?;", theaterId);

    // If there are any, set the new ones
    if (technologies != null && !technologies.isEmpty()) {
      StringBuilder query = new StringBuilder();
      query.append("INSERT INTO technologies_to_theaters VALUES ");

      for (Technology technology : technologies) {
        query.append("(").append(technology.getId()).append(", ").append(theaterId).append("), ");
      }

      // Need to replace the last hanging comma with a semicolon to avoid an SQL error
      query.replace(query.lastIndexOf(","), query.length(), ";");

      jdbc.update(query.toString());
    }
  }
}
