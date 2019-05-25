package dk.kea.stud.biotrio.cinema;

import java.util.List;

/**
 * The representation of a movie theater within the system
 */
public class Theater {
  /**
   * An integer representing the theater's id within the database
   */
  private int id;
  /**
   * A string representing the theater's name
   */
  private String name;
  /**
   * An integer representing the number of rows in the theater
   */
  private int noOfRows;
  /**
   * An integer representing the number of seats per row
   */
  private int seatsPerRow;
  /**
   * A list of the theater's supported technologies
   */
  private List<Technology> supportedTechnologies;

  /**
   * @return An integer representation of the {@link Theater}'s id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id An integer to set the {@link Theater}'s id to
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return A {@link String} representation of the {@link Theater}'s name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name A {@link String} to set the {@link Theater}'s name to
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return An integer representation of the {@link Theater}'s number of rows
   */
  public int getNoOfRows() {
    return noOfRows;
  }

  /**
   * @param noOfRows An integer to set the {@link Theater}'s number of rows to
   */
  public void setNoOfRows(int noOfRows) {
    this.noOfRows = noOfRows;
  }

  /**
   * @return An integer representation of the {@link Theater}'s number of seats per row
   */
  public int getSeatsPerRow() {
    return seatsPerRow;
  }

  /**
   * @param seatsPerRow An integer to set the {@link Theater}'s number of seats per row to
   */
  public void setSeatsPerRow(int seatsPerRow) {
    this.seatsPerRow = seatsPerRow;
  }

  /**
   * @return An list of {@link Technology} objects representing the
   * {@link Theater}'s supported technologies
   */
  public List<Technology> getSupportedTechnologies() {
    return supportedTechnologies;
  }

  /**
   * @param supportedTechnologies An list of {@link Technology} objects to set
   *                              the {@link Theater}'s supported technologies to
   */
  public void setSupportedTechnologies(List<Technology> supportedTechnologies) {
    this.supportedTechnologies = supportedTechnologies;
  }
}