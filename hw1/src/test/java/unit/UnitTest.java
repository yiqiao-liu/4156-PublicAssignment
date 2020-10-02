package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import models.GameBoard;
import models.Player;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class UnitTest {

  @Test
  public void testStartGameX() {
    GameBoard board = new GameBoard();
    board.startgame('X');
    Player p1 = board.getP1();
    assertEquals('X', p1.getType());
    assertEquals(1, p1.getId());
    assertEquals(false, board.getGameStarted());
  }

  @Test
  public void testStartGameO() {
    GameBoard board = new GameBoard();
    board.startgame('O');
    Player p1 = board.getP1();
    assertEquals('O', p1.getType());
    assertEquals(1, p1.getId());
    assertEquals(false, board.getGameStarted());
  }

  @Test
  public void testJoinGame() {
    GameBoard board = new GameBoard();
    board.startgame('O');
    board.joingame();
    Player p2 = board.getP2();
    assertEquals(2, p2.getId());
    assertEquals(true, board.getGameStarted());
    assertEquals(1, board.getTurn());
  }

  @Test
  public void testSetter() {
    GameBoard board = new GameBoard();
    board.startgame('X');
    board.joingame();
    board.setter(1, 0, 0);
    assertEquals(2, board.getTurn());
    assertEquals(false, board.setter(2, 0, 0));
    assertEquals(false, board.setter(1, 1, 1));
    assertEquals(true, board.setter(2, 1, 1));
  }

  @Test
  public void testCheckWinner_Row() {
    GameBoard board = new GameBoard();
    board.startgame('X');
    board.joingame();
    board.setter(1, 0, 0);
    board.setter(2, 1, 1);
    board.setter(1, 0, 1);
    board.setter(2, 1, 2);
    board.setter(1, 0, 2);
    assertEquals(true, board.checkWinner(0, 2));
  }
  
  @Test
  public void testCheckWinner_Col() {
    GameBoard board = new GameBoard();
    board.startgame('O');
    board.joingame();
    board.setter(1, 0, 0);
    board.setter(2, 1, 1);
    board.setter(1, 1, 0);
    board.setter(2, 1, 2);
    board.setter(1, 2, 0);
    assertEquals(true, board.checkWinner(2, 0));
  }
  
  @Test
  public void testCheckWinner_LeftDiagonal() {
    GameBoard board = new GameBoard();
    board.startgame('O');
    board.joingame();
    board.setter(1, 0, 0);
    board.setter(2, 0, 1);
    board.setter(1, 1, 1);
    board.setter(2, 1, 2);
    board.setter(1, 2, 2);
    assertEquals(true, board.checkWinner(2, 2));
  } 
  
  @Test
  public void testCheckWinner_RightDiagonal() {
    GameBoard board = new GameBoard();
    board.startgame('O');
    board.joingame();
    board.setter(1, 0, 2);
    board.setter(2, 0, 1);
    board.setter(1, 1, 1);
    board.setter(2, 1, 2);
    board.setter(1, 2, 0);
    assertEquals(true, board.checkWinner(2, 0));
  } 

  @Test
  public void testCheckDraw() {
    GameBoard board = new GameBoard();
    board.startgame('X');
    board.joingame();
    board.setter(1, 1, 1);
    board.setter(2, 0, 0);
    board.setter(1, 2, 1);
    board.setter(2, 0, 1);
    board.setter(1, 0, 2);
    board.setter(2, 2, 0);
    board.setter(1, 1, 0);
    board.setter(2, 1, 2);
    board.setter(1, 2, 2);
    assertEquals(true, board.checkDraw());
  }
  
}