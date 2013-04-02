import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clasa ce reprezinta o solutie partiala pentru problema de rezolvat. Aceste
 * solutii partiale constituie task-uri care sunt introduse in workpool.
 */
class PartialSolutionMap {
	String numeFis;
	int start, len;
	public PartialSolutionMap(String numeFis, int start, int len) {
		this.numeFis = numeFis;
		this.start = start;
		this.len = len;
	}
	
	public String toString() {
		return numeFis + start + len;
	}
}

/**
 * Clasa ce reprezinta un thread worker care executa operatia Map
 */
class MapWorker extends Thread {
	WorkPoolMap wpm;
	//WorkPoolReduce wpr;
	HashMap<String, ArrayList<HashMap<String, Integer>>> total;

	public MapWorker(WorkPoolMap workpoolMap, HashMap<String, ArrayList<HashMap<String, Integer>>> total) {
		this.wpm = workpoolMap;
		//this.wpr = workpoolReduce;
		this.total = total;
	}

	/**
	 * Procesarea unei solutii partiale. Aceasta poate implica generarea unor
	 * noi solutii partiale care se adauga in workpool folosind putWork().
	 * Daca s-a ajuns la o solutie finala, aceasta va fi afisata.
	 */
	void processPartialSolution(PartialSolutionMap ps) {
		HashMap<String, Integer> nrCuvinte = new HashMap<String, Integer>();
		//Deschide fisierul si adauga task uri in workpool-ul pentru Reduce
		//taskurile sunt de tipul PartialSolutionReduce
		
		//Deschidem fisierul pentru citire
		File file = new File(ps.numeFis);
		long size = file.length();
		int x, y, z;
		int inceput, lungime;
		inceput = ps.start;
		lungime = ps.len;
		byte b[] = new byte[lungime];
		byte b1[] = new byte[1];
		
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			//verificam daca se incepe la mijlocul cuvantului, iar daca da, atunci sarim peste cuvant
			if (inceput > 0) {
				raf.seek(inceput-1);
				y = raf.read(b1, 0, 1);
				//Daca inainte de prima litera pe care vrem sa o citim, avem un delimitator, inseamna ca este inceput
				//de cuvant, atunci valoarea var start ramane aceeasi, altfel, modificam valoarea var start, astfel
				//incat sa indice inceputul urmatorului cuvant
				
				//atata timp cat gasim o litera, trecem pe pozitia urmatoare, pana cand gasim un delimitator
				if (new String(b1).matches("[a-zA-Z]")) {
					while ((new String(b1).matches("[a-zA-Z]"))) {
						inceput++;
						lungime--;
						raf.seek(inceput-1);
						y = raf.read(b1, 0, 1);
						//daca se ajunge la icneputul fisierului, se iese din while
						if (y == -1)
							break;
					}
				}
			}
			
			//verificam daca fragmentul se termina in mijlocul unui cuvant
			
			//daca, respectand lungimea, se trece dincolo de sfarsitul fisierului, modificam lungimea 
			if (inceput + lungime >= size) {
				lungime = (int) (size - inceput);
			}
			else {
				//daca, cu lungimea data, nu se ajunge la sfarsitul fisierului
				raf.seek(inceput+lungime-1);
				y = raf.read(b1, 0, 1);
				if (new String(b1).matches("[a-zA-Z]")) {
					while ((new String(b1).matches("[a-zA-Z]"))) {
						lungime++;
						raf.seek(inceput+lungime-1);
						y = raf.read(b1, 0, 1);
						//daca se ajunge ll sfarsitul fisierului, se iese din while
						if (y == -1)
							break;
					}
				}
			}
			b = new byte[lungime];
			//dupa ce am stabilit inceputul si lungimea, incepem sa numaram cuvintele
			raf.seek(inceput);
			//s-a modificat lungimea, deci trebuie sa reinitializam vectorul
			b = new byte[lungime];
			x = raf.read(b, 0, lungime);
			String str = new String(b);
			str = str.toLowerCase();
			//se face split dupa orice delimitator(tot ce nu este litera)
			String s[] = str.split("[^a-z]+");
			//parcurgem tot vectorul de cuvinte si numaram de cate ori apare fiecare cuvant
			for(int i = 0; i < s.length; i++) {
				System.out.print(s[i]+" ");
				//daca am mai intalnit acest cuvant pana acum, marim nr-ul de aparitii cu 1
				if (nrCuvinte.containsKey(s[i])) {
					int nr = nrCuvinte.get(s[i]);
					nr++;
					nrCuvinte.put(s[i], nr);
				}
				else {
					//daca este prima aparitie a cuvantului => nr. aparitii este 1
					nrCuvinte.put(s[i], 1);
				}
			}
			//adaugam rezultatul partial, in hashMap-ul in care pastram toate rezultatele
			if (total.containsKey(ps.numeFis)) {
				total.get(ps.numeFis).add(nrCuvinte);
			}
			else {
				total.put(ps.numeFis, new ArrayList<HashMap<String, Integer>>());
				total.get(ps.numeFis).add(nrCuvinte);
			}
			
			//Dupa ce avem vectorul de cuvinte, putem incepe sa le numaram
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
		
	}
	
	public void run() {
		//System.out.println("Thread-ul worker " + this.getName() + " a pornit...");
		while (true) {
			PartialSolutionMap ps = wpm.getWork();
			if (ps == null)
				break;
			
			processPartialSolution(ps);
		}
		//System.out.println("Thread-ul worker " + this.getName() + " s-a terminat...");
	}

	
}
