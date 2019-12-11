import java.math.BigDecimal;

/**
 * @author maingo, jemimahcharles, maggieburkart, emmatanur
 * Object to store membership information
 */
public abstract class Membership {
	
	public String membershipType;
	public BigDecimal baseRate;
	public int rideDurationLimit;
	
	/** get the duration limit for a ride before the user gets charged overtime fees,
	 *  corresponding to the membership */
	public int getRideDurationLimit() {
		return rideDurationLimit;
	};
	
	/** given the ride duration, calculate the amount the user would be charged for the ride */
	public abstract BigDecimal getChargeForRide(int rideDurationInMinutes);
	
	/**
	 * @return the baseRate
	 */
	public BigDecimal getBaseRate() {
		return baseRate;
	}

	/**
	 * @param baseRate the baseRate to set
	 */
	public void setBaseRate(BigDecimal baseRate) {
		this.baseRate = baseRate;
	}

	/**
	 * @return the membershipType
	 */
	public String getMembershipType() {
		return membershipType;
	}

	/**
	 * @param membershipType the membershipType to set
	 */
	public void setMembershipType(String membershipType) {
		this.membershipType = membershipType;
	}

	/**
	 * @param rideDurationLimit the rideDurationLimit to set
	 */
	public void setRideDurationLimit(int rideDurationLimit) {
		this.rideDurationLimit = rideDurationLimit;
	}
	
}
