/**
 * 
 */

/**
 * @author maingo
 *
 */
public class FoundingMember extends Membership {

	/**
	 * 
	 */
	public FoundingMember() {
		baseRate = 90;
		rideDurationLimit = 60;
	}

	@Override
	public void chargeForRide() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getRideDurationLimit() {
		// TODO Auto-generated method stub
		return rideDurationLimit;
	}

}
