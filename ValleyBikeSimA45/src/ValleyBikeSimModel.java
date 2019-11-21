import java.util.HashMap;
import java.util.HashSet;

/**
 * @author maingo
 *
 */
public class ValleyBikeSimModel {
	
	private HashMap<Station, HashSet<Integer>> stations;
	private HashMap<Rider, ArrayList<PaymentMethod>> paymentMethods;
	private User activeUser;
	private HashSet<Ticket> tickets;
	private HashMap<Rider,HashSet<Ride>> ridesCompleted;
	private HashMap<Rider,HashSet<Ride>> ridesInProgress;
	
	/**
	 * Constructor for the Valley Bike Simulator Model.
	 */
	private ValleyBikeSimModel() {
		
		HashMap<Station, HashSet<Integer>> stations = new HashMap<>();
		HashMap<Rider, ArrayList<PaymentMethod>> paymentMethods; = new HashMap<>()
		User activeUser = null; // what's the default value here and when should it be initialized?
		HashSet<Ticket> tickets = new HashSet<>();
		HashMap<Rider,HashSet<Ride>> ridesCompleted = new HashMap<>();
		HashMap<Rider,HashSet<Ride>> ridesInProgress = new HashMap<>();
		
	} 
}
