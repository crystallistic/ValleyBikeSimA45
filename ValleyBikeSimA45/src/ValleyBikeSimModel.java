import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.sun.tools.internal.ws.processor.model.Model;

import java.util.Date;

/**
 * @author maingo
 *
 */
public class ValleyBikeSimModel {
	
	/** map all stations to their station ID */
	private HashMap<Integer, Station> stations;
	
	/** map all bikes to their bike ID */
	private HashMap<Integer, Bike> bikes;
	
	/** map each station to its list of bike IDs */
	private HashMap<Station, HashSet<Integer>> stationsBikes;
	
	/** map each rider to their payment methods */
	private HashMap<Rider, ArrayList<PaymentMethod>> paymentMethods;
	
	/** the currently logged in user */
	private User activeUser;
	
	/** contains all technical support tickets */
	private HashSet<Ticket> tickets;
	
	/** map all users to their user names */
	private HashMap<String,User> users;
	
	/** map all users to their emails */
	private HashMap<String,User> emails;
	
	/** map all riders to the rides they have completed */
	private HashMap<Rider,HashSet<Ride>> ridesCompleted;
	
	/** map all riders to the ride they currently have in progress */
	private HashMap<Rider,Ride> ridesInProgress;
	
	
	/**
	 * Constructor for the Valley Bike Simulator Model.
	 */
	public ValleyBikeSimModel() {
		
		HashMap<Station, HashSet<Integer>> stations = new HashMap<>();
		HashMap<Rider, ArrayList<PaymentMethod>> paymentMethods = new HashMap<>();
		User activeUser = null; // what's the default value here and when should it be initialized?
		HashSet<Ticket> tickets = new HashSet<>();
		HashMap<Rider,HashSet<Ride>> ridesCompleted = new HashMap<>();
		HashMap<Rider,HashSet<Ride>> ridesInProgress = new HashMap<>();		
	} 
	
	
	/**
	 * @param userInputName 	the user's input type
	 * @param userInput			the user's input
	 * @return 
	 */
	public boolean isValid(String userInputName, String userInput) {
		
		boolean inputIsValid = false;
		
		switch (userInputName) {
		case "stationId":			
			inputIsValid = (stations.containsKey(userInput));
			break;
		case "bikeId":
			inputIsValid = (bikes.containsKey(userInput));
			break;
		case "loginInfo":
			String[] info = userInput.split(" ");
			inputIsValid = (users.get(info[0]).getPassword().equals(info[1]));
			break;
		case "newUsername":
			inputIsValid = (!users.containsKey(userInput) && userInput.length() >= 6);
			break;
			
		} 	
		return inputIsValid;
	}

	/**
	 * Check if the currently active user is an admin
	 * @return
	 */
	public boolean activeUserIsAdmin() {		
		return (activeUser instanceof Admin);
	}
	
	/**
	 * Returns true if the user has a ride currently in progress, else false
	 * @return 
	 */
	public boolean isRideInProgress() {
		return ridesInProgress.containsKey(activeUser);
	}
	
	/**
	 * @return the activeUser
	 */
	public User getActiveUser() {
		return activeUser;
	}

	public boolean bikeIsOverdue() {
		// retrieve the Ride in progress associated with the active user
		Ride ride = ridesInProgress.get(activeUser);
		Date currentTime = new Date(); // get current time
		Date startTime = ride.getStartTime(); // get ride start time
		long difference = (currentTime.getTime() - startTime.getTime()) / 1000; // difference in seconds
		
		return (difference >= 86400);
	}
	
	/**
	 * Charges the user the $2000 lost bike fee + overtime fee, and return this total.
	 * @return the total amount billed to the user account
	 */
	public int chargeUser() {
		
		int amountCharged = 2000;
		
		// calculate overtime
		
		
		// charge the user
		
		return amountCharged;
	}

	/**
	 * @param activeUser the activeUser to set
	 */
	public void setActiveUser(User activeUser) {
		this.activeUser = activeUser;
	}
	
	/**
	 * @param activeUser the activeUser to set
	 */
	public void setActiveUser(String activeUsername) {
		this.activeUser = users.get(activeUsername);
	}
	
	
}
