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
	
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getNumBikes() {
		return numBikes;
	}
	public void setNumBikes(int numBikes) {
		this.numBikes = numBikes;
	}
	public int getNumFreeDocks() {
		return numFreeDocks;
	}
	public void setNumFreeDocks(int numFreeDocks) {
		this.numFreeDocks = numFreeDocks;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getMaintenanceReqs() {
		return maintenanceReqs;
	}
	public void setMaintenanceReqs(int maintenanceReqs) {
		this.maintenanceReqs = maintenanceReqs;
	}
	public boolean isHasKiosk() {
		return hasKiosk;
	}
	public void setHasKiosk(boolean hasKiosk) {
		this.hasKiosk = hasKiosk;
	}
	
	
}
