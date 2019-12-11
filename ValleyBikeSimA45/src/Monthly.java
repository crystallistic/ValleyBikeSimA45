import java.math.BigDecimal;

/**
 * @author maingo, jemimahcharles, maggieburkart, emmatanur
 *
 */
public class Monthly extends Membership {
	
	
	/**
	 * constructor for Monthly object
	 */
	public Monthly() {
		membershipType = "Monthly";
		baseRate = new BigDecimal(20.00);
		rideDurationLimit = 45;
	}
	
	/**
	 *
	 */
	@Override
	public BigDecimal getChargeForRide(int rideDurationInMinutes) {
		
		BigDecimal totalAmount = new BigDecimal(0.00);
		
		// if the user's ride lasted 45 mins or more
		if (rideDurationInMinutes >= 45) {
			
			// charge $0.15 for every minute, rounding up to the next minute. 
			// If the user's ride lasted 45mins15sec, 
			// they would be charged for 46mins
			totalAmount = totalAmount.add(new BigDecimal((1 + (rideDurationInMinutes - 45)) * 0.15));
		}
		return totalAmount;	
	}

}
