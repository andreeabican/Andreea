import java.io.*;
import java.util.*;

/**
 * 
 * @author Simona Badoiu
 *
 */
public class Main {
	public static void main(String[] args) {
		//citire din dictionar, am creat un map in care cheia reprezinta lungimea cuvintelor care sunt retinute in vectorul din value
		// in vactorul din value, retinem elemente de tip Cuvant.
		Map<Integer, ArrayList<Cuvant>> map = new TreeMap<Integer, ArrayList<Cuvant>>();
		
		try {
			FileInputStream f = new FileInputStream("dict.txt");
			DataInputStream in = new DataInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String s;
			Cuvant c;
			String[] str;
			while ((s = br.readLine()) != null) {
				str = s.split(" ");
				int l = str[0].length();
				c = new Cuvant(Integer.parseInt(str[1]), str[0]);
				if (map.containsKey(l)) {
					map.get(l).add(c);
				}
				else {
					ArrayList<Cuvant> cuvinte = new ArrayList<Cuvant>();
					cuvinte.add(c);
					map.put(l, cuvinte);
				}
					
			}
			in.close();
		}
		catch (Exception e) {
			System.err.println("Error: " +  e.getMessage());
		}
		
		Scanner sc = new Scanner(System.in);
		String s = sc.nextLine();
		//long t = System.nanoTime();	
		System.out.println(Prelucrari.prelProp(map, s));
		//long t1 = System.nanoTime() - t;
		//System.out.println(t1/1000000);
	}
}
