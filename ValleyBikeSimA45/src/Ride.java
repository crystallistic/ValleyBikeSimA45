import java.util.Date;

/**
 * The Ride class contains information about a ride.
 * @author maingo
 *
 */
public class Ride {

	int bikeId;
	Station startStation;
	Station endStation;
	Date startTime;
	Date endTime;
	
	/**
	 * Constructor for a Ride object, given the ride ID and bike ID, 
	 * the start station, and the start time. 
	 * End station and end time can be unknown at object creation.
	 * @param rideId
	 * @param bikeId
	 * @param startStation
	 * @param startTime
	 */
	public Ride(int bikeId, Station startStation, Date startTime) {

		this.bikeId = bikeId;
		this.startStation = startStation;
		this.startTime = startTime;
		this.endStation = null;
		this.endTime = null;
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

	/**
	 * @return the startStation
	 */
	public Station getStartStation() {
		return startStation;
	}

	/**
	 * @param startStation the startStation to set
	 */
	public void setStartStation(Station startStation) {
		this.startStation = startStation;
	}

	/**
	 * @return the endStation
	 */
	public Station getEndStation() {
		return endStation;
	}

	/**
	 * @param endStation the endStation to set
	 */
	public void setEndStation(Station endStation) {
		this.endStation = endStation;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
}
