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
		String fieldsToValidateInModel[] = { "bikeId", "stationId", "newUsername", "newEmail","newStationId", "newStationName", "newStationAddress", };
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
		regex.put("email", Pattern.compile("\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\b"));
		regex.put("newPassword", Pattern.compile(".{6}.*")); // password has to be at least 6 characters
		regex.put("address", Pattern.compile(".*")); // we assume the user will enter a valid address
		regex.put("phoneNumber", Pattern.compile("^[1-9][0-9]{9}$")); // phone number format

		// credit card format, regex adapted from regular-expressions.com
		// Source: https://www.regular-expressions.info/creditcard.html
		// In order: Visa, Mastercard, American Express, Diners Club, Discover, JCB
		// - Visa: ^4[0-9]{12}(?:[0-9]{3})?$ All Visa card numbers start with a 4.
		// New cards have 16 digits. Old cards have 13.
		// - MasterCard:
		// ^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$
		// MasterCard numbers either start with the numbers 51 through 55 or with the
		// numbers 2221 through 2720.
		// All have 16 digits.
		// - American Express: ^3[47][0-9]{13}$
		// American Express card numbers start with 34 or 37 and have 15 digits.
		// - Diners Club: ^3(?:0[0-5]|[68][0-9])[0-9]{11}$
		// Diners Club card numbers begin with 300 through 305, 36 or 38.
		// All have 14 digits. There are Diners Club cards that begin with 5 and have 16
		// digits.
		// These are a joint venture between Diners Club and MasterCard, and should be
		// processed like a MasterCard.
		// - Discover: ^6(?:011|5[0-9]{2})[0-9]{12}$
		// Discover card numbers begin with 6011 or 65. All have 16 digits.
		// - JCB: ^(?:2131|1800|35\d{3})\d{11}$
		// JCB cards beginning with 2131 or 1800 have 15 digits. JCB cards beginning
		// with 35 have 16 digits.

		/**
		 * regex.put("creditCardNumber", Pattern.compile("^(" +
		 * "?:4[0-9]{12}(?:[0-9]{3})?|" +
		 * "(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}|"
		 * + "3[47][0-9]{13}|" + "3(?:0[0-5]|[68][0-9])[0-9]{11}|" +
		 * "6(?:011|5[0-9]{2})[0-9]{12}|" + "(?:2131|1800|35\\d{3})\\d{11}" + ")$"));
		 * 
		 */

		regex.put("creditCardNumber", Pattern.compile("^[0-9]{16}$"));
		regex.put("billingAddress", Pattern.compile(".*")); // we assume the user will enter a valid address
		regex.put("creditCardDate", Pattern.compile("^(0[1-9]|1[0-2])\\/([0-9]{2})$")); // expiry date of credit card. Regex source:																							// https://stackoverflow.com/questions/20430391/regular-expression-to-match-credit-card-expiration-date
		regex.put("CVV", Pattern.compile("^[0-9]{3,4}$")); // CVV of credit card. Regex source:
															// https://stackoverflow.com/questions/12011792/regular-expression-matching-a-3-or-4-digit-cvv-of-a-credit-card
		regex.put("fullName", Pattern.compile("^[a-zA-Z ,.'-]+$")); // first and last name separated by space. Regex
																	// source:
																	// https://stackoverflow.com/questions/2385701/regular-expression-for-first-and-last-name
		regex.put("billingName", Pattern.compile("^[a-zA-Z ,.'-]+$")); // billingName
		regex.put("capacity",Pattern.compile("^[1-9][0-9]$"));
		regex.put("hasKiosk", Pattern.compile("^(0|1)$"));
		regex.put("fileName", Pattern.compile("^[a-zA-Z0-9-]*\\.csv$"));
	}

	/**
	 * Start the ValleyBike program and load data. User can login, sign up for a
	 * rider account, or exit the system.
	 */
	public void start() {

		// load data in model
		model.readData();

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
		view.displayLoginScreen();
		String username = getUserInput("username");

		if (username.equals("leave")) {
			start();
			return;
		}

		model.setActiveUser(username);
		boolean userIsAdmin = model.activeUserIsAdmin();

		// if active user is a rider
		if (!userIsAdmin) {
			boolean rideIsInProgress = model.isRideInProgress(); // check if user has a ride in progress

			// if the rider has a ride in progress
			if (rideIsInProgress) {
				view.remindEndRide(); // display message reminding user to end ride
				boolean isOverdue = model.bikeIsOverdue(); // check whether bike is overdue

				// if bike is overdue
				if (isOverdue) {
					int amountCharged = model.chargeUser(); // charge user $2000 overdue bike
					view.notifyOverdue(amountCharged); // notify user of charge
				}
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
		String address = getUserInput("address");
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

		// get credit card information
		String billingName = getUserInput("billingName");
		String creditCardNumber = getUserInput("creditCardNumber");
		String billingAddress = getUserInput("billingAddress");
		String creditCardDate = getUserInput("creditCardDate");
		String cvv = getUserInput("CVV");

		PaymentMethod paymentMethod = new PaymentMethod(billingName, creditCardNumber, billingAddress, creditCardDate,
				cvv);

		// Charge user's credit card. To simplify process: we assume for now that all
		// credit card payments go through
		view.displayPurchaseMembershipSuccess(membership.getMembershipType(), membership.getBaseRate());
		view.displayAccountCreationSuccess(newUsername);

		Rider rider = new Rider(newUsername, password, fullName, email, phoneNumber, address);

		model.addUser(rider); // maps rider to username in system
		model.addEmail(email, rider); // map email address to rider in the system
		model.addPaymentMethod(newUsername, paymentMethod); // add payment method to rider's account
		model.setMembership(newUsername, membership); // set rider's membership

		model.setActiveUser(newUsername); // set rider as currently active user
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

		int optionSelected;
		if (model.activeUserIsAdmin()) { // Deal with the Admin menu options
			optionSelected = Integer.parseInt(getUserInput("option9"));
			switch (optionSelected) {
			case 1: // 1) Add station
				addStation();
				break;
			case 2: // 2) Remove station
				//removeStation();
				System.out.println("Feature not yet available, check back soon!");
				break;
			case 3:// 3) Add bikes
				//addBike();
				System.out.println("Feature not yet available, check back soon!");
				break;
			case 4:// 4) Remove bikes
				//removeBike();
				System.out.println("Feature not yet available, check back soon!");
				break;
			case 5:// 5) Redistribute bikes
				equalizeStations();
				break;
			case 6:// 6) View station list
				displayStationList();
				break;
			case 7:// 7) Resolve ride
				resolveRide();
				break;
			case 8:// 8) Create support ticket
				//createSupportTicket();
				System.out.println("Feature not yet available, check back soon!");
				break;
			case 9: // ) saveData()
				saveData();
				break;
			case 10:// 10) Log out
				view.displayLogout();
				model.setActiveUser(null);
				start();
				break;
			}
		} else { // Deal with the Rider menu options
			optionSelected = Integer.parseInt(getUserInput("option10"));
			switch (optionSelected) {
			case 1:// 1) View station list
				displayStationList();
				break;
			case 2:// 2) Start ride
				startRide();
				break;
			case 3:// 3) End ride
				endRide();
				break;
			case 4:// 4) Edit profile
				//editProfile();
				System.out.println("Feature not yet available, check back soon!");
				break;
			case 5:// 5) Edit payment method
				//editPaymentMethod();
				System.out.println("Feature not yet available, check back soon!");
				break;
			case 6:// 6) Edit membership
				//editMembership();
				System.out.println("Feature not yet available, check back soon!");
				break;
			case 7:// 7) View ride history
				//displayRideHistory();
				System.out.println("Feature not yet available, check back soon!");
				break;
			case 8:// 8) View transaction history
				//displayTransactionHistory();
				System.out.println("Feature not yet available, check back soon!");
				break;
			case 9:// 9) Report issue
				//reportIssue();
				System.out.println("Feature not yet available, check back soon!");
				break;
			case 10:// 10) Log out
				view.displayLogout();
				model.setActiveUser(null);
				start();
				break;
			}
		}
		mainMenu(userIsAdmin);
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
			inputIsValid = model.isValid(userInputName, userInput); //

			while (!inputIsValid) {
				System.out.println("Invalid input, please follow the instructions and try again.");
				userInput = view.prompt(userInputName);
				inputIsValid = model.isValid(userInputName, userInput);
			}
		}
		// validate the number the user enters to pick a menu option
		else if (userInputName.contains("option")) {
			
			boolean isValidInt = StringUtils.isNumeric(userInput);
			
			int intUserInput = 0;
			int range = Integer.parseInt(userInputName.substring(6));
			if (isValidInt) {
				intUserInput = Integer.parseInt(userInput);
			}

			inputIsValid = (isValidInt && intUserInput <= range && intUserInput >= 1);

			while (!inputIsValid) {
				System.out.println("Invalid input, please follow the instructions and try again.");
				userInput = view.prompt(userInputName);
				isValidInt = StringUtils.isNumeric(userInput);
				
				if (isValidInt) {
					intUserInput = Integer.parseInt(userInput);
				}
				
				inputIsValid = (isValidInt && intUserInput <= range && intUserInput >= 1);
			}

			userInput = Integer.toString(intUserInput);

		} else {
			Pattern r = regex.get(userInputName);

			// if input is supposed to be a string of digits, clean up input to remove all
			// non-digit characters
			if (userInputName.equals("creditCardNumber") || userInputName.equals("phoneNumber")) {
				userInput = userInput.replaceAll("[^0-9]+", "");
			}

			Matcher m = r.matcher(userInput);

			inputIsValid = m.find();
			while (!inputIsValid) {
				System.out.println("Invalid input, please try again.");
				userInput = view.prompt(userInputName);

				// if input is a credit card number or phone number, clean up input to remove all non-digit
				// characters
				if (userInputName.equals("creditCardNumber") || userInputName.equals("phoneNumber")) {
					userInput = userInput.replaceAll("[^0-9]+", "");
				}
				m = r.matcher(userInput);
				inputIsValid = m.find();
			}
		}
		return userInput;

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
				else {
					
					// Assumption: The user will correctly enter the bike ID of a bike at this station
					int bikeId = Integer.parseInt(getUserInput("bikeId"));
					int stationId = Integer.parseInt(getUserInput("stationId"));
					
					// TODO for A5: check if bike is at this station
					model.startRide(bikeId, stationId); // Passes this info to startRide in Model
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
		float chargeAmount = model.endRide(stationId);
		view.chargeUserForRide(chargeAmount);
	}
	
	/**
	 * Asks the user for information to create a new station
	 */
	public void addStation() {
		//Ask the user for station id, name, capacity, # of pedelecs, kiosk?, address (a lot of calls to the view, then verification with calls to the model for each piece of information)
		int stationId = Integer.parseInt(getUserInput("newStationId"));
		String stationName = getUserInput("newStationName");
		String address = getUserInput("newStationAddress");
		int capacity = Integer.parseInt(getUserInput("capacity"));
		boolean hasKiosk = getUserInput("hasKiosk").contentEquals("1");
		
		Station station = model.addStation(stationId,stationName,address,capacity,hasKiosk);
		
		view.displayStationAdded(station.toString());
		
		// return to main menu
		mainMenu(model.activeUserIsAdmin());
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
	public void resolveRide() {
		
		// get the file name from user
		String fileName = getUserInput("fileName");
		
		
		// model reads in file can calculate
		String resolveRideResult = model.readRidesDataFile(fileName);
		
		// prompts the user for correct file name until file can be read successfully
		while (resolveRideResult.length() == 0) {
			view.displayInvalidFileName();
			fileName = getUserInput("fileName");	
			resolveRideResult = model.readRidesDataFile(fileName); // read in file and calculate
		}
		
		// display results
		view.displayResolveRide(resolveRideResult);
		
	}

	
	/**
	 * Save all data in the system into .csv files.
	 */
	public void saveData() {
		
		// model saves data in files
		model.saveData();
		
		// display confirmation
		view.displaySaveData();
	}

}
