package dk.kea.stud.biotrio.security;

import dk.kea.stud.biotrio.administration.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
  @Autowired
  private JdbcTemplate jdbc;
  @Autowired
  private EmployeeRepository employeeRepo;

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
      result.setEmployee(employeeRepo.findEmployee(rs.getInt("employee_id")));
    }

    return result;
  }

  public List<User> getAllUsers() {
    List<User> result = new ArrayList<>();
    String query = "SELECT users.id, users.username, users.employee_id, roles.name as role " +
        "FROM users INNER JOIN roles ON users.role = roles.id";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    while (rs.next()) {
      User user = new User();
      user.setId(rs.getInt("id"));
      user.setUsername(rs.getString("username"));
      user.setRole(rs.getString("role"));
      user.setEmployee(employeeRepo.findEmployee(rs.getInt("employee_id")));
      user.setPassword(null);
      result.add(user);
    }

    return result;
  }

  public List<String> getAllRoles() {
    List<String> result = new ArrayList<>();
    String query = "SELECT * FROM roles";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    while (rs.next()) {
      result.add(rs.getString("name"));
    }

    return result;
  }

  public int getRoleId(String roleName) {
    int result = -1;
    String query = "SELECT id FROM roles WHERE name = ?;";
    SqlRowSet rs = jdbc.queryForRowSet(query, roleName);

    if (rs.first()) {
      result = rs.getInt(1);
    }

    return result;
  }

  public User findById(int id) {
    User result = new User();
    String query = "SELECT users.id, users.username, " +
        "users.employee_id, roles.name as role " +
        "FROM users INNER JOIN roles ON users.role = roles.id " +
        "WHERE users.id = ?;";
    SqlRowSet rs = jdbc.queryForRowSet(query, id);

    if (rs.first()) {
      result = new User();
      result.setId(rs.getInt("id"));
      result.setUsername(rs.getString("username"));
      result.setRole(rs.getString("role"));
      result.setEmployee(employeeRepo.findEmployee(rs.getInt("employee_id")));
      result.setPassword(null);
    }

    return result;
  }

  public boolean checkPassword(User user, String password) {
    String query = "SELECT password FROM users WHERE id = ?;";
    SqlRowSet rs = jdbc.queryForRowSet(query, user.getId());

    if (rs.first() && password.equals(rs.getString(1))) {
      return true;
    }

    return false;
  }

  public void addUser(User userData) {
    int roleId = getRoleId(userData.getRole());
    String query = "INSERT INTO users (username, password, role, employee_id) VALUES (?, ?, ?, ?);";
    jdbc.update(query, userData.getUsername(), userData.getPassword(), roleId, userData.getEmployee().getId());
  }

  public void deleteUser(int id) {
    String query = "DELETE FROM users WHERE id = ?;";
    jdbc.update(query, id);
  }

  public void editUser(User user) {
    int roleId = getRoleId(user.getRole());
    if (user.getPassword() == null) {
      jdbc.update("UPDATE users SET username = ?, role = ?, employee_id = ? WHERE id = ?",
          user.getUsername(), roleId, user.getEmployee() == null ?
              null : user.getEmployee().getId(), user.getId());
    } else {
      jdbc.update("UPDATE users SET username = ?, password = ?, role = ?, employee_id = ? WHERE id = ?",
          user.getUsername(), user.getPassword(), roleId, user.getEmployee() == null ?
              null : user.getEmployee().getId(), user.getId());
    }
  }
}