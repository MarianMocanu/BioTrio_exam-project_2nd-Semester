package dk.kea.stud.biotrio.cinema;

import dk.kea.stud.biotrio.AppSettings;

import java.time.LocalDate;
import java.util.List;

public class Movie {
  private int id;
  private String title;
  private int runtime;
  private String synopsis;
  private String genre;
  private String language;
  private String subtitles;
  private List<Technology> requiredTechnologies;
  private String trailerLink;
  private LocalDate releaseDate;
  private String cast;
  private String director;
  private String ageRestriction;
  private String poster;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getRuntime() {
    return runtime;
  }

  public void setRuntime(int runtime) {
    this.runtime = runtime;
  }

  public String getSynopsis() {
    return synopsis;
  }

  public void setSynopsis(String synopsis) {
    this.synopsis = synopsis;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getSubtitles() {
    return subtitles;
  }

  public void setSubtitles(String subtitles) {
    this.subtitles = subtitles;
  }

  public List<Technology> getRequiredTechnologies() {
    return requiredTechnologies;
  }

  public void setRequiredTechnologies(List<Technology> requiredTechnologies) {
    this.requiredTechnologies = requiredTechnologies;
  }

  public String getTrailerLink() {
    return trailerLink;
  }

  public void setTrailerLink(String trailerLink) {
    this.trailerLink = trailerLink;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public String getFormattedReleaseDate() {
    return releaseDate == null ? null : releaseDate.format(AppSettings.DateFormat);
  }

  public void setReleaseDate(LocalDate releaseDate) {
    this.releaseDate = releaseDate;
  }

  public String getCast() {
    return cast;
  }

  public void setCast(String cast) {
    this.cast = cast;
  }

  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }

  public String getAgeRestriction() {
    return ageRestriction;
  }

  public void setAgeRestriction(String ageRestriction) {
    this.ageRestriction = ageRestriction;
  }

  public String getPoster() {
    return poster;
  }

  public void setPoster(String poster) {
    this.poster = poster;
  }

  @Override
  public String toString() {
    return "Movie{" +
        "id=" + id +
        ", title='" + title + '\'' +
        '}';
  }
}
