import java.math.BigDecimal;
import java.util.Date;

/**
 * Transaction object. Each Transaction object represents a transaction.
 * @author maingo,  jemimahcharles, maggieburkart, emmatanur
 *
 */
public class Transaction {
	
	
	/** the user that initiated this transaction */
	private String username;
	
	/** the transaction amount */
	private BigDecimal amount;
	
	/** transaction timestamp */
	private Date time;
	
	/** transaction description */
	private String description;
	
	
	/**
	 * @param username HOlds the current username
	 * @param amount Holds the amount that the user is charged in USD
	 * @param time Holds the time a rider rode in hours and minutes
	 * @param description Holds the explanation of the user's transaction 
	 */
	public Transaction(String username, BigDecimal amount, Date time, String description) {
		this.username = username;
		this.amount = amount;
		this.time = time;
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
	 * @return the amount the user is charged
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @param amount HOlds the amount that is being charged
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}
	/**
	 * @param time Holds the amount of time a rider rode in hours and minutes
	 */
	public void setTime(Date time) {
		this.time = time;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description Holds the description of the transaction 
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
