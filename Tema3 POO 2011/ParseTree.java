import java.util.Stack;

/**
 * 
 * @author Simona Andreea
 * Algoritm construire arbore de parsare
 */
public class ParseTree {
	
	public Node PTree(String s, int l) {
		
		// initializare stive pentru construire arbore
		
		Stack<Node> result = new Stack<Node>();
		Stack<Node> operator = new Stack<Node>();
		String[] a = s.split(" ");
		int coloana = 1;
		
		//parcurg vectorul de String si construiesc arborele
		for (int i=0; i < a.length; i ++) {
			String str = a[i];
			
			// pe pozitiile pare avem intotedeauna variabile sau valori, iar pe cele impare avem operatori
			if ((i % 2) == 0) {
				if (97 <= a[i].charAt(0) && a[i].charAt(0) <= 122 && a[i].length() == 1 ) {
					// daca a[i] este o litera inseamna ca este variabila
					Variabila v = new Variabila(str, coloana, l);
					result.push(v);
				}
				else {
					// altfel este o valoare
					Valoare v = new Valoare(str, coloana, l);
					result.push(v);
				}
			}
			else {
				if (str.equals("=")) {
					OperatorEgal e = new OperatorEgal(str, coloana, l);
					// il adaugam direct pentru ca in limbajul nostru "=" apare doar la inceput, deci va fi mereu primul
						operator.push(e);
				}
				else
					if (str.equals("+")) {
						OperatorPlus p = new OperatorPlus(str, coloana, l);
						// atata timp cat exista * in stiva operator
						while (operator.lastElement().value.equals("*")) {
							// se extrag primele doua elem. din stiva result si acestea devin copiii operatorului extras din stiva
							operator.lastElement().copii.add(result.pop());
							operator.lastElement().copii.addFirst(result.pop());
							result.push(operator.pop());
						}
						// se adauga noul operator dupa ce s-au calculat toate rezultatele partiale necesare
						operator.push(p);
					}
					else 
						if (str.equals("*")) {
							OperatorOri o = new OperatorOri(str, coloana, l);
							operator.push(o);
						}
			}
			if (i == 0)
				coloana += str.length()+1;
			else
				coloana += str.length()+1;
		}
		// se formeaza arborele - varianta finala
		while (result.size() > 1) {
			// se extrag primele doua elem. din stiva result si acestea devin copiii operatorului extras din stiva
			operator.lastElement().copii.add(result.pop());
			operator.lastElement().copii.addFirst(result.pop());
			result.push(operator.pop());
		}
		// singurul Nod care mai ramane in stiva result este radacina pentru expresia respectiva
		return result.pop();
		
	}
}
