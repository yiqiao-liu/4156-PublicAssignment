package models;

public class Player {

  private char type;

  private int id;
  
  public void startgame(char x) {
    type = x;
    id = 1;
  }
  
  public void joingame(char x) {
    type = x;
    id = 2;
  }
  
  /* below are getters ------------------------------- */
  
  public char getType() {
    return type;
  }
  
  public int getId() {
    return id;
  }

}
