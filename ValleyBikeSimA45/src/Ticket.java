/**
 * @author maingo
 *
 */
public class Ticket {
	
	int ticketId;
	String description;
	
	/**
	 * @param ticketId
	 * @param description
	 */
	public Ticket(int ticketId, String description) {
		this.ticketId = ticketId;
		this.description = description;
	}
	/**
	 * @return the ticketId
	 */
	public int getTicketid() {
		return ticketId;
	}
	/**
	 * @param ticketId the ticketId to set
	 */
	public void setTicketid(int ticketId) {
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
	
}
