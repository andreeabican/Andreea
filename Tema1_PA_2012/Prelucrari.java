import java.util.ArrayList;
import java.util.Map;

/**
 * Aici am declarat functiile care aplica prelucrari pentru a corecta cuvintele si propozitiile
 * @author Simona Badoiu
 *
 */
public class Prelucrari {
	/**
	 * 
	 * @param s1 - sir de caractere
	 * @param s2 - sir de caractere
	 * @return distanta dintre s1 si s2 (cate modificari trebuie sa facem pentru a ajunge de la sirul 1 la sirul 2 - 
	 * modificari insemnand sterge, inlocuieste sau adauga un caracter
	 */
	public static int levenshtein(String s1, String s2) {
		int m = s1.length()+1;
		int n = s2.length()+1;
		char[] str1 = s1.toCharArray();
		char[] str2 = s2.toCharArray();
		int[][] dist = new int[m][n];
		for (int i = 0; i < m; i++) {
			dist[i][0] = i;
		}
		for (int j = 0; j < n; j++) {
			dist[0][j] = j;
		}
		
		for (int j = 1; j < n; j++) {
			for (int i = 1; i < m; i++) {
				if (str1[i-1] == str2[j-1]) {
					dist[i][j] = dist[i-1][j-1];
				}
				else {
					int m1 = Math.min(dist[i-1][j] + 1, dist[i][j-1] + 1);
					int min = Math.min(m1, dist[i-1][j-1] + 1);
					dist[i][j] = min;
				}
			}
		}
		return dist[m-1][n-1];
	}
	/**
	 * Modifica cuvMin, daca gaseste un cuvant la distanta mai mica decat ultimul cuvant memorat cu un pas in urma
	 * @param a - arrayList in care am retinut cuvintele din dictionar cu lungimea l 
	 * @param c - cuvantul cu care comparam, pe care vrem sa il corectam
	 * @param distMin - distanta minima
	 * @param cuvMin - cuvantul minim = cea mai apropiata potrivire pe care am gasit-o pana in acest moment
	 * @return - noua distanta minima, daca s-a gasit alta
	 */
	public static int cautaCuv(ArrayList<Cuvant> a, String c, int distMin, Cuvant cuvMin) {
		String cuvant;
		int freq;
		int dist = 0;
		//cautam in toate cuvintele de lungimea sa, si retinem cea mai mica distanta si cuvantul fata de care a fost calculata
		for (int i = 0; i < a.size(); i++) {
			dist = levenshtein(a.get(i).value, c);
			cuvant = a.get(i).value;
			freq = a.get(i).freq;
			//daca am gasit o distanta mai mica, retinem distanta minima si cuvantul si frecventa acestuia
			if (dist < distMin) {
				distMin = dist;
				cuvMin.freq = freq;
				cuvMin.value = cuvant;
			}
			//daca distantele sunt egale, alegem cuvantul cu frecventa de aparitie cea mai mare
			else {
				if (dist == distMin) {
					if ((cuvMin.freq < 0) || (cuvMin.freq < freq)) {
						//cuvMin = new Cuvant(freq, cuvant);
						cuvMin.freq = freq;
						cuvMin.value = cuvant;
					}
					else {
						//daca si frecventele sunt egale, alegem primul cuvant, in ordine lexicografica
						if (cuvMin.freq == freq) {
							if ((cuvMin.value == null) || (cuvMin.value.compareTo(cuvant) > 0)) {
								//cuvMin = new Cuvant(freq, cuvant);
								cuvMin.freq = freq;
								cuvMin.value = cuvant;
							}
						}
					}
				}
			}
		}
		return distMin;
	}
	
	/**
	 * 
	 * @param map - este un Map in care avem toate cuvintele din dictionar. In value retinem elemente de tip Cuvant, care contin cuvinte cu
	 * lungimea egala cu valoarea din cheie
	 * @param c - cuvantul pe care urmeaza sa il corectam daca este nevoie
	 */
	public static Proprietati prelCuvant(Map<Integer, ArrayList<Cuvant>> map, String c) {
		int l = c.length();
		//cea mai mica distanta intre ce cuvant am primit si cuvantul cel mai apropiat pe care l-am gasit
		int distMin = l+1;
		//cuvantul pe care l-am gasit la acea distanta si frecventa acestuia
		Cuvant cuvMin = new Cuvant(0, null);
		boolean ok = false;
		// pozitie din map
		ArrayList<Cuvant> a;
		if (l > map.size()) {
			a = map.get(map.size()-1);
			l = map.size()-1;
		}
		else {
			if (l == 0) {
				return new Proprietati(new Cuvant(0, ""), 0, 0);
			}
			else {
				a = map.get(l);
			}	
		}
			
		//verificam daca avem un cuvant corect, care exista in dictionar il returnam
		if (map.containsKey(l)) {
			for (int i = 0; i < a.size(); i++) {
				if (a.get(i).value.equals(c)) {
					ok = true;
					distMin = 0;
					return new Proprietati(new Cuvant(a.get(i).freq, c), distMin, 1);
				}
			}
		}
		if (!ok) {
			//daca, cuvantul nu a fost scris corect, adica nu a fost gasit in map
			//il cautam in cuvintele cu lungimea cu 1 mai mica sau in cele cu lungimea mai mare cu maxim 2
			distMin = cautaCuv(a, c, distMin, cuvMin);
			int k = 0;
			int l1 = l;
			while (k < 2) {
				if (k==0)
					if (l-1 > 0) {
						a = map.get(l-1);
						distMin = cautaCuv(a, c, distMin, cuvMin);
					}
				if (l1+1 < map.size()) {
					a = map.get(l1+1);
					distMin = cautaCuv(a, c, distMin, cuvMin);
				}
				
				k++;
				l--;
				l1++;
			}
		}
		//daca nu s-a gasit cuvantul in cele de lungimea lui, atunci il afisam pe cel returnat de functia cautaCuv
		if (!ok) {
			return new Proprietati(cuvMin, distMin, 1);
		}
		return null;
	}
	
	public static String prelProp(Map<Integer, ArrayList<Cuvant>> map, String prop) {
		String s = minim(map, prop);
		return s;
	}
	/**
	 * 
	 * @param array - un vector de proprietati din care trebuie sa le alegem pe toate cu distanta cea mai mica, apoi dintre acestea, le
	 * alegem pe toate cu cel mai mic numar de cuvinte, dintre cele cu cel mai mic numar de cuvinte le alegem pe cele cu frecventa cea mai mare,
	 * iar dintre cele cu frecventa cea mai mare, il alegem pe cel mai mic in ordine lexicografica
	 * @return - element de tip Proprietati, care a rezultat dupa aplicarea conditiilor de mai sus
	 */
	public static Proprietati propFinal(ArrayList<Proprietati> array0) {
		Proprietati pf;
		ArrayList<Proprietati> array = new ArrayList<Proprietati>();
		if (array0.size() == 1) {
			pf = array0.get(0);
		}
		else {
			array.add(array0.get(0));
			for (int i = 1; i < array0.size(); i++) {
				Proprietati p = array0.get(i);
				if (p.distMin < array.get(0).distMin) {
					array.clear();
					array.add(p);
				}
				else {
					//daca gasim un element cu aceeasi distanta, il retinem si pe acela
					if (p.distMin == array.get(0).distMin) {
						array.add(p);
					}
				}
			}
			
		if (array.size() == 1) {
			pf = array.get(0);
		}
		else {
			//daca avem mai multe elemente cu aceeasi distanta, comparam numarul de cuvinte
			//retinem tot asa intr un vector toate elementele de tip Proprietati care au nr_cuv minim
			ArrayList<Proprietati> array1 = new ArrayList<Proprietati>();
			//adaugam mai intai primul element
			array1.add(array.get(0));
			for (int index = 1; index < array.size(); index++) {
				Proprietati prop = array.get(index);
				//daca gasim un element cu nr_cuv mai mic decat ce avem pana acum in vector, stergem tot ce aveam in vector
				//si adaugam noul element
				if (prop.nr_cuv < array1.get(0).nr_cuv) {
					array1.clear();
					array1.add(prop);
				}
				else {
					//daca au nr_cuv egal, adaugam si acest element in vector
					if (prop.nr_cuv == array1.get(0).nr_cuv) {
						array1.add(prop);
					}
				}
			}
			if (array1.size() == 1) {
				pf = array1.get(0);
			}
			else {
				ArrayList<Proprietati> array2 = new ArrayList<Proprietati>();
				array2.add(array1.get(0));
				for (int index = 1; index < array1.size(); index++) {
					Proprietati prop = array1.get(index);
					if (prop.cuvant.freq > array2.get(0).cuvant.freq) {
						array2.clear();
						array2.add(prop);
					}
					else {
						if (prop.cuvant.freq == array2.get(0).cuvant.freq) {
							array2.add(prop);
						}
					}
				}
				if (array2.size() == 1) {
					pf = array2.get(0);
				}
				else {
					pf = array2.get(0);
					//incepem sa parcurgem si array2
					//nu mai avem nevoie de un vector, deoarece o sa gasim sigur o singura varianta
					for (int index = 1; index < array2.size(); index++) {
						Proprietati prop = array2.get(index);
						if (pf.cuvant.value.compareTo(prop.cuvant.value)  > 0) {
							pf = prop;
						}
					}
				}
			}
		}
		}
		return pf;
	}
	
	/**
	 * 
	 * @param s - propozitia care urmeaza sa fie prelucrata
	 * @return - propozitia corectata
	 */
	public static String minim(Map<Integer, ArrayList<Cuvant>> map, String str1) {
		//in v avem toate cuvintele din propozitie, dar ar trebui sa avem un singur sir, sa stergem spatiile si sa il facem charArray
		String s = str1.replaceAll(" ", "");
		char[] v = s.toCharArray();
		int l = v.length;
		//matricea in care vom forma sirurile de caractere
		//incepem sa umplem matricea pe linii, incepand de la prima linie
		//prima linie nu depinde de nimic altceva, asa ca o umplem la inceput
		Proprietati[][] minm = new Proprietati[l][l];
		//umplem prima linie
		for (int i = 0; i < l; i++) {
			if (i <= 18) {
				String str = s.substring(0, i+1);
				Proprietati c = prelCuvant(map, str);
				minm[0][i] = new Proprietati(c.cuvant, c.distMin, c.nr_cuv);
			}
			else
				minm[0][i] = minm[0][i-1];
		}
		//acum incepem sa umplem si celelalte linii
		
		//pf = proprietate finala, aici vom retine elementul de pe coloana, care respecta conditiile specificate in enunt
		Proprietati pf;
		//parcurgem toate liniile, determinam ce pf trebuie sa adaugam la elementele de pe fiecare linie si completam
		for (int i = 1; i < l; i++) {
			ArrayList<Proprietati> array = new ArrayList<Proprietati>();
			//parcurgem restul coloanei cu o unitate mai mica decat linia pe care ne aflam
			//adaugam in array tot ce avem pe acea coloana cu o unitate mai mica decat linia pe care ne aflam
			for (int j = 0; j < i; j++) {
				array.add(minm[j][i-1]);
			}
			//determinam ce cuvant trebuie sa adaugam cuvintelor de pe linia pe care ne aflam
			pf = propFinal(array); 
			//parcurgem toata linia
			for (int col = i; col < l; col++) {
				if (col - i < 18) {
					String str = s.substring(i,	col+1);
					//prelucram cuvantul, pentru a gasi cea mai apropiata varianta din dictionar;
					//daca avem un cuvant prea lung, gen mai mare decat 18 cu 4 sau 5, nu are rost sa il mai cautam in dictionar pe tot
					Proprietati prop = prelCuvant(map, str); //sirul inainte sa adaugam altceva
					//la acel cuvant, adaugam ce avem in pf
					Cuvant c = new Cuvant(prop.cuvant.freq + pf.cuvant.freq, pf.cuvant.value + " " + prop.cuvant.value);
					//update distanta si numar cuvinte
					minm[i][col] = new Proprietati(c, prop.distMin + pf.distMin, prop.nr_cuv + pf.nr_cuv);
				}
				else
					minm[i][col] = minm[i][col-1];
			}
		}
		//dupa ce am parcurs toate liniile si am facut chestiile astea
		//formam un vector cu ultima coloana din matrice
		ArrayList<Proprietati> array = new ArrayList<Proprietati>();
		for (int i = 0; i < l; i++) {
			array.add(minm[i][l-1]);
		}
		Proprietati propF = propFinal(array);
		return propF.cuvant.value;
	}
}
