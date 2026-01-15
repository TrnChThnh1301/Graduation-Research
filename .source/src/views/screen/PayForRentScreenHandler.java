package views.screen;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import boundaries.InterbankBoundary;
import controllers.RentBikeController;
import entities.Bike;
import entities.CreditCard;
import entities.Dock;
import entities.Invoice;
import exceptions.ecobike.EcoBikeException;
import javafx.stage.Stage;
import layouts.BaseLayout;
import views.screen.popup.ToastContainer;

public class PayForRentScreenHandler extends PaymentScreenHandler {
	
    private static PayForRentScreenHandler paymentScreenHandler;

    private Bike bikeToRent;

		private Dock dockToReturn;
    
    private PayForRentScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }
    
    public static PayForRentScreenHandler getPayForRentScreenHandler(Stage stage, String screenPath, BaseLayout prevScreen, Bike bike, Dock dock) throws IOException {
    	if (paymentScreenHandler == null) {
    		paymentScreenHandler = new PayForRentScreenHandler(stage, screenPath);
    		paymentScreenHandler.setScreenTitle("Register payment method");
    	}
  
			if (bike != null) {
    		paymentScreenHandler.bikeToRent = bike;
    		paymentScreenHandler.dockToReturn = dock;
    		paymentScreenHandler.renderBikeRentInformation();
    		
    	}
  
			paymentScreenHandler.initialize();
  
			return paymentScreenHandler;
    	
    }
    
    private void renderBikeRentInformation() {
    	
    }
    
    protected void initialize(){
    	super.initializeComponent();

    	bikeName.setText(this.bikeToRent.getName());
    	bikeType.setText(this.bikeToRent.getBikeType());
    	rentalTime.setText(Integer.toString(RentBikeController.getRentBikeServiceController(null).pauseBikeRental(bikeToRent)) + " mins");
    	rentalPrice.setText(Float.toString(RentBikeController.getRentBikeServiceController(null).getRentalFee(bikeToRent)) + " " + this.bikeToRent.getCurrency());
    }

    public void confirmPaymentMethod() throws EcoBikeException, SQLException, IOException, ParseException {
   		CreditCard card = new CreditCard(cardHolderName.getText(), cardNumber.getText(), securityCode.getText(), expirationDate.getText());
   		Invoice invoice = RentBikeController.getRentBikeServiceController(new InterbankBoundary("ACB")).returnBike(bikeToRent, dockToReturn, card);
   		
			if (invoice != null){
   			ToastContainer.success("Return Bike Successfully");    			
   			InvoiceScreenHandler invoiceScreen = InvoiceScreenHandler.getInvoiceScreenHandler(this.stage, this, invoice); 
   			invoiceScreen.show();
   		} else {	
   			ToastContainer.error("Cannot Perform Transaction!");
   		}
    }
}
