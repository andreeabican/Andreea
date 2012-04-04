import java.io.PrintWriter;
import java.util.*;

/**
 * 
 * @author Simona Badoiu
 * parcurgerea arborelui construit la cerinta 1 si verificarea corectitudinii semantice a expresiilor. 
 */
public class SemanticVisitor implements Visitor {
	Map<String, String> dictionar = new TreeMap<String, String>();
	String type = "";
	PrintWriter out;
	int err = 0;
	/**
	 * 
	 * @param fisier - fisierul in care se vor scrie rezultatele
	 */
	SemanticVisitor(PrintWriter fisier) {
		out = fisier;
	}
	@Override
	public void visit(Variabila v) {
		//Stim ca variabila nu are copii, asa ca vom verifica doar daca aceasta se afla in dictionar
		//daca variabila nu exista in dictionar, se afiseaza eroare - variabila nedeclarata
		if (!dictionar.containsKey(v.value)) {
			out.println(v.value + " nedeclarata la linia " + v.linie + " coloana " + v.coloana);
			type = "nedeclarata";
			err = 1;
		}
		else
			type = dictionar.get(v.value);
	}

	@Override
	public void visit(Valoare v) {
		// nici nodurile de tip Valoare nu au copii, asa ca si aici facem doar o verificare
		// Daca valoarea este de tip boolean sau integer
		if (v.value.equals("true") || v.value.equals("false")) {
			type = "boolean";
		}
		else
			type = "integer";
		
	}

	@Override
	public void visit(OperatorEgal e) {
		if (e.copii.size() > 0) {
			// daca, copilul stang nu este de tip Variabila, se afiseaza eroare
			if (!(e.copii.get(0) instanceof Variabila)) {
				out.println("membrul stang nu este o variabila la linia " + e.copii.get(0).linie + " coloana " + e.copii.get(0).coloana);
				err = 1;
			}
			//se apeleaze accept pentru copilul drept al operatorului egal
			e.copii.get(1).accept(this);
			// In urma apelarii accept, type isi va modifica valoarea, deci vom face verificarea, sa vedem ce tip are variabila din stanga
			// si sa facem modificarile in dictionar
			if (type.equals("integer") || type.equals("boolean"))
					dictionar.put(e.copii.get(0).value, type);
			else
				if (dictionar.containsKey(e.copii.get(0).value))
					dictionar.remove(e.copii.get(0).value);
			}
		
	}

	@Override
	public void visit(OperatorPlus p) {
		if (p.copii.size() > 0) {
			// se apeleaza aceept pt copilul din stanga si re retine tipul la care s-a ajuns
			p.copii.get(0).accept(this);
			String type1  = type;
			// apoi se apeleaza accept pentru copilul din dreapta 
			p.copii.get(1).accept(this);
			// se compara tipul din stanga cu tipul din dreapta si... daca nu sunt identice, se afiseaza eroare
			if (!type.equals(type1) && !(type.equals("nedeclarata") || type1.equals("nedeclarata"))) {
				out.println("+ intre tipuri incompatibile la linia " + p.linie + " coloana " + p.coloana);
				type = "nedeclarata";
				err = 1;
			}
		}
		
		
	}

	@Override
	public void visit(OperatorOri o) {
		// acelasi procedeu ca la adunare
		if (o.copii.size() > 0) {
			o.copii.get(0).accept(this);
			String type1 = type;
			o.copii.get(1).accept(this);
			if (!type.equals(type1) && !(type.equals("nedeclarata") || type1.equals("nedeclarata"))) {
				out.println("* intre tipuri incompatibile la linia " + o.linie + " coloana " + o.coloana);
				type = "nedeclarata";
				err = 1;
			}
		}
		
	}
	/**
	 * 
	 * @return - valoarea lui err
	 */
	public int getError() {
		return err;
	}

}
