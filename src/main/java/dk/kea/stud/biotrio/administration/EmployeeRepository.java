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

  public Employee findEmployee(int id) {
    SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM employees WHERE id = ?", id);
    Employee employee = new Employee();
    //while (rs.next()) - reads all the results and then picks the one with the unique id.
    //since the id is unique we know that we get only one result
    // so we use conditioning to read the first (and only row)
    if (rs.first()) {
      employee.setId(rs.getInt("id"));
      employee.setFirstName(rs.getString("first_name"));
      employee.setLastName(rs.getString("last_name"));
    }
    return employee;
  }

  public List<Employee> findAllEmployees() {
    List<Employee> employees = new ArrayList();

    String query = "SELECT * FROM employees";
    SqlRowSet rs = jdbc.queryForRowSet(query);

    while (rs.next()) {
      Employee employee = new Employee();
      employee.setId(rs.getInt("id"));
      employee.setFirstName(rs.getString("first_name"));
      employee.setLastName(rs.getString("last_name"));

      employees.add(employee);
    }
    return employees;
  }

  public void insert(Employee employee) {
    //this works like prepared statement
    jdbc.update("INSERT INTO employees(first_name, last_name) VALUES(?,?)",
        employee.getFirstName(), employee.getLastName());
  }

  public void update(Employee employee) {

    jdbc.update("UPDATE employees SET first_name =? ,last_name =?  WHERE id = ?",
        employee.getFirstName(), employee.getLastName(), employee.getId());
  }

  public void deleteEmployee(int id) {
    jdbc.update("DELETE FROM employees WHERE id = ?", id);
  }


}
