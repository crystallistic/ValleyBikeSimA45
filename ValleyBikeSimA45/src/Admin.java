/**
 * @author maingo, jemimahcharles, maggieburkart, emmatanur
 *
 */
public class Admin extends User {

	/**
	 * @param userName Holds username for current user, which must be 6 characters and only contain numbers and letters
	 * @param password Holds the password, which must be 6 characters and only contain numbers and letters
	 */
	public Admin(String userName, String password) {
		super(userName, password);	
	}
}
