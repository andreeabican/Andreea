import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;


public class Graf {
	/**
	 * 
	 * @param v - vectorul in care am retinut pixelii din imagine
	 * @param cols - numarul de coloane din matricea imaginii
	 * @param t -treshold
	 * @return - un graf ca cel descris in enuntul temei
	 */
	public static HashMap<Integer, Vector<Continut>> graf(Vector<Integer> v, int cols, int t, int l) {
		int n = v.size();
		Param paramBg = Parametrii.calcul("mask_bg.pgm", v);
		Param paramFg = Parametrii.calcul("mask_fg.pgm", v);
		double ufg = paramFg.u, ubg = paramBg.u, sfg = paramFg.s, sbg = paramBg.s;
		//retinem graful cu liste de vecini
		HashMap<Integer, Vector<Continut>> g = new HashMap<Integer, Vector<Continut>>();
		//adaugam si sursa si drena in hashMap
		g.put(n, new Vector<Continut>());
		g.put(n+1, new Vector<Continut>());
		for (int i = 0; i < n; i++) {
			int pixel = v.get(i);
			g.put(i, new Vector<Continut>());
			double capacS = fui(1, i, ufg, ubg, sfg, sbg, v);
			double capacD = fui(0, i, ufg, ubg, sfg, sbg, v);
			//Adaugam sursa
			g.get(i).add(new Continut(capacS, 0, n, 0, pixel));
			g.get(n).add(new Continut(capacS, 0, i, pixel, 0));
			//Adauga drena
			g.get(i).add(new Continut(capacD, 0, n+1, 0, pixel));
			g.get(n+1).add(new Continut(capacD, 0, i, pixel, 0));
		}
		
		//vecini
		int pixelVecin, s, d, vecin;
		
		//adaugam nodurile normale
		for (int i = 0; i < n; i++) {
			int valPixel = v.get(i);
			Continut c;
			if (i%cols != cols-1) {
				//System.out.println("Nimic");
				vecin = i+1;
				pixelVecin = v.get(vecin);
				if (Math.abs(valPixel - pixelVecin) <= t) {
					c = new Continut(l, 0, i+1, pixelVecin, valPixel);
					g.get(i).add(c);
				}
			}
			if (i%cols != 0) {
				vecin = i - 1;
				pixelVecin = v.get(vecin);
				if (Math.abs(valPixel - pixelVecin) <= t) {
					c = new Continut(l, 0, i-1, pixelVecin, valPixel);
					g.get(i).add(c);
				}
			}
			if (i + cols < n) {
				vecin = i + cols;
				pixelVecin = v.get(vecin);
				if (Math.abs(valPixel - pixelVecin) <= t) {
					c = new Continut(l, 0, i+cols, pixelVecin, valPixel);
					g.get(i).add(c);
				}
			}
			if ( i - cols >= 0) {
				vecin = i - cols;
				pixelVecin = v.get(vecin);
				if (Math.abs(valPixel - pixelVecin) <= t) {
					c = new Continut(l, 0, i-cols, pixelVecin, valPixel);
					g.get(i).add(c);
				}
			}
		}
		//System.out.println(g);
		return g;
	}
	
//====================================================================================================================================
	
	/**
	 * 
	 * @param g - graful
	 * @param s - sursa
	 * @param d	- drena
	 * @return - drumul de la s la d
	 */
	public static Vector<Integer> bfs(HashMap<Integer, Vector<Continut>> g, int s, int d) {
		  int n = g.size();
		  int[] parents = new int[n];
		  for (int i = 0; i < n; i++) {
			  parents[i] = -2;
		  }
		  Vector<Integer> drum = new Vector<Integer>();
		  Queue<Integer> q = new LinkedList<Integer>();
		  q.add(s);
		  parents[s] = -1;
		  int node = -1;
		  boolean ok = false;
		  while (q.size() != 0) {
			  node = q.poll();
			  if (node == d) {
				  ok = true;
				  break;
			  }
			  //luam toti vecinii sursei
			  Vector<Continut> v = g.get(node);  
			  for (int i = 0; i < v.size(); i++) {
				  Continut c = v.get(i);
				  int vecin = c.v;
				  //daca nodul nu a fost deja adaugat
				  if (parents[vecin] == -2 && c.c > 0) {
					 //il adaugam in coada
					  q.add(vecin);
					  //il marcam ca adaugat
					  parents[vecin] = node;
				  }
			  }
		  }
		  if (ok == false) {
			  return new Vector<Integer>();
		  }
		int parent = parents[node];
		drum.add(node);
		while(parent != -1) {
			drum.add(parent);
			parent = parents[parent];
		}
		Vector<Integer> drum1 = new Vector<Integer>();
		for (int i = drum.size()-1; i >= 0; i--) {
			drum1.add(drum.get(i));
		}
		return drum1;
		
	}

//====================================================================================================================================
	/**
	 * 
	 * @param g - graful
	 * @param path - drumul care trebuie saturat
	 * @return - fluxul maxim pe acel drum
	 */
	public static double saturatePath(HashMap<Integer, Vector<Continut>> g, Vector<Integer> path) {
		//parcurgem tot drumul determinat cu BFS si calculam fluxul maxim pe acel drum
		int n = path.size();
		int s = path.get(0);
		//in c retin toate elem de tip continut care fac parte din drum,
		//pentru ca dupa ce determin maxf, sa pot sa scad maxf din
		//capacitatile acestora
		Vector<Continut> c = new Vector<Continut>();
		//in vecinii sursei caut nodul vecin cu sursa
		Continut continut = vecin(g.get(s), path.get(1));
		//in vecinii nodului vecin cu sursa, caut sursa
		Continut continut1 = vecin(g.get(path.get(1)), s);
		c.add(continut);
		c.add(continut1);
		//initializam maxf cu capacitatea primei muchii din drum
		double maxf = continut.c;
		//parcurg toate celelalte noduri din path si pentru fiecare muchie
		//verific ce capacitate are, iar daca este mai mica decat maxf, updatez
		//maxf cu noua valoare
		for (int i = 1; i < n-1; i++) {
			int n1 = path.get(i), n2 = path.get(i+1);
			continut = vecin(g.get(n1), n2);
			continut1 = vecin(g.get(n2), n1);
			if (continut.c < maxf) {
				maxf = continut.c;
			}
			c.add(continut);
			c.add(continut1);
		}
		//saturez drumul???
		for (Continut cont : c) {
			cont.c -= maxf;
		}
	//	System.out.println("MaxFlowmereu: " + maxf);
		return maxf;
	}

//====================================================================================================================================
	/**
	 * 
	 * @param v - vector de vecinii ai unui nod
	 * @param nod - nodul pe care il cautam in vector
	 * @return - elementul din vector care este corespunzator nodului nod
	 */
	public static Continut vecin(Vector<Continut> v, int nod) {
		for ( Continut c : v) {
			if (c.v == nod) {
				return c;
			}
		}
		return null;
	}
	
//====================================================================================================================================
	/**
	 * Determina fluxul maxim in graf
	 * @param g - graful
	 * @param s - sursa
	 * @param d - drena
	 * @return - fluxul maxim
	 */
	public static double maxFlow(HashMap<Integer, Vector<Continut>> g, int s, int d) {
		double maxf = 0;
		Vector<Integer> drum;
		
		while (true) {
			drum = bfs(g,s,d);
			if (drum.size() == 0) {
				return maxf;
			}
			else
				maxf += saturatePath(g, drum);
		}
	}
//====================================================================================================================================
	/**
	 * 
	 * @param x - valoarea pentru care se calculeaza functia
	 * @param i - al catelea pixel din imagine
	 * @param ufg - miu foreground
	 * @param ubg - miu background
	 * @param sfg - sigma foreground
	 * @param sbg - sigma background
	 * @param v - vectorul in care sunt retinuti pixelii din imagine
	 * @return - valoarea functiei
	 */
	public static double fui(int x, int i, double ufg, double ubg, double sfg, double sbg, Vector<Integer> v) {
		double f = 0;
		int pixel = v.get(i);
		//System.out.println("Pixelul este: " + pixel);
		//System.out.println(ufg + " " + sfg);
		f = x*(0.5*Math.pow(((pixel - ufg)/sfg), 2) + Math.log(Math.sqrt(2*Math.PI*sfg*sfg)));
		f += (1 - x) * (0.5*Math.pow((pixel - ubg)/sbg, 2) + Math.log(Math.sqrt(2*Math.PI*sbg*sbg)));
		
		return Math.min(f, 10);
	}
	
//====================================================================================================================================

	public static void bfsCut(HashMap<Integer, Vector<Continut>> g, int s, int cols, int lines) {
		  int n = g.size();
		  int[] parents = new int[n];
		  for (int i = 0; i < n; i++) {
			  parents[i] = -2;
		  }
		  Queue<Integer> q = new LinkedList<Integer>();
		  q.add(s);
		  parents[s] = -1;
		  int node = -1;
		  //boolean ok = false;
		  while (q.size() != 0) {
			  node = q.poll();
			  //luam toti vecinii nodului pe care ne aflam
			  Vector<Continut> v = g.get(node);  
			  for (int i = 0; i < v.size(); i++) {
				  Continut c = v.get(i);
				  int vecin = c.v;
				  //daca nodul nu a fost deja adaugat
				  if (parents[vecin] == -2 && c.c > 0) {
					 //il adaugam in coada
					  q.add(vecin);
					  //il marcam ca adaugat
					  parents[vecin] = node;
				  }
			  }
		  }
		  
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter("out.pgm"));
			bf.write("P2\n");
			bf.write(cols + " " + lines + "\n");
			bf.write(255 + "\n");
			for (int i = 0; i < n-2; i++) {
				if (parents[i] != -2) {
					bf.write("0");
					bf.write(" \n");
				}
				else {
					bf.write("255");
					bf.write(" \n");
				}
			}
			bf.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
}
