package views.screen;

import entities.Bike;
import entities.Dock;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import exceptions.ecobike.EcoBikeException;
import controllers.EcoBikeInformationController;
import layouts.BaseLayout;
import constants.Paths;
import views.screen.popup.ToastContainer;

public class MainScreenHandler extends BaseLayout {
    private static MainScreenHandler ecoBikeMainScreenHandler = null;

    @FXML
    private ImageView dock1;

    @FXML
    private ImageView dock2;

    @FXML
    private ImageView dock3;

    @FXML
    private TextField searchBarField;

    @FXML	
    private Button searchBtn;
   
    private MainScreenHandler(Stage stage, String path) throws IOException {
        super(stage, path);
    }

    public static MainScreenHandler getEcoBikeMainScreenHandler(Stage stage, BaseLayout prevScreen) {
        if (ecoBikeMainScreenHandler == null) {
            try {
                ecoBikeMainScreenHandler = new MainScreenHandler(stage, Paths.MAIN_SCREEN);
                EcoBikeInformationController.getEcoBikeInformationController();
                ecoBikeMainScreenHandler.setScreenTitle("ECO BIKE RENTAL");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (prevScreen != null) {
            ecoBikeMainScreenHandler.setPreviousScreen(prevScreen);
        }

        ecoBikeMainScreenHandler.initialize();
        
        return ecoBikeMainScreenHandler;
    }
   
    protected void initialize() {
        handleDockClickOnMap(dock1, 1);
        handleDockClickOnMap(dock2, 2);
        handleDockClickOnMap(dock3, 3);
        handleClickSearchButton();
    }

    private void handleDockClickOnMap(ImageView dock, int dockID) {
        dock.setOnMouseClicked(e -> {
			try {
				showDock(dockID);
			} catch (NumberFormatException | SQLException | EcoBikeException e1) {
				e1.printStackTrace();
			}
		});
    } 

    private void handleClickSearchButton() {
        searchBtn.setOnMouseClicked(e->{
			try {
				search();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
    }

    private void search() throws IOException {
    	String searchString = searchBarField.getText();
        
    	try {
    		Bike bike = EcoBikeInformationController.getEcoBikeInformationController().getBikeInformationByName(searchString);
    		if (bike != null) {
    			BikeInformationScreenHandler.getBikeInformationScreenHandler(this.stage, this, bike).show();            	
    	    } else {
    	        ToastContainer.error("NO_BIKE_FOUND");
    	    }
    	} catch (Exception e) { 
    		ToastContainer.error(e.getMessage());
    	}

    }

    private void showDock(int dockID) throws NumberFormatException, SQLException, EcoBikeException {
        Dock dock = EcoBikeInformationController.getEcoBikeInformationController().getDockInformationByID(dockID);

        if (dock == null) {
        	try {
        		ToastContainer.error("NO_DOCK_INFORMATION");
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        DockInformationScreenHandler.getDockInformationScreenHandler(this.stage, this, dock).show();
    }
}
