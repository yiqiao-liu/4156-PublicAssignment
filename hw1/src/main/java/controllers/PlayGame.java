package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;
import models.GameBoard;
import models.Message;
import org.eclipse.jetty.websocket.api.Session;

public class PlayGame {

  private static final int PORT_NUMBER = 8080;

  private static Javalin app;

  final static GameBoard board = new GameBoard();

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
      Gson gson = new Gson();
      String json = gson.toJson(board); 
      ctx.result(json);
    });
    
    app.get("/newgame", ctx -> {
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
      board.startgame(type);
      
      Gson gson = new Gson();
      String json = gson.toJson(board); 
      ctx.result(json);
    });
    
    app.get("/joingame", ctx -> {
      board.joingame();
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
      
      /* updates the board and create corresponding message objects */
      Message msg = new Message();
      if (board.setter(playerid, row, col) == true) {
        msg.validSetter();
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
