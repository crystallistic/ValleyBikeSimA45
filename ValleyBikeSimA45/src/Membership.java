import java.util.Date;

/**
 * @author maingo
 *
 */
public abstract class Membership {
	
	public int baseRate;
	public int rideDurationLimit;
	
	/** charge the user for a ride */
	public abstract void chargeForRide();
	
	/** get the duration limit for a ride before the user gets charged overtime fees,
	 *  corresponding to the membership */
	public abstract int getRideDurationLimit();

	/**
	 * @return the baseRate
	 */
	public int getBaseRate() {
		return baseRate;
	}

	/**
	 * @param baseRate the baseRate to set
	 */
	public void setBaseRate(int baseRate) {
		this.baseRate = baseRate;
	}

	/**
	 * @param rideDurationLimit the rideDurationLimit to set
	 */
	public void setRideDurationLimit(int rideDurationLimit) {
		this.rideDurationLimit = rideDurationLimit;
	}
	
}
