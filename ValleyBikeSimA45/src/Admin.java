/**
 * @author maingo
 *
 */
public class Admin extends User {

	/**
	 * @param userName
	 * @param password
	 */
	public Admin(String userName, String password) {
		super(userName, password);	
	}
	
	@Override
	public String toString() {
		return "Admin [userName=" + super.getUserName() + ", password=" + super.getPassword() + "]";
	}

}
