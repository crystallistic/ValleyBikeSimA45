import java.util.Date;

/**
 * The Ride class contains information about a ride.
 * @author maingo, jemimahcharles, maggieburkart, emmatanur
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
	 * @param bikeId Holds the bike id associated with bike of the rider's ride
	 * @param startStationId Holds the station id where the rider's bike came from
	 * @param startStationName Holds the station name where the rider's bike came from
	 * @param startTime Holds the start time of when the rider began a ride
	 */
	public Ride(int bikeId, int startStationId, String startStationName, Date startTime) {

		this.bikeId = bikeId;
		this.startStationId = startStationId;
		this.startStationName = startStationName;
		this.startTime = startTime;
		this.endTime = null;
	}

	/**
	 * @return the bikeId of the ride in progress associated with the user
	 */
	public int getBikeId() {
		return bikeId;
	}

	/**
	 * @param bikeId the bikeId to set to the bike chosen by the user
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
	 * @return the startStationName associated with the bike that is checked out
	 */
	public String getStartStationName() {
		return startStationName;
	}

	/**
	 * @param startStationName the startStationName of the bike that is checked out
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
	 * @param endStationName the endStationName is the end station chosen by the user once they have finished their ride
	 */
	public void setEndStationName(String endStationName) {
		this.endStationName = endStationName;
	}

}
