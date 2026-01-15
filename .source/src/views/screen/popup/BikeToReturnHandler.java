package views.screen.popup;

import entities.Bike;
import entities.Dock;

import exceptions.ecobike.EcoBikeUndefinedException;
import exceptions.ecobike.RentBikeException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import constants.Assets;
import controllers.EcoBikeInformationController;
import layouts.BaseLayout;

public class BikeToReturnHandler extends BaseLayout {
    private Dock currentDock;

    private Bike currentBike;

    @FXML
    private ImageView bikeImage;

    @FXML
    private Label bikeName;

    @FXML
    private Label bikeType;

    @FXML
    private Button returnBtn;

    public BikeToReturnHandler(Stage stage, Dock dock, Bike bike, String screenPath, BaseLayout prevScreen) throws IOException {
        super(stage, screenPath);
        
        this.currentDock = dock;
        this.currentBike = bike;

        if (prevScreen != null) {
        	this.setPreviousScreen(prevScreen);
        }

        initialize();
    }

    protected void initialize() {
    	if (currentBike.getBikeImage() != null && currentBike.getBikeImage().length() != 0) {
    		bikeImage.setImage(new Image((new File(Assets.BIKES_PATH+ "/" +currentBike.getBikeImage())).toURI().toString()));    		
    	}
        
        bikeName.setText(currentBike.getName());
        bikeType.setText(currentBike.getBikeType().toString());

        handleReturnButtonClick();
    }

    protected void handleReturnButtonClick() {
        returnBtn.setOnMouseClicked(e -> {
        	try {
				EcoBikeInformationController.getRentBikeService().returnBike(currentBike, currentDock);
				this.stage.hide();
			} catch (RentBikeException | EcoBikeUndefinedException | IOException e1) {
				e1.printStackTrace();
			}
        });
    }

}
