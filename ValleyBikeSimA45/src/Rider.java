/**
 * @author maingo
 *
 */
public class Rider extends User {
	
	private String fullName;
	private String phoneNumber;
	private String address;
	private String email;

	/**
	 * @param Username
	 * @param password
	 */
	public Rider(String username, String password, String fullName, String email, String phoneNumber, String address) {
		super(username, password);
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.email = email;
	}

	/**
	 * 
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return super.getUsername();
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

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}	
	
	@Override
	public String toString() {
		String heading = "Username\tFull Name\tEmail\t\t\tPhone Number\tAddress\n";
		String line = super.getUsername()+"\t"+fullName+"\t"+email+"\t"+phoneNumber+"\t"+address+"\n";
		return heading+line;
	}

}
