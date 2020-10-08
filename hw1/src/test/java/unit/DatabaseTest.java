package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import models.Move;
import models.Player;
import org.junit.jupiter.api.Test;
import utils.DatabaseJdbc;

public class DatabaseTest {
  DatabaseJdbc db = new DatabaseJdbc();

  @Test
  public void testCreateMoveTable() {
    Connection conc = db.createConnection();
    assertEquals(true, db.createMoveTable(conc));
  }

  @Test
  public void testCreatePlayerTable() {
    Connection conc = db.createConnection();
    assertEquals(true, db.createPlayerTable(conc));
  }

  @Test
  public void testAddMoveData() {
    Connection conc = db.createConnection();
    db.createMoveTable(conc);
    Player p = new Player();
    p.startgame('X');
    Move move = new Move(p, 1, 0);
    assertEquals(true, db.addMoveData(conc, move));
  }

  @Test
  public void testAddPlayerDataTypeX() {
    Connection conc = db.createConnection();
    db.createPlayerTable(conc);
    Player p = new Player();
    p.startgame('X');
    assertEquals(true, db.addPlayerData(conc, p));
  }
  
  @Test
  public void testAddPlayerDataTypeO() {
    Connection conc = db.createConnection();
    db.createPlayerTable(conc);
    Player p = new Player();
    p.startgame('O');
    assertEquals(true, db.addPlayerData(conc, p));
  }
  
  @Test
  public void testDeleteAllData() {
    Connection conc = db.createConnection();
    assertEquals(true, db.deleteAllData(conc));
  }

  @Test
  public void testFetchMoveData() {
    int[] arr = new int[3];
    arr[0] = 1;
    arr[1] = 1;
    arr[2] = 0;
    List<int[]> a = new ArrayList<int[]>();
    a.add(arr);
    Connection conc = db.createConnection();
    db.deleteAllData(conc);
    Connection c = db.createConnection();
    testAddMoveData();
    List<int[]> b = db.fetchMoveData(c);
    assertEquals(a.get(0)[0], b.get(0)[0]);
    assertEquals(a.get(0)[1], b.get(0)[1]);
    assertEquals(a.get(0)[2], b.get(0)[2]);
  }
  
  @Test
  public void testFetchPlayerData() {
    int[] arr = new int[2];
    arr[0] = 1;
    arr[1] = 1;
    List<int[]> a = new ArrayList<int[]>();
    a.add(arr);
    Connection conc = db.createConnection();
    db.deleteAllData(conc);
    Connection c = db.createConnection();
    testAddPlayerDataTypeX();
    List<int[]> b = db.fetchPlayerData(c);
    assertEquals(a.get(0)[0], b.get(0)[0]);
    assertEquals(a.get(0)[1], b.get(0)[1]);
  }

}