import dataaccess.DataBaseConnection;
import entities.Record;
import general.Constants;
import general.Utilities;
import graphicuserinterface.ClientGraphicUserInterface;
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

public class ClientServlet extends HttpServlet {
    final public static long    serialVersionUID = 10021002L;
    
    public String               selectedTable, selectedCollection, selectedDomain;
    public String               userDisplayName;
    
    public ArrayList<Record>	shoppingCart;
 
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            DataBaseConnection.openConnection();
            selectedTable       = Constants.BOOKS_TABLE;
            selectedCollection  = Constants.ALL;
            selectedDomain      = Constants.ALL;
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
        HttpSession session = request.getSession(true);
        try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
            userDisplayName = session.getAttribute(Constants.IDENTIFIER).toString();
            shoppingCart = (ArrayList<Record>)session.getAttribute(Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()));
            if (shoppingCart == null) {
                shoppingCart = new ArrayList<>();
            }            
            String errorMessage = "";
            Enumeration parameters = request.getParameterNames();
            while(parameters.hasMoreElements()) {
                String parameter = (String)parameters.nextElement();                
                if (parameter.equals("selectedCollection")) {
                    selectedCollection = request.getParameter(parameter);
                }
                if (parameter.equals("selectedDomain")) {
                    selectedDomain = request.getParameter(parameter);
                }               
                
                // TO DO (exercise 8): create shopping cart
                
                // TO DO (exercise 11): add logic for canceling / completing command
                
                // TO DO (exercise 12): add logic for client logout
            }
            response.setContentType("text/html");
            ClientGraphicUserInterface.displayClientGraphicUserInterface(userDisplayName,errorMessage,selectedTable,selectedCollection,selectedDomain,shoppingCart,printWriter);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        userDisplayName = session.getAttribute(Constants.IDENTIFIER).toString();
        shoppingCart = (ArrayList<Record>)session.getAttribute(Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()));

        response.setContentType("text/html");
        try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
            ClientGraphicUserInterface.displayClientGraphicUserInterface(userDisplayName,null,selectedTable,selectedCollection,selectedDomain,shoppingCart,printWriter);
        }
    }     	 
}
