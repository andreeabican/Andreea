import general.Constants;
import graphicuserinterface.ClientGraphicUserInterface;
import graphicuserinterface.TechnicalReplyGraphicUserInterface;

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


public class TechnicalReplyServlet extends HttpServlet {

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

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		userDisplayName = session.getAttribute(Constants.IDENTIFIER).toString();
		Enumeration parameters = request.getParameterNames();
		String signupError = null;
		boolean ok = false;
		while(parameters.hasMoreElements()) {
			String parameter = (String)parameters.nextElement();
			if (parameter.contains(Constants.PROBLEMS_BUTTON_NAME.toLowerCase())) {	}
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

			TechnicalReplyGraphicUserInterface.displayTechnicalReplyGraphicUserInterface(userDisplayName, signupError, printWriter);
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		userDisplayName = session.getAttribute(Constants.IDENTIFIER).toString();
		response.setContentType("text/html");
		try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
			TechnicalReplyGraphicUserInterface.displayTechnicalReplyGraphicUserInterface(userDisplayName, null, printWriter);
		}
	}
}
