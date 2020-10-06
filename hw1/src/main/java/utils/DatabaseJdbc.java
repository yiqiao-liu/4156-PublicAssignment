package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import models.Move;
import models.Player;

public class DatabaseJdbc {
  /*
  public static void main(String[] args) {
    DatabaseJdbc db = new DatabaseJdbc();

    Connection conc = db.createConnection();
    boolean create = db.createTable(conc);

    Move move = new Move();

    try {
      conc.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }*/

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

  public boolean createTable(Connection c) {
    Statement stmt = null;

    try {
      stmt = c.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS MOVEINFO " 
                   + "(PLAYER_ID      INT     NOT NULL,"
                   + " MOVE_X         INT     NOT NULL, " 
                   + " MOVE_Y         INT     NOT NULL)";
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

}