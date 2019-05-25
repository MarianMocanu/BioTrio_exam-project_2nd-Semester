package dk.kea.stud.biotrio.security;

import dk.kea.stud.biotrio.administration.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class that is responsible with managing {@link User} data within the database
 */
@Repository
public class UserRepository {
  @Autowired
  private JdbcTemplate jdbc;
  @Autowired
  private EmployeeRepository employeeRepo;

  /**
   * Find a particular set of user credentials based on a username.
   * Particularly useful in our {@link CustomAuthentication} class
   *
   * @param name The string representation of the username
   * @return A {@link User} object if found, null otherwise
   */
  public User findByUsername(String name) {
    User result = null;
    String query = "SELECT users.id, users.username, users.password, " +
        "users.employee_id, roles.name as role " +
        "FROM users INNER JOIN roles ON users.role = roles.id " +
        "WHERE username = ?;";
    SqlRowSet rs = jdbc.queryForRowSet(query, name);

    if (rs.first()) {
      result = extractUserFromRowSet(rs);
    }

    return result;
  }

  /**
   * Helper method to extract the {@link User} object the {@link SqlRowSet}
   * is currently pointing to
   *
   * @param rs The {@link SqlRowSet} containing the data
   * @return A {@link User} object
   */
  private User extractUserFromRowSet(SqlRowSet rs) {
    User result = new User();
    result.setId(rs.getInt("id"));
    result.setUsername(rs.getString("username"));
    result.setPassword(rs.getString("password"));
    result.setRole(rs.getString("role"));
    result.setEmployee(employeeRepo.findEmployee(rs.getInt("employee_id")));
    return result;
  }

  /**
   * Get all the user credentials from the database
   *
   * @return A list of {@link User} objects, or null if there are
   * no credentials stored in the database
   */
  public List<User> getAllUsers() {
    List<User> result = new ArrayList<>();
    String query = "SELECT users.id, users.username, users.employee_id, roles.name as role " +
        "FROM users INNER JOIN roles ON users.role = roles.id";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    while (rs.next()) {
      result.add(extractUserFromRowSet(rs));
    }

    return result;
  }

  /**
   * Get the list of all possible user roles within the system
   *
   * @return A list of {@link String} objects that represent the user roles
   */
  public List<String> getAllRoles() {
    List<String> result = new ArrayList<>();
    String query = "SELECT * FROM roles";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    while (rs.next()) {
      result.add(rs.getString("name"));
    }

    return result;
  }

  /**
   * Find the id within the database of a particular user role
   *
   * @param roleName The {@link String} representation of a user role
   * @return An integer representing the id. Returns -1 if not found
   */
  public int getRoleId(String roleName) {
    int result = -1;
    String query = "SELECT id FROM roles WHERE name = ?;";
    SqlRowSet rs = jdbc.queryForRowSet(query, roleName);

    if (rs.first()) {
      result = rs.getInt(1);
    }

    return result;
  }

  /**
   * Gets a set of {@link User} credentials based on a user id
   *
   * @param id An integer representing the user id
   * @return A {@link User} object if found, null otherwise
   */
  public User findById(int id) {
    User result = null;
    String query = "SELECT users.id, users.username, " +
        "users.employee_id, roles.name as role " +
        "FROM users INNER JOIN roles ON users.role = roles.id " +
        "WHERE users.id = ?;";
    SqlRowSet rs = jdbc.queryForRowSet(query, id);

    if (rs.first()) {
      result = extractUserFromRowSet(rs);
    }

    return result;
  }

  /**
   * Insert a new set of {@link User} credentials into the database
   *
   * @param userData A {@link User} object containing the data
   */
  public void addUser(User userData) {
    int roleId = getRoleId(userData.getRole());
    String query = "INSERT INTO users (username, password, role, employee_id) VALUES (?, ?, ?, ?);";
    jdbc.update(query, userData.getUsername(), userData.getPassword(),
        roleId, userData.getEmployee().getId());
  }

  /**
   * Delete a set of {@link User} credentials from the databased based on a user id
   *
   * @param id An integer representing that user's id which is to be deleted
   */
  public void deleteUser(int id) {
    String query = "DELETE FROM users WHERE id = ?;";
    jdbc.update(query, id);
  }

  /**
   * Update a set of {@link User} credentials in the database
   *
   * @param user A {@link User} object containing the updated data
   */
  public void editUser(User user) {
    // Find the role's id within the database; to be used for the foreign key
    int roleId = getRoleId(user.getRole());

    if (user.getPassword() == null) {
      // If the password attribute is null, update the rest of the information
      // without modifying the password
      jdbc.update("UPDATE users SET username = ?, role = ?, employee_id = ? WHERE id = ?",
          user.getUsername(), roleId, user.getEmployee() == null ?
              null : user.getEmployee().getId(), user.getId());
    } else {
      // If there is a password set, update that as well
      jdbc.update("UPDATE users SET username = ?, password = ?, role = ?, employee_id = ?" +
              " WHERE id = ?",
          user.getUsername(), user.getPassword(), roleId, user.getEmployee() == null ?
              null : user.getEmployee().getId(), user.getId());
    }
  }
}