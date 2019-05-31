package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class that is responsible with managing {@link Technology} data within the database
 */
@Repository
public class TechnologyRepository {
  @Autowired
  private JdbcTemplate jdbc;

  /**
   * Gets all the technology records from the database
   *
   * @return A list of {@link Technology} objects, or null if there are no records in the database
   */
  public List<Technology> getAllTechnologies() {
    String query = "SELECT * FROM technologies ORDER BY id;";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    return extractTechnologiesFromRowSet(rs);
  }

  /**
   * Finds a particular technology in the database based on an id
   *
   * @param id The integer id to look up in the database
   * @return A {@link Technology} object if found, null otherwise
   */
  public Technology getTechnologyById(int id) {
    String query = "SELECT * FROM technologies WHERE id = ?;";
    SqlRowSet rs = jdbc.queryForRowSet(query, id);

    List<Technology> result = extractTechnologiesFromRowSet(rs);

    // If the ID exists in the database, there will always be exactly one result for the
    // query, otherwise the method is supposed to return null
    return result == null ? null : result.get(0);
  }

  /**
   * Gets a theater's supported technologies
   *
   * @param theaterId The integer id of the theater to look up
   * @return A list of {@link Technology} objects representing the list of
   * technologies supported by the theater
   */
  public List<Technology> getSupportedTechnologies(int theaterId) {
    String query = "SELECT technologies.id, technologies.name FROM technologies_to_theaters " +
        "INNER JOIN technologies ON technologies_to_theaters.technology_id = technologies.id " +
        "WHERE technologies_to_theaters.theater_id = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, theaterId);

    return extractTechnologiesFromRowSet(rs);
  }

  /**
   * Gets a movie's required technologies
   *
   * @param movieId The integer id of the movie to look up
   * @return A list of {@link Technology} objects representing the list of
   * technologies required by the movie
   */
  public List<Technology> getRequiredTechnologies(int movieId) {
    String query = "SELECT technologies.id, technologies.name FROM technologies_to_movies " +
        "INNER JOIN technologies ON technologies_to_movies.technology_id = technologies.id " +
        "WHERE technologies_to_movies.movie_id = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, movieId);

    return extractTechnologiesFromRowSet(rs);
  }

  /**
   * Helper method to convert a list of technologies to a list of integers
   * that represent those technologies' ids from the database
   *
   * @param technologies The list of {@link Technology} objects to convert
   * @return A list of {@link Integer} objects representing the technologies' ids
   */
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

  /**
   * Helper method that converts a list of integers into the list of technologies
   * that are each associated to those integers in the database
   *
   * @param techIds The list of {@link Integer} objects to convert
   * @return A list of {@link Technology} objects representing the data associated with
   * the integer ids
   */
  public List<Technology> convertFromIdList(List<Integer> techIds) {
    if (techIds != null) {
      List<Technology> result = new ArrayList<>();
      for (int id : techIds) {
        result.add(getTechnologyById(id));
      }
      return result;
    } else {
      return null;
    }
  }

  /**
   * Helper method to extract all {@link Technology} objects from a {@link SqlRowSet}
   *
   * @param rs The {@link SqlRowSet} object to parse
   * @return A list of {@link Technology} objects if such data exists, null otherwise
   */
  private List<Technology> extractTechnologiesFromRowSet(SqlRowSet rs) {
    // If there are no results in the rowset initialize it to null,
    // thus skipping the loop and return null from the method
    List<Technology> result = rs.isBeforeFirst() ? new ArrayList<>() : null;
    while (rs.next()) {
      Technology newTech = new Technology();
      newTech.setId(rs.getInt("id"));
      newTech.setName(rs.getString("name"));

      result.add(newTech);
    }

    return result;
  }

  /**
   * Adds a technology entry to the database
   *
   * @param name A {@link String} representation of the technology's name
   */
  public void addTechnology(String name) {
    jdbc.update("INSERT INTO technologies(name) VALUE (?);", name);
  }

  /**
   * Deletes a technology entry from the database based on its id
   *
   * @param id The integer id by which to look up the technology entry
   */
  public void deleteTechnology(int id) {
    //jdbc.update("DELETE FROM technologies_to_movies WHERE technology_id = ?;", id);
    //jdbc.update("DELETE FROM technologies_to_theaters WHERE technology_id = ?;", id);
    jdbc.update("DELETE FROM technologies WHERE id = ?;", id);
  }

  /**
   * Saves the list of a movie's required technologies to the database
   *
   * @param movieId      The integer of a {@link Movie}'s id to update its required technologies
   * @param technologies The list of {@link Technology} objects to associate with the movie
   *                     In case this is null, the movie's required technologies will be deleted
   */
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

      // Need to replace the last hanging comma with a semicolon to avoid an SQL exception
      query.replace(query.lastIndexOf(","), query.length(), ";");

      jdbc.update(query.toString());
    }
  }

  /**
   * Saves the list of a theater's supported technologies to the database
   *
   * @param theaterId    The integer of a {@link Theater}'s id to update its supported technologies
   * @param technologies The list of {@link Technology} objects to associate with the theater
   *                     In case this is null, the theater's supported technologies will be deleted
   */
  public void setSupportedTechnologies(int theaterId, List<Technology> technologies) {
    // First clear the old associated technologies
    jdbc.update("DELETE FROM technologies_to_theaters WHERE theater_id = ?;", theaterId);

    // If there are any, set the new ones
    if (technologies != null && !technologies.isEmpty()) {
      StringBuilder query = new StringBuilder();
      query.append("INSERT INTO technologies_to_theaters VALUES ");

      for (Technology technology : technologies) {
        query.append("(").append(technology.getId()).append(", ").append(theaterId).append("), ");
      }

      // Need to replace the last hanging comma with a semicolon to avoid an SQL exception
      query.replace(query.lastIndexOf(","), query.length(), ";");

      jdbc.update(query.toString());
    }
  }
}
