package graphicuserinterface;

import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.swing.text.TabExpander;

import dataaccess.DataBaseConnection;

import entities.DefecteTable;
import entities.Entity;
import entities.MembriEchipaTable;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Defecte implements Initializable {
	private Stage						applicationStage;
	private Scene						applicationScene;
	private double						sceneWidth, sceneHeight;
	private static int 					idUserLogat;
	private int							idDefectSelectat;
	private String						proiectSelectat;
	
	@FXML TableView<Entity> 			tableContent;
	@FXML TableColumn<Entity, String> 	iddefect;
	@FXML TableColumn<Entity, String> 	denumiredefect;
	@FXML TableColumn<Entity, String> 	severitatedefect;
	@FXML TableColumn<Entity, String> 	descrieredefect;
	@FXML TableColumn<Entity, String> 	proiectdefect;
	@FXML TableColumn<Entity, String> 	versiunedefect;
	@FXML TableColumn<Entity, String> 	reproductibilitatedefect;
	@FXML TableColumn<Entity, String> 	statutdefect;
	@FXML TableColumn<Entity, String> 	rezultatdefect;
	@FXML TableColumn<Entity, String> 	ultimamodificare;
	@FXML TableColumn<Entity, String>	utilizatorultimamodificare;
	@FXML Button						adaugaDefectButton;
	@FXML Button						modificaDefectButton;
	
	@FXML TextArea 						comentarii;
	@FXML TextArea 						comentariu;
	
	@FXML TextField						denumire;
	@FXML ComboBox						comboseveritate;
	@FXML TextField						descriere;
	@FXML ComboBox						comboproiect;
	@FXML ComboBox						comboversiune;
	@FXML TextField						reproductibilitate;
	@FXML ComboBox						combostatut;
	@FXML ComboBox						comborezultat;
	
	@FXML CheckBox						sortareSeveritate;
	@FXML CheckBox						sortareVersiune;
	@FXML CheckBox						sortareUltimaActualizare;
	
	@FXML ComboBox						filtruStatut;
	@FXML ComboBox						filtruRezultat;
	@FXML ComboBox						andOr;
	
	@FXML Label							eroareComentariu;
	@FXML Label							eroareAdaugaModifica;
	@FXML Label							eroareFiltrare;
		
	
	
	public Defecte() {
		idDefectSelectat = -1;
		proiectSelectat = null;
	}
	
	public Defecte(int idUserLogat) {
		this.idUserLogat = idUserLogat;
	}
	
	public void start() throws IOException, SQLException {
		System.out.println("start");
		applicationStage = new Stage();
		applicationStage.setTitle(Constants.APPLICATION_NAME);
		applicationStage.getIcons().add(new Image(Constants.ICON_FILE_NAME));
		applicationScene = new Scene((Parent)FXMLLoader.load(getClass().getResource("Defecte.fxml")));
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		sceneWidth  = Constants.SCENE_WIDTH_SCALE*screenDimension.width;
		sceneHeight = Constants.SCENE_HEITH_SCALE*screenDimension.height;
		applicationStage.setScene(applicationScene);
		applicationStage.show(); 
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//populeaza tabela cu defecte
		tableContent.setEditable(true);
		initCellValueFactory();
		populeazaTabelaDefecte();
		
		try {
			initComboValues();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//dezactiveaza toate campurile ce pot fi completate pentru modificarea
		//continutului tabelei pentru defecte
		denumire.setDisable(true);
		comboseveritate.setDisable(true);
		descriere.setDisable(true);
		comboproiect.setDisable(true);
		comboversiune.setDisable(true);
		reproductibilitate.setDisable(true);
		combostatut.setDisable(true);
		comborezultat.setDisable(true);	
		adaugaDefectButton.setDisable(true);
		modificaDefectButton.setDisable(true);
	}
	
	public boolean verificaResponsabilEchipa() throws SQLException {
		//TODO sterge eroare
		String query = "select E.idresponsabil from echipe E, proiecte P where" +
						" P.denumire = \'" + proiectSelectat + "\'" +
								" AND P.idproiect = E.idproiect AND " +
								"E.idresponsabil = " + idUserLogat;
		ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 1);
		return !result.isEmpty();
	}
	
	public boolean verificaAngajatQA() throws SQLException {
		String query = "select F.iddepartament from asociereutilizatorfunctie A, functii F WHERE " +
						"A.idutilizator = \'" + idUserLogat + "\' AND A.idfunctie = F.idfunctie";
		ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 1);
		if (result.isEmpty()) return false;
		int departamentangajat = Integer.parseInt(result.get(0).get(0).toString());
		System.out.println("Departament QA: " + departamentangajat);
		if (departamentangajat == 4)
			return true;
		return false;
	}
	
	public boolean verificaProgramator() throws SQLException {
		String query = "select F.iddepartament from asociereutilizatorfunctie A, functii F WHERE " +
				"A.idutilizator = \'" + idUserLogat + "\' AND A.idfunctie = F.idfunctie";
		ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 1);
		if (result.isEmpty()) return false;
		int departamentangajat = Integer.parseInt(result.get(0).get(0).toString());
		if (departamentangajat == 3)
			return true;
		return false;
	}
	
	@FXML
	public void rowMouseClick() throws SQLException {
        Entity ent = ((Entity)tableContent.getSelectionModel().getSelectedItem());
        ArrayList<String> values = null;
        if (ent != null) {
        	values = ent.getValues();
			//ArrayList<String> values = ((Entity)tableContent.getSelectionModel().getSelectedItem()).getValues();
			denumire.setText(values.get(1));
			comboseveritate.setValue(values.get(2));
			descriere.setText(values.get(3));
			comboproiect.setValue(values.get(4));
			comboversiune.setValue(values.get(5));
			reproductibilitate.setText(values.get(6));
			combostatut.setValue(values.get(7));
			comborezultat.setValue(values.get(8));
			idDefectSelectat = Integer.parseInt(values.get(0));
			proiectSelectat = values.get(4);
			
			//gaseste id-ul proiectului selectat
        }
		
		String coments = comentarii();
		comentarii.setText(coments);
		
		//verifica ce tip de utilizator este si in functie de rezultat
		//seteaza anumite campuri ca fiind editabile
		if (verificaResponsabilEchipa()) {
			System.out.println("este responsabil echipa");
			denumire.setDisable(false);
			comboseveritate.setDisable(false);
			descriere.setDisable(false);
			comboproiect.setDisable(false);
			comboversiune.setDisable(false);
			reproductibilitate.setDisable(false);
			combostatut.setDisable(false);
			comborezultat.setDisable(false);
			adaugaDefectButton.setDisable(false);
			modificaDefectButton.setDisable(false);
			return;
		}
		if (verificaAngajatQA()) {
			System.out.println("face parte din QA");
			denumire.setDisable(false);
			comboseveritate.setDisable(false);
			descriere.setDisable(false);
			comboproiect.setDisable(false);
			comboversiune.setDisable(false);
			reproductibilitate.setDisable(false);
			combostatut.setDisable(true);
			comborezultat.setDisable(false);
			adaugaDefectButton.setDisable(false);
			modificaDefectButton.setDisable(false);
			return;
		}
		if (verificaProgramator()) {
			denumire.setDisable(true);
			comboseveritate.setDisable(true);
			descriere.setDisable(true);
			comboproiect.setDisable(true);
			comboversiune.setDisable(true);
			reproductibilitate.setDisable(true);
			combostatut.setDisable(false);
			comborezultat.setDisable(true);	
			adaugaDefectButton.setDisable(true);
			modificaDefectButton.setDisable(false);
			return;
		}
	}
	
	public String comentarii() throws SQLException {
		//returneaza toate comenariile pentru defectul selectat
		String query = "select comentariu from comentariidefecte where iddefect = " + idDefectSelectat;
		ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 1);
		String coments = "Comentarii:\n\n";
		if (!result.isEmpty()) {
			for (ArrayList<Object> value : result) {
				if (value != null || !value.isEmpty())
					coments += value.get(0).toString() + "\n\n";
			}
		}
		return coments;
	}
	
	public void populeazaTabelaDefecte() {
		String query = "select D.iddefect, D.denumire, D.severitate, D.descriere, P.denumire, V.denumire," +
				" D.reproductibilitate, D.statut, D.rezultat, D.ultimamodificare," +
				" CONCAT(U.nume, \" \", U.prenume) as utilizatorultimamodificare from" +
				" defecte D, versiuniproiecte V, utilizatori U, proiecte P WHERE" +
				" V.idversiune = D.idversiune AND" +
				" U.idutilizator = D.idutilizatormodificare AND" +
				" D.idproiect IN " +
				"(select E.idproiect from " +
				"asociereechipaangajat A, echipe E where " +
				"A.idechipa = E.idechipa AND " +
				"A.idutilizator = \'" + idUserLogat +  "\') AND " +
				"D.idproiect = P.idproiect";
		
		String filtruString = "";
		//filtreaza dupa rezultat si severitate
		if (filtruRezultat.getValue() != null && filtruStatut.getValue() != null && andOr.getValue() != null) {
			String connector = andOr.getValue().toString();
			filtruString = " AND (D.statut = \'" + filtruStatut.getValue().toString() + "\' " + connector  +
							" D.rezultat = \'" + filtruRezultat.getValue().toString() + "\')";
		}
		query += filtruString;
		
		//sorteaza daca este nevoie
		String continueString = "";
		if (sortareSeveritate.isSelected()) {
			continueString += "(select severitateValue(D.severitate)) ASC"; 
		}
		
		if (sortareVersiune.isSelected()) {
			if (!continueString.isEmpty())
				continueString += ", V.denumire DESC";
			else
				continueString += "V.denumire DESC";
		}
		
		if (sortareUltimaActualizare.isSelected()) {
			if (!continueString.isEmpty())
				continueString += ", D.ultimamodificare";
			else
				continueString += "D.ultimamodificare";
		}
		
		if (!continueString.isEmpty()) {
			query += " ORDER BY " + continueString;
		}
		System.out.println("Continue string: " + continueString);
		populateTableView(query, 11);
	}
	
	public void initComboValues() throws SQLException {
		//populeaza severitate, versiune, statut si rezultat - combo chestii
		comborezultat.getItems().clear();
		comborezultat.getItems().addAll(Arrays.asList(Constants.REZULTAT));
		comboseveritate.getItems().clear();
		comboseveritate.getItems().addAll(Arrays.asList(Constants.SEVERITATE));
		combostatut.getItems().clear();
		combostatut.getItems().addAll(Arrays.asList(Constants.STATUT));
		comboproiect.getItems().clear();
		comboversiune.getItems().clear();
		//gaseste toate proiectele in care este implicat utilizatorul
		String query = "select P.denumire from echipe E, proiecte P where" +
						" E.idechipa in " +
						"(select idechipa from " +
						"asociereechipaangajat where" +
						" idutilizator = " + idUserLogat + ") AND " +
						"E.idproiect = P.idproiect";
		ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 1);
		for (ArrayList<Object> value : result) {
			comboproiect.getItems().addAll(value);
		}
		
		//populeaza si combourile care ajuta la filtrare
		filtruRezultat.getItems().clear();
		filtruStatut.getItems().clear();
		andOr.getItems().clear();
		filtruRezultat.getItems().addAll(Arrays.asList(Constants.REZULTAT));
		filtruStatut.getItems().addAll(Arrays.asList(Constants.STATUT));
		andOr.getItems().add("AND");
		andOr.getItems().add("OR");
	}
	
	@FXML
	public void adaugaComentariuMouseClick() throws SQLException {
		if (idDefectSelectat == -1) {
			return;
		}
		String expression = "INSERT INTO comentariidefecte VALUES(DEFAULT, " + idDefectSelectat + ", \'" + comentariu.getText() + "\', (SELECT CURDATE()))";
		comentariu.setText("");
		DataBaseConnection.insertQuery(expression);
		String coments = comentarii();
		comentarii.setText(coments);
	}
	
	@FXML
	public void aplicaFiltruMouseClick() {
		if (filtruRezultat.getValue() != null && filtruStatut.getValue() != null && andOr.getValue() != null) {
			eroareFiltrare.setText("");
		} else {
			eroareFiltrare.setText("Trebuie sa completezi toate campurile pentru filtrare");
		}
		populeazaTabelaDefecte();
	}
	
	public boolean verificaCompletareCampuri() {
		if (denumire.getText().isEmpty()) {
			eroareAdaugaModifica.setText("Nu ati completat campul denumire");
			return false;
		}
		if (comboseveritate.getValue() == null) {
			eroareAdaugaModifica.setText("Nu ati completat campul severitate");
			return false;
		}
		if (descriere.getText().isEmpty()) {
			eroareAdaugaModifica.setText("Nu ati completat campul descriere");
			return false;
		}
		if (comboproiect.getValue() == null) {
			eroareAdaugaModifica.setText("Nu ati completat campul proiect");
			return false;
		}
		if (comboversiune.getValue() == null) {
			eroareAdaugaModifica.setText("Nu ati completat campul versiune");
			return false;
		}
		if (reproductibilitate.getText().isEmpty()) {
			eroareAdaugaModifica.setText("Nu ati completat campul reproductibilitate");
			return false;
		}
		if (combostatut.getValue() == null) {
			eroareAdaugaModifica.setText("Nu ati completat campul statut");
			return false;
		}
		if (comborezultat.getValue() == null) {
			eroareAdaugaModifica.setText("Nu ati completat campul rezultat");
			return false;
		}
		return true;
	}
	
	@FXML
	public void selecteazaVersiuneMouseClick() throws SQLException {
		//populeaza comboul pentru versiune in functie de proiectul selectat
		if (comboproiect.getValue() == null) {
			comboversiune.getItems().clear();
		} else {
			String query = "select denumire from versiuniproiecte where " +
					"idproiect = (select idproiect from proiecte" +
					" where denumire = \'" + comboproiect.getValue().toString() + "\')";
			ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 1);
			comboversiune.getItems().clear();
			for (ArrayList<Object> value : result) {
				comboversiune.getItems().addAll(value);
			}
		}
	}
	
	@FXML
	public void adaugaDefectMouseClick() {
		if (verificaCompletareCampuri()) {
			int idproiect = queryUtil.idProiect(comboproiect.getValue().toString());
			int idversiune = queryUtil.idVersiune(comboversiune.getValue().toString(), idproiect);
			String query = "INSERT INTO defecte VALUES ( " +
					"DEFAULT, " +
					"\'" + denumire.getText() + "\', " +
					"\'" + comboseveritate.getValue() + "\', " +
					"\'" + descriere.getText() + "\', " + 
					idproiect + ", " +
					idversiune + ", " +
					"\'" + reproductibilitate.getText() + "\', " +
					"\'" + combostatut.getValue() + "\', " +
					"\'" + comborezultat.getValue() + "\', " +
					"(SELECT CONCAT((SELECT CURDATE()), \" \", (SELECT CURTIME()))), " +
					"\'" + idUserLogat + "\')";
			try {
				DataBaseConnection.insertQuery(query);
				populeazaTabelaDefecte();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String campuriDeModificat() {
		String campuri = "";
		int idproiect = -1;
		int idversiune = -1;
		if (!denumire.getText().isEmpty()) {
			System.out.println("camp denumire");
			campuri += "denumire = " + "\'" + denumire.getText().toString() + "\', ";
		}
		if (!(comboseveritate.getValue() == null)) {
			System.out.println("camp severitate");
			campuri += "severitate = " + "\'" + comboseveritate.getValue().toString() + "\', ";
		}
		if (!descriere.getText().isEmpty()) {
			System.out.println("camp descriere");
			campuri += "descriere = " + "\'" + descriere.getText().toString() + "\', ";
		}
		if (!(comboproiect.getValue() == null)) {
			System.out.println("camp proiect");
			idproiect = queryUtil.idProiect(comboproiect.getValue().toString());
			campuri += "idproiect = " + idproiect + ", ";
		}
		if (!(comboversiune.getValue() == null)) {
			System.out.println("camp versiune");
			idversiune = queryUtil.idVersiune(comboversiune.getValue().toString(), idproiect);
			campuri += "idversiune = " + idversiune + ", ";
		}
		if (!reproductibilitate.getText().isEmpty()) {
			System.out.println("camp repro");
			campuri += "reproductibilitate = " + "\'" + reproductibilitate.getText().toString() + "\', ";
		}
		if (!(combostatut.getValue() == null)) {
			System.out.println("camp statut");
			campuri += "statut = " + "\'" + combostatut.getValue().toString() + "\', ";
		}
		if (!(comborezultat.getValue() == null)) {
			System.out.println("camp rezultat");
			campuri += "rezultat = " + "\'" + comborezultat.getValue().toString() + "\', ";
		}
		if (campuri != "") {
			campuri = campuri.substring(0, campuri.length() - 2);
		}
		return campuri;
	}
	
	@FXML
	public void modificaDefectMouseClick() {
		String campuri = campuriDeModificat();
		String query = "UPDATE defecte SET " + campuri + 
						", ultimamodificare = (SELECT CONCAT((SELECT CURDATE()), \" \", (SELECT CURTIME()))), " +
						"idutilizatormodificare = " + idUserLogat + " WHERE iddefect = " + idDefectSelectat; 
		try {
			DataBaseConnection.insertQuery(query);
			populeazaTabelaDefecte();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
				
	}
	
	@FXML
	public void sortareUltimaActualizare() {
		System.out.println("Sortare");
		populeazaTabelaDefecte();
	}
	
	@FXML 
	public void sortareVersiune() {
		System.out.println("Sortare");
		populeazaTabelaDefecte();
	}
	
	@FXML
	public void sortareSeveritate() {
		System.out.println("Sortare");
		populeazaTabelaDefecte();
	}
	
	public void initCellValueFactory() {
		iddefect.setCellValueFactory(new PropertyValueFactory<Entity, String>("iddefect"));
		denumiredefect.setCellValueFactory(new PropertyValueFactory<Entity, String>("denumiredefect"));
		severitatedefect.setCellValueFactory(new PropertyValueFactory<Entity, String>("severitatedefect"));
		descrieredefect.setCellValueFactory(new PropertyValueFactory<Entity, String>("descrieredefect"));
		proiectdefect.setCellValueFactory(new PropertyValueFactory<Entity, String>("proiectdefect"));
		versiunedefect.setCellValueFactory(new PropertyValueFactory<Entity, String>("versiunedefect"));
		reproductibilitatedefect.setCellValueFactory(new PropertyValueFactory<Entity, String>("reproductibilitatedefect"));
		statutdefect.setCellValueFactory(new PropertyValueFactory<Entity, String>("statutdefect"));
		rezultatdefect.setCellValueFactory(new PropertyValueFactory<Entity, String>("rezultatdefect"));
		ultimamodificare.setCellValueFactory(new PropertyValueFactory<Entity, String>("ultimamodificare"));
		utilizatorultimamodificare.setCellValueFactory(new PropertyValueFactory<Entity, String>("utilizatorultimamodificare"));
	}
	
	//populeaza tabelul cu defecte
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
			return new DefecteTable(values);
		
	}


}
