import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;


public class Main {

	/**
	 * @param args: NT, fisIn, fisOut
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		/*
		 	NC - nr. cuvinte
		 	D - dimensiunea in octeti a fragmentelor in care se impart fisierele
		 	N - cate cuvinte(cele mai frecvente) se retin pentru document
		 	X - cate documente vreau sa primesc ca raspuns
		 	ND - nr ul de documente in care se va face cautarea
		 	cuvinte - cuvintele cheie cautate
		*/
		int NC, D, N, X, ND;
		String cuvinte[];
		ArrayList<String> documente = new ArrayList<String>();
		int NT = Integer.parseInt(args[0]);
		String fisIn = args[1];
		String fisOut = args[2];
		
		//citire din fisier
		try{
			  //Deschide fisierul pentru citire
			  FileInputStream fstream = new FileInputStream(fisIn);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Citeste NC
			  strLine = br.readLine();
			  NC = Integer.parseInt(strLine);
			  //Citeste cuvintele si le retine in vectorul cuvinte
			  strLine = br.readLine();
			  cuvinte = strLine.split(" ");
			  //Citeste D
			  strLine = br.readLine();
			  D = Integer.parseInt(strLine);
			  //Citeste N
			  strLine = br.readLine();
			  N = Integer.parseInt(strLine);
			  //citeste X
			  strLine = br.readLine();
			  X = Integer.parseInt(strLine);
			  //Citeste ND
			  strLine = br.readLine();
			  ND = Integer.parseInt(strLine);
			  //Citeste numele celor ND documente
			  for (int i = 0; i < ND; i++) {
				  strLine = br.readLine();
				  //System.out.println(strLine);
				  documente.add(strLine);
			  }
			  WorkPoolMap wpm = new WorkPoolMap(NT);
			  //pentru fiecare bloc din fiecare document, adaugam un work in  workpool
			  for (int i = 0; i < documente.size(); i++) {
				  String fisier = documente.get(i);
				  File f = new File(fisier);
				  long size = f.length();
				  int index = 0;
				  while (index < size) {
					  PartialSolutionMap psm = new PartialSolutionMap(fisier, index, D);
					  wpm.putWork(psm);
					  index += D;
				  }
			  }
			  MapWorker mp[] = new MapWorker[NT];
			  HashMap<String, ArrayList<HashMap<String, Integer>>> total = new HashMap<String, ArrayList<HashMap<String, Integer>>>();
			  for (int i = 0; i < NT; i++) {
				  mp[i] = new MapWorker(wpm, total);
				  mp[i].start();
			  }
			  for (int i = 0; i < NT; i++) {
				  mp[i].join();
			  }
			  /*inainte de a incepe prelucrarile, avem nevoie de un LinkedHashMap in care vom retine documentele
			   * in ordinea in care s-au citit din folder, pentru a le afisa in ordinea corecta la sfarsitul
			   * prelucrarii - aici vom retine cate dintre cuvintele cautate apar in fisier
			   */
			  LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
			  //populam lista
			  for (int i = 0; i < documente.size(); i++) {
				  lhm.put(documente.get(i), null);
			  }
			  //Incepem operatiile de tip reduce
			  //Pentru fiecare document, adaugam un work de tip reduce
			  WorkPoolReduce wpr = new WorkPoolReduce(NT);
			  for (Map.Entry<String, ArrayList<HashMap<String, Integer>>> entry : total.entrySet()) {
				  PartialSolutionReduce psr = new PartialSolutionReduce(entry.getKey(), entry.getValue(), cuvinte, lhm, null, 1);
				  wpr.putWork(psr);
			  }
			  ReduceWorker rw[] = new ReduceWorker[NT];
			  for (int i = 0; i < NT; i++) {
				  rw[i] = new ReduceWorker(wpr, N, X);
				  rw[i].start();
			  }
			  for (int i = 0; i < NT; i++) {
				  rw[i].join();
			  }
			  //parcurgem lhm si afisam documentele care ceva in ArrayList
			  int count = X;
			  //Afisam in fisier
			  FileWriter fstreamout = new FileWriter(fisOut);
			  BufferedWriter out = new BufferedWriter(fstreamout);
			  String mystring = "";
			  for (int i = 0; i < cuvinte.length; i++) {
				  mystring = mystring + cuvinte[i] + ", ";
			  }
			  mystring = mystring.substring(0, mystring.length() - 2);
			  out.write("Rezultate pentru: " + "(" + mystring + ")" + "\n");
			  for (Map.Entry<String, String> entry : lhm.entrySet()) {
				  String ald = entry.getValue();
				  if (count == 0)
					  break;
				  if (ald != null) {
					  out.write("\n" + entry.getKey()+" (");
					  String auxstring = entry.getValue();
					  auxstring = auxstring.substring(0,auxstring.length() - 2);
					  out.write(auxstring + ")");
					  count--;
				  }
			  }
			  out.close();
			  in.close();
	}catch (Exception e){//Catch exception if any
				  System.err.println("Error: " + e.getMessage());
		}
	
	}
}
