package controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;

import entities.Bike;
import entities.Dock;
import entities.EBike;

import exceptions.ecobike.EcoBikeException;
import exceptions.ecobike.NoInformationException;
import interfaces.ServicesProps;

import utils.Configs;
import utils.EndPoints;

import boundaries.RentBikeServiceBoundary;

import constants.BikeStatuses;


public class EcoBikeInformationController implements PropertyChangeListener {
	private static EcoBikeInformationController ecoBikeInformationController;
	private ServicesProps rentBikeServiceInterface;
	
	private ArrayList<Dock> listAllDocks = new ArrayList<Dock>();
	private ArrayList<Bike> listAllBikes = new ArrayList<Bike>();
	private ArrayList<Bike> listAllRentedBikes = new ArrayList<Bike>();
	private ArrayList<Bike> listAllFreeBikes = new ArrayList<Bike>();

	public static EcoBikeInformationController getEcoBikeInformationController() throws SQLException, EcoBikeException {
		if (ecoBikeInformationController == null) {
			ecoBikeInformationController = new EcoBikeInformationController();
			ecoBikeInformationController.listAllDocks = EndPoints.getAllDocks();
			ecoBikeInformationController.rentBikeServiceInterface = new RentBikeServiceBoundary();

			for (Dock dock: ecoBikeInformationController.listAllDocks) {
				for (Bike bike: dock.getAllBikesInDock()) {
					ecoBikeInformationController.listAllBikes.add(bike);
					ecoBikeInformationController.listAllFreeBikes.add(bike);
					bike.addStatusObserver(ecoBikeInformationController);
				}
			}

			ArrayList<Bike> rentedBikes = EndPoints.getAllRentedBikes();

			for (Bike bike: rentedBikes) {
				ecoBikeInformationController.listAllBikes.add(bike);
				ecoBikeInformationController.listAllRentedBikes.add(bike);
				bike.addStatusObserver(ecoBikeInformationController);
			}
		}

		return ecoBikeInformationController;
	}
	
	public static ServicesProps getRentBikeService() {
		return ecoBikeInformationController.rentBikeServiceInterface;
	}
	
	public ArrayList<Dock> getAllDocks() {
		return this.listAllDocks;
	}
	
	public ArrayList<Bike> getAllBikes() {
		return this.listAllBikes;
	}
	
	public ArrayList<Bike> getAllRentedBikes() {
		return this.listAllRentedBikes;
	}
	
	public ArrayList<Bike> getAllFreeBikes() {
		return this.listAllFreeBikes;
	}
	
	public Dock getDockInformationByID(int dockID) throws SQLException, EcoBikeException {
		for (Dock dock : listAllDocks) {
			if (dock.getDockID() == dockID) {
				return dock;
			}
		}
		return null;
	}
	
	public Bike getBikeInformationByName(String name) throws SQLException, EcoBikeException {
		if (String.valueOf(name) == null || String.valueOf(name).length() == 0) {
			throw new NoInformationException("EMPTY INPUT!");
		}
		
		for (Bike bike: listAllBikes) {
			if (bike.getName().toLowerCase().contains(name.toLowerCase())) {
				return bike;
			}
		}
		
		return null;
	}
	
	public String getBikeLocation(String bikeBarcode) throws SQLException, EcoBikeException {
		if (String.valueOf(bikeBarcode) == null) {
			throw new NoInformationException("no keyword to search");
		}
		
		if (String.valueOf(bikeBarcode).length() == 0) {
			throw new NoInformationException("no keyword to search");
		}	
		
		for (Bike bike: listAllBikes) {
			if (bike.getBikeBarCode().equals(bikeBarcode)) {
				return bike.getCurrentDock().getName();
			}
		}
		
		return null;
	}
	

	public float getBikeBattery(String bikeBarcode) throws SQLException, EcoBikeException {
		if (String.valueOf(bikeBarcode) == null) {
			throw new NoInformationException("no keyword to search");
		}
		
		if(String.valueOf(bikeBarcode).length() == 0) {
			throw new NoInformationException("no keyword to search");
		}	
		
		for (Bike bike: this.listAllBikes) {
			if (bike.getBikeType().equals(Configs.BikeType.EBike.toString())) {
				return (float) ((EBike) bike).getBattery();
			}
		}
		
		return -1;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object val = evt.getNewValue();
		
		if (val instanceof BikeStatuses.Instances) {
			BikeStatuses.Instances newBikeStatus = (BikeStatuses.Instances) val;
			BikeStatuses.Instances oldBikeStatus = (BikeStatuses.Instances) evt.getOldValue();
			
			Bike sourceBike = (Bike) evt.getSource();
			
			if (oldBikeStatus == BikeStatuses.Instances.FREE && newBikeStatus == BikeStatuses.Instances.RENTED) {
				this.listAllFreeBikes.remove(sourceBike);
				this.listAllRentedBikes.add(sourceBike);
			} 
			else if (newBikeStatus == BikeStatuses.Instances.FREE) {
				this.listAllFreeBikes.add(sourceBike);
				this.listAllRentedBikes.remove(sourceBike);
			}
		}
		
	}
}
