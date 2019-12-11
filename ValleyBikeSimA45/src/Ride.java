import java.util.Date;

/**
 * The Ride class contains information about a ride.
 * @author maingo
 *
 */
public class Ride {

	private int bikeId;
	private int startStationId;
	private String startStationName;
	private int endStationId;
	private String endStationName;
	private Date startTime;
	private Date endTime;
	
	/**
	 * Constructor for a Ride object, given the ride ID and bike ID, 
	 * the start station, and the start time. 
	 * End station and end time can be unknown at object creation.
	 * @param rideId
	 * @param bikeId
	 * @param startStation
	 * @param startTime
	 */
	public Ride(int bikeId, int startStationId, String startStationName, Date startTime) {

		this.bikeId = bikeId;
		this.startStationId = startStationId;
		this.startStationName = startStationName;
		this.startTime = startTime;
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
	 * @return the start station ID
	 */
	public int getStartStationId() {
		return startStationId;
	}

	/**
	 * @param startStationId the start station ID to set
	 */
	public void setStartStationId(int startStationId) {
		this.startStationId = startStationId;
	}

	/**
	 * @return the endStation ID
	 */
	public int getEndStationId() {
		return endStationId;
	}

	/**
	 * @param endStation the endStation ID to set
	 */
	public void setEndStationId(int endStationId) {
		this.endStationId = endStationId;
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
	
	/**
	 * Calculates ride duration in milliseconds and returns it
	 */
	public long getRideDuration() {
		if (endTime != null) {
			return (endTime.getTime() - startTime.getTime());
		}
		return 0;
	}

	/**
	 * @return the startStationName
	 */
	public String getStartStationName() {
		return startStationName;
	}

	/**
	 * @param startStationName the startStationName to set
	 */
	public void setStartStationName(String startStationName) {
		this.startStationName = startStationName;
	}

	/**
	 * @return the endStationName
	 */
	public String getEndStationName() {
		return endStationName;
	}

	/**
	 * @param endStationName the endStationName to set
	 */
	public void setEndStationName(String endStationName) {
		this.endStationName = endStationName;
	}

}
