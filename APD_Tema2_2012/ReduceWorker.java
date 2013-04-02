import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Clasa ce reprezinta o solutie partiala pentru problema de rezolvat. Aceste
 * solutii partiale constituie task-uri care sunt introduse in workpool.
 */
class PartialSolutionReduce {
	String fisier;
	int tip;
	ArrayList<HashMap<String, Integer>> al;
	String[] cuvinte;
	LinkedHashMap<String, String> lhm;
	HashMap<String, Double> mapProcente;
	/**
	 * 
	 * @param fisier - fisierul in care se cauta cuvintele
	 * @param N - cele mai frecvente N cuvinte
	 * @param al - vectorul de rezultate pentru fisierul "fisier" obtinute in urma operatiei de tip MAP
	 * @param cuvinte - cuvintele pe care le cautam in fisier
	 * @param tip - tipul de REDUCE(1 sau 2 - ca in enuntul temei)
	 */
	public PartialSolutionReduce(String fisier, ArrayList<HashMap<String, Integer>> al, String cuvinte[], LinkedHashMap<String, String> lhm,  HashMap<String, Double> mapProcente, int tip) {
		this.fisier = fisier;
		this.al = al;
		this.cuvinte = cuvinte;
		this.tip = tip;
		this.mapProcente = mapProcente;
		this.lhm = lhm;
	}
	
	public String toString() {
		return fisier + " " + tip;
	}
}

/**
 * Clasa ce reprezinta un thread worker care executa operatia Reduce
 */
class ReduceWorker extends Thread {
	WorkPoolReduce wp;
	int X, N;

	public ReduceWorker(WorkPoolReduce workpool, int N, int X) {
		this.wp = workpool;
		this.N = N;
		this.X = X;
	}

	/**
	 * Procesarea unei solutii partiale. Aceasta poate implica generarea unor
	 * noi solutii partiale care se adauga in workpool folosind putWork().
	 * Daca s-a ajuns la o solutie finala, aceasta va fi afisata.
	 */
	void processPartialSolution(PartialSolutionReduce ps) {
		if (ps.tip == 1) {
			//in acest hash map vom avea numarul total de aparitii al fiecarui cuvant in fisier
			HashMap<String, Integer> nrFinal = new HashMap<String, Integer>();
			HashMap<String, Double> procentFinal = new HashMap<String, Double>();
			ValueComparator bvc = new ValueComparator(procentFinal);
			TreeMap<String, Double> procentFinal2 = new TreeMap<String, Double>(bvc);
			HashMap<String, Double> deTrimis = new HashMap<String, Double>();
			//numarul total de cuvinte din fisier
			int cuvinteTotal = 0;
			//insumam aparitiile fiecarui cuvant in fiecare HashMap din ArrayList
			Iterator<HashMap<String, Integer>> it = ps.al.iterator();
			while (it.hasNext()) {
				HashMap<String, Integer> aux = it.next();
				for (Map.Entry<String, Integer> entry : aux.entrySet() ) {
					String str = entry.getKey();
					if (!str.equals("")) {
						int nr = entry.getValue();
						cuvinteTotal += nr;
						if (nrFinal.containsKey(str)) {
							int nraux = nrFinal.get(str) + nr;
							nrFinal.put(str, nraux);
						}
						else {
							nrFinal.put(str, nr);
						}
					}
				}
			}
			
			//calculam procentul cu 2 zecimale pentru fiecare cuvant
			for (Map.Entry<String, Integer> entry : nrFinal.entrySet()) {
				Double procent = entry.getValue()/(double)cuvinteTotal*100;
				//trunchiem la 2 zecimale
				procent = (double)((int)( procent *100))/100;
				procentFinal.put(entry.getKey(), procent);
			}
			procentFinal2.putAll(procentFinal);
			//calculam un vector cu cele mai frecvente N cuvinte din fisier
			int k = N;
			//System.out.println("k este: " + k);
			Double precedent = new Double(-1);
			for (Map.Entry<String, Double> entry : procentFinal2.entrySet()) {
				//daca am ajuns la sfarsit, adaugam toate cuvintele care au frecventa de aparitie egala
				//frecventa ultimului cuvant
				if (k == 0) {
					if (precedent.equals(entry.getValue())) {
						deTrimis.put(entry.getKey(), entry.getValue());
					}
					else
						break;
				}
				else {
					precedent = entry.getValue();
					deTrimis.put(entry.getKey(), precedent);
					k--;
				}
			}
		PartialSolutionReduce psr2 = new PartialSolutionReduce(ps.fisier, ps.al, ps.cuvinte, ps.lhm, deTrimis, 2);
		wp.putWork(psr2);
		}
		else {
			//prelucrare REDUCE de tip 2
			//avem un HashMap global in care adauga informatii fiecare worker
			boolean ok = true;
			String procente = "";
			for (int index = 0; index < ps.cuvinte.length; index++) {
				//daca in fisier, unul din cuvintele cautate nu se gaseste in vectorul de frecvente maxime
				//atunci documentul nu va fi afisat
				if (!ps.mapProcente.containsKey(ps.cuvinte[index])) {
					ps.lhm.put(ps.fisier, null);
					ok = false;
					break;
				}
				else {
					String pr = String.format("%.2f", ps.mapProcente.get(ps.cuvinte[index]));
					pr = pr.replace(',', '.');
					procente = procente + pr + ", ";
				}
			}
			//am gasit un fisier care corespunde cautarii
			if (ok == true) {
				ps.lhm.put(ps.fisier, procente);
			}
		}
			
	}
	
	public void run() {
		//System.out.println("Thread-ul worker " + this.getName() + " a pornit...");
		while (true) {
			PartialSolutionReduce ps = wp.getWork();
			if (ps == null)
				break;
			
			processPartialSolution(ps);
		}
		//System.out.println("Thread-ul worker " + this.getName() + " s-a terminat...");
	}

	
}

class ValueComparator implements Comparator<String> {
	Map<String, Double> base;
	public ValueComparator(Map<String, Double> base) {
		this.base = base;
	}
	@Override
	public int compare(String o1, String o2) {
		if (base.get(o1) >= base.get(o2)) {
            return -1;
        }
		else {
            return 1;
        } // returning 0 would merge keys
    }	
}
