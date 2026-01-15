package controllers;

import exceptions.ecobike.EcoBikeException;
import interfaces.InterbankProps;
import entities.Bike;
import entities.BikeTracker;
import entities.CreditCard;
import entities.Dock;
import entities.Invoice;
import entities.PaymentTransaction;
import entities.strategies.RentalFactory;
import utils.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

import constants.BikeStatuses;
import constants.MockCreditCard;

public class RentBikeController {
	private static int invoiceCounter = 1;
	private InterbankProps interbankSystem;
	private static RentBikeController rentBikeServiceController;
	private HashMap<String, BikeTracker> listBikeTracker;
	

	public static RentBikeController getRentBikeServiceController(InterbankProps interbankSystem){
		if (rentBikeServiceController == null) {
			rentBikeServiceController = new RentBikeController();
		}
		if (interbankSystem != null) {
			rentBikeServiceController.interbankSystem = interbankSystem;
		}
		return rentBikeServiceController;
	}

	private RentBikeController() {
		this.listBikeTracker = new HashMap<String, BikeTracker>();
	}

	public boolean rentBike(Bike bikeToRent, CreditCard card) throws EcoBikeException, SQLException, IOException {

		if (!checkCardIdentity(card)) {
			return false;
		}
		
		PaymentTransaction transaction = interbankSystem.payDeposit(card, bikeToRent.getDeposit(), "PAY_DEPOSIT");
		
		if (transaction == null) {
			return false;
		}
		
		int rentID = EndPoints.addStartRentBikeRecord(bikeToRent.getBikeBarCode());
		transaction.setRentID(rentID);
		EndPoints.removeBikeFromDock(bikeToRent.getBikeBarCode());
		bikeToRent.getOutOfDock();

		BikeTracker newTracker = new BikeTracker(bikeToRent, rentID);
		EndPoints.addTransaction(transaction, rentID);
		newTracker.addTransaction(transaction);		
		newTracker.startCountingRentTime();
		EndPoints.changeBikeStatus(bikeToRent.getBikeBarCode(), BikeStatuses.Instances.RENTED.toString());
		this.listBikeTracker.put(bikeToRent.getBikeBarCode(), newTracker);
		
		return true;
	}
	
	private boolean checkCardIdentity(CreditCard card) {
		if (!card.getCardHolderName().equalsIgnoreCase(MockCreditCard.CardHolderName)) {
			return false;
		}
		
		if (!card.getCardNumber().equalsIgnoreCase(MockCreditCard.CardNumber)) {
			return false;
		}
		
		if (!card.getExpirationDate().equalsIgnoreCase(MockCreditCard.ExpirationDate)) {
			return false;
		}
		
		if (!card.getCardSecurity().equalsIgnoreCase(MockCreditCard.CCV)) {
			return false;
		}
		
		return true;
	}
	
	public int pauseBikeRental(Bike bike) {
		BikeTracker tracker = this.listBikeTracker.get(bike.getBikeBarCode());
		
		if (tracker == null) {
			try {
				tracker = EndPoints.getCurrentBikeRenting(bike);
				if (tracker != null) {
					listBikeTracker.put(bike.getBikeBarCode(), tracker);
				}
			} catch (SQLException | EcoBikeException e) {
				e.printStackTrace();
			}
		}
		
		try {
			tracker.stopCountingRentTime();
			bike.setCurrentStatus(BikeStatuses.Instances.PAUSED);
		} catch (SQLException | EcoBikeException e) {
			e.printStackTrace();
		}
		
		return tracker.getRentedTime();
		
	}
	
	public void resumeBikeRental(Bike bike) {
		BikeTracker tracker = this.listBikeTracker.get(bike.getBikeBarCode());
		
		if (tracker == null) {
			try {
				int rentPeriod = EndPoints.getCurrentRentPeriodOfBike(bike.getBikeBarCode());
				if (rentPeriod != 0) {
					tracker = EndPoints.getCurrentBikeRenting(bike);
					tracker.resumeCountingRentTime();
				}
			} catch (SQLException | EcoBikeException e) {
				e.printStackTrace();
			}
		}
		
		try {
			tracker.resumeCountingRentTime();
			bike.setCurrentStatus(BikeStatuses.Instances.RENTED);
		} catch (SQLException | EcoBikeException e) {
			e.printStackTrace();
		}
	}
	
	public Invoice returnBike(Bike bikeToRent, Dock dockToReturn, CreditCard card) throws IOException, SQLException, EcoBikeException, ParseException {
		if (!checkCardIdentity(card)) {
			return null;
		}
		
		BikeTracker tracker = this.listBikeTracker.get(bikeToRent.getBikeBarCode());
		
		if (tracker == null) {
			tracker = EndPoints.getCurrentBikeRenting(bikeToRent);
		}
		
		int period = tracker.stopCountingRentTime();
		float rentCost = calculateFee(bikeToRent.getRentFactor(), period);
		
		PaymentTransaction transaction = interbankSystem.payRental(card, rentCost, "PAY_RENTAL");
		
		if (transaction == null) {
			return null;
		}
		
		EndPoints.addEndRentBikeRecord(tracker.getRentID(), period);
		transaction.setRentID(tracker.getRentID());;
		EndPoints.addTransaction(transaction, tracker.getRentID());
		tracker.addTransaction(transaction);
		Invoice invoice = tracker.createInvoice(invoiceCounter);
		invoiceCounter++;
		EndPoints.changeBikeStatus(bikeToRent.getBikeBarCode(), BikeStatuses.Instances.FREE.toString());
		bikeToRent.goToDock(dockToReturn);
		EndPoints.addBikeToDock(bikeToRent.getBikeBarCode(), dockToReturn.getDockID());
		this.listBikeTracker.remove(bikeToRent.getBikeBarCode());
		
		return invoice;
	}
		
	
	
	private float calculateFee(float factor, int rentTime) {
		return RentalFactory.getRentalStrategy(rentTime).getRentalPrice(factor, rentTime);
	}

	public float getRentalFee(Bike bike)  {
		BikeTracker tracker = this.listBikeTracker.get(bike.getBikeBarCode());
		
		if (tracker == null) {
			try {
				tracker = EndPoints.getCurrentBikeRenting(bike);
				if (tracker == null) {
					return -1;
				}
			} catch (SQLException | EcoBikeException e) {
				e.printStackTrace();
			}
		}
		
		return calculateFee(bike.getRentFactor(), tracker.getRentedTime());
	}
}
