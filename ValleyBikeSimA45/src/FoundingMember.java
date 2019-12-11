import java.math.BigDecimal;

/**
 * @author maingo, jemimahcharles, maggieburkart, emmatanur
 * Object to store Founding Member membership information
 */
public class FoundingMember extends Membership {

	/**
	 * constructor for of founding member object
	 */
	public FoundingMember() {
		membershipType = "Founding Member";
		baseRate = new BigDecimal(90.00);
		rideDurationLimit = 60;
	}

	
	/**
	 *
	 */
	@Override
	public BigDecimal getChargeForRide(int rideDurationInMinutes) {
		
		BigDecimal totalAmount = new BigDecimal(0.00);
		
		// if the user's ride lasted 60 mins or more
		if (rideDurationInMinutes >= 60) {
			
			// charge $0.15 for every minute, rounding up to the next minute. 
			// If the user's ride lasted 65mins15sec, 
			// they would be charged for 66mins
			totalAmount = totalAmount.add(new BigDecimal((1 + (rideDurationInMinutes - 60)) * 0.15));
		}
		return totalAmount;
		
	}
}
