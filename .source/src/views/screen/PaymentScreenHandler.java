package views.screen;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import exceptions.ecobike.EcoBikeException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import layouts.BaseLayout;

public abstract class PaymentScreenHandler extends BaseLayout {
	@FXML
    protected Label bikeName;

	@FXML
	protected Label bikeType;

	@FXML
	protected Label rentalTime;

	@FXML
	protected Label rentalPrice;	

    @FXML
    protected TextField cardHolderName;

    @FXML
    protected TextField cardNumber;

    @FXML
    protected TextField expirationDate;

    @FXML
    protected TextField securityCode;
    
    @FXML
    protected Button confirmPaymentButton;
	
	public PaymentScreenHandler(Stage stage, String screenPath) throws IOException {
		super(stage, screenPath);
	}

	protected void initializeComponent() {
    	confirmPaymentButton.setOnMouseClicked(e->{
			try {
				confirmPaymentMethod();
			} catch (EcoBikeException | SQLException | IOException | ParseException e1) {
				e1.printStackTrace();
			}
		});
	}

	public abstract void confirmPaymentMethod() throws EcoBikeException, SQLException, IOException, ParseException;
}
