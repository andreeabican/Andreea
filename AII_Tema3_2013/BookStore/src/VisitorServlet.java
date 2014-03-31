import general.Constants;
import general.Utilities;
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
import entities.Record;


public class VisitorServlet extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7970867758031078140L;
	

    public String               selectedTable;
    //public String               userDisplayName;
    public Double				minPrice, maxPrice;
    public String				nameSearch, descriptionSearch;
    public Boolean				sortByPrice, sortByRating;
    
    //public ArrayList<Record>	shoppingCart;
 
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            DataBaseConnection.openConnection();
            selectedTable       = Constants.BOOKS_TABLE;
            minPrice 			= null;
            maxPrice			= null;
            nameSearch			= null;
            descriptionSearch	= null;
            sortByPrice			= false;
            sortByRating		= false;
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
            //userDisplayName = session.getAttribute(Constants.IDENTIFIER).toString();
            //shoppingCart = (ArrayList<Record>)session.getAttribute(Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()));
            //if (shoppingCart == null) {
              //  shoppingCart = new ArrayList<>();
            //}            
            String errorMessage = "";
            Enumeration parameters = request.getParameterNames();
            while(parameters.hasMoreElements()) {
                String parameter = (String)parameters.nextElement();                
                
                // cautare dupa nume
                if (parameter.equals("cautaredupadenumire")) {
                	nameSearch = request.getParameter("cautaredenumire");
                	if (nameSearch.isEmpty())
                		nameSearch = null;
                	System.out.println("Name search: " + nameSearch);
                }
                
                // cautare dupa descriere
                if (parameter.equals("cautaredupadescriere")) {
                	descriptionSearch = request.getParameter("cautaredescriere");
                	if (descriptionSearch.isEmpty())
                		descriptionSearch = null;
                	System.out.println("description Search: " + descriptionSearch);
                }    
                
                //sortare dupa pret
                if (parameter.equals("sortaredupapret")) {
                	sortByPrice = !sortByPrice;
                	sortByRating = false;
                }
                
              //sortare dupa rating
                if (parameter.equals("sortareduparating")) {
                	sortByRating = !sortByRating;
                	sortByPrice = false;
                } 
                
                //aplicare filtru pret
                if (parameter.equals("filtrupret")) {
                	String minP, maxP;
                	maxP = request.getParameter("maxPrice");
                	minP = request.getParameter("minPrice");
                	if (minP != null && !minP.isEmpty())
                		minPrice = Double.parseDouble(minP);
                	else
                		minPrice = null;
                	if (maxP != null && !maxP.isEmpty())
                		maxPrice = Double.parseDouble(maxP);
                	else
                		maxPrice = null;
                	System.out.println("pret minim: " + minPrice + "\n pret maxim: " + maxPrice);
                }
                
                // autentificare
                if (parameter.equals("autentificare")) {
                	RequestDispatcher requestDispatcher = null;
                	requestDispatcher = getServletContext().getRequestDispatcher("/LoginServlet");
                	if (requestDispatcher != null) {
                		requestDispatcher.forward(request, response);
                	}
                }
                
                //creeaza cont nou
                if (parameter.equals("contnou")) {
                	RequestDispatcher requestDispatcher = null;
                	requestDispatcher = getServletContext().getRequestDispatcher("/SignUpServlet");
                	if (requestDispatcher != null) {
                		requestDispatcher.forward(request, response);
                	}
                }
                
            }
            response.setContentType("text/html");
            VisitorGraphicUserInterface.displayVisitorGraphicUserInterface(errorMessage, minPrice, maxPrice, nameSearch, descriptionSearch, sortByPrice, sortByRating, printWriter);
        } catch (Exception e) {
//			 TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        //userDisplayName = session.getAttribute(Constants.IDENTIFIER).toString();
        //shoppingCart = (ArrayList<Record>)session.getAttribute(Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()));

        response.setContentType("text/html");
        try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
            VisitorGraphicUserInterface.displayVisitorGraphicUserInterface(null, minPrice, maxPrice, nameSearch, descriptionSearch, sortByPrice, sortByRating, printWriter);
        }
    }
	
}
