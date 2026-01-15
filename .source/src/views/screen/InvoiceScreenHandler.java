package views.screen;

import entities.Invoice;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import layouts.BaseLayout;
import java.io.IOException;

import constants.Paths;

public class InvoiceScreenHandler extends BaseLayout {
    private static InvoiceScreenHandler invoiceScreenHandler = null;

    private Invoice currentInvoice = null;

    @FXML
    private Label invoiceIDTxt;

    @FXML
    private Label invoiceDateTxt;

    @FXML
    private Label bikeNameTxt;

    @FXML
    private Label bikeTypeTxt;

    @FXML
    private Label totalRentTimeTxt;

    @FXML
    private Label rentIDTxt;

    @FXML
    private Label depositIDTxt;

    @FXML
    private Label depositAmtTxt;

    @FXML
    private Label rentalPaymentIDTxt;

    @FXML
    private Label rentalPaymentAmtTxt;

    @FXML
    private Label totalTxt;

    @FXML
    private Button backToMainScreenButton;
    
    private InvoiceScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

	public static InvoiceScreenHandler getInvoiceScreenHandler(Stage stage, BaseLayout prevScreen, Invoice invoice) {
        if (invoiceScreenHandler == null) {
            try {
                invoiceScreenHandler = new InvoiceScreenHandler(stage, Paths.MODAL_INVOICE);
                invoiceScreenHandler.setScreenTitle("INVOICE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (invoice != null) {
            invoiceScreenHandler.currentInvoice = invoice;
        }

        if (prevScreen != null) {
            invoiceScreenHandler.setPreviousScreen(prevScreen);
        }

        invoiceScreenHandler.initialize();

        return invoiceScreenHandler;
    }

	protected void initialize() {
        handleBackHomeClick();
        invoiceIDTxt.setText(Integer.toString(currentInvoice.getInvoiceID()));
        invoiceDateTxt.setText(currentInvoice.getIssuedDate());
        rentIDTxt.setText(Integer.toString(currentInvoice.getRentID()));
        bikeNameTxt.setText(currentInvoice.getBike().getName());
        bikeTypeTxt.setText(currentInvoice.getBike().getBikeType());
        totalRentTimeTxt.setText(Integer.toString(currentInvoice.getTotalRentTime()));
        depositIDTxt.setText(currentInvoice.getTransactionList().get(0).getTransactionId());
        depositAmtTxt.setText(Double.toString(currentInvoice.getTransactionList().get(0).getAmount()) + " VND");
        rentalPaymentIDTxt.setText(currentInvoice.getTransactionList().get(1).getTransactionId());
        rentalPaymentAmtTxt.setText(Double.toString(currentInvoice.getTransactionList().get(1).getAmount()) + " VND");
        totalTxt.setText(Double.toString(currentInvoice.getTotal()) + " VND");
	}

    protected void handleBackHomeClick() {
        backToMainScreenButton.setOnMouseClicked(e -> {
        	MainScreenHandler.getEcoBikeMainScreenHandler(this.stage, null).show();
    	  this.stage.hide();
        });
    }
}
