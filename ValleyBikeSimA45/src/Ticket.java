/**
 * @author maingo, jemimahcharles, maggieburkart, emmatanur
 * Object to store Ticket information
 */
public class Ticket {
	
	/** ticket ID number */
	private int ticketId;
	
	/** The username of the user who submitted this ticket */
	private String username;
	
	/** 
	 * 	Ticket category. 
	 * 	Possible values: bike, station, general
	 *  - Bike: Bike-related issues (OOO) 
	 *  - Station: Station-related issues. These tickets are linked to stations' maintenance requests 
	 *  - General: All other requests
	 */
	private String category;
	
	/** Identifying information that would help with ticket resolution
	 * 	Possible values:
	 * 	- 3-digit bike ID for tickets in "bike" category
	 * 	- 2-digit station ID for tickets in "station" category
	 *  - Empty string for general tickets
	 */
	private String identifyingInfo;
	
	/** Problem description */
	private String description;
	
	/** Ticket status. True if ticket is resolved, else false. */
	private boolean isResolved;
	
	/**
	 * @param ticketId Holds the id for the ticket, in numbers
	 * @param description Holds the description  of the problem, cannot be empty
	 * @param username Holds the username of the user that submitted the request
	 * @param category Holds the category of the problem (1-4)
	 * @param identifyingInfo Holds the bikeid or stationid or is empty depending on the category of the ticket
	 * @param isResolved Holds a boolean (yes or no) for if the ticket has been resolved
	 */
	public Ticket(int ticketId, String username, String category, String identifyingInfo, boolean isResolved, String description) {
		this.ticketId = ticketId;
		this.username = username;
		this.category = category;
		this.identifyingInfo = identifyingInfo;
		this.description = description;
		this.isResolved = isResolved;
	}
	
	/**
	 * @return the ticketId
	 */
	public int getTicketId() {
		return ticketId;
	}
	/**
	 * @param ticketId the ticketId to set
	 */
	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * @return the boolean isResolved 
	 */
	public boolean isResolved() {
		return isResolved;
	}

	/**
	 * @param isResolved Holds
	 */
	public void setResolved(boolean isResolved) {
		this.isResolved = isResolved;
	}

	/**
	 * @return the identifyingInfo
	 */
	public String getIdentifyingInfo() {
		return identifyingInfo;
	}

	/**
	 * @param identifyingInfo the identifyingInfo to set
	 */
	public void setIdentifyingInfo(String identifyingInfo) {
		this.identifyingInfo = identifyingInfo;
	}
}
