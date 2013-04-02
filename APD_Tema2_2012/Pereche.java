import java.util.HashMap;

/**
 * Cu ajutorul obiectelor de acest tip, vom retine perechile de tip (numefisier, vector aparitii)
 * @author Simona Badoiu
 *
 */
public class Pereche {
	String numeFis;
	HashMap<String, Integer> nrAparitii;
	
	public Pereche(String numeFis, HashMap<String, Integer> nrAparitii) {
		this.numeFis = numeFis;
		this.nrAparitii = nrAparitii;
	}
}
