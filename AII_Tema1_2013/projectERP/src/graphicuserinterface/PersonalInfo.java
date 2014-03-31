package graphicuserinterface;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dataaccess.DataBaseConnection;

import entities.Entity;
import entities.HRinfo;
import entities.HRtable;
import general.Constants;
import general.queryUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PersonalInfo implements EventHandler, Initializable {

	private Stage                   			applicationStage;
	private Scene                  	 			applicationScene;
	private double                  			sceneWidth, sceneHeight;
	private static int 							idUserLogat;
	
	@FXML private TextField						campDataStart;
	@FXML private TextField						campDataSfarsit;

	@FXML private Label							concediuOdihna;
	@FXML private Label							concediuMedical;
	@FXML private Label							concediuFaraPlata;
	@FXML private Label							totalOre;
	@FXML private Label							totalOreSuplimentare;
	@FXML private Label							activityError;
	
	@FXML private TableView<Entity>				tableActivity;
	@FXML private TableColumn<Entity, String>	orasosire;
	@FXML private TableColumn<Entity, String>	oraplecare;
	@FXML private TableColumn<Entity, String>	intarziere;
	@FXML private TableColumn<Entity, String>	dupaprogram;
	@FXML private TableColumn<Entity, String>	oresuplimentare;
	
	public PersonalInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public PersonalInfo(int idUserLogat) {
		this.idUserLogat = idUserLogat;
	}
	
	public void start() throws IOException {
		applicationStage = new Stage();
		applicationStage.setTitle(Constants.APPLICATION_NAME);
		applicationStage.getIcons().add(new Image(Constants.ICON_FILE_NAME));
		applicationScene = new Scene((Parent)FXMLLoader.load(getClass().getResource("PersonalInfo.fxml")));
		applicationScene.addEventHandler(EventType.ROOT, (EventHandler<? super Event>)this);
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		sceneWidth  = Constants.SCENE_WIDTH_SCALE*screenDimension.width;
		sceneHeight = Constants.SCENE_HEITH_SCALE*screenDimension.height;
		applicationStage.setScene(applicationScene);
		applicationStage.show(); 
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initCellValueFactoryHRsearch();
		String query = queryUtil.createActivityQuery(idUserLogat, campDataStart, campDataSfarsit);
		System.out.println(query);
		populateTableViewInfo(query, 5);
		try {
			queryUtil.computeZileConcediu(idUserLogat, concediuOdihna, concediuMedical, concediuFaraPlata);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		queryUtil.fillActivityLabels(idUserLogat, campDataStart, campDataSfarsit, totalOre, totalOreSuplimentare, activityError);
	}
	
	@FXML
	public void handleButtonShowActivityAction() {
		String query = queryUtil.createActivityQuery(idUserLogat, campDataStart, campDataSfarsit);
		System.out.println(query);
		populateTableViewInfo(query, 5);
		queryUtil.fillActivityLabels(idUserLogat, campDataStart, campDataSfarsit, totalOre, totalOreSuplimentare, activityError);
	}
	
	public void populateTableViewInfo(String query, int numberOfColumns) {
        try {
            ArrayList<ArrayList<Object>> values = DataBaseConnection.executeQuery(query, numberOfColumns);
            ObservableList<Entity> data = FXCollections.observableArrayList();
            for (ArrayList<Object> record:values) {
                System.out.println("Record: " + record);
            	data.add(getCurrentEntity(record));
            }
            tableActivity.setItems(data);
        } catch (Exception exception) {
            System.out.println ("exceptie: "+exception.getMessage());
            exception.printStackTrace();
        }
        
    }
	
	private Entity getCurrentEntity(ArrayList<Object> values) {
			return new HRinfo(values);
    }
	
	public void initCellValueFactoryHRsearch() {
		orasosire.setCellValueFactory(new PropertyValueFactory<Entity, String>("orasosire"));
		oraplecare.setCellValueFactory(new PropertyValueFactory<Entity, String>("oraplecare"));
		intarziere.setCellValueFactory(new PropertyValueFactory<Entity, String>("intarziere"));
		dupaprogram.setCellValueFactory(new PropertyValueFactory<Entity, String>("dupaprogram"));
		oresuplimentare.setCellValueFactory(new PropertyValueFactory<Entity, String>("oresuplimentare"));
	}

	@Override
	public void handle(Event arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
