import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author maingo
 *
 */
public class ValleyBikeSimView {
	
	private Scanner sc;
	/**
	 * Constructor for the Valley Bike Simulator View.
	 */
	public ValleyBikeSimView() {
		sc = new Scanner(System.in);
	}
	
	/**
	 * Displays welcome screen, with options to create a new account, login, or exit program
	 */
	public void displayWelcomeScreen() {
		
		// show welcome message and options for sign up, login, and exit program
		System.out.println("Welcome to Valley Bike! Please enter a number corresponding to one of the options below:\n"
				+ "1) Sign up\n"
				+ "2) Login\n"
				+ "3) Exit\n");
	}
	
	/**
	 * Displays message on the login screen.
	 */
	public void displayLoginScreen() {
		// show login screen
		System.out.println("Please enter your login information.");
	}
	
	/**
	 * Displays message on the signup screen.
	 */
	public void displaySignupScreen() {
		// show sign up screen
		System.out.println("Please enter the following information to create an account.");
	}
	
	/**
	 * Displays a list from all membership options and prompts the rider to enter their selection.
	 */
	public void displayMembershipOptions() {
		System.out.println("Please choose from the membership options below by entering the corresponding option number (1-5):\n"
				+ "1) Pay per ride\n"
				+ "2) Day Pass\n"
				+ "3) Monthly\n"
				+ "4) Yearly\n"
				+ "5) Founding Member\n");
	}
	
	/**
	 * Displays message confirming purchase of membership and amount charged to rider for membership.
	 * @param membershipType	the type of the membership
	 * @param baseRate			the amount initially charged to the user's account (i.e. does not include overtime fee for rides)
	 */
	public void displayPurchaseMembershipSuccess(String membershipType, BigDecimal baseRate) {
		System.out.println("You have successfully purchased the " + membershipType + " membership. Your credit card has been charged in the amount of $" + baseRate.toString());
	}
	
	/**
	 * Displays message confirming new account creation and username.
	 * @param newUsername 	username associated with newly created account
	 */
	public void displayAccountCreationSuccess(String newUsername) {
		System.out.println("Congratulations! You have successfully created an account with ValleyBike. Your username is " + newUsername + ".");
	}
	
	/**
	 * Displays message confirming successful login.
	 * @param newUsername 	username associated with newly created account
	 */
	public void displayLoginSuccess() {
		System.out.println("You are now logged in.");
	}
	
	/**
	 * Displays message at program exit.
	 */
	public void displayExit() {
		System.out.println("Thank you for using ValleyBike!");
		sc.close(); // exit program, close scanner to stop taking in user input
	}
	
	/**
	 * Prompts the user for information 
	 * @param prompt the information that the program is prompting for 
	 */
	public String prompt(String prompt) {
		
		String input = null;
		
		switch (prompt) {
		case "email":
			System.out.println("Please enter your email:");
			break;
		case "stationId":
			System.out.println("Please enter the 2-digit station ID. The station ID has to exist in the system:");
			break;
		case "bikeId":
			System.out.println("Please enter the 3-digit bike ID. The bike ID has to exist in the system:");
			break;
		case "welcome": // welcome screen, with 3 options for login, sign up, or exit
			System.out.println("To start, please enter a number corresponding to one of the options below (1-3)");
			break;
		case "username": // prompt for user name
			System.out.println("Please enter your username:");
			break;
		case "password": // prompt for password
			System.out.println("Please enter your password:");
			break;
		case "newUsername":
			System.out.println("Please enter a username (at least 6 characters):");
			break;
		case "newPassword":
			System.out.println("Please enter a password (at least 6 characters):");
			break;
		case "newEmail":
			System.out.println("Please enter your email. You should not use an email that is already registered to another ValleyBike user.");
			break;
		case "fullName":
			System.out.println("Please enter your first and last name:");
			break;
		case "phoneNumber":
			System.out.println("Please enter your 10-digit cellphone number:");
			break;
		case "address":
			System.out.println("Please enter your address on one line, in the following format:\n"
					+ "\"Address line 1, Address line 2 (if applicable), City, State/Province/Region, Zipcode, Country\"");
			break;
		case "billingName":
			System.out.println("Please enter the full name on your credit card:");
			break;
		case "billingAddress":
			System.out.println("Please enter your billing address on one line, in the following format:\n" 
					+ "\"Address line 1, Address line 2 (if applicable), City, State/Province/Region, Zipcode, Country\"");
			break;
		case "creditCardNumber":
			System.out.println("Please enter your 16-digit credit card number:");
			break;
		case "creditCardDate":
			System.out.println("Please enter your credit card expiration date in the following formats - MM/YY:");
			break;
		case "CVV":
			System.out.println("Please enter your 3-digit security code on your credit card:");
			break;
		case "newStationId":
			System.out.println("Please enter the station's ID number. It cannot be the same as one of the existing stations,\n"
					+ "and has to be within the range (01-99):");
			break;
		case "newStationName":
			System.out.println("Please enter the station's name. It cannot be the same as one of the existing stations:");
			break;
		case "newStationAddress":
			System.out.println("Please enter the station's address. It cannot be the same as one of the existing stations\n"
					+ "and must follow the following format:\n"
					+ "\"Address line 1, Address line 2 (if applicable), City, State/Province/Region, Zipcode, Country\"");
			break;
		case "capacity":
			System.out.println("Please enter the station's capacity (within the range of 01-27):");
			break;
		case "hasKiosk":
			System.out.println("Please enter 1 if the station has a kiosk, otherwise enter 0:");
			break;
		case "fileName":
			System.out.println("Please enter the name of the data file, including the .csv extension. The file must be stored in the data-files folder:");
			break;		
		default: // default is options for a menu
			System.out.println("Please enter a number corresponding to your option:");
			break;
		
		} 
		
		input = sc.nextLine();
		
		return input;
	}
	
	/**
	 * Remind the user to end the current ride.
	 */
	public void remindEndRide() {
		System.out.println("You have a ride currently in progress. Please check in your bike.");
	}
	
	/**
	 * 
	 * @param amountCharged
	 */
	public void notifyOverdue() {
		System.out.println("You have an overdue bike. You have been charged a stolen bike fee in the amount of $2000.");
	}
	
	/**
	 * Displays the menu appropriate for the type of user (rider or admin)
	 * @param userIsAdmin
	 */
	public void displayMainMenu(boolean userIsAdmin) {
		
		if (userIsAdmin) {
			displayAdminMainMenu();
		} else {
			displayRiderMainMenu();
		}
	}

	/**
	 * Displays an admin's main menu
	 */
	public void displayAdminMainMenu() {
		System.out.println("Please enter a number corresponding to one of the actions below:\n"
				+ "1) Add station\n"
				+ "2) Remove station\n"
				+ "3) Add bikes\n"
				+ "4) Remove bikes\n"
				+ "5) Redistribute bikes\n"
				+ "6) View station list\n"
				+ "7) Resolve ride\n"
				+ "8) Create support ticket\n"
				+ "9) Log out");
	}
	
	/**
	 * Displays a rider's main menu
	 */
	public void displayRiderMainMenu() {
		System.out.println("Please enter a number corresponding to one of the actions below:\n"
				+ "1) View station list\n"
				+ "2) Start ride\n"
				+ "3) End ride\n"
				+ "4) Edit profile\n"
				+ "5) Edit payment method\n"
				+ "6) Edit membership\n"
				+ "7) View ride history\n"
				+ "8) View transaction history\n"
				+ "9) Report issue\n"
				+ "10) Log out\n");
	}

	/**
	 * Display a message confirming successful bike check-out and beginning of a ride.
	 */
	public void displayRideStart() {
		System.out.println("Enjoy your ride.");
	}
	
	/**
	 * Display a message confirming successful bike check-out and beginning of a ride.
	 */
	public void displayStationList(ArrayList<String> stationList) {
		for (String s : stationList) {
			System.out.println(s);
		}
	}
	
	/**
	 * Display message confirming that the user has logged out.
	 */
	public void displayLogout() {
		System.out.println("You have successfully logged out of your account. Come back soon!");
	}

	/**
	 * Displays error message when the user tries to end a ride but they don't have any ride in progress.
	 */
	public void displayNoActiveRide() {
		System.out.println("Sorry, action cannot be completed because you have no ride in progress.\n"
				+ "If you are trying to return a bike that is not yours, please submit a maintenance request.");
	}

	public void displayFullDock() {
		System.out.println("Dock is full. Please return bike to another station");
		
	}

	/**
	 * Notifies the user that they have been charged the specified amount.
	 * @param chargeAmount		amount charged to the user's account
	 */
	public void chargeUserForRide(BigDecimal chargeAmount) {
		System.out.printf("You have been charged " + chargeAmount.toString() + " for your past ride.\n");
	}

	/**
	 * Displays message confirming successful addition of a new station.
	 * @param stationInfo	Information of the newly added station.
	 */
	public void displayStationAdded(String stationInfo) {
		System.out.println("You have successfully added a new station with the following information: \n ID\tBikes\tAvDocs\tMainReq\tCap\tKiosk\tName - Address\n" + stationInfo);
		
	}

	/**
	 * Confirms successful equalization of stations
	 */
	public void displayEqualizationCompleted() {
		System.out.println("Bikes redistributed successfully.");
	}

	/**
	 * Display message notifying user of unsuccessful reading of file.
	 */
	public void displayInvalidFileName() {
		System.out.println("The system has encountered an error while trying to read the file you entered. Please check for errors and try again.");	
	}

	/**
	 * Display the result of calculations for resolveRide.
	 * @param resolveRideResult 		Average stats for the day
	 */
	public void displayResolveRide(String resolveRideResult) {
		System.out.println(resolveRideResult);
	}
	
	/**
	 * Displays confirmation that all data in the system have been saved in .csv files.
	 */
	public void displaySaveData() {
		System.out.println("All data have been saved successfully.");	
	}

	/**
	 * Notifies user that their credit card has expired.
	 */
	public void cardExpired() {
		System.out.println("Your credit card has expired. Please select or enter a different payment method in order to continue with this action.");	
	}

	/**
	 * Notifies user of stolen bike charge.
	 */
	public void bikeStolen() {
		System.out.println("You have an overdue bike which has been marked as stolen. You have been charged $2000. Please contact ValleyBike for instructions for next steps");
	}

	/**
	 * Notifies user that no station currently exists in the system.
	 */
	public void displayNoStationExistsError() {
		System.out.println("Action cannot be completed because no station currently exists in the system.");
		
	}

	/**
	 * Confirm removal of station from system
	 * @param stationId
	 */
	public void removeStationSuccess(int stationId) {
		System.out.println("Station " + Integer.toString(stationId) + " has been successfully removed from the system.");
	}

	public void displayInvalidInput() {
		System.out.println("Invalid input - wrong input format, or there has been some conflict with existing data in the system.\n"
				+ "Please follow the instructions and try again.");
		
	}

	/**
	 * Display the formatted ride list
	 * @param formattedRideList
	 */
	public void displayRideList(ArrayList<String> formattedRideList) {
		System.out.println("You have completed "+(formattedRideList.size()-1)+" rides since joing ValleyBike:\n");
		for (String rideInfo: formattedRideList) {
			System.out.println(rideInfo);
		}
		System.out.println("\nThank you for using ValleyBike!\n");
	}
	
}
