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
	public int getRideDurationLimit() {
		// TODO Auto-generated method stub
		return rideDurationLimit;
	}

}
