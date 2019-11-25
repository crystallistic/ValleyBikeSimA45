/**
 * @author maingo
 *
 */
public class FoundingMember extends Membership {

	/**
	 * 
	 */
	public FoundingMember() {
		membershipType = "Founding Member";
		baseRate = 90;
		rideDurationLimit = 60;
	}

	
	/**
	 *
	 */
	@Override
	public float getChargeForRide(int rideDurationInMinutes) {
		
		int totalAmount = 0;
		
		// if the user's ride lasted 60 mins or more
		if (rideDurationInMinutes >= 60) {
			
			// charge $0.15 for every minute, rounding up to the next minute. 
			// If the user's ride lasted 65mins15sec, 
			// they would be charged for 66mins
			totalAmount += (1 + (rideDurationInMinutes - 60)) * 0.15;
		}
		return totalAmount;
		
	}
}
