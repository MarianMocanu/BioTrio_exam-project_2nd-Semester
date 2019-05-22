package dk.kea.stud.biotrio.cinema;

public class Technology {
  private int id;
  private String name;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean equals(Technology other) {
    return this.id == other.getId();
  }
}
