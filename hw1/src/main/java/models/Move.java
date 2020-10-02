package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;
  
  /* below are getters and setters -------------------- */
  
  public void setplayer() {
    player = new Player();
  }
  
  public void setmoveX(int row) {
    moveX = row;
  }
  
  public void setmoveY(int col) {
    moveY = col;
  }
  
  public Player getplayer() {
    return player;
  }
  
  public int getmoveX() {
    return moveX;
  }
  
  public int getmoveY() {
    return moveY;
  }

}
