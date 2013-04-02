import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Vector;

/**
 * 
 * @author Simona Badoiu
 * Contine functii care calculeaza paramterii u si sigma
 */
public class Parametrii {
	
	/**
	 * 
	 * @param fisier - numele fisierului din care vrem sa citim
	 * @return - parametrii u si sigma
	 */
	public static Param calcul(String fisier, Vector<Integer> v) {
		double u = 0;
		double s = 0;
		int nrNenule = 0;
		Vector<Integer> v1 = new Vector<Integer>();
		try {
			FileInputStream fstream = new FileInputStream(fisier);
			Scanner sc = new Scanner(fstream);
			int x;
			//citeste prima linie, care nu este importanta
			sc.nextLine();
			int linii = sc.nextInt();
			int col = sc.nextInt();
			int maxVal = sc.nextInt();
			int i = 0;
			while (sc.hasNext()) {
				x = sc.nextInt();
				if (x != 0) {
					//pastram indicele pentru care pixelul era nenul. Il vom folosi pentru calculul sigma
					v1.add(i);
					//incepem sa calculam u
					u += v.get(i);
					nrNenule++;
				}
				i++;
			}
		fstream.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		//valoare finala u
		u = u/nrNenule;
		for (int i = 0; i < nrNenule; i++) {
			s += Math.pow(u - v.get(v1.get(i)), 2);
		}
		s = Math.sqrt(s/nrNenule);
		return new Param(u, s);
	}
	
}
