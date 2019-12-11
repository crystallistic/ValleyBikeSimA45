import java.math.BigDecimal;

/**
 * @author maingo, jemimahcharles, maggieburkart, emmatanur
 *
 */
public class PayPerRide extends Membership {

	/**
	 * constructor of Pay Per Ride object
	 */
	public PayPerRide() {
		membershipType = "Pay Per Ride";
		baseRate = new BigDecimal(2.00);
		rideDurationLimit = 30;
	}
	
	/**
	 *
	 */
	@Override
	public BigDecimal getChargeForRide(int rideDurationInMinutes) {
		
		// user pays $2 for every ride, plus overtime fee if applicable
		BigDecimal totalAmount = new BigDecimal(2.00);
		
		// if the user's ride lasted 30 mins or more
		if (rideDurationInMinutes >= 30) {
			
			// charge $0.15 for every minute, rounding up to the next minute. 
			// If the user's ride lasted 32mins15sec, 
			// they would be charged for 33mins
			totalAmount = totalAmount.add(new BigDecimal((1 + (rideDurationInMinutes - 30)) * 0.15));
		}
		return totalAmount;
		
	}

	/**
	 *@return rideDurationLimit Holds the amount of time a rider can ride without being charged for overtime
	 */
	@Override
	public int getRideDurationLimit() {
		return rideDurationLimit;
	}

}
