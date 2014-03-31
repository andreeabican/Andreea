package graphicuserinterface;

import dataaccess.DataBaseConnection;
import entities.Entity;
import entities.HRinfo;
import entities.HRtable;
import entities.UserDataTable;
import general.Constants;
import general.queryUtil;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import sun.font.TextLabel;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AdministratorGUI implements EventHandler, Initializable {
	private Stage				applicationStage;
	private Scene				applicationScene;
	private double				sceneWidth, sceneHeight;
	
	private static int			idUserLogat;
	private static String		tipUserLogat;
	private int					idUserSelectat;
	
	
	@FXML private TextField				cnp;
	@FXML private TextField				nume;
	@FXML private TextField				prenume;
	@FXML private TextField				adresa;
	@FXML private TextField				telefon;
	@FXML private TextField				email;
	@FXML private TextField				iban;
	@FXML private TextField				nrcontract;
	@FXML private TextField				dataangajarii;
	@FXML private TextField				tip;
	@FXML private TextField				zileconcediu;
	@FXML private TextField				orecontract;
	@FXML private TextField				salariunegociat;
	@FXML private TextField				username;
	@FXML private TextField				parola;
	
	@FXML private TableView<Entity> 			tableContent;
	
	@FXML private TableColumn<Entity, String>	idutilizator;
	@FXML private TableColumn<Entity, String>	cnputilizator;
	@FXML private TableColumn<Entity, String>	numeutilizator;
	@FXML private TableColumn<Entity, String>	prenumeutilizator;
	@FXML private TableColumn<Entity, String>	adresautilizator;
	@FXML private TableColumn<Entity, String>	telefonutilizator;
	@FXML private TableColumn<Entity, String>	emailutilizator;
	@FXML private TableColumn<Entity, String>	ibanutilizator;
	@FXML private TableColumn<Entity, String>	nrcontractutilizator;
	@FXML private TableColumn<Entity, String>	dataangajariiutilizator;
	@FXML private TableColumn<Entity, String>	tiputilizator;
	@FXML private TableColumn<Entity, String>	zileconcediuutilizator;
	@FXML private TableColumn<Entity, String>	orecontractutilizator;
	@FXML private TableColumn<Entity, String>	salariuutilizator;
	@FXML private TableColumn<Entity, String>	usernameutilizator;
	@FXML private TableColumn<Entity, String>	parolautilizator;
	
	@FXML private Label							eroareAdauga;
	
	
	public AdministratorGUI() {
		idUserSelectat = -1;
	}
	
	public AdministratorGUI(int idUserLogat) {
		System.out.println("intra aici");
		this.idUserLogat = idUserLogat;
		tipUserLogat = queryUtil.tipUtilizator(idUserLogat);
		System.out.println("Tip user logat: " +tipUserLogat);
	}
	
	public void start() throws IOException, SQLException {
		applicationStage = new Stage();
		applicationStage.setTitle(Constants.APPLICATION_NAME);
		applicationStage.getIcons().add(new Image(Constants.ICON_FILE_NAME));
		applicationScene = new Scene((Parent)FXMLLoader.load(getClass().getResource("Admin.fxml")));
		applicationScene.addEventHandler(EventType.ROOT, (EventHandler<? super Event>)this);
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		sceneWidth  = Constants.SCENE_WIDTH_SCALE*screenDimension.width;
		sceneHeight = Constants.SCENE_HEITH_SCALE*screenDimension.height;
		applicationStage.setScene(applicationScene);
		applicationStage.show(); 
	}
	
	@FXML
	public void adaugaMouseClick() throws SQLException {
		if (idUserSelectat == -1) {
			eroareAdauga.setText("Selectati un utilizator!");
		} else {
			eroareAdauga.setText("");
			if (!verificaTextCompletat()) {
				return;
			}
			String query = "INSERT into utilizatori VALUES(DEFAULT, " + 
							 "\'" + cnp.getText().toString() + "\', " +
							 "\'" + nume.getText().toString() + "\', " +
							 "\'" + prenume.getText().toString() + "\', " +
							 "\'" + adresa.getText().toString() + "\', " +
							 "\'" + telefon.getText().toString() + "\', " +
							 "\'" + email.getText().toString() + "\', " +
							 "\'" + iban.getText().toString() + "\', " +
							 "\'" + nrcontract.getText().toString() + "\', " +
							 "\'" + dataangajarii.getText().toString() + "\', " +
							 "\'" + tip.getText().toString() + "\', " +
							 "\'" + zileconcediu.getText().toString() + "\', " +
							 "\'" + orecontract.getText().toString() + "\', " +
							 "\'" + salariunegociat.getText().toString() + "\', " +
							 "\'" + username.getText().toString() + "\', " +
							 "\'" + parola.getText().toString() + "\')";
			DataBaseConnection.insertQuery(query);
			populeazaTabelaUtilizatori();
		}
	}
	
	@FXML
	public void modificaMouseClick() throws SQLException {
		if (idUserSelectat == -1) {
			eroareAdauga.setText("Selectati un utilizator!");
		} else {
			//eroareAdauga.setText("");
			String campuri = campuriUpdate();
			String query = "UPDATE utilizatori SET " + campuri + " WHERE idutilizator = " + idUserSelectat;
			DataBaseConnection.insertQuery(query);
			populeazaTabelaUtilizatori();
		}
	}
	
	@FXML
	public void stergeMouseClick() throws SQLException {
		String query = "DELETE FROM utilizatori WHERE idutilizator = " + idUserSelectat;
		DataBaseConnection.insertQuery(query);
		populeazaTabelaUtilizatori();
	}

	@FXML
	public void rowMouseClick() {
		Entity ent = ((Entity)tableContent.getSelectionModel().getSelectedItem());
		if (ent == null)
			return;
		
		ArrayList<String> values = ent.getValues();
		cnp.setText(values.get(1));             
		nume.setText(values.get(2));            
		prenume.setText(values.get(3));     
		adresa.setText(values.get(4));          
		telefon.setText(values.get(5));         
		email.setText(values.get(6));           
		iban.setText(values.get(7));            
		nrcontract.setText(values.get(8));      
		dataangajarii.setText(values.get(9));   
		tip.setText(values.get(10));             
		zileconcediu.setText(values.get(11));    
		orecontract.setText(values.get(12));     
		salariunegociat.setText(values.get(13)); 
		username.setText(values.get(14));        
		parola.setText(values.get(15));    
		idUserSelectat = Integer.parseInt(values.get(0));
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initCellValueFactory();
		populeazaTabelaUtilizatori();
	}
	
	public void populeazaTabelaUtilizatori() {
		String query = "SELECT * from utilizatori";
		if (tipUserLogat.equals("super-administrator"))
			populateTableView(query, 16);
		else {
			query += " WHERE tip != 'administrator' AND tip != 'super-administrator'"; 
			populateTableView(query, 16);
		}
	}
	
	public String campuriUpdate() {
		String campuri = "";
		if (!cnp.getText().isEmpty()) {
			campuri += "CNP = \'" + cnp.getText().toString() + "\', ";
		}
		if (!nume.getText().isEmpty()) {
			campuri += "nume = \'" + nume.getText().toString() + "\', ";
		}
		if (!prenume.getText().isEmpty()) {
			campuri += "prenume = \'" + prenume.getText().toString() + "\', ";
		}
		if (!adresa.getText().isEmpty()) {
			campuri += "adresa = \'" + adresa.getText().toString() + "\', ";
		}
		if (!telefon.getText().isEmpty()) {
			campuri += "telefon = \'" + telefon.getText().toString() + "\', ";
		}
		if (!email.getText().isEmpty()) {
			campuri += "email = \'" + email.getText().toString() + "\', ";
		}
		if (!iban.getText().isEmpty()) {
			campuri += "IBAN = \'" + iban.getText().toString() + "\', ";
		}
		if (!nrcontract.getText().isEmpty()) {
			campuri += "nrcontract = \'" + nrcontract.getText().toString() + "\', ";
		}
		if (!dataangajarii.getText().isEmpty()) {
			campuri += "dataangajarii = \'" + dataangajarii.getText().toString() + "\', ";
		}
		if (!tip.getText().isEmpty()) {
			campuri += "tip = \'" + tip.getText().toString() + "\', ";
		}
		if (!zileconcediu.getText().isEmpty()) {
			campuri += "zileconcediuramase = \'" + zileconcediu.getText().toString() + "\', ";
		}
		if (!orecontract.getText().isEmpty()) {
			campuri += "numarorecontract = \'" + orecontract.getText().toString() + "\', ";
		}
		if (!salariunegociat.getText().isEmpty()) {
			campuri += "salariunegociat = \'" + salariunegociat.getText().toString() + "\', ";
		}
		if (!username.getText().isEmpty()) {
			campuri += "numeutilizator = \'" + username.getText().toString() + "\', ";
		}
		if (!parola.getText().isEmpty()) {
			campuri += "parola = \'" + parola.getText().toString() + "\', ";
		}
		if (campuri != "") {
			campuri = campuri.substring(0, campuri.length() - 2);
			return campuri;
		}
		return campuri;
	}
	
	public boolean verificaTextCompletat() {
		if (cnp.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul cnp");
			return false;
		}
		if (nume.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul nume");
			return false;
		}
		if (prenume.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul prenume");
			return false;
		}
		if (adresa.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul adresa");
			return false;
		}
		if (telefon.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul telefon");
			return false;
		}
		if (email.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul email");
			return false;
		}
		if (iban.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul iban");
			return false;
		}
		if (nrcontract.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul nrcontract");
			return false;
		}
		if (dataangajarii.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul data angajarii");
			return false;
		}
		if (tip.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul tip angajat");
			return false;
		}
		if (zileconcediu.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul zile concediu odihna");
			return false;
		}
		if (orecontract.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul ore contract");
			return false;
		}
		if (salariunegociat.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul salariu negociat");
			return false;
		}
		if (username.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul nume utilizator");
			return false;
		}
		if (parola.getText().isEmpty()) {
			eroareAdauga.setText("Trebuie sa completati campul parola");
			return false;
		}       
		return true;
	}
	
	public void populateTableView(String query, int numberOfColumns) {
        try {
            ArrayList<ArrayList<Object>> values = DataBaseConnection.executeQuery(query, numberOfColumns);
            ObservableList<Entity> data = FXCollections.observableArrayList();
            for (ArrayList<Object> record:values) {
            	data.add(getCurrentEntity(record));
            }
            tableContent.setItems(data);
        } catch (Exception exception) {
            System.out.println ("exceptie: "+exception.getMessage());
            exception.printStackTrace();
        }
        
    }
	
	private Entity getCurrentEntity(ArrayList<Object> values) {
		return new UserDataTable(values);
    }

	
	public void initCellValueFactory() {
		idutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("idutilizator"));
		cnputilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("cnputilizator"));
		numeutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("numeutilizator"));
		prenumeutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("prenumeutilizator"));
		adresautilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("adresautilizator"));
		telefonutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("telefonutilizator"));
		emailutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("emailutilizator"));
		ibanutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("ibanutilizator"));
		nrcontractutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("nrcontractutilizator"));
		dataangajariiutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("dataangajariiutilizator"));
		tiputilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("tiputilizator"));
		zileconcediuutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("zileconcediuutilizator"));
		orecontractutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("orecontractutilizator"));
		idutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("idutilizator"));
		salariuutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("salariuutilizator"));
		usernameutilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("usernameutilizator"));
		parolautilizator.setCellValueFactory(new PropertyValueFactory<Entity, String>("parolautilizator"));
	}

	@Override
	public void handle(Event arg0) {
		// TODO Auto-generated method stub
		
	}
}
