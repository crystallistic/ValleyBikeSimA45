/**
 * @author maingo
 *
 */
public class Ticket {
	
	int ticketId;
	String description;
	String username;
	
	/**
	 * @param ticketId
	 * @param description
	 */
	public Ticket(int ticketId, String description, String username) {
		this.ticketId = ticketId;
		this.description = description;
		this.username = username;
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
	
	
	
}
