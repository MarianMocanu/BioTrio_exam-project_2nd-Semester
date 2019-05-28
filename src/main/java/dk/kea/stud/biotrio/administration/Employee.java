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
   * @return An integer representation of the {@link Employee}'s id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id An integer to set the {@link Employee}'s id to
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return A {@link String} representation of the {@link Employee}'s first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName A {@link String} to set the {@link Employee}'s first name to
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return A {@link String} representation of the {@link Employee}'s last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName A {@link String} to set the {@link Employee}'s last name to
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
