package entities;

import exceptions.ecobike.InvalidEcoBikeInformationException;

import interfaces.ElectricBikeProps;

import utils.Configs;

public class EBike extends Bike implements ElectricBikeProps {
	private double battery;

	public EBike(
			String name, 
			String licensePlateCode, 
			String bikeImage, 
			String bikeBarcode, 
			String currencyUnit, 
			String createDate, 
			float battery
		) 
		throws InvalidEcoBikeInformationException {
			super(name, licensePlateCode, bikeImage, bikeBarcode, currencyUnit, createDate);
			this.setBikeType(Configs.BikeType.EBike);
			this.setBattery(battery);
	}	

	public float getBattery() {
		return (float)this.battery;
	}
	
	public void setBattery(double battery) {
		this.battery = battery;
	}
}
