package dk.kea.stud.biotrio.cinema;

/**
 * The representation of a technology that a movie might require,
 * or that a theater supports (e.g. 3D projection, Dolby Atmos, etc)
 */
public class Technology {
  /**
   * An integer representing the technology's id within the database
   */
  private int id;
  /**
   * A {@link String} representing the technology's name in the database
   */
  private String name;

  /**
   * @return An integer representation of the {@link Technology}'s id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id An integer to set the {@link Technology}'s id to
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return An {@link String} representation of the {@link Technology}'s name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name A {@link String} to set the {@link Technology}'s name to
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Check if two {@link Technology} objects point to the same technology in the database
   *
   * @param other The other {@link Technology} object to check against
   * @return true if the two objects have the same id, false otherwise
   */
  public boolean equals(Technology other) {
    return this.id == other.getId();
  }
}
