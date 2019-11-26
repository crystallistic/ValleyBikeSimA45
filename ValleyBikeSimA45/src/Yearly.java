/**
 * @author maingo
 *
 */
public class Yearly extends Membership {

	/**
	 * 
	 */
	public Yearly() {
		membershipType = "Yearly";
		baseRate = 80;
		rideDurationLimit = 45;
	}
	
	/**
	 *
	 */
	@Override
	public float getChargeForRide(int rideDurationInMinutes) {
		
		float totalAmount = 0;
		
		// if the user's ride lasted 45 mins or more
		if (rideDurationInMinutes >= 45) {
			
			// charge $0.15 for every minute, rounding up to the next minute. 
			// If the user's ride lasted 45mins15sec, 
			// they would be charged for 46mins
			totalAmount += (1 + (rideDurationInMinutes - 45)) * 0.15;
		}
		return totalAmount;
		
	}
}
