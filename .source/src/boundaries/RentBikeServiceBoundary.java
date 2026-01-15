package boundaries;

import java.io.IOException;
import java.sql.SQLException;

import constants.Paths;
import controllers.RentBikeController;
import entities.Bike;
import entities.Dock;
import exceptions.ecobike.EcoBikeException;
import exceptions.ecobike.EcoBikeUndefinedException;
import exceptions.ecobike.RentBikeException;
import interfaces.ServicesProps;
import javafx.stage.Stage;
import views.screen.PayForDepositScreenHandler;
import views.screen.PayForRentScreenHandler;

public class RentBikeServiceBoundary implements ServicesProps {
	public RentBikeServiceBoundary() {
		super();
	}
	
	public void rentBike(Bike bike) throws IOException, EcoBikeException, SQLException {
		PayForDepositScreenHandler paymentScreenHandler = PayForDepositScreenHandler.getPayForDepositScreenHandler(new Stage(), Paths.PAY_FOR_DEPOSIT_SCREEN, null, bike);
		paymentScreenHandler.show();
	}
	
	public void returnBike(Bike bike, Dock dock) throws RentBikeException, EcoBikeUndefinedException, IOException {
		PayForRentScreenHandler paymentScreenHandler = PayForRentScreenHandler.getPayForRentScreenHandler(new Stage(), Paths.PAY_FOR_RENTAL_SCREEN, null, bike, dock);
		paymentScreenHandler.show();
	}
	
	public void pauseBikeRental(Bike bike) throws EcoBikeException, SQLException {
		RentBikeController.getRentBikeServiceController(null).pauseBikeRental(bike);	
	}
	
	public void resumeBikeRental(Bike bike) throws EcoBikeException, SQLException {
		RentBikeController.getRentBikeServiceController(null).resumeBikeRental(bike);	
	}
}
