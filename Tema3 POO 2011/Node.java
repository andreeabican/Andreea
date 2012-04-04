import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * 
 * @author Simona Badoiu
 *
 */
public class Node implements Visitable {
	LinkedList<Node> copii = new LinkedList<Node>();
	String value;
	int coloana=0;
	int linie = 0;
	/**
	 * 
	 * @param str - valoarea citita din fisier
	 * @param col - coloana pe care se afla valoarea
	 * @param l - linia pe care se afla valoarea
	 */
	public Node(String str, int col, int l) {
		value = str;
		coloana = col;
		linie = l;
	}
	/**
	 * Afiseaza intregul arbore
	 * @param out - fisierul in care se scriu rezultatele
	 */
	public void display(PrintWriter out) {
		String s = "";
		preorder(s, out);
	}
	/**
	 * Parcurgere RSD - deoarece toti arborii care pornesc din copiii radacinei sunt arbori binari
	 * @param s - sirul de caractere format din \t care determina indentarea
	 * @param out - fisierul in care se scriu rezultatele
	 */
	public void preorder(String s, PrintWriter out) {
			out.println(s + value);
			for (int i=0; i<copii.size(); i++) {
				copii.get(i).preorder(s + "\t", out);
			}
		}

	@Override
	public void accept(Visitor v) {
		
	}
}
