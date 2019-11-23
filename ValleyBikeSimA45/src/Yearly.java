/**
 * @author maingo
 *
 */
public class Yearly extends Membership {

	/**
	 * 
	 */
	public Yearly() {
		// TODO Auto-generated constructor stub
		baseRate = 80;
		rideDurationLimit = 45;
	}

	@Override
	public int getRideDurationLimit() {
		// TODO Auto-generated method stub
		return rideDurationLimit;
	}

}
