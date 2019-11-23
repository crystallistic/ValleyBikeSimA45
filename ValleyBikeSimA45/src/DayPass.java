/**
 * 
 */

/**
 * @author maingo
 *
 */
public class DayPass extends Membership {

	/**
	 * 
	 */
	public DayPass() {
		baseRate = 6;
		rideDurationLimit = 30;
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
