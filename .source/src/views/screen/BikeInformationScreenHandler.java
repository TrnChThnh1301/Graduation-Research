package views.screen;

import entities.Bike;
import entities.strategies.DepositFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import constants.Assets;
import constants.BikeStatuses;
import constants.Paths;

import controllers.EcoBikeInformationController;
import views.screen.popup.ToastContainer;
import layouts.BaseLayout;
import exceptions.ecobike.EcoBikeException;
import interfaces.ElectricBikeProps;

public class BikeInformationScreenHandler extends BaseLayout implements PropertyChangeListener {
    private static BikeInformationScreenHandler bikeInformationScreenHandler = null;
    private Bike currentBike = null;

    @FXML
    private Label nameValue;
    @FXML
    private Label typeValue;
    @FXML
    private Label statusValue;
    @FXML
    private Label distanceCoveredValue;
    @FXML
    private Label depositValue;
    @FXML
    private Button rentButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button pauseButton;
    @FXML
    private ImageView bikeImage;
    @FXML
    private ImageView prevScreenIcon;
    @FXML
    private Label currentDockValue;
    @FXML
    private Label batteryLabel, batteryValue;

    private BikeInformationScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public static BikeInformationScreenHandler getBikeInformationScreenHandler(Stage stage, BaseLayout prevScreen, Bike bike) {
        if (bikeInformationScreenHandler == null) {
            try {
                bikeInformationScreenHandler = new BikeInformationScreenHandler(stage, Paths.VIEW_BIKE_SCREEN);
                bikeInformationScreenHandler.setScreenTitle("Detailed Bike Information");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (prevScreen != null) {
            bikeInformationScreenHandler.setPreviousScreen(prevScreen);
        }

        if (bike != null) {
        	bikeInformationScreenHandler.currentBike = bike;
        	bikeInformationScreenHandler.currentBike.addStatusObserver(bikeInformationScreenHandler);
        }

        bikeInformationScreenHandler.initialize();

        return bikeInformationScreenHandler;
    }

    public void propertyChange(PropertyChangeEvent e) {
    	Object val = e.getNewValue();

    	if (val instanceof BikeStatuses.Instances ) {
    		BikeStatuses.Instances newStatus = (BikeStatuses.Instances) val;
    		statusValue.setText(newStatus.toString());
    		
        	if (newStatus == BikeStatuses.Instances.FREE) {
        		rentButton.setDisable(false);
        		pauseButton.setDisable(true);
        		returnButton.setDisable(true);
        	} 
        	else if (newStatus == BikeStatuses.Instances.PAUSED) {
        		rentButton.setDisable(true);
        		pauseButton.setText("CONTINUE");
        		pauseButton.setDisable(false);
        		returnButton.setDisable(false);
        	} 
        	else if (newStatus == BikeStatuses.Instances.RENTED) {
        		rentButton.setDisable(true);
        		pauseButton.setText("PAUSE");
        		pauseButton.setDisable(false);
        		returnButton.setDisable(false);
        	}
        
            returnButton.setDisable((BikeStatuses.Instances) e.getNewValue() == BikeStatuses.Instances.FREE ? true : false);
    	}
    	
    }
    
    protected void initialize() {
        rentButton	.setOnMouseClicked(e -> rentBike());
        pauseButton	.setOnMouseClicked(e -> pauseBikeRental());
        returnButton.setOnMouseClicked(e -> returnBike());
        
        prevScreenIcon.setOnMouseClicked(e -> {
            if (this.getPreviousScreen() != null) {
            	this.getPreviousScreen().show();
            }
        });
        
        if (currentBike.getBikeImage() != null && currentBike.getBikeImage().length() != 0) {
    		bikeImage.setImage(new Image(new File(Assets.BIKES_PATH + "/" + currentBike.getBikeImage()).toURI().toString()));
    	}
        

        // Fetch thông tin của cái xe được truyền từ component cha
        nameValue.setText(currentBike.getName());
        typeValue.setText(currentBike.getBikeType());
        statusValue.setText(currentBike.getCurrentStatus().toString());
        
        if (currentBike instanceof ElectricBikeProps) {
        	batteryValue.setVisible(true);
        	batteryLabel.setVisible(true);
        	batteryLabel.setText(Float.toString(((ElectricBikeProps) currentBike).getBattery()));
        	
        	try {
        		batteryLabel.setText(Float.toString(EcoBikeInformationController.getEcoBikeInformationController().getBikeBattery(currentBike.getBikeBarCode())) + " %");
			} catch (SQLException | EcoBikeException e1) {
				e1.printStackTrace();
			}
        	
        	batteryLabel.setVisible(true);
        } else {
        	batteryValue.setVisible(false);
        	batteryLabel.setVisible(false);
        }
        
        distanceCoveredValue.setText("100 km");
        depositValue.setText(DepositFactory.getDepositStrategy().getDepositPrice((float) currentBike.getDeposit()) + " " + currentBike.getCurrency());
        
        if (currentBike.getCurrentDock() != null) {
        	currentDockValue.setText(currentBike.getCurrentDock().getName());        	
        } else {
        	currentDockValue.setText("<No Dock>");
        }

        rentButton.setDisable(currentBike.getCurrentStatus() == BikeStatuses.Instances.FREE ? false : true);
        returnButton.setDisable(currentBike.getCurrentStatus() == BikeStatuses.Instances.FREE ? true : false);
        pauseButton.setDisable(currentBike.getCurrentStatus() == BikeStatuses.Instances.FREE ? true : false);
    }

    public void rentBike() {
        try {
            EcoBikeInformationController.getRentBikeService().rentBike(this.currentBike);
        } catch (EcoBikeException e) {
            try {
            	ToastContainer.error(e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        } catch (Exception e1) {
        	e1.printStackTrace();
        }
    }

    public void returnBike() {
        try {
            SelectDockToReturnBikeScreenHandler selectDockHandler = new SelectDockToReturnBikeScreenHandler(new Stage(), this, EcoBikeInformationController.getEcoBikeInformationController().getAllDocks(), this.currentBike);
            selectDockHandler.show();
        } catch (Exception e) {
            e.printStackTrace();
            try {
            	ToastContainer.error("UNDEFINED ERROR OCCURED");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
    }

    public void pauseBikeRental() {
        try {
        	if (this.currentBike.getCurrentStatus() == BikeStatuses.Instances.RENTED) {
        		EcoBikeInformationController.getRentBikeService().pauseBikeRental(this.currentBike);
        		ToastContainer.success("BIKE RENTAL PAUSED!");
        	} 
        	else if (this.currentBike.getCurrentStatus() == BikeStatuses.Instances.PAUSED) {
        		EcoBikeInformationController.getRentBikeService().resumeBikeRental(this.currentBike);
        		ToastContainer.success("BIKE RENTAL RESUMED!");
        	}
        } catch (Exception e) {
        	try {
        		ToastContainer.error("UNDEFINED ERROR OCCURED!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            e.printStackTrace();
        }
    }

}
