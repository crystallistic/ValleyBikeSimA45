import java.util.Arrays;
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
		String arr[] = {"bikeId", "stationId", "newUsername", "email"}; 
	    // Set demonstration using HashSet Constructor 
		validateInModel = new HashSet<>(Arrays.asList(arr));
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
		regex.put("phoneNumber", Pattern.compile("")); // add later
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
			login();
		case "2":
			signup();
		default: 
			exit();
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
	
	public void signup() {
		view.displaySignupScreen();
		
		
	}
	
	/**
	 * Invokes appropriate menu for the currently active user.
	 * @param userIsAdmin 	true if user is admin, else false
	 */
	public void mainMenu(boolean userIsAdmin) {
		
		view.displayMainMenu(userIsAdmin);
		
		String newUserName = getUserInput("newUsername");
		String password = getUserInput("newPassword");
		String email = getUserInput("email");
		String address = getUserInput("address");
		
	}
	
	/**
	 * Prompts user and validates using the name of the thing that we want (e.g. getUserInput(email))
	 * @return user input as a string
	 */
	public String getUserInput(String userInputName) {
		
		boolean inputIsValid;
		String userInput = view.prompt(userInputName);
		
		if (userInputName.equals("username")) {
			String password = view.prompt("password");
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
			Matcher m = r.matcher(userInput);
			
			inputIsValid = m.find();
			while (!inputIsValid) {
				System.out.println("Invalid input, please try again.");
				userInput = view.prompt(userInputName);
				m = r.matcher(userInput);
				inputIsValid = m.find();
			}
		}
		return userInput;
		
	}
}
