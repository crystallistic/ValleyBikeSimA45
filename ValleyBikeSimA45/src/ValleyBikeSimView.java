import java.util.Scanner;

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
	
	public void displayLoginScreen() {
		// show login screen
		System.out.println("Please enter your login information.");
	}
	
	public void displaySignupScreen() {
		// show sign up screen
		System.out.println("Please enter the following information to create an account.");
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
			System.out.println("Please enter a username (at least 6 characters)");
			break;
		case "newPassword":
			System.out.println("Please enter a password (at least 6 characters)");
			break;
		} 
		
		input = sc.nextLine();
		
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
	 * 
	 * @param userIsAdmin
	 */
	public void displayMainMenu(boolean userIsAdmin) {
		//
	}
}
