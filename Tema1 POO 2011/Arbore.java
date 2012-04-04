
public class Arbore {
	int n;
	String c;
	Arbore s, d, p;
	/**
	 * 
	 * @param k - reprezinta numarul de caractere care s-au adunat pana la nodul curent, in total, incluzand caracterele care se repeta
	 * @param str - reprezinta un sir de caractere la care se concateneaza fiecare caracter care trimite catre acest nod
	 */
	Arbore(int k, String str) {
		n = k;
		c = str;
		s = null;
		d = null;
		p = null;
	}
	/**
	 * 
	 * @param k - reprezinta numarul de caractere care s-au adunat in total pana la nodul curent
	 * @param str - reprezinta un sir de caractere la care se concateneaza fiecare caracter care trimite catre acest nod
	 * @param stang - legatura catre arborele stang 
	 * @param drept - legatura catre arborele drept
	 * @param parinte - lagatura catre parinte
	 */
	Arbore(int k, String str, Arbore stang, Arbore drept, Arbore parinte) {
		n = k;
		c = str;
		s = stang;
		d = drept;
		p = parinte;
	}

	Arbore() {
		n = 0;
		c = "";
		s = null;
		d = null;
		p = null;
	}
	
}
