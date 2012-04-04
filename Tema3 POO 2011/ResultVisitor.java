import java.io.PrintWriter;
import java.util.*;
/**
 * 
 * @author Simona Badoiu
 * evalueaza expresiile si afiseaza valorile variabilelor in ordine alfabetica
 */
public class ResultVisitor implements Visitor {
	Map<String, String> dictionar = new TreeMap<String, String>();
	String val;
	PrintWriter out;
	/*
	 * primeste fisierul in care se vor scrie rezultatele
	 */
	public ResultVisitor(PrintWriter fisier) {
		out = fisier;
	}

	@Override
	public void visit(Variabila v) {
		if (dictionar.containsKey(v.value))
			val = dictionar.get(v.value);
		
	}

	@Override
	public void visit(Valoare v) {
		val = v.value;
	}

	@Override
	public void visit(OperatorEgal e) {
		e.copii.get(1).accept(this);
		// se retine in dictionar valoarea pe care o are membrul din stanga
		dictionar.put(e.copii.get(0).value, val);
		
	}

	@Override
	public void visit(OperatorPlus p) {
		// se apeleaza accept pentru copilul din stanga
		p.copii.get(0).accept(this);
		// se retine valoarea la care s-a ajuns
		String v1 = val;
		// se apeleaza accept pentru copilul din dreapta
		p.copii.get(1).accept(this);
		// se face prelucrarea pentru tipul boolean
		if (v1.equals("false") && val.equals("false")) {
			val = "false";
		}
		else {
			if((v1.equals("false") && val.equals("true")) || (v1.equals("true") && val.equals("false")) || (v1.equals("true") && val.equals("true") ))
				val = "true";
		}
		//  sau pentru integer
		if (!val.equals("false") && !val.equals("true")) {
			int s = Integer.parseInt(v1) + Integer.parseInt(val);
			val = Integer.toString(s);
		}
	}

	@Override
	public void visit(OperatorOri o) {
		// se apeleaza accept pentru copilul din stanga
		o.copii.get(0).accept(this);
		// se retine valoarea obtinuta pana aici
		String v1 = val;
		// se apeleaza accept pentru copilul din dreapta
		o.copii.get(1).accept(this);
		// se face prelucrarea daca este boolean
		if (v1.equals("true") && val.equals("true")) {
			val = "true";
		}
		else
			if((v1.equals("false") && val.equals("true")) || (v1.equals("true") && val.equals("false")) || (v1.equals("false") && val.equals("false"))) {
				val = "false";
			}
		// se face prelucrarea daca este integer
		if (!val.equals("false") && !val.equals("true")) {
			int s = Integer.parseInt(v1) * Integer.parseInt(val);
			val = Integer.toString(s);
		}
		
		
	}
	/*
	 * afiseaza, in ordine alfabetica, rezultatele obtinute in urma evaluarii expresiilor
	 */
	public void getVal() {
		for (Map.Entry<String, String> entry : dictionar.entrySet()) {
		    out.println(entry.getKey() + " = " + entry.getValue());
		}
	}
}
