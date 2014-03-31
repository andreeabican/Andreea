package graphicuserinterface;

import general.Constants;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.prism.paint.Color;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ProgrammerAndControllerGUI implements EventHandler, Initializable  {
	private Stage				applicationStage;
	private Scene				applicationScene;
	private double				sceneWidth, sceneHeight;
	
	private static int			idUserLogat;
	
	@FXML Circle				modificaBubble;
	@FXML Circle				proiecteBubble;
	
	public ProgrammerAndControllerGUI() throws SQLException {
		System.out.println("Se apeleaza singur");
		// TODO Auto-generated constructor stub
	}
	
	public ProgrammerAndControllerGUI(int userid) throws SQLException {
		System.out.println("asta");
		this.idUserLogat = userid;
	}
	
	public void start() throws IOException, SQLException {
		System.out.println("start");
		
		applicationStage = new Stage();
		applicationStage.setTitle(Constants.APPLICATION_NAME);
		applicationStage.getIcons().add(new Image(Constants.ICON_FILE_NAME));
		applicationScene = new Scene((Parent)FXMLLoader.load(getClass().getResource("ProgrammerAndControllerMainPage.fxml")));
		applicationScene.addEventHandler(EventType.ROOT, (EventHandler<? super Event>)this);
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		sceneWidth  = Constants.SCENE_WIDTH_SCALE*screenDimension.width;
		sceneHeight = Constants.SCENE_HEITH_SCALE*screenDimension.height;
		applicationStage.setScene(applicationScene);
		applicationStage.show(); 
	}
	
	public void setIdUserLogat(int idUserLogat) {
		this.idUserLogat = idUserLogat;
	}
	
	public void onPersonalBubbleAction() throws IOException {
		PersonalInfo personalinfo = new PersonalInfo(idUserLogat);
		personalinfo.start();
	}
	
	public void onProiecteBubbleAction() throws IOException {
		System.out.println("idUserLogat: " + idUserLogat);
		Proiecte proiecte = new Proiecte(idUserLogat);
		proiecte.start();
	}
	
	public void onDefecteBubbleAction() throws IOException, SQLException {
		Defecte defecte = new Defecte(idUserLogat);
		defecte.start();
	}
	
	public void onModificaBubbleAction() throws IOException, SQLException {
		AdaugaModificaProiecte adaugaModificaProiecte = new AdaugaModificaProiecte(idUserLogat);
		adaugaModificaProiecte.start();
	}
	
	/*
	 * verifica daca utilizatorul este sef de departament
	 * daca nu este responsabil de departament, dezactiveaza bubble-ul de modificare/adaugare proiecte
	 */
	public boolean verificaResponsabilDepartament() throws SQLException {
		String query = "SELECT * from asocieredepartamentresponsabil WHERE " +
						"idresponsabil = " + "\'" + idUserLogat + "\'" + " AND " +
						"(iddepartament = " + "\'" + Constants.DEPARTAMENT_PROGRAMARE + "\' OR " +
						"iddepartament = " + "\'" + Constants.DEPARTAMENT_ASIGURAREA_CALITATII + "\')";
		ArrayList<ArrayList<Object>> result = dataaccess.DataBaseConnection.executeQuery(query, 3);
		if (result != null && !result.isEmpty()) {
			System.out.println("Result: " + result.get(0));
			return true;
		}
		return false;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			if (!verificaResponsabilDepartament()) {
				//modificaBubble.setStroke(javafx.scene.paint.Color.GREY);
				modificaBubble.setDisable(true);
				modificaBubble.setFill(javafx.scene.paint.Color.GREY);
				modificaBubble.setEffect(new javafx.scene.effect.DropShadow());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void handle(Event arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
