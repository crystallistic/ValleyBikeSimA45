import java.util.Date;

/**
 * 
 */

/**
 * @author maingo
 *
 */
public class PayPerRide extends Membership {

	/**
	 * 
	 */
	public PayPerRide() {
		baseRate = 2;
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
