import java.util.ArrayList;


public class Main {
	public static void main(String[] args) {
		Tabla tabla = new Tabla();
		Evaluate ev = new Evaluate();
		ArrayList<Integer> zaruri = new ArrayList<Integer>();
		zaruri.add(4);
		zaruri.add(5);
		Tabla rezultat = new Tabla();
		long t = System.currentTimeMillis();
		double score = ev.expectiminimax(tabla, 3, zaruri, 0, rezultat);
		long t1 = System.currentTimeMillis();
		System.out.println("Timpul: " + (t1 - t));
		System.out.println("Scorul este: " + score);
		System.out.println("rezultatul " + rezultat.mutari);
	}
}
