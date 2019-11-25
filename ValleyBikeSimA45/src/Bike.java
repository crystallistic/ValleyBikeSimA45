/**
 * @author maingo
 *
 */
public class Bike {
	
	
	private int bikeId; // the unique three-digit number associated with a bike

	/**
	 * @param bikeId
	 */
	public Bike(int bikeId) {
		this.bikeId = bikeId;
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
	
	
}
