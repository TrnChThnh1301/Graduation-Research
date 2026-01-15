package views.screen;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import boundaries.InterbankBoundary;
import controllers.RentBikeController;
import entities.Bike;
import entities.CreditCard;
import entities.strategies.DepositFactory;
import exceptions.ecobike.EcoBikeException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import layouts.BaseLayout;
import views.screen.popup.ToastContainer;

public class PayForDepositScreenHandler extends PaymentScreenHandler {
    @FXML
    private Label depositPrice;
    private static PayForDepositScreenHandler paymentScreenHandler;

    private Bike bikeToRent;
    
    private PayForDepositScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }
    
    public static PayForDepositScreenHandler getPayForDepositScreenHandler(Stage stage, String screenPath, BaseLayout prevScreen, Bike bike) throws IOException {
    	if (paymentScreenHandler == null) {
    		paymentScreenHandler = new PayForDepositScreenHandler(stage, screenPath);
    		paymentScreenHandler.setScreenTitle("Register Payment Method");
    	}
    	
    	if (bike != null) {
    		paymentScreenHandler.bikeToRent = bike;
    	}
    	
    	paymentScreenHandler.initialize();
    	
    	return paymentScreenHandler;
    	
    }
    
    protected void initialize() {    
    	super.initializeComponent();
    	bikeName.setText(this.bikeToRent.getName());
    	bikeType.setText(this.bikeToRent.getBikeType());
    	depositPrice.setText(
    		Double.toString(
    			DepositFactory.getDepositStrategy().getDepositPrice((float) bikeToRent.getDeposit())
    		) + " " + this.bikeToRent.getCurrency()
    	);
    }

    public void confirmPaymentMethod() throws EcoBikeException, SQLException, IOException, ParseException {
   		CreditCard card = new CreditCard(cardHolderName.getText(), cardNumber.getText(), securityCode.getText(), expirationDate.getText());
   		InterbankBoundary interbank = new InterbankBoundary("ACB");
   		
   		if (RentBikeController.getRentBikeServiceController(interbank).rentBike(bikeToRent, card)) {
   			ToastContainer.success("Successfully rent " + bikeToRent.getName() + "!");
   			this.stage.hide();
   		} else {
   			ToastContainer.error("TRANSACTION_ERROR");
   		}
    }
}
