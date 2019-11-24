import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

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
	 * @param view
	 * @param model
	 */
	public ValleyBikeSimController(ValleyBikeSimView view, ValleyBikeSimModel model) {
		this.view = view;
		this.model = model;
		this.regex = new HashMap<>();
		generateRegex();
		String fieldsToValidateInModel[] = {"bikeId", "stationId", "newUsername", "newEmail"}; 
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
		regex.put("phoneNumber", Pattern.compile("[1-9][0-9]{9}")); // phone number format
		
		// credit card format, regex adapted from regular-expressions.com
		// Source: https://www.regular-expressions.info/creditcard.html
		// In order: Visa, Mastercard, American Express, Diners Club, Discover, JCB
		// - Visa: ^4[0-9]{12}(?:[0-9]{3})?$ All Visa card numbers start with a 4. 
		// New cards have 16 digits. Old cards have 13.
		// - MasterCard: ^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$ 
		// MasterCard numbers either start with the numbers 51 through 55 or with the numbers 2221 through 2720. 
		// All have 16 digits.
		// - American Express: ^3[47][0-9]{13}$ 
		// American Express card numbers start with 34 or 37 and have 15 digits.
		// - Diners Club: ^3(?:0[0-5]|[68][0-9])[0-9]{11}$ 
		// Diners Club card numbers begin with 300 through 305, 36 or 38. 
		// All have 14 digits. There are Diners Club cards that begin with 5 and have 16 digits. 
		// These are a joint venture between Diners Club and MasterCard, and should be processed like a MasterCard.
		// - Discover: ^6(?:011|5[0-9]{2})[0-9]{12}$ 
		// Discover card numbers begin with 6011 or 65. All have 16 digits.
		// - JCB: ^(?:2131|1800|35\d{3})\d{11}$ 
		// JCB cards beginning with 2131 or 1800 have 15 digits. JCB cards beginning with 35 have 16 digits.
		regex.put("creditCardNumber", Pattern.compile("^("
				+ "?:4[0-9]{12}(?:[0-9]{3})?|"
				+ "(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}|"
				+ "3[47][0-9]{13}|"
				+ "3(?:0[0-5]|[68][0-9])[0-9]{11}|"
				+ "6(?:011|5[0-9]{2})[0-9]{12}|"
				+ "(?:2131|1800|35\\d{3})\\d{11}"
				+ ")$")); 
		regex.put("billingAddress", Pattern.compile(".*")); // we assume the user will enter a valid address
		regex.put("creditCardDate", Pattern.compile("^(0[1-9]|1[0-2])\\/?([0-9]{4}|[0-9]{2})$")); // expiry date of credit card. Regex source: https://stackoverflow.com/questions/20430391/regular-expression-to-match-credit-card-expiration-date
		regex.put("CVV", Pattern.compile("^[0-9]{3,4}$")); // CVV of credit card. Regex source: https://stackoverflow.com/questions/12011792/regular-expression-matching-a-3-or-4-digit-cvv-of-a-credit-card
		regex.put("fullName", Pattern.compile("^[a-z ,.'-]+$")); // first and last name separated by space. Regex source: https://stackoverflow.com/questions/2385701/regular-expression-for-first-and-last-name
		regex.put("billingName", Pattern.compile("^[a-z ,.'-]+$")); // billingName
	}
	
	/**
	 * start program and show main menu
	 */  
	public void start() {
		// show welcome screen with options to login, signup, or exit program
		view.displayWelcomeScreen();
		String optionSelected = getUserInput("option3");		
		
		switch (optionSelected) {
		case "1":
			login(); // rider logs in using valid username and password
			break;
		case "2":
			signup(); // rider creates new account and purchase a membership
			break;
		case "3": 
			exit(); // displays exit message, exit system
			break;
		}
		
		
	}
	
	/**
	 * Prompts for the user's valid login information and logs the user in the system.
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
		
		mainMenu(userIsAdmin); // show admin menu if user is admin, else show rider menu
		
	}
	
	/**
	 * Creates a new account and purchase membership for a ValleyBike user. Only Riders can create an account this way.
	 */
	public void signup() {
		view.displaySignupScreen();
		
		String newUserName = getUserInput("newUsername");
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
		
		PaymentMethod paymentMethod = new PaymentMethod(billingName, creditCardNumber, billingAddress, creditCardDate, cvv);
		
		// Charge user's credit card. To simplify process: we assume for now that all credit card payments go through
		view.displayPurchaseMembershipSuccess(membership.getMembershipType(), membership.getBaseRate());
		view.displayAccountCreationSuccess(newUserName);
		
		Rider rider = new Rider(newUserName, password, fullName, email, phoneNumber, address);
		
		model.addUser(rider); // maps rider to username in system
		model.addEmail(rider, email); // map email address to rider in the system
		model.addPaymentMethod(rider, paymentMethod); // add payment method to rider's account
		model.setMembership(rider, membership); // set rider's membership
		 
		
		mainMenu(false); // show rider menu (userIsAdmin = false)
		model.setActiveUser(newUserName); // set rider as currently active user
		
	}
	
	public void exit() {
		view.displayExit();
		System.exit(0);
	}
	
	/**
	 * Invokes appropriate menu for the currently active user.
	 * @param userIsAdmin 	true if user is admin, else false
	 */
	public void mainMenu(boolean userIsAdmin) {
		
		view.displayMainMenu(userIsAdmin);
		
	}
	
	/**
	 * Prompts user and validates using the name of the thing that we want (e.g. getUserInput(email))
	 * @return user input as a string
	 */
	public String getUserInput(String userInputName) {
		
		boolean inputIsValid;
		
		String userInput = view.prompt(userInputName);
		
		if (userInputName.equals("username")) {
			String password = view.prompt("password").trim();
			String loginInfo = userInput + " " + password;
			inputIsValid = model.isValid("loginInfo",loginInfo);
			
			while (!inputIsValid) {
				
				System.out.println("Invalid username and password combination, please try again.");
	
				userInput = view.prompt(userInputName); // username
				password = view.prompt("password");
				loginInfo = userInput + " " + password;
				inputIsValid = model.isValid("loginInfo",loginInfo);
			}
		}
		else if (validateInModel.contains(userInputName)) {
			inputIsValid = model.isValid(userInputName,userInput); // 
			
			while(!inputIsValid) {
				System.out.println("Invalid " + userInputName + ", please try again.");
				userInput = view.prompt(userInputName);
				inputIsValid = model.isValid(userInputName,userInput);
			}
		}
		// validate the number the user enters to pick a menu option
		else if (userInputName.contains("option")) {
			int intUserInput = Integer.parseInt(userInput);
			int range = Integer.parseInt(userInputName.substring(6));
	
			 inputIsValid = (intUserInput <= range && intUserInput >= 1);
			 
			 while (!inputIsValid) {
				 System.out.println("Invalid input, please try again.");
				 intUserInput = Integer.parseInt(view.prompt(userInputName));
				 inputIsValid = (intUserInput <= range && intUserInput >= 1);
			 }
			 
			 userInput = Integer.toString(intUserInput);
			
		} else {
			Pattern r = regex.get(userInputName);
			
			// if input is supposed to be a string of digits, clean up input to remove all non-digit characters
			if (userInputName.equals("creditCardNumber") || userInputName.equals("phoneNumber")) {
				userInput.replaceAll("[^0-9]+", "");
			}
			
			Matcher m = r.matcher(userInput);
			
			inputIsValid = m.find();
			while (!inputIsValid) {
				System.out.println("Invalid input, please try again.");
				userInput = view.prompt(userInputName);
				
				// if input is a credit card number, clean up input to remove all non-digit characters
				if (userInputName.equals("creditCardNumber") || userInputName.equals("phoneNumber")) {
					userInput.replaceAll("[^0-9]+", "");
				}
				m = r.matcher(userInput);
				inputIsValid = m.find();
			}
		}
		return userInput;
		
	}
}
