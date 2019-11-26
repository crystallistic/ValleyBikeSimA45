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
			System.out.println(e.getMessage());		
		}
	}
	
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
					
					Date startTime = toDate(array[4]);
					Date endTime = toDate(array[5]);
					totalDuration += endTime.getTime() - startTime.getTime();
					
				}
				counter++;
			} 
			
			rideDataReader.close();
			
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
	 * @param userInputName 	the user's input type
	 * @param userInput			the user's input
	 * @return 
	 */
	public boolean isValid(String userInputName, String userInput) {
		
		boolean inputIsValid = true;
		boolean matchRegex = true;
		boolean notExistInSys = true;
		Pattern r = null;
		
		switch (userInputName) {
		case "stationId":			
			inputIsValid = (stations.containsKey(Integer.parseInt(userInput)));
			break;
		case "bikeId":
			inputIsValid = (bikes.containsKey(Integer.parseInt(userInput)));
			break;
		case "loginInfo":
			String[] info = userInput.split(" ");
			
			inputIsValid = (users.containsKey(info[0]) && users.get(info[0]).getPassword().equals(info[1]));
			break;
		case "newUsername":
			inputIsValid = (!users.containsKey(userInput) && userInput.length() >= 6);
			break;
		case "newEmail":
			// regex to validate email format
			r = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"); 
			
			// email is valid if it's in valid format and it does not belong to an existing user
			matchRegex = r.matcher(userInput).find();
			notExistInSys = !emails.containsKey(userInput);
			inputIsValid = (matchRegex && notExistInSys);
			break;
		case "newStationId":	
			// Assumption: Valley Bike's station IDs are two-digit and only within the 01-99 range.
			r = Pattern.compile("^[0-9]{2}$"); 
			matchRegex = r.matcher(userInput).find();
			
			// new station ID is valid if it's 2-digit and has not appeared in the system.
			inputIsValid = (matchRegex && !stations.containsKey(Integer.parseInt(userInput)));
			break;
		case "newStationName":	
			
			for (Station station : stations.values()) {
				if (station.getStationName() == userInput) {
					inputIsValid = false;
				}
			}
			break;
		case "newStationAddress":	
			
			for (Station station : stations.values()) {
				if (station.getAddress() == userInput) {
					inputIsValid = false;
				}
			}
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
	
	/**
	 * Removes the checked out bike from the start station,
	 * and creates a new ride for the user.
	 * @param bikeId the id of the bike being checked out
	 * @param stationId the id of the station the bike is being removed from
	 */
	public void startRide(int bikeId, int stationId) {
		//Remove the bikeId from the HashSet of bikeIds associated to the station in stations HashMap
		stationsBikes.get(stationId).remove(bikeId);
		
		//Update numFreeDocks in startStation to reflect one new free dock
		Station startStation = stations.get(stationId);
		startStation.setNumFreeDocks(startStation.getNumFreeDocks()+1);
		
		//Creates new Ride object with bikeId and start time --> get current time 
		Ride ride = new Ride(bikeId, startStation, new Date() );
		
		//Add ride and activeUser to ridesInProgress
		ridesInProgress.put(activeUser.getUserName(), ride);
	}

	/**
	 * Charges the user for the ride and updates the model to reflect
	 * the bike being added to the end station
	 * and the ride object being updated
	 * @param stationId the id of the station that the bike is being returned to
	 * @return the amount that the user has been charged
	 */
	public float endRide (int stationId) {
		String activeUsername = activeUser.getUserName(); //active User's username
		Membership membership = memberships.get(activeUsername); //user's membership
		Date now = new Date(); //current time
		Ride ride = ridesInProgress.get(activeUsername); //Ride being completed
		PaymentMethod paymentMethod = paymentMethods.get(activeUsername).get(0); //active user's payment method
		Station endStation = stations.get(stationId); //station where the bike is being returned
		
		//Charge user for the completed ride
		int rideDuration = (int)(now.getTime() - ride.getStartTime().getTime()) / 60000;
		float chargeAmount = membership.getChargeForRide(rideDuration);
		paymentMethod.chargeCard(chargeAmount);
		
		//Update bike list at current Station
		stationsBikes.get(stationId).add(ride.getBikeId());
		endStation.setNumFreeDocks(endStation.getNumFreeDocks()-1);
		
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

	/**
	 * Check station to see if all the docks are full.
	 * @param stationId		station ID
	 * @return boolean 
	 */
	public boolean isStationDockFull(int stationId) {

		Station station = stations.get(stationId);
		return (station.getNumFreeDocks() == 0);
	}
	
}
