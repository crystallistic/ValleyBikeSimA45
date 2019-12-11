import java.math.BigDecimal;

/**
 * @author maingo
 *
 */
public class PaymentMethod {
	
	private String billingName; //customer's name
	private String cardNumber; //user's credit card number
	private String billingAddress; //user's address associated with their credit card
	private String expiryDate; //user's credit card's expiration date
	private String cvv; //user's credit card's 3 digit code on the back of their card
	
	/**
	 * @param billingName Holds the user's name associated with the credit card
	 * @param cardNumber Hold's the user's credit card's number
	 * @param address Hold's the address associated with the credit card 
	 * @param expiryDate Hold's the expiration date of the credit card 
	 * @param cvv Hold's the secret code for the credit cared (found on the back of the card)
	 */
	public PaymentMethod(String billingName, String cardNumber, String billingAddress, String expiryDate, String cvv) {
		this.billingName = billingName;
		this.cardNumber = cardNumber;
		this.billingAddress = billingAddress;
		this.expiryDate = expiryDate;
		this.cvv = cvv;
	}

	/**
	 * @return the billingName
	 */
	public String getBillingName() {
		return billingName;
	}

	/**
	 * @param billingName the billingName to set, and is the name associated a specific credit card
	 */
	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}

	/**
	 * @return the cardNumber that is associated with the card on file and the user
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * @param cardNumber the cardNumber to set
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * @return the address associated with the user's credit card on file
	 */
	public String getAddress() {
		return billingAddress;
	}

	/**
	 * @param address Holds the billing address of the user's credit card
	 */
	public void setAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @return the expiryDate
	 */
	public String getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @return the cvv
	 */
	public String getCvv() {
		return cvv;
	}

	/**
	 * @param cvv the cvv to set
	 */
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	@Override
	public String toString() {
		String s = "Card number\t\tExpiry Date\tBilling Name\t\tBilling Address\n";
		s += "************"+cardNumber.substring(12,16)+"\t";
		s += expiryDate+"\t\t";
		s += billingName + "\t\t" + billingAddress + "\n";
		return s;
	}
	
	/**
	 * Charge the user the specified amount. 
	 * Since we don't deal with credit card, this function does nothing for now, but maybe useful
	 * in the future if we want to integrate an API to do this. 
	 * @param chargeAmount		Amount to be charged
	 */
	public void chargeCard(BigDecimal chargeAmount) {
		
	}
	
}
