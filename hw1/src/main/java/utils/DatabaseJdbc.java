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

  /**
   * Create a connection with the database.
   * 
   * @return the connection created
   */
  public Connection createConnection() {
    Connection c = null;

    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:test.db");
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
    System.out.println("Opened database successfully");

    return c;
  }

  /**
   * Create a table containing move data.
   * 
   * @param c connection to the database
   * @return whether the table is created successfully
   */
  public boolean createMoveTable(Connection c) {
    Statement stmt = null;

    try {
      stmt = c.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS MOVEINFO" + " (PLAYER_ID      INT     NOT NULL,"
          + " MOVE_X         INT     NOT NULL, " + " MOVE_Y         INT     NOT NULL)";
      stmt.executeUpdate(sql);
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException e) {
          System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
      }
    }
    System.out.println("Table created successfully");
    return true;
  }

  /**
   * Create a table containing player data.
   * 
   * @param c connection to the database
   * @return whether the table is created successfully
   */
  public boolean createPlayerTable(Connection c) {
    Statement stmt = null;

    try {
      stmt = c.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS PLAYERINFO" + " (PLAYER_ID      INT     NOT NULL,"
          + " TYPE         INT     NOT NULL)";
      stmt.executeUpdate(sql);
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException e) {
          System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
      }
    }
    System.out.println("Table created successfully");
    return true;
  }

  /**
   * Add move data to the table MOVEINFO.
   * 
   * @param c    connection to the database
   * @param move a move object containing its player and coordinates
   * @return whether the data is added successfully
   */
  public boolean addMoveData(Connection c, Move move) {
    Statement stmt = null;

    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      String sql = "INSERT INTO MOVEINFO (PLAYER_ID, MOVE_X, MOVE_Y)" + "VALUES (" 
          + move.getplayer().getId() + ", "
          + move.getmoveX() + ", " + move.getmoveY() + ");";
      stmt.executeUpdate(sql);
      c.commit();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException e) {
          System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
      }
    }
    System.out.println("Records created successfully");
    return true;
  }

  /**
   * Add player data to the table PLAYERINFO.
   * 
   * @param c      connection to the database
   * @param player a player object containing the player's id and type
   * @return whether the data is added successfully
   */
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
      String sql = "INSERT INTO PLAYERINFO (PLAYER_ID, TYPE)" + "VALUES (" 
          + player.getId() + ", " + type + ");";
      stmt.executeUpdate(sql);
      c.commit();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException e) {
          System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
      }
    }
    System.out.println("Records created successfully");
    return true;
  }

  /**
   * Fetch all the move data from the table MOVEINFO.
   * 
   * @param c connection to the database
   * @return a list of arrays that contains the data of all the moves
   */
  public List<int[]> fetchMoveData(Connection c) {
    Statement stmt = null;
    List<int[]> a = new ArrayList<int[]>();
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM MOVEINFO;");
      try {
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
      } finally {
        rs.close();
        c.close();
      }
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return a;
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException e) {
          System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
      }
    }
    System.out.println("Retrieval done successfully");
    return a;
  }

  /**
   * Fetch all the player data from the table PLAYERINFO.
   * 
   * @param c connection to the database
   * @return a list of arrays that contains the data of all the players
   */
  public List<int[]> fetchPlayerData(Connection c) {
    Statement stmt = null;
    List<int[]> a = new ArrayList<int[]>();
    try {
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM PLAYERINFO;");
      try {
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
      } finally {
        rs.close();
        c.close();
      }
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return a;
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException e) {
          System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
      }
    }
    System.out.println("Retrieval done successfully");
    return a;
  }

  /**
   * Delete all the data in the database, including MOVEINFO and PLAYERINFO.
   * 
   * @param c connection to the database
   * @return whether the data are deleted successfully
   */
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
      c.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException e) {
          System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
      }
    }
    System.out.println("Deletion done successfully");
    return true;
  }

}