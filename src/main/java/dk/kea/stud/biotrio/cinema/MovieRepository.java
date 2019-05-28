package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class that is responsible with managing {@link Movie} data withing the database
 */
@Repository
public class MovieRepository {

    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private TechnologyRepository technologyRepo;

    /**
     * Gets all movies from the database
     *
     * @return A list of {@link Movie} objects, or null if the database contains no such data
     */
    public List<Movie> findAllMovies() {
        List<Movie> moviesList = new ArrayList<>();

        String query = "SELECT * FROM movies";
        SqlRowSet rs = jdbc.queryForRowSet(query);

        while (rs.next()) {
            moviesList.add(extractNextMovieFromRowSet(rs));
        }
        return moviesList;
    }

    /**
     * Helper method to extract the {@link Movie} object that
     * the {@link SqlRowSet} is currently pointing to
     *
     * @param rs The {@link SqlRowSet} containing the data
     * @return A {@link Movie} object
     */
    private Movie extractNextMovieFromRowSet(SqlRowSet rs) {
        Movie movie = new Movie();
        movie.setId(rs.getInt("id"));
        movie.setTitle(rs.getString("title"));
        movie.setRuntime(rs.getInt("runtime"));
        movie.setSynopsis(rs.getString("synopsis"));
        movie.setGenre(rs.getString("genre"));
        movie.setLanguage(rs.getString("language"));
        movie.setSubtitles(rs.getString("subtitles"));
        movie.setTrailerLink(rs.getString("trailer_link"));
        Timestamp ts = rs.getTimestamp("release_date");
        movie.setReleaseDate(ts == null ? null : ts.toLocalDateTime().toLocalDate());
        movie.setCast(rs.getString("cast"));
        movie.setDirector(rs.getString("director"));
        movie.setAgeRestriction(rs.getString("age_restriction"));
        movie.setPoster(rs.getString("poster"));
        movie.setRequiredTechnologies(technologyRepo.getRequiredTechnologies(movie.getId()));

        return movie;
    }

    /**
     * Finds a particular {@link Movie} in the database based on the id
     *
     * @param id An integer representing the id to look up in the database
     * @return A {@link Movie} object on success, otherwise null if nothing is found
     */
    public Movie findMovieById(int id) {
        Movie movie = null;

        String query = "SELECT * FROM movies WHERE id = ?";
        SqlRowSet rs = jdbc.queryForRowSet(query, id);
        if (rs.first()) {
            movie = extractNextMovieFromRowSet(rs);
        }
        return movie;
    }

    /**
     * Saves the data of a {@link Movie} object to the database as a new entry
     *
     * @param movie The {@link Movie} object containing the data
     * @return The updated {@link Movie} object also containing the id that
     *         was just generated for the newly inserted entry
     */
    public Movie addMovie(Movie movie) {
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO movies (title, runtime, " +
                        "synopsis, genre, language, subtitles, trailer_link, release_date, " +
                        "cast, director, age_restriction, poster) VALUES  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                        new String[]{"id"});
                ps.setString(1, movie.getTitle());
                ps.setInt(2, movie.getRuntime());
                ps.setString(3, movie.getSynopsis());
                ps.setString(4, movie.getGenre());
                ps.setString(5, movie.getLanguage());
                ps.setString(6, movie.getSubtitles());
                ps.setString(7, movie.getTrailerLink());
                Date relaseDate = movie.getReleaseDate() == null ? null : Date.valueOf(movie.getReleaseDate());
                ps.setDate(8, relaseDate);
                ps.setString(9, movie.getCast());
                ps.setString(10, movie.getDirector());
                ps.setString(11, movie.getAgeRestriction());
                ps.setString(12, movie.getPoster());
                return ps;
            }
        };

        KeyHolder id = new GeneratedKeyHolder();
        jdbc.update(psc, id);

        movie.setId(id.getKey().intValue());

        technologyRepo.setRequiredTechnologies(movie.getId(), movie.getRequiredTechnologies());
        return movie;
    }

    /**
     * Updates an existing record in the database with the data of a {@link Movie} object
     *
     * @param movie A {@link Movie} object to update the database with
     */
    public void updateMovie(Movie movie) {
        jdbc.update("UPDATE movies SET " +
                        "title = ?," +
                        "runtime = ?," +
                        "synopsis = ?, " +
                        "genre = ?, " +
                        "language = ?, " +
                        "subtitles = ?," +
                        "trailer_link = ?," +
                        "release_date = ?," +
                        "cast = ?," +
                        "director = ?," +
                        "age_restriction = ?," +
                        "poster = ? " +
                        "WHERE id= ?",
                movie.getTitle(),
                movie.getRuntime(),
                movie.getSynopsis(),
                movie.getGenre(),
                movie.getLanguage(),
                movie.getSubtitles(),
                movie.getTrailerLink(),
                movie.getReleaseDate() == null ? null : Date.valueOf(movie.getReleaseDate()),
                movie.getCast(),
                movie.getDirector(),
                movie.getAgeRestriction(),
                movie.getPoster(),
                movie.getId());

        technologyRepo.setRequiredTechnologies(movie.getId(), movie.getRequiredTechnologies());
    }

    /**
     * Deletes a record from the database based on an id
     *
     * @param id An integer representing the id by which to
     *           identify the record within the database
     */
    public void deleteMovie(int id) {
        technologyRepo.setRequiredTechnologies(id, null);
        jdbc.update("DELETE FROM movies WHERE id = ?;", id);
    }

    /**
     * Checks whether a movie has any associated screenings
     *
     * @param m A {@link Movie} object to check for associated screenings
     * @return true if no associated screenings are found, false otherwise
     */
    public boolean canDelete(Movie m){
        String query = ("SELECT COUNT(*) FROM screenings WHERE movie_id = ?;");
        SqlRowSet rs = jdbc.queryForRowSet(query, m.getId());
        rs.first();
        int noScreenings = rs.getInt(1);

        return noScreenings == 0;
    }

    /**
     * Add a movie to the upcoming movies list
     *
     * @param movie The {@link Movie} object to add to the list
     * @param estimated_date A {@link LocalDate} object representing the movie's
     *                       estimated screening debut date. Used for sorting purposes
     */
    public void addMovieToUpcomingList(Movie movie, LocalDate estimated_date) {
        jdbc.update("INSERT INTO upcoming_movies VALUE (?, ?);", movie.getId(),
            Date.valueOf(estimated_date));
    }

    /**
     * Remove a movie from the list of upcoming movies
     *
     * @param movieId An integer id by which to identify the entry to be
     *                deleted from the upcoming movies list
     */
    public void removeMovieFromUpcomingList(int movieId) {
        jdbc.update("DELETE FROM upcoming_movies WHERE movie_id = ?;", movieId);
    }

    /**
     * Gets the full list of upcoming movies
     *
     * @return A list of {@link Movie} objects that are in the upcoming movies list
     */
    public List<Movie> getUpcomingMovies() {
        SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM upcoming_movies " +
            "INNER JOIN movies ON upcoming_movies.movie_id = movies.id " +
            "ORDER BY upcoming_movies.estimated_date;");

        List<Movie> result = rs.isBeforeFirst() ? new ArrayList<>() : null;
        while (rs.next()) {
            result.add(extractNextMovieFromRowSet(rs));
        }

        return result;
    }

    /**
     * Gets the list of movies that aren't in the upcoming movies list
     *
     * @return A list of {@link Movie} objects that aren't in the upcoming movies list
     */
    public List<Movie> getMoviesThatArentUpcoming() {
        SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM movies " +
            "WHERE id NOT IN (SELECT movie_id FROM upcoming_movies);");

        List<Movie> result = rs.isBeforeFirst() ? new ArrayList<>() : null;
        while (rs.next()) {
            result.add(extractNextMovieFromRowSet(rs));
        }

        return result;
    }

    /**
     * Gets the list of movies that have upcoming screenings associated, or
     * with other words, that are currently playing in the theaters
     *
     * @return A list of {@link Movie} objects that have upcoming screenings associated
     */
    public List<Movie> getMoviesCurrentlyPlaying() {
        SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM movies WHERE id IN " +
            "(SELECT DISTINCT movie_id FROM screenings WHERE start_time >= CURDATE());");

        List<Movie> result = rs.isBeforeFirst() ? new ArrayList<>() : null;
        while (rs.next()) {
            result.add(extractNextMovieFromRowSet(rs));
        }

        return result;
    }
}
