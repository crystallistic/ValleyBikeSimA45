import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.util.regex.Pattern;

/**
 * @author maingo, jemimahcharles, maggieburkart, emmatanur
 *
 * Model component of Valley Bike system. 
 * Stores and modifies system data
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

	/** map all tickets to their ids */
	private HashMap<Integer,Ticket> tickets;
	
	/** map all usernames to technical support tickets */
	private HashMap<String, HashSet<Ticket>> usersTickets;

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
	
	/** stores a list of users that should be charged on that day for each calendar day */
	private ArrayList<HashSet<Rider>> billingCycleMonthly;
	
	/** maps all riders by username to the date they should be charged*/
	private HashMap<String, String> chargeDates;
	
	
	/**
	 * Constructor for the Valley Bike Simulator Model.
	 */
	public ValleyBikeSimModel() {
		
		stations = new HashMap<>();
		bikes = new HashMap<>();
		stationsBikes = new HashMap<>();
		paymentMethods = new HashMap<>();
		activeUser = null;
		users = new HashMap<>();
		tickets = new HashMap<>();
		usersTickets = new HashMap<>();
		ridesCompleted = new HashMap<>();
		ridesInProgress = new HashMap<>();
		ridesOverdue = new HashMap<>();
		emails = new HashMap<>();
		memberships = new HashMap<>();
		transactionsByUser = new HashMap<>();
		billingCycleMonthly = new ArrayList<>();
		for (int i=0; i<32; i++) {
			billingCycleMonthly.add(new HashSet<Rider>());
		}
		chargeDates = new HashMap<>();
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
					
					// create new station object. It has no bikes yet - this info will correspond to bike-data file
					Station station = new Station(Integer.parseInt(array[0]), array[1], 
							array[8], Integer.parseInt(array[6]), Integer.parseInt(array[5]),(array[7].equals("1")));

					// map station to its ID number
					stations.put(Integer.parseInt(array[0]), station); 
					stationsBikes.put(Integer.parseInt(array[0]), new HashSet<Integer>());
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
	 * Read in the bike data file, store bikes in the model.
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
					
					// add bike IDs to the set of bike IDs at corresponding stations 
					// if they are not in storage / OOO / out on a ride
					if (stationId > 0) {
						stationsBikes.get(stationId).add(bikeId);	
					}	
					
				}
				counter++;	
			}
			bikesDataReader.close();
		} 
		catch(Exception e) {
		
			System.out.println(e.getMessage()+ " error in readBikeData!");
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
					
					// add rider to billingCycleMonthly according to date
					if (array[6].contentEquals("Monthly")) {
						int day = Integer.parseInt(array[7].substring(3,5));
						billingCycleMonthly.get(day).add(rider);
					}
					
					// add rider's charge date to chargeDages
					chargeDates.put(array[0], array[7]);
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
					Ticket ticket = new Ticket(Integer.parseInt(array[0]), array[1], array[2],array[3],
							(array[4].equals("Yes")), array[5]);
					
					// map Ticket object to ticket ID
					this.tickets.put(Integer.parseInt(array[0]), ticket);

					// map Ticket object to username
					this.usersTickets.putIfAbsent(array[1], new HashSet<Ticket>());
					this.usersTickets.get(array[1]).add(ticket);
					
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
					Date startTime = toDate(array[6]);
					Ride ride = new Ride(Integer.parseInt(array[1]), Integer.parseInt(array[2]), array[3], startTime);
					ride.setEndStationId(Integer.parseInt(array[4]));
					ride.setEndStationName(array[5]);
					ride.setEndTime(toDate(array[7]));
					
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
					Date startTime = toDate(array[4]);
					
					// create Ride object
					Ride ride = new Ride(Integer.parseInt(array[1]), Integer.parseInt(array[2]), array[3], startTime);
					
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
	 * Reads the rides overdue data file, record all the rides overdue
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
					
					// if line is not empty, read line
					if (array.length > 0) {
						// create new Ride object
						Date startTime = toDate(array[4]);
						Ride ride = new Ride(Integer.parseInt(array[1]), Integer.parseInt(array[2]), array[3], startTime);
						
						// add ride to user's list of completed rides
						String username = array[0];
						ridesOverdue.put(username, ride);
					}
						
				}
				counter++;
			} 
			ridesOverdueDataReader.close();
		} 
		
		catch (Exception e) {
			System.out.println(e.getMessage() + " error in readRidesOverdueData()");
		}
	}
	
	/**
	 * Reads the payment methods data file, record all payment methods in model
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
	 * Read in the transaction data file and store transactions in the model
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
		
		// getting first credit card on file.
		PaymentMethod pm = paymentMethods.get(activeUser.getUsername()); 
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
	 * Checks user input validity (check for formatting and membership)
	 * @param userInputName 	the user's input type (stationID, bikeID, etc...)
	 * @param userInput			the user's input
	 * @return true if valid input, else false
	 */
	public boolean isValid(String userInputName, String userInput) {
		
		boolean inputIsValid = false;
		boolean matchRegex = false;
		boolean existInSys = false;
		HashMap<String,Pattern> regex = new HashMap<>(); 
		regex.put("stationId", Pattern.compile("^(0*[1-9]|[1-9][0-9])$")); // accept values between 1-99
		regex.put("bikeId", Pattern.compile("^[0-9]{3}$"));	// 3 digits 
		regex.put("newUsername", Pattern.compile("^[a-zA-Z0-9]{6,}$")); // lowercase + uppercase + nummbers, min 6 chars
		regex.put("newStationName", Pattern.compile("^[^ ]+.*$")); // must contain at least 1 non-whitespace character
		regex.put("newEmail", Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}$"));
		regex.put("stationAddress", Pattern.compile("^([a-zA-Z0-9 .'\\/#-]+)," // address line 1
													+ "([a-zA-Z0-9 \\/#.'-]+,)*" // address line 2
													+ "([a-zA-Z .'-]+),"		// city
													+ "([a-zA-Z0-9 .'\\/#-]+),"	// state
													+ " *([0-9]{5}) *$"));	// zip code
		regex.put("ticketId", Pattern.compile("^[0-9]+$"));
		
		switch (userInputName) {
		case "stationId":	// valid if is numeric and exists in system	
			matchRegex = regex.get(userInputName).matcher(userInput).find();
			
			if (matchRegex) {
				existInSys = stations.containsKey(Integer.parseInt(userInput));
			}
			
			inputIsValid = (matchRegex && existInSys);
			break;
		case "bikeId":	// valid if is numeric and exists in system	
			matchRegex = regex.get(userInputName).matcher(userInput).find();
			
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
			//is valid if only contains lowercase, uppercase letter, digits, min 6 characters and 
			// doesn't exist in system
			matchRegex = regex.get(userInputName).matcher(userInput).find();
			existInSys = users.containsKey(userInput);
			inputIsValid = (matchRegex && !existInSys);
			break;
		case "newEmail":
			// email is valid if it's in valid format and it does not belong to an existing user
			matchRegex = regex.get(userInputName).matcher(userInput).find();
			existInSys = emails.containsKey(userInput);
			inputIsValid = (matchRegex && !existInSys);
			break;
		case "newStationId":	
			// Assumption: Valley Bike's station IDs are two-digit and only within the 01-99 range.
			matchRegex = regex.get("stationId").matcher(userInput).find();
			
			// only check to see if the station ID exists in the system if input is numeric
			if (matchRegex) {
				existInSys = stations.containsKey(Integer.parseInt(userInput));
			}
			
			// new station ID is valid if it's 2-digit and has not appeared in the system.
			inputIsValid = (matchRegex && !existInSys);
			break;
		case "newStationName":	
			
			matchRegex = regex.get(userInputName).matcher(userInput).find();
			userInput = userInput.trim();
			String userInputWithoutSpaces = userInput.replaceAll(" ", "");
			
			// new station name must not coincide with existing station names, even with different spacing
			for (Station station : stations.values()) {
				if (station.getStationName().replaceAll(" ", "").equalsIgnoreCase(userInputWithoutSpaces)) {
					existInSys = true;
				}
			}
			
			// a new station name is valid if it doesn't already exist in the system
			inputIsValid = (matchRegex && !existInSys);
			break;
		case "newStationAddress":	
			matchRegex = regex.get("stationAddress").matcher(userInput).find();
			
			// new station address must not coincide with existing station address
			for (Station station : stations.values()) {
				if (station.getAddress() == userInput) {
					existInSys = true;
				}
			}
			
			inputIsValid = (matchRegex && !existInSys);
			break;
		case "newBikeId":	// valid if is numeric, has 3 digits and doesn't exist in system	
			matchRegex = regex.get("bikeId").matcher(userInput).find();
			
			if (matchRegex) {
				existInSys = bikes.containsKey(Integer.parseInt(userInput));
			}
			
			inputIsValid = (matchRegex && !existInSys);
			break;
		case "bikeIdInStorage": // valid if bike ID exists in system and bike is in storage
			matchRegex = regex.get("bikeId").matcher(userInput).find();
			
			if (matchRegex) {
				existInSys = bikes.containsKey(Integer.parseInt(userInput));
			}
			
			inputIsValid = (matchRegex && existInSys && 
					bikes.get(Integer.parseInt(userInput)).getStatus().equals("inStorage"));
			break;
		case "removeBikeId": // valid if it is a 3 digit number, exists in system and is not being used by anyone.
			matchRegex = regex.get("bikeId").matcher(userInput).find();
			
			if (matchRegex) {
				existInSys = bikes.containsKey(Integer.parseInt(userInput));
			}
			
			// check to see if bike is in use
			boolean bikeInUse = false;
			for (Ride ride : ridesInProgress.values()) {
				if (ride.getBikeId() == Integer.parseInt(userInput)) {
					bikeInUse = true;
				}
			}
			inputIsValid = (matchRegex && existInSys && !bikeInUse);
		case "ticketId": // valid if it is a number and exists in the system and has not been resolved
			matchRegex = regex.get(userInputName).matcher(userInput).find();
			
			boolean isResolved = true;
			if (matchRegex) {
				existInSys = tickets.containsKey(Integer.parseInt(userInput));
				if (existInSys) {
					isResolved = tickets.get(Integer.parseInt(userInput)).isResolved();
				}
			}
			inputIsValid = (matchRegex && existInSys && !isResolved);
			
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
		return ridesInProgress.containsKey(activeUser.getUsername());
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
		Ride ride = ridesInProgress.get(activeUser.getUsername());
		Date currentTime = new Date(); // get current time
		Date startTime = ride.getStartTime(); // get ride start time
		long difference = (currentTime.getTime() - startTime.getTime()) / 1000; // difference in seconds
		
		return (difference >= 86400);
	}
	
	/**
	 * Charges the user the $2000.00 lost bike fee + overtime fee, and return this total
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
	 * @param activerUsername the activeUser to set
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
	 * @param username		The username associated with the rider
	 * @param membership	The rider's membership
	 */
	public void setMembership(String username, Membership membership) {
		//Set the user's membership
		this.memberships.put(username, membership);
		
		if (!membership.getMembershipType().equals("PayPerRide")) {
			//Charge the user the base rate for the membership (unless they have pay-per-ride)
			PaymentMethod paymentMethod = paymentMethods.get(username);
			BigDecimal chargeAmount;
			chargeAmount = membership.getBaseRate();
			
			paymentMethod.chargeCard(chargeAmount);
			
			//Create new Transaction and add to list
			String description = "ValleyBike ";
			switch(membership.getMembershipType()) {
			case "Monthly":
				description = description + "Monthly ";
				break;
			case "Yearly":
				description = description + "Yearly ";
				break;
			case "FoundingMember":
				description = description + "Founding Member ";
				break;
			case "DayPass":
				description = description + "Day Pass ";
				break;
			}
			description = description + "Subscription";
			
			Transaction transaction = new Transaction(username,chargeAmount,new Date(),description);
			transactionsByUser.putIfAbsent(username, new ArrayList<Transaction>());
			transactionsByUser.get(username).add(transaction);
			
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("MM/dd/yy");
			String chargeDate = df.format(now);
			chargeDates.put(username,chargeDate);
			
			//updates user lists
			saveUserLists();
			//appends to transactions file
			saveAllTransaction(transaction);
		}
	}
	
	/**
	 * Map the user to the associated username.
	 * @param user the user to add
	 */
	public void addUser(User user) {
		this.users.put(user.getUsername(), user);
	}
	
	/**
	 * Map a rider with their payment methods.
	 * @param username		The username associated with the rider
	 * @param paymentMethod	The rider's payment method
	 */
	public void addPaymentMethod(String username, PaymentMethod paymentMethod) {

		// associate payment method with user
		if (paymentMethods.containsKey(username)) {
			paymentMethods.replace(username, paymentMethod);
		} else {
			paymentMethods.put(username,paymentMethod);
		}
		//update payment methods file
		savePaymentMethodList();
	}
	
	/**
	 * Map an email address to the associated ValleyBike rider.
	 * @param email			The rider's email address
	 * @param rider			The rider
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
		
		//Creates new Ride object with bikeId and start time --> get current time 
		Ride ride = new Ride(bikeId, stationId, stations.get(stationId).getStationName(), new Date() );
		
		//Add ride and activeUser to ridesInProgress
		ridesInProgress.put(activeUser.getUsername(), ride);
		
		//Updates bike file
		saveBikeList();
		//Updates station file
		saveStationList();
		//Appends to rides in progress
		saveAllRideInProgress(ride,activeUser.getUsername());
	}

	/**
	 * Charges the user for the ride and updates the model to reflect
	 * the bike being added to the end station
	 * and the ride object being updated
	 * @param stationId the id of the station that the bike is being returned to
	 * @param dockIsFull if the station the user selects to return their bike is full
	 * @return the amount that the user has been charged
	 */
	public BigDecimal endRide (int stationId, boolean dockIsFull) {
		String activeUsername = activeUser.getUsername(); //active User's username
		Membership membership = memberships.get(activeUsername); //user's membership
		Date now = new Date(); //current time
		Ride ride = ridesInProgress.get(activeUsername); //Ride being completed
		PaymentMethod paymentMethod = paymentMethods.get(activeUsername); //active user's payment method
		
		//Charge user for the completed ride
		int rideDuration = (int)(now.getTime() - ride.getStartTime().getTime()) / 60000;
		BigDecimal chargeAmount = membership.getChargeForRide(rideDuration);
		paymentMethod.chargeCard(chargeAmount);
		
		//Create new Transaction and add to list
		Transaction transaction = new Transaction(activeUser.getUsername(),chargeAmount,now,"ValleyBike Ride");
		transactionsByUser.putIfAbsent(activeUser.getUsername(), new ArrayList<Transaction>());
		transactionsByUser.get(activeUser.getUsername()).add(transaction);
		
		// if station is full, move bike to storage
		if (dockIsFull) {
			bikes.get(ride.getBikeId()).setStatus("inStorage");
		} else { // Update bike list at current Station if station isn't full
			stationsBikes.get(stationId).add(ride.getBikeId());
		}
		
		//Add end time and end station to Ride associated to User
		ride.setEndTime(now);
		ride.setEndStationId(stationId);
		ride.setEndStationName(stations.get(stationId).getStationName());
		
		//Move the ride from ridesInProgress to ridesCompleted
		ridesCompleted.putIfAbsent(activeUsername, new HashSet<Ride>());
		ridesCompleted.get(activeUsername).add(ride);
		ridesInProgress.remove(activeUsername);
		
		//Update rides-in-progress file
		saveRidesInProgressList();
		
		//Append to rides-completed file
		saveAllRideCompleted(ride, activeUser.getUsername());
		
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
	 * Creates a new Station object and adds it to the list of stations
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
		
		Station station = new Station(stationId,stationName,address,capacity,0,hasKiosk);
		
		stations.put(stationId, station);
		stationsBikes.put(stationId,new HashSet<Integer>());
		
		//Add station to stations file
		saveAllStation(station);
		
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
			}
		}
		
		System.out.println(extraBikes.size());
		
		//Loop through the stations in need, giving them extra bikes
		for (ArrayList<Integer> need : needBikes) {
			int stationId = need.get(0);
			int numNeeded = need.get(1);
			HashSet<Integer> bikeSet = stationsBikes.get(stationId);
			
			//Remove bikeIds from extraBikes and add them to the station's bikeSet
			for (int i=0; i<numNeeded; i++) {
				bikeSet.add(extraBikes.remove(0));
			}
		}
		System.out.println(extraBikes.size());
		
		int indexStation = 0;
		while (extraBikes.size() > 0) {
			Station s = stationArray[indexStation];
			stationsBikes.get(s.getStationId()).add(extraBikes.remove(0));
			indexStation++;
		}
		
		//update bike list
		saveBikeList();
		//update station list
		saveStationList();
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
	 * @return a list of all the stations
	 */
	public ArrayList<String> getStationList() {
		ArrayList<String> formattedStationList = new ArrayList<>();
		formattedStationList.add("ID\tBikes\tAvDocs\tMainReq\tCap\tKiosk\tName - Address\n");
		Object[] stationIds = stations.keySet().toArray();
		Arrays.sort(stationIds);
		
		for (Object stationId : stationIds) {	
			formattedStationList.add(formatStationToString((Integer)stationId));
		}
		
		return formattedStationList;
	}
	
	/**
	 * Helper method for JUnit testing. Returns Station object corresponding to the id
	 * @param stationId the id of the station
	 * @return the station attached to the stationId
	 */
	public Station getStation(int stationId) {
		return stations.get(stationId);
	}

	/**
	 * Check station to see if all the docks are full.
	 * @param stationId		station ID
	 * @return boolean if the station is full
	 */
	public boolean isStationDockFull(int stationId) {
		
		// station is full if the number of bikes at that station equals capacity
		return (stationsBikes.get(stationId).size() >= stations.get(stationId).getCapacity());
	}
	
	/**
	 * Save the updated transaction list to the CSV file, by overwriting all the entries and adding new entries for the new transactions
	 */
	private void saveTransactionsList() {
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/transaction-data.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "username,amount,time,description".split(",");
		      writer.writeNext(record,false);

		      writer.close();
		      
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//loops through and saves all transactions
		for (ArrayList<Transaction> transactions: transactionsByUser.values()) {
			for (Transaction transaction: transactions) {
				saveAllTransaction(transaction);
			}
		}
	}
	
	/**
	 * Save a transaction to the CSV file
	 * @param transaction
	 */
	private void saveAllTransaction(Transaction transaction) {
		try {
			csvWriter = new FileWriter("data-files/transaction-data.csv",true);

			//adding all the ride details into the csv
			DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");	

			csvWriter.append(transaction.getUsername());
			csvWriter.append(",");
			csvWriter.append(""+transaction.getAmount());
			csvWriter.append(",");
			csvWriter.append(df.format(transaction.getTime()));
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
	 * Save the updated rides in progress lists to the CSV file, by overwriting all the entries and adding new entries for the new rides
	 */
	private void saveRidesInProgressList() {
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/rides-in-progress.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "username,bikeId,From,FromName,Start".split(",");
		      writer.writeNext(record,false);

		      writer.close();
		      
		} catch (IOException e) {
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
			DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");	
			
			csvWriter.append(""+username);
			csvWriter.append(",");
			csvWriter.append(""+ride.getBikeId());
			csvWriter.append(",");
			csvWriter.append(Integer.toString(ride.getStartStationId()));
			csvWriter.append(",");
			csvWriter.append(ride.getStartStationName());
			csvWriter.append(",");
			csvWriter.append(df.format(ride.getStartTime()));
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
		String filename = "data-files/rides-"+month+"-"+day+"-"+year+".csv";
		File ridesToday = new File(filename);

		//either adds the ride to the appropriate file or creates the file
		if (!ridesToday.exists()) {
			try {
				//overwrites existing file with new data
				csvWriter = new FileWriter(filename);
				writer = new CSVWriter(csvWriter);
				String [] record = "username,bikeId,From,FromName,To,ToName,Start,End".split(",");
				writer.writeNext(record,false);

				writer.close();
					      
			} catch (IOException e) {
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
			DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");	
			
			csvWriter.append(""+activeUser.getUsername());
			csvWriter.append(",");
			csvWriter.append(""+ride.getBikeId());
			csvWriter.append(",");
			csvWriter.append(""+ride.getStartStationId());
			csvWriter.append(",");
			csvWriter.append(ride.getStartStationName());
			csvWriter.append(",");
			csvWriter.append(""+ride.getEndStationId());
			csvWriter.append(",");
			csvWriter.append(ride.getEndStationName());
			csvWriter.append(",");
			csvWriter.append(df.format(ride.getStartTime()));
			csvWriter.append(",");
			csvWriter.append(df.format(ride.getEndTime()));
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save the updated rides overdue lists to the CSV file, by overwriting all the entries and adding new entries for the new rides
	 */
	private void saveRidesOverdueList() {
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/rides-overdue.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "username,bikeId,From,FromName,Start".split(",");
		      writer.writeNext(record,false);

		      writer.close();
		      
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//loops through and saves all rides
		for (String username: ridesOverdue.keySet()) {
			Ride ride = ridesOverdue.get(username);
			saveAllRideOverdue(ride, username);
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
			DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");	
			
			csvWriter.append(""+username);
			csvWriter.append(",");
			csvWriter.append(""+ride.getBikeId());
			csvWriter.append(",");
			csvWriter.append(Integer.toString(ride.getStartStationId()));
			csvWriter.append(",");
			csvWriter.append(ride.getStartStationName());
			csvWriter.append(",");
			csvWriter.append(df.format(ride.getStartTime()));
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
			DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");	
			
			csvWriter.append(""+username);
			csvWriter.append(",");
			csvWriter.append(""+ride.getBikeId());
			csvWriter.append(",");
			csvWriter.append(""+ride.getStartStationId());
			csvWriter.append(",");
			csvWriter.append(ride.getStartStationName());
			csvWriter.append(",");
			csvWriter.append(""+ride.getEndStationId());
			csvWriter.append(",");
			csvWriter.append(ride.getEndStationName());
			csvWriter.append(",");
			csvWriter.append(df.format(ride.getStartTime()));
			csvWriter.append(",");
			csvWriter.append(df.format(ride.getEndTime()));
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save the updated ticket list to the CSV file, by overwriting all the entries and adding new entries
	 */
	public void saveTicketList() {     
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/ticket-data.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "ticketId,username,category,identifyingInfo,isResolved,description".split(",");
		      writer.writeNext(record,false);

		      writer.close();
		      
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Ticket[] ticketsArray = tickets.values().toArray(new Ticket[0]);	
		//loops through and saves all paymentMethods
		for (Ticket ticket : ticketsArray) {
			saveAllTicket(ticket);
		}
	}
	
	/**
	 * Ancillary function to assist the saveTicketList() function.
	 */
	private void saveAllTicket(Ticket ticket) {
		try {
			csvWriter = new FileWriter("data-files/ticket-data.csv",true);

			//adding all the bike details into the csv
			csvWriter.append(""+ticket.getTicketId());
			csvWriter.append(",");
			csvWriter.append(ticket.getUsername());
			csvWriter.append(",");
			csvWriter.append(ticket.getCategory());
			csvWriter.append(",");
			csvWriter.append(ticket.getIdentifyingInfo());
			csvWriter.append(",");
			csvWriter.append((ticket.isResolved() ? "Yes" : "No"));
			csvWriter.append(",");
			csvWriter.append("\""+ticket.getDescription()+"\"");
			
			csvWriter.append("\n");
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save the updated payment method list to the CSV file, by overwriting all the entries and adding new entries 
	 */
	public void savePaymentMethodList() {     
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/payment-methods-data.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "username,billingName,cardNumber,billingAddress,expiryDate,cvv".split(",");

		      writer.writeNext(record,false);

		      writer.close();
		      
		} catch (IOException e) {
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
		      String [] record = "bikeId,stationId,status".split(",");

		      writer.writeNext(record,false);

		      writer.close();
		      
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Save all bikes out of order or in storage with station -1 
		for (int bikeId : bikes.keySet()) {
			Bike bike = bikes.get(bikeId);
			if (bike.getStatus().equals("OOO")) {
				saveAllBike(bikeId, -1,"OOO");
			} else if (bike.getStatus().equals("inStorage")) {
				saveAllBike(bikeId, -1,"inStorage");
			}
		}
		
		//Save all bikes currently being ridden with station 0
		for (Ride ride : ridesInProgress.values()) {
			int bikeId = ride.getBikeId();
			saveAllBike(bikeId,0,"working");
		}
		
		//Save all bikes currently at a station
		for (int stationId : stationsBikes.keySet()) {
			for (int bikeId : stationsBikes.get(stationId)) {
				saveAllBike(bikeId, stationId,"working");
			}
		}
	}
	
	/**
	 * Ancillary function to assist the saveBikeList() function.
	 */
	private void saveAllBike(int bikeId, int stationId, String status) {
		try {
			csvWriter = new FileWriter("data-files/bike-data.csv",true);

			//adding all the bike details into the csv
			
			csvWriter.append(Integer.toString(bikeId));
			csvWriter.append(",");
			csvWriter.append(Integer.toString(stationId));
			csvWriter.append(",");
			csvWriter.append(status);
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
	
	/**
	 * Ancillary function to assist the saveUserLists() function
	 */
	private void saveRiderList(ArrayList<Rider> riders) {
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/rider-data.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "username,password,fullname,email,phoneNumber,address,membershipType,chargeDate".split(",");
		      
		      writer.writeNext(record,false);

		      writer.close();
		      
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//loops through and saves all stations
		for (Rider rider : riders) {
			saveAllRider(rider);
		}
		
	}
	
	/**
	 * Ancillary function to assist the saveUserLists() function
	 */
	private void saveAdminList(ArrayList<Admin> admins) {
		try {
			  //overwrites existing file with new data
			  csvWriter = new FileWriter("data-files/admins-data.csv");
			  writer = new CSVWriter(csvWriter);
		      String [] record = "username,password,fullname,email,phoneNumber,address,membershipType".split(",");
		      
		      writer.writeNext(record,false);

		      writer.close();
		      
		} catch (IOException e) {
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
			csvWriter.append(rider.getUsername());
			csvWriter.append(",");
			csvWriter.append(rider.getPassword());
			csvWriter.append(",");
			csvWriter.append(rider.getFullName());
			csvWriter.append(",");
			csvWriter.append(rider.getEmail());
			csvWriter.append(",");
			csvWriter.append(rider.getPhoneNumber());
			csvWriter.append(",");
			csvWriter.append(rider.getAddress());
			csvWriter.append(",");
			
			String membershipType;
			Membership membership = memberships.get(rider.getUsername());
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
			csvWriter.append(",");
			
			String chargeDate = chargeDates.get(rider.getUsername());
			
			csvWriter.append(chargeDate);
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
			csvWriter.append(admin.getUsername());
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

		      writer.writeNext(record,false);

		      writer.close();
		      
		} catch (IOException e) {
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
			
			
			// append number of free docks
			int numFreeDocks = station.getCapacity() - stationsBikes.get(station.getStationId()).size();
			csvWriter.append(Integer.toString(numFreeDocks));
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
	 * @return true if currently active user has a ride that exceeds 24 hours, else false
	 */
	public boolean checkStolenBikes() {
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
				paymentMethod.chargeCard(new BigDecimal("2000.00"));
				
				//create new Transaction
				Transaction transaction = new Transaction(username,new BigDecimal("2000.00"),new Date(),"ValleyBike Stolen Bike Fee");
			
				//add new transaction to data structure
				transactionsByUser.putIfAbsent(username, new ArrayList<Transaction>());
				transactionsByUser.get(username).add(transaction);
				
				//append transaction to data file
				saveAllTransaction(transaction);
				
			}
		}
		for (String username : overdueUsernames) {
			//move ride from ridesInProgress to ridesOverdue
			ridesOverdue.put(username,ridesInProgress.remove(username));
		}
		
		//update rides overdue file
		saveRidesOverdueList();
		//update rides in progress file
		saveRidesInProgressList();
		//update bike file
		saveBikeList();
		return (ridesOverdue.containsKey(activeUser.getUsername()));
	}

	/**
	 * Checks whether the active user has stolen a bike
	 * @return true if the user has stolen a bike
	 */
	public boolean activeUserStolenBike() {
		return ridesOverdue.containsKey(activeUser.getUsername());
	}

	public void createNewRider(Rider rider, PaymentMethod paymentMethod, Membership membership) {
		addUser(rider); // maps rider to username in system
		addEmail(rider.getEmail(), rider); // map email address to rider in the system
		addPaymentMethod(rider.getUsername(), paymentMethod); // add payment method to rider's account, adds payment method to file
		setMembership(rider.getUsername(), membership); // set rider's membership, adds rider to file
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
		for (Integer bikeId : stationsBikes.get(stationId)) {
			bikes.get(bikeId).setStatus("inStorage");
		}
		stationsBikes.remove(stationId);
		
		// remove station from station list
		stations.remove(stationId);
		
		// update stations data file to reflect the change
		saveStationList();		
		
		// update bikes data file to reflect the change
		saveBikeList();
	}

	
	/**
	 * Returns a formatted list of the user's ride history
	 * @return formatted list of rides
	 */
	public ArrayList<String> getRideList() {
		ArrayList<String> formattedRideList = new ArrayList<>();
		formattedRideList.add("Start Time\tEnd Time\tDuration\tStart Station-End Station\n");
		DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");
		for (Ride ride : ridesCompleted.get(activeUser.getUsername())) {
			String startStationName = ride.getStartStationName();
			String endStationName = ride.getEndStationName();
			String stations = startStationName+" - "+endStationName;
			Date startTime = ride.getStartTime();
			Date endTime = ride.getEndTime();
			long difference = (endTime.getTime() - startTime.getTime()) / 1000 / 60; // difference in minutes
			String duration = ""+difference+" minutes";
			
			formattedRideList.add(df.format(startTime)+"\t"+df.format(endTime)+"\t"+duration+"\t"+stations+"\n");
		}
		
		return formattedRideList;
	}
	
	/**
	 * Returns a formatted list of the user's transaction history
	 * @return formatted list of transactions
	 */
	public ArrayList<String> getTransactionList() {
		ArrayList<String> formattedTransactionList = new ArrayList<>();
		formattedTransactionList.add("Date\t\tAmount\t\tDescription\n");
		DateFormat df = new SimpleDateFormat("MM/dd/yy");
		for (Transaction transaction : transactionsByUser.get(activeUser.getUsername())) {
			String date = df.format(transaction.getTime());
			String amount = "$"+transaction.getAmount().toString();
			String description = transaction.getDescription();
			formattedTransactionList.add(date+"\t"+amount+"\t\t"+description+"\n");
		}
		return formattedTransactionList;
	}

	/**
	 * Add a new bike to storage by ID.
	 * @param bikeId The bike's ID number
	 */
	public void addNewBikeToStorage(int bikeId) {
		Bike bike = new Bike(bikeId,"inStorage");
		bikes.put(bikeId, bike);	
		
		// Updates bike file
		saveBikeList();
	}

	/**
	 * Add a bike from storage to a station by ID, and update station and bike data files.
	 * @param bikeId 	The bike's ID number
	 * @param stationId The station's ID number
	 */
	public void addBikeFromStorageToStation(int bikeId,int stationId) {
		
		// add bike to station
		stationsBikes.putIfAbsent(stationId, new HashSet<Integer>());
		stationsBikes.get(stationId).add(bikeId);
		
		// move bike from storage to "working" status
		bikes.get(bikeId).setStatus("working");
		
		// Updates bike file
		saveBikeList();
		// Updates station file
		saveStationList();
	}

	/**
	 * Check if a bike is in storage by ID
	 * @param bikeId The bike ID
	 * @return true if bike is in storage, else false
	 */
	public boolean isBikeInStorage(int bikeId) {
		return bikes.get(bikeId).getStatus().equals("inStorage");
	}

	/**
	 * Check if a station is at capacity by comparing its capacity 
	 * to the number of bikes at the station.
	 * @param stationId		Station ID
	 * @return true if station is at capacity, else false.
	 */
	public boolean isStationAtCapacity(int stationId) {
		return stations.get(stationId).getCapacity() == stationsBikes.get(stationId).size();
	}

	/**
	 * Check if this bike is indeed at this station.
	 * @param stationId		Station ID
	 * @param bikeId		Bike ID
	 * @return true if bike at station, else false
	 */
	public boolean stationHasBike(int stationId, int bikeId) {
		return stationsBikes.get(stationId).contains(bikeId);
	}

	/**
	 * Helper function to return the list of bikes at a station.
	 * @param stationId
	 * @return list of bikes at a station
	 */
	public HashSet<Integer> getBikeListFromStation(int stationId) {
		return stationsBikes.get(stationId);
	}
	
	/**
	 * Helper function to wrap all information pertaining to the specified station in a string.
	 * @param stationId		Station ID
	 * @return	a string with all of this station's information, formatted
	 */
	public String formatStationToString(int stationId) {
		Station station = stations.get(stationId);
		String id = Integer.toString(stationId);
		String numBikes = Integer.toString(stationsBikes.get(stationId).size());
		String cap = Integer.toString(station.getCapacity());
		String avDocks = Integer.toString(Integer.parseInt(cap)-Integer.parseInt(numBikes));
		String mainReq = Integer.toString(station.getMaintenanceReqs());
		String hasKiosk = (station.isHasKiosk() ? "Yes" : "No");
		String nameAddress = station.getStationName() + " - " + station.getAddress();
		return id + "\t" + numBikes + "\t" + avDocks + "\t" + mainReq + "\t" + cap + "\t" + hasKiosk + "\t" + nameAddress;
	}

	/**
	 * 
	 * @return the name of the type of membership the active user has
	 */
	public String getActiveUserMembershipName() {
		return memberships.get(activeUser.getUsername()).getMembershipType();
	}

	/**
	 * Checks if the user has completed any rides
	 * @return
	 */
	public boolean activeUserHasRidesCompleted() {
		return ridesCompleted.containsKey(activeUser.getUsername());
	}
	
	/**
	 * Checks if the user has made any transactions
	 * @return
	 */
	public boolean activeUserHasTransactions() {
		return transactionsByUser.containsKey(activeUser.getUsername());
	}
	
	/**
	 * Returns a bike's status
	 * @param bikeId 	The bike ID
	 * @return the bike's status as a string
	 */
	public String getBikeStatus(int bikeId) {
		return bikes.get(bikeId).getStatus();
	}

	/**
	 * Move bike from station to storage, and returns the name and ID of the station to which the bike belonged.
	 * @param bikeId	The bike ID
	 * @param bikeStatus the status of the bike
	 * @return station name and ID
	 */
	public String[] moveBikeFromStationToStorage(int bikeId, String bikeStatus) {
		String[] returnData = new String[2];
		for (Integer sId : stationsBikes.keySet()) {
			if (stationsBikes.get(sId).contains(bikeId)) {
				returnData[0] = Integer.toString(sId);
				returnData[1] = stations.get(sId).getStationName();
			}
		}
		
		// move bike from this station to storage
		stationsBikes.get(Integer.parseInt(returnData[0])).remove(bikeId);
		bikes.get(bikeId).setStatus(bikeStatus);
		
		// save changes to bike and station data files
		saveStationList();
		saveBikeList();
		return returnData;
	}

	/**
	 * Remove a bike currently in storage from the Valley Bike system
	 * @param bikeId	The bike ID
	 */
	public void removeBikeInStorageFromSystem(int bikeId) {
		bikes.remove(bikeId);
		
		// save changes to bike data file
		saveBikeList();
	}

	/**
	 * Returns the string version of the active user's payment method
	 * @return
	 */
	public String getPaymentMethodString() {
		return paymentMethods.get(activeUser.getUsername()).toString();
	}
	
	/**
	 * Returns the string version of the active user's profile
	 * @return
	 */
	public String getUserProfileString() {
		return users.get(activeUser.getUsername()).toString();
	}

	/**
	 * Sets activeUser's billing name
	 * @param billingName
	 */
	public void setBillingName(String billingName) {
		PaymentMethod paymentMethod = paymentMethods.get(activeUser.getUsername());
		paymentMethod.setBillingName(billingName);
		savePaymentMethodList();
	}

	/**
	 * Sets activeUser's billing address
	 * @param billingAddress
	 */
	public void setBillingAddress(String billingAddress) {
		PaymentMethod paymentMethod = paymentMethods.get(activeUser.getUsername());
		paymentMethod.setAddress(billingAddress);
		savePaymentMethodList();
	}

	/**
	 * Adds a new card to activeUser's account
	 * @param creditCardNumber
	 * @param creditCardDate
	 * @param cvv
	 */
	public void addCard(String creditCardNumber, String creditCardDate, String cvv) {
		PaymentMethod paymentMethod = paymentMethods.get(activeUser.getUsername());
		paymentMethod.setCardNumber(creditCardNumber);
		paymentMethod.setExpiryDate(creditCardDate);
		paymentMethod.setCvv(cvv);
		savePaymentMethodList();
	}

	/**
	 * Reads rides from the specified file and returns the formatted rides list and statistics
	 * @param filename
	 * @return String stating the number and average duration of rides that day
	 */
	public String getRidesStatistics(String filename) {
		String allLines = "";
		String formattedRideList = "From\tTo\tStart\t\tEnd\n";
		long sumRideDurations = 0;
		try {
			CSVReader ridesCompletedDataReader = new CSVReader(new FileReader(filename));
			
			List<String[]> allRideEntries = ridesCompletedDataReader.readAll();
			
			
			int counter = 0;
			for(String[] array : allRideEntries) {
				if(counter > 0) {
					formattedRideList += array[2] + "\t" + array[4] + "\t" + array[6] + "\t" + array[7] + "\n";
					
					Date startTime = toDate(array[6]);
					Date endTime = toDate(array[7]);
					long difference = (endTime.getTime() - startTime.getTime()) / 1000 / 60; // difference in minutes
					sumRideDurations += difference;
				}
				counter++;
			} 
			ridesCompletedDataReader.close();
			
			long avgRideDuration = sumRideDurations/(counter-1);
			allLines = "On this day there were "+(counter-1)+" rides with average ride time of "+avgRideDuration+" minutes\n\n";
			allLines += formattedRideList;
		} 
		
		catch (Exception e) {
			return "No rides completed on this day.\n";
		}
		return allLines;
	}

	/**
	 * Gets a list of all transactions completed in a specified day
	 * @param desiredDate date to summarize 
	 * @return
	 */
	public ArrayList<String> getTransactionsStatistics(String desiredDate) {
		ArrayList<String> formattedTransactionList = new ArrayList<>();
		formattedTransactionList.add("Amount\t\tDescription\n");
		DateFormat df = new SimpleDateFormat("MMM-dd-yy");
		
		BigDecimal total = new BigDecimal(0);
		for (ArrayList<Transaction> userTransactions : transactionsByUser.values()) {
			for (Transaction transaction : userTransactions) {
				String date = df.format(transaction.getTime());

				// check if the date is the desired date
				if (date.equals(desiredDate)) {
					String amount = transaction.getAmount().toString();
					total = total.add(new BigDecimal(amount));
					String description = transaction.getDescription();
					formattedTransactionList.add("$"+amount + "\t\t" + description + "\n");
				}
			}
		}
		String stats = "ValleyBike made $"+total.toString()+" on this day.\n";
		ArrayList<String> output = new ArrayList<String>();
		output.add(stats);
		output.addAll(formattedTransactionList);
		return output;
	}

	/**
	 * Change the activeUser's specified field
	 * @param fieldName the field
	 * @param value what to change the active user's field to
	 */
	public void setActiveUserInfo(String fieldName, String value) {
		Rider activeRider = (Rider)users.get(activeUser.getUsername());
		switch(fieldName) {
		case "username":
			activeUser.setUsername(value);
			break;
		case "password":
			activeUser.setPassword(value);
			break;
		case "fullName":
			activeRider.setFullName(value);
			break;
		case "email":
			activeRider.setEmail(value);
			break;
		case "phoneNumber":
			activeRider.setPhoneNumber(value);
			break;
		case "address":
			activeRider.setAddress(value);
			break;
		}
		saveUserLists();
	}

	/**
	 * Charges user for their monthly subscriptions (if they haven't already been charged today)
	 */
	public boolean chargeSubscriptions() {
		boolean monthlyUncharged = false;
		
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("MM/dd/yy");
		String today = df.format(now);
		
		for (String username: memberships.keySet()) {
			String chargeDate = chargeDates.get(username);
			Membership membership = memberships.get(username);
			if ((membership instanceof DayPass) || (membership instanceof Monthly)) {
				if (!chargeDate.contentEquals(today)) { //if the user has not already been charged today
					//check if user has a day pass. If so, give them a pay per ride pass instead and charge
					if (membership instanceof DayPass) {
						MembershipFactory mf = new MembershipFactory();
						Membership newMembership = mf.getMembership("PayPerRide");
						memberships.put(username,newMembership);
						Transaction transaction = new Transaction(username,new BigDecimal("2.00"),new Date(),"ValleyBike Pay Per Ride Pass");
						transactionsByUser.get(username).add(transaction);
						saveAllTransaction(transaction);
						chargeDates.put(username, today);
					} else if (membership instanceof Monthly) {
						monthlyUncharged = true;
					}
				}
			} else if (!(membership instanceof PayPerRide)){
				String chargeDateYear = chargeDate.substring(6);
				if (!chargeDateYear.equals(today.substring(6))) {
					//Reset their membership to Pay Per Ride
					MembershipFactory mf = new MembershipFactory();
					Membership newMembership = mf.getMembership("PayPerRide");
					memberships.put(username,newMembership);
					
					Transaction transaction = new Transaction(username,new BigDecimal("0.00"),new Date(),"ValleyBike Pay Per Ride Reset");
					transactionsByUser.get(username).add(transaction);
					saveAllTransaction(transaction);
					chargeDates.put(username, today);
				}
			}
		}
		saveUserLists();
		return monthlyUncharged;
	}

	/**
	 * Charges monthly users for their subscriptions
	 */
	public void chargeMonthly() {
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("MM/dd/yy");
		int day = Integer.parseInt(df.format(now).substring(3,5));
		int month = Integer.parseInt(df.format(now).substring(0,2));
		
		HashSet<Rider> ridersToCharge = billingCycleMonthly.get(day);
		for (Rider rider: ridersToCharge) {
			int chargeMonth = Integer.parseInt(chargeDates.get(rider.getUsername()).substring(0,2));
			if (chargeMonth!=month) {
				Transaction transaction = new Transaction(rider.getUsername(),new BigDecimal("20.00"),now,"ValleyBike Monthly Subscription");
				transactionsByUser.get(rider.getUsername()).add(transaction);
				saveAllTransaction(transaction);
				chargeDates.put(rider.getUsername(), df.format(now));
			}
		}
		saveUserLists();
	}

	/**
	 * Move all users charged after that day of the month to the first day of the month
	 * @param today
	 */
	public void moveChargeDates(int today) {
		for (int i=today+1; i<32; i++) {
			HashSet<Rider> riders = billingCycleMonthly.get(i);
			
			for (Rider rider: riders) {
				String chargeDate = chargeDates.get(rider.getUsername());
				String newChargeDate = chargeDate.substring(0,3)+"01"+chargeDate.substring(5);
				chargeDates.put(rider.getUsername(),newChargeDate);
			}
			
			billingCycleMonthly.get(1).addAll(riders);
		}
		saveUserLists();
	}
	
	/**
	 * Returns the bike ID of the user's ride in progress
	 * @return bike ID
	 */
	public int getBikeIdRideInProgress() {
		return ridesInProgress.get(activeUser.getUsername()).getBikeId();
	}

	/**
	 * Returns the ID of the bike that the user has not checked in for more than 24hrs
	 * @return
	 */
	public int getActiveUserStolenBikeId() {
		return ridesOverdue.get(activeUser.getUsername()).getBikeId();
	}

	/**
	 * Returns full formatted list of support tickets.
	 * @param forRider true if the request is from the rider
	 * @return Array List containing tickets
	 */
	public ArrayList<String> getFormattedTicketList(boolean forRider) {
		ArrayList<String> formattedTicketList = new ArrayList<>();
		formattedTicketList.add("ID\tUsername\tCategory\tIdenfyingInfo\tIs resolved?\tDescription\n");
		Object[] ticketIds = tickets.keySet().toArray(); // set of all ticket IDs
		Arrays.sort(ticketIds); // sort ticket IDs to display in that order
		String activeUsername = activeUser.getUsername(); // the username of currently active user
		
		// traverse all tickets
		for (Object ticketId : ticketIds) {	
			
			// if we're getting a rider's list of tickets and this ticket isn't theirs, don't include in list
			if (forRider && !tickets.get((Integer)ticketId).getUsername().equals(activeUsername)) {
				continue;
			}
			
			// else add ticket to list
			formattedTicketList.add(formatTicketToString((Integer)ticketId));
		}
		
		return formattedTicketList;
	}
	
	/**
	 * Helper function to wrap all information pertaining to the specified ticket in a string.
	 * @param ticketId	ticket ID
	 * @return	a string with all of this ticket's information, formatted
	 */
	public String formatTicketToString(int ticketId) {
		Ticket ticket = tickets.get(ticketId);
		String id = Integer.toString(ticketId);
		String username = ticket.getUsername();
		String category = ticket.getCategory();
		String info = ticket.getIdentifyingInfo();
		String isResolved = (ticket.isResolved() ? "Yes" : "No");
		String description = ticket.getDescription();
		return id + "\t" + username + "\t" + category + "\t\t" + info + "\t\t" + isResolved + "\t\t" + description;
	}

	
	/**
	 * Check if this bike is out of order
	 * @param bikeId the bike ID
	 * @return true if bike is OOO, or in storage
	 */
	public boolean bikeIsInStorage(int bikeId) {
		String bikeStatus = bikes.get(bikeId).getStatus();
		return (bikeStatus.equals("OOO") || bikeStatus.equals("inStorage"));
	}
	
	/**
	 * Check if this bike is in use
	 * @param bikeId the bike ID
	 * @return true if bike is in use
	 */
	public boolean bikeIsInUse(int bikeId) {
		boolean inUse = false;
		
		for (Ride ride : ridesInProgress.values()) {
			inUse = (ride.getBikeId() == bikeId);
		}
		return inUse;
	}
	
	/**
	 * Create a support ticket, and store it in the database. Update data files accordingly.
	 * @param category
	 * @param identifyingInfo
	 * @param description
	 */
	public int createSupportTicket(String category, String identifyingInfo, String description) {
		
		// create ticket object
		int ticketId = (Integer)Collections.max(tickets.keySet()) + 1;
		String username = activeUser.getUsername();
		Ticket ticket = new Ticket(ticketId, username, category,identifyingInfo,false, description);
		
		// map ticket object to the user and to the ticket ID
		tickets.put(ticketId, ticket);
		usersTickets.putIfAbsent(username, new HashSet<>());
		usersTickets.get(username).add(ticket);
		
		// bike category issues 
		if (category.equals("bike")) {
			int bikeId = Integer.parseInt(identifyingInfo);
			// if bike is broken, move to storage
			if (ticket.getDescription().equalsIgnoreCase("Bike OOO")) {
				moveBikeFromStationToStorage(bikeId, "OOO");
			} else if (ticket.getDescription().equalsIgnoreCase("Check in bike at full station")){ 

				// ticket is instantly resolved since we've moved the bike to storage
				tickets.get(ticketId).setResolved(true);
				saveStationList();
				saveBikeList();
			} else { // if the user had an overdue bike, close ticket, inform them of the $2000 charge
				tickets.get(ticketId).setResolved(true);
			}
		}
		
		// if ticket is maintenance request for station, edit station data
		if (category.equals("station")) {
			int stationId = Integer.parseInt(identifyingInfo);
			
			// increase the number of maintenance requests at this station
			Station station = stations.get(stationId);
			station.setMaintenanceReqs(station.getMaintenanceReqs()+1);
			saveStationList();
		}
		
		// save ticket to data file
		saveTicketList();
		
		return ticketId;
	}

	/**
	 * Resolves an unresolved support ticket depending on category.
	 * @param ticketId the ticket ID
	 */
	public void resolveSupportTicket(int ticketId) {
		String category = tickets.get(ticketId).getCategory();
		String identifyingInfo = tickets.get(ticketId).getIdentifyingInfo();
		
		if (category.equals("station")) { // ticket is a maintenance request
			// the admin should have already resolved this maintenance request 
			// in real life before resolving it in the system
			
			// remove maintenance request from station
			Station station = stations.get(Integer.parseInt(identifyingInfo));
			station.setMaintenanceReqs(station.getMaintenanceReqs()-1);;
			
			// update station data file to reflect this change
			saveStationList();
			
		} else if (category.equals("bike")) { // ticket of OOO bike
			// (because this ticket is bike-related and a "bike overdue" ticket is always resolved, 
			// this can only be a ticket of an OOO bike)
			
			// symbolically "fixing" this bike and change its status to inStorage
			int bikeId = Integer.parseInt(identifyingInfo);
			bikes.get(bikeId).setStatus("inStorage"); 
			
			// update bike data file to reflect this change
			saveBikeList();
		}
		
		// set resolved ticket to true
		tickets.get(ticketId).setResolved(true);
		
		saveTicketList();
		
	}

	/**
	 * Checks if yearly/founding member pass has expired
	 * @return
	 */
	public boolean hasSubscriptionExpired() {
		//check if user's transaction list contains a reset
		for (Transaction transaction: transactionsByUser.get(activeUser.getUsername())) {
			String description = transaction.getDescription();
			if (description.contains("Reset")) {
				transactionsByUser.get(activeUser.getUsername()).remove(transaction);
				saveTransactionsList();
				saveUserLists();
				return true;
			}
		}
		return false;
	}

}
