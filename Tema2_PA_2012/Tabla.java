import java.util.ArrayList;

/**
 * Retine tabla de joc, aici se aplica toate modificarile
 * @author Simona Badoiu
 * tabla este reprezentata cu ajutorul unu vector de prop, care are 24 de elemente.
 */
public class Tabla {
	// in mutari retinem mutarile pe care le-am facut pentru a ajunge la aceasta stare
	ArrayList<Integer> mutari = new ArrayList<Integer>();
	private ArrayList<Prop> t;
	//numarul de piese scoase de alb
	private int scoaseW = 0;
	public int getScoaseW() {
		return scoaseW;
	}

	public void setScoaseW(int scoaseW) {
		this.scoaseW = scoaseW;
	}

	public int getScoaseB() {
		return scoaseB;
	}

	public void setScoaseB(int scoaseB) {
		this.scoaseB = scoaseB;
	}
	//numarul de piese scoase ne negru
	private int scoaseB = 0;
	/**
	 * Constructor
	 */
	public Tabla() {
		t = new ArrayList<Prop>(26);
		//initializeaza fiecare pozitie din tabla cu Prop de -1 si 0, adica nu are  nicio piesa pe tabla
		//iar culoarea este -1(adica nu are inca asociata o culoare). Pe pozitia 0 avem bara pentru negru si pe pozitia 25
		//avemm bara pentru alb
		for (int i = 0; i < 26; i++) {
			t.add(new Prop(-1, 0));
		}
		//bara negru
		t.set(0, new Prop(1, 0));
		//aranjare piese pe tabla
		t.set(1, new Prop(1, 2));
		t.set(6, new Prop(0, 5));
		t.set(8, new Prop(0, 3));
		t.set(12, new Prop(1, 5));
		t.set(13, new Prop(0, 5));
		t.set(17, new Prop(1, 3));
		t.set(19, new Prop(1, 5));
		t.set(24, new Prop(0, 2));
		//bara alb
		t.set(25, new Prop(0, 0));
	}
	
	/**
	 * 
	 * @param i - pozitia in t
	 * @return - numarul de piese de pe pozitia i
	 */
	public int nr_pcs(int i) {
		return t.get(i).get_pcs();
	}
	
	/**
	 * 
	 * @param i - pozitia in t
	 * @return Culoare pieselor de pe pozitia i
	 */
	public int color(int i) {
		return t.get(i).get_color();
	}
	
	/**
	 * 
	 * @param color - culoarea pentru care vrem sa verificam daca are piese pe bara
	 * @return - true daca are cel putin o piesa pe bara si false daca nu
	 */
	public boolean onBar(int color) {
		if (color == 0) {
			if (t.get(25).get_color() == 0)
				return false;
			else
				return true;
		}
		else {
			if (color == 1) {
				if (t.get(0).get_color() == 0)
					return false;
				else
					return true;
			}
		}
		return false;
	}
	
	//parcurg toate valorile zarurilor, si daca introduce are valoarea true, atunci mai raman de jucat doar valorile care
	//nu au fost folosite aici
	/**
	 * 
	 * @param x - valoarea de pe zar
	 * @param culoare - culoarea cu care se joaca
	 * @return daca s-a introdus sau nu acea piesa pe tabla
	 */
	public boolean introduce(int x, int culoare) {
		int c;
		int p;
		//jucatorul alb trebuie sa introduca piesa de pe bara in casa jucatorului negru
		if (culoare == 0) {
			Prop loc = t.get(25-x);
			c = loc.get_color();
			p = loc.get_pcs();
			if (p == 0 || c == culoare) {
				//scade nr-ul pieselor de pe bara si creste numarul pieselor de pe pozitia loc
				t.get(25).rmPiece();
				loc.addPiece();
				//daca nu aveam piese in acea pozitie, updatam si culoarea
				if (p == 0) {
					loc.changeColor(culoare);
				}
				
			}
			else {
				if (p == 1 && c != culoare) {
					t.get(0).addPiece();
					t.get(25).rmPiece();
					t.set(25-x, new Prop(0, 1));
				}
				else
					return false;
			}
		}
		//daca avem culoarea negru
		else {
			Prop loc = t.get(x);
			c = loc.get_color();
			p = loc.get_pcs();
			if (p == 0 || c == culoare) {
				//scade nr-ul pieselor de pe bara si creste numarul pieselor de pe pozitia loc
				t.get(0).rmPiece();
				loc.addPiece();
				//daca nu aveam piese in acea pozitie, updatam si culoarea
				if (p == 0) {
					loc.changeColor(culoare);
				}
				
			}
			else {
				if (p == 1 && c != culoare) {
					t.get(25).addPiece();
					t.get(0).rmPiece();
					t.set(x, new Prop(1, 1));
				}
				else
					return false;
			}
				
		}
			
		return true;
	}
	
	int peBara(int c) {
		if (c == 0)
			return t.get(25).get_pcs();
		else
			return t.get(0).get_pcs();
	}
	
	/**
	 * Executa mutarea de la s la f, daca se poate
	 * @param s - pozitia de start
	 * @param f - distanta pe care trebuie sa mute
	 * @return true - daca mutarea s-a putut face si false daca nu
	 */
	public boolean move(int s, int d, int color) {
		//daca primim o valoare in afara limitelor, intoarcem false
		if (s <= 0 || s >= 25 || d <= 0 || d > 6)
			return false;
		
		Prop start = t.get(s);
		Prop fin = new Prop(-1, 0);//sau null???
		int sColor = start.get_color();
		int sPcs = start.get_pcs();
		
		if (color != sColor)
			return false;
		
		//daca in start nu avem piese, nu se poate face mutarea => false
		if (sPcs == 0)
			return false;
		
		//daca muta jucatorul alb, muta de la mai mare la mai mic
		if (sColor == 0) {
			int f = s-d;
			if (f <= 0)
				return false;
			fin = t.get(f);
		}
		//daca muta jucatorul negru, muta de la mic la mare
		else {
			if (sColor == 1) {
				int f = s+d;
				if (f >= 25)
					return false;
				fin = t.get(f);
			}
		}
		
		int fColor = fin.get_color();
		int fPcs = fin.get_pcs();
		
		
		//daca mutam aceeasi culoare cu ce se afla pe pozitia f, sau daca pe fin nu se afla inca nicio piesa
		if ((sColor == fColor) || (fPcs == 0)) {
			start.rmPiece();
			fin.addPiece();
			//daca nu aveam nicio piesa pe pozitia fin, ii dam culoarea pe care o avea piesa din start
			if (fPcs == 0) {
				fin.changeColor(start.get_color()); //???sa pun inainte sau dupa ce modific fin?????????
			}
			return true;
		}
		else {
			if (fPcs == 1) {
				start.rmPiece();
				if (fin.get_color() == 0) {
					t.get(25).addPiece();
					fin.changeColor(1);
				}
				else
					if (fin.get_color() == 1) {
						t.get(0).addPiece();
						fin.changeColor(0);
					}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Muta o piesa care trebuie scoasa, daca ajunge undeva tot pe tabla, o muta acolo, iar daca
	 * ajunge in afara, mareste variabila scoase :)
	 * @param s - pozitia de unde scoatem
	 * @param d - valoarea zarului
	 * @param c - culoarea pentru care scoatem
	 */
	//trebuie sa ii dau mutare valida pentru ca nu verific
	public boolean scoate(int s, int d, int c) {
		if (t.get(s).get_pcs() == 0 || t.get(s).get_color() != c) return false;
		if (c == 0) {
			//System.out.println("!!!Am scos o piesa alba!!!");
			if (s - d <= 0) {
				scoaseW++;
				t.get(s).rmPiece();
				return true;
			}
		}
		else {
			if (c == 1) {
				//System.out.println("!!!Am scos o piesa neagra!!!");
				if (s+d >= 25) {
					scoaseB++;
					t.get(s).rmPiece();
					return true;
				}
			}
		}
		//daca nu scoatem piesa, atunci facem mutarea normala
		return move(s, d, c);
	}
	
	public boolean muta(int s, int d, int color, int casa) {
			if (color == 0) {
				if (casa + scoaseW == 15) {
				//	System.out.println("Sunt toate piesele in casa alba");
					return scoate(s, d, color);
				}
			}
			else {
				if (color == 1) {
					if (casa + scoaseB == 15) {
						//System.out.println("Sunt toate piesele in casa neagra");
						return scoate(s, d, color);
					}
				}
			}
			return move(s, d, color);
	}
	//TODO pe undeva nu stiu pe unde - sa nu uit sa verific mereu daca am piese de bagat pe tabla, chiar daca
	// am inceput sa scot din casa

	
	public int size () {
		return t.size();
	}
	
	public Prop get(int i) {
		return t.get(i);
	}
	
	
	public Tabla clone() {
		Tabla tabla = new Tabla();
		for (int i = 0; i < 26; i++) {
			tabla.t.set(i, new Prop(t.get(i).get_color(), t.get(i).get_pcs()));
		}
		tabla.scoaseB = scoaseB;
		tabla.scoaseW = scoaseW;
		for (int mutare : mutari) {
			tabla.mutari.add(mutare); ////????????????????????
		}
		return tabla;
	}
	/**
	 * 
	 * @param c - culoarea pentru care vrem sa vedem cate elemente sunt in casa
	 * @return - numarul de piese din casa culorii c
	 */
	public int inCasa(int c) {
		int nr = 0;
		if (c == 0) {
			for (int i = 1; i < 7; i++) {
				if (t.get(i).get_color() == c)
					nr += t.get(i).get_pcs();
			}
		}
		else {
			if (c == 1) {
				for (int i = 19; i < 25; i++) {
					if (t.get(i).get_color() == c)
						nr += t.get(i).get_pcs();
				}
			}
		}
		return nr;
	}
	/**
	 * 
	 * @param c - culoarea pentru care se calculeaza scorul
	 * @return - scorul tablei la momentul respectiv, pentru culoarea c
	 */
	public int score(int c) {
		return 0;
	}
	
	@Override
	public String toString() {
		String s="";
		for (int i = 0; i < t.size(); i++) {
			s += i + " " + t.get(i).toString();
			s += "\n";
		}
		return s;
	}
	
	
}
