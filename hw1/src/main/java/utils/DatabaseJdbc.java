package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Move;
import models.Player;

public class DatabaseJdbc {
  
  
  /*public static void main(String[] args) { 
    DatabaseJdbc db = new DatabaseJdbc();
    
    Connection conc = db.createConnection(); 
    db.createMoveTable(conc);
    Player p = new Player();
    p.startgame('X');
    Move move = new Move(p, 1, 0);
    db.addMoveData(conc, move);
    
    boolean create = db.createPlayerTable(conc);
    
    db.addPlayerData(conc, p);
    
    try { conc.close(); } catch (SQLException e) { e.printStackTrace(); } }
  */ 

  public Connection createConnection() {
    Connection c = null;

    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:test.db");
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
    System.out.println("Opened database successfully");

    return c;
  }

  public boolean createMoveTable(Connection c) {
    Statement stmt = null;

    try {
      stmt = c.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS MOVEINFO" + " (PLAYER_ID      INT     NOT NULL,"
          + " MOVE_X         INT     NOT NULL, " + " MOVE_Y         INT     NOT NULL)";
      stmt.executeUpdate(sql);
      stmt.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }
    System.out.println("Table created successfully");
    return true;
  }
  
  public boolean createPlayerTable(Connection c) {
    Statement stmt = null;

    try {
      stmt = c.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS PLAYERINFO" + " (PLAYER_ID      INT     NOT NULL,"
          + " TYPE         INT     NOT NULL)";
      stmt.executeUpdate(sql);
      stmt.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }
    System.out.println("Table created successfully");
    return true;
  }

  public boolean addMoveData(Connection c, Move move) {
    Statement stmt = null;

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      String sql = "INSERT INTO MOVEINFO (PLAYER_ID, MOVE_X, MOVE_Y)" 
          + "VALUES (" + move.getplayer().getId() + ", "
          + move.getmoveX() + ", " + move.getmoveY() + ");";
      stmt.executeUpdate(sql);

      stmt.close();
      c.commit();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }
    System.out.println("Records created successfully");
    return true;
  }
  
  public boolean addPlayerData(Connection c, Player player) {
    Statement stmt = null;
    int type;
    if (player.getType() == 'X') {
      type = 1;
    } else {
      type = 0;
    }

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      String sql = "INSERT INTO PLAYERINFO (PLAYER_ID, TYPE)" 
          + "VALUES (" + player.getId() + ", "
          + type + ");";
      stmt.executeUpdate(sql);

      stmt.close();
      c.commit();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }
    System.out.println("Records created successfully");
    return true;
  }

  public List<int[]> fetchMoveData(Connection c) {
    Statement stmt = null;
    List<int[]> a = new ArrayList<int[]>();
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM MOVEINFO;");

      while (rs.next()) {
        int id = rs.getInt("player_id");
        int row = rs.getInt("move_x");
        int col = rs.getInt("move_y");
        
        int[] arr = new int[3];
        arr[0] = id;
        arr[1] = row;
        arr[2] = col;
        a.add(arr);
        
        System.out.println("PLAYER_ID = " + id);
        System.out.println("MOVE_X = " + row);
        System.out.println("MOVE_Y = " + col);
      }
      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return a;
    }
    System.out.println("Retrieval done successfully");
    return a;
  }
  
  public List<int[]> fetchPlayerData(Connection c) {
    Statement stmt = null;
    List<int[]> a = new ArrayList<int[]>();
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM PLAYERINFO;");
      
      while (rs.next()) {
        int id = rs.getInt("player_id");
        int type = rs.getInt("type");
        int[] arr = new int[2];
        arr[0] = id;
        arr[1] = type;
        a.add(arr);
        
        System.out.println("PLAYER_ID = " + id);
        System.out.println("TYPE = " + type);
      }
      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return a;
    }
    System.out.println("Retrieval done successfully");
    return a;
  }
  
  public boolean deleteAllData(Connection c) {
    Statement stmt = null;
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      String sql1 = "DELETE from MOVEINFO";
      stmt.executeUpdate(sql1);
      String sql2 = "DELETE from PLAYERINFO";
      stmt.executeUpdate(sql2);
      c.commit();

      stmt.close();
      c.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }
    System.out.println("Deletion done successfully");
    return true;
  }

}