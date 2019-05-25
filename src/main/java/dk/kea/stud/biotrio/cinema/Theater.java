package dk.kea.stud.biotrio.cinema;

import java.util.List;

public class Theater {
  private int id;
  private String name;
  private int noOfRows;
  private int seatsPerRow;
  private List<Technology> supportedTechnologies;

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

  public int getNoOfRows() {
    return noOfRows;
  }

  public void setNoOfRows(int noOfRows) {
    this.noOfRows = noOfRows;
  }

  public int getSeatsPerRow() {
    return seatsPerRow;
  }

  public void setSeatsPerRow(int seatsPerRow) {
    this.seatsPerRow = seatsPerRow;
  }

  public List<Technology> getSupportedTechnologies() {
    return supportedTechnologies;
  }

  public void setSupportedTechnologies(List<Technology> supportedTechnologies) {
    this.supportedTechnologies = supportedTechnologies;
  }
}