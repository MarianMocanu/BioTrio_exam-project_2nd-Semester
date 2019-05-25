package dk.kea.stud.biotrio.security;

import dk.kea.stud.biotrio.administration.Employee;

/**
 * Represents user credentials within the system
 */
public class User {
  /**
   * The user's unique id as an integer
   */
  private int id;
  /**
   * A string representation of the user's username
   */
  private String username;
  /**
   * A string representation of the user's password
   */
  private String password;
  /**
   * A string representation of the user's role within the system
   */
  private String role;
  /**
   * The associated {@link Employee} data to a set of user
   * credentials. Can be null.
   */
  private Employee employee;

  /**
   * @return {@link User#id}
   */
  public int getId() {
    return id;
  }

  /**
   * @param id {@link User#id}
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return {@link User#username}
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username {@link User#username}
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return {@link User#password}
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password {@link User#password}
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return {@link User#role}
   */
  public String getRole() {
    return role;
  }

  /**
   * @param role {@link User#role}
   */
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * @return {@link User#employee}
   */
  public Employee getEmployee() {
    return employee;
  }

  /**
   * @param employee {@link User#employee}
   */
  public void setEmployee(Employee employee) {
    this.employee = employee;
  }
}
