package interfaces;

import java.io.IOException;
import java.sql.SQLException;

import entities.Bike;
import entities.Dock;

import exceptions.ecobike.EcoBikeException;
import exceptions.ecobike.EcoBikeUndefinedException;
import exceptions.ecobike.RentBikeException;

public interface ServicesProps {
	public void rentBike(Bike bike) throws RentBikeException, EcoBikeUndefinedException, IOException, EcoBikeException, SQLException;

	public void returnBike(Bike bike, Dock dock) throws RentBikeException, EcoBikeUndefinedException, IOException;

	public void pauseBikeRental(Bike bike) throws RentBikeException, EcoBikeUndefinedException, EcoBikeException, SQLException;

	public void resumeBikeRental(Bike bike) throws EcoBikeException, SQLException;
}
