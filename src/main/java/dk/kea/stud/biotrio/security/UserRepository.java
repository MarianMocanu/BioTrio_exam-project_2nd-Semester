package dk.kea.stud.biotrio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
  @Autowired
  private JdbcTemplate jdbc;

  public User findByUsername(String name) {
    User result = null;
    String query = "SELECT users.id, users.username, users.password, " +
        "users.employee_id, roles.name as role " +
        "FROM users INNER JOIN roles ON users.role = roles.id " +
        "WHERE username = ?;";
    SqlRowSet rs = jdbc.queryForRowSet(query, name);

    if (rs.first()) {
      result = new User();
      result.setId(rs.getInt("id"));
      result.setUsername(rs.getString("username"));
      result.setPassword(rs.getString("password"));
      result.setRole(rs.getString("role"));
      result.setEmployee(null);
    }

    return result;
  }
}