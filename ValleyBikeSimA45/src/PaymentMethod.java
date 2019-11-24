/**
 * @author maingo
 *
 */
public class PaymentMethod {
	
	String billingName;
	String cardNumber;
	String billingAddress;
	String expiryDate;
	String cvv;
	
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
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
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
	
}
