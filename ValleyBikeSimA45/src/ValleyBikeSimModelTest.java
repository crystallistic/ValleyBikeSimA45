import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValleyBikeSimModelTest {

	ValleyBikeSimModel model = new ValleyBikeSimModel();
	User user = new User("username", "password");
	
	@Test
	//Tests addUserTest method and makes sure username and password 
	//is stored correctly and active user is correct
	void addUserTest() {
		model.addUser(user);
		model.setActiveUser("username");
		assertEquals("username", model.getActiveUser().getUsername());
	}
	
	//Tests if readRidesDataFile works and ride data is accurate
	@Test
	void readRidesDataFileTest() {
		String message = "The ride list contains 20 rides and the average ride time is 36 minutes." + "\n";
		assertEquals(message, model.readRidesDataFile("rides-completed-data.csv")); //Error: this should work
	}
	
	//Tests if startRide method is working correctly
	@Test
	void startRideTest() {
		model.readData();
		model.addUser(user);
		model.setActiveUser("username");
		
		//startRide should add a free dock to the current station
		int start_num_freedocks = model.getStation(21).numFreeDocks;
		model.startRide(830,21);
		assertTrue(model.getStation(21).numFreeDocks == start_num_freedocks+1);
		assertTrue(model.isRideInProgress()); //there is a ride in progress for the active user
		
		//Controller checks to see if bikeID is valid at the current station and catches error for non-numeric input
	}
	
	//Tests if endRide method is working correctly
	@Test
	void endRideTest() {
		model.readData();
		Rider rider = new Rider("username", "password", "myname", "myemail", "8008008000", "1 Chapin Way Northampton MA 01063");
		model.addUser(rider);
		model.setActiveUser("username");
		MembershipFactory membership = new MembershipFactory();
		PaymentMethod payment = new PaymentMethod("myname", "1010101010101010", "1 Chapin Way Northampton MA 01063", "02/24", "100");
		model.addPaymentMethod("username", payment);
		model.setMembership("username", membership.getMembership("DayPass"));
		
		model.startRide(830,33);
		int start_num_freedocks = model.getStation(23).numFreeDocks;
		model.endRide(23);
		assertFalse(model.isRideInProgress()); //Ride should no longer be in progress
		assertTrue(model.getStation(23).numFreeDocks == start_num_freedocks-1); //Number of free docks at the new station should increase by one

		//Controller checks to see if dock is full, stationID does not exist, or a ride is not in progress 		
	}
	
	/* NEW SECTION HERE */
	/* TESTING isValid() method below, which verifies user input */
	
	//isValid should return true if station ID exists in station-data.csv file
	@Test
	void isValidStationID() {
		model.readData(); //reads in station-data.csv
		assertTrue(model.isValid("stationId", "30"));
		assertFalse(model.isValid("stationId", "300"));
		assertFalse(model.isValid("stationId", "string")); //user passes a string
		assertFalse(model.isValid("stationId", "30string")); //user passes a string with an integer)
		assertFalse(model.isValid("stationId", "")); //user passes an empty string
		assertFalse(model.isValid("stationId", " ")); //user passes a space
		assertFalse(model.isValid("stationId", " 30")); //user passes a number with a space before
	}
	
	//isValid should return true if bike ID exists in bike-data.csv
	@Test
	void isValidBikeId() {
		model.readData();
		assertTrue(model.isValid("bikeId", "628"));
		assertFalse(model.isValid("bikeId", "hello")); //user passes a string of characters
		assertFalse(model.isValid("bikeId", "62812"));
		assertFalse (model.isValid("bikeId", " ")); //user passes a space
		assertFalse(model.isValid("bikeId", "")); //user passes an empty string
	}
	
	//isValid should return true if username and password combination exists in rider-data.csv
	@Test
	void isValidLoginInfo() {
		model.readData();
		assertTrue(model.isValid("loginInfo", "username1 password1"));
		assertFalse(model.isValid("loginInfo", "string string"));
		assertFalse(model.isValid("loginInfo", "")); //user passes an empty string
		assertFalse(model.isValid("loginInfo", " ")); //user passes a space
		assertFalse(model.isValid("loginInfo", "username1")); //user does not enter a password
		assertFalse(model.isValid("loginInfo", " password1")); //user does not enter a username
		assertFalse(model.isValid("loginInfo", "010101 100")); //user enters numbers
	}
	
	//isValid should return true if username is a minimum of six characters and doesn't already exist
	@Test
	void isValidNewUsername() {
		model.readData();
		assertTrue(model.isValid("newUsername", "BobSnel"));
		assertTrue(model.isValid("newUsername", "0101010"));
		assertFalse(model.isValid("newUsername", "Bob")); //username is less than six characters
		assertFalse(model.isValid("newUsername", "")); //user passes an empty string
		assertFalse(model.isValid("newUsername", " ")); //user passes a space
		assertFalse(model.isValid("newUsername", "Bob Snel")); //user passes a string with a space in-between
		assertFalse(model.isValid("newUsername", "username1")); //user passes a username that already exists
	}
	
	//isValid should return true if email is in a valid format and it does not belong to an existing user
	@Test
	void isValidNewEmail() {
		model.readData();
		assertTrue(model.isValid("newEmail", "user@server.com"));
		assertFalse(model.isValid("newEmail", "user.com"));
		assertFalse(model.isValid("newEmail", "user@@server.com"));
		assertFalse(model.isValid("newEmail", "user@server"));
		assertFalse(model.isValid("newEmail", "user@server..com"));
		assertFalse(model.isValid("newEmail", "user.com@"));
		assertFalse(model.isValid("newEmail", "@server.com"));
		assertFalse(model.isValid("newEmail", "hpotter@hogwarts.edu")); //user passes an email that already exists in rider-data.csv
	}
	
	//isValid should return true if station ID is 2-digit, only within the 01-99 range, and has not appeared in the system
	@Test
	void isValidNewStationId() {
		model.readData();
		assertTrue(model.isValid("newStationId", "35"));
		assertFalse(model.isValid("newStationId", "21")); //station ID exists in station-data.csv
		assertFalse(model.isValid("newStationId", "100")); //station ID is more than two digits
		assertFalse(model.isValid("newStationId", "00")); //station ID is not within the 01-99 range
		assertFalse(model.isValid("newStationId", " ")); //user passes a space
		assertFalse(model.isValid("newStationId", "")); //user passes an empty string
		assertFalse(model.isValid("newStationId", "string")); //user passes a string of characters
		assertFalse(model.isValid("newStationId", "my name")); //user passes a string with a space in-between
	}
	
	//isValid should return true if new station name does not coincide with existing station names
	@Test
	void isValidNewStationName() {
		model.readData();
		assertTrue(model.isValid("newStationName", "Candy Land"));
		assertTrue(model.isValid("newStationName", "010101")); //user passes a string of numbers
		assertFalse(model.isValid("newStationName", "Northampton High School")); //this exists in station-data.csv
		assertFalse(model.isValid("newStationName", "Northampton High School   ")); //this exists in station-data.csv but with spaces
		assertFalse(model.isValid("newStationName", "northampton high school")); //this exists in station-data.csv
		assertFalse(model.isValid("newStationName", "NoRtHaMpToN HIgH ScHoOl")); //this exists in station-data.csv
		assertFalse(model.isValid("newStationName", "Northampton HighSchool")); //this exists in station-data.csv
		assertFalse(model.isValid("newStationName", " ")); //user passes a space
		assertFalse(model.isValid("newStationName", "")); //user passes an empty string
	}
	
	//isValid should return true if the station address passed does not exist in station-data.csv and follows the correct regex format
	@Test
	void isValidNewStationAddress() {
		assertTrue(model.isValid("newStationAddress", "1 Smith Way Northampton MA 01063"));
		assertFalse(model.isValid("newStationAddress", "10 0101 0101 01010 0101 01010")); //user passes an invalid address
		assertFalse(model.isValid("newStationAddress", "this is an address")); //user passes an invalid address
		assertFalse(model.isValid("newStationAddress", "a town, Unit 7214/, City, MA, 34324, USA")); //user passes a non-existing address in the correct format
		assertFalse(model.isValid("newStationAddress", "a town, fsdfsf, MA, 34324, USA")); //user passes a non-existing address in the correct format
		assertFalse(model.isValid("newStationAddress", "8409 Forest Rd., Benton Harbor, Benton Harbor, MI, 49022, USA")); //user passes a non-existing address in the correct format
		assertFalse(model.isValid("newStationAddress", "11806 Cline Ave,Crown Point,IN,46307,USA")); //user passes a non-existing address in the correct format
		assertFalse(model.isValid("newStationAddress", "42 Wallaby Way, Sydney, New South Wales, 32432, Australia")); //user passes a non-existing address in the correct format
		assertFalse(model.isValid("newStationAddress", " ")); //user passes a space
		assertFalse(model.isValid("newStationAddress", "")); //user passes an empty string
	} 
	
	
}
