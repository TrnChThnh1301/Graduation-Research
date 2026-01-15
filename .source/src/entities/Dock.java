package entities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.ecobike.EcoBikeException;

public class Dock implements PropertyChangeListener {
	private String name;
	
	private int dockID;

	private String dockAddress;

	private double dockArea;

	private int numAvailableBike;

	private int numDockSpaceFree;
	
	private int totalSpace;
	
	private String dockImage;
	
	private PropertyChangeSupport propertyNotifier;
	
	private ArrayList<Bike> bikeInDock;
	
	public Dock(String name, int dockID, String dockAddress, double dock_area, int totalSpace, String dockImage) throws SQLException, EcoBikeException {
		this.setName(name);
		this.setDockID(dockID);
		this.setDockAddress(dockAddress);
		this.setDockArea(dock_area);
		this.totalSpace = totalSpace;
		this.numDockSpaceFree = totalSpace;
		this.setDockImage(dockImage);
		this.bikeInDock = new ArrayList<Bike>();
		this.propertyNotifier = new PropertyChangeSupport(this);
	}

	public void addObserver(PropertyChangeListener pcl) {
		this.propertyNotifier.addPropertyChangeListener(pcl);
	}
	
	public void removeObserver(PropertyChangeListener pcl) {
		this.propertyNotifier.removePropertyChangeListener(pcl);
	}
	
	public String getDockImage() {
		return dockImage;
	}

	private void setDockImage(String dockImage) {
		this.dockImage = dockImage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDockID() {
		return dockID;
	}

	public void setDockID(int dockID) {
		this.dockID = dockID;
	}

	public String getDockAddress() {
		return dockAddress;
	}

	public void setDockAddress(String dockAddress) {
		this.dockAddress = dockAddress;
	}

	public double getDockArea() {
		return dockArea;
	}

	public void setDockArea(double dockArea) {
		this.dockArea = dockArea;
	}

	public int getNumAvailableBike() {
		return numAvailableBike;
	}

	public void setNumAvailableBike(int numAvailableBike) {
		this.numAvailableBike = numAvailableBike;
	}

	public int getNumDockSpaceFree() {
		return numDockSpaceFree;
	}

	public void setNumDockSpaceFree(int numDockSpaceFree) {
		this.numDockSpaceFree = numDockSpaceFree;
	}
	
	public String toString() {
		try {
			JSONObject obj = new JSONObject();

			obj.put("name", this.getName());
			obj.put("dock_id", this.getDockID());
			obj.put("dock_address", this.getDockAddress());
			obj.put("dock_area", this.getDockArea());
			obj.put("num_available_bike", this.getNumAvailableBike());
			obj.put("num_free_dock", this.getNumDockSpaceFree());
			obj.put("dock_image", this.getDockImage());
			
			return obj.toString();
		} catch (Exception | JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public ArrayList<Bike> getAllBikesInDock() {
		return this.bikeInDock;
	}

	public void addBikeToDock(Bike bike) {
		if (!this.bikeInDock.contains(bike)) {
			this.bikeInDock.add(bike);
			this.numDockSpaceFree -= 1;
			this.numAvailableBike += 1;
			this.propertyNotifier.firePropertyChange("numDockSpaceFree", this.numDockSpaceFree + 1, this.numDockSpaceFree);	
		}
	}
	
	public void removeBikeFromDock(Bike bike) {
		if (this.bikeInDock.contains(bike)) {
			this.bikeInDock.remove(bike);
			this.numDockSpaceFree += 1;
			this.numAvailableBike -= 1;
			this.propertyNotifier.firePropertyChange("numDockSpaceFree", this.numDockSpaceFree - 1, this.bikeInDock);
		}
	}
	
	public int getTotalSpace() {
		return this.totalSpace;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {}
	
	
}
