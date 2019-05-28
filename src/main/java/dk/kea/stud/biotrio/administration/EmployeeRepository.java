package dk.kea.stud.biotrio.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class that is responsible with managing {@link Employee} data within the database
 */
@Repository
public class EmployeeRepository {
  @Autowired
  private JdbcTemplate jdbc;

  /**
   * Finds a particular employee in the database based on an id
   *
   * @param id The integer id to look up in the database
   * @return An {@link Employee} object if found, null otherwise
   */
  public Employee findEmployee(int id) {
    SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM employees WHERE id = ?", id);
    Employee employee = null;

    // The query always returns either one or no results
    if (rs.first()) {
      employee = extractNextEmployeeFromRowSet(rs);
    }
    return employee;
  }

  /**
   * Gets all the employee records
   *
   * @return A list of {@link Employee} objects, or null if there are no records in the database
   */
  public List<Employee> findAllEmployees() {
    List<Employee> employees = new ArrayList();

    String query = "SELECT * FROM employees";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    while (rs.next()) {
      employees.add(extractNextEmployeeFromRowSet(rs));
    }
    return employees;
  }

  /**
   * Gets all employee records which don't have an associated user account
   *
   * @return A list of {@link Employee} objects, or null if none are found
   */
  public List<Employee> findAllEmployeesWithoutAccount() {
    List<Employee> employees = new ArrayList();

    String query = "SELECT * FROM employees WHERE id NOT IN (SELECT employee_id FROM users WHERE employee_id IS NOT NULL);";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    while (rs.next()) {
      employees.add(extractNextEmployeeFromRowSet(rs));
    }
    return employees;
  }

  /**
   * Helper method that returns the {@link Employee}
   * object that the RowSet is currently pointing to
   *
   * @param rs The {@link SqlRowSet} containing the data
   * @return An {@link Employee} object
   */
  private Employee extractNextEmployeeFromRowSet(SqlRowSet rs) {
    Employee result = new Employee();
    result.setId(rs.getInt("id"));
    result.setFirstName(rs.getString("first_name"));
    result.setLastName(rs.getString("last_name"));
    return result;
  }

  /**
   * Saves the data of an {@link Employee} object to the database as a new entry
   *
   * @param employee The {@link Employee} object to save in the database
   */
  public void insert(Employee employee) {
    jdbc.update("INSERT INTO employees(first_name, last_name) VALUES(?,?)",
        employee.getFirstName(), employee.getLastName());
  }

  /**
   * Updates an existing record in the database with the data of an {@link Employee} object
   *
   * @param employee An {@link Employee} object to update the database with
   */
  public void update(Employee employee) {

    jdbc.update("UPDATE employees SET first_name = ?, last_name = ? WHERE id = ?;",
        employee.getFirstName(), employee.getLastName(), employee.getId());
  }

  /**
   * Deletes an employee record from the database
   *
   * @param id An integer id by which to identify the record within the database
   */
  public void deleteEmployee(int id) {
    jdbc.update("DELETE FROM employees WHERE id = ?", id);
  }
}
