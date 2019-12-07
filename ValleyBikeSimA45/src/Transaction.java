import java.util.Date;

/**
 * Transaction object. Each Transaction object represents a transaction.
 * @author maingo
 *
 */
public class Transaction {
	
	
	/** the user that initiated this transaction */
	private String username;
	
	/** the transaction amount */
	private float amount;
	
	/** transaction timestamp */
	private Date time;
	
	/** transaction description */
	private String description;
	
	
	/**
	 * @param username
	 * @param amount
	 * @param time
	 * @param description
	 */
	public Transaction(String username, int amount, Date time, String description) {
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
	 * @return the amount
	 */
	public float getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}
	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}
	/**
	 * @param time the time to set
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
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
