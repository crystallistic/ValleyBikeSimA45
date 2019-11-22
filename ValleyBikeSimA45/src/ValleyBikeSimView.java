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
		} 	
		
		input = sc.nextLine();
		
		sc.close();
		
		return input;
	}
}
