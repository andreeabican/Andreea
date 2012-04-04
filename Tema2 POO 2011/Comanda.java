import java.util.*;
/**
 * Contine functii care prelucreaza calea catre fisier/director 
 * @author Simona Andreea Badoiu
 *
 */
public class Comanda {
	//Se retin toate tipurile posibile de fisiere
	/**
	 * 
	 * @return penultimul fisier/director care este specificat in cale
	 * @param a - radacina arborelui SGF
	 * @param cale - calea catre fisier/director
	 */
	public AFisier fisDest(Director a, String cale) {
		// Daca nu s-a specificat corect cale, adica aceasta nu incepe cu "/", se intoarce null
		//if (cale.equals("/")) return a;
		if (cale.indexOf("/") != 0) return null;
		String[] s = cale.split("/");
		int i=1;
		//Se merge pana la penultimul element din cale
		//Stim ca in afara de ultimul element din cale, toate celelalte trebuie sa fie directoare
		//Avansam urmand calea, atata timp cat directoarele exsita... in caz contrar se intoarce null
		while (a.content.c.containsKey(s[i])) {
			AFisier fis = a.content.c.get(s[i]);
			if (i<s.length-1 && fis.type == "") {
				a = (Director)fis;
				i++;
				}
			else break;
		}
		if (i==s.length-1)
			return a;	
		return null;
	}
	/**
	 * 
	 * @param s - comanda citita de la tastatura
	 * @return - comanda care trebuie executata
	 */
	public String actiune(String s) {
		String[] str = s.split(" ");
		if (str.length>1)
			return str[1];
		return null;
	}
	/**
	 * 
	 * @param s - comanda citita de la tastatura
	 * @return - userul care a dat comanda
	 */
	public String user(String s) {
		String[] str = s.split(" ");
		if (str.length>0)
			return str[0];
		return null;
	}
	/**
	 * 
	 * @param s - comanda citita de la tastatura
	 * @return calea catre fisier/director
	 */
	public String cale(String s) {
		String[] str = s.split(" ");
		if (str.length > 2)
			return str[2];
		return null;
	}
	/**
	 * 
	 * @param s - calea catre fisier/director
	 * @return - ultimul element din calea catre fisier/director
	 */
	public String ultim(String s) {
		if (s.lastIndexOf("/") >=0) {
			String str = s.substring(s.lastIndexOf("/")+1, s.length());
			return str;
		}
		return null;
	}
	/**
	 * 
	 * 
	 * @param s - sirul de caractere citit de la tastatura
	 * @return tipul fisierului care trebuie sa fie creat sau null
	 */
	public String type(String s) {
		String[] str = s.split(" ");
		if (str.length > 3)
			return str[3];
		return null;
	}
	/**
	 * 
	 * @param s - sirul de caractere citit de la tastatura
	 * @return sirul de caractere care reprezinta continutul noului fisiser sau null
	 */
	public String continut(String s) {
		// daca exista caracterul " de cel putin doua ori, tot ce se afla intre aceste ghilimele va fi considerat
		//continut al fisierului nou creat
		if ((s.lastIndexOf("\"") >=0 && s.lastIndexOf("\"") == s.length()-1 && s.indexOf("\"") != s.lastIndexOf("\""))) {
			s = s.substring(0, s.length()-1);
			String str = s.substring(s.lastIndexOf("\"") + 1, s.length());
			return str;
		}
			return null;
		}
	/**
	 * 
	 * @param f - fisierul de la care incep sa se aplice schimbarile de grup - in cazul nostru este mereu root
	 * @param user - utilizatorul care detine grupul
	 * @param grup - noul grup care trebuie atribuit variabilei owner_group
	 */
	public void schimbaGrup(AFisier f, String user, String grup) {
		// am folosit parcurgerea in adancime
		if (((Director)f).content.c.isEmpty()) 
			return;
		Map<String, AFisier> aux = ((Director)f).content.c;
		//pentru fiecare director din directorul curent, am avansat in contentul acestuia si i-am aplicat
		//modificarile in caz ca a fost nevoie
		for (Map.Entry<String, AFisier> entry : aux.entrySet()) {
			if (entry.getValue().type.equals("")) {
				f = entry.getValue();
				//System.out.println(f.name + " " + ((Director)f).content);
				if (f.owner_user.equals(user))
					f.owner_group = grup;
				schimbaGrup(f, user, grup);
			}
			// daca f nu era director, doar aplicam modificarile pentru grup(daca era nevoie)
			else {
				if (f.owner_user.equals(user))
					f.owner_group = grup;
			}
		}
		return;
	}
	// determinare vector grupuri pentru usermod
	/**
	 * 
	 * @param str - sirul de caractere citit de la tastatura
	 * @return un vector de String care contine toate grupurile din care vrem sa fie sters utilizatorul
	 */
	public String[] um(String str) {
		// am eliminat din sirul initial primele doua cuvinte(userul si comanda) si ultimul cuvant(utilizatorul
		// care dorim sa fie sters)
		String grupuri = str.substring(str.indexOf(" ")+1, str.length());
		grupuri = grupuri.substring(grupuri.indexOf(" ")+1, grupuri.length());
		return grupuri.substring(0, grupuri.lastIndexOf(" ")).split(" ");
		
	}
	/**
	 * 
	 * @param str - sirul de caractere citit de la tastatura
	 * @return - calea catre fisierul asupra caruia vrem sa aplicam chmod sau null
	 */
	public String caleChmod(String str) {
		String[] s = str.split(" ");
		if (s.length >= 6) {
			return s[5];
		}
		return null;
	}
	/**
	 * 
	 * @param str - sirul de caractere citit de la tastatura
	 * @return proprietarul din cadrul comenzii chmod(u - user, a - all sau g - grup) sau null
	 */
	public String proprietar(String str) {
		String[] s = str.split(" ");
		if (s.length >= 3) {
			String p = s[2];
			if (p.equals("u") || p.equals("a") || p.equals("g"))
				return p;
		}
		return null;
	}
	/**
	 * 
	 * @param str - sirul de caractere citit de la tastatura
	 * @return operatorul din cadrul  comenzii chmod(+, -, sau =) sau null
	 */
	public String operator(String str) {
		String[] s = str.split(" ");
		if (s.length >= 4) {
			String c = s[3];
			//daca operatorul este unul din cei asteptati, il returnam
			if (c.equals("+") || c.equals("-") || c.equals("=")) {
				return c;
			}
		}
		return null;
	}
	/**
	 * 
	 * @param str - sirul de caractere citit de la tastatura
	 * @return sirul de caractere care constituie permisiunile care vor fi modificate
	 */
	public String permisiuni(String str) {
		String[] s = str.split(" ");
		ArrayList<Integer> v = new ArrayList<Integer>();
		if (s.length >= 5) {
			String p = s[4];
			if (p.equals("")) return p;
			if (p.length() > 3) return null; // daca avem mai mult de 3 caractere inseamna ca cel putin unul se
											// repeta sau mai exista si alte caractere decat cele asteptate;
			for (int i = 0; i < p.length(); i++) {
				if (s[i].equals("r")) v.set(0, v.get(0)+1);
				else
					if (s[i].equals("w")) v.set(1, v.get(1)+1);
					else
						if (s[i].equals("x")) v.set(2, v.get(2)+1);
			}
			// daca in sirul de caractere care contine permisiunile se repeta cel putin un caracter se intoarce null
			for (int j = 0; j < v.size(); j++) {
				if (v.get(j) > 1) return null;
			}
		return p;
		}
		return null;
	}
}
