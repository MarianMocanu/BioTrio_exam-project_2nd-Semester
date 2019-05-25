package dk.kea.stud.biotrio.cinema;

import dk.kea.stud.biotrio.AppGlobals;

import java.time.LocalDate;
import java.util.List;

/**
 * The representation of a movie within the system
 */
public class Movie {
  /**
   * An integer representing the movie's id within the database
   */
  private int id;
  /**
   * A {@link String} representation of the movie's title
   */
  private String title;
  /**
   * An integer representation of the movie's runtime length in minutes
   */
  private int runtime;
  /**
   * A {@link String} representation of the movie's synopsis (plot summary)
   */
  private String synopsis;
  /**
   * A {@link String} representation of the movie's genre
   */
  private String genre;
  /**
   * A {@link String} representation of the movie's audio language
   */
  private String language;
  /**
   * A {@link String} representation of the movie's subtitle language
   */
  private String subtitles;
  /**
   * A list of {@link Technology} objects representing the movie's required
   * technologies that a theater must support in order to successfully screen it
   */
  private List<Technology> requiredTechnologies;
  /**
   * A {@link String} representation of the YouTube id of a trailer for the movie
   */
  private String trailerLink;
  /**
   * A {@link LocalDate} representation of the movie's initial release date
   */
  private LocalDate releaseDate;
  /**
   * A {@link String} representation of the movie's main cast
   */
  private String cast;
  /**
   * A {@link String} representation of the movie's director
   */
  private String director;
  /**
   * A {@link String} representation of the movie's rating
   */
  private String ageRestriction;
  /**
   * A {@link String} representation of a web link to a poster for the movie
   */
  private String poster;

  /**
   * @return An integer representing the {@link Movie}'s id within the database
   */
  public int getId() {
    return id;
  }

  /**
   * @param id An integer to set the {@link Movie}'s id to
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return A {@link String} representing the {@link Movie}'s title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title A {@link String} to set the {@link Movie}'s title to
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return An integer representation of the {@link Movie}'s runtime
   */
  public int getRuntime() {
    return runtime;
  }

  /**
   * @param runtime An integer to set the {@link Movie}'s runtime to
   */
  public void setRuntime(int runtime) {
    this.runtime = runtime;
  }

  /**
   * @return A {@link String} representing the {@link Movie}'s synopsis
   */
  public String getSynopsis() {
    return synopsis;
  }

  /**
   * @param synopsis A {@link String} to set the {@link Movie}'s synopsis to
   */
  public void setSynopsis(String synopsis) {
    this.synopsis = synopsis;
  }

  /**
   * @return A {@link String} representing the {@link Movie}'s genre
   */
  public String getGenre() {
    return genre;
  }

  /**
   * @param genre A {@link String} to set the {@link Movie}'s genre to
   */
  public void setGenre(String genre) {
    this.genre = genre;
  }

  /**
   * @return A {@link String} representing the {@link Movie}'s audio language
   */
  public String getLanguage() {
    return language;
  }

  /**
   * @param language A {@link String} to set the {@link Movie}'s audio language to
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * @return A {@link String} representing the {@link Movie}'s subtitle language
   */
  public String getSubtitles() {
    return subtitles;
  }

  /**
   * @param subtitles A {@link String} to set the {@link Movie}'s subtitle language to
   */
  public void setSubtitles(String subtitles) {
    this.subtitles = subtitles;
  }

  /**
   * @return A list of {@link Technology} objects representing
   * the {@link Movie}'s required technologies
   */
  public List<Technology> getRequiredTechnologies() {
    return requiredTechnologies;
  }

  /**
   * @param requiredTechnologies A list of {@link Technology} objects to set
   *                            the {@link Movie}'s required technologies to
   */
  public void setRequiredTechnologies(List<Technology> requiredTechnologies) {
    this.requiredTechnologies = requiredTechnologies;
  }

  /**
   * @return A {@link String} representing the {@link Movie}'s YouTube trailer id
   */
  public String getTrailerLink() {
    return trailerLink;
  }

  /**
   * @param trailerLink A {@link String} to set the {@link Movie}'s YouTube trailer id to
   */
  public void setTrailerLink(String trailerLink) {
    this.trailerLink = trailerLink;
  }

  /**
   * @return A {@link LocalDate} object representing the {@link Movie}'s release date
   */
  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  /**
   * @return A {@link String} representing the {@link Movie}'s formatted release date.
   * Meant for ease of use within Thymeleaf templates
   */
  public String getFormattedReleaseDate() {
    return releaseDate == null ? null : releaseDate.format(AppGlobals.DateFormat);
  }

  /**
   * @param releaseDate A {@link LocalDate} object to set the {@link Movie}'s release date to
   */
  public void setReleaseDate(LocalDate releaseDate) {
    this.releaseDate = releaseDate;
  }

  /**
   * @return A {@link String} representing the {@link Movie}'s main cast
   */
  public String getCast() {
    return cast;
  }

  /**
   * @param cast A {@link String} to set the {@link Movie}'s main cast to
   */
  public void setCast(String cast) {
    this.cast = cast;
  }

  /**
   * @return A {@link String} representing the {@link Movie}'s director
   */
  public String getDirector() {
    return director;
  }

  /**
   * @param director A {@link String} to set the {@link Movie}'s director to
   */
  public void setDirector(String director) {
    this.director = director;
  }

  /**
   * @return A {@link String} representing the {@link Movie}'s rating
   */
  public String getAgeRestriction() {
    return ageRestriction;
  }

  /**
   * @param ageRestriction A {@link String} to set the {@link Movie}'s rating to
   */
  public void setAgeRestriction(String ageRestriction) {
    this.ageRestriction = ageRestriction;
  }

  /**
   * @return A {@link String} representing a web link to a poster for the {@link Movie}
   */
  public String getPoster() {
    return poster;
  }

  /**
   * @param poster A {@link String} to set the web link to a poster for the {@link Movie}
   */
  public void setPoster(String poster) {
    this.poster = poster;
  }
}
