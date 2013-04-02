/**
 * O folosim pentru a retine datele din matricea folosita pentru reconstructia unei propozitii
 * @author Simona Badoiu
 *
 */
public class Proprietati {
	Cuvant cuvant;
	int distMin;
	int nr_cuv;
	/**
	 * 
	 * @param c - de tip Cuvant. contine cuvantul si frecvennta de aparitie a cuvantului
	 * @param dm - distanta minima pana la cuvantul din c, de la cuvantul pentru care am calculat si pe care il construim pe baza matricei
	 */
	public Proprietati(Cuvant c, int dm, int n_c) {
		cuvant = c;
		distMin = dm;
		nr_cuv = n_c;
	}
	
	@Override
	public String toString() {
		return cuvant.toString() + " " + distMin + " " + nr_cuv + " ";
	}
}
