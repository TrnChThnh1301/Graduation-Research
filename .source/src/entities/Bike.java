package entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import constants.BikeStatuses;
import exceptions.ecobike.InvalidEcoBikeInformationException;
import utils.Configs;

public abstract class Bike {
	private String name;

	private Configs.BikeType bikeType;

	private String licensePlateCode;

	private String bikeImage;

	private String bikeBarcode;

	private String currencyUnit;
	
	private double deposit;

	private String createDate;
	
	private String creator;

	private BikeStatuses.Instances currentStatus;
	
	protected int saddles, pedals, rearSeats;

	protected float rentFactor;
	
	private Dock currentDock;

	
	private PropertyChangeSupport statusNotifier;
	private PropertyChangeSupport dockNotifier;
	
	protected Bike(String name, String licensePlateCode, String bikeImage, 
			String bikeBarcode, String currencyUnit, 
			String createDate) throws InvalidEcoBikeInformationException {
		this.setName(name);
		this.setLicensePlateCode(licensePlateCode);
		this.setBikeImage(bikeImage);
		this.setBikeBarCode(bikeBarcode);
		this.setCurrency(currencyUnit);
		this.setCreateDate(createDate);
		this.statusNotifier = new PropertyChangeSupport(this);
		this.dockNotifier = new PropertyChangeSupport(this);
	}

	public void addStatusObserver(PropertyChangeListener pcl) {
		this.statusNotifier.addPropertyChangeListener(pcl);
	}

	public void removeStatusObserver(PropertyChangeListener pcl) {
		this.statusNotifier.removePropertyChangeListener(pcl);
	}

	public void addDockObserver(PropertyChangeListener pcl) {
		this.dockNotifier.addPropertyChangeListener(pcl);
	}

	public void removeDockObserver(PropertyChangeListener pcl) {
		this.dockNotifier.removePropertyChangeListener(pcl);
	}
	
	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getBikeType() {
		return bikeType.toString();
	}

	protected void setBikeType(Configs.BikeType bikeType) {				
		this.statusNotifier.firePropertyChange("bikeType", this.bikeType, bikeType);
		this.bikeType = bikeType;
		this.rearSeats = Configs.BikeType.getTypeRearSeat(bikeType);
		this.saddles = Configs.BikeType.getTypeSadde(bikeType);
		this.pedals = Configs.BikeType.getTypePedals(bikeType);
		this.rentFactor = Configs.BikeType.getMultiplier(bikeType);
		this.deposit = Configs.BikeType.getTypePrice(bikeType);
	}
	
	public float getRentFactor() {
		return this.rentFactor;
	}
	
	public int getRearSeats() {
		return this.rearSeats;
	}
	
	public int getSaddle() {
		return this.saddles;
	}
	
	public int getPedals() {
		return this.pedals;
	}
	

	public String getLicensePlateCode() {
		return licensePlateCode;
	}

	private void setLicensePlateCode(String licensePlateCode) {
		this.licensePlateCode = licensePlateCode;
	}

	public String getBikeImage() {
		return bikeImage;
	}

	private void setBikeImage(String bikeImage) {
		this.bikeImage = bikeImage;
	}

	public String getBikeBarCode() {
		return bikeBarcode;
	}

	private void setBikeBarCode(String barCode) {
		this.bikeBarcode = barCode;
	}

	public double getDeposit() {
		return deposit;
	}

	@SuppressWarnings("unused")
	private void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public String getCurrency() {
		return currencyUnit;
	}

	private void setCurrency(String currency) {
		this.currencyUnit = currency;
	}

	public String getCreateDate() {
		return createDate;
	}

	private void setCreateDate(String createDate) {
		this.createDate = createDate.toString();
	}

	public BikeStatuses.Instances getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(BikeStatuses.Instances currentStatus) {
		this.statusNotifier.firePropertyChange("currentStatus", this.currentStatus, currentStatus);
		this.currentStatus = currentStatus;
	}
	
	public void goToDock(Dock dock) {
		this.currentDock = dock;
		this.addDockObserver(dock);
		dock.addBikeToDock(this);
		this.setCurrentStatus(BikeStatuses.Instances.FREE);
		this.dockNotifier.firePropertyChange("currentStatus", this.currentStatus, currentStatus);
	}

	public void getOutOfDock() {
		this.setCurrentStatus(BikeStatuses.Instances.RENTED);
		this.currentDock.removeBikeFromDock(this);
		this.removeDockObserver(this.currentDock);
		this.currentDock = null;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Dock getCurrentDock() {
		return this.currentDock;
	}
}
