
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
    * This method starts a new game before every test run. It will run every time before a test.
    */
    @BeforeEach
    public void startNewGame() {
    	// Test if server is running. You need to have an endpoint /
        // If you do not wish to have this end point, it is okay to not have anything in this method.
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
        // Remember to use asString() only once for an endpoint call. Every time you call asString(), a new request will be sent to the endpoint. Call it once and then use the data in the object.
        HttpResponse response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
        String responseBody = response.getBody();
        
        // --------------------------- JSONObject Parsing ----------------------------------
        
        System.out.println("Start Game Response: " + responseBody);
        
        // Parse the response to JSON object
        JSONObject jsonObject = new JSONObject(responseBody);

        // Check if game started after player 1 joins: Game should not start at this point
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
