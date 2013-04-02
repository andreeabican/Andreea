/**
 * 
 * @author Simona Badoiu
 * Contine frecventa de aparitie a cuvantului si cuvantul, sau propozitia mai nou
 */
public class Cuvant implements Comparable<Cuvant> {
	int freq;
	String value;
	/**
	 * 
	 * @param f - Frecventa de aparitie
	 * @param s - Cuvantul care are aceasta frecventa de aparitie
	 */
	public Cuvant(int f, String s) {
		freq = f;
		value = s;
	}

	@Override
	public int compareTo(Cuvant o) {
		if (value.compareTo(o.value) > 0) return 1;
		return -1;
	}
	
	@Override
	public boolean equals(Object obj) {
		Cuvant c = (Cuvant)obj;
		return ((freq == c.freq) && (value.equals(c.value)));
	}
	
	@Override
	public String toString() {
		return ("[" + value + ", " + freq + "]");
	}
	
	@Override
	public int hashCode() {
		return value.hashCode() + ((Integer)freq).hashCode();
	}
}
