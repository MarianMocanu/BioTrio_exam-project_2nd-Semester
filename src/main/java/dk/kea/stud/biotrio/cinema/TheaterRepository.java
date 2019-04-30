package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TheaterRepository {
    @Autowired
    private JdbcTemplate jdbc;

    public Theater findTheater(int id){
        SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM theaters WHERE id = ?",id);
        Theater theater = new Theater();
        while (rs.next()){
            theater.setId(rs.getInt("id"));
            theater.setName(rs.getString("name"));
            theater.setNoOfRows(rs.getInt("no_of_rows"));
            theater.setSeatsPerRow(rs.getInt("seats_per_row"));
        }
        return theater;
    }
    public List<Theater> findAllTheaters(){
        SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM theaters");
        List<Theater> theaterList = new ArrayList<>();
        while (rs.next()){
            Theater theater = new Theater();
            theater.setId(rs.getInt("id"));
            theater.setName(rs.getString("name"));
            theater.setNoOfRows(rs.getInt("no_of_rows"));
            theater.setSeatsPerRow(rs.getInt("seats_per_row"));

            theaterList.add(theater);
        }
        return theaterList;
    }
    public void insert(Theater theater){
        //this works like prepared statement
        jdbc.update("INSERT INTO theaters(name, no_of_rows, seats_per_row) VALUES(?,?,?)",
                theater.getName(),theater.getNoOfRows(), theater.getSeatsPerRow());
    }
    public void update(Theater theater) {

        jdbc.update("UPDATE theaters SET name =? ,no_of_rows =? , seats_per_row =?  WHERE id = ?",
                theater.getName(),theater.getNoOfRows(), theater.getSeatsPerRow(), theater.getId() );
    }
    // do delete--------->
    public void delete(Theater theater){

    }

}
