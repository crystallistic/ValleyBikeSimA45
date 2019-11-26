/**
 * @author maingo
 *
 */
public class DayPass extends Membership {

	/**
	 * 
	 */
	public DayPass() {
		membershipType = "Day Pass";
		baseRate = 6;
		rideDurationLimit = 30;
	}
	
	/**
	 *
	 */
	@Override
	public float getChargeForRide(int rideDurationInMinutes) {
		
		float totalAmount = 0;
		
		// if the user's ride lasted 30 mins or more
		if (rideDurationInMinutes >= 30) {
			
			// charge $0.15 for every minute, rounding up to the next minute. 
			// If the user's ride lasted 36mins15sec, 
			// they would be charged for 37mins
			totalAmount += (1 + (rideDurationInMinutes - 30)) * 0.15;
		}
		return totalAmount;
		
	}
}
