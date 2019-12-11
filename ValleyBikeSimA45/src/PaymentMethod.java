import java.math.BigDecimal;

/**
 * @author maingo
 *
 */
public class PaymentMethod {
	
	private String billingName;
	private String cardNumber;
	private String billingAddress;
	private String expiryDate;
	private String cvv;
	
	/**
	 * @param billingName
	 * @param cardNumber
	 * @param address
	 * @param expiryDate
	 * @param cvv
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
	 * @param billingName the billingName to set
	 */
	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}

	/**
	 * @return the cardNumber
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
	 * @return the address
	 */
	public String getAddress() {
		return billingAddress;
	}

	/**
	 * @param address the address to set
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
