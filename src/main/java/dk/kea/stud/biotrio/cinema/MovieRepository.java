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

@Repository
public class MovieRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public List<Movie> findAllMovies() {
        List<Movie> moviesList = new ArrayList<>();

        String query = "SELECT * FROM movies";
        SqlRowSet rs = jdbc.queryForRowSet(query);

        while (rs.next()) {
            moviesList.add(extractNextMovieFromRowSet(rs));
        }
        return moviesList;
    }

    private Movie extractNextMovieFromRowSet(SqlRowSet rs) {
        Movie movie = new Movie();
        movie.setId(rs.getInt("id"));
        movie.setTitle(rs.getString("title"));
        movie.setRuntime(rs.getInt("runtime"));
        movie.setSynopsis(rs.getString("synopsis"));
        movie.setGenre(rs.getString("genre"));
        movie.setLanguage(rs.getString("language"));
        movie.setSubtitles(rs.getString("subtitles"));
        movie.setProjectionType(rs.getString("projection_type"));
        movie.setTrailerLink(rs.getString("trailer_link"));
        Timestamp ts = rs.getTimestamp("release_date");
        movie.setReleaseDate(ts == null ? null : ts.toLocalDateTime().toLocalDate());
        movie.setCast(rs.getString("cast"));
        movie.setDirector(rs.getString("director"));
        movie.setAgeRestriction(rs.getString("age_restriction"));
        movie.setPoster(rs.getString("poster"));

        return movie;
    }

    public Movie findMovieById(int id) {
        Movie movie = null;

        String query = "SELECT * FROM movies WHERE id = ?";
        SqlRowSet rs = jdbc.queryForRowSet(query, id);
        if (rs.first()) {
            movie = extractNextMovieFromRowSet(rs);
        }
        return movie;
    }

    public Movie addMovie(Movie movie) {
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO movies (title, runtime, " +
                        "synopsis, genre, language, subtitles, projection_type, trailer_link, release_date, " +
                        "cast, director, age_restriction, poster) VALUES  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                        new String[]{"id"});
                ps.setString(1, movie.getTitle());
                ps.setInt(2, movie.getRuntime());
                ps.setString(3, movie.getSynopsis());
                ps.setString(4, movie.getGenre());
                ps.setString(5, movie.getLanguage());
                ps.setString(6, movie.getSubtitles());
                ps.setString(7, movie.getProjectionType());
                ps.setString(8, movie.getTrailerLink());
                Date relaseDate = movie.getReleaseDate() == null ? null : Date.valueOf(movie.getReleaseDate());
                ps.setDate(9, relaseDate);
                ps.setString(10, movie.getCast());
                ps.setString(11, movie.getDirector());
                ps.setString(12, movie.getAgeRestriction());
                ps.setString(13, movie.getPoster());
                return ps;
            }
        };

        KeyHolder id = new GeneratedKeyHolder();
        jdbc.update(psc, id);

        movie.setId(id.getKey().intValue());
        return movie;
    }

    public void updateMovie(Movie movie) {
        jdbc.update("UPDATE movies SET " +
                        "title = ?," +
                        "runtime = ?," +
                        "synopsis = ?, " +
                        "genre = ?, " +
                        "language = ?, " +
                        "subtitles = ?," +
                        "projection_type = ?," +
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
                movie.getProjectionType(),
                movie.getTrailerLink(),
                movie.getReleaseDate() == null ? null : Date.valueOf(movie.getReleaseDate()),
                movie.getCast(),
                movie.getDirector(),
                movie.getAgeRestriction(),
                movie.getPoster(),
                movie.getId());
    }

    public void deleteMovie(int id) {
        jdbc.update("DELETE FROM movies WHERE id = ?", id);
    }

    public boolean canDelete(Movie m){
        String query = ("SELECT COUNT(*) FROM screenings WHERE movie_id= "+m.getId());
        SqlRowSet rs = jdbc.queryForRowSet(query);
        rs.first();
        int noScreenings = rs.getInt(1);

        return noScreenings == 0;
    }

    public void addMovieToUpcomingList(Movie movie, LocalDate estimated_date) {
        jdbc.update("INSERT INTO upcoming_movies VALUE (?, ?);", movie.getId(), Date.valueOf(estimated_date));
    }

    public void removeMovieFromUpcomingList(int movieId) {
        jdbc.update("DELETE FROM upcoming_movies WHERE movie_id = ?;", movieId);
    }

    public List<Movie> getUpcomingMovies() {
        SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM upcoming_movies INNER JOIN movies ON upcoming_movies.movie_id = movies.id ORDER BY upcoming_movies.estimated_date;");

        List<Movie> result = rs.isBeforeFirst() ? new ArrayList<>() : null;
        while (rs.next()) {
            result.add(extractNextMovieFromRowSet(rs));
        }

        return result;
    }

    public List<Movie> getMoviesThatArentUpcoming() {
        SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM movies WHERE id NOT IN (SELECT movie_id FROM upcoming_movies);");

        List<Movie> result = rs.isBeforeFirst() ? new ArrayList<>() : null;
        while (rs.next()) {
            result.add(extractNextMovieFromRowSet(rs));
        }

        return result;
    }
}
