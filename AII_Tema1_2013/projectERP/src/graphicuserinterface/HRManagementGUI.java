package graphicuserinterface;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.javafx.geom.ConcentricShapePair;
import com.sun.org.apache.xalan.internal.xsltc.dom.CurrentNodeListFilter;
import com.sun.xml.internal.ws.api.server.Container;

import dataaccess.DataBaseConnection;

import entities.Author;
import entities.Bill;
import entities.BillDetail;
import entities.Book;
import entities.Category;
import entities.Collection;
import entities.Entity;
import entities.HRinfo;
import entities.HRtable;
import entities.PublishingHouse;
import entities.SupplyOrder;
import entities.SupplyOrderDetail;
import entities.User;
import entities.Writer;
import general.Constants;
import general.queryUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HRManagementGUI implements EventHandler, Initializable {
	private Stage                   			applicationStage;
	private Scene                  	 			applicationScene;
	private double                  			sceneWidth, sceneHeight;
	private String								tableName;
	private int									userid;
	private static int							state = 0;
	private static int 							idUserLogat;
	
	private static String						currentQuery;
	private static int							queryNumberOfColumns;
	
	@FXML private AnchorPane					pane;
	
	@FXML private TextField						campNume;
	@FXML private TextField						campPrenume;
	@FXML private TextField						campDepartament;
	@FXML private TextField						campDataStart;
	@FXML private TextField						campDataSfarsit;
	
	@FXML private TableView<Entity>				tableContent1;
	@FXML private TableView<Entity>				tableActivity;
	@FXML private MenuBar						applicationMenu;  
	@FXML private Label							concediuOdihna;
	@FXML private Label							concediuMedical;
	@FXML private Label							concediuFaraPlata;
	@FXML private Label							totalOre;
	@FXML private Label							totalOreSuplimentare;
	@FXML private Label							activityError;
	
	@FXML private TableView<Entity>				tableContent;
	@FXML private ArrayList<Label>				attributeLabels;
	@FXML private ArrayList<Control>			attributeControls;
	
	@FXML private TableColumn<Entity, String>	idutilizator;
	@FXML private TableColumn<Entity, String>	nume;
	@FXML private TableColumn<Entity, String>	prenume;
	@FXML private TableColumn<Entity, String>	dataangajarii;
	@FXML private TableColumn<Entity, String>	iddepartament;
	@FXML private TableColumn<Entity, String>	departament;
	
	@FXML private TableColumn<Entity, String>	orasosire;
	@FXML private TableColumn<Entity, String>	oraplecare;
	@FXML private TableColumn<Entity, String>	intarziere;
	@FXML private TableColumn<Entity, String>	dupaprogram;
	@FXML private TableColumn<Entity, String>	oresuplimentare;
	
	public HRManagementGUI(int idUserLogat) {
		this.idUserLogat = idUserLogat;
	}
	
	public HRManagementGUI() {
		if (state == 0) {
			currentQuery = Constants.HR_MANAGEMENT_QUERY;
			queryNumberOfColumns = 6;
		}
	}

	public void start() throws IOException {
		applicationStage = new Stage();
		applicationStage.setTitle(Constants.APPLICATION_NAME);
		applicationStage.getIcons().add(new Image(Constants.ICON_FILE_NAME));
		applicationScene = new Scene((Parent)FXMLLoader.load(getClass().getResource("HRinit.fxml")));
		applicationScene.addEventHandler(EventType.ROOT, (EventHandler<? super Event>)this);
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		sceneWidth  = Constants.SCENE_WIDTH_SCALE*screenDimension.width;
		sceneHeight = Constants.SCENE_HEITH_SCALE*screenDimension.height;
		applicationStage.setScene(applicationScene);
		applicationStage.show(); 
	}


	@FXML
	public void rowMouseClick() throws SQLException {
		Entity ent = ((Entity)tableContent.getSelectionModel().getSelectedItem());
		if (ent == null)
			return;
		ArrayList<String> values = ent.getValues();
		//ArrayList<String> values = ((Entity)tableContent.getSelectionModel().getSelectedItem()).getValues();
		System.out.println("intra aici");
		switch (state) {
		case 0:
			System.out.println("========== State este 0 ==========");
			campNume.setText(values.get(1));
			campPrenume.setText(values.get(2));
//			campDataStart.setText(values.get(3));
			campDepartament.setText(values.get(5));
			break;
		case 1:
			System.out.println("========== State este 1 ==========");
			campDataStart.setText(values.get(3));
			userid = Integer.parseInt(values.get(0));
			// scrie numarul de zile pentru fiecare tip de concediu
			queryUtil.computeZileConcediu(userid, concediuOdihna, concediuMedical, concediuFaraPlata);
			queryUtil.fillActivityLabels(userid, campDataStart, campDataSfarsit, totalOre, totalOreSuplimentare, activityError);
			break;
		default:
			break;
		}
	}
	
	@FXML
	/* search the person based on it's personal information */
	public void handleButtonCautareAction() {
		//change layout - load other fxml file
		String query = "select U.idutilizator," +
						" U.nume, U.prenume, U.dataangajarii," +
						" D.iddepartament, D.denumire from utilizatori U," +
						" departamente D, functii F, asociereutilizatorfunctie A ";
		
		String whereClause = "where ";
		if (campNume != null && !campNume.getText().isEmpty()) {
			whereClause += "U.nume = " +
							"\'" + campNume.getText() + "\'" +
							" AND ";
		}
		if (campPrenume != null && !campPrenume.getText().isEmpty()) {
			whereClause += "U.prenume = " +
							"\'" + campPrenume.getText() + "\'" +
							" AND ";
		}
		whereClause += "A.idutilizator = U.idutilizator AND " +
						"A.idfunctie = F.idfunctie AND " +
						"D.iddepartament = F.iddepartament";
		
		if (campDepartament != null && !campDepartament.getText().isEmpty()) {
			whereClause += " AND D.denumire = " +
							"\'" + campDepartament.getText() + "\'";
		}
		query += whereClause;
		currentQuery = query;
		queryNumberOfColumns = 6;
		state = 1;
		System.out.println("New query : " + currentQuery);
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HRinfo.fxml"));
			pane.getChildren().clear();
			pane.getChildren().add((Node)fxmlLoader.load());
//			populateTableViewInfo(currentQuery, queryNumberOfColumns);
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
	}
	
	@FXML
	public void handleButtonShowActivityAction() {
		String query = queryUtil.createActivityQuery(userid, campDataStart, campDataSfarsit);
		System.out.println(query);
		tableName = "HRinfo";
		populateTableViewInfo(query, 5);
		queryUtil.fillActivityLabels(userid, campDataStart, campDataSfarsit, totalOre, totalOreSuplimentare, activityError);
	}
	
	/* go back to the users table to choose another one */
	public void handleIntoarcereMenuAction() throws IOException {
		state = 0;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HRinit.fxml"));
		pane.getChildren().clear();
		pane.getChildren().add((Node)fxmlLoader.load());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//fill the table with information
		if (state == 0) {
			tableName = "HRtable";
			initCellValueFactoryHRInit();
			tableContent.setEditable(true);
			populateTableView(currentQuery, queryNumberOfColumns);
		} else {
			tableName = "HRtable";
			initCellValueFactoryHRsearch();
			initCellValueFactoryHRInit();
			tableContent.setEditable(true);
			populateTableView(currentQuery, queryNumberOfColumns);
		}

	}
	
	public void restartScene() throws IOException {
		//applicationScene = new Scene((Parent)FXMLLoader.load(getClass().getResource("HRinfo.fxml")));
		//applicationScene.addEventHandler(EventType.ROOT, (EventHandler<? super Event>)this);
		//Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		//sceneWidth  = Constants.SCENE_WIDTH_SCALE*screenDimension.width;
		//sceneHeight = Constants.SCENE_HEITH_SCALE*screenDimension.height;
		//applicationStage.setScene(applicationScene);
		//applicationStage.show();
	}
	
	public void initCellValueFactoryHRInit() {
		idutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("idutilizator"));
		nume.setCellValueFactory(new PropertyValueFactory<Entity, String>("nume"));
		prenume.setCellValueFactory(new PropertyValueFactory<Entity, String>("prenume"));
		dataangajarii.setCellValueFactory(new PropertyValueFactory<Entity, String>("dataangajarii"));
		iddepartament.setCellValueFactory(new PropertyValueFactory<Entity, String>("iddepartament"));
		departament.setCellValueFactory(new PropertyValueFactory<Entity, String>("departament"));
	}
	
	public void initCellValueFactoryHRsearch() {
		orasosire.setCellValueFactory(new PropertyValueFactory<Entity, String>("orasosire"));
		oraplecare.setCellValueFactory(new PropertyValueFactory<Entity, String>("oraplecare"));
		intarziere.setCellValueFactory(new PropertyValueFactory<Entity, String>("intarziere"));
		dupaprogram.setCellValueFactory(new PropertyValueFactory<Entity, String>("dupaprogram"));
		oresuplimentare.setCellValueFactory(new PropertyValueFactory<Entity, String>("oresuplimentare"));
	}
	
	public void populateTableView(String query, int numberOfColumns) {
        try {
            ArrayList<ArrayList<Object>> values = DataBaseConnection.executeQuery(query, numberOfColumns);
            ObservableList<Entity> data = FXCollections.observableArrayList();
            for (ArrayList<Object> record:values) {
                System.out.println("Record: " + record);
            	data.add(getCurrentEntity(record));
            }
            System.out.println(tableContent.getColumns().get(1).getText());
            tableContent.setItems(data);
        } catch (Exception exception) {
            System.out.println ("exceptie: "+exception.getMessage());
            exception.printStackTrace();
        }
        
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
		switch (tableName) {
		case "HRtable":
			return new HRtable(values);
		case "HRinfo":
			return new HRinfo(values);
		default:
			return null;
		}
    }

	@Override
	public void handle(Event arg0) {
		// TODO Auto-generated method stub
		
	}
		
}
