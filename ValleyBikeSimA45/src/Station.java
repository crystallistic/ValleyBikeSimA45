/**
 * 
 */

/**
 * @author maingo
 *
 */

public class Station {
	
	
	int stationId;
	String address;
	int numBikes;
	int numFreeDocks;
	int capacity;
	int maintenanceReqs;
	boolean hasKiosk;
	
	
	/**
	 * Getter for stationId
	 * @return the station's id number
	 */
	public int getStationId() {
		return stationId;
	}
	/**
	 * Sets the station ID to the given value
	 * @param stationId The station's id number
	 */
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	/**
	 * @return
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return
	 */
	public int getNumBikes() {
		return numBikes;
	}
	/**
	 * @param numBikes
	 */
	public void setNumBikes(int numBikes) {
		this.numBikes = numBikes;
	}
	/**
	 * @return
	 */
	public int getNumFreeDocks() {
		return numFreeDocks;
	}
	/**
	 * @param numFreeDocks
	 */
	public void setNumFreeDocks(int numFreeDocks) {
		this.numFreeDocks = numFreeDocks;
	}
	/**
	 * @return
	 */
	public int getCapacity() {
		return capacity;
	}
	/**
	 * @param capacity
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	/**
	 * @return
	 */
	public int getMaintenanceReqs() {
		return maintenanceReqs;
	}
	/**
	 * @param maintenanceReqs
	 */
	public void setMaintenanceReqs(int maintenanceReqs) {
		this.maintenanceReqs = maintenanceReqs;
	}
	/**
	 * @return
	 */
	public boolean isHasKiosk() {
		return hasKiosk;
	}
	/**
	 * @param hasKiosk
	 */
	public void setHasKiosk(boolean hasKiosk) {
		this.hasKiosk = hasKiosk;
	}
}
