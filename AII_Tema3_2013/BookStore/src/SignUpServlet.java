import general.Constants;
import graphicuserinterface.LoginGraphicUserInterface;
import graphicuserinterface.SignUpGraphicUserInterface;
import graphicuserinterface.VisitorGraphicUserInterface;

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

import dataaccess.DataBaseConnection;


public class SignUpServlet extends HttpServlet {

	private static final long serialVersionUID = -7643516047269408352L;

	public String               userName, userPassword;
	 
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            DataBaseConnection.openConnection();
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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration parameters = request.getParameterNames();
        String signupError = null;
        boolean ok = false;
        while(parameters.hasMoreElements()) {
            String parameter = (String)parameters.nextElement();
            System.out.println("Parametrii");
            if (parameter.equals("inregistrare")) {
            	ok = true;
            	//get all the input from the user
            	//if one of the fields is not filled display an error
            	ArrayList<String> values = new ArrayList<>();
            	values.add(request.getParameter(Constants.CNP));
            	values.add(request.getParameter(Constants.LAST_NAME));
            	values.add(request.getParameter(Constants.FIRST_NAME));
            	values.add(request.getParameter(Constants.ADDRESS));
            	values.add(request.getParameter(Constants.PHONE_NUMBER));
            	values.add(request.getParameter(Constants.EMAIL));
            	values.add(request.getParameter(Constants.IBAN_ACCOUNT));
            	values.add(Constants.CLIENT_ROLE);
            	values.add(request.getParameter(Constants.USER_NAME));
            	values.add(request.getParameter(Constants.USER_PASSWORD));
            	
            	for (String att : values) {
            		if (att == null || att.isEmpty()) {
            			ok = false;
            			signupError = "Trebuie sa completati toate campurile";
            			break;
            		}
            	}
            	
            	if (ok) {
            		//check if username already exists
            		String query = "SELECT count(*) FROM cerere_cont_utilizator where numeutilizator = \"" + request.getParameter(Constants.USER_NAME) +"\"";
            		System.out.println("Query: " + query);
            		int result = -2;
					try {
						result = Integer.parseInt(DataBaseConnection.executeQuery(query, 1).get(0).get(0).toString());
						System.out.println("Result: " + result);
					} catch (NumberFormatException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            		if (result > 0) {
            			signupError = "Numele de utilizator nu este disponibil";
            			ok = false;
            		}
					else
						try {
							DataBaseConnection.insertValuesIntoTable("cerere_cont_utilizator", null, values, true);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            	}
            }
        }
        
        response.setContentType("text/html");
        try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
        	HttpSession session = request.getSession(true);
        	RequestDispatcher requestDispatcher = null;
        	if (ok)
        		requestDispatcher = getServletContext().getRequestDispatcher("/VisitorServlet");

        	if (requestDispatcher!=null) {
        		requestDispatcher.forward(request,response);
        	}

        	SignUpGraphicUserInterface.displaySignUpGraphicUserInterface(signupError, printWriter);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
            SignUpGraphicUserInterface.displaySignUpGraphicUserInterface(null, printWriter);
        }
    }     
}
