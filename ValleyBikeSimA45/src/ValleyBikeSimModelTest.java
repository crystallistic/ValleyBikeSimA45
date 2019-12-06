import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValleyBikeSimModelTest {

	ValleyBikeSimModel model = new ValleyBikeSimModel();
	User user = new User("username", "password");
	
	
	@Test
	/*
	 * Tests addUserTest method and makes sure username and password 
	 * is stored correctly and active user is correct
	*/
	void addUserTest() {
		model.addUser(user);
		model.setActiveUser("username");
		assertEquals("username", model.getActiveUser().getUserName());
	}
	
	/*Tests readRidesDataFile works and ride data is accurate */
	@Test
	void readRidesDataFileTest() {
		String message = "The ride list contains 20 rides and the average ride time is 36 minutes." + "\n";
		assertEquals(message, model.readRidesDataFile("rides-completed-data.csv")); //Error: this should work
	}
	
	
	/* NEW SECTION HERE: TESTING isValid() method */
	
	/*
	 * Tests isValid() to make sure station id is validated correctly and deals with edge cases
	 */
	@Test
	void isValidStationID() {
		model.readData(); //reads in station-data.csv
		assertTrue(model.isValid("stationId", "30")); //station ID exists in station-data.csv file
		assertFalse(model.isValid("stationId", "300"));
		
		boolean thrown = false;
		try {
			model.isValid("stationId", "string"); //Passes a string
		}
		catch(Exception e) {
			thrown = true;
		}
		assertFalse(thrown);
				
		
		thrown = false; //reset boolean
		try {
			model.isValid("stationId", "30string"); //Passes a string with an integer
		}
		catch(Exception e) {
			thrown = true;
		}
		assertFalse(thrown);
		
		
		thrown = false; //reset boolean
		try {
			model.isValid("stationId", ""); //Passes an empty string
		}
		catch(Exception e) {
			thrown = true;
		}
		assertFalse(thrown);
		
		
		thrown = false; //reset boolean
		try {
			model.isValid("stationId", " "); //Passes a space
		}
		catch(Exception e) {
			thrown = true;
		}
		assertFalse(thrown);
		
		
		thrown = false; //reset boolean
		try {
			model.isValid("stationId", " 30"); //Passes a number with a space before
		}
		catch(Exception e) {
			thrown = true;
		}
		assertFalse(thrown);
	}
	
	/*
	 * Tests isValid() to make sure bike id is validated correctly
	 */
	@Test
	void isValidBikeId() {
		model.readData();
		assertTrue(model.isValid("bikeId", "628")); //bike ID exists in bike-data.csv
		
	}
	
	/*
	 * Tests isValid() to make sure email is validated correctly
	 * and deals with edge cases
	 */
	@Test 
	void isValidEmail(){
		assertTrue(model.isValid("newEmail", "user@server.com"));
		assertFalse(model.isValid("newEmail", "user.com"));
		assertFalse(model.isValid("newEmail", "user@@server.com"));
		assertFalse(model.isValid("newEmail", "user@server"));
		assertFalse(model.isValid("newEmail", "user@server..com"));
		assertFalse(model.isValid("newEmail", "user.com@"));
		assertFalse(model.isValid("newEmail", "@server.com"));
	}
	
	/*
	 * Tests isValid() to make sure login information is validated correctly
	 *  and deals with edge cases
	 */
	@Test
	void isValidLoginInfo() {
		model.readData();
		assertTrue(model.isValid("loginInfo", "username1 password1")); //valid username and password stored in rider-data.csv
		assertFalse(model.isValid("loginInfo", "string string"));
		
		boolean thrown = false; //reset boolean
		try {
			model.isValid("loginInfo", "username1"); //password not entered
		}
		catch(Exception e) {
			thrown = true;
		}
		assertFalse(thrown);
		
		thrown = false; //reset boolean
		try {
			model.isValid("loginInfo", " password1"); //username not entered
		}
		catch(Exception e) {
			thrown = true;
		}
		assertFalse(thrown);
		
		thrown = false; //reset boolean
		try {
			model.isValid("loginInfo", "010101 100"); //numbers are entered
		}
		catch(Exception e) {
			thrown = true;
		}
		assertFalse(thrown);
		
	}
	
	/*
	 * Tests isValid() to make sure username input is validated correctly
	 *  and deals with edge cases
	 */
	@Test
	void isValidNewUsername() {
		model.readData();
		assertTrue(model.isValid("newUsername", "BobSnel")); //Usernames must be a minimum of six characters and can't already exist
		assertTrue(model.isValid("newUsername", "0101010"));
		assertFalse(model.isValid("newUsername", "Bob")); //Less than six characters
		assertFalse(model.isValid("newUsername", "")); //empty string
		assertFalse(model.isValid("newUsername", " ")); //space
		assertFalse(model.isValid("newUsername", "Bob Snel")); //username has a space
		assertFalse(model.isValid("newUsername", "username1")); //username already exists
	}
	
	/*
	 * Tests isValid() to make sure new email input is validated correctly
	 *  and deals with edge cases
	 */
	@Test
	void isValidNewEmail() {
		// email is valid if it's in valid format and it does not belong to an existing user
		model.readData();
		assertTrue(model.isValid("newEmail", "user@server.com"));
		assertFalse(model.isValid("newEmail", "user.com"));
		assertFalse(model.isValid("newEmail", "user@@server.com"));
		assertFalse(model.isValid("newEmail", "user@server"));
		assertFalse(model.isValid("newEmail", "user@server..com"));
		assertFalse(model.isValid("newEmail", "user.com@"));
		assertFalse(model.isValid("newEmail", "@server.com"));
		assertFalse(model.isValid("newEmail", "hpotter@hogwarts.edu")); //email already exists in rider-data.csv
		
	}
	
	
}
