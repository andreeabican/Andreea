package graphicuserinterface;

import general.Constants;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AboutGUI implements EventHandler, Initializable {

    private Stage                   applicationStage;
    private Scene                   applicationScene;    
    @FXML private Label             aboutLabel;
    @FXML private Button            closeButton;
    @FXML private ImageView			imageView;
    
    public AboutGUI() {       
    }
    
    public void start() throws IOException {
    	
    	// TO DO: Exercise 5
    	applicationStage = new Stage();
    	applicationScene = new Scene((Parent)FXMLLoader.load(getClass().getResource("about.fxml")));
    	applicationScene.addEventHandler(EventType.ROOT, (EventHandler<? super Event>)this);
    	applicationStage.setTitle("Despre");
    	applicationStage.getIcons().add(new Image(Constants.ICON_FILE_NAME));
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        applicationStage.setScene(applicationScene);
        applicationStage.show();
        
    }
    
    @Override
    public void handle(Event event) {           
   
    }   
    
    @FXML protected void handleCloseButtonAction(Event event) {
    	Stage stage = (Stage)closeButton.getScene().getWindow();
    	stage.close();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		aboutLabel.setText(Constants.ABOUT_TEXT);
		aboutLabel.setWrapText(true);
        aboutLabel.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 20; -fx-text-fill: darkred;");
	}
}
