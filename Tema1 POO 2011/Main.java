import java.util.Scanner;
import java.lang.String;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args[0].equals("c")) {
		int poz; /*ultima pozitie pe care se gaseste caracterul cautat
					care coincide cu numarul de aparitii ale caracterului in text
					deoarece textul este sortat si dupa fiecare cautare, sterg caracterele numarate*/
		Arbore arb = new Arbore();
		// Citirea textului pana la o linie care contine caracterul "."(punct)
		Scanner sc = new Scanner(System.in).useDelimiter("\\n\\.");
		String text = sc.next();
		//am sters ultimul caracter din text pentru ca imi lua si "\n"nu stiu de ce
		text = text.substring(0, text.length()-1);  
		
		//Sortarea textului in vederea determinarii frecventei caracterelor
		char[] content = text.toCharArray();
		java.util.Arrays.sort(content);
		String sortat = new String(content);
		//folosim variabila pentru a elimina caracterele care se repeta
		String text1 = sortat;
		
		//Determinarea frecventei fiecarui caracter din text si retinerea acesteia intr-un vector
		Vector_arb f = new Vector_arb(127);
		int i = 0;
		while (sortat != "") {
			char c = sortat.charAt(0);
			poz = sortat.lastIndexOf(c); // ultima pozitie pe care se gaseste in vectorul sorta => de cate ori se repeta caracterul respectiv
										//avand in vedere ca mereu stergem caracterele pe care le-am numarat deja
			//adaugarea elementelor in vectorul de frecventa
			f.f[i] = new Arbore(poz+1, "", null, null, null);
			f.f[i].c += c;
			//Stergerea caracterelor care au fost deja numarate si retinute in vectorul de frecventa
			if (sortat.length()-1>poz)
			sortat = sortat.substring(poz+1, sortat.length());
			else
				sortat = "";
			i++;
			}
		
		//Sortare vector frecventa
		int lungime = i;
		for (i = 0; i <lungime; i++) {
			for (int j=i+1; j<lungime; j++) {
				if (f.f[j].n < f.f[i].n) {
					Arbore aux = f.f[i];
					f.f[i] = f.f[j];
					f.f[j] = aux;
				}
			}
		}

		arb = f.huffman(lungime); //construire arbore Huffman
		
		//Parcurgere arbore si determinare coduri
		text1 = arb.c;
		String[] cod = new String[text1.length()];
		for(i = 0; i < text1.length(); i++) {
			cod[i]="";
			Arbore lucru = arb; //arborele cu care lucram, in arb pastram adresa de inceput
			String c = "";
			c = c+text1.charAt(i);
			while (lucru != null) {
				//daca sirul de caractere retinut variabila de tip Arbore are lungimea 1, atunci inseamna ca am ajuns la caracterul cautat
				if (lucru.c.length()==1) break;
				// verificam daca in stanga exista caracterul cautat, iar daca da, avansam pe partea stanga si adaugam '0' la cod
				if (lucru.s.c.indexOf(c) != -1) {
					lucru = lucru.s;
					cod[i] = cod[i] + '0';
				}
				// daca nu am gasit caracterul cautat in stanga, atunci cu siguranta este in dreapta, deci avansam spre partea dreapta si adaugam '1' la cod
				else {
					lucru = lucru.d;
					cod[i] = cod[i] + '1';
				}
		}
			//Afisare cod ASCII si codul determinat de noi
		System.out.println((int)text1.charAt(i) + " " + cod[i]);
		}
		//Afisarea textului codificat
		System.out.println(".");
		String textcod = "";
		//Construirea textului cu ajutorul codurilor obtinute
		for( i=0; i<text.length(); i++) {
			char c = text.charAt(i);
			// pe aceeasi pozitie pe care gasim un caracter in sirul de caractere din arb, gasim codul acestuia in cod... astfel am generat textul codificat
			textcod = textcod + cod[text1.indexOf(c)]; 
		}
		System.out.println(textcod);
}
		// Decodificarea
		if (args[0].equals("d")) {
		
		//Citirea textului codificat si a codurilor 
		Scanner sc1 = new Scanner(System.in);
		// cu ajutorul variabilei numar, contorizam cate elemente are vectorul vectorStr
		int numar = 0;
		String[] vectorStr = new String[500];
		for (numar = 0;;numar ++) {
			vectorStr[numar] = sc1.next();
			// Oprim citirea la punct
			if (vectorStr[numar].equals(".")) break;
		}
		//citim textul codificat
		String cod = sc1.next();
		
		//Creare arbore Huffman pentru decodificare
		Arbore arb = new Arbore();
		Arbore lucru = arb;
		for (int i=0; i<numar-1; i+=2) {
			// Caracterul al carui cod il parcurgem pentru  formarea arborelui
			char caracter = (char)Integer.parseInt(vectorStr[i]);
			lucru = arb;
			String secventa = vectorStr[i+1];
			// parcurgem secventa de biti si daca ramura pe care trebuie sa mergem nu este creata o cream si mergem pe ea mai departe
			for (int j=0; j < secventa.length(); j++) {
				if (secventa.charAt(j) == '1') {
					if (lucru.d == null) {
						Arbore a = new Arbore(0, Character.toString(caracter), null, null, lucru);
						lucru.d = a;
						lucru = lucru.d;
					}
					else {
						// daca ramura deja exista, avansam
						lucru = lucru.d;
						}
				}
				else
					if (secventa.charAt(j) == '0') {
						if (lucru.s == null) {
							Arbore a = new Arbore(0, Character.toString(caracter), null, null, lucru);
							lucru.s = a;
							lucru = lucru.s;
						}
						else
							lucru = lucru.s;
					}
			}
		}
		//formam textul decodificat
		String sirulnostru= new String();
		lucru = arb;
		//parcurgem arborele in ordinea dictata de cod, pana cand ajungem la o frunza.
		//Cand ajungem la o frunza adaugam la sirulnostru(textul pe care vrem sa-l descoperim dupa decodificare) caracterul care se gaseste in campul s
		for (int i=0; i < cod.length(); i++) {
			String sub = cod.substring(i, i+1);
			if (sub.equals("0")) {
				lucru = lucru.s;
				
				if (lucru.s == null && lucru.d == null) {
					sirulnostru = sirulnostru + lucru.c;
					lucru = arb;
				}
			}
			else {
				if (sub.equals("1")) {
					lucru = lucru.d;
					if (lucru.d == null && lucru.s == null) {
						sirulnostru = sirulnostru + lucru.c;
						lucru = arb;
						}
					}

			}
		}
		// afisare
		System.out.println(sirulnostru); 
		System.out.print(".");
		}
		}
}
	
