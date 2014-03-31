package graphicuserinterface;

import general.Constants;
import general.queryUtil;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.text.TabableView;
import javax.xml.crypto.Data;

import com.sun.org.apache.xerces.internal.impl.xs.identity.ValueStore;

import sun.util.calendar.Era;

import dataaccess.DataBaseConnection;
import entities.Entity;
import entities.MembriEchipaTable;
import entities.ProiecteBasicInfo;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AdaugaModificaProiecte implements EventHandler, Initializable {

	private static int					idUserLogat;
	private int							idProiectSelectat;
	private String 						currentTable;	
	
	private Stage						applicationStage;
	private Scene						applicationScene;
	private double						sceneWidth, sceneHeight;
	
	@FXML TableView<Entity> 			tableContentAngajati;
	@FXML TableColumn<Entity, String> 	numeangajat;
	@FXML TableColumn<Entity, String> 	prenumeangajat;
	@FXML TableColumn<Entity, String> 	datainceput;
	@FXML TableColumn<Entity, String> 	datasfarsit;
	
	@FXML TableView<Entity>				tableContent;
	@FXML TableColumn<Entity, String>	idproiect;
	@FXML TableColumn<Entity, String>	numeproiect;
	@FXML TableColumn<Entity, String>	descriereproiect;
	
	@FXML TextField						campNumeProiect;
	@FXML TextArea						campDescriereProiect;
	@FXML Button						adaugaButton;
	@FXML Label							eroareAdaugaProiect;
	
	@FXML ComboBox						comboEchipe;
	@FXML Button						okButton;
	@FXML ComboBox						comboResponsabili;
	@FXML Button						adaugaEchipaButton;
	@FXML Label							eroareAdaugaEchipa;
	
	@FXML TextField						datastart;
	@FXML TextField						datafinal;
	@FXML ComboBox						comboAngajati;
	@FXML Label							eroareAdaugaAngajat;
	
	@FXML TextField						dataStartVersiune;
	@FXML TextField						dataTerminareVersiune;
	@FXML TextField						denumireVersiune;
	@FXML Label							eroareAdaugaVersiune;
	
	public AdaugaModificaProiecte() {
		// TODO Auto-generated constructor stub
		idProiectSelectat = -1;
	}
	
	AdaugaModificaProiecte(int idUserLogat) {
		this.idUserLogat = idUserLogat;
	}
	
	public void start() throws IOException, SQLException {
		
		applicationStage = new Stage();
		applicationStage.setTitle(Constants.APPLICATION_NAME);
		applicationStage.getIcons().add(new Image(Constants.ICON_FILE_NAME));
		applicationScene = new Scene((Parent)FXMLLoader.load(getClass().getResource("AdaugaModificaProiecte.fxml")));
		applicationScene.addEventHandler(EventType.ROOT, (EventHandler<? super Event>)this);
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		sceneWidth  = Constants.SCENE_WIDTH_SCALE*screenDimension.width;
		sceneHeight = Constants.SCENE_HEITH_SCALE*screenDimension.height;
		applicationStage.setScene(applicationScene);
		applicationStage.show(); 
	}
	
	public void initCellValueFactory() {
		idproiect.setCellValueFactory(new PropertyValueFactory<Entity, String>("idproiect"));
		numeproiect.setCellValueFactory(new PropertyValueFactory<Entity, String>("numeproiect"));
		descriereproiect.setCellValueFactory(new PropertyValueFactory<Entity, String>("descriereproiect"));
	}
	
	public void initCellValueFactoryMembriEchipa() {
		numeangajat.setCellValueFactory(new PropertyValueFactory<Entity, String>("numeangajat"));
		prenumeangajat.setCellValueFactory(new PropertyValueFactory<Entity, String>("prenumeangajat"));
		datainceput.setCellValueFactory(new PropertyValueFactory<Entity, String>("datainceput"));
		datasfarsit.setCellValueFactory(new PropertyValueFactory<Entity, String>("datasfarsit"));
	}
	
	//populeaza tabelul cu proiecte
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
	
	//populeaza tabelul cu angajati
	public void populateTableViewAngajati(String query, int numberOfColumns) {
		try {
            ArrayList<ArrayList<Object>> values = DataBaseConnection.executeQuery(query, numberOfColumns);
            ObservableList<Entity> data = FXCollections.observableArrayList();
            for (ArrayList<Object> record:values) {
            	System.out.println("Record: " + record);
            	data.add(getCurrentEntity(record));
            }
            tableContentAngajati.setItems(data);
        } catch (Exception exception) {
            System.out.println ("exceptie: "+exception.getMessage());
            exception.printStackTrace();
        }
    }
	
	@FXML
	public void onAdaugaProiectButtonAction() {
		if (campNumeProiect.getText().isEmpty() || campDescriereProiect.getText().isEmpty()) {
			eroareAdaugaProiect.setText("!!! Trebuie sa completati ambele campuri !!!");
			return;
		} else {
			//update in baza de date
			eroareAdaugaProiect.setText("");
			//adauga proiect
			String expression = "INSERT INTO " + Constants.TABELA_PROIECTE + " VALUES (DEFAULT, \'" + 
							campNumeProiect.getText() +
							"\', \'" + campDescriereProiect.getText() + "\')";
			try {
				DataBaseConnection.insertQuery(expression);
				String query1 = "SELECT idproiect, denumire, descriere from proiecte";
				populateTableView(query1, 3);
				campNumeProiect.setText("");
				campDescriereProiect.setText("");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	@FXML
	public void adaugaMembruEchipaButton() throws SQLException {
		//gaseste echipa in care trebuie sa fie adaugam angajatul
		if (idProiectSelectat == -1) {
			eroareAdaugaAngajat.setText("Trebuie sa selectezi un proiect");
			return;
		}
		if (datastart.getText().isEmpty() || datafinal.getText().isEmpty()) {
			eroareAdaugaAngajat.setText("Trebuie sa completezi ambele campuri pentru data");
			return;
		}
		
		//gaseste toti angajatii care nu fac parte din nicio echipa sau care
		//fac parte dintr-o echipa dar sunt liberi in perioada datastart-datafinal
		String query = "SELECT U.nume, U.prenume FROM " +
				"utilizatori U, asociereutilizatorfunctie ASUF, functii F, asociereechipaangajat A WHERE" +
				" U.idutilizator = A.idutilizator AND " +
				"ASUF.idutilizator = U.idutilizator AND " +
				"ASUF.idfunctie = F.idfunctie AND " +
				"NOT (A.datastart < " + "\'" + datafinal.getText() + "\' AND \'" +  datafinal.getText() + "\' < A.dataincheiere)" +
				"UNION " +
				"SELECT U.nume, U.prenume FROM " +
				"utilizatori U, asociereutilizatorfunctie ASUF, functii F  WHERE " +
				"NOT EXISTS " +
				"(SELECT * FROM " +
				"asociereechipaangajat A WHERE " +
				"U.idutilizator = A.idutilizator) AND " +
				"ASUF.idutilizator = U.idutilizator AND " +
				"ASUF.idfunctie = F.idfunctie AND " +
				"(F.iddepartament = 3 OR F.iddepartament = 4)";
		ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 2);
	}
	
	@FXML
	public void adaugaResponsabilMouseClick() throws SQLException {
		if (idProiectSelectat == -1) {
			eroareAdaugaEchipa.setText("Trebuie sa selectezi un proiect din tabel");
			return;
		}
		eroareAdaugaEchipa.setText("");
		String responsabil = comboResponsabili.getValue().toString();
		String[] numeprenume = responsabil.split(" ");
		String nume = numeprenume[0];
		String prenume = "";
		for (int i = 1; i < numeprenume.length; i++)
			prenume += numeprenume[i] + " ";
		String query = "SELECT idutilizator FROM utilizatori WHERE " +
				"nume = " + "\'" + nume + "\' AND " +
				"prenume = \'" + prenume.substring(0, prenume.length() - 1) + "\'";
		ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 1);
		int id = Integer.parseInt(result.get(0).get(0).toString());
		
		/*
		 * adauga in echipe o noua linie
		 * daca deja exista o echipa asignata proiectului, atunci aceasta se va sterge
		 * si se va asigna noua echipa. Altfel, se va adauga o noua linie cu proiectul selectat
		 * si o noua echipa
		 */
		System.out.println("Proiect selectat: " + idProiectSelectat);
		int idechipa = queryUtil.idEchipa(idProiectSelectat);
		System.out.println(idechipa + " Id echipa");
		//daca nu exista inca o echipa asignata acestui proiect
		if (idechipa == -1) {
			eroareAdaugaEchipa.setText("");
			query = "INSERT INTO echipe VALUES (DEFAULT, " + idProiectSelectat + ", (SELECT idutilizator from utilizatori" +
					" WHERE nume = " + "\'" + nume + "\' AND " +
					"prenume = \'" + prenume.substring(0, prenume.length() - 1) + "\'))"; 
			DataBaseConnection.insertQuery(query);
		} else {
			eroareAdaugaEchipa.setText("Un responsabil deja a fost ales pentru acest proiect");
		}
	}
	
	@FXML
	public void adaugaVersiuneMouseClick() throws SQLException {
		if (idProiectSelectat == -1) {
			eroareAdaugaVersiune.setText("Trebuie sa selectezi un proiect");
			return;
		}
		if (dataStartVersiune.getText().isEmpty() || dataTerminareVersiune.getText().isEmpty() || denumireVersiune.getText().isEmpty()) {
			eroareAdaugaVersiune.setText("Trebuie completate toate campurile");
			return;
		}
		eroareAdaugaVersiune.setText("");
		String expression = "INSERT INTO versiuniproiecte VALUES(DEFAULT, " + idProiectSelectat + ", \'"+
							dataStartVersiune.getText() + "\',  \'" + dataTerminareVersiune.getText() + 
							"\', \'" + denumireVersiune.getText() + "\')"; 
		DataBaseConnection.insertQuery(expression);
		dataStartVersiune.setText("");
		dataTerminareVersiune.setText("");
		denumireVersiune.setText("");
	}
	
	@FXML
	public void rowMouseClick() throws SQLException {
		Entity ent = ((Entity)tableContent.getSelectionModel().getSelectedItem());
		if (ent == null)
			return;
		ArrayList<String> values = ent.getValues();
		//ArrayList<String> values = ((Entity)tableContent.getSelectionModel().getSelectedItem()).getValues();
		System.out.println("Values.size " + values.size());
			
		//retine id-ul proiectului selectat pentru a putea face modificari pentru acest proiect
		idProiectSelectat = Integer.parseInt(values.get(0).toString());
		
		// gaseste id-ul echipei care se ocupa de acest proiect
		int idechipa = queryUtil.idEchipa(idProiectSelectat);
		System.out.println("Id echipa: " + idechipa);
		
		//gaseste toti angajatii care fac parte din aceasta echipa
		String query = "select U.nume as numeangajat, U.prenume as prenumeangajat, A.datastart as datainceput, A.dataincheiere as datasfarsit from " +
				"utilizatori U, asociereechipaangajat A WHERE " +
				"idechipa = \'" + idechipa + "\' AND U.idutilizator = A.idutilizator";
		currentTable = "echipaTable";
		populateTableViewAngajati(query, 4);
	}
	
	@FXML
	public void angajatiComboClick() throws SQLException {
		System.out.println("Verifica combo");
		if (idProiectSelectat == -1) {
			eroareAdaugaAngajat.setText("Trebuie sa selectezi un proiect");
			return;
		}
		if (datastart.getText().isEmpty() || datafinal.getText().isEmpty()) {
			eroareAdaugaAngajat.setText(Constants.EROARE_ADAUGA_ANGAJAT_LA_ECHIPA);
			comboAngajati.getItems().clear();
			return;
		} 
			
		eroareAdaugaAngajat.setText("");
		//gaseste toti angajatii care nu fac parte din nicio echipa sau care
		//fac parte dintr-o echipa dar sunt liberi in perioada datastart-datafinal
		String query = "SELECT U.nume, U.prenume FROM " +
				"utilizatori U, asociereutilizatorfunctie ASUF, functii F, asociereechipaangajat A WHERE" +
				" U.idutilizator = A.idutilizator AND " +
				"ASUF.idutilizator = U.idutilizator AND " +
				"ASUF.idfunctie = F.idfunctie AND " +
				"NOT (A.datastart <= " + "\'" + datafinal.getText() + "\' AND \'" +  datastart.getText() + "\' <= A.dataincheiere)" +
				" UNION " +
				"SELECT U.nume, U.prenume FROM " +
				"utilizatori U, asociereutilizatorfunctie ASUF, functii F  WHERE " +
				"NOT EXISTS " +
				"(SELECT * FROM " +
				"asociereechipaangajat A WHERE " +
				"U.idutilizator = A.idutilizator) AND " +
				"ASUF.idutilizator = U.idutilizator AND " +
				"ASUF.idfunctie = F.idfunctie AND " +
				"(F.iddepartament = 3 OR F.iddepartament = 4)";
		ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 2);
		
		comboAngajati.getItems().clear();
		for (ArrayList<Object> value : result) {
			String item = "";
			for (Object s : value) {
				item += s + " ";
			}
			comboAngajati.getItems().addAll(item);
		}
	}
	
	@FXML
	public void adaugaAngajatMouseClick() throws SQLException {
		System.out.println("Verifica aici");
		if (datainceput != null && datasfarsit != null && comboAngajati != null) {
			if (datastart.getText().isEmpty() || datafinal.getText().isEmpty()) {
				eroareAdaugaAngajat.setText(Constants.EROARE_ADAUGA_ANGAJAT_LA_ECHIPA);
				return;
			}
		}
		
		//determina echipa in care trebuie sa fie adaugat angajatul
		int idEchipa = queryUtil.idEchipa(idProiectSelectat);
		int idUser = queryUtil.idUtilizator(comboAngajati.getValue().toString());
		String expression = "INSERT INTO asociereechipaangajat VALUES (" +
							"DEFAULT, \'" + idEchipa + "\', \'" + idUser + "\', " + 
							"\'" + datastart.getText() + "\', \'" +
							datafinal.getText() + "\')";
		DataBaseConnection.insertQuery(expression);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//afiseaza toate proiectele existente
		currentTable = "proiecteTable";
		initCellValueFactory();
		initCellValueFactoryMembriEchipa();
		tableContent.setEditable(true);
		tableContentAngajati.setEditable(true);
		String query = "SELECT idproiect, denumire, descriere from proiecte";
		populateTableView(query, 3);
		
		//initializeaza comboBoxul pentru echipa si responsabil echipa
		query = "select nume, prenume from utilizatori U, functii F, asociereutilizatorfunctie A WHERE" +
				" U.idutilizator = A.idutilizator AND" +
				" F.idfunctie = A.idfunctie AND" +
				" (F.iddepartament = 3 OR F.iddepartament = 4)";
		ArrayList<ArrayList<Object>> result;
		try {
			result = DataBaseConnection.executeQuery(query, 2);
			if (result != null) {
				comboResponsabili.getItems().clear();
				for (ArrayList<Object> value : result) {
					String item = "";
					for (Object s : value) {
						item += s + " ";
					}
					comboResponsabili.getItems().addAll(item);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private Entity getCurrentEntity(ArrayList<Object> values) {
		if (currentTable.equals("proiecteTable")) {
			return new ProiecteBasicInfo(values);
		}
		if (currentTable.equals("echipaTable")) {
			return new MembriEchipaTable(values);
		}
		return null;
	}

	@Override
	public void handle(Event arg0) {
		// TODO Auto-generated method stub
		
	}

}
