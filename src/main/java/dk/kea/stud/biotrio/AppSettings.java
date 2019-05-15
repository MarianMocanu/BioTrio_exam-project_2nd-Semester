package dk.kea.stud.biotrio;

import java.time.format.DateTimeFormatter;

public class AppSettings {
  public static final int TIME_BUFFER_MINUTES_BETWEEN_SCREENINGS = 15;
  public static final int MAX_NUMBER_OF_SEATS_PER_BOOKING = 4;
  public static final DateTimeFormatter DTFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
}
