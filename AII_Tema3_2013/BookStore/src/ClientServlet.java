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
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.DatabaseMetaData;

public class ClientServlet extends HttpServlet {
    final public static long    serialVersionUID = 10021002L;
    
    public String               selectedTable, selectedCollection, selectedDomain;
    public String               userDisplayName;
    public Double				minPrice, maxPrice;
    public String				nameSearch, descriptionSearch;
    public Boolean				sortByPrice, sortByRating;
    
    public ArrayList<Record>	shoppingCart;
 
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            DataBaseConnection.openConnection();
            selectedTable       = Constants.BOOKS_TABLE;
            selectedCollection  = Constants.ALL;
            selectedDomain      = Constants.ALL;
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
    
    public String getUserId(String userDisplayName) throws SQLException {
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
                
                
                // create shopping cart
                String s = request.getParameter(parameter);
                if (!s.isEmpty() && parameter.contains("exemplare")) {
                	int nrExemplare = Integer.parseInt(request.getParameter(parameter));
                	int indexProiect = Integer.parseInt(parameter.substring(parameter.indexOf('_') + 1));
                	System.out.println("index proiect: " + indexProiect);
                	
            		int exista = 0;
            		for (Record r : shoppingCart) {
                		if (Integer.parseInt(r.getAttribute()) == indexProiect) {
            				exista = 1;
            				int totalExemplare = nrExemplare + Integer.parseInt(r.getValue());
            				r.setValue(Integer.toString(totalExemplare));
            			}
                	}
                	
            		if (exista == 0) {
	            		Record r = new Record(Integer.toString(indexProiect), Integer.toString(nrExemplare));
	                	shoppingCart.add(r);
            		}
                	session.setAttribute(Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()), shoppingCart);
                }
                
                if (!s.isEmpty() && parameter.contains("delete")) {
                	String substr = parameter.substring(0,parameter.indexOf('.'));
                	int indexProiect = Integer.parseInt(substr.substring(substr.indexOf('_') + 1));
                	System.out.println("Index: " + indexProiect);
                	Iterator<Record> it = shoppingCart.iterator();
                	while (it.hasNext()) {
                		Record r = it.next();
                		if (r.getAttribute().equals(Integer.toString(indexProiect))) {
                			it.remove();
                			break;
                		}
                	}
                }
                
                
                // TO DO (exercise 11): add logic for canceling / completing command
                if (parameter.equals("golire")) {
                    shoppingCart.clear();
                    session.setAttribute(Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()), shoppingCart);
                } 
                
                if (parameter.equals("finalizare") && !shoppingCart.isEmpty()) {
                    String billNumber = Utilities.generateBillNumber();
                    String userid = getUserId(userDisplayName);
                    
                    //compute bill total value
                    Double total = 0.0;
                    for (Record r : shoppingCart) {
                    	int idproiect = Integer.parseInt(r.getAttribute());
                    	int nrexemplare = Integer.parseInt(r.getValue());
                    	String query = "SELECT pretProiect(idproiect) from proiecte where idproiect = " + idproiect;
                    	Double value = Double.parseDouble(DataBaseConnection.executeQuery(query, 1).get(0).get(0).toString());
                    	total += value;
                    }
                	
                	ArrayList<String> values = new ArrayList<>();
                	
                	String expression = "INSERT into facturi VALUES(DEFAULT,\"" +
                						billNumber + "\", (SELECT CURDATE()), " + total + ", \"emisa\", (SELECT ADDDATE((SELECT CURDATE()), 4)), \"" + userid + "\")";
                	DataBaseConnection.insertQuery(expression);
                	
                	//gaseste id-ul ultimei facturi adaugate
                	String query = "SELECT idfactura from facturi WHERE nrfactura = \"" + billNumber + "\"";
                	String idFactura = DataBaseConnection.executeQuery(query, 1).get(0).get(0).toString();
                	
                	
                	//adauga in detalii_factura
                	for (Record r : shoppingCart) {
                		values = new ArrayList<>();
                		values.add(r.getAttribute());
                		values.add(idFactura);
                		values.add(r.getValue());
                		
                		DataBaseConnection.insertValuesIntoTable("randfactura", null, values, true);
                	}
                	
                	shoppingCart.clear();
                    session.setAttribute(Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()), shoppingCart);
                } 
                
              //adauga vot in baza de date
                if (!s.isEmpty() && parameter.contains(Constants.VOTE.toLowerCase())) {
                	int indexProiect = Integer.parseInt(parameter.substring(parameter.indexOf('_') + 1));
                	String vote = request.getParameter(parameter);
                	System.out.println("Vote: " + vote);
                	String query = "SELECT rating, nrvoturi from proiecte where idproiect = " + indexProiect;
                	ArrayList<Object> DBvalues = DataBaseConnection.executeQuery(query, 2).get(0);
                	int rating = Integer.parseInt(DBvalues.get(0).toString());
                	int nrvoturi = Integer.parseInt(DBvalues.get(1).toString());
                	
                	ArrayList<String> attributes = new ArrayList<>();
                	attributes.add("rating");
                	attributes.add("nrvoturi");
                	
                	ArrayList<String> values = new ArrayList<>();
                	rating += Integer.parseInt(vote);
                	nrvoturi++;
                	values.add(Integer.toString(rating));
                	values.add(Integer.toString(nrvoturi));
                	
                	String whereClause = "idproiect = " + indexProiect;
                	DataBaseConnection.updateRecordsIntoTable("proiecte", attributes, values, whereClause);
                }
                
                if (!s.isEmpty() && parameter.contains("raporteaza")) {
                	int indexProiect = Integer.parseInt(parameter.substring(parameter.indexOf('_') + 1));
                	RequestDispatcher requestDispatcher = null;
                	requestDispatcher = getServletContext().getRequestDispatcher("/FeedbackServlet");
                	session.setAttribute("id_proiect_raportat", indexProiect);
                	if (requestDispatcher != null) {
                		requestDispatcher.forward(request, response);
                	}
                }
                
                // TO DO (exercise 12): add logic for client logout
                if (parameter.equals("logout")) {
                	RequestDispatcher requestDispatcher = null;
                	requestDispatcher = getServletContext().getRequestDispatcher("/VisitorServlet");
                	if (requestDispatcher != null) {
                		requestDispatcher.forward(request, response);
                	}
                }
                
            }
            response.setContentType("text/html");
            ClientGraphicUserInterface.displayClientGraphicUserInterface(userDisplayName,errorMessage, minPrice, maxPrice, nameSearch, descriptionSearch, sortByPrice, sortByRating, shoppingCart,printWriter);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        userDisplayName = session.getAttribute(Constants.IDENTIFIER).toString();
        shoppingCart = (ArrayList<Record>)session.getAttribute(Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()));

        response.setContentType("text/html");
        try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
            ClientGraphicUserInterface.displayClientGraphicUserInterface(userDisplayName, null, minPrice, maxPrice, nameSearch, descriptionSearch, sortByPrice, sortByRating, shoppingCart,printWriter);
        }
    }     	 
}
