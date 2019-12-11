import java.math.BigDecimal;

/**
 * @author maingo, jemimahcharles, maggieburkart, emmatanur
 * Object to store the Day Pass membership
 */
public class DayPass extends Membership {

	/**
	 * constructor for the day pass object
	 */
	public DayPass() {
		membershipType = "Day Pass";
		baseRate = new BigDecimal(6.00);
		rideDurationLimit = 30;
	}
	
	/**
	 *@param rideDurationInMinutes Holds how long the users ride has been 
	 */
	@Override
	public BigDecimal getChargeForRide(int rideDurationInMinutes) {
		
		BigDecimal totalAmount = new BigDecimal(0.00);
		
		// if the user's ride lasted 30 mins or more
		if (rideDurationInMinutes >= 30) {
			
			// charge $0.15 for every minute, rounding up to the next minute. 
			// If the user's ride lasted 36mins15sec, 
			// they would be charged for 37mins
			totalAmount = totalAmount.add(new BigDecimal((1 + (rideDurationInMinutes - 30)) * 0.15));
		}
		return totalAmount;
		
	}
}
