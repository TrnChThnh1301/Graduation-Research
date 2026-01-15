package views.screen.popup;

import java.io.IOException;
import constants.Assets;
import constants.Paths;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import layouts.BaseLayout;

public class ToastContainer extends BaseLayout {
  @FXML
  ImageView tickicon;

  @FXML
  Label message;

  public ToastContainer(Stage stage) throws IOException {
    super(stage, Paths.MODAL_TOAST);
  }

  private static ToastContainer popup(String message, String imagepath, Boolean undecorated) throws IOException {
	ToastContainer popup = new ToastContainer(new Stage());
    
	if (undecorated) {
      popup.stage.initStyle(StageStyle.UNDECORATED);
    }
	
    popup.message.setText(message);
    popup.setImage(imagepath);
    popup.initialize();
    
    return popup;
  }
  
  protected void initialize() {}

  public static void success(String message) throws IOException {
    popup(message, Assets.ICONS_PATH + "/" + "tickgreen.png", true)
    	.toast(true);
  }

  public static void error(String message) throws IOException {
    popup(message, Assets.ICONS_PATH + "/" + "tickerror.png", false)
    	.toast(false);
  }

  public static ToastContainer loading(String message) throws IOException {
    return popup(message, Assets.ICONS_PATH + "/" + "loading.gif", true);
  }

  public void setImage(String path) {
    super.setImage(tickicon, path);
  }

  public void toast(Boolean autoclose) {
    super.show();

    if (autoclose) {
      close(1);
    }
  }

  public void toast(double time) {
    super.show();
    close(time);
  }

  public void close(double time) {
    PauseTransition delay = new PauseTransition(Duration.seconds(time));
    
    delay.setOnFinished(event -> stage.close());
    delay.play();
  }
}
