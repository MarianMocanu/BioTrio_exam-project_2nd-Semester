# Check how many associated screenings does a movie have

# SELECT COUNT(*) FROM screenings WHERE movie_id = ?;

# Examples:
SELECT COUNT(*) FROM screenings WHERE movie_id = (SELECT id FROM movies WHERE title = 'The Matrix');
SELECT COUNT(*) FROM screenings WHERE movie_id = (SELECT id FROM movies WHERE title = 'Fight Club');

# Get all movie information for the movies that have been added to the upcoming movies list

SELECT *
FROM upcoming_movies
    INNER JOIN movies ON upcoming_movies.movie_id = movies.id
ORDER BY upcoming_movies.estimated_date;

# Get all movie information for movies that aren't in the upcoming movies list
# (Used to populate the select box where the user chooses the movie to add to
# the upcoming movies list)

SELECT *
FROM movies
WHERE id NOT IN
      (SELECT movie_id
      FROM upcoming_movies);

# Get all movies that have associated upcoming screenings

SELECT *
FROM movies
WHERE id IN
      (SELECT DISTINCT movie_id
      FROM screenings
      WHERE start_time >= CURDATE());

# Find all past screenings

SELECT *
FROM screenings
WHERE start_time < CURDATE()
ORDER BY start_time;

# Get all future screenings for a particular movie
# SELECT * FROM screenings WHERE start_time >= CURDATE() AND movie_id = ? ORDER BY start_time;
# Examples:

SELECT *
FROM screenings
WHERE start_time >= CURDATE()
  AND movie_id = (SELECT id FROM movies WHERE title='Monty Python and the Holy Grail')
ORDER BY start_time;

# Count the number of booked seats for a particular screening
# SELECT COUNT(*) FROM bookings INNER JOIN booked_seats ON booked_seats.booking_id = bookings.id
# WHERE bookings.screening_id = ?;

# Count the number of tickets sold for a particular screening
# SELECT COUNT(*) FROM tickets WHERE screening_id = ?;

# Get all screening information for those that begin 30 minutes or more in the future
# In the app, the number of minutes is not hardcoded, rather it is taken from a global variable

SELECT *
FROM screenings
WHERE start_time >= ADDTIME(UTC_TIMESTAMP(), TIME('00:30'))
ORDER BY start_time;

# The query used to find screenings that take place in a particular theater inside a time window
# Used to find potential candidates that might conflict with a given screening, which will
# afterwards be checked on the Java side

# SELECT *
# FROM screenings
# WHERE start_time > ? - INTERVAL 8 HOUR
#   AND start_time < ? + INTERVAL 8 HOUR
#   AND theater_id = ?
# ORDER BY start_time;

# Example:

SELECT *
FROM screenings
WHERE start_time > TIMESTAMP('2019-06-29 16:00') - INTERVAL 8 HOUR
  AND start_time < TIMESTAMP('2019-06-29 16:00') + INTERVAL 8 HOUR
  AND theater_id = (SELECT id FROM theaters WHERE name='The Blue Theater')
ORDER BY start_time;

# Query to get the count of bookings and tickets for a screening. This is used to check whether
# a screening has any associated bookings or tickets, if the result is higher than 0

# SELECT COUNT(*)
# FROM
#      (SELECT id
#      FROM bookings
#      WHERE screening_id = ?
#      UNION
#      SELECT id
#      FROM tickets
#      WHERE screening_id = ?) AS x;

