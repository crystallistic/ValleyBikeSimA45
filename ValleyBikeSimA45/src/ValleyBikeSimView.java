import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
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
		System.out.println("Welcome to Valley Bike!\n"
				+ "1) Sign up\n"
				+ "2) Login\n"
				+ "3) Exit");
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
				+ "1) Pay per ride\t($2 per ride, first 30 minutes are free, $0.15 for each additional minute)\n"
				+ "2) Day Pass\t($6 for one day, first 30 minutes are free, $0.15 for each additional minute)\n"
				+ "3) Monthly\t($20 a month, first 45 minutes are free, $0.15 for each additional minute)\n"
				+ "4) Yearly\t($80 a year, first 45 minutes are free, $0.15 for each additional minute)\n"
				+ "5) Founding Member\t($90 a year, first 60 minutes are free, $0.15 for each additional minute)\n");
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
			System.out.println("Please enter your first and last name.\n"
					+ "Make sure to capitalize the first letter of your first and last name (example: Johnny Appleseed):");
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
					+ "\"Address line 1, Address line 2 (if applicable), City, State/Province/Region, Zipcode\"");
			break;
		case "capacity":
			System.out.println("Please enter the station's capacity (within the range of 05-27):");
			break;
		case "hasKiosk":
			System.out.println("Please enter 1 if the station has a kiosk, otherwise enter 0:");
			break;
		case "fileName":
			System.out.println("Please enter the name of the data file, including the .csv extension. The file must be stored in the data-files folder:");
			break;	
		case "newBikeId":
			System.out.println("Please enter the new 3-digit bike ID. It must not coincide with another bike ID number in the system:");
			break;	
		case "bikeIdInStorage":
			System.out.println("Please enter the 3-digit bike ID. The bike must be in storage in order to be added to a station:");
			break;	
		case "riderAddress":
			System.out.println("Please enter your address on one line, in the following format:\n"
					+ "\"Address line 1, Address line 2 (if applicable), City, State/Province/Region, Zipcode, Country\"");
			break;
		case "removeBikeId":
			System.out.println("Please enter the 3-digit bike ID. The bike must not be in use by any user in order to be removed:");
			break;
		case "pastDay":
			System.out.println("Please enter the day you want the statistics for. The format should be mm-dd-yy:");
			break;
		case "ticketDescription":
			System.out.println("Please describe the issue in a single paragraph. The message may not be empty.\n"
					+ "Press \"Enter\" when you finish writing the message:");
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
				+ "1) Add a station\n"
				+ "2) Remove a station\n"
				+ "3) Add a bike\n"
				+ "4) Remove a bike\n"
				+ "5) Redistribute bikes\n"
				+ "6) View station list\n"
				+ "7) View daily statistics\n"
				+ "8) Create support ticket\n"
				+ "9) Resolve support ticket\n"
				+ "10)Log out");
	}
	
	/**
	 * Displays a rider's main menu
	 */
	public void displayRiderMainMenu() {
		System.out.println("Please enter a number corresponding to one of the actions below:\n"
				+ "1) View station list\n"
				+ "2) Start a ride\n"
				+ "3) End a ride\n"
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
	 * Display the full list of station in the ValleyBike System.
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
				+ "If you are trying to return a bike that is not yours or you have an overdue bike, please submit a support ticket\n"
				+ "specifically for those issues.");
	}

	public void displayFullDock() {
		System.out.println("This station has no available docks. Would you like to return your bike to a different station?\n"
							+ "1) Return my bike to a different station\n"
							+ "2) Contact customer support to end your ride here");
		
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
		System.out.println("You have successfully added a new station with the following information: \nID\tBikes\tAvDocs\tMainReq\tCap\tKiosk\tName - Address\n" + stationInfo);
		
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
		System.out.println("You have an overdue bike which has been marked as stolen. A hold has been placed on your account.\n"
				+ "Please contact ValleyBike for instructions for next steps");
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
	 * Instruct the user to choose between adding a bike to a station or storage
	 */
	public void displayAddNewOrExistingBike() {
		System.out.println("Would you like to add a new or existing bike?\n"
				+ "1) Add new bike to storage/station\n"
				+ "2) Add existing bike from storage to station");
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
	}
	
	/**
	 * Display the formatted transaction list
	 * @param formattedTransactionList
	 */
	public void displayTransactionList(ArrayList<String> formattedTransactionList) {
		for (String transactionInfo : formattedTransactionList) {
			System.out.println(transactionInfo);
		}
		
	}

	/*
	 * Ask the user whether they would like to add a bike from storage to a station
	 */
	public void displayAddBikeToStationOrNot() {
		System.out.println("Would you like to add the bike to a station, or leave it in storage?\n"
				+ "1) Add bike to station\n"
				+ "2) Leave bike in storage");
	}

	/**
	 * Confirm successful addition of a new bike to storage.
	 * @param string
	 */
	public void addNewBikeToStorageSuccess(String bikeId) {
		System.out.println("You have successfully added a new bike with ID " + bikeId + " into the system.\n"
				+ "The bike is currently in storage.");
		
	}

	/**
	 * Confirm successful addition of a  bike from storage to a station.
	 * @param bikeId		The bike's ID number
	 * @param stationId		The station's ID number
	 */
	public void displayAddBikeFromStorageToStationSuccess(int bikeId, int stationId) {
		System.out.println("You have successfully added bike " + Integer.toString(bikeId) + " to station " + Integer.toString(stationId) + ".");
	}

	/**
	 * Notify the user that the station they chose is at capacity
	 * @param stationId		The station's ID
	 */
	public void displayStationAtCapacity(int stationId) {
		System.out.println("Station " + Integer.toString(stationId) + " is currently at capacity.\n"
				+ "Please select a different station");
		
	}

	/**
	 * Tells the user that they have entered a bike number for a bike not at their current station
	 * @param stationId
	 * @param bikeId
	 * @param bikeIdList
	 */
	public void displayBikeNotBelongToStation(int stationId, int bikeId, HashSet<Integer> bikeIdList) {
		System.out.println("Station " + Integer.toString(stationId) + " does not have bike " + Integer.toString(bikeId) + ".\n"
				+ "You may want to select a bike ID from the list of bike IDs currently at this station below:");
		for (Integer bId : bikeIdList) {
			System.out.print(bId);
			System.out.print(',');
		}
		System.out.println();
		
	}

	/**
	 * Tells the user what membership they have and that the default is Pay-per-ride
	 * @param numCurrentMembership
	 */
	public void displayEditMembership(int numCurrentMembership) {
		System.out.println("numCurrentMembership: "+numCurrentMembership);
		String[] membershipOptions = {"Pay per ride","Day Pass","Monthly","Yearly","Founding Member"};
		String options = "Please choose from the membership options below by entering the corresponding option number (1-5):\n";
		for (int i=0; i<5; i++) {
			String option = membershipOptions[i];
			if (numCurrentMembership==(i+1)) {
				options = options+(i+1)+") Keep existing membership ("+option+")\n";
			} else {
				options = options+(i+1)+") "+option+"\n";
			}
		}
		System.out.println(options);
	}

	/**
	 * Displays message reassuring user that they have kept their current membership
	 */
	public void displayKeepCurrentMembership() {
		System.out.println("You have chosen to keep your current membership");
	}

	/**
	 * Displays message when a user with no ride history tries to view their ride history
	 */
	public void displayNoRidesMade() {
		System.out.println("You have not yet completed any rides.");
	}
	
	/**
	 * Displays message when a user with no transaction history tries to view their transaction history
	 */
	public void displayNoTransactionsMade() {
		System.out.println("You have not yet completed any transactions.");
	}
	
	/**
	 * Informs user that the bike is in, or has been moved to, storage.
	 * @param bikeInStorageMessage	Message to be shown
	 */
	public void displayBikeInStorageMessage(String bikeInStorageMessage) {
		System.out.println(bikeInStorageMessage);
		
	}

	/**
	 * Ask the user if they would like to remove the bike from the system.
	 */
	public void promptRemoveBikeFromSystem() {
		System.out.println("Would you like to remove this bike from the system, or leave it in storage?\n"
				+ "1) Remove bike from system\n"
				+ "2) Leave bike in storage");
		
	}

	/**
	 * Displays confirmation of successful removal of bike from system.
	 * @param bikeId		The bike ID
	 */
	public void displayRemoveBikeFromSystemSuccess(int bikeId) {
		System.out.println("Bike " + bikeId + " has been removed successfully from the system.");
	}

	/**
	 * Displays message when user views their current payment method and offers options
	 * @param paymentMethodString
	 */
	public void displayCurrentPaymentMethod(String paymentMethodString) {
		System.out.println("Your current payment method:\n\n"+paymentMethodString);
		System.out.println("Which of the following would you like to edit?:\n"
							+ "1) Change billing name\n"
							+ "2) Change billing address\n"
							+ "3) Add new card\n"
							+ "4) Return to main menu\n");
	}
	
	/**
	 * Displays user's current profile and options for what to edit
	 * @param userProfileString
	 */
	public void displayCurrentUserProfile(String userProfileString) {
		System.out.println("Your current user profile:\n\n"+userProfileString);
		System.out.println("Which of the following would you like to edit?:\n"
							+ "1) Password\n"
							+ "2) Full name\n"
							+ "3) Email\n"
							+ "4) Phone number\n"
							+ "5) Address\n"
							+ "6) Return to main menu\n");
	}

	/**
	 * Prints out ride statistics for a day
	 * @param rideStatistics
	 */
	public void displayRideStatistics(String rideStatistics) {
		System.out.println(rideStatistics);
	}

	/**
	 * Display all ticket categories
	 */
	public void displayTicketCategory() {
		System.out.println("What is this issue related to?\n"
							+ "1) A station (issues with kiosks, docks, etc.)\n"
							+ "2) A broken bike\n"
							+ "3) Resolve issue with an overdue bike\n"
							+ "4) Other (user account, payment methods, etc.)");

	}

	/**
	 * Prompt user to choose a support ticket category
	 */
	public void displayChooseStation() {
		System.out.println("Which station is this issue related to?");
		
	}

	/**
	 * Confirm successful creation of support ticket. Displays different confirmation messages depending on the ticket content
	 */
	public void displaySubmitSupportTicketSuccess(int ticketId, String optionSelected) {
		
		// open tickets
		if (optionSelected.equals("1") || optionSelected.equals("2") || optionSelected.equals("4")) {
			System.out.println("You have successfully submitted a support ticket regarding your issue.\n"
					+ "Your support ticket number is " + ticketId + ".");
		}
		
		if (optionSelected.equals("3")) {
			System.out.println("You have been charged $2000 for not returning your bike to a station within 24 hours of starting your ride."
					+ "\nPlease note that this charge is non-refundable even if you return this bike to ValleyBike,\n"
					+ "although we do encourage that you do so by contacting customer service.\n"
					+ "You may now start a new ride.");
		}
		
		
		
	}
	
	/*
	 * Prints out transaction statistics for a day
	 * @param transactionStatistics
	 */
	public void displayTransactionStatistics(ArrayList<String> transactionStatistics) {
		if (transactionStatistics.size()<=2) {
			System.out.println("No transactions were completed on the given day.");
		} else {
			for (String line : transactionStatistics) {
				System.out.println(line);
			}
		}
	}

	/**
	 * Tells the user that their date is invalid.
	 */
	public void displayInvalidDate() {
		System.out.println("Date does not exist");	
	}

	/**
	 * Tell the user they cannot enter an empty string
	 */
	public void displayEmptyInputError() {
		System.out.println("You may not enter an empty input. Please try again.");
	}
	
	/*
	 * Tells the user that they can't re-use their old password
	 */
	public void displayOldPassword() {
		System.out.println("New password is the same as old password. Try again.");
	}

	/**
	 * Tells the user that their Day Pass has expired 
	 */
	public void displayDayPassExpired() {
		System.out.println("Your Day Pass has expired. You now have a Pay-Per-Ride Pass.");
	}
	
	/**
	 * Informs user that they've contacted customer support successfully about returning a bike at a full station
	 */
	public void displayReturnBikeAtFullStationSuccess() {
		System.out.println("Thank you for letting us know! Please lock the bike securely near the station, \n"
				+ "and we will mark your ride as completed in the system. Thank you for riding with ValleyBike.");
	}

	/**
	 * Informs user that they did not steal a bike.
	 */
	public void displayUserDidNotStealBike() {
		System.out.println("You do not have any overdue rides.");
	}

	/**
	 * Display the full list of support tickets.
	 */
	public void displaySupportTickets(ArrayList<String> formattedTicketList) {
		for (String s : formattedTicketList) {
			System.out.println(s);
		}
		
	}

	/**
	 * Tells user this bike is already in storage/already reported as broken
	 */
	public void displayBikeAlreadyInStorage() {
		System.out.println("This bike is in storage or has already been reported as broken.");
		
	}

	/**
	 * Let user know they should check in their bike before submitting the ticket.
	 */
	public void displayEndRideFirst() {
		System.out.println("This bike is currently in use. If you are using this bike, or know someone who is,\n"
				+ "please dock this bike at a station before submitting a support ticket for a broken bike.\n"
				+ "This will help ValleyBike locate the bike for repair.");
	}
	
}
