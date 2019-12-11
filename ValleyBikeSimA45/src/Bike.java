/**
 * @author maingo
 *
 */
public class Bike {
	
	
	private int bikeId; // the unique three-digit number associated with a bike
	private String status; // working, OOO, or stolen

	/**
	 * @param bikeId Holds bike's id number, which is three numbers only
	 * @param status Holds the bike's status, which is either working, OOO(out of order) or stolen
	 */
	public Bike(int bikeId, String status) {
		this.bikeId = bikeId;
		this.status = status;
	}

	/**
	 * @return the bikeId
	 */
	public int getBikeId() {
		return bikeId;
	}

	/**
	 * @param bikeId the bikeId to set
	 */
	public void setBikeId(int bikeId) {
		this.bikeId = bikeId;
	}

	@Override
	public String toString() {
		return "Bike [bikeId=" + bikeId + "]";
	}
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	

	/**
	 * @param status the status to set. Available statuses: working, inStorage, OOO
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
