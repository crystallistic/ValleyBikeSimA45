/**
 * @author maingo
 *
 */
public class Rider extends User {
	
	String fullName;
	String phoneNumber;
	String address;

	/**
	 * @param userName
	 * @param password
	 */
	public Rider(String userName, String password, String fullName, String phoneNumber, String address) {
		super(userName, password);
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}

	/**
	 * 
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

}
