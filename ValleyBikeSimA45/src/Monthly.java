import java.util.Date;

/**
 * 
 */

/**
 * @author maingo
 *
 */
public class Monthly extends Membership {
	
	
	/**
	 * 
	 */
	public Monthly() {
		baseRate = 20;
		rideDurationLimit = 45;
	}

	@Override
	public int getRideDurationLimit() {
		return rideDurationLimit;
	}

}
