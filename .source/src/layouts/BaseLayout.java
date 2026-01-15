package layouts;

import java.io.File;
import java.io.IOException;

import java.util.Hashtable;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public abstract class BaseLayout {
	 	protected final Stage stage;
	    protected Hashtable<String, String> messages;
	    protected FXMLLoader loader;
	    protected AnchorPane content;
	    private Scene scene;
	    private BaseLayout prev;

	    public BaseLayout(Stage newStage, String path) throws IOException {
	        this.loader = new FXMLLoader(getClass().getResource(path));
	        this.loader.setController(this);
	        this.content = (AnchorPane) loader.load();
	        this.stage = newStage;
	    }

	    public void show() {
	        if (this.scene == null) {
	            this.scene = new Scene(this.content);
	        }
	        this.stage.setScene(this.scene);
	        this.stage.show();
	    }

	    public AnchorPane getContent() {
	        return this.content;
	    }

	    public FXMLLoader getLoader() {
	        return this.loader;
	    }

	    public void setImage(ImageView imv, String path) {
	        File file = new File(path);
	        Image img = new Image(file.toURI().toString());
	        imv.setImage(img);
	    }

	    public void setScreenTitle(String string) {
	        this.stage.setTitle(string);
	    }

	    public void setPreviousScreen(BaseLayout prev) {
	        this.prev = prev;
	    }

	    public BaseLayout getPreviousScreen() {
	        return this.prev;
	    }
	    
	    protected abstract void initialize();
}