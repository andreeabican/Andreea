import dataaccess.DataBaseConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import dataaccess.DataBaseConnection;
import general.Constants;
import general.queryUtil;
import graphicuserinterface.AdminMainPage;
import graphicuserinterface.AdministratorGUI;
import graphicuserinterface.ContabilMainPage;
import graphicuserinterface.DataBaseManagementGUI;
import graphicuserinterface.Defecte;
import graphicuserinterface.HRMainPage;
import graphicuserinterface.HRManagementGUI;
import graphicuserinterface.ProgrammerAndControllerGUI;


public class LoginPage extends Application implements EventHandler {
	 private Stage               applicationStage;
	    private Scene               applicationScene;
	    
	    @FXML private TextField     campTextNumeUtilizator;
	    @FXML private PasswordField campTextParola;
	    @FXML private Label         etichetaAfisare;    
	    
	    public LoginPage() {
	    }  
	    
	    @Override
	    public void start(Stage mainStage) {
	        applicationStage = mainStage;
	        try {
	            applicationScene = new Scene((Parent)FXMLLoader.load(getClass().getResource(Constants.FXML_DOCUMENT_NAME)));
	            applicationScene.addEventHandler(EventType.ROOT,(EventHandler<? super Event>)this);
	        } catch (Exception exception) {
	            System.out.println ("exception : "+exception.getMessage());
	        }        
	        applicationStage.setTitle(Constants.APPLICATION_NAME);
	        applicationStage.getIcons().add(new Image(Constants.ICON_FILE_NAME));
	        applicationStage.setScene(applicationScene);
	        applicationStage.show();
	    }
	    
	    @FXML protected void handleButonAcceptareAction(ActionEvent event) throws Exception {
	    	ArrayList<String> attributes = new ArrayList<>();
	    	attributes.add("idutilizator");
	    	attributes.add("nume");
	    	attributes.add("prenume");
	    	
	    	String whereClause = "numeutilizator = " + "\"" + campTextNumeUtilizator.getText() + "\"" + " and parola = " + "\"" + campTextParola.getText() + "\"";
	    	ArrayList<ArrayList<Object>> result = DataBaseConnection.getTableContent("utilizatori", attributes, whereClause, null, null);
	    	if (result.size() > 0) {
	    		ArrayList<Object> r = result.get(0);
	    		int userid = Integer.parseInt((String)result.get(0).get(0));
	    		startGUI(userid);
	    	}
	    	else {
	    		etichetaAfisare.setText(Constants.ERROR_USERNAME_PASSWORD);
	    	}
	    }  
	    
	    @FXML protected void handleButonRenuntareAction(ActionEvent event) {
	    	System.exit(0);
	    } 
	    
	    @Override
	    public void handle(Event event) {
	        if (event.getTarget() instanceof Button && ((Button)event.getTarget()).getText().equals(Constants.SUBMIT_BUTTON) && event.getEventType().equals(ActionEvent.ACTION) && !applicationStage.isFocused()) {
	           applicationStage.hide(); 
	        }
	    }
	    
	    public static void main(String[] args) {
	        launch(args);
	    }
	    
	    public static void startGUI(int userid) throws SQLException, IOException {
	    	String tiputilizator = queryUtil.tipUtilizator(userid);
	    	if (tiputilizator.equals("administrator") || tiputilizator.equals("super-administrator")) {
	    		AdminMainPage adminmainpage = new AdminMainPage(userid);
	    		adminmainpage.start();
	    		return;
	    	}
	    	int iddepartament = -1;
	    	String query = "SELECT F.iddepartament FROM asociereutilizatorfunctie A, functii F WHERE " +
	    					"A.idutilizator = " + "\'" + userid +"\'" + " AND " +
	    					"A.idfunctie = F.idfunctie";
			ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 1);
			if (result != null) {
				ArrayList<Object> value = result.get(0);
				iddepartament = Integer.parseInt(value.get(0).toString());
			}
			
			switch (iddepartament) {
			// HR
			case 1:
				HRMainPage hrmainpage = new HRMainPage(userid);
				hrmainpage.start();
				break;
			// contabilitate
			case 2:
				ContabilMainPage contabilmainpage = new ContabilMainPage(userid);
				contabilmainpage.start();
				break;
			// programare
			case 3:
				ProgrammerAndControllerGUI programmerAndControllerGUI = new ProgrammerAndControllerGUI(userid);
				programmerAndControllerGUI.start();
				break;
			// asigurarea calitatii
			case 4:
				ProgrammerAndControllerGUI programmerAndControllerGUI1 = new ProgrammerAndControllerGUI(userid);
				programmerAndControllerGUI1.start();
				break;
			default:
				break;
			
			}
	    }
}
