import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import constants.Paths;
import views.screen.MainScreenHandler;

public class App extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		try {
			Pane root = (Pane) FXMLLoader.load(getClass().getResource(Paths.WAITING_SCREEN));
			
			Scene scene = new Scene(root);
			stage.setScene(scene);
			
			FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
			fadeIn.setFromValue(0);
			fadeIn.setToValue(1);
			fadeIn.setCycleCount(1);
			
			FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), root);
			fadeOut.setFromValue(1);
			fadeOut.setToValue(0);
			fadeOut.setCycleCount(1);
			
			stage.show();
			
			fadeIn.play();
			fadeIn.setOnFinished((event) -> {
				fadeOut.play();
			});	
			
			fadeOut.setOnFinished((event) -> {
				MainScreenHandler mainScreenHandler = MainScreenHandler.getEcoBikeMainScreenHandler(stage, null);
				mainScreenHandler.show();
			});			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
