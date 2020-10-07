package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Queue;
import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;
import org.eclipse.jetty.websocket.api.Session;
import utils.DatabaseJdbc;

public class PlayGame {

  private static final int PORT_NUMBER = 8080;

  private static Javalin app;
  
  private static DatabaseJdbc db = new DatabaseJdbc();

  //final static GameBoard board = new GameBoard();

  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {

    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    }); 
    
    // Create a new end point to conveniently get the game board
    app.get("/getboard", ctx -> {
      GameBoard board = new GameBoard();
      Connection c = db.createConnection();
      List<int[]> playerdata = db.fetchPlayerData(c);
      if (! playerdata.isEmpty()) {
        for (int i = 0; i < playerdata.size(); i++) {
          if (playerdata.get(i)[0] == 1) {
            char type;
            if (playerdata.get(i)[1] == 1) {
              type = 'X';
            } else {
              type = 'O';
            }
            board.startgame(type);
          } else {
            board.joingame();
          }
        }
        if (playerdata.size() == 2) {
          Connection cc = db.createConnection();
          List<int[]> movedata = db.fetchMoveData(cc);
          for (int i = 0; i < movedata.size(); i++) {
            board.setter(movedata.get(i)[0], movedata.get(i)[1], movedata.get(i)[2]);
          }
        }
      }
      
      Gson gson = new Gson();
      String json = gson.toJson(board); 
      ctx.result(json);
    });
    
    app.get("/newgame", ctx -> {
      Connection c = db.createConnection();
      if (db.deleteAllData(c) == true) {
        Connection cc = db.createConnection();
        db.createPlayerTable(cc);
        db.createMoveTable(cc);
      } else {
        db.createPlayerTable(c);
        db.createMoveTable(c);
      }
      ctx.redirect("/tictactoe.html");
    });
    
    app.post("/startgame", ctx -> {
      
      String body = ctx.body();
      char type = ' ';
      for (int i = 0; i < body.length(); i++) {
        if (body.charAt(i) == 'X') {
          type = 'X';
        }
        if (body.charAt(i) == 'O') {
          type = 'O';
        }
      }
      GameBoard board = new GameBoard();
      board.startgame(type);
      Connection c = db.createConnection();
      db.addPlayerData(c, board.getP1());
      
      Gson gson = new Gson();
      String json = gson.toJson(board); 
      ctx.result(json);
    });
    
    app.get("/joingame", ctx -> {
      GameBoard board = new GameBoard();
      Connection c = db.createConnection();
      List<int[]> playerdata = db.fetchPlayerData(c);
      if (! playerdata.isEmpty()) {
        for (int i = 0; i < playerdata.size(); i++) {
          if (playerdata.get(i)[0] == 1) {
            char type;
            if (playerdata.get(i)[1] == 1) {
              type = 'X';
            } else {
              type = 'O';
            }
            board.startgame(type);
          }
        }
      }
      
      board.joingame();
      Connection cc = db.createConnection();
      db.addPlayerData(cc, board.getP2());
      
      ctx.redirect("/tictactoe.html?p=2");
      
      Gson gson = new Gson();
      String json = gson.toJson(board); 
      ctx.result(json);
      
      sendGameBoardToAllPlayers(json);
    });
    
    app.post("/move/:playerid", ctx -> {
      /* extraction of context param/body */
      String id = ctx.pathParam("playerid");
      int playerid = Integer.parseInt(id);
      
      String body = ctx.body();
      int row = -1;
      int col = -1;
      Boolean x = false;
      Boolean y = false;
      for (int i = 0; i < body.length(); i++) {
        char cur = body.charAt(i);
        if (cur == 'x') {
          x = true;
        }
        if (cur == 'y') {
          y = true;
        }
        if (Character.isDigit(cur) && x == true && y == false) {
          row = Character.getNumericValue(cur);
        }
        if (Character.isDigit(cur) && x == true && y == true) {
          col = Character.getNumericValue(cur);
        }
      }
      /* extraction ends here */
      
      /* add data from database back to board */
      GameBoard board = new GameBoard();
      Connection c = db.createConnection();
      List<int[]> playerdata = db.fetchPlayerData(c);
      if (! playerdata.isEmpty()) {
        for (int i = 0; i < playerdata.size(); i++) {
          if (playerdata.get(i)[0] == 1) {
            char type;
            if (playerdata.get(i)[1] == 1) {
              type = 'X';
            } else {
              type = 'O';
            }
            board.startgame(type);
          } else {
            board.joingame();
          }
        }
        if (playerdata.size() == 2) {
          Connection cc = db.createConnection();
          List<int[]> movedata = db.fetchMoveData(cc);
          for (int i = 0; i < movedata.size(); i++) {
            board.setter(movedata.get(i)[0], movedata.get(i)[1], movedata.get(i)[2]);
          }
        }
      }
      /* ends */
      
      /* updates the board and create corresponding message objects */
      Message msg = new Message();
      if (board.setter(playerid, row, col) == true) {
        msg.validSetter();
        Player p = new Player();
        /* update the database */
        if (playerid == 1) {
          p = board.getP1();
        } else {
          p = board.getP2();
        }
        Move move = new Move(p, row, col);
        Connection cc = db.createConnection();
        db.addMoveData(cc, move);
        /* database update ends here */
      } else {
        msg.invalidSetter();
      }
      Gson gsonmsg = new Gson();
      String strmsg = gsonmsg.toJson(msg); 
      ctx.result(strmsg);
      
      Gson gson = new Gson();
      String json = gson.toJson(board);
      sendGameBoardToAllPlayers(json);    
    });
    
    
    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
