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
	public int getRideDurationLimit() {
		return rideDurationLimit;
	}

}
