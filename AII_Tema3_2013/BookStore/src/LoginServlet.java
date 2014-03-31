import dataaccess.DataBaseConnection;
import general.Constants;
import graphicuserinterface.LoginGraphicUserInterface;
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

public class LoginServlet extends HttpServlet {
    final public static long    serialVersionUID = 10001000L;
    
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
    
    public boolean isLoginError(String userName, String userPassword) {
        return (userName != null && !userName.isEmpty() && userPassword != null && !userPassword.isEmpty() && getUserRole(userName,userPassword) == Constants.USER_NONE);
    }
    
    public int getUserRole(String userName, String userPassword) {
        int result = Constants.USER_NONE;
        try {
            ArrayList<String> attributes = new ArrayList<>();
            attributes.add(Constants.USER_ROLE);
            ArrayList<ArrayList<Object>> role = DataBaseConnection.getTableContent(Constants.USERS_TABLE, attributes, Constants.USER_NAME+"=\'"+userName+"\' AND "+Constants.USER_PASSWORD+"=\'"+userPassword+"\'", null, null);
            if (role != null && !role.isEmpty() && role.get(0) != null && role.get(0).get(0) != null)
                switch (role.get(0).get(0).toString()) {
                    case Constants.ADMINISTRATOR_ROLE:
                        return Constants.USER_ADMINISTRATOR;
                    case Constants.CLIENT_ROLE:
                        return Constants.USER_CLIENT;
                    case Constants.TECHNICAL_SUPPORT_ROLE:
                        return Constants.USER_TECHNICAL_SUPPORT;
                }
        } catch (SQLException exception) {
            System.out.println("exceptie: "+exception.getMessage());
            if (Constants.DEBUG)
                exception.printStackTrace();
        }
        return result;
    }

    public String getUserDisplayName(String userName, String userPassword) {
        String result = new String();
        try {
            ArrayList<String> attributes = new ArrayList<>();
            attributes.add(DataBaseConnection.getTableDescription(Constants.USERS_TABLE));
            ArrayList<ArrayList<Object>> displayName = DataBaseConnection.getTableContent(Constants.USERS_TABLE, attributes, Constants.USER_NAME+"=\'"+userName+"\' AND "+Constants.USER_PASSWORD+"=\'"+userPassword+"\'", null, null);
            if (displayName != null)
                return displayName.get(0).get(0).toString();
        } catch (SQLException exception) {
            System.out.println("exceptie: "+exception.getMessage());
            if (Constants.DEBUG)
                exception.printStackTrace();
        }
        return result;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration parameters = request.getParameterNames();
        boolean found = false;
        while(parameters.hasMoreElements()) {
            String parameter = (String)parameters.nextElement();
            if (parameter.equals(Constants.USER_NAME)) {
                found = true;
                userName = request.getParameter(parameter);
            }
            if (parameter.equals(Constants.USER_PASSWORD)) {
                found = true;
                userPassword = request.getParameter(parameter);
            }
        }
        if (!found) {
            userName        = "";
            userPassword    = "";
        }
        response.setContentType("text/html");
        try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
            if (getUserRole(userName,userPassword) != Constants.USER_NONE) {
                HttpSession session = request.getSession(true);
                session.setAttribute(Constants.IDENTIFIER,getUserDisplayName(userName,userPassword));
                RequestDispatcher requestDispatcher = null;
                switch(getUserRole(userName,userPassword)) {
                    case Constants.USER_ADMINISTRATOR:
                        requestDispatcher = getServletContext().getRequestDispatcher("/AdministratorServlet");
                        break;
                    case Constants.USER_CLIENT:
                        requestDispatcher = getServletContext().getRequestDispatcher("/ClientServlet");
                        break;
                    case Constants.USER_TECHNICAL_SUPPORT:
                        requestDispatcher = getServletContext().getRequestDispatcher("/TechnicalConsultantServlet");
                        break;
                }
                if (requestDispatcher!=null) {
                    requestDispatcher.forward(request,response);
                }
            }
            
            LoginGraphicUserInterface.displayLoginGraphicUserInterface(isLoginError(userName,userPassword), printWriter);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
            LoginGraphicUserInterface.displayLoginGraphicUserInterface(isLoginError(userName,userPassword), printWriter);
        }
    }      	 
}
