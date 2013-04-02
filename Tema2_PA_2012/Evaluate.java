import java.util.ArrayList;
import java.util.Iterator;


public class Evaluate {
	
	//intoarce numarul de piese ale culorii c care sunt singure
	public static int oPiesa(int c, Tabla t) {
		int nr = 0;
		for (int i = 1; i < 25; i++) {
			Prop loc = t.get(i);
			if (loc.get_pcs() == 1 && loc.get_color() == c) {
				nr++;
			}
		}
		return nr;
	}
	
	//intoarce numarul de piese de pe bara ale culorii c
	public static int dePeBara(int c, Tabla t) {
		if (c == 0) {
			return t.getScoaseW();
		}
		else
			return t.getScoaseB();
	}
	
	//numarul portilor din casa
	public static int poartaInCasa(int c, Tabla t) {
		int p = 0;
		if (c == 0) {
			for (int i = 1; i <= 6; i++) {
				Prop loc = t.get(i);
				if (loc.get_pcs() == 2) {
					p++;
				}
			}
		}
		else {
			for (int i = 19; i < 25; i++) {
				Prop loc = t.get(i);
				if (loc.get_pcs() == 2) {
					p++;
				}
			}
		}
		return p;
	}
	/**
	 * Calculeaza numarul de mutari pe care trebuie sa le mai faca jucatorul de culoare c pentru a termina jocul
	 * @param t - Tabla de la momentul respectiv
	 * @param c - culoarea pentru care vrem sa calculam scorul
	 */
	public static float score(Tabla t, int c) {
		float score = 0;
		score -= oPiesa(c, t);
		score -= dePeBara(c, t);
		score += poartaInCasa(c, t);
 		return score;
	}
	
	public ArrayList<Integer> genMoves(int i, int j) {
		ArrayList<Integer> result;
		if (i == j) {
			result = new ArrayList<Integer>();
			result.add(i);
			result.add(j);
		}
		else {
			result = new ArrayList<Integer>();
			for (int k = 0; k < 4; k++) {
				result.add(i);
			}
		}
		return result;
	}
	
	public int peBara(int color, Tabla tabla) {
		if (color == 0) {
			return tabla.get(25).get_pcs();
		}
		else {
			return tabla.get(0).get_pcs();
		}
	}
	
	float min(float a, float b) {
		if (a < b)
			return a;
		else
			return b;
	}

	public double expectiminimax(Tabla t, int adancime, ArrayList<Integer> zaruri, int c, Tabla rezultat) {
		//scorul global
		double s = 0;
		//culoare adversar
		int cc;
		if ( c == 0)
			cc = 1;
		else
			cc = 0;
		if (adancime == 0) {
			//System.out.println("Adancaim0");
			return Evaluate.score(t, c);
		}
		if (adancime == 3) {
			//System.out.println("\n\nAdancime3\n\n");
			s = Float.MIN_VALUE;
			Iterator<Integer> it = zaruri.iterator();
			Tree tree = new Tree(t, c, 0, 0, c);
			int inCasa = t.inCasa(c);
			ArrayList<Tree> c1 = new ArrayList<Tree>();
			if (it.hasNext()) {
				int zar = it.next();
				tree.fcopii(t, c, zar, inCasa, c);
				c1 = tree.copii;
				//System.out.println("Primul c1: " + c1);	
				while (it.hasNext()) {
					zar = it.next();
					ArrayList<Tree> c2 = new ArrayList<Tree>();
					for (Tree copil : c1) {
						inCasa = copil.t.inCasa(c);
						copil.fcopii(copil.t, c, zar, inCasa, c);
						c2.addAll(copil.copii);
						//System.out.println("c2 este: " + c2);
					}
					c1 = c2;
					//System.out.println("c1 este: " + c1);
				}
			}
			
			for (Tree tr : c1) {
				double score = expectiminimax(tr.t, adancime - 1, zaruri, c, rezultat);
				if (score > s) {
					rezultat.mutari.addAll(tr.t.mutari);
					s = score;
				}
			}
		}
		else {		
			
			if (adancime == 2) {
				//System.out.println("\n\n\nAdancime2\n\n\n");
				s = 0;
				double probability;
				for (int i = 1; i < 7; i++) {
					for (int j = 1; j < 7; j++) {
						if (i == j) {
							probability = 0.0277777;
							//System.err.println("================================ " + probability);
						}
						else {
							probability = 0.05555;
							//System.err.println("========================== " + probability);
						}
						ArrayList<Integer> val_zaruri = genMoves(i, j);
						s += probability * expectiminimax(t, adancime-1, val_zaruri, cc, rezultat);
					}
				}
			}
			else {
				if (adancime == 1) {
				//System.out.println("\n\nAdancime1\n\n");
					int peBara = t.peBara(cc);
					Iterator<Integer> it = zaruri.iterator();
					//se introduc piesele de pe bara
					while (peBara > 0 && it.hasNext()) {
						int d = it.next();
						if (t.introduce(d, cc)) {
							it.remove();
						}
					}
					//mutarile executate dupa ce nu mai avem piese pe bara
					ArrayList<Tree> copii = new ArrayList<Tree>();
					ArrayList<Integer> dice = new ArrayList<Integer>();
					dice = ClientStub.correctOrder(zaruri, cc, t);
					it = dice.iterator();
					Tree tree = new Tree(t, c, 0, 0, c);
					int inCasa = t.inCasa(cc);
					if (peBara == 0 && it.hasNext()) {
						int zar = it.next();
						tree.fcopii(t, cc, zar, inCasa, cc);
						copii = tree.copii;
						/*while (it.hasNext()) {
							zar = it.next();
							ArrayList<Tree> copii1 = new ArrayList<Tree>();
							for (Tree copil : copii) {
								inCasa = copil.t.inCasa(cc);
								copil.fcopii(copil.t, cc, zar, inCasa, cc);
								copii1.addAll(copil.copii);
							}
							copii = copii1;
						}*/
					}
					s = Double.MIN_VALUE;
					/*if (copii.size() == 0) {
					
						s1 += probability * Evaluate.score(t, cc);
						System.out.println("Copiii in else " + copii );
					}*/
						//System.out.println("Copiii in else " + copii );
						for (Tree copil : copii) {
							double s1 = expectiminimax(copil.t, adancime-1, zaruri, cc, rezultat);
							if (s1 > s) {
								s = s1;
							}
							//System.out.println("Scorul s1 este: " + s1);
						}
				}
						 
			}
		}
		return s;
	}

	
}
