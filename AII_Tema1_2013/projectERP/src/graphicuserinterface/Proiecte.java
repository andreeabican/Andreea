package graphicuserinterface;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import general.Constants;


import dataaccess.DataBaseConnection;
import entities.Entity;
import entities.ProiecteTable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Proiecte implements EventHandler, Initializable {
	private Stage								applicationStage;
	private Scene								applicationScene;
	private double								sceneWidth, sceneHeight;
	private String								tableName;
	private static int							idUserLogat;
	
	@FXML TableView<Entity>						tableContent;
	
	@FXML private TableColumn<Entity, String>	numeproiect;
	@FXML private TableColumn<Entity, String>	datastart;
	@FXML private TableColumn<Entity, String>	datafinal;
	@FXML private TableColumn<Entity, String>	versiuni;
	
	public Proiecte() {
		System.out.println("Constructor prim");
		// TODO Auto-generated constructor stub
	}
	
	public Proiecte(int idUserLogat) {
		System.out.println("Constructor doi");
		System.out.println(idUserLogat + "++++");
		this.idUserLogat = idUserLogat;
		System.out.println(this.idUserLogat + "------");
	}
	
	public void start() throws IOException {
		System.out.println("start");
		/*URL location = getClass().getResource("Proiecte.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(location);
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
		System.out.println("seteaza IduserlogatProiecte: " + idUserLogat);
		Parent root = (Parent) fxmlLoader.load(location.openStream());
		Proiecte controller = (Proiecte)(fxmlLoader.getController());
		controller.idUserLogat = idUserLogat;*/
		
		applicationStage = new Stage();
		applicationStage.setTitle(Constants.APPLICATION_NAME);
		applicationStage.getIcons().add(new Image(Constants.ICON_FILE_NAME));
		applicationScene = new Scene((Parent)FXMLLoader.load(getClass().getResource("Proiecte.fxml")));
//		applicationScene = new Scene(root);
		applicationScene.addEventHandler(EventType.ROOT, (EventHandler<? super Event>)this);
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		sceneWidth  = Constants.SCENE_WIDTH_SCALE*screenDimension.width;
		sceneHeight = Constants.SCENE_HEITH_SCALE*screenDimension.height;
		applicationStage.setScene(applicationScene);
		applicationStage.show(); 
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//init table columns
		System.out.println("============= init ===============");
		initCellValueFactory();
		String query = "select P.denumire, A.datastart, A.dataincheiere, " +
						"GROUP_CONCAT(V.denumire SEPARATOR ', ') as versiuni from " +
						"utilizatori U, proiecte P, versiuniproiecte V, asociereechipaangajat A, echipe E " +
						"where U.idutilizator = " + "\'" + idUserLogat + "\' and " +
						"A.idutilizator = \'" + idUserLogat + "\' and " +
						"A.idechipa = E.idechipa and " +
						"P.idproiect = E.idproiect and " +
						"V.idproiect = E.idproiect AND " +
						"(V.datastart <= A.dataincheiere AND A.datastart <= V.dataterminare)" +
						"group by P.idproiect";
		populateTableView(query, 4);
		System.out.println(idUserLogat + " LaLALAProiecte");
	}
	
	public void initCellValueFactory() {
		numeproiect.setCellValueFactory(new PropertyValueFactory<Entity, String>("numeproiect"));
		datastart.setCellValueFactory(new PropertyValueFactory<Entity, String>("datastart"));
		datafinal.setCellValueFactory(new PropertyValueFactory<Entity, String>("datafinal"));
		versiuni.setCellValueFactory(new PropertyValueFactory<Entity, String>("versiuni"));
	}
	
	public void populateTableView(String query, int numberOfColumns) {
        System.out.println("Intra aici:   ===========");
		try {
            ArrayList<ArrayList<Object>> values = DataBaseConnection.executeQuery(query, numberOfColumns);
            ObservableList<Entity> data = FXCollections.observableArrayList();
            for (ArrayList<Object> record:values) {
                //System.out.println("Record: " + record);
            	data.add(getCurrentEntity(record));
            }
           // System.out.println(tableContent.getColumns().get(1).getText());
            tableContent.setItems(data);
        } catch (Exception exception) {
            System.out.println ("exceptie: "+exception.getMessage());
            exception.printStackTrace();
        }
        
    }
	
	private Entity getCurrentEntity(ArrayList<Object> values) {
		return new ProiecteTable(values);
	}

	@Override
	public void handle(Event arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
