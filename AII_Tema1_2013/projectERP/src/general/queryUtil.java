package general;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import dataaccess.DataBaseConnection;

public class queryUtil {
	
	/* seteaza in fiecare label numarul corespunzator de zile de concediu */
	public static void computeZileConcediu(int iduser, Label concediuOdihna,
								Label concediuMedical, 
								Label concediuFaraPlata) throws SQLException {
		String query;
		ArrayList<ArrayList<Object>> result;
		ArrayList<Object> values;
		// cate zile de concediu de odihna i-au mai ramas
		query = "SELECT zileconcediuramase FROM utilizatori WHERE idutilizator = " + iduser;
		result = DataBaseConnection.executeQuery(query, 1);
		if (result != null && (values = result.get(0)) != null && values.get(0) != null) {
			concediuOdihna.setText((String) values.get(0));
		} else {
			concediuOdihna.setText("0");
		}

		// cate zile de concediu medical si-a luat in ultimele 12 luni
		query = "SELECT sum(durata) FROM concedii where idutilizator =" +
				"\'" + iduser + "\'" + " AND " +
				" DATE(datainceput) < CURRENT_DATE AND" +
				" DATE(datainceput) > (CURRENT_DATE - INTERVAL 12 MONTH) AND " +
				"tip =  + \'medical\'";
		result = DataBaseConnection.executeQuery(query, 1);
		if (result != null) {
			values = result.get(0);
			System.out.println(values);
			if (values != null && values.get(0) != null) {
				concediuMedical.setText((String) values.get(0));
			} else {
				concediuMedical.setText("0");
			}
		}

		// cate zile de concediu neplatit sau din motive speciale si-a luat in ultimul an
		query = "SELECT sum(durata) FROM concedii where" +
				" idutilizator =" + "\'" + iduser + "\'" +
				" AND YEAR(CURRENT_DATE) = YEAR(datainceput) AND" +
				" (tip = \'fara plata\' OR tip = \'motive speciale\')";
		result = DataBaseConnection.executeQuery(query, 1);
		if (result != null) {
			values = result.get(0);
			if (values!= null && values.get(0) != null) {
				concediuFaraPlata.setText((String)values.get(0));
			} else {
				concediuFaraPlata.setText("0");
			}
		}
	}
	
	/* 
	 * creeaza un query care intoarce toate intrarile din activitate zilnica pentru perioada cuprinsa
	 * intre datastart si datasfarsit, corespunzatoare utilizatorului iduser
	 */
	public static String createActivityQuery(int iduser, TextField campDataStart, TextField campDataSfarsit) {
		String query = "select " +
				"TIME(A.orasosire)," +
				"TIME(A.oraplecare), " +
				"time_to_sec(timediff(TIME(orasosire), '08:00:00'))/3600 as intarziere, " +
				"time_to_sec(timediff(TIME(oraplecare), '17:00:00'))/3600 as dupaprogram, " +
				"(time_to_sec(timediff(oraplecare, orasosire))/3600 - 9) as oresuplimentare  " +
				"from activitatezilnica A where A.idutilizator = " + 
				"\'" + iduser + "\'"; 
		
		if (!campDataStart.getText().isEmpty() && !campDataSfarsit.getText().isEmpty()) {
			query += " AND DATE(A.orasosire) between " + 
						"\'" + campDataStart.getText() + "\'" + 
						" and " +
						"\'" + campDataSfarsit.getText() + "\'";
		} else {
			if (!campDataStart.getText().isEmpty()) {
				query += " AND DATE(A.orasosire) > " +
							"\'" + campDataStart.getText() + "\'"; 
			} else {
				if (!campDataSfarsit.getText().isEmpty()) {
					query += " AND DATE(A.orasosire) < " +
							"\'" + campDataStart.getText() + "\'"; 
				}
			}
		}
		return query;
	}
	
	/*
	 * intoarce in fiecare label numarul de ore efectuate si numarul de ore suplimentare
	 * numarul de ore suplimentare se calculeaza scazand din numarul de ore lucrate, numarul de ore
	 * corespunazator numarului de zile lucratoare din intervalul datastart - datasfarsit
	 */
	public static void fillActivityLabels(int iduser, TextField campDataStart, TextField campDataSfarsit,
											Label totalOre, Label totalOreSuplimentare, Label activityError) {
		String query = "select " +
				"sum(9), " +
				"sum(time_to_sec(timediff(TIME(orasosire), '08:00:00'))/3600) as intarziere, " +
				"sum(time_to_sec(timediff(TIME(oraplecare), '17:00:00'))/3600) as dupaprogram, " +
				"sum(time_to_sec(timediff(oraplecare, orasosire))/3600 - 9) as oresuplimentare  " +
				"from activitatezilnica A where A.idutilizator = " + 
				"\'" + iduser + "\'"; 
		
		if (!campDataStart.getText().isEmpty() && !campDataSfarsit.getText().isEmpty()) {
			query += " AND DATE(A.orasosire) between " + 
						"\'" + campDataStart.getText() + "\'" + 
						" and " +
						"\'" + campDataSfarsit.getText() + "\'";
		} else {
			if (!campDataStart.getText().isEmpty()) {
				query += " AND DATE(A.orasosire) > " +
							"\'" + campDataStart.getText() + "\'"; 
			} else {
				if (!campDataSfarsit.getText().isEmpty()) {
					query += " AND DATE(A.orasosire) < " +
							"\'" + campDataStart.getText() + "\'"; 
				}
			}
		}
		try {
			ArrayList<Object> value;
			ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 4);
			System.out.println(result.get(0));
			if (result != null && (value = result.get(0)) != null) {
				System.out.println(value.get(0) + " " + value.get(1) + " " + value.get(2) + " " + value.get(3));
				double total = ((value.get(0) == null)?0:Double.parseDouble((String)value.get(0))) - 
							((value.get(1) == null)?0:Double.parseDouble((String)value.get(1))) +
							((value.get(2) == null)?0:Double.parseDouble((String)value.get(2)));
				totalOre.setText(Double.toString(total));
				totalOreSuplimentare.setText(Double.toString((value.get(3) == null)?0:Double.parseDouble((String)value.get(3))));
				int zileLucratoare = numarZileLucratoare(iduser, campDataStart, campDataSfarsit);
				if (total < zileLucratoare*9) {
					activityError.setText("Nu s-a indeplinit numarul de ore!!!");
				} else {
					activityError.setText("");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*public static String dataAngajarii(int userid) {
		String query = "SELECT dataangajarii FROM utilizatori WHERE" +
						"idutilizator = " + userid;
		try {
			ArrayList<Object> value;
			ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 1);
			if (result != null && (value = result.get(0)) != null && value.get(0) != null) {
				return value.get(0).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}*/
	
	/*
	 * gaseste prima zi de activitate cuprinsa in intervalul de cautare
	 */
	public static String firstDate(int userid, TextField datastart) {
		String query;
		if (datastart.getText().isEmpty()) {
			query = "SELECT DATE(orasosire) FROM activitatezilnica WHERE " +
					"idutilizator = " + userid + " LIMIT 1";
		} else {
			query = "SELECT DATE(orasosire) FROM activitatezilnica WHERE " +
							"idutilizator = " + userid + " AND " +
							"DATE(orasosire) >= DATE(\'" + datastart.getText() + "\') LIMIT 1";
		}
		try {
			ArrayList<Object> value;
			ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 1);
			if (result != null && (value = result.get(0)) != null && value.get(0) != null) {
				return value.get(0).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//returneaza numarul de zile lucratoare din intervalul datastart - datasfarsit
	public static int numarZileLucratoare(int userid, TextField datastart, TextField datasfarsit) throws SQLException {
		String query;
		String startdate = firstDate(userid, datastart);
		if (startdate == null)
			return -1;
		if (datasfarsit.getText().isEmpty()) {
			query = "SELECT zileLucratoareInterval(\'" + startdate + "\', CURRENT_DATE)";
		} else {
			query = "SELECT zileLucratoareInterval(\'" + datastart.getText() + "\', " +
															"\'" + datasfarsit.getText() + "\')";
		}
		ArrayList<ArrayList<Object>> result = DataBaseConnection.executeQuery(query, 1);
		System.out.println("Afiseaza aici: " + result.get(0));
		return Integer.parseInt(result.get(0).get(0).toString());
	}
	
	//returneaza id-ul echipei care se ocupa de proiectul primit ca parametru de intrare
	public static int idEchipa(int idProiect) {
		String query = "SELECT idechipa from echipe WHERE idproiect = "  + "\'" + idProiect + "\'";
		ArrayList<ArrayList<Object>> result = null;
		try {
			result = DataBaseConnection.executeQuery(query, 1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Object> values;
		if (result != null && !result.isEmpty() && (values = result.get(0)) != null && !values.isEmpty()) {
			return Integer.parseInt(values.get(0).toString());
		}
		return -1;
	}
	
	//returneaza id-ul proiectului care are acest nume
		public static int idProiect(String numeproiect) {
			String query = "SELECT idproiect from proiecte WHERE denumire = "  + "\'" + numeproiect + "\'";
			ArrayList<ArrayList<Object>> result = null;
			try {
				result = DataBaseConnection.executeQuery(query, 1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ArrayList<Object> values;
			if (result != null && !result.isEmpty() && (values = result.get(0)) != null && !values.isEmpty()) {
				return Integer.parseInt(values.get(0).toString());
			}
			return -1;
		}
		
		/* returneaza id-ul versiuni cu acest nume si care este atribuita proiectului cu id ul primit ca parametru */
		public static int idVersiune(String numeversiune, int idproiect) {
			String query = "SELECT idversiune from versiuniproiecte WHERE denumire = "  + "\'" + numeversiune + "\' " +
					"AND idproiect = " + idproiect;
			ArrayList<ArrayList<Object>> result = null;
			try {
				result = DataBaseConnection.executeQuery(query, 1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ArrayList<Object> values;
			if (result != null && !result.isEmpty() && (values = result.get(0)) != null && !values.isEmpty()) {
				return Integer.parseInt(values.get(0).toString());
			}
			return -1;
		}
		
	public static String tipUtilizator(int userid) {
		String query = "SELECT tip from utilizatori WHERE idutilizator = "  + userid;
		ArrayList<ArrayList<Object>> result = null;
		try {
			result = DataBaseConnection.executeQuery(query, 1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Object> values;
		if (result != null && !result.isEmpty() && (values = result.get(0)) != null && !values.isEmpty()) {
			return values.get(0).toString();
		}
		return "unknown";
	}
	
	/* id-ul utilizatorului care are numele primit ca parametru */
	public static int idUtilizator(String numeUtilizator) {
		String[] numeprenume = numeUtilizator.split(" ");
		String nume  = numeprenume[0];
		String prenume = "";
		for (int i = 1; i < numeprenume.length; i++) {
			prenume += numeprenume[i] + " ";
		}
		prenume = prenume.substring(0, prenume.length() - 1);
		String query = "SELECT idutilizator from utilizatori WHERE " +
				"nume = "  + "\'" + nume + "\' AND " +
				"prenume = \'" + prenume + "\'";
		ArrayList<ArrayList<Object>> result = null;
		try {
			result = DataBaseConnection.executeQuery(query, 1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Object> values;
		if (result != null && !result.isEmpty() && (values = result.get(0)) != null && !values.isEmpty()) {
			return Integer.parseInt(values.get(0).toString());
		}
		return -1;
	}
	
}
