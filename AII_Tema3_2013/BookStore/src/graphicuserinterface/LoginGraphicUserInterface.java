package graphicuserinterface;

import general.Constants;
import java.io.PrintWriter;

public class LoginGraphicUserInterface {
    
    public LoginGraphicUserInterface() { }
    
    public static void displayLoginGraphicUserInterface(boolean isLoginError, PrintWriter printWriter) {
        String content = new String();
        content += "<html>\n";
        content += "<head>\n";
        content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" /><title>Librarie Virtuala</title>\n";
        content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/bookstore.css\" />\n";  
        content += "</head>\n";
        content += "<body>\n";
        content += "<center>\n";
        content += "<h2>Librarie Virtuala - Autentificare</h2>\n";
        content += "<form name=\"formular\" action=\"LoginServlet\" method=\"POST\">\n";        
        content += "<table>\n";
        content += "<tr><td>Utilizator</td><td><input type=\"text\" name=\""+Constants.USER_NAME+"\"></td></tr>\n";
        content += "<tr><td>Parola</td><td><input type=\"password\" name=\""+Constants.USER_PASSWORD+"\"></td></tr>\n";
        content += "</table>\n";        
        content += "<input type=\"submit\" name=\""+Constants.LOGIN.toLowerCase()+"\" value=\""+Constants.LOGIN+"\">\n";        
        content += "</form>\n";
        content += "Creeaza cont nou acum:";
        content += "<a href=\"SignUpServlet\"> Cont nou </a>";
        content += "<p>\n";        
        if (isLoginError)
            content += "<font color=\"red\">"+Constants.LOGIN_ERROR+"</font>\n";
        content += "</center>\n";
        content += "</body>\n";
        content += "</html>";
        printWriter.println(content);
    }
}
