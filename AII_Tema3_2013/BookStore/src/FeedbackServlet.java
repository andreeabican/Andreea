import general.Constants;
import graphicuserinterface.ClientGraphicUserInterface;
import graphicuserinterface.FeedbackGraphicUserInterface;
import graphicuserinterface.SignUpGraphicUserInterface;

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


public class FeedbackServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String               userDisplayName;

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

	public int finBillid(String userDisplayName, int projectid) throws SQLException {
		String userid = ClientGraphicUserInterface.getUserId(userDisplayName);
		String query = "select f.idfactura from "
				+ "facturi f, randfactura rf "
				+ "where"
				+ " f.idfactura = rf.idfactura and"
				+ " f.idutilizator = "+ userid +" and"
				+ " rf.idproiect = " + projectid;
		ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 1);
		if (!result.isEmpty()) {
			return Integer.parseInt(result.get(0).get(0).toString());
		}
		return -1;
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		int projectid = Integer.parseInt(session.getAttribute("id_proiect_raportat").toString());
		userDisplayName = session.getAttribute(Constants.IDENTIFIER).toString();
		Enumeration parameters = request.getParameterNames();
		String signupError = null;
		boolean ok = false;
		while(parameters.hasMoreElements()) {
			String parameter = (String)parameters.nextElement();
			System.out.println(parameter);
			if (parameter.contains(Constants.PROBLEMS_BUTTON_NAME.toLowerCase())) {
				System.out.println("Intra pe aici...");
				ok = true;
				//get all the input from the user
				//if one of the fields is not filled display an error
				ArrayList<String> values = new ArrayList<>();
				values.add(request.getParameter(Constants.MESSAGE_SUBJECT));
				values.add(request.getParameter("mesaj"));
				values.add("consilier_tehnic");
				values.add(userDisplayName);
				
				values.add("CURDATE()");
				values.add("necitit");
				values.add(Integer.toString(projectid));
				//gaseste id-ul primei facturi in care apare acest produs
				int idfactura = -1;
				try {
					idfactura = finBillid(userDisplayName, projectid);
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				values.add(Integer.toString(idfactura));
				System.out.println(values);
				for (String att : values) {
					if (att == null || att.isEmpty()) {
						ok = false;
						signupError = "Trebuie sa completati toate campurile";
						break;
					}
				}

				if (ok) {
					try {
						DataBaseConnection.insertValuesIntoTable("mesaje", null, values, true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		response.setContentType("text/html");
		try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
			userDisplayName = session.getAttribute(Constants.IDENTIFIER).toString();
			RequestDispatcher requestDispatcher = null;
			if (ok)
				requestDispatcher = getServletContext().getRequestDispatcher("/VisitorServlet");

			if (requestDispatcher!=null) {
				requestDispatcher.forward(request,response);
			}

			FeedbackGraphicUserInterface.displayFeedbackGraphicUserInterface(userDisplayName, signupError, projectid, printWriter);
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		int projectid = Integer.parseInt(session.getAttribute("id_proiect_raportat").toString());
		userDisplayName = session.getAttribute(Constants.IDENTIFIER).toString();
		response.setContentType("text/html");
		try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
			FeedbackGraphicUserInterface.displayFeedbackGraphicUserInterface(userDisplayName, null, projectid, printWriter);
		}
	}  

}
