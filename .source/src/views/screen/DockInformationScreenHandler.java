package views.screen;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import constants.Assets;
import constants.Paths;

import entities.Bike;
import entities.Dock;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import layouts.BaseLayout;
import exceptions.ecobike.EcoBikeException;
import views.screen.popup.BikeInDockHandler;
import controllers.EcoBikeInformationController;

public class DockInformationScreenHandler extends BaseLayout implements PropertyChangeListener {
    private static DockInformationScreenHandler dockInformationScreenHandler = null;
    private Dock currentDock = null;
    private ArrayList<Bike> bikesInDock = null;

    @FXML
    private ImageView dockImageView;
    @FXML
    private Label dockNameText;
    @FXML
    private Label dockAddressText;
    @FXML
    private Label dockCount;
    @FXML
    private Label availableBikeCount;
    @FXML
    private Label availableDocksCount;
    @FXML
    private Button returnBikeButton;
    @FXML
    private VBox bikeVBox;
    @FXML
    private ImageView previousScreenIcon;

    private DockInformationScreenHandler(Stage stage, String screenPath, BaseLayout prevScreen) throws IOException {
        super(stage, screenPath);
        this.setPreviousScreen(prevScreen);
    }

    public static DockInformationScreenHandler getDockInformationScreenHandler(Stage stage, BaseLayout prevScreen, Dock dock) {
    	if (dockInformationScreenHandler == null) {
            try {
                dockInformationScreenHandler = new DockInformationScreenHandler(stage, Paths.VIKE_DOCK_SCREEN, prevScreen);
                dockInformationScreenHandler.setScreenTitle("DETAILED DOCK INFORMATION");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (prevScreen != null) {
            dockInformationScreenHandler.setPreviousScreen(prevScreen);
        }

        if (dock != null) {
            dockInformationScreenHandler.currentDock = dock;
            dockInformationScreenHandler.currentDock.addObserver(dockInformationScreenHandler);
        }

        ArrayList<Bike> bikeList = dockInformationScreenHandler.currentDock.getAllBikesInDock();

        if (bikeList != null && bikeList.size() != 0) {
            dockInformationScreenHandler.bikesInDock = bikeList;
        }

        dockInformationScreenHandler.initialize();

        return dockInformationScreenHandler;
    }

    protected void initialize() {    	
        returnBikeButton.setOnMouseClicked(e -> {
			try {
				returnBike();
			} catch (IOException | SQLException | EcoBikeException e1) {
				e1.printStackTrace();
			}
		});

        previousScreenIcon.setOnMouseClicked(e -> {
            if (this.getPreviousScreen() != null)
                this.getPreviousScreen().show();
        });
        
        renderDockInformation();
    }

    private void renderDockInformation() {
    	if (currentDock.getDockImage() != null && currentDock.getDockImage().length() != 0) {
    		dockImageView.setImage(new Image(new File(Assets.DOCKS_PATH + "/" + currentDock.getDockImage()).toURI().toString()));
    	}
    	
        dockNameText.setText(currentDock.getName());
        dockAddressText.setText(currentDock.getDockAddress());
        dockCount.setText(currentDock.getTotalSpace() + " ");
        availableDocksCount.setText(currentDock.getNumDockSpaceFree() + " ");
        availableBikeCount.setText(currentDock.getNumAvailableBike() + " ");
        
        addBike(bikesInDock);
        
    	if (currentDock.getNumDockSpaceFree() > 0) {
    		returnBikeButton.setDisable(false);
    	} else {
    		returnBikeButton.setDisable(true);
    	}
    }

    private void returnBike() throws IOException, SQLException, EcoBikeException {
    	SelectBikeToReturnScreenHandler bikeToReturnHandler = new SelectBikeToReturnScreenHandler(new Stage(), this, EcoBikeInformationController.getEcoBikeInformationController().getAllRentedBikes(), this.currentDock);
    	bikeToReturnHandler.show();
    }

    private void addBike(ArrayList<Bike> bikeList) {
    	bikeVBox.getChildren().clear();

    	for (Bike bike: bikeList) {
    		BikeInDockHandler bikeHandler;

			try {
				bikeHandler = new BikeInDockHandler(this.stage, bike, Paths.COMPONENT_BIKE_IN_DOCK, this);
				bikeVBox.getChildren().add(bikeHandler.getContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		renderDockInformation();
	}


}
