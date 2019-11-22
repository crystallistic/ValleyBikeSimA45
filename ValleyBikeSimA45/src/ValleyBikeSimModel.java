import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
		case "bikeId":
			inputIsValid = (bikes.containsKey(userInput));
		} 
		
		return inputIsValid;
		
	}
}
