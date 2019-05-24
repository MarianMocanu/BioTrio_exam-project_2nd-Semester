package dk.kea.stud.biotrio.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepository {
  @Autowired
  private JdbcTemplate jdbc;

  // Find employee by ID
  public Employee findEmployee(int id) {
    SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM employees WHERE id = ?", id);
    Employee employee = null;
    //while (rs.next()) - reads all the results and then picks the one with the unique id.
    //since the id is unique we know that we get only one result
    // so we use conditioning to read the first (and only row)
    if (rs.first()) {
      employee = extractNextEmployeeFromRowSet(rs);
    }
    return employee;
  }
  //Get all employees
  public List<Employee> findAllEmployees() {
    List<Employee> employees = new ArrayList();

    String query = "SELECT * FROM employees";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    while (rs.next()) {
      employees.add(extractNextEmployeeFromRowSet(rs));
    }
    return employees;
  }
  //Get all employees that have no user accounts
  public List<Employee> findAllEmployeesWithoutAccount() {
    List<Employee> employees = new ArrayList();

    String query = "SELECT * FROM employees WHERE id NOT IN (SELECT employee_id FROM users WHERE employee_id IS NOT NULL);";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    while (rs.next()) {
      employees.add(extractNextEmployeeFromRowSet(rs));
    }
    return employees;
  }

  private Employee extractNextEmployeeFromRowSet(SqlRowSet rs) {
    Employee result = new Employee();
    result.setId(rs.getInt("id"));
    result.setFirstName(rs.getString("first_name"));
    result.setLastName(rs.getString("last_name"));
    return result;
  }

  //Insert a new employee
  public void insert(Employee employee) {
    //this works like prepared statement
    jdbc.update("INSERT INTO employees(first_name, last_name) VALUES(?,?)",
        employee.getFirstName(), employee.getLastName());
  }
  // Update an employee
  public void update(Employee employee) {

    jdbc.update("UPDATE employees SET first_name =? ,last_name =?  WHERE id = ?",
        employee.getFirstName(), employee.getLastName(), employee.getId());
  }
  //Delete an employee
  public void deleteEmployee(int id) {
    jdbc.update("DELETE FROM employees WHERE id = ?", id);
  }


}
