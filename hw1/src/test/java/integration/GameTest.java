package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import controllers.PlayGame;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.GameBoard;
import models.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class GameTest {

  /**
   * Runs only once before the testing starts.
   */
  @BeforeAll
  public static void init() {
    // Start Server
    PlayGame.main(null);
    System.out.println("Before All");
  }

  /**
   * This method starts a new game before every test run. It will run every time
   * before a test.
   */
  @BeforeEach
  public void startNewGame() {
    // Test if server is running. You need to have an endpoint /
    // If you do not wish to have this end point, it is okay to not have anything in
    // this method.
    HttpResponse response = Unirest.get("http://localhost:8080/").asString();
    int restStatus = response.getStatus();

    System.out.println("Before Each");
  }

  /**
   * This is a test case to evaluate the newgame endpoint.
   */
  @Test
  @Order(1)
  public void newGameTest() {

    // Create HTTP request and get response
    HttpResponse response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();

    // Check assert statement (New Game has started)
    assertEquals(restStatus, 200);
    System.out.println("Test New Game");
  }

  /**
   * This is a test case to evaluate the startgame endpoint.
   */
  @Test
  @Order(2)
  public void startGameTest() {

    // Create a POST request to startgame endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you
    // call asString(), a new request will be sent to the endpoint. Call it once and
    // then use the data in the object.
    HttpResponse response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    String responseBody = response.getBody().toString();

    // --------------------------- JSONObject Parsing
    // ----------------------------------

    System.out.println("Start Game Response: " + responseBody);

    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game started after player 1 joins: Game should not start at this
    // point
    assertEquals(false, jsonObject.get("gameStarted"));

    // ---------------------------- GSON Parsing -------------------------

    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();

    // Check if player type is correct
    assertEquals('X', player1.getType());

    System.out.println("Test Start Game");
  }
  
  /**
   * This is a test case to evaluate the joingame endpoint.
   */
  @Test
  @Order(3)
  public void joinGameTest() {
    HttpResponse response = Unirest.get("http://localhost:8080/joingame").asString();
    String responseBody = response.getBody().toString();
    
    System.out.println("Join Game Response: " + responseBody);
    
    HttpResponse resboard = Unirest.get("http://localhost:8080/getboard").asString();
    String resboardBody = resboard.getBody().toString();
    JSONObject jsonObject = new JSONObject(resboardBody);
    assertEquals(true, jsonObject.get("gameStarted"));
    
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player2 = gameBoard.getP2();
    assertEquals('O', player2.getType());
    int turn = gameBoard.getTurn();
    assertEquals(1, turn);
    
    System.out.println("Test Join Game");
  }
  
  /**
   * This is a test case to evaluate the joingame endpoint.
   */
  @Test
  @Order(4)
  public void testmove_WinnerCase() {
    /* Test that after game has started Player 1 always makes the first move. */
    HttpResponse response0 = Unirest.post("http://localhost:8080/move/2").body("x=0&y=1").asString();
    String response0Body = response0.getBody().toString();
    JSONObject jsonObject0 = new JSONObject(response0Body);   
    assertEquals(300, jsonObject0.get("code"));
    /* First test ends here */
    
    HttpResponse response1 = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    String response1Body = response1.getBody().toString();
    
    System.out.println("Move Response: " + response1Body);
    
    /* Test that a player cannot make two moves in their turn. */
    HttpResponse response2 = Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    String response2Body = response2.getBody().toString();
    JSONObject jsonObject2 = new JSONObject(response2Body);   
    assertEquals(300, jsonObject2.get("code"));
    /* Test ends */
    
    /* Test that a player should be able to win a game. */
    Unirest.post("http://localhost:8080/move/2").body("x=0&y=1").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=0&y=2").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();
    
    HttpResponse resboard = Unirest.get("http://localhost:8080/getboard").asString();
    String resboardBody = resboard.getBody().toString();
    JSONObject jsonObject = new JSONObject(resboardBody);
    assertEquals(1, jsonObject.get("winner"));
    /* Test ends */
    
    System.out.println("Test Winner Case");
    
  }
  
  @Test
  @Order(5)
  public void testmove_DrawCase() {
    Unirest.get("http://localhost:8080/newgame").asString();
    Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=2&y=1").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=0&y=1").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();
    /* cell exhausted */
    
    HttpResponse resboard = Unirest.get("http://localhost:8080/getboard").asString();
    String resboardBody = resboard.getBody().toString();
    JSONObject jsonObject = new JSONObject(resboardBody);
    assertEquals(true, jsonObject.get("isDraw"));
    
    System.out.println("Test Draw Case");
  }

  /**
   * This will run every time after a test has finished.
   */
  @AfterEach
  public void finishGame() {
    System.out.println("After Each");
  }

  /**
   * This method runs only once after all the test cases have been executed.
   */
  @AfterAll
  public static void close() {
    // Stop Server
    PlayGame.stop();
    System.out.println("After All");
  }
}
