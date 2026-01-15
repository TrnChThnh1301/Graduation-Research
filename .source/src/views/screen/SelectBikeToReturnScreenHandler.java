package views.screen;

import java.io.IOException;
import java.util.ArrayList;

import entities.Bike;
import entities.Dock;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import views.screen.popup.BikeToReturnHandler;
import constants.Paths;
import layouts.BaseLayout;

public class SelectBikeToReturnScreenHandler extends BaseLayout {
	@FXML
	private VBox bikeVBox;
	
	private Dock dockToReturn;
	
  public SelectBikeToReturnScreenHandler(Stage stage, BaseLayout prevScreen, ArrayList<Bike> listRentedBike, Dock dock) throws IOException {
   	super(stage, Paths.MODAL_SELECT_BIKE_TO_RETURN);
   	
   	this.dockToReturn = dock;
   	this.setScreenTitle("Select Bike To Return In " + dockToReturn.getName());
   	
   	addBike(listRentedBike);
   	initialize();
   }
    
   protected void initialize() {}

	 private void addBike(ArrayList<Bike> listBike) {
   	bikeVBox.getChildren().clear();
   	
   	for (Bike bike : listBike) {
   		BikeToReturnHandler bikeHandler;
   		
			try {
				bikeHandler = new BikeToReturnHandler(this.stage, dockToReturn, bike, Paths.COMPONENT_BIKE_TO_RETURN, this);
				bikeVBox.getChildren().add(bikeHandler.getContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
  	}
  }
    
}
