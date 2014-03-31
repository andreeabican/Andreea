package graphicuserinterface;

import general.Constants;

import java.io.PrintWriter;

public class SignUpGraphicUserInterface {
	
	public SignUpGraphicUserInterface() { };
	
	public static void displaySignUpGraphicUserInterface(String signupError, PrintWriter printWriter) {
        String content = new String();
        content += "<html>\n";
        content += "<head>\n";
        content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" /><title>Librarie Virtuala</title>\n";
        content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/bookstore.css\" />\n";  
        content += "</head>\n";
        content += "<body>\n";
        content += "<center>\n";
        content += "<h2>Cont nou</h2>\n";
        content += "<form name=\"formular\" action=\"SignUpServlet\" method=\"POST\">\n";        
        content += "<table>\n";
        content += "<tr><td>Nume</td><td><input type=\"text\" name=\""+Constants.LAST_NAME+"\"></td></tr>\n";
        content += "<tr><td>Prenume</td><td><input type=\"text\" name=\""+Constants.FIRST_NAME+"\"></td></tr>\n";
        content += "<tr><td>Adresa</td><td><input type=\"text\" name=\""+Constants.ADDRESS+"\"></td></tr>\n";
        content += "<tr><td>Telefon</td><td><input type=\"text\" name=\""+Constants.PHONE_NUMBER+"\"></td></tr>\n";
        content += "<tr><td>IBAN</td><td><input type=\"text\" name=\""+Constants.IBAN_ACCOUNT+"\"></td></tr>\n";
        content += "<tr><td>Email</td><td><input type=\"text\" name=\""+Constants.EMAIL+"\"></td></tr>\n";
        content += "<tr><td>CNP</td><td><input type=\"text\" name=\""+Constants.CNP+"\"></td></tr>\n";
        content += "<tr><td>Username</td><td><input type=\"text\" name=\""+Constants.USER_NAME+"\"></td></tr>\n";
        content += "<tr><td>Parola</td><td><input type=\"password\" name=\""+Constants.USER_PASSWORD+"\"></td></tr>\n";
        content += "</table>\n";        
        content += "<input type=\"submit\" name=\""+Constants.SIGN_UP.toLowerCase()+"\" value=\""+Constants.SIGN_UP+"\">\n";        
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
