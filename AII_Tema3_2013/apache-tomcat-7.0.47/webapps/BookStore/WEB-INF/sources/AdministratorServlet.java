import dataaccess.DataBaseConnection;
import entities.Record;
import general.Constants;
import graphicuserinterface.AdministratorGraphicUserInterface;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdministratorServlet extends HttpServlet {
    final public static long    serialVersionUID = 10011001L;
    
    private ArrayList<String>   dataBaseStructure;
    private String              selectedTable;
    private String              userDisplayName;
 
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            DataBaseConnection.openConnection();
            dataBaseStructure   = DataBaseConnection.getTableNames();
            selectedTable       = dataBaseStructure.get(0);
        } catch (SQLException exception) {
            System.out.println("exceptie: "+exception.getMessage());
            if (Constants.DEBUG)
                exception.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            DataBaseConnection.closeConnection();
        } catch (SQLException exception) {
            System.out.println("exceptie: "+exception.getMessage());
            if (Constants.DEBUG)
                exception.printStackTrace();
        }
    }

    public ArrayList<String> getAttributes(ArrayList<Record> records) {
        ArrayList<String> result = new ArrayList<>();
        for (Record record: records) {
            result.add(record.getAttribute());
        }
        return result;
    }

    public ArrayList<String> getValues(String tableName, ArrayList<Record> records) {
        ArrayList<String> result        = new ArrayList<>();       
        ArrayList<String> attributes    = getAttributes(records);
        for (String attribute:attributes) {
            for (Record record: records) {
                if (attribute.equals(record.getAttribute())) {
                    String value = record.getValue();
                    if (value == null || value.equals("")) {
                        value = Constants.INVALID_VALUE;
                    }
                    result.add(value);
                }
            }
        }
        return result;      
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<Record> insertRecords  = new ArrayList<>();
        ArrayList<Record> updateRecords  = new ArrayList<>();
        ArrayList<Record> deleteRecords  = new ArrayList<>();
        ArrayList<Record> genericRecords = new ArrayList<>();
        Enumeration parameters = request.getParameterNames();        
        int operation = Constants.OPERATION_NONE;
        String primaryKeyAttribute = new String();
        try {
            primaryKeyAttribute = DataBaseConnection.getTablePrimaryKey(selectedTable);
        } catch (SQLException exception) {
            System.out.println ("exceptie: "+exception.getMessage());
            if (Constants.DEBUG)
                exception.printStackTrace();
        }
        String primaryKeyValue = new String();
        while(parameters.hasMoreElements()) {
            String parameter = (String)parameters.nextElement();
            if (parameter.equals(Constants.LOGOUT.toLowerCase())) {
                operation = Constants.OPERATION_LOGOUT;
            }
            if (parameter.equals(Constants.ADD_BUTTON_NAME.toLowerCase())) {
                operation = Constants.OPERATION_INSERT;
            }
            else if (parameter.contains(Constants.UPDATE_BUTTON_NAME.toLowerCase()+"1_")) {
                operation = Constants.OPERATION_UPDATE_PHASE1;
                primaryKeyValue = parameter.substring(parameter.indexOf("_")+1);
            }
            else if (parameter.contains(Constants.UPDATE_BUTTON_NAME.toLowerCase()+"2_")) {
                operation = Constants.OPERATION_UPDATE_PHASE2;
                primaryKeyValue = parameter.substring(parameter.indexOf("_")+1);
            }            
            else if (parameter.contains(Constants.DELETE_BUTTON_NAME.toLowerCase())) {
                operation = Constants.OPERATION_DELETE;
                primaryKeyValue = parameter.substring(parameter.indexOf("_")+1);
                deleteRecords.add(new Record(primaryKeyAttribute,primaryKeyValue));
            }
            else {
                genericRecords.add(new Record(parameter,request.getParameter(parameter)));
            }
            if (parameter.equals(Constants.SELECTED_TABLE)) {
                selectedTable = request.getParameter(parameter);
            }
        }
        response.setContentType("text/html");
        try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
            switch (operation) {
                case Constants.OPERATION_INSERT:
                    for (Record record:genericRecords) {
                        String attribute = record.getAttribute();
                        String value     = record.getValue();
                        if (attribute.endsWith("_"+Constants.ADD_BUTTON_NAME.toLowerCase())) {
                            insertRecords.add(new Record(attribute.substring(0,attribute.indexOf("_")),value));
                        }
                    }
                    try {
                        DataBaseConnection.insertValuesIntoTable(selectedTable,getAttributes(insertRecords),getValues(selectedTable,insertRecords),false);
                    } catch (Exception exception) {
                        System.out.println ("exceptie: "+exception.getMessage());
                        if (Constants.DEBUG)
                            exception.printStackTrace();
                    }
                    break;
                case Constants.OPERATION_UPDATE_PHASE2:
                    String whereClause = new String();
                    for (Record record:genericRecords) {
                        String attribute = record.getAttribute();
                        String value     = record.getValue();
                        if (attribute.endsWith("_"+primaryKeyValue)) {
                            if (attribute.startsWith(primaryKeyAttribute))
                                whereClause += primaryKeyAttribute + "=\'"+ primaryKeyValue + "\'";
                            else
                                updateRecords.add(new Record(attribute.substring(0,attribute.indexOf("_")),value));
                        }
                    }    
                    try {
                        if (whereClause.isEmpty())
                            whereClause += primaryKeyAttribute + "=\'"+ primaryKeyValue + "\'";
                        DataBaseConnection.updateRecordsIntoTable(selectedTable,getAttributes(updateRecords),getValues(selectedTable,updateRecords),whereClause);
                    } catch (Exception exception) {
                        System.out.println ("exceptie: "+exception.getMessage());
                        if (Constants.DEBUG)
                            exception.printStackTrace();
                    }
                    break;
                case Constants.OPERATION_DELETE:
                    try {
                        DataBaseConnection.deleteRecordsFromTable(selectedTable,getAttributes(deleteRecords),getValues(selectedTable,deleteRecords),null);
                    } catch (Exception exception) {
                        System.out.println ("exceptie: "+exception.getMessage());
                        if (Constants.DEBUG)
                            exception.printStackTrace();
                    }
                    break;
                case Constants.OPERATION_LOGOUT:
                    // TO DO (exercise 12): add logic for administrator logout
                    break;
            } 	
            
            HttpSession session = request.getSession(true);
            userDisplayName = session.getAttribute(Constants.IDENTIFIER).toString();        
            if (operation == Constants.OPERATION_UPDATE_PHASE1) {
                AdministratorGraphicUserInterface.displayAdministratorGraphicUserInterface(userDisplayName, dataBaseStructure, selectedTable, primaryKeyValue, printWriter);
            }
            else {
                AdministratorGraphicUserInterface.displayAdministratorGraphicUserInterface(userDisplayName, dataBaseStructure, selectedTable, null, printWriter);
            }
        }	
    }     

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        userDisplayName = session.getAttribute(Constants.IDENTIFIER).toString();       
        response.setContentType("text/html");
        try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
            AdministratorGraphicUserInterface.displayAdministratorGraphicUserInterface(userDisplayName, dataBaseStructure, selectedTable, null, printWriter);
        }
    }  	 
}
