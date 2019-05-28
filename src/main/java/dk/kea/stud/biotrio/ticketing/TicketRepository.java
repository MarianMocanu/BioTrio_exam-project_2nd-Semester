package dk.kea.stud.biotrio.ticketing;

import dk.kea.stud.biotrio.cinema.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class that is responsible with managing {@link Ticket} data within the database
 */
@Repository
public class TicketRepository {

  @Autowired
  private JdbcTemplate jdbc;
  @Autowired
  private ScreeningRepository screeningRepo;

  /**
   * Gets all tickets for a particular screening from the database based on a screening's id
   *
   * @param id The integer id to look up in the database
   * @return An {@link ArrayList} of {@link Ticket} objects if found,
   * otherwise null
   */
  public List<Ticket> findTicketsForScreening(int id) {

    String query = "SELECT * FROM tickets WHERE screening_id = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, id);

    List<Ticket> screeningTickets = rs.isBeforeFirst() ? new ArrayList<>() : null;
    while (rs.next()) {
      Ticket ticket = new Ticket();
      ticket.setId(rs.getInt("id"));
      ticket.setScreening(screeningRepo.findById(rs.getInt("screening_id")));

      //Initializing Ticket's Seat object
      Seat seat = new Seat();
      //Setting Seat's attributes
      seat.setRowNo(rs.getInt("row_no"));
      seat.setSeatNo(rs.getInt("seat_no"));
      seat.setAvailable(false);
      seat.setSold(true);
      //Setting Seat object of a Ticket
      ticket.setSeat(seat);

      screeningTickets.add(ticket);
    }

    return screeningTickets;
  }

  /**
   * Saves the data of a {@link Ticket} object to the database as a new entry
   *
   * @param ticket The {@link Ticket} object containing the data
   * @return The updated {@link Ticket} object also containing the id that
   *         was just generated for the newly inserted entry
   */
  public Ticket addTicket(Ticket ticket) {
    PreparedStatementCreator psc = new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO tickets (screening_id, row_no, seat_no) " +
                "VALUES (?, ?, ?)", new String[]{"id"}
        );
        ps.setInt(1, ticket.getScreening().getId());
        ps.setInt(2, ticket.getSeat().getRowNo());
        ps.setInt(3, ticket.getSeat().getSeatNo());
        return ps;
      }
    };

    KeyHolder key = new GeneratedKeyHolder();
    jdbc.update(psc, key);

    ticket.setId(key.getKey().intValue());
    return ticket;
  }

  /**
   * Saves the data of a {@link List} of {@link Ticket} objects to the database as new entries
   *
   * @param tickets The {@link List} of {@link Ticket} objects to be saved
   */
  public void addTickets(List<Ticket> tickets) {
    for (Ticket ticket : tickets) {
      addTicket(ticket);
    }
  }

  /**
   * Deletes a record from the database based on a {@link Ticket} object
   *
   * @param ticket A {@link Ticket} object containing data by which to
   *               identify the record in the database
   */
  public void deleteTicket(Ticket ticket) {
    jdbc.update("DELETE FROM tickets WHERE " +
            "screening_id = ? AND " +
            "row_no = ? AND " +
            "seat_no = ?;",
        ticket.getScreening().getId(),
        ticket.getSeat().getRowNo(),
        ticket.getSeat().getSeatNo());
  }
}
