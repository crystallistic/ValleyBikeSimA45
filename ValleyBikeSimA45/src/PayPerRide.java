/**
 * @author maingo
 *
 */
public class PayPerRide extends Membership {

	/**
	 * 
	 */
	public PayPerRide() {
		membershipType = "Pay Per Ride";
		baseRate = 2;
		rideDurationLimit = 30;
	}
	
	/**
	 *
	 */
	@Override
	public float getChargeForRide(int rideDurationInMinutes) {
		
		// user pays $2 for every ride, plus overtime fee if applicable
		float totalAmount = 2;
		
		// if the user's ride lasted 30 mins or more
		if (rideDurationInMinutes >= 30) {
			
			// charge $0.15 for every minute, rounding up to the next minute. 
			// If the user's ride lasted 32mins15sec, 
			// they would be charged for 33mins
			totalAmount += (1 + (rideDurationInMinutes - 30)) * 0.15;
		}
		return totalAmount;
		
	}

	/**
	 *
	 */
	@Override
	public int getRideDurationLimit() {
		return rideDurationLimit;
	}

}
