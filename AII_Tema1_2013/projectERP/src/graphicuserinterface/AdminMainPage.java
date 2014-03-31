package graphicuserinterface;

import general.Constants;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AdminMainPage implements EventHandler, Initializable {
	private Stage				applicationStage;
	private Scene				applicationScene;
	private double				sceneWidth, sceneHeight;
	
	private static int			idUserLogat;
	
	public AdminMainPage() {
		
	}
	
	public AdminMainPage(int idUserLogat) {
		this.idUserLogat = idUserLogat;
	}
	
	public void start() throws IOException, SQLException {
		applicationStage = new Stage();
		applicationStage.setTitle(Constants.APPLICATION_NAME);
		applicationStage.getIcons().add(new Image(Constants.ICON_FILE_NAME));
		applicationScene = new Scene((Parent)FXMLLoader.load(getClass().getResource("AdminMainPage.fxml")));
		applicationScene.addEventHandler(EventType.ROOT, (EventHandler<? super Event>)this);
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		sceneWidth  = Constants.SCENE_WIDTH_SCALE*screenDimension.width;
		sceneHeight = Constants.SCENE_HEITH_SCALE*screenDimension.height;
		applicationStage.setScene(applicationScene);
		applicationStage.show(); 
	}
	
	@FXML
	public void onPersonalBubbleAction() throws IOException {
		PersonalInfo personalInfo = new PersonalInfo(idUserLogat);
		personalInfo.start();
	}
	
	@FXML
	public void onOperatiiAdminBubbleAction() throws IOException, SQLException {
		AdministratorGUI admingui = new AdministratorGUI(idUserLogat);
		admingui.start();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handle(Event arg0) {
		// TODO Auto-generated method stub
		
	}

}
