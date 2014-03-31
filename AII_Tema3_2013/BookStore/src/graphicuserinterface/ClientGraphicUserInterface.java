package graphicuserinterface;

import dataaccess.DataBaseConnection;
import entities.Record;
import general.Constants;
import general.Utilities;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientGraphicUserInterface {

	public ClientGraphicUserInterface() { }

	/*public static ArrayList<String> getDistinctContent(String tableName, String attribute) {
		ArrayList<String> result = new ArrayList<>();
		result.add(Constants.ALL);
		try {
			ArrayList<String> attributes = new ArrayList<>();
			attributes.add("DISTINCT("+attribute+")");
			ArrayList<ArrayList<Object>> tableContent = DataBaseConnection.getTableContent(tableName,attributes,null,null,null);
			for (ArrayList<Object> tableRow:tableContent)
				result.add(tableRow.get(0).toString());
		} catch (SQLException exception) {
			System.out.println("exceptie: "+exception.getMessage());
			if (Constants.DEBUG)
				exception.printStackTrace();
		}
		return result;
	}*/
	
	//gaseste id-ul utilizatorului logat
	public static String getUserId(String userDisplayName) throws SQLException {
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
	
	//verifica daca utilizatorul logat a cumparat acest proiect - adica daca exista in una dintre facturi
	public static boolean hasBought(int idproiect, String userDisplayName) {
		String userid = null;
		try {
			userid = getUserId(userDisplayName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String query = "select f.idutilizator, rf.idproiect "
						+ "from facturi f, randfactura rf "
						+ "where "
						+ "f.idutilizator = " + userid + " and "
						+ "idproiect = " + idproiect + " and "
						+ "f.statut = 'achitata'";
		try {
			if (DataBaseConnection.executeQuery(query, 2).size() > 0)
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static void displayClientGraphicUserInterface(String userDisplayName, String errorMessage, Double minPrice,
														Double maxPrice, String nameSearch, String descriptionSearch, 
														Boolean sortByPrice, Boolean sortByRating,
														ArrayList<Record>	shoppingCart, PrintWriter printWriter) {
		String content = new String();
		content += "<html>\n";
		content += "<head>\n";
		content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" /><title>Magazin Virtual</title>\n";
		content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/bookstore.css\" />\n";  
		content += "</head>\n";       
		content += "<body>\n";
		content += "<form name=\"formular\" action=\"ClientServlet\" method=\"POST\">\n";
		content += "<a href = \"PersonalServlet\">" + Constants.WELCOME_MESSAGE+userDisplayName+"</a><br/>\n";
		// TO DO (exercise 12): add control for client logout
		content += "<input type = \"submit\" value=\"Logout\" name=\"logout\">";
		content += "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
				+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
				+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";
		
		content += "<input type = \"text\" placeholder = \"Cauta dupa denumire...\" name = \"cautaredenumire\">\n"; 
		content += "<input type = \"submit\" value=\"Cauta dupa denumire!\" name=\"cautaredupadenumire\">\n";
		
		content += "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
				+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";
		
		content += "<input type = \"text\" placeholder = \"Cauta dupa descriere...\" name = \"cautaredescriere\">\n"; 
		content += "<input type = \"submit\" value=\"Cauta dupa descriere\" name=\"cautaredupadescriere\">\n";
		content += "<center>\n";
		content += "<h2>Magazin Virtual - Achizitie Programe</h2>\n";
		content += "<table  bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\"><tbody>\n";
		content += "<tr>\n";
		content += "<td align=\"left\" valign=\"top\">\n";
		content += "<table  bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\"><tbody>\n";
		content += "<tr>\n";
		content += "<tr> Filtreaza dupa pret:  </tr>\n";
		content += "<tr>\n";
		content += "<td>"+Constants.MIN_PRICE+"</td>\n";
		content += "<td>\n";
		//content += "<select name=\"minPrice\" onchange=\"document.formular.submit()\">\n";
		content += "<input type = \"text\" placeholder = \"pret minim\" name = \"minPrice\">\n";       
		content += "</td>\n";
		content += "</tr>\n";
		//content += "<tr><td colspan=\"2\">&nbsp</td></tr>\n";
		//content += "<tr>\n";
		content += "<td>"+Constants.MAX_PRICE+"</td>\n";
		content += "<td>\n";
		//content += "<select name=\"maxPrice\" onchange=\"document.formular.submit()\">\n";
		content += "<input type = \"text\" placeholder = \"pret maxim\" name = \"maxPrice\">\n";
		content += "</td>\n";
		content += "</tr>\n";
		content += "<tr>\n";
		content += "<td>\n";
		content += "<input type = \"submit\" value=\"Aplica Filtru\" name=\"filtrupret\">\n";
		content += "<td/>\n";
		content += "<tr/>\n";
		//add sort buttons
		content += "<tr>\n";
		content += "<td>\n";
		content += "Sorteaza produsele: \n";
		content += "</td>\n";
		content += "</tr>\n";
		content += "<tr>\n";
		content += "<td>\n";
		if (sortByPrice) {
			content += "<input type = \"submit\" style=\"background-color: #82B182\" value = \"Sortare dupa pret\" name = \"sortaredupapret\">\n";
		} else {
			content += "<input type = \"submit\" value = \"Sortare dupa pret\" name = \"sortaredupapret\">\n";
		}
		content += "</td>\n";
		content += "</tr>\n";
		content += "<tr>\n";
		content += "<td>\n";
		if (sortByRating) {
			content += "<input type = \"submit\" style=\"background-color: #82B182\" value = \"Sortare dupa rating\" name = \"sortareduparating\">\n";
		} else {
			content += "<input type = \"submit\" value = \"Sortare dupa rating\" name = \"sortareduparating\">\n";
		}
		content += "</td>\n";
		content += "</tr>\n";
		content += "</tbody></table>\n";
		content += "</td>\n";
		content += "<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n";
		content += "<td width=\"60%\" align=\"center\">\n";
		try {
			if (errorMessage != null)
				content += errorMessage + "<br/><br/>\n";
			String whereClause = new String();
			String orderByClause = new String("ORDER BY");
			
			if (minPrice != null) {
				whereClause += " AND pretProiect(p.idproiect) >= " + minPrice;
			}
			if (maxPrice != null) {
				whereClause += " AND pretProiect(p.idproiect) <= " + maxPrice;
			}
			if (nameSearch != null) {
				whereClause += " AND p.denumire LIKE '%" + nameSearch + "%'";
			} else {
				if (descriptionSearch != null) {
					whereClause += " AND p.descriere LIKE '%" + descriptionSearch + "%'";
				}
			}
			
			if (sortByPrice) {
				orderByClause = " ORDER BY pretProiect(p.idproiect) ASC";
			} 
			else if (sortByRating) {
				orderByClause = " ORDER BY ratingProiect(p.idproiect) DESC";
			}
			else {
				orderByClause = new String();
			}
			// display finished projects
			String query = "select p.denumire, p.descriere, pretProiect(p.idproiect), p.idproiect, p.rating, p.nrvoturi, ratingProiect(p.idproiect) "
					+ "from proiecte p where "
					+ "dataSfarsitProiect(p.idproiect) < DATE(NOW())" + whereClause + orderByClause;
			System.out.println("QUERY: " + query);
			int numberOfColumns = 7;
			ArrayList<ArrayList<Object>> tableContent = DataBaseConnection.executeQuery(query, numberOfColumns);
			content += "<table  bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\"><tbody>\n";
			for (ArrayList<Object> tableRow:tableContent) {
				//String currentPrimaryKey = tableRow.get(primayKeyIndex).toString();
				content += "<tr>\n";
				content += "<td valign=\"top\"><img src=\"images/product.jpg\"/></td>\n";
				content += "<td>&nbsp</td>\n";
				content += "<td bgcolor=\"#ebebeb\">\n";
				//content += "autor(i): "+getBookAuthors(currentPrimaryKey)+"<br/>\n";
				//int currentIndex = 0;
				content += "Denumire: " + tableRow.get(0) + "<br/>\n";
				content += "Descriere: " + tableRow.get(1) + "<br/>\n";
				content += "Rating: " + tableRow.get(6) + "<br/>\n";
				content += "Numar voturi: " + tableRow.get(5) + "<br/>\n";
				content += "Pret: " + tableRow.get(2) + "<br/>\n";
				
				content += Constants.COPIES+": <input type=\"text\" name=\""+Constants.COPIES.toLowerCase()+"_"+tableRow.get(3)+"\" size=\"3\"/>\n";
				content += "<input type=\"submit\" name=\""+Constants.ADD_BUTTON_NAME.toLowerCase()+"_"+tableRow.get(3)+"\" value=\""+Constants.ADD_BUTTON_NAME+"\"/><br/>\n";
				if (hasBought(Integer.parseInt(tableRow.get(3).toString()), userDisplayName)) {
					content += Constants.VOTE+": <input type=\"text\" name=\""+Constants.VOTE.toLowerCase()+"_"+tableRow.get(3)+"\" size=\"3\"/>\n";
					content += "<input type=\"submit\" name=\""+Constants.VOTE_BUTTON_NAME.toLowerCase()+"_"+tableRow.get(3)+"\" value=\""+Constants.VOTE_BUTTON_NAME+"\"/>(minim 0 maxim 5)<br/>\n";
					content += "<input type=\"submit\" name=\""+Constants.PROBLEMS_BUTTON_NAME.toLowerCase()+"_"+tableRow.get(3)+"\" value=\""+Constants.PROBLEMS_BUTTON_NAME+"\"/><br/>\n";
				}
				content += "</td>\n";
				content += "</tr><tr><td colspan=\"3\">&nbsp;</td></tr>\n";
			}
			content += "</tbody></table>\n";
			
			// future projects
			content += "<center>\n";
			content += "<h2>Lansari viitoare...</h2>\n";
			query = "select p.denumire, p.descriere, pretProiect(p.idproiect), p.idproiect "
					+ "from proiecte p where "
					+ "dataSfarsitProiect(p.idproiect) >= DATE(NOW())" + whereClause + orderByClause;
			System.out.println("QUERY: " + query);
			numberOfColumns = 4;
			tableContent = DataBaseConnection.executeQuery(query, numberOfColumns);
			content += "<table  bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\"><tbody>\n";
			for (ArrayList<Object> tableRow:tableContent) {
				//String currentPrimaryKey = tableRow.get(primayKeyIndex).toString();
				content += "<tr>\n";
				content += "<td valign=\"top\"><img src=\"images/comingsoon.jpg\"/></td>\n";
				content += "<td>&nbsp</td>\n";
				content += "<td bgcolor=\"#ebebeb\">\n";
				//content += "autor(i): "+getBookAuthors(currentPrimaryKey)+"<br/>\n";
				//int currentIndex = 0;
				content += "Denumire: " + tableRow.get(0) + "<br/>\n";
				content += "Descriere: " + tableRow.get(1) + "<br/>\n";
				content += "Pret: " + tableRow.get(2) + "<br/>\n";

				//content += Constants.COPIES+": <input type=\"text\" name=\""+Constants.COPIES.toLowerCase()+"_"+tableRow.get(3)+"\" size=\"3\"/><br/>\n";
				//content += "<input type=\"submit\" name=\""+Constants.ADD_BUTTON_NAME.toLowerCase()+"_"+tableRow.get(3)+"\" value=\""+Constants.ADD_BUTTON_NAME+"\"/><br/>\n";
				content += "</td>\n";
				content += "</tr><tr><td colspan=\"3\">&nbsp;</td></tr>\n";
			}

			content += "</tbody></table>\n";
			
			content += "</td>\n";
			content += "<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n";
			content += "<td align=\"left\" valign=\"top\">\n";
			content += "<table><tr><td><img src=\"images/shoppingcart.png\"/></td><td valign=\"center\">"+Constants.SHOPPING_CART+"</td></tr></table><br/>\n";
			if (shoppingCart != null && !shoppingCart.isEmpty()) {
				Double total = 0.0;
				// TO DO (exercise 9): get shopping cart content and display it
				for (Record r : shoppingCart) {
					query = "Select p.denumire, pretProiect(p.idproiect) from proiecte p where p.idproiect = " + r.getAttribute();
					ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 2);
					System.out.println("Value:" + r.getValue() + " Att: " + r.getAttribute());
					ArrayList<Object> value = result.get(0);
					
					content +=  "<input type=\"image\" src=\"images/deleteItem.png\" name=\"delete_" + r.getAttribute() + "\" width=\"16\" height=\"16\">" +
								r.getValue() + " x " + value.get(0) + "=" +
								Integer.parseInt(r.getValue()) * Double.parseDouble(value.get(1).toString()) + "<br>";  
					total += Integer.parseInt(r.getValue()) * Double.parseDouble(value.get(1).toString());
				}
				content += "Total: " + total + "<br>";
				
				// TO DO (exercise 10): add controls for finalizing and canceling command
				content += "<br><input type = \"submit\" value = \"Golire cos\" name = \"golire\" >";
				content += "<input type = \"submit\" value = \"Finalizare comanda\" name = \"finalizare\" >";

			} else {
				content += Constants.EMPTY_CART+"<br/>\n";
			} 
			content += "</td>\n";
			content += "</tr>\n";
			content += "</tbody></table>\n";
		} catch (SQLException exception) {
			System.out.println ("exceptie: "+exception.getMessage());
			if (Constants.DEBUG)
				exception.printStackTrace();
		}       
		content += "</form>\n";
		content += "</center>\n";
		content += "</body>\n";
		content += "</html>";  
		//System.out.println(content);
		printWriter.println(content);
	}

	public static void displayClientGraphicUserInterface(
			String userDisplayName, String errorMessage, Object min,
			PrintWriter printWriter) {
		// TODO Auto-generated method stub
		
	}    
}
