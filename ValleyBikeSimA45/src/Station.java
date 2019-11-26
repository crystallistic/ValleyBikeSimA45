/**
 * @author maingo
 *
 */

public class Station {
	
	
	int stationId;
	String stationName;
	int numBikes;
	String address;
	int numFreeDocks;
	int capacity;
	int maintenanceReqs;
	boolean hasKiosk;
	
	
	/**
	 * 
	 * Constructor for the Station object.
	 * @param stationId
	 * @param address
	 * @param numBikes
	 * @param numFreeDocks
	 * @param capacity
	 * @param maintenanceReqs
	 * @param hasKiosk
	 */
	public Station(int stationId, String stationName, String address, int numBikes, 
			int numFreeDocks, int capacity, int maintenanceReqs, boolean hasKiosk) {
		this.stationId = stationId;
		this.stationName = stationName;
		this.numBikes = numBikes;
		this.address = address;
		this.numFreeDocks = numFreeDocks;
		this.capacity = capacity;
		this.maintenanceReqs = maintenanceReqs;
		this.hasKiosk = hasKiosk;
	}

	/**
	 * Getter for stationId
	 * @return the station's ID number
	 */
	public int getStationId() {
		return stationId;
	}
	/**
	 * Sets the station ID to the given value
	 * @param stationId 	The station's id number
	 */
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	/**
	 * @return the address of the station
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address 		the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the number of free docks
	 */
	public int getNumFreeDocks() {
		return numFreeDocks;
	}
	/**
	 * @param numFreeDocks 		the number of free docks to set
	 */
	public void setNumFreeDocks(int numFreeDocks) {
		this.numFreeDocks = numFreeDocks;
	}
	/**
	 * @return the capacity of the station 
	 */
	public int getCapacity() {
		return capacity;
	}
	/**
	 * @param capacity	the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	/**
	 * @return the number of maintenance requests
	 */
	public int getMaintenanceReqs() {
		return maintenanceReqs;
	}
	/**
	 * @param maintenanceReqs	the number of maintenance requests to set
	 */
	public void setMaintenanceReqs(int maintenanceReqs) {
		this.maintenanceReqs = maintenanceReqs;
	}
	/**
	 * @return true if this station has a kiosk, else false
	 */
	public boolean isHasKiosk() {
		return hasKiosk;
	}
	/**
	 * @param hasKiosk		boolean to set for whether this station has a kiosk or not
	 */
	public void setHasKiosk(boolean hasKiosk) {
		this.hasKiosk = hasKiosk;
	}

	/**
	 * @return the stationName
	 */
	public String getStationName() {
		return stationName;
	}

	/**
	 * @param stationName the stationName to set
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Override
	public String toString() {
		return stationId + "\t" + stationName + "\t" + numBikes + "\t" + address + "\t"
				+ numFreeDocks + "\t" + maintenanceReqs + "\t" + capacity + "\t" +
				hasKiosk + address + "\n";
	}
	
	
	
}
