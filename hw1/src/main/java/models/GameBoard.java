package models;

public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;
  
  /** 
   *  Check if there is a winner, based on the current move. 
   */
  public boolean checkWinner(int row, int col) {
    char type = boardState[row][col];
    // check if a row is the same character
    for (int i = 0; i < 3; i++) {
      if (i == 2 && boardState[row][i] == type) {
        return true;
      }
      if (boardState[row][i] != type) {
        break;
      }
    }
    
    // check if a column is the same character
    for (int i = 0; i < 3; i++) {
      if (i == 2 && boardState[i][col] == type) {
        return true;
      }
      if (boardState[i][col] != type) {
        break;
      }
    }
    
    // check if a diagonal line is the same character when the current move is on the line
    if ((row == 0 && col == 2) || (row == 2 && col == 0) || (row == 1 && col == 1)) {
      if ((boardState[0][2] == type) && (boardState[1][1] == type) && (boardState[2][0] == type)) {
        return true;
      }
    }
  
    if ((row == 0 && col == 0) || (row == 2 && col == 2) || (row == 1 && col == 1)) {
      if ((boardState[0][0] == type) && (boardState[1][1] == type) && (boardState[2][2] == type)) {
        return true;
      }
    }
  
    return false;
  }
  
  /**
   *  Check if the board is a draw.
   */
  public boolean checkDraw() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (boardState[i][j] == '\u0000') {
          return false;
        }
      }
    }
    return true;
  }
  
  /**
   * When the first player picks X or O, startgame function is called to set up the game board.
   * @param   ch          the character (X or O) the first player picks
   */
  public void startgame(char ch) {
    p1 = new Player();
    boardState = new char[3][3];
    
    p1.startgame(ch);
    gameStarted = false;
    turn = 1;
    winner = 0;
    isDraw = false;
  }
  
  /**
   * When the second player join the game, 
   * joingame function is called to set up the rest of the board,
   * which is basically p2.
   */
  public void joingame() {
    char type1 = p1.getType();
    p2 = new Player();
    
    char type2 = ' ';
    if (type1 == 'X') {
      type2 = 'O';
    }
    if (type1 == 'O') {
      type2 = 'X';
    }
    
    p2.joingame(type2);
    gameStarted = true;
  }
  
  /**
   * setter updates the board when a player makes a move.
   * @param   playerid      the id of the player (1 or 2)
   * @param   row           the row number of the move
   * @param   col           the column number of the move
   * @return                whether the move is valid or not
   */
  public boolean setter(int playerid, int row, int col) {
    if (turn != playerid) {
      return false;
    }

    char type = ' ';
    if (playerid == 1) {
      type = p1.getType();
    } else if (playerid == 2) {
      type = p2.getType();
    } 
    
    if (boardState[row][col] == '\u0000') {
      boardState[row][col] = type;
      if (checkWinner(row, col) == true) {
        winner = playerid;
      } else if (checkDraw() == true) {
        isDraw = true;
      } else {
        if (playerid == 1) {
          turn = 2;
        } else if (playerid == 2) {
          turn = 1;
        }
      }
      return true;
    } else {
      return false;
    }
    
  }
  
  /* below are getters ------------------------------- */
  
  public boolean getGameStarted() {
    return gameStarted;
  }
  
  public int getWinner() {
    return winner;
  }
  
  public boolean getIsDraw() {
    return isDraw;
  }
  
  public Player getP1() {
    return p1;
  }
  
  public Player getP2() {
    return p2;
  }

  public int getTurn() {
    return turn;
  }

}

