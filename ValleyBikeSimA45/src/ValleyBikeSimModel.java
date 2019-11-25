import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
//import java.util.regex.Pattern;

import com.opencsv.CSVReader;

import java.util.Date;
import java.util.regex.Pattern;

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
	private HashMap<Integer, HashSet<Integer>> stationsBikes;

	/** map each rider by username to their payment methods */
	private HashMap<String, ArrayList<PaymentMethod>> paymentMethods;

	/** the currently logged in user */
	private User activeUser;

	/** map all usernames to technical support tickets */
	private HashMap<String, Ticket> tickets;

	/** map all users to their user names. The HashMap contains Admin and Rider objects, 
	 * and requires casting to the proper child class at retrieval. */
	private HashMap<String, User> users;

	/** map all users by username to their emails */
	private HashMap<String, Rider> emails;

	/** map all riders by username to the rides they have completed */
	private HashMap<String, HashSet<Ride>> ridesCompleted;

	/** map all riders by username to the ride they currently have in progress */
	private HashMap<String, Ride> ridesInProgress;

	/** map all riders by username to their membership type */
	private HashMap<String, Membership> memberships;
	
	/**
	 * Constructor for the Valley Bike Simulator Model.
	 */
	public ValleyBikeSimModel() {
		
		stations = new HashMap<>();
		bikes = new HashMap<>();
		stationsBikes = new HashMap<>();
		paymentMethods = new HashMap<>();
		activeUser = null; // what's the default value here and when should it be initialized?
		users = new HashMap<>();
		tickets = new HashMap<>();
		ridesCompleted = new HashMap<>();
		ridesInProgress = new HashMap<>();
		emails = new HashMap<>();
		memberships = new HashMap<>();
	} 
	
	/**
	 * Read in all the data files and store them in appropriate data structures.
	 */
	public void readData() {
		readStationData();
		readBikeData();
		readAdminData();
		readRiderData();
		readTicketData();
		readRidesCompletedData();
		readRidesInProgressData();
		readPaymentMethodData();
	}
	
	/**
	 * Read in the station data file and store stations in the model.
	 */
	private void readStationData() {
		try {
			String stationData = "data-files/station-data.csv";
			
			CSVReader stationDataReader = new CSVReader(new FileReader(stationData));

			
			/* read the CSV data row wise and map stations to station IDs */
			List<String[]> allStationEntries = stationDataReader.readAll();
			
			int counter = 0;
			for(String[] array : allStationEntries) {
				if(counter != 0) {
					
					// create new station object
					Station station = new Station(Integer.parseInt(array[0]), array[1], 
							array[8], Integer.parseInt(array[2]), Integer.parseInt(array[4]), 
							Integer.parseInt(array[6]), Integer.parseInt(array[5]),(array[7].equals("1")));
					
					// map station to its ID number
					stations.put(Integer.parseInt(array[0]), station); 
				}
				counter++;	
			}
			stationDataReader.close();

		} 
		catch(Exception e) {
			System.out.println(e.getMessage());
		}	
	}
	
	
	/**
	 * Read in the bike data file, store bikes in the model, and .
	 */
	private void readBikeData() {
		try {
			String bikeData = "data-files/bike-data.csv";
			CSVReader bikesDataReader = new CSVReader(new FileReader(bikeData));
			
			/* read the CSV data row wise and map bikes to station IDs */
			List<String[]> allBikesEntries = bikesDataReader.readAll();
			
			int counter = 0;
			for(String[] array : allBikesEntries) {
				if(counter != 0) {
					
					int bikeId = Integer.parseInt(array[0]);
					
					// create new bike object
					Bike bike = new Bike(bikeId);
					
					// map bike object to bike ID
					bikes.put(bikeId, bike);
					
					// obtain station ID
					int stationId = Integer.parseInt(array[1]);
					
					// add bike ID to the set of bike IDs at this station
					stationsBikes.putIfAbsent(stationId, new HashSet<Integer>());
					stationsBikes.get(stationId).add(bikeId);	
					
					
				}
				counter++;	
			}
			bikesDataReader.close();
		} 
		catch(Exception e) {
		
			System.out.println(e.getMessage()+ "error!");
		}	
	}
	
	/**
	 * Read in the admin data file, stores information about admins in model.
	 */
	private void readAdminData() {
		try {
			String adminData = "data-files/admins-data.csv";
			CSVReader adminDataReader = new CSVReader(new FileReader(adminData));
			
			/* read the CSV data row wise and map bikes to station IDs */
			List<String[]> allAdminEntries = adminDataReader.readAll();
			
			
			int counter = 0;
			for(String[] array : allAdminEntries) {
				if(counter != 0) {
					
					// create new Admin object
					Admin admin = new Admin(array[0], array[1]);
					
					// map rider object to username
					this.users.put(array[0], admin);
				}
				counter++;	
			}
			adminDataReader.close();
		} 
		
		catch(Exception e) {
			System.out.println(e.getMessage());
		}		
	}
	
	/**
	 * Read in the rider data file, stores information about riders, memberships and emails in model.
	 */
	private void readRiderData() {
		try {
			String riderData = "data-files/rider-data.csv";
			CSVReader riderDataReader = new CSVReader(new FileReader(riderData));
			
			/* read the CSV data row wise and map bikes to station IDs */
			List<String[]> allRiderEntries = riderDataReader.readAll();
			
			
			int counter = 0;
			MembershipFactory membershipFactory = new MembershipFactory();
			for(String[] array : allRiderEntries) {
				if(counter != 0) {
					
					// create new rider object
					Rider rider = new Rider(array[0], array[1], array[2], array[3], array[4], array[5]);
					
					// create membership object
					Membership membership = membershipFactory.getMembership(array[6]);
					
					// map rider object to username
					this.users.put(array[0], rider);
					
					// map email to rider object
					this.emails.put(array[3], rider);
					
					// map rider object to membership object
					this.memberships.put(array[0],  membership);
				}
				counter++;	
			}
			riderDataReader.close();
		} 
		
		catch(Exception e) {
			System.out.println(e.getMessage());
		}	
	}
	
	/**
	 * Read in the tickets data file, stores information about tickets in model.
	 */
	private void readTicketData() {
		try {
			String ticketData = "data-files/ticket-data.csv";
			CSVReader ticketDataReader = new CSVReader(new FileReader(ticketData));
			
			/* read the CSV data row wise and map bikes to station IDs */
			List<String[]> allTicketEntries = ticketDataReader.readAll();
			
			
			int counter = 0;
		
			for(String[] array : allTicketEntries) {
				if(counter != 0) {
					
					// create new ticket object
					Ticket ticket = new Ticket(Integer.parseInt(array[0]), array[1], array[2]);
					
					
					// map user to Ticket
					this.tickets.put(array[2], ticket);
				}
				counter++;	
			}
			ticketDataReader.close();
		} 
		
		catch(Exception e) {
			System.out.println(e.getMessage());
		}	
	}
	
	/**
	 * Reads the rides completed data file, record all the completed rides
	 */
	private void readRidesCompletedData() {
		String ridesCompletedData = "data-files/rides-completed-data.csv";
		
		try {
			CSVReader ridesCompletedDataReader = new CSVReader(new FileReader(ridesCompletedData));
			
			List<String[]> allRidesCompletedEntries = ridesCompletedDataReader.readAll();
			
			
			int counter = 0;
			for(String[] array : allRidesCompletedEntries) {
				if(counter == 0) {
					
				} else {
					// create new Ride object
					Date startTime = toDate(array[4]);
					Ride ride = new Ride(Integer.parseInt(array[1]), this.stations.get(Integer.parseInt(array[2])), startTime);
					ride.setEndStation(this.stations.get(Integer.parseInt(array[3])));
					ride.setEndTime(toDate(array[5]));
					
					// add ride to user's list of completed rides
					String username = array[0];
					ridesCompleted.putIfAbsent(username, new HashSet<>());
					ridesCompleted.get(username).add(ride);
				}
				counter++;
			} 
			ridesCompletedDataReader.close();
		} 
		
		catch (Exception e) {
			System.out.println("\n" + e.getMessage());
		}
	}
	
	/**
	 * Reads the rides in progress data file, record all the rides currently in progress
	 */
	private void readRidesInProgressData() {
		String ridesInProgressData = "data-files/rides-in-progress.csv";
		
		try {
			CSVReader ridesInProgressDataReader = new CSVReader(new FileReader(ridesInProgressData));
			
			List<String[]> allRidesInProgressEntries = ridesInProgressDataReader.readAll();
			
			
			int counter = 0;
			for(String[] array : allRidesInProgressEntries) {
				if(counter == 0) {
					
				} else {
					// create new Ride object with endTime and endStation set to null
					Date startTime = toDate(array[3]);
					Ride ride = new Ride(Integer.parseInt(array[1]), this.stations.get(Integer.parseInt(array[2])), startTime);
					
					ridesInProgress.put(array[0], ride);
				}
				counter++;
	
			} 
			ridesInProgressDataReader.close();
		} 
		
		catch (Exception e) {
			System.out.println("\n" + e.getMessage());
		}
		
		
	}
	
	/**
	 * Reads the payment methods data file, record all payment methods in model.
	 */
	private void readPaymentMethodData() {
		String paymentMethodsData = "data-files/payment-methods-data.csv";
		try {
			CSVReader paymentMethodsDataReader = new CSVReader(new FileReader(paymentMethodsData));
			
			List<String[]> allPaymentMethodsEntries = paymentMethodsDataReader.readAll();
			
			
			int counter = 0;
			for(String[] array : allPaymentMethodsEntries) {
				if(counter == 0) {
					
				} else {
					// create new PaymentMethod object
					PaymentMethod pm = new PaymentMethod(array[1], array[2], array[3], array[4], array[5]);
					
					// map payment method to username of user
					String username = array[0];
					this.paymentMethods.putIfAbsent(username, new ArrayList<>());
					this.paymentMethods.get(username).add(pm);
					
				}
				counter++;
			} 
			paymentMethodsDataReader.close();
		} 
		
		catch (Exception e) {
			System.out.println("\n" + e.getMessage());
		}
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
			inputIsValid = (stations.containsKey(Integer.parseInt(userInput)));
			break;
		case "bikeId":
			inputIsValid = (bikes.containsKey(Integer.parseInt(userInput)));
			break;
		case "loginInfo":
			String[] info = userInput.split(" ");
			inputIsValid = (users.get(info[0]).getPassword().equals(info[1]));
			break;
		case "newUsername":
			inputIsValid = (!users.containsKey(userInput) && userInput.length() >= 6);
			break;
		case "newEmail":
			// regex to validate email format
			Pattern r = Pattern.compile("\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\b"); 
			
			// email is valid if it's in valid format and it does not belong to an existing user
			inputIsValid = (r.matcher(userInput).find() && !emails.containsKey(userInput));
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
		return ridesInProgress.containsKey(activeUser.getUserName());
	}
	
	/**
	 * @return the activeUser
	 */
	public User getActiveUser() {
		return activeUser;
	}

	public boolean bikeIsOverdue() {
		// retrieve the Ride in progress associated with the active user
		Ride ride = ridesInProgress.get(activeUser.getUserName());
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
		this.activeUser = this.users.get(activeUsername);
	}
	
	/**
	 * 
	 * Map a rider to the associated membership.
	 * @param rider			The rider
	 * @param membership	The rider's membership
	 */
	public void setMembership(String username, Membership membership) {
		this.memberships.put(username, membership);
	}
	
	/**
	 * Map the user to the associated username.
	 * @param user the user to add
	 */
	public void addUser(User user) {
		this.users.put(user.getUserName(), user);
	}
	
	/**
	 * Map a rider with their payment methods.
	 * @param rider			The rider
	 * @param paymentMethod	The rider's payment method
	 */
	public void addPaymentMethod(String username, PaymentMethod paymentMethod) {
		// create new list of payment methods if new user
		this.paymentMethods.putIfAbsent(username, new ArrayList<PaymentMethod>());
		
		// add payment method to list of payment methods associated with user
		this.paymentMethods.get(username).add(paymentMethod);
	}
	
	/**
	 * Map an email address to the associated ValleyBike rider.
	 * @param rider			The rider
	 * @param email			The rider's email address
	 */
	public void addEmail(String email,Rider rider) {
	
		// add email associated with user
		this.emails.put(email, rider);
	}
	
	
	/*
	 *
	 * ********* HELPER FUNCTIONS START HERE: ***********
	 *
	 */
	
	/**
	 * Convert a string to Date java object.
	 * @throws ParseException 
	 */
	private static Date toDate(String s) throws ParseException {
		Date dateTime = new SimpleDateFormat("MM/dd/yy HH:mm").parse(s);
		return dateTime;
	}
}
