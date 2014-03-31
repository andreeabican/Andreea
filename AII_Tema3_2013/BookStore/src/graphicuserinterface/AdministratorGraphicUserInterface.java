package graphicuserinterface;

import dataaccess.DataBaseConnection;
import general.Constants;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdministratorGraphicUserInterface {
    
    public AdministratorGraphicUserInterface() { }
    
    public static void displayAdministratorGraphicUserInterface(String userDisplayName, ArrayList<String> dataBaseStructure, String currentTableName, String primaryKeyValueForEditableRecord, PrintWriter printWriter) {
        String content = new String();
        content += "<html>\n";
        content += "<head>\n";
        content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" /><title>Librarie Virtuala</title>\n";
        content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/bookstore.css\" />\n";  
        content += "</head>\n";       
        content += "<body>\n";
        content += "<form name=\"formular\" action=\"AdministratorServlet\" method=\"POST\">\n";  
        content += Constants.WELCOME_MESSAGE+userDisplayName+"<br/>\n";
        // TO DO (exercise 12): add control for administrator logout
        content += "<input type = \"submit\" value=\"Logout\" name=\"" + Constants.LOGOUT.toLowerCase() + "\">";
        content += "<center>\n";
        content += "<h2>Magazin Virtual - Pagina de Administrare</h2>\n";              
        content += "<select name=\"selectedTable\" onchange=\"document.formular.submit()\">\n";
        for (String tableName: dataBaseStructure)
            if (tableName.equals(currentTableName))
                content += "<option value=\""+tableName+"\" SELECTED>"+tableName+"</option>\n";
            else
                content += "<option value=\""+tableName+"\">"+tableName+"</option>\n";
        content += "</select>\n";
        content += "<p>\n";      
        try {
            content += "<table  bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\"><tbody>\n"; 
            ArrayList<String> attributes = DataBaseConnection.getTableAttributes(currentTableName);
            int primayKeyIndex = DataBaseConnection.getAttributeIndexInTable(currentTableName, DataBaseConnection.getTablePrimaryKey(currentTableName));
            content += "<tr>\n";
            for (String attribute: attributes)
                content += "<th>"+attribute+"</th>\n";
            content += "<th></th><th></th>\n";
            if (currentTableName.equals("cerere_cont_utilizator")) {
            	content += "<th></th><th></th></tr>\n";
            } else {
            	content += "</tr>\n";
            }
            content += "<tr  bgcolor=\"#ebebeb\">\n";
            for (String attribute: attributes)
                if (attribute.equals(DataBaseConnection.getTablePrimaryKey(currentTableName)) && !currentTableName.equals(Constants.USERS_TABLE))
                    content += "<td><input type=\"text\" name=\""+attribute+"_"+Constants.ADD_BUTTON_NAME.toLowerCase()+"\" value=\""+(DataBaseConnection.getTablePrimaryKeyMaxValue(currentTableName)+1)+"\" disabled/></td>\n";
                else
                    content += "<td><input type=\"text\" name=\""+attribute+"_"+Constants.ADD_BUTTON_NAME.toLowerCase()+"\" /></td>\n";
            if (currentTableName.equals("cerere_cont_utilizator"))
            	content += "<td align=\"center\" colspan=\"4\"><input type=\"submit\" name=\""+Constants.ADD_BUTTON_NAME.toLowerCase()+"\" value=\""+Constants.ADD_BUTTON_NAME+"\"></td>\n";
            else
            	content += "<td align=\"center\" colspan=\"2\"><input type=\"submit\" name=\""+Constants.ADD_BUTTON_NAME.toLowerCase()+"\" value=\""+Constants.ADD_BUTTON_NAME+"\"></td>\n";
            content += "</tr>\n";             
            ArrayList<ArrayList<Object>> tableContent = DataBaseConnection.getTableContent(currentTableName,attributes,null,null,null);
            for (ArrayList<Object> tableRow:tableContent) {
                content += "<tr bgcolor=\"#ebebeb\">\n";
                String currentPrimaryKey = tableRow.get(primayKeyIndex).toString();
                boolean isEditableRecord = (primaryKeyValueForEditableRecord != null && primaryKeyValueForEditableRecord.equals(currentPrimaryKey));
                int currentIndex = 0;
                for (Object tableColumn:tableRow) {                    
                    if (isEditableRecord)                            
                        content += "<td><input type=\"text\" name=\""+attributes.get(currentIndex)+"_"+currentPrimaryKey+"\" value=\""+tableColumn.toString()+"\" "+((attributes.get(currentIndex).equals(DataBaseConnection.getTablePrimaryKey(currentTableName)) && !currentTableName.equals(Constants.USERS_TABLE))?"disabled":"")+"/></td>";
                    else
                        content += "<td>"+tableColumn.toString()+"</td>";
                    currentIndex++;                    
                }
                content += "<td><input type=\"submit\" name=\""+Constants.UPDATE_BUTTON_NAME.toLowerCase()+(isEditableRecord?"2":"1")+"_"+currentPrimaryKey+"\" value=\""+Constants.UPDATE_BUTTON_NAME+"\"></td><td><input type=\"submit\" name=\""+Constants.DELETE_BUTTON_NAME.toLowerCase()+"_"+currentPrimaryKey+"\" value=\""+Constants.DELETE_BUTTON_NAME+"\"></td>\n";
                if (currentTableName.equals("cerere_cont_utilizator")) {
                	content += "<td><input type=\"submit\" name=\""+Constants.ACCEPT_BUTTON_NAME.toLowerCase()+"_"+currentPrimaryKey+"\" value=\""+Constants.ACCEPT_BUTTON_NAME+"\"></td>\n";
                	content += "<td><input type=\"submit\" name=\""+Constants.REJECT_BUTTON_NAME.toLowerCase()+"_"+currentPrimaryKey+"\" value=\""+Constants.REJECT_BUTTON_NAME+"\"></td>\n";
                }
                content += "</tr>\n";
            }           
            content += "</tbody></table>\n";
        } catch (SQLException exception) {
            System.out.println ("exceptie: "+exception);
            if (Constants.DEBUG)
                exception.printStackTrace();
        }
        content += "</form>\n";
        content += "</center>\n";
        content += "</body>\n";
        content += "</html>\n";        
        printWriter.println(content);
    }    
}
