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
   * @return An integer representation of the {@link User}'s id within the database
   */
  public int getId() {
    return id;
  }

  /**
   * @param id An integer to set the {@link User}'s id to
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return A {@link String} representation of the {@link User}'s username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username A {@link String} to set the {@link User}'s password to
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return A {@link String} representation of the {@link User}'s password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password A {@link String} to set the {@link User}'s password to
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return A {@link String} representation of the {@link User}'s role
   */
  public String getRole() {
    return role;
  }

  /**
   * @param role A {@link String} to set the {@link User}'s role to
   */
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * @return A {@link Employee} object representing the {@link User}'s associated employee data
   */
  public Employee getEmployee() {
    return employee;
  }

  /**
   * @param employee A {@link Employee} object to set the {@link User}'s
   *                 associated employee data to
   */
  public void setEmployee(Employee employee) {
    this.employee = employee;
  }
}
