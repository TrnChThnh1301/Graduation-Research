package views.screen;

import entities.Bike;
import entities.Dock;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import constants.Paths;
import views.screen.popup.DockForReturnHandler;
import layouts.BaseLayout;

public class SelectDockToReturnBikeScreenHandler extends BaseLayout {
	@FXML
	private VBox dockVBox;
	
	private Bike bikeToReturn;

  public SelectDockToReturnBikeScreenHandler(Stage stage, BaseLayout prevScreen, ArrayList<Dock> listDock, Bike bike) throws IOException {
    super(stage, Paths.MODAL_SELECT_DOCK_TO_RETURN);
    this.setScreenTitle("Select Dock To Return");
    this.bikeToReturn = bike;
    addDock(listDock);
    initialize();
  }
    
  protected void initialize() {}

  private void addDock(ArrayList<Dock> listDock) {
  	dockVBox.getChildren().clear();

   	for (Dock dock: listDock) {
   		DockForReturnHandler dockHandler;

			try {
				dockHandler = new DockForReturnHandler(this.stage, dock, this.bikeToReturn, Paths.COMPONENT_DOCK_FOR_RETURN, this);
				dockVBox.getChildren().add(dockHandler.getContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
   	}
   }  
}
