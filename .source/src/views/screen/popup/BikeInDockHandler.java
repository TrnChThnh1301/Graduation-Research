package views.screen.popup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import entities.Bike;
import constants.Assets;
import layouts.BaseLayout;
import views.screen.BikeInformationScreenHandler;

public class BikeInDockHandler extends BaseLayout {
    private Bike currentBike;
    @FXML
    private ImageView bikeImage;
    @FXML
    private Label bikeName;
    @FXML
    private Label bikeType;
    @FXML
    private Label distanceEstimation;
    @FXML
    private Button viewBikeButton;

    public BikeInDockHandler(Stage stage, Bike bike, String screenPath, BaseLayout prevScreen) throws IOException {
        super(stage, screenPath);
        this.currentBike = bike;
        
        if (prevScreen != null) {
        	this.setPreviousScreen(prevScreen);
        }
        
        initialize();
    }
    
    protected void initialize() {
    	if (currentBike.getBikeImage() != null && currentBike.getBikeImage().length() != 0) {
    		bikeImage.setImage(new Image((new File(Assets.BIKES_PATH + "/" + currentBike.getBikeImage())).toURI().toString()));    		
    	}

    	bikeName.setText(currentBike.getName());
        bikeType.setText(currentBike.getBikeType().toString());
        viewBikeButton.setOnMouseClicked(e -> viewBikeInformation());
    }

    public void viewBikeInformation() {
    	BikeInformationScreenHandler bikeInfHandler = BikeInformationScreenHandler.getBikeInformationScreenHandler(this.stage, this.getPreviousScreen(), this.currentBike);
    	bikeInfHandler.show();
    }

}
