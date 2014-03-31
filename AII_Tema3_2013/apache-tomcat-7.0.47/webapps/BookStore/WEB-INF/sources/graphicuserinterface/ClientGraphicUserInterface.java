package graphicuserinterface;

import dataaccess.DataBaseConnection;
import entities.Record;
import general.Constants;
import general.Utilities;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientGraphicUserInterface {
    
    public ClientGraphicUserInterface() { }
    
    public static String getBookAuthors(String bookID) {
        String result = new String();
        try {
            ArrayList<String> attributes = new ArrayList<>();
            attributes.add("GROUP_CONCAT(CONCAT(s.prenume,' ',s.nume))");
            ArrayList<ArrayList<Object>> tableContent = DataBaseConnection.getTableContent("scriitori s, autori a",attributes,"a.idcarte=\'"+bookID+"\' AND s.idscriitor=a.idscriitor",null,null);
            if (tableContent == null || tableContent.get(0) == null || tableContent.get(0).get(0) == null)
                return "-";
            return tableContent.get(0).get(0).toString();
        } catch (SQLException exception) {
            System.out.println("exceptie: "+exception.getMessage());
            if (Constants.DEBUG)
                exception.printStackTrace();
        }
        return result;
    }
    
    public static ArrayList<String> getDistinctContent(String tableName, String attribute) {
        ArrayList<String> result = new ArrayList<>();
        result.add(Constants.ALL);
        try {
            ArrayList<String> attributes = new ArrayList<>();
            attributes.add("DISTINCT("+attribute+")");
            ArrayList<ArrayList<Object>> tableContent = DataBaseConnection.getTableContent(tableName,attributes,null,null,null);
            for (ArrayList<Object> tableRow:tableContent)
                result.add(tableRow.get(0).toString());
        } catch (SQLException exception) {
            System.out.println("exceptie: "+exception.getMessage());
            if (Constants.DEBUG)
                exception.printStackTrace();
        }
        return result;
    }
    
    public static void displayClientGraphicUserInterface(String userDisplayName, String errorMessage, String currentTableName, String currentCollection, String currentDomain, ArrayList<Record> shoppingCart, PrintWriter printWriter) {
        String content = new String();
        content += "<html>\n";
        content += "<head>\n";
        content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" /><title>Librarie Virtuala</title>\n";
        content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/bookstore.css\" />\n";  
        content += "</head>\n";       
        content += "<body>\n";
        content += "<form name=\"formular\" action=\"ClientServlet\" method=\"POST\">\n";
        content += Constants.WELCOME_MESSAGE+userDisplayName+"<br/>\n";
        // TO DO (exercise 12): add control for client logout
        
        content += "<center>\n";
        content += "<h2>Librarie Virtuala - Achizitie de Carte</h2>\n";
        content += "<table  bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\"><tbody>\n";
        content += "<tr>\n";
        content += "<td align=\"left\" valign=\"top\">\n";
        content += "<table  bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\"><tbody>\n";
        content += "<tr>\n";
        content += "<td>"+Constants.COLLECTION+"</td>\n";
        content += "<td>\n";
        content += "<select name=\"selectedCollection\" onchange=\"document.formular.submit()\">\n";
        for (String collection: getDistinctContent(Constants.COLLECTIONS_TABLE,DataBaseConnection.getTableDescription(Constants.COLLECTIONS_TABLE)))
            if (collection.equals(currentCollection))
                content += "<option value=\""+collection+"\" SELECTED>"+collection+"</option>\n";
            else
                content += "<option value=\""+collection+"\">"+collection+"</option>\n";
        content += "</select>\n";       
        content += "</td>\n";
        content += "</tr>\n";
        content += "<tr><td colspan=\"2\">&nbsp</td></tr>\n";
        content += "<tr>\n";
        content += "<td>"+Constants.DOMAIN+"</td>\n";
        content += "<td>\n";
        content += "<select name=\"selectedDomain\" onchange=\"document.formular.submit()\">\n";
        for (String domain: getDistinctContent(Constants.DOMAINS_TABLE,DataBaseConnection.getTableDescription(Constants.DOMAINS_TABLE)))
            if (domain.equals(currentDomain))
                content += "<option value=\""+domain+"\" SELECTED>"+domain+"</option>\n";
            else
                content += "<option value=\""+domain+"\">"+domain+"</option>\n";
        content += "</select>\n";
        content += "</td>\n";
        content += "</tr>\n";
        content += "</tbody></table>\n";
        content += "</td>\n";
        content += "<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n";
        content += "<td width=\"60%\" align=\"center\">\n";
        try {
            if (errorMessage != null)
                content += errorMessage + "<br/><br/>\n";
            ArrayList<String> attributes = DataBaseConnection.getTableAttributes(currentTableName);
            String whereClause = new String();
            
            // TO DO (exercise 7): construct whereClause as to filter books by collections and domains
            
            ArrayList<ArrayList<Object>> tableContent = DataBaseConnection.getTableContent(currentTableName,attributes,(whereClause.length()!=0?whereClause:null),null,null);
            int primayKeyIndex = DataBaseConnection.getAttributeIndexInTable(currentTableName, DataBaseConnection.getTablePrimaryKey(currentTableName));
            content += "<table  bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\"><tbody>\n";
            for (ArrayList<Object> tableRow:tableContent) {
                String currentPrimaryKey = tableRow.get(primayKeyIndex).toString();
                content += "<tr>\n";
                content += "<td valign=\"top\"><img src=\"images/"+Utilities.removeSpaces(tableRow.get(DataBaseConnection.getAttributeIndexInTable(currentTableName, Constants.TITLE)).toString().toLowerCase())+".jpg\"/></td>\n";
                content += "<td>&nbsp</td>\n";
                content += "<td bgcolor=\"#ebebeb\">\n";
                content += "autor(i): "+getBookAuthors(currentPrimaryKey)+"<br/>\n";
                int currentIndex = 0;
                for (Object tableColumn:tableRow) {
                    if (currentIndex != 0)
                        content += attributes.get(currentIndex)+": "+tableColumn.toString()+"<br/>\n";
                    currentIndex++;
                }
                content += Constants.COPIES+": <input type=\"text\" name=\""+Constants.COPIES.toLowerCase()+"_"+currentPrimaryKey+"\" size=\"3\"/><br/>\n";
                content += "<input type=\"submit\" name=\""+Constants.ADD_BUTTON_NAME.toLowerCase()+"_"+currentPrimaryKey+"\" value=\""+Constants.ADD_BUTTON_NAME+"\"/><br/>\n";
                content += "</td>\n";
                content += "</tr><tr><td colspan=\"3\">&nbsp;</td></tr>\n";
            }
            content += "</tbody></table>\n";
            content += "</td>\n";
            content += "<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n";
            content += "<td align=\"left\" valign=\"top\">\n";
            content += "<table><tr><td><img src=\"images/shoppingcart.png\"/></td><td valign=\"center\">"+Constants.SHOPPING_CART+"</td></tr></table><br/>\n";
            if (shoppingCart != null && !shoppingCart.isEmpty()) {
                // TO DO (exercise 9): get shopping cart content and display it
                
                // TO DO (exercise 10): add controls for finalizing and canceling command
                
            } else {
                content += Constants.EMPTY_CART+"<br/>\n";
            } 
            content += "</td>\n";
            content += "</tr>\n";
            content += "</tbody></table>\n";
        } catch (SQLException exception) {
            System.out.println ("exceptie: "+exception.getMessage());
            if (Constants.DEBUG)
                exception.printStackTrace();
        }       
        content += "</form>\n";
        content += "</center>\n";
        content += "</body>\n";
        content += "</html>";  
        printWriter.println(content);
    }    
}
