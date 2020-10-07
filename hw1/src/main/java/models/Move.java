package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;
  
  /* below are getters and setters -------------------- */
  
  public Move(Player p, int x, int y) {
    player = p;
    moveX = x;
    moveY = y;
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
