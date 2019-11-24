import java.util.Scanner;

/**
 * @author maingo
 *
 */
/**
 * @author maingo
 *
 */
public class ValleyBikeSimView {
	
	
	/**
	 * Constructor for the Valley Bike Simulator View.
	 */
	public ValleyBikeSimView() {
		
	}
	
	/**
	 * Displays welcome screen, with options to create a new account, login, or exit program
	 */
	public void displayWelcomeScreen() {
		
		// show welcome message and options for sign up, login, and exit program
		System.out.println("Welcome to Valley Bike!:\n"
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
	public void displayPurchaseMembershipSuccess(String membershipType, int baseRate) {
		System.out.println("You have successfully purchased the " + membershipType + " membership. Your credit card has been charged in the amount of $" + Integer.toString(baseRate));
	}
	
	/**
	 * Displays message confirming new account creation and username.
	 * @param newUserName 	username associated with newly created account
	 */
	public void displayAccountCreationSuccess(String newUserName) {
		System.out.println("Congratulations! You have successfully created an account with ValleyBike. Your username is " + newUserName + ".");
	}
	
	/**
	 * Displays message at program exit.
	 */
	public void displayExit() {
		System.out.println("Thank you for using ValleyBike!");
	}
	
	/**
	 * Prompts the user for information 
	 * @param prompt the information that the program is prompting for 
	 */
	public String prompt(String prompt) {
		
		String input = null;
		Scanner sc = new Scanner(System.in);
		
		switch (prompt) {
		case "email":
			System.out.println("Please enter your email:\n");
			break;
		case "welcome": // welcome screen, with 3 options for login, sign up, or exit
			System.out.println("To start, please enter a number corresponding to one of the options below (1-3)\n");
			break;
		case "username": // prompt for user name
			System.out.println("Please enter your username: \n");
			break;
		case "password": // prompt for password
			System.out.println("Please enter your password: \n");
			break;
		case "newUserName":
			System.out.println("Please enter a username (at least 6 characters): \n");
			break;
		case "newPassword":
			System.out.println("Please enter a password (at least 6 characters): \n");
			break;
		case "fullName":
			System.out.println("Please enter your first and last name: \n");
			break;
		case "phoneNumber":
			System.out.println("Please enter your 10-digit cellphone number: \n");
			break;
		case "address":
			System.out.println("Please enter your address on one line, in the following format: \"Address line 1, Address line 2 (if applicable), City, State, Zipcode, Country\":\n");
			break;
		case "billingName":
			System.out.println("Please enter the full name on your credit card: \n");
			break;
		case "billingAddress":
			System.out.println("Please enter your billingaddress on one line, in the following format: \"Address line 1, Address line 2 (if applicable), City, State, Zipcode, Country\":\n");
			break;
		case "creditCardNumber":
			System.out.println("Please enter your credit card number: \n");
			break;
		case "creditCardDate":
			System.out.println("Please enter your credit card expiration date in one of the following formats - MM/YY or MM/YYYY: \n");
			break;
		case "CVV":
			System.out.println("Please enter your 3 or 4-digit security code on your credit card: \n");
			break;
		default: // default is options for a menu
			System.out.println("Please enter a number corresponding to your option: \n");
			break;
		
		} 
		
		input = sc.nextLine().trim();
		
		sc.close();
		
		return input;
	}
	
	/**
	 * 
	 */
	public void remindEndRide() {
		System.out.println("You have a ride currently in progress. Please check in your bike.");
	}
	
	/**
	 * 
	 * @param amountCharged
	 */
	public void notifyOverdue(int amountCharged) {
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

	public void displayAdminMainMenu() {
		
	}
	
	public void displayRiderMainMenu() {
		
	}
	
}
