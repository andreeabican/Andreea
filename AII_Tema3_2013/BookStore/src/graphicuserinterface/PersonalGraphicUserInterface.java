package graphicuserinterface;

import general.Constants;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import dataaccess.DataBaseConnection;

public class PersonalGraphicUserInterface {
	
	public PersonalGraphicUserInterface() { }
	
	public static String getUserId(String userDisplayName) throws SQLException {
    	String[] numeprenume = userDisplayName.split(" ");
        String nume = numeprenume[1];
        String prenume = numeprenume[0];
        
        String query = "SELECT idutilizator from utilizatori WHERE "
        		+ "nume = \"" + nume + "\" AND "
        		+ "prenume = \"" + prenume + "\""; 
    	ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 1);
    	String userid = result.get(0).get(0).toString();
    	return userid;
    }
	
	public static void displayPersonalGraphicUserInterface(String userDisplayName, String errorMesage, Integer idFacturaDetaliata, ArrayList<String> dataBaseStructure, String currentTableName, String primaryKeyValueForEditableRecord, PrintWriter printWriter) {
        String content = new String();
        content += "<html>\n";
        content += "<head>\n";
        content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" /><title>Magazin Virtual</title>\n";
        content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/bookstore.css\" />\n";  
        content += "</head>\n";       
        content += "<body>\n";
        content += "<form name=\"formular\" action=\"PersonalServlet\" method=\"POST\">\n";  
        content += Constants.WELCOME_MESSAGE+userDisplayName+"<br/>\n";
        content += "<input type = \"submit\" value=\"Logout\" name=\"" + Constants.LOGOUT.toLowerCase() + "\">";
        content += "<center>\n";
        content += "<h2>Informatii personale</h2>\n";              
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
            if (currentTableName.equals("facturi")) {
            	content += "<th></th></tr>\n";
            } else {
            	content += "</tr>\n";
            }
            /*content += "<tr  bgcolor=\"#ebebeb\">\n";
            for (String attribute: attributes)
                if (attribute.equals(DataBaseConnection.getTablePrimaryKey(currentTableName)) && !currentTableName.equals(Constants.USERS_TABLE))
                    content += "<td><input type=\"text\" name=\""+attribute+"_"+Constants.ADD_BUTTON_NAME.toLowerCase()+"\" value=\""+(DataBaseConnection.getTablePrimaryKeyMaxValue(currentTableName)+1)+"\" disabled/></td>\n";
                else
                    content += "<td><input type=\"text\" name=\""+attribute+"_"+Constants.ADD_BUTTON_NAME.toLowerCase()+"\" /></td>\n";
            if (currentTableName.equals("cerere_cont_utilizator"))
            	content += "<td align=\"center\" colspan=\"4\"><input type=\"submit\" name=\""+Constants.ADD_BUTTON_NAME.toLowerCase()+"\" value=\""+Constants.ADD_BUTTON_NAME+"\"></td>\n";
            else
            	content += "<td align=\"center\" colspan=\"2\"><input type=\"submit\" name=\""+Constants.ADD_BUTTON_NAME.toLowerCase()+"\" value=\""+Constants.ADD_BUTTON_NAME+"\"></td>\n";
            content += "</tr>\n"; */   
            
            String whereClause = new String();
            String userid1 = getUserId(userDisplayName);
            if (currentTableName.equals("utilizatori")) {
            	whereClause = "idutilizator = " + userid1;
            } else {
            	whereClause = "idutilizator = " + userid1;
            }
            ArrayList<ArrayList<Object>> tableContent = DataBaseConnection.getTableContent(currentTableName,attributes, whereClause,null,null);
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
                if (currentTableName.equals("utilizatori")) {
                	content += "<td><input type=\"submit\" name=\""+Constants.UPDATE_BUTTON_NAME.toLowerCase()+(isEditableRecord?"2":"1")+"_"+currentPrimaryKey+"\" value=\""+Constants.UPDATE_BUTTON_NAME+"\"></td>\n";
//            		+ "<td><input type=\"submit\" name=\""+Constants.DELETE_BUTTON_NAME.toLowerCase()+"_"+currentPrimaryKey+"\" value=\""+Constants.DELETE_BUTTON_NAME+"\"></td>\n";
                }
                
                if (currentTableName.equals("facturi") && tableRow.get(4).toString().equals("emisa")) {
                	System.out.println("Asta e: " + attributes.get(4).toString());
                	content += "<td><input type=\"submit\" name=\""+Constants.PAY_BUTTON_NAME.toLowerCase()+"_"+currentPrimaryKey+"\" value=\""+Constants.PAY_BUTTON_NAME+"\"></td>\n";
                	content += "<td><input type=\"submit\" name=\""+Constants.CANCEL_BUTTON_NAME.toLowerCase()+"_"+currentPrimaryKey+"\" value=\""+Constants.CANCEL_BUTTON_NAME+"\"></td>\n";
                	content += "<td><input type=\"submit\" name=\""+Constants.DETAILS_BUTTON_NAME.toLowerCase()+"_"+currentPrimaryKey+"\" value=\""+Constants.DETAILS_BUTTON_NAME+"\"></td>\n";
                } 
                else if (currentTableName.equals("facturi") && !tableRow.get(4).toString().equals("emisa")){
                	content += "<td></td><td></td>\n";
                	content += "<td><input type=\"submit\" name=\""+Constants.DETAILS_BUTTON_NAME.toLowerCase()+"_"+currentPrimaryKey+"\" value=\""+Constants.DETAILS_BUTTON_NAME+"\"></td>\n";
                }
                content += "</tr>\n";
            }           
            content += "</tbody></table>\n";
            
            //afiseaza detalii factura
            if (idFacturaDetaliata != -1 && currentTableName.equals("facturi")) {
            	content += "<table  bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\"><tbody>\n"; 
                content += "<tr>\n";
                content += "<th>Denumire</th>\n";
                content += "<th>Pret</th>\n";
                content += "<th>Cantitate</th>\n";
                content += "</tr>\n";
                 
                String query = "select p.denumire, pretProiect(p.idproiect), rf.cantitate "
                				+ "from proiecte p, randfactura rf "
                				+ "where "
                				+ "p.idproiect = rf.idproiect and rf.idfactura = " + idFacturaDetaliata;
                ArrayList<ArrayList<Object>> tableContent1 = DataBaseConnection.executeQuery(query, 3);
                for (ArrayList<Object> tableRow:tableContent1) {
                    content += "<tr bgcolor=\"#ebebeb\">\n";
                    for (Object tableColumn:tableRow) {                    
                            content += "<td>"+tableColumn.toString()+"</td>";
                    }
                    
                    content += "</tr>\n";
                }           
                content += "</tbody></table>\n";
                
            }
        } catch (SQLException exception) {
            System.out.println ("exceptie: "+exception);
            if (Constants.DEBUG)
                exception.printStackTrace();
        }
        if (currentTableName.equals("facturi") && errorMesage != null)
        	content += "<p style = \"color:red;\">" + errorMesage + "</p>";
        content += "</form>\n";
        content += "</center>\n";
        content += "</body>\n";
        content += "</html>\n";        
        printWriter.println(content);
    }
}
