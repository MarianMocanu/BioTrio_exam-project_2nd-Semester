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

@Repository
public class TicketRepository {

  @Autowired
  private JdbcTemplate jdbc;
  @Autowired
  private ScreeningRepository screeningRepo;

  public List<Ticket> findTicketsForScreening(int id) {
    Ticket ticket = null;
    List<Ticket> screeningTickets = new ArrayList<>();

    String query = "SELECT * FROM tickets WHERE screening_id = ?";
    SqlRowSet rs = jdbc.queryForRowSet(query, id);

    while (rs.next()) {
      ticket = new Ticket();
      ticket.setId(rs.getInt("id"));
      ticket.setScreening(screeningRepo.findById(rs.getInt("screening_id")));
      ticket.getSeat().setRowNo(rs.getInt("row_no"));
      ticket.getSeat().setSeatNo(rs.getInt("seat_no"));

      screeningTickets.add(ticket);
    }
    return screeningTickets;
  }

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

  public void updateTicket(Ticket ticket) {
    String query = "UPDATE tickets SET " +
        "screening_id = ?, " +
        "row_no = ?, " +
        "seat_no = ? " +
        "WHERE id = ?;";
    jdbc.update(query,
        ticket.getScreening().getId(),
        ticket.getSeat().getRowNo(),
        ticket.getSeat().getSeatNo(),
        ticket.getId());
  }

  public void deleteTicket(int id) {
    jdbc.update("DELETE FROM tickets WHERE id = ?;", id);
  }

}
