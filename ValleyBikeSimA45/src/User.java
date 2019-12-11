/**
 * The User class. Note that there is no function to set username as the username cannot be changed.
 * @author maingo,  jemimahcharles, maggieburkart, emmatanur
 * 
 */
public class User {

	private String username;
	private String password;
	
	/**
	 * @param userName Holds username for current user, which must be 6 characters and only contain numbers and letters
	 * @param password Holds the password, which must be 6 characters and only contain numbers and letters
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username Holds the current user name
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password Holds the current user password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
