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
	public int getRideDurationLimit() {
		// TODO Auto-generated method stub
		return rideDurationLimit;
	}

}
