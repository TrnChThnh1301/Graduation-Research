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

public class DockForReturnHandler extends BaseLayout {
    private Dock currentDock;

    private Bike currentBike;

    @FXML
    private ImageView dockImage;

    @FXML
    private Label dockName;

    @FXML
    private Label dockAddressTxt;

    @FXML
    private Button returnBtn;

    @FXML
    private Label availableSlotsTxt;

    public DockForReturnHandler(Stage stage, Dock dock, Bike bike, String screenPath, BaseLayout prevScreen) throws IOException {
        super(stage, screenPath);
        
        this.currentDock = dock;
        this.currentBike = bike;

        if (prevScreen != null) {
        	this.setPreviousScreen(prevScreen);
        }

        initialize();
    }

    protected void initialize() {
    	if (currentDock.getDockImage() != null && currentDock.getDockImage().length() != 0) {
    		dockImage.setImage(new Image((new File(Assets.DOCKS_PATH + "/" +currentDock.getDockImage())).toURI().toString()));    		
    	}
        dockName.setText(currentDock.getName());
        dockAddressTxt.setText(currentDock.getDockAddress() + "");
        availableSlotsTxt.setText(Integer.toString(currentDock.getNumDockSpaceFree()));

        if (currentDock.getNumDockSpaceFree() > 0) {
        	returnBtn.setDisable(false);
        } else {
        	returnBtn.setDisable(true);
        }
    
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
