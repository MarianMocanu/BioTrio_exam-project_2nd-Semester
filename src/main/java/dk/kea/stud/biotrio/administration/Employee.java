package dk.kea.stud.biotrio.administration;

/**
 * The representation of an individual Bio Trio employee
 */
public class Employee {
  /**
   * An integer representing the employee's id within the database
   */
  private int id;
  /**
   * A string representation of the employee's first name
   */
  private String firstName;
  /**
   * A string representation of the employee's last name
   */
  private String lastName;

  /**
   * {@link Employee#id}
   */
  public int getId() {
    return id;
  }

  /**
   * {@link Employee#id}
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * {@link Employee#firstName}
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * {@link Employee#firstName}
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * {@link Employee#lastName}
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * {@link Employee#lastName}
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
