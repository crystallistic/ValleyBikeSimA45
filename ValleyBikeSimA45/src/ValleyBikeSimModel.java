import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
//import java.util.regex.Pattern;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

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
	private HashMap<String, PaymentMethod> paymentMethods;

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
	
	/** map all riders by username to a ride with a stolen bike */
	private HashMap<String, Ride> ridesOverdue;

	/** map all riders by username to their membership type */
	private HashMap<String, Membership> memberships;
	
	/** map all users by username to ArrayList of all the transactions they've made */
	private HashMap<String, ArrayList<Transaction>> transactionsByUser;
	
	/** used to write .csv files */
	private FileWriter csvWriter;
	
	/** used to write .csv files */
	private CSVWriter writer;
	
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
		ridesOverdue = new HashMap<>();
		emails = new HashMap<>();
		memberships = new HashMap<>();
		transactionsByUser = new HashMap<>();
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
		readRidesOverdueData();
		readPaymentMethodData();
		readTransactionData();
	}
	
	/**
	 * 
	 * 
	 * ******************************* HELPER FUNCTIONS START HERE *********************************
	 * 
	 */
	
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
							array[8], Integer.parseInt(array[3]), Integer.parseInt(array[4]), 
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
					String status = array[2];
					
					// create new bike object
					Bike bike = new Bike(bikeId,status);
					
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
		
		// file path
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
					
					// create Ride object
					Ride ride = new Ride(Integer.parseInt(array[1]), this.stations.get(Integer.parseInt(array[2])), startTime);
					
					// add ride to database of rides currently in progress
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
	 * Reads the rides completed data file, record all the completed rides
	 */
	private void readRidesOverdueData() {
		String ridesOverdueData = "data-files/rides-overdue.csv";
		
		try {
			CSVReader ridesOverdueDataReader = new CSVReader(new FileReader(ridesOverdueData));
			
			List<String[]> allRidesOverdueEntries = ridesOverdueDataReader.readAll();
			
			
			int counter = 0;
			for(String[] array : allRidesOverdueEntries) {
				if(counter == 0) {
					
				} else {
					// create new Ride object
					Date startTime = toDate(array[3]);
					Ride ride = new Ride(Integer.parseInt(array[1]), this.stations.get(Integer.parseInt(array[2])), startTime);
					
					// add ride to user's list of completed rides
					String username = array[0];
					ridesOverdue.put(username, ride);	
				}
				counter++;
			} 
			ridesOverdueDataReader.close();
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
					this.paymentMethods.put(username, pm);
					
				}
				counter++;
			} 
			paymentMethodsDataReader.close();
		} 
		
		catch (Exception e) {
			System.out.println(e.getMessage());		
		}
	}
	
	/**
	 * Read in the transaction data file and store stations in the model.
	 */
	private void readTransactionData() {
		try {
			String transactionData = "data-files/transaction-data.csv";
			
			CSVReader transactionDataReader = new CSVReader(new FileReader(transactionData));
			
			/* read the CSV data row wise and map transactions to usernames */
			List<String[]> allTransactionEntries = transactionDataReader.readAll();
			
			int counter = 0;
			for(String[] array : allTransactionEntries) {
				if(counter != 0) {
					
					// create new transaction object
					String username = array[0];
					BigDecimal amount = new BigDecimal(array[1]);
					Date time = toDate(array[2]);
					String description = array[3];
					
					Transaction transaction = new Transaction(username,amount,time,description);
					
					// map transaction to its user
					transactionsByUser.putIfAbsent(username, new ArrayList<Transaction>());
					transactionsByUser.get(username).add(transaction);
				}
				counter++;	
			}
			transactionDataReader.close();

		} 
		catch(Exception e) {
			System.out.println(e.getMessage());
		}	
	}
	
	
	
	/**
	 * Checks if a credit card is expired by comparing expiration date with current date.
	 * @param expirationDate Expiration date on credit card, of MM/YY format
	 * @return true if card is not expired, else false
	 * @throws ParseException  
	 */
	public boolean activeUserCreditCardExpired() throws ParseException {
		
		// getting first credit card on file. TODO: Verify method to keep track of preferred payment method
		PaymentMethod pm = paymentMethods.get(activeUser.getUserName()); 
		Date date = new SimpleDateFormat("MM/yy").parse(pm.getExpiryDate());
		Date now = new Date();
		
		return (now.getTime() - date.getTime() > 0);
	}
	
	
	/**
	 * 
	 * 
	 * ******************************** END OF HELPER FUNCTIONS ************************************
	 *
	 *
	 */
	
	
	
	
	/**
	 * Reads a ride data file that contains all the rides for one day of service.
	 * After processing the data, returns statistics for the day.
	 * @return true if file is successfully read in, false if file cannot be read.
	 */
	public String readRidesDataFile(String fileName) {
		String rideData = "data-files/" + fileName;
		
		try {
			CSVReader rideDataReader = new CSVReader(new FileReader(rideData));
			List<String[]> allRideEntries = rideDataReader.readAll();
			long totalDuration = 0;
			
			int counter = 0;
			for(String[] array : allRideEntries) {
				if(counter == 0) {
					
				} else {
					
					// add to the total duration
					Date startTime = toDate(array[4]);
					Date endTime = toDate(array[5]);
					totalDuration += endTime.getTime() - startTime.getTime();
					
				}
				counter++;
			} 
			
			rideDataReader.close();
			
			// calculate the average duration of a ride
			long calc = (totalDuration / (allRideEntries.size()-1))/60000;
			int averageDuration = (int) calc;
			return "The ride list contains " + (allRideEntries.size()-1) + " rides and the average ride time is " + averageDuration + " minutes.\n";
			
		} 
		
		catch (Exception e) {
			//System.out.println("\n" + e.getMessage());
			System.out.println("Invalid file name, please try again.");
			
		}
		return "";
	}
	
	/**
	 * Checks user input validity (check for formatting and membership)
	 * @param userInputName 	the user's input type
	 * @param userInput			the user's input
	 * @return true if valid input, else false
	 */
	public boolean isValid(String userInputName, String userInput) {
		
		boolean inputIsValid = false;
		boolean matchRegex = false;
		boolean existInSys = false;
		Pattern r = null;
		Pattern numeric = Pattern.compile("^[0-9]+$");
		
		switch (userInputName) {
		case "stationId":	// valid if is numeric and exists in system	
			matchRegex = numeric.matcher(userInput).find();
			
			if (matchRegex) {
				existInSys = stations.containsKey(Integer.parseInt(userInput));
			}
			
			inputIsValid = (matchRegex && existInSys);
			break;
		case "bikeId":	// valid if is numeric and exists in system	
			matchRegex = numeric.matcher(userInput).find();
			
			if (matchRegex) {
				existInSys = bikes.containsKey(Integer.parseInt(userInput));
			}
			
			inputIsValid = (matchRegex && existInSys);
			break;
		case "loginInfo":
			String[] info = userInput.split(" ");
			inputIsValid = (users.containsKey(info[0]) && users.get(info[0]).getPassword().equals(info[1]));
			break;
		case "newUsername":
			existInSys = users.containsKey(userInput);
			inputIsValid = (!existInSys && userInput.length() >= 6);
			break;
		case "newEmail":
			// regex to validate email format
			r = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"); 
			
			// email is valid if it's in valid format and it does not belong to an existing user
			matchRegex = r.matcher(userInput).find();
			existInSys = emails.containsKey(userInput);
			inputIsValid = (matchRegex && !existInSys);
			break;
		case "newStationId":	
			// Assumption: Valley Bike's station IDs are two-digit and only within the 01-99 range.
			r = Pattern.compile("^[0-9]{2}$"); 
			matchRegex = r.matcher(userInput).find();
			
			// only check to see if the station ID exists in the system if input is numeric
			if (matchRegex) {
				existInSys = stations.containsKey(Integer.parseInt(userInput));
			}
			
			// new station ID is valid if it's 2-digit and has not appeared in the system.
			inputIsValid = (matchRegex && !existInSys);
			break;
		case "newStationName":	
			
			userInput = userInput.trim();
			// new station name must not coincide with existing station names
			for (Station station : stations.values()) {
				if (station.getStationName().equalsIgnoreCase(userInput)) {
					existInSys = true;
				}
			}
			
			// a new station name is valid if it doesn't already exist in the system
			inputIsValid = !existInSys;
			break;
		case "newStationAddress":	
			r = Pattern.compile("^([a-zA-Z0-9 .'\\/#-]+)," // address line 1
								+ "([a-zA-Z0-9 \\/#.'-]+,)*" // address line 2 (optional)
								+ "([a-zA-Z .'-]+)," // city
								+ "([a-zA-Z0-9 .'\\/#-]+)," // state
								+ " *([0-9]{5}) *," // zip code
								+ " *([a-zA-Z .,'-]+)$");  // country)
			matchRegex = r.matcher(userInput).find();
			
			// new station address must not coincide with existing station address
			for (Station station : stations.values()) {
				if (station.getAddress() == userInput) {
					existInSys = true;
				}
			}
			
			inputIsValid = (matchRegex && !existInSys);
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

	/**
	 * Checks if bike is overdue by 24hrs
	 * @return true if overdue, else false
	 */
	public boolean bikeIsOverdue() {
		// retrieve the Ride in progress associated with the active user
		Ride ride = ridesInProgress.get(activeUser.getUserName());
		Date currentTime = new Date(); // get current time
		Date startTime = ride.getStartTime(); // get ride start time
		long difference = (currentTime.getTime() - startTime.getTime()) / 1000; // difference in seconds
		
		return (difference >= 86400);
	}
	
	/**
	 * Charges the user the $2000.00 lost bike fee + overtime fee, and return this total.
	 * @return the total amount billed to the user account
	 */
	public BigDecimal chargeOverdue() {
		
		BigDecimal amountCharged = new BigDecimal("2000.00");
		
		// calculate overtime
		// TODO: 
		
		// charge the user
		
		return amountCharged;
	}

	/**
	 * @param activeUser the activeUser to set
	 */
	public void setActiveUser(String activeUsername) {
		if (activeUsername == null) {
			this.activeUser = null;
		} else {
			this.activeUser = this.users.get(activeUsername);
		}
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

		// associate payment method with user
		this.paymentMethods.put(username,paymentMethod);
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
	
	/**
	 * Removes the checked out bike from the start station,
	 * and creates a new ride for the user.
	 * @param bikeId the id of the bike being checked out
	 * @param stationId the id of the station the bike is being removed from
	 */
	public void startRide(int bikeId, int stationId) {
		//Remove the bikeId from the HashSet of bikeIds associated to the station in stations HashMap
		stationsBikes.get(stationId).remove(bikeId);
		
		//Update numFreeDocks in startStation to reflect one new free dock and one fewer bike
		Station startStation = stations.get(stationId);
		startStation.setNumFreeDocks(startStation.getNumFreeDocks()+1);
		startStation.setNumBikes(startStation.getNumBikes()-1);
		
		//Creates new Ride object with bikeId and start time --> get current time 
		Ride ride = new Ride(bikeId, startStation, new Date() );
		
		//Add ride and activeUser to ridesInProgress
		ridesInProgress.put(activeUser.getUserName(), ride);
		
		//Updates bike file
		saveBikeList();
		//Updates station file
		saveStationList();
		//Appends to rides in progress
		saveAllRideInProgress(ride,activeUser.getUserName());
	}

	/**
	 * Charges the user for the ride and updates the model to reflect
	 * the bike being added to the end station
	 * and the ride object being updated
	 * @param stationId the id of the station that the bike is being returned to
	 * @return the amount that the user has been charged
	 */
	public BigDecimal endRide (int stationId) {
		String activeUsername = activeUser.getUserName(); //active User's username
		Membership membership = memberships.get(activeUsername); //user's membership
	
		Date now = new Date(); //current time
		Ride ride = ridesInProgress.get(activeUsername); //Ride being completed
		PaymentMethod paymentMethod = paymentMethods.get(activeUsername); //active user's payment method
		Station endStation = stations.get(stationId); //station where the bike is being returned
		
		//Charge user for the completed ride
		int rideDuration = (int)(now.getTime() - ride.getStartTime().getTime()) / 60000;
		BigDecimal chargeAmount = membership.getChargeForRide(rideDuration);
		paymentMethod.chargeCard(chargeAmount);
		
		//Create new Transaction and add to list
		Transaction transaction = new Transaction(activeUser.getUserName(),chargeAmount,now,"Ride");
		transactionsByUser.get(activeUser.getUserName()).add(transaction);
		
		//Update bike list at current Station
		stationsBikes.get(stationId).add(ride.getBikeId());
		
		//Update station information
		endStation.setNumFreeDocks(endStation.getNumFreeDocks()-1);
		endStation.setNumBikes(endStation.getNumBikes()+1);
		
		//Add end time and end station to Ride associated to User
		ride.setEndTime(now);
		ride.setEndStation(endStation);
		
		//Move the ride from ridesInProgress to ridesCompleted
		if (ridesCompleted.containsKey(activeUsername)) {
			ridesCompleted.get(activeUsername).add(ride);
		} else {
			HashSet<Ride> activeUserRides = new HashSet<>();
			activeUserRides.add(ride);
			ridesCompleted.put(activeUsername, activeUserRides);
		}
		ridesInProgress.remove(activeUsername);
		
		//Update rides-in-progress file
		saveRidesInProgressList();
		
		//Append to rides-completed file
		saveAllRideCompleted(ride, activeUser.getUserName());
		
		//Update file for rides today
		saveRideToday(ride);
		
		//Update bike file
		saveBikeList();
		
		//Update station file
		saveStationList();
		
		//Append to transactions file
		saveAllTransaction(transaction);
	
		//Return the amount charged to the user's card
		return chargeAmount;
	}

	/**
	 * Creates a new Station object and adds it to the list
	 * @param stationId 	The station's ID
	 * @param stationName	The station's name
	 * @param address		The station's address
	 * @param capacity		The station's capacity
	 * @param hasKiosk		True if the station has a kiosk, else false
	 */
	public Station addStation(int stationId, String stationName, String address, int capacity, boolean hasKiosk) {
		//creates a new station object
		//passes in 0 for numBikes and maintenanceRequests
		//passes in the capacity for numFreeDocks
		
		Station station = new Station(stationId,stationName,address,0,capacity,capacity,0,hasKiosk);
		
		stations.put(stationId, station);
		HashSet<Integer> bikes = new HashSet<Integer>();
		stationsBikes.put(stationId,bikes);
		return station;
	}
	
	/**
	 * Equally divides all the bikes between stations
	 * to avoid stations being under- or over-occupied
	 */
	public void equalizeStations() {
		int totalBikes = bikes.size();
		int totalCapacity = 0;
		Station[] stationArray = stations.values().toArray(new Station[0]);
		
		//maps station IDs to number of bikes needed (can be negative)
		ArrayList<ArrayList<Integer>> needBikes = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> extraBikes = new ArrayList<Integer>();
		
		//Calculate the number of bikes that can be accommodated by the stations
		for (Station station : stationArray) {
			totalCapacity += station.getCapacity();
		}
		
		//Loop through stations removing extra bikes and adding them to a list
		for (Station station : stationArray) {
			int stationId = station.getStationId();
			int numBikesHere = stationsBikes.get(stationId).size();
			int capacityHere = station.getCapacity();
			int idealNumBikes = Math.round(capacityHere * totalBikes/totalCapacity);
			
			//If a station has too few bikes, store how many more are needed
			if (numBikesHere < idealNumBikes) {
				ArrayList<Integer> need = new ArrayList<Integer>();
				need.add(stationId);
				need.add(idealNumBikes - numBikesHere);
				
				//Add station and the number of bikes it needs to needBikes
				needBikes.add(need);
				
			} 
			
			//If a station has too many bikes, remove the extras and store them
			else if (numBikesHere > idealNumBikes) {
				Integer[] bikeArray = stationsBikes.get(stationId).toArray(new Integer[0]);
				int numBikesAboveIdeal = numBikesHere - idealNumBikes;
				
				for (int i=0; i<numBikesAboveIdeal; i++) {
					Integer bikeId = bikeArray[i];
					stationsBikes.get(stationId).remove(bikeId);
					
					//Add extra bike to extraBikes
					
					extraBikes.add(bikeId);
				}
				station.setNumBikes(idealNumBikes);
	
				//Update Station data to reflect loss
				station.setNumFreeDocks(station.getNumFreeDocks() + numBikesAboveIdeal);
			}
		}
		
		System.out.println(extraBikes.size());
		
		//Loop through the stations in need, giving them extra bikes
		for (ArrayList<Integer> need : needBikes) {
			int stationId = need.get(0);
			int numNeeded = need.get(1);
			Station station = stations.get(stationId);
			HashSet<Integer> bikeSet = stationsBikes.get(stationId);
			
			//Remove bikeIds from extraBikes and add them to the station's bikeSet
			for (int i=0; i<numNeeded; i++) {
				bikeSet.add(extraBikes.remove(0));
			}
			station.setNumBikes(station.getNumBikes() + numNeeded);
			//Update Station data to reflect the gain
			station.setNumFreeDocks(station.getNumFreeDocks() - numNeeded);
		}
		System.out.println(extraBikes.size());
		
		int indexStation = 0;
		while (extraBikes.size() > 0) {
			Station s = stationArray[indexStation];
			s.setNumBikes(s.getNumBikes() + 1);
			s.setNumFreeDocks(s.getNumFreeDocks() - 1);
			stationsBikes.get(s.getStationId()).add(extraBikes.remove(0));
			indexStation++;
		}
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
	
	/**
	 * Returns the full list of all the stations within the Valley Bike system.
	 */
	public ArrayList<String> getStationList() {
		ArrayList<String> formattedStationList = new ArrayList<>();
		formattedStationList.add("ID\tBikes\tAvDocs\tMainReq\tCap\tKiosk\tName - Address\n");
		Object[] stationIds = stations.keySet().toArray();
		Arrays.sort(stationIds);
		
		for (Object stationId : stationIds) {	
			formattedStationList.add(stations.get((Integer)stationId).toString());
		}
		
		return formattedStationList;
	}
	

	/*
	 * Helper method for JUnit testing. Returns Station object corresponding to the id
	 */
	public Station getStation(int id) {
		return stations.get(id);
	}

	/**
	 * Check station to see if all the docks are full.
	 * @param stationId		station ID
	 * @return boolean 
	 */
	public boolean isStationDockFull(int stationId) {

		Station station = stations.get(stationId);
		return (station.getNumFreeDocks() == 0);
	}
	
	public void saveData() {
		saveStationList();
		saveUserLists();
		saveBikeList();
		savePaymentMethodList();
		saveTicketList();
		//saveRideLists();
		//saveRidesToday();
	}
	
	/**
	 * Save a transaction to the CSV file
	 * @param transaction
	 */
	private void saveAllTransaction(Transaction transaction) {
		try {
			csvWriter = new FileWriter("data-files/transaction-data.csv",true);

			//adding all the ride details into the csv
			
			csvWriter.append(transaction.getUsername());
			csvWriter.append(",");
			csvWriter.append(""+transaction.getAmount());
			csvWriter.append(",");
			csvWriter.append(transaction.getTime().toString());
			csvWriter.append(",");
			csvWriter.append(transaction.getDescription());
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save the updated ride lists to the CSV files, by overwriting all the entries and adding new entries for the new rides
	 */
	private void saveRideLists() {
		// TODO Auto-generated method stub
		saveRidesInProgressList();
	}
	
	/**
	 * Save the updated rides in progress lists to the CSV file, by overwriting all the entries and adding new entries for the new rides
	 */
	private void saveRidesInProgressList() {
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/rides-in-progress.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "username,bikeId,From,Start".split(",");
		      writer.writeNext(record);

		      writer.close();
		      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//loops through and saves all rides
		for (String username: ridesInProgress.keySet()) {
			Ride ride = ridesInProgress.get(username);
			saveAllRideInProgress(ride, username);
		}
	}
	
	/**
	 * Save a ride in progress to the CSV file
	 * @param ride
	 * @param username
	 */
	private void saveAllRideInProgress(Ride ride, String username) {
		try {
			csvWriter = new FileWriter("data-files/rides-in-progress.csv",true);

			//adding all the ride details into the csv
			
			csvWriter.append(""+username);
			csvWriter.append(",");
			csvWriter.append(""+ride.getBikeId());
			csvWriter.append(",");
			csvWriter.append(ride.getStartStation().getStationName());
			csvWriter.append(",");
			csvWriter.append(ride.getStartTime().toString());
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save a ride to the current daily rides file
	 * @param ride
	 */
	private void saveRideToday(Ride ride) {
		// Date format: dow mon dd hh:mm:ss zzz yyyy
		Date now = new Date();
		String month = now.toString().substring(4,7);
		String day = now.toString().substring(8,10);
		String year = now.toString().substring(24,28);
		String filename = "rides-"+month+"-"+day+"-"+year+".csv";
		File ridesToday = new File(filename);

		//either adds the ride to the appropriate file or creates the file
		if (!ridesToday.exists()) {
			try {
				//overwrites existing file with new data
				csvWriter = new FileWriter(filename);
				writer = new CSVWriter(csvWriter);
				String [] record = "username,bikeId,From,To,Start,End".split(",");
				writer.writeNext(record);

				writer.close();
					      
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		saveAllRideToday(ride, filename);
	}
	
	/**
	 * Save a current ride to the CSV file
	 * @param ride
	 * @param filename
	 */
	private void saveAllRideToday(Ride ride, String filename) {
		try {
			csvWriter = new FileWriter(filename,true);

			//adding all the ride details into the csv
			
			csvWriter.append(""+activeUser.getUserName());
			csvWriter.append(",");
			csvWriter.append(""+ride.getBikeId());
			csvWriter.append(",");
			csvWriter.append(ride.getStartStation().getStationName());
			csvWriter.append(",");
			csvWriter.append(ride.getEndStation().getStationName());
			csvWriter.append(",");
			csvWriter.append(ride.getStartTime().toString());
			csvWriter.append(",");
			csvWriter.append(ride.getEndTime().toString());
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save an overdue ride to the CSV file
	 * @param ride
	 * @param username
	 */
	private void saveAllRideOverdue(Ride ride, String username) {
		try {
			csvWriter = new FileWriter("data-files/rides-overdue.csv",true);

			//adding all the ride details into the csv
			
			csvWriter.append(""+username);
			csvWriter.append(",");
			csvWriter.append(""+ride.getBikeId());
			csvWriter.append(",");
			csvWriter.append(ride.getStartStation().getStationName());
			csvWriter.append(",");
			csvWriter.append(ride.getStartTime().toString());
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save a completed ride to the CSV file
	 * @param ride
	 * @param username
	 */
	private void saveAllRideCompleted(Ride ride, String username) {
		try {
			csvWriter = new FileWriter("data-files/rides-completed-data.csv",true);

			//adding all the ride details into the csv
			
			csvWriter.append(""+username);
			csvWriter.append(",");
			csvWriter.append(""+ride.getBikeId());
			csvWriter.append(",");
			csvWriter.append(ride.getStartStation().getStationName());
			csvWriter.append(",");
			csvWriter.append(ride.getEndStation().getStationName());
			csvWriter.append(",");
			csvWriter.append(ride.getStartTime().toString());
			csvWriter.append(",");
			csvWriter.append(ride.getEndTime().toString());
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save the updated bike list to the CSV file, by overwriting all the entries and adding new entries for the new stations. 
	 */
	public void saveTicketList() {     
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/ticket-data.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "ticketIds,description,username".split(",");
		      writer.writeNext(record);

		      writer.close();
		      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Ticket[] ticketsArray = tickets.values().toArray(new Ticket[0]);	
		//loops through and saves all paymentMethods
		for (Ticket ticket : ticketsArray) {
			saveAllTicket(ticket);
		}
	}
	
	/**
	 * Ancillary function to assist the saveBikeList() function.
	 */
	private void saveAllTicket(Ticket ticket) {
		try {
			csvWriter = new FileWriter("data-files/ticket-data.csv",true);

			//adding all the bike details into the csv
			
			csvWriter.append(""+ticket.getTicketId());
			csvWriter.append(",");
			csvWriter.append(ticket.getDescription());
			csvWriter.append(",");
			csvWriter.append(ticket.getUsername());
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save the updated payment method list to the CSV file, by overwriting all the entries and adding new entries for the new stations. 
	 */
	public void savePaymentMethodList() {     
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/payment-methods-data.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "username,billingName,cardNumber,billingAddress,expiryDate,cvv".split(",");

		      writer.writeNext(record);

		      writer.close();
		      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] usernamesArray = paymentMethods.keySet().toArray(new String[0]);	
		//loops through and saves all paymentMethods
		for (String username : usernamesArray) {
			PaymentMethod paymentMethod = paymentMethods.get(username);
			saveAllPaymentMethod(username, paymentMethod);
		}
	}
	
	/**
	 * Ancillary function to assist the savePaymentMethodList() function.
	 */
	private void saveAllPaymentMethod(String username, PaymentMethod paymentMethod) {
		try {
			csvWriter = new FileWriter("data-files/payment-methods-data.csv",true);

			//adding all the bike details into the csv
			
			csvWriter.append(username);
			csvWriter.append(",");
			csvWriter.append(paymentMethod.getBillingName());
			csvWriter.append(",");
			csvWriter.append(paymentMethod.getCardNumber());
			csvWriter.append(",");
			csvWriter.append(paymentMethod.getAddress());
			csvWriter.append(",");
			csvWriter.append(paymentMethod.getExpiryDate());
			csvWriter.append(",");
			csvWriter.append(paymentMethod.getCvv());
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save the updated bike list to the CSV file, by overwriting all the entries and adding new entries for the new stations. 
	 */
	public void saveBikeList() {     
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/bike-data.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "bikeId,stationId".split(",");

		      writer.writeNext(record);

		      writer.close();
		      
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Save all bikes out of order with station -1
		for (int bikeId : bikes.keySet()) {
			Bike bike = bikes.get(bikeId);
			if (bike.getStatus().equals("OOO")) {
				saveAllBike(bikeId, -1);
			}
		}
		
		//Save all bikes currently being ridden with station 0
		for (Ride ride : ridesInProgress.values()) {
			int bikeId = ride.getBikeId();
			saveAllBike(bikeId,0);
		}
		
		//Save all bikes currently at a station
		for (int stationId : stationsBikes.keySet()) {
			for (int bikeId : stationsBikes.get(stationId)) {
				saveAllBike(bikeId, stationId);
			}
		}
	}
	
	/**
	 * Ancillary function to assist the saveBikeList() function.
	 */
	private void saveAllBike(int bikeId, int stationId) {
		try {
			csvWriter = new FileWriter("data-files/bike-data.csv",true);

			//adding all the bike details into the csv
			
			csvWriter.append(Integer.toString(bikeId));
			csvWriter.append(",");
			csvWriter.append(Integer.toString(stationId));
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save the Admins and Riders to their respective files
	 */
	public void saveUserLists() {
		User[] usersArray = users.values().toArray(new User[0]);
		ArrayList<Admin> admins = new ArrayList<Admin>();
		ArrayList<Rider> riders = new ArrayList<Rider>();
		for (User user : usersArray) {
			if (user instanceof Admin) {
				admins.add((Admin)user);
			} else {
				riders.add((Rider)user);
			}
		}
		saveAdminList(admins);
		saveRiderList(riders);
	}
	
	
	private void saveRiderList(ArrayList<Rider> riders) {
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/rider-data.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "username,password,fullname,email,phoneNumber,address,membershipType".split(",");
		      
		      writer.writeNext(record);

		      writer.close();
		      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//loops through and saves all stations
		for (Rider rider : riders) {
			saveAllRider(rider);
		}
		
	}
	
	private void saveAdminList(ArrayList<Admin> admins) {
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/admins-data.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "username,password,fullname,email,phoneNumber,address,membershipType".split(",");
		      
		      writer.writeNext(record);

		      writer.close();
		      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//loops through and saves all stations
		for (Admin admin: admins) {
			saveAllAdmin(admin);
		}
		
	}
	
	/**
	 * Ancillary function to assist the saveRiderList() function.
	 */
	private void saveAllRider(Rider rider) {
		try {
			csvWriter = new FileWriter("data-files/rider-data.csv",true);

			//adding all the rider details into the csv
			csvWriter.append(rider.getUserName());
			csvWriter.append(",");
			csvWriter.append(rider.getPassword());
			csvWriter.append(",");
			csvWriter.append(rider.getFullName());
			csvWriter.append(",");
			csvWriter.append(rider.getPhoneNumber());
			csvWriter.append(",");
			csvWriter.append(rider.getAddress());
			csvWriter.append(",");
			
			String membershipType;
			Membership membership = memberships.get(rider.getUserName());
			if (membership instanceof DayPass) {
				membershipType = "DayPass";
			} else if (membership instanceof FoundingMember) {
				membershipType = "FoundingMember";
			} else if (membership instanceof Monthly) {
				membershipType = "Monthly";
			} else if (membership instanceof Yearly) {
				membershipType = "Yearly";
			} else {
				membershipType = "PayPerRide";
			}
			
			csvWriter.append(membershipType);
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ancillary function to assist the saveAdminList() function.
	 */
	private void saveAllAdmin(Admin admin) {
		try {
			csvWriter = new FileWriter("data-files/admins-data.csv",true);

			//adding all the admin details into the csv
			csvWriter.append(admin.getUserName());
			csvWriter.append(",");
			csvWriter.append(admin.getPassword());
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save the updated station list to the CSV file, by overwriting all the entries and adding new entries for the new stations. 
	 */
	public void saveStationList() {     
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/station-data.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "ID,Name,Bikes,Pedelecs,Available Docks,Maintainence Request,Capacity,Kiosk,Address".split(",");

		      writer.writeNext(record);

		      writer.close();
		      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Station[] stationsArray = stations.values().toArray(new Station[0]);
		//loops through and saves all stations
		for (Station station : stationsArray) {
			saveAllStation(station);
		}
	}
	
	/**
	 * Ancillary function to assist the saveStationList() function.
	 */
	private void saveAllStation(Station station) {
		try {
			csvWriter = new FileWriter("data-files/station-data.csv",true);

			//adding all the station details into the csv
			csvWriter.append(Integer.toString(station.getStationId()));
		    csvWriter.append(',');
			csvWriter.append(station.getStationName());
			csvWriter.append(',');
			csvWriter.append("0");
			csvWriter.append(',');
			
			int numBikes = stationsBikes.get(station.getStationId()).size();
			csvWriter.append(Integer.toString(numBikes));
			csvWriter.append(',');
			
			csvWriter.append(Integer.toString(station.getNumFreeDocks()));
			csvWriter.append(',');
			csvWriter.append(Integer.toString(station.getMaintenanceReqs()));
			csvWriter.append(',');
			csvWriter.append(Integer.toString(station.getCapacity()));
			csvWriter.append(',');
			
			String kioskState;
			if (station.isHasKiosk()) {
				kioskState = "1";
			} else {
				kioskState = "0";
			}
			csvWriter.append(kioskState);
			
			
			csvWriter.append(',');
			csvWriter.append(station.getAddress());
			
			
			csvWriter.append("\n");



			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check all current rides for overdue/stolen bikes, and charge the users
	 */
	public void checkStolenBikes() {
		ArrayList<String> overdueUsernames = new ArrayList<String>();
		for (String username : ridesInProgress.keySet()) {
			Ride ride = ridesInProgress.get(username);
			Date currentTime = new Date(); // get current time
			Date startTime = ride.getStartTime(); // get ride start time
			long difference = (currentTime.getTime() - startTime.getTime()) / 1000; // difference in seconds
			difference = difference/60/60; //difference in hours
			
			if (difference >= 24) {
				//remove bike from the database
				int bikeId = ride.getBikeId();
				bikes.remove(bikeId);
				
				overdueUsernames.add(username);
				
				//charge user for stolen bike
				PaymentMethod paymentMethod = paymentMethods.get(username);
				paymentMethod.chargeCard(new BigDecimal(2000.00));
			}
		}
		for (int i=0; i<overdueUsernames.size(); i++) {
			//move ride to ridesOverdue
			String username = overdueUsernames.get(i);
			ridesOverdue.put(username,ridesInProgress.remove(username));
		}
		
	}

	/**
	 * Checks whether the active user has stolen a bike
	 * @return true if the user has stolen a bike
	 */
	public boolean activeUserStolenBike() {
		return ridesOverdue.containsKey(activeUser.getUserName());
	}
	
	/**
	 * Checks whether the station list is empty
	 * @return true if the station list is empty, else false
	 */
	public boolean noStationInSys() {
		return (stations.size() == 0);
	}

	/**
	 * Removes a station from the system, moves all bikes from this station to storage, 
	 * and updates data file accordingly.
	 * @param stationId The ID of the station to be removed. 
	 */
	public void removeStation(int stationId) {
		
		// move all bikes at this station to storage
		HashSet<Integer> bikeIdsAtStation = stationsBikes.get(stationId);
		for (Integer bikeId : bikeIdsAtStation) {
			bikes.get(bikeId).setStatus("inStorage");
		}
		
		// remove station from station list
		stations.remove(stationId);
		
		// update stations data file to reflect the change
		saveStationList();
		
		
	}

	
}
