import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author maingo
 *
 */
public class ValleyBikeSimController {
	ValleyBikeSimView view;
	ValleyBikeSimModel model;
	HashMap<String, Pattern> regex;
	Set<String> validateInModel;

	/**
	 * Constructor for the Valley Bike Simulator Controller.
	 * 
	 * @param view
	 * @param model
	 */
	public ValleyBikeSimController(ValleyBikeSimView view, ValleyBikeSimModel model) {
		this.view = view;
		this.model = model;
		this.regex = new HashMap<>();
		generateRegex();
		String fieldsToValidateInModel[] = { "bikeId", "stationId", "newUsername", "newEmail","newStationId", "newStationName", "newStationAddress","newBikeId","bikeIdInStorage","removeBikeId"};
		// Set demonstration using HashSet Constructor
		validateInModel = new HashSet<>(Arrays.asList(fieldsToValidateInModel));
	}

	/**
	 * @return the view
	 */
	public ValleyBikeSimView getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(ValleyBikeSimView view) {
		this.view = view;
	}

	/**
	 * @return the model
	 */
	public ValleyBikeSimModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(ValleyBikeSimModel model) {
		this.model = model;
	}

	/**
	 * Generate all the regular expressions needed to validate user input.
	 */
	private void generateRegex() {
		//regex.put("email", Pattern.compile("\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\b"));
		regex.put("newPassword", Pattern.compile("^[a-zA-Z0-9]{6,}$")); // password has to be at least 6 characters
		regex.put("riderAddress", Pattern.compile("^([a-zA-Z0-9 .'\\/#-]+)," // address line 1
											+ "([a-zA-Z0-9 \\/#.'-]+,)*" // address line 2 (optional)
											+ "([a-zA-Z .'-]+)," // city 
											+ "([a-zA-Z0-9 .'\\/#-]+)," // state
											+ " *([0-9]{5}) *," // zip code
											+ " *([a-zA-Z .,'-]+)$"));  // country
		regex.put("phoneNumber", Pattern.compile("^[1-9][0-9]{9}$")); // phone number format
		
		regex.put("creditCardNumber", Pattern.compile("^[0-9]{16}$")); // we're not supporting Amex, so all credit card numbers will be 16 digits minimum
		regex.put("billingAddress", Pattern.compile("^([a-zA-Z0-9 .'\\/#-]+)," // address line 1
													+ "([a-zA-Z0-9 \\/#.'-]+,)*" // address line 2 (optional)
													+ "([a-zA-Z .'-]+)," // city
													+ "([a-zA-Z0-9 .'\\/#-]+)," // state
													+ " *([0-9]{5}) *," // zip code
													+ " *([a-zA-Z .,'-]+)$")); // country
		regex.put("creditCardDate", Pattern.compile("^(0[1-9]|1[0-2])\\/([0-9]{2})$")); // expiry date of credit card. Regex source:																							// https://stackoverflow.com/questions/20430391/regular-expression-to-match-credit-card-expiration-date
		regex.put("CVV", Pattern.compile("^[0-9]{3,4}$")); // CVV of credit card. Regex source:
															// https://stackoverflow.com/questions/12011792/regular-expression-matching-a-3-or-4-digit-cvv-of-a-credit-card
		regex.put("fullName", Pattern.compile("^([A-Z][a-zA-Z'-]+) ([A-Z][a-zA-Z'-]+)$")); // first and last name separated by space.
		regex.put("billingName", Pattern.compile("^([A-Z][a-zA-Z'-]+) ([A-Z][a-zA-Z'-]+)$")); // billingName, first name and last name separated by space
		regex.put("capacity",Pattern.compile("^(0*[5-9]|1[0-9]|2[0-7])$")); // capacity is within the range [5-27]
		regex.put("hasKiosk", Pattern.compile("^(0|1)$")); // 0 if there's no kiosk at this station, 1 if there is
		regex.put("fileName", Pattern.compile("^[a-zA-Z0-9-]*\\.csv$"));
	}

	/**
	 * Start the ValleyBike program and load data. User can login, sign up for a
	 * rider account, or exit the system.
	 */
	public void start() {

		// show welcome screen with options to login, signup, or exit program
		view.displayWelcomeScreen();
		String optionSelected = getUserInput("option3");

		switch (optionSelected) {
		case "1":
			signup(); // rider logs in using valid username and password
			break;
		case "2":
			login(); // rider creates new account and purchase a membership
			break;
		case "3":
			exit(); // displays exit message, exit system
			break;
		}
	}

	/**
	 * Prompts for the user's valid login information and logs the user in the
	 * system.
	 */
	public void login() {
		
		// check entire system to see if there are any stolen bikes, and charge users $2000.00
		// isOverdue = true is currently logged in user has stolen a bike, else false
		view.displayLoginScreen();
		String username = getUserInput("username");

		if (username.equals("leave")) {
			start();
			return;
		}

		model.setActiveUser(username);
		boolean userIsAdmin = model.activeUserIsAdmin();
		
		boolean isOverdue = model.checkStolenBikes();
		// if active user is a rider
		if (!userIsAdmin) {
			// check if user has a ride in progress < 24hrs in duration
			boolean rideIsInProgress = model.isRideInProgress(); 

			if (rideIsInProgress) {
				view.remindEndRide(); // display message reminding user to end ride
			}
			if (isOverdue) {
				view.bikeStolen(); // notify user of overdue charge
			}
		}

		view.displayLoginSuccess();
		mainMenu(userIsAdmin); // show admin menu if user is admin, else show rider menu

	}

	/**
	 * Creates a new account and purchase membership for a ValleyBike user. Only
	 * Riders can create an account this way.
	 */
	public void signup() {
		view.displaySignupScreen();

		String newUsername = getUserInput("newUsername");
		String password = getUserInput("newPassword");
		String fullName = getUserInput("fullName");
		String email = getUserInput("newEmail");
		String address = getUserInput("riderAddress").replaceAll(",", "");	
		String phoneNumber = getUserInput("phoneNumber");

		view.displayMembershipOptions();
		String membershipOption = getUserInput("option5");

		// create membership object based on user input
		Membership membership = null;
		MembershipFactory mf = new MembershipFactory();
		switch (membershipOption) {
		case "1":
			membership = mf.getMembership("PayPerRide");
			break;
		case "2":
			membership = mf.getMembership("DayPass");
			break;
		case "3":
			membership = mf.getMembership("Monthly");
			break;
		case "4":
			membership = mf.getMembership("Yearly");
			break;
		case "5":
			membership = mf.getMembership("FoundingMember");
			break;
		}

		// get credit card information, add to user account
		String billingName = getUserInput("billingName");
		String creditCardNumber = getUserInput("creditCardNumber");
		String billingAddress = getUserInput("billingAddress").replaceAll(",", "");
		String creditCardDate = getUserInput("creditCardDate");
		String cvv = getUserInput("CVV");

		PaymentMethod paymentMethod = new PaymentMethod(billingName, creditCardNumber, billingAddress, creditCardDate,
				cvv);
		model.addPaymentMethod(newUsername, paymentMethod);

		Rider rider = new Rider(newUsername, password, fullName, email, phoneNumber, address);

		model.createNewRider(rider,paymentMethod,membership);
		model.setActiveUser(newUsername); // set rider as currently active user
		
		// Charge user's credit card. To simplify process: we assume for now that all
		// credit card payments go through
		view.displayPurchaseMembershipSuccess(membership.getMembershipType(), membership.getBaseRate());
		view.displayAccountCreationSuccess(newUsername);
				
		mainMenu(false); // show rider menu (userIsAdmin = false)
	}

	public void exit() {
		view.displayExit();
		System.exit(0);
	}

	/**
	 * Invokes appropriate menu for the currently active user.
	 * 
	 * @param userIsAdmin true if user is admin, else false
	 */
	public void mainMenu(boolean userIsAdmin) {

		view.displayMainMenu(userIsAdmin);

		String optionSelected;
		if (model.activeUserIsAdmin()) { // Deal with the Admin menu options
			optionSelected = getUserInput("option9");
			switch (optionSelected) {
			case "1": // 1) Add station
				addStation(); 
				break;
			case "2": // 2) Remove station
				removeStation();
				break;
			case "3":// 3) Add bike
				addBike();
				break;
			case "4":// 4) Remove bike
				removeBike();
				break;
			case "5":// 5) Redistribute bikes
				equalizeStations();
				break;
			case "6":// 6) View station list
				displayStationList();
				break;
			case "7":// 7) View daily statistics (previously resolve ride)
				displayDailyStatistics();
				break;
			case "8":// 8) Create support ticket
				//createSupportTicket();
				System.out.println("Feature not yet available, check back soon!");
				break;
			case "9":// 9) Log out
				view.displayLogout();
				model.setActiveUser(null);
				start();
				break;
			}
		} else { // Deal with the Rider menu options
			optionSelected = getUserInput("option10");
			switch (optionSelected) {
			case "1":// 1) View station list
				displayStationList();
				break;
			case "2":// 2) Start ride
				startRide();
				break;
			case "3":// 3) End ride
				endRide();
				break;
			case "4":// 4) Edit profile
				//editProfile();
				System.out.println("Feature not yet available, check back soon!");
				break;
			case "5":// 5) Edit payment method
				editPaymentMethod();
				break;
			case "6":// 6) Edit membership
				editMembership();
				break;
			case "7":// 7) View ride history
				displayRideHistory();
				break;
			case "8":// 8) View transaction history
				displayTransactionHistory();
				break;
			case "9":// 9) Report issue
				//reportIssue();
				System.out.println("Feature not yet available, check back soon!");
				break;
			case "10":// 10) Log out
				view.displayLogout();
				model.setActiveUser(null);
				start();
				break;
			}
		}
		mainMenu(userIsAdmin);
	}

	private void editPaymentMethod() {
		//print out rider's current information, asks user what they want to edit
		view.displayCurrentPaymentMethod(model.getPaymentMethodString());

		String option = getUserInput("option4");	
		switch(option) {
		case "1": //change billing name
			String billingName = getUserInput("billingName");
			model.setBillingName(billingName);
			editPaymentMethod();
			break;
		case "2": //change billing address
			String billingAddress = getUserInput("billingAddress").replaceAll(",", "");
			model.setBillingAddress(billingAddress);
			editPaymentMethod();
			break;
		case "3": //add new card
			String creditCardNumber = getUserInput("creditCardNumber");
			String creditCardDate = getUserInput("creditCardDate");
			String cvv = getUserInput("CVV");
			model.addCard(creditCardNumber, creditCardDate, cvv);
			editPaymentMethod();
			break;
		case "4": //return to main menu
			break;
		}
	}

	/**
	 * displays the transaction history of the active user
	 */
	private void displayTransactionHistory() {
		if (model.activeUserHasTransactions()) {
			//get formatted transaction list from model
			ArrayList<String> formattedTransactionList = model.getTransactionList();
			//display in view
			view.displayTransactionList(formattedTransactionList);
		} else {
			view.displayNoTransactionsMade();
		}
		
	}

	/**
	 * displays the ride history of the active user
	 */
	private void displayRideHistory() {
		// get formatted station list from model
		if (model.activeUserHasRidesCompleted()) {
			ArrayList<String> formattedRideList = model.getRideList();
					
			// display in view
			view.displayRideList(formattedRideList);
		} else {
			view.displayNoRidesMade();
		}
	}

	/**
	 * Prompts user and validates using the name of the thing that we want (e.g.
	 * getUserInput(email))
	 * 
	 * @return user input as a string
	 */
	public String getUserInput(String userInputName) {

		boolean inputIsValid;

		String userInput = view.prompt(userInputName);

		if (userInputName.equals("username")) {
			String password = view.prompt("password").trim();
			String loginInfo = userInput + " " + password;
			inputIsValid = model.isValid("loginInfo", loginInfo);

			while (!inputIsValid) {

				System.out.println("Invalid username and password combination, please try again.");

				userInput = view.prompt(userInputName); // username
				password = view.prompt("password");
				loginInfo = userInput + " " + password;
				inputIsValid = model.isValid("loginInfo", loginInfo);
			}
		} else if (validateInModel.contains(userInputName)) {
			inputIsValid = model.isValid(userInputName, userInput); // test for validity against model data

			while (!inputIsValid) {
				view.displayInvalidInput();
				userInput = view.prompt(userInputName);
				inputIsValid = model.isValid(userInputName, userInput);
			}
			
			// if user input is an address, strip all commas so we could store it in csv data files
			if (userInputName.toLowerCase().contains("address")) {
				userInput = userInput.replaceAll(",", " ");
			}
		}
		// validate the number the user enters to pick a menu option
		else if (userInputName.contains("option")) {
			
			// remove all white spaces
			userInput = userInput.replaceAll(" ", "");
			
			// check whether input is a number
			boolean isValidInt = StringUtils.isNumeric(userInput);
			
			int intUserInput = 0; // variable to store the user's choice
			int range = Integer.parseInt(userInputName.substring(6)); // obtain the valid range of options
			
			// only parse the user input if it is an int
			if (isValidInt) {
				intUserInput = Integer.parseInt(userInput);
			}
			
			// input is valid if it is an integer within the range specified
			inputIsValid = (isValidInt && intUserInput <= range && intUserInput >= 1);

			while (!inputIsValid) {
				System.out.println("Invalid input, please follow the instructions and try again.");
				
				// prompt user for input again and remove all white spaces 
				userInput = view.prompt(userInputName).replaceAll(" ", "");
				
				// check to see if input is a parsable integer
				isValidInt = StringUtils.isNumeric(userInput);
				
				if (isValidInt) {
					intUserInput = Integer.parseInt(userInput);
				}
				
				// input is valid if it is an integer within the range specified
				inputIsValid = (isValidInt && intUserInput <= range && intUserInput >= 1);
			}
			
			// convert user input to a string and return
			userInput = Integer.toString(intUserInput);

		} else if (userInputName.equals("pastDay")) {
			inputIsValid = false;
			while (!inputIsValid) {
				userInput = view.prompt(userInputName);
				try {
					Date df = new SimpleDateFormat("MM-dd-yy").parse(userInput);
					inputIsValid = true;
				} catch (ParseException e) {
					System.out.println("Invalid input, please follow the provided instructions and try again.");
				}
			}
		}
		else {
			Pattern r = regex.get(userInputName);
			Matcher m = r.matcher(userInput);

			inputIsValid = m.find();
			while (!inputIsValid) {
				System.out.println("Invalid input, please follow the provided instructions and try again.");
				userInput = view.prompt(userInputName);
				m = r.matcher(userInput);
				inputIsValid = m.find();
			}
		}
		return userInput;

	}
	
	private void editMembership() {
		//print out the current membership they have
		String currentMembershipName = model.getActiveUserMembershipName();
		System.out.println("currentMembershipName: "+currentMembershipName);
		String[] membershipOptions = {"Pay Per Ride","Day Pass","Monthly","Yearly","Founding Member"};
		int numCurrentMembership = -1;
		for (int i=0; i<5; i++) {
			if (currentMembershipName.equals(membershipOptions[i])) {
				numCurrentMembership = i+1;
			}
		}
		
		view.displayEditMembership(numCurrentMembership);
		int membershipOption = Integer.parseInt(getUserInput("option5"));

		if (membershipOption == numCurrentMembership) {
			view.displayKeepCurrentMembership();
		} else {
			// create membership object based on user input
			Membership membership = null;
			MembershipFactory mf = new MembershipFactory();
			switch (membershipOption) {
			case 1:
				membership = mf.getMembership("PayPerRide");
				break;
			case 2:
				membership = mf.getMembership("DayPass");
				break;
			case 3:
				membership = mf.getMembership("Monthly");
				break;
			case 4:
				membership = mf.getMembership("Yearly");
				break;
			case 5:
				membership = mf.getMembership("FoundingMember");
				break;
			}
			
			model.setMembership(model.getActiveUser().getUsername(), membership); // set rider's membership
			view.displayPurchaseMembershipSuccess(membership.getMembershipType(), membership.getBaseRate());
		}
	}

	/**
	 * Starts a ride. Prompts the user for a valid bike ID number and a valid station ID number.
	 */
	public void startRide() {
		boolean rideIsInProgress = model.isRideInProgress();
		
		// if the user already has a ride in progress, remind them to end ride
		if (rideIsInProgress) {
			view.remindEndRide();
		} else
			try {
				
				// if user's credit card expired, display error message
				if (model.activeUserCreditCardExpired()) {
					view.cardExpired();
					// TODO: subsequent actions: prompt user for new payment method, or select from existing
				}
				else if (model.activeUserStolenBike()) {
					view.bikeStolen();
				}
				else {
					
					int bikeId = Integer.parseInt(getUserInput("bikeId"));
					int stationId = Integer.parseInt(getUserInput("stationId"));
					
					// check if bike is at this station, prompt user until input is valid
					boolean stationHasBike = model.stationHasBike(stationId, bikeId);
					while (!stationHasBike) {
						view.displayBikeNotBelongToStation(stationId, bikeId, model.getBikeListFromStation(stationId));
						bikeId = Integer.parseInt(getUserInput("bikeId"));
						stationId = Integer.parseInt(getUserInput("stationId"));
					}
					
					// Pass info to model to start ride and modify system data
					model.startRide(bikeId, stationId); 
					view.displayRideStart(); // print something like “Enjoy your ride!”
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Error parsing bike ID or station ID.");
			} catch (ParseException e) {
				e.printStackTrace();
				System.out.println("Error parsing creditcard expiration date.");
			}
		mainMenu(model.activeUserIsAdmin()); // back to menu
	}
	
	/**
	 * End a ride. Prompts the user for a valid station ID number.
	 */
	public void endRide() {
		
		// if the user doesn't have any currently active ride, display message and do nothing
		if (!model.isRideInProgress()) {
			view.displayNoActiveRide();
			return;
		}
		int stationId = Integer.parseInt(getUserInput("stationId"));
		
		//Check if dock is full
		boolean dockIsFull = model.isStationDockFull(stationId);
		
		if (dockIsFull) {
			view.displayFullDock();
			//TODO Maybe generate a ticket automatically about this full station?
			return;
		}

		// charge the user for this ride
		BigDecimal chargeAmount = model.endRide(stationId);
		view.chargeUserForRide(chargeAmount);
	}
	
	/**
	 * Asks the user for information to create a new station
	 */
	public void addStation() {
		//Ask the user for station id, name, capacity, # of pedelecs, kiosk?, address (a lot of calls to the view, then verification with calls to the model for each piece of information)
		
		// get new station ID (model will cross-check with database to make sure the station ID is unique)
		int stationId = Integer.parseInt(getUserInput("newStationId"));
		
		// get new station name (model will cross-check with database to make sure the station name is unique)
		String stationName = getUserInput("newStationName");
		String address = getUserInput("newStationAddress");
		int capacity = Integer.parseInt(getUserInput("capacity"));
		boolean hasKiosk = getUserInput("hasKiosk").contentEquals("1");
		
		Station station = model.addStation(stationId,stationName,address,capacity,hasKiosk);
		
		view.displayStationAdded(model.formatStationToString(stationId));
	
	}
		
	/**
	 * Remove a station from the system.
	 */
	public void removeStation() {
		
		if (model.noStationInSys()) {
			view.displayNoStationExistsError();
			return;
		}
		
		// get station ID from user
		int stationId = Integer.parseInt(getUserInput("stationId"));
		
		// remove station from database and move all bikes at this station to storage.
		model.removeStation(stationId);
		
		// notify user of successful removal of station
		view.removeStationSuccess(stationId);
	}
	
	/**
	 * Add a bike. The user could do the following:
	 * - Add new bike to storage
	 * - Add new bike to station
	 * - Add bike from storage to station
	 */
	private void addBike() {
		
		// prompt the user to choose between adding a bike to a station, or to storage.
		view.displayAddNewOrExistingBike();
		String optionSelected = getUserInput("option2");
		
		int bikeId = 0;
		// if user wants to add a new bike
		if (optionSelected.equals("1")) {
			bikeId = Integer.parseInt(getUserInput("newBikeId"));
			model.addNewBikeToStorage(bikeId);
			view.addNewBikeToStorageSuccess(Integer.toString(bikeId));
		}
		
		// if user wants to add an existing bike
		if (optionSelected.equals("2")) {
			// get bike ID from user
			bikeId = Integer.parseInt(getUserInput("bikeIdInStorage"));
		}
		
		// ask user whether they want to add the bike to a station 
		view.displayAddBikeToStationOrNot();
		optionSelected = getUserInput("option2");
		// user wants to add bike to station
		if (optionSelected.equals("1")) {
			// get station ID
			int stationId = Integer.parseInt(getUserInput("stationId"));
			
			// while the specified station is at capacity, prompt user for valid station ID
			boolean atCapacity = model.isStationAtCapacity(stationId);
			while (atCapacity) {
				view.displayStationAtCapacity(stationId);
				stationId = Integer.parseInt(getUserInput("stationId"));
				atCapacity = model.isStationAtCapacity(stationId);
			}
			
			// add the bike from storage to the specified station
			model.addBikeFromStorageToStation(bikeId,stationId);
			
			// display confirmation 
			view.displayAddBikeFromStorageToStationSuccess(bikeId,stationId);
		}		
	}
	
	
	/**
	 * Remove a bike. The user can:
	 * - Remove a bike from a station and move to storage
	 * - Remove a bike from a station and delete it from the system
	 * - Remove a bike from storage and delete it from the system
	 */
	private void removeBike() {
		int bikeId = Integer.parseInt(getUserInput("removeBikeId"));
		
		String status = model.getBikeStatus(bikeId);
		String bikeInStorageMessage = "";
		if (status.equals("working")) { // if bike is docked at a station, move it to storage
			String[] stationData = model.moveBikeFromStationToStorage(bikeId);
			int stationId = Integer.parseInt(stationData[0]);
			String stationName = stationData[1];
			bikeInStorageMessage = "Bike " + bikeId + " has been moved to storage from station " + stationName + " (#" + stationId + ").";
		} else if (status.equals("inStorage")) { // if already in storage, do nothing
			bikeInStorageMessage = "Bike " + bikeId + " is currently in storage.";
		}
		view.displayBikeInStorageMessage(bikeInStorageMessage);
		view.promptRemoveBikeFromSystem();
		String optionSelected = getUserInput("option2");
		
		// if user wants to remove bike from system
		if (optionSelected.equals("1")) {
			model.removeBikeInStorageFromSystem(bikeId);
			view.displayRemoveBikeFromSystemSuccess(bikeId);
		}
		
	}
	
	/**
	 * Displays the full list of stations within the Valley Bike system.
	 */
	public void displayStationList() {
		
		// get formatted station list from model
		ArrayList<String> formattedStationList = model.getStationList();
		
		// display in view
		view.displayStationList(formattedStationList);
	}
	
	/**
	 * Equally divides all the bikes between stations
	 * to avoid stations being under- or over-occupied
	 */
	public void equalizeStations() {
		
		// equalizes stations in model
		model.equalizeStations();
		
		// display confirmation
		view.displayEqualizationCompleted();
	}
	
	/**
	 * Reads a .csv ride data file that contains all the rides for one day of service.
	 * After processing the data, returns statistics for the day.
	 */
	public void displayDailyStatistics() {
		//Ask the admin what day they want statistics for
		String dateString = getUserInput("pastDay");
		String month = "";
		switch(dateString.substring(0,2)) {
		case "01":
			month = "Jan";
			break;
		case "02":
			month = "Feb";
			break;
		case "03":
			month = "Mar";
			break;
		case "04":
			month = "Apr";
			break;
		case "05":
			month = "May";
			break;
		case "06":
			month = "Jun";
			break;
		case "07":
			month = "Jul";
			break;
		case "08":
			month = "Aug";
			break;
		case "09":
			month = "Sep";
			break;
		case "10":
			month = "Oct";
			break;
		case "11":
			month = "Nov";
			break;
		case "12":
			month = "Dec";
			break;
		}
		dateString = month+dateString.substring(2,6)+"20"+dateString.substring(6);
		String filename = "data-files/rides-"+dateString+".csv";
		
		String rideStatistics = model.getRidesStatistics(filename);
		view.displayRideStatistics(rideStatistics);
		
		
		
	}
			
}
