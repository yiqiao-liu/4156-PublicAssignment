package models;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;
  
  /**
   * Sets the private variables when a move is valid.
   */
  public void validSetter() {
    moveValidity = true;
    code = 100;
    message = "";
  }
  
  /**
   * Sets the private variables when a move is invalid.
   */
  public void invalidSetter() {
    moveValidity = false;
    code = 300;
    message = "Invalid move!";
  }
  
  /* below are getters ------------------------------- */
  
  public boolean getmoveValidity() {
    return moveValidity;
  }
  
  public int getcode() {
    return code;
  }
  
  public String getmessage() {
    return message;
  }

}
