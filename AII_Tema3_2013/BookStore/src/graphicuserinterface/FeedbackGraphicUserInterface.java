package graphicuserinterface;

import general.Constants;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dataaccess.DataBaseConnection;

public class FeedbackGraphicUserInterface {

	public FeedbackGraphicUserInterface() { }
	
	public static void displayFeedbackGraphicUserInterface(String userDisplayName, String signupError, int projectid, PrintWriter printWriter) {
        String content = new String();
        content += "<html>\n";
        content += "<head>\n";
        content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" /><title>Magazin Virtual</title>\n";
        content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/bookstore.css\" />\n";  
        content += "</head>\n";
        content += "<body>\n";
        content += "<center>\n";
        content += "<h2>Probleme</h2>\n";
        content += "<form name=\"formular\" action=\"FeedbackServlet\" method=\"POST\">\n";        
        content += "<table>\n";
        content += "<tr><td>Puteti scrie aici ce probleme ati intampinat:</td></tr>\n";
        content += "<tr><td><input type=\"text\" name=\"" + Constants.MESSAGE_SUBJECT +"\" placeholder=\"" + Constants.MESSAGE_SUBJECT+"\">\n";
        content += "<tr><td><textarea rows = \"10\" cols = \"100\" name=\"mesaj\" placeholder=\"mesaj\"></textarea></td></tr>\n";
        content += "</table>\n";        
        content += "<input type=\"submit\" name=\""+Constants.PROBLEMS_BUTTON_NAME.toLowerCase()+"_"+projectid+"\" value=\""+Constants.PROBLEMS_BUTTON_NAME+"\">\n";        
        content += "</form>\n";
        content += "<p>\n";        
        if (signupError != null)
            content += "<font color=\"red\">"+signupError+"</font>\n";
        content += "</center>\n";
        content += "</body>\n";
        content += "</html>";
        printWriter.println(content);
    }

}
