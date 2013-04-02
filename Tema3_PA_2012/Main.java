import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;
import java.util.Map.Entry;


public class Main {
	private static final long MEGABYTE = 1024L * 1024L;

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}
	public static void main(String[] args) {
		//long t1 = System.currentTimeMillis();
		int n = 0;
		int lines = 0, cols = 0, maxVal = 0;
		Vector<Integer> v = new Vector<Integer>();
		//citire imagine=========================================================================
		try {
			FileInputStream fstream = new FileInputStream("imagine.pgm");
			Scanner sc = new Scanner(fstream);
			int x;
			//citeste prima linie, care nu este importanta
			sc.nextLine();
			cols = sc.nextInt();
			lines = sc.nextInt();
			maxVal = sc.nextInt();
			//System.out.println("Pentru imagine");
			while (sc.hasNext()) {
				x = sc.nextInt();
				v.add(x);
				//System.out.println(x);
			}
		fstream.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		//Calculare valori u si sigma========================================================================
		Param paramBg = Parametrii.calcul("mask_bg.pgm", v);
		Param paramFg = Parametrii.calcul("mask_fg.pgm", v);
		//System.out.println("Parametrii bg");
		//System.out.println(paramBg.u + " " + paramBg.s);
		//System.out.println("Parametrii fg");
		//System.out.println(paramFg.u + " " + paramFg.s);
		
		//Citire lambda si treshold=========================================================================
		int lam = 0, th = 0;
		try {
			FileInputStream fstream = new FileInputStream("parametri.txt");
			Scanner sc = new Scanner(fstream);
			lam = sc.nextInt();
			th = sc.nextInt();
			
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		
		//Determinare graf==================================================================================
		HashMap<Integer, Vector<Continut>> g = Graf.graf(v, cols, th, lam);
		
		n = g.size()-2;		
		
		//Calculare maxFlow===================================================================================
		double maxf = Graf.maxFlow(g, n, n+1);
		System.out.println("MaxFlow: " + maxf);
		
		//Afisare in fisier in functie de minCut==============================================================
		//System.out.println(n);
		Graf.bfsCut(g, n, cols, lines);
		//System.out.println("timpul: " + (System.currentTimeMillis() - t1));
		
		// Get the Java runtime
		//Runtime runtime = Runtime.getRuntime();
		// Run the garbage collector
		//runtime.gc();
		// Calculate the used memory
		//long memory = runtime.totalMemory() - runtime.freeMemory();
		//System.out.println("Used memory is bytes: " + memory);
		//System.out.println("Used memory is megabytes: "
			//	+ bytesToMegabytes(memory));

	}	
}
