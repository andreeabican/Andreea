import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Arbore pentru expectiminimax
 * @author Simona Badoiu
 *
 */
public class Tree {
	Tabla t;
	int color;
	ArrayList<Tree> copii = new ArrayList<Tree>();
	//start si distanta pentru mutarea care a dus la aceasta tabla
	int s,d;
	int tip;
	float probability;
	/**
	 * 
	 * @param tabla - tabla de joc
	 * @param c - culoarea cu care jucam
	 * @param start - pozitia de start
	 * @param dist - un vector cu distanta mutarilor pe care ar mai trebui sa le facem
	 * @param tipe - tipul arborelui (cu mutari pentru mine, cu mutari pentru adversar sau cu zaruri)
	 */
	public Tree(Tabla tabla, int c, int start, int d, int tipe) {
		t = tabla.clone();
		color = c;
		s = start;
		this.d = d;
		tip = tipe; //si pe urma daca tip == c inseamna ca sunt eu, daca nu, inseamna ca e adversarul
	}
	
	@Override
	public String toString() {
		return "[" + t + " " + t.mutari + "]";
	}
	/**
	 * 
	 * @param tabla - tabla de pe care incepem mutarea
	 * @param c - culoarea cu care jucam
	 * @param d - distanta pe care vrem sa mutam
	 * @param casa - daca are valoarea 1 inseamna ca avem toate elementele in casa, iar daca are valoarea 0, nu avem toate elem in casa
	 */
	//creeaza toti copiii pentru acest nod, inclusiv piesele pe care le putem scoate, asta in cazul in care toate piesele sunt in casa
	public void fcopii(Tabla tabla, int c, int d, int casa, int tip) {
			int inCasa;
			if (c == 0) {
				inCasa = casa + tabla.getScoaseW();
			}
			else {
				inCasa = casa + tabla.getScoaseB();
			}
		//	System.out.println("Avem in casa lui " + c + " " + inCasa + "piese");
		//creeaza copiii
			if (inCasa < 15) {
				//System.out.println("Nu avem in casa 15 piese");
				for (int i = 1; i < 25; i++) {
					//cloneaza tabla pe care am primit-o
					Tabla ta = tabla.clone();
						if (ta.move(i, d, c) == true) {
							//retine mutarea in tabla
							ta.mutari.add(i);
							ta.mutari.add(d);
							//System.out.println("Adaug in copii");
							copii.add(new Tree(ta, c, i, d, tip));
						}
					}
			}
			else {
				Tabla ta = tabla.clone();
				if (c == 0) {
					//daca putem sa scoatem de pe pozitia d
					if (tabla.get(d).get_pcs() > 0 && tabla.get(d).get_color() == c) {
						if (ta.scoate(d, d, c)) {
							//retine mutarea in tabla
							ta.mutari.add(d);
							ta.mutari.add(d);
							copii.add(new Tree(ta, c, d, d, tip));
						}
					}
					//daca avem piese pe o pozitie mai mare decat d
					else {
						int ok = 0;
						for (int i = 6; i > d; i--) {
							ta = tabla.clone();
							if (ta.move(i, d, c) == true) {
								ta.mutari.add(i);
								ta.mutari.add(d);
								ok = 1;
								copii.add(new Tree(ta, c, i, d, tip));
								break;
							}
						}
						if (ok == 0) {
							for (int i = d-1; i > 0; i--) {
								ta = tabla.clone();
								if (ta.scoate(i, d, c) == true) {
									ta.mutari.add(i);
									ta.mutari.add(d);
									copii.add(new Tree(ta,c,i,d, tip));
									break;
								}
							}
						}
					}
				}
				else {
					ta = tabla.clone();
					if (c == 1) {
						if (tabla.get(25-d).get_pcs() > 0 && tabla.get(25-d).get_color() == c) {
							if (ta.scoate(25-d, d, c)) {
								ta.mutari.add(25-d);
								ta.mutari.add(d);
								copii.add(new Tree(ta, c, 25-d, d, tip));
							}
						}
						else {
							int ok = 0;
							for (int i = 19; i < 25-d; i++) {
								ta = tabla.clone();
								if (ta.move(i, d, c) == true) {
									ta.mutari.add(i);
									ta.mutari.add(d);
									ok = 1;
									copii.add(new Tree(ta, c, i, d, tip));
									break;
								}
							}
							if (ok == 0) {
								for (int i = (26-d); i < 25; i++) {
									ta = tabla.clone();
									if (ta.scoate(i, d, c) == true) {
										ta.mutari.add(i);
										ta.mutari.add(d);
										copii.add(new Tree(ta, c, i, d, tip));
										break;
									}
								}
							}
						}
					}
				}
			}
	}
	/**
	 * 
	 * @param tabla - tabla la momentul respectiv
	 * @param c - culoarea cu care jucam noi
	 * @param mutari - aici se retin mutarile cele mai bune pe care trebuie sa le facem dupa apelul acestei functii
	 * @param ad - adancimea pana la care vrem sa se mearga: 3 - inseamna pana ca se ajunge o data la un joc al adversarului, 5 inseamna ca se ajunge la inca un 
	 * 				joc al meu etc. 
	 * @param zaruri - toate valorile zarurilor care trebuie sa se transforme in mutari(pentru (6,6) => [6,6,6,6], pentru (2,3) => [2, 3])
	 * @return scorul cel mai bun
	 */
	
	public Move maxMove() {
		int s = 0;
		int d = 0;
		if (copii.size() == 0) return null;
		float score = Evaluate.score(copii.get(0).t,color);
		s = copii.get(0).s;
		d = copii.get(0).d;
	
		for (int i = 1; i < copii.size(); i++) {
			float sc = Evaluate.score(copii.get(i).t, color);
			//System.out.println("Scor dupa: " + sc);
			if (sc > score) {
				score = sc;
				s = copii.get(i).s;
				d = copii.get(i).d;
			}
		}
		return new Move(s, d, score);
	}
	
	public Move max(ArrayList<Integer> zaruri) {
		int s = 0;
		int d = 0;
		if (copii.size() == 0) return null;
		float score = Evaluate.score(copii.get(0).t,color);
		s = copii.get(0).s;
		d = copii.get(0).d;
	
		for (int i = 1; i < copii.size(); i++) {
			float sc = Evaluate.score(copii.get(i).t, color);
			//System.out.println("Scor dupa: " + sc);
			if (sc > score) {
				score = sc;
				s = copii.get(i).s;
				d = copii.get(i).d;
			}
		}
		return new Move(s, d, score);
	}
}
