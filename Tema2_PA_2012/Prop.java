/**
 * Proprietatile unui loc de pe tabla
 * @author Simona Badoiu
 * 
 */
public class Prop {
	private int color;
	private int nr_pcs;
	
	public Prop(int c, int n_p) {
		color = c;
		nr_pcs = n_p;
	}
	/**
	 * Verifica daca, culoarea de pe aceasta pozitie este aceeasi cu culoarea piesei pe care vrem sa o adaugam
	 * @param c - culoarea piesei pe care vrem sa o adaugam pe aceasta pozitie
	 * @return True, daca c == color, False, altfel
	 */
	public boolean check_color(int c) {
		return (color == c);
	}
	/**
	 * mareste numarul pieselor cu 1
	 */
	public void addPiece() {
		nr_pcs++;
	}
	/**
	 * scade numarul pieselor cu 1
	 */
	public void rmPiece() {
		nr_pcs--;
	}
	
	/**
	 * Schimba culoarea de pe aceasta pozitie
	 * @param c - noua culoare pe care o punem pe pozitia aceasta
	 */
	public void changeColor(int c) {
		color = c;
	}
	
	/**
	 * 
	 * @return - culoarea de pe aceasta pozitie
	 */
	public int get_color() {
		return color;
	}
	/**
	 * 
	 * @return - numarul de piese de pe aceasta pozitie
	 */
	public int get_pcs() {
		return nr_pcs;
	}
	
	@Override
	public String toString() {
		String s = "";
		String col;
		if (color == 0) {
			col = "o";
			}
		else {
			if (color == 1)
				col = "x";
			else
				col = "-";
		}
		for (int i = 0; i < nr_pcs; i++) {
			s +=  col + " ";
		}
		return s;
	}
}
