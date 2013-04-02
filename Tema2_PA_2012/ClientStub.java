import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;



public class ClientStub {
	
	//da ordinea corecta in care sa se faca mutarile. 
	public static ArrayList<Integer> correctOrder(ArrayList<Integer> zaruri, int c, Tabla t) {
		if (zaruri.size() == 1 || zaruri.size() >= 3 || zaruri.size() == 0) return zaruri;
		Iterator<Integer> it = zaruri.iterator();
		ArrayList<Integer> z = new ArrayList<Integer>();
		Tree tree = new Tree(t, c, 0, 0, c);
		int incasa = t.inCasa(c);
		int z1 = it.next();
		tree.fcopii(t, c, z1, incasa, c);
		Move m = tree.maxMove();
		//daca prima mutare nu se poate face, le interschimbam
		if (m == null) {
			it.remove();
			z.add(it.next());
			z.add(z1);
			//System.out.println("zaruri daca " + z1 + " este null" + z);
			return z;
		}
		System.out.println("zaruri: " + zaruri.toString());
		return zaruri;
	}
	
	public static void main(String args[]) {
		if (args.length < 4) {
			System.out.println("Usage: ./client server_hostname server_port opponent_level(1=dumb, 5, 7, 8) own_level(1=dumb, 5, 7, 8)");
			return;
		}
		// variabile pentru conexiune
		Socket socket = null;
		DataOutputStream out = null;
        DataInputStream in = null;

		try {
			// realizez conexiunea la server
			socket = new Socket(args[0], Integer.parseInt(args[1]));
			// scriu in out pe socket
			out = new DataOutputStream(socket.getOutputStream());
			// citesc din in de pe socket
            in = new DataInputStream(socket.getInputStream());
            
            // trimit primul mesaj - dificulatea adversarului
            byte[] message = new byte[1];
            message[0] = Byte.parseByte(args[2]);
            ConnectionExample.sendMessage(message, out);
            
            // primesc raspuns cu culoarea mea
            int c = -1;
            int c1 = -1;
            message = ConnectionExample.readMessage(in);
            if (message[0] == 1) {
            	// sunt jucatorul negru
            	c = 1;
            	c1 = 0;
            	System.out.println("Sunt jucatorul negru");
            } else if (message[0] == 0) {
            	c = 0;
            	c1 = 1;
            	System.out.println("Sunt jucatorul alb");
            } else {
            	// mesaj invalid; eroare!
            }
            //Tabla noastra
            Tabla tabla = new Tabla();
            while (true) {
            	message = ConnectionExample.readMessage(in);
            	// Primesti un mesaj de la server care contine miscarile adversarului si zarul tau 
            	// Daca sunteti primii la mutare, atunci nu primiti mutari din partea adversarului
            	// Daca adversarul nu a putut sa mute, atunci nu primiti mutari din partea adversarului
            	
            	// la un moment dat jocul se va termina
            	boolean gameOver = false;
            	if (message.length == 1) {
            		gameOver = true;
            		System.out.println("S-a terminat");
            	}
            	
            	if (gameOver){
            		break;
            	}
            	//numarul de piese din casa adversarului
            	int inCasa1 = tabla.inCasa(c1);
            	int l = message.length;
            	//executam mutarile oponentului, daca sunt
            	if (l > 2) {
            		for (int i = 0; i < l-2; i+=2) {
            			inCasa1 = tabla.inCasa(c1);
            			System.out.println(message[i] + " " + message[i+1]);
            			if (message[i] == 30) {
            				tabla.introduce(message[i+1], c1);
            			}
            			else
            				tabla.muta(message[i], message[i+1], c1, inCasa1);
            		}
            		System.out.println("Tabla bot");
            		System.out.println(tabla);
            	}
            	//ce zaruri am primit de la server
            	byte y = message[l - 1];
            	byte x = message[l - 2];
            	System.out.println("Zarul1: " + x);
            	System.out.println("Zarul2:" + y);
            	ArrayList<Integer> mutari = new ArrayList<Integer>();
            	//vectorul cu mutari pe care trebuie sa le facem, daca se poate
            	if (x == y) {
            		for (int i = 0; i < 4; i++) {
            			int nr = x;
            			mutari.add(nr);
            		}
            	}
            	else {
            		int nr1 = x;
            		int nr2 = y;
            		mutari.add(nr1);
            		mutari.add(nr2);
            	}
            	System.out.println("Cate mutari trebuie sa facem " + mutari.size());
            	//in response retinem mutarile pe care o sa le trimitem catre server
            	int[] response = new int[100];
            	int index = 0;
            	//introducem in joc toate piesele pe care le avem pe bara
            	int peBara = 0;
            	if (c == 1)
            		peBara = tabla.get(0).get_pcs();
            	else
            		peBara = tabla.get(25).get_pcs();
            	Iterator<Integer> it = mutari.iterator();
            	while (peBara > 0 && it.hasNext()) {
            		int m = it.next();
            		//daca introducem o piesa in joc
            		if (tabla.introduce(m, c)) {
            			System.out.println("introd de pe bara pe pozitia " + m);
            			it.remove();
            			peBara--;
            			response[index] = 30;
            			index++;
            			response[index] = m;
            			index++;
            		}
            	}
            	
            	//verificam daca avem toate piesele in casa. Daca da, atunci incepem sa scoatem si nu mai apelam functia de evaluare pentru ca nu mai este
            	//nevoie
            	int inCasa = tabla.inCasa(c);
            	System.out.println("Cate piese avem in casa: " + inCasa);
            	
            	//ok este 1 daca am dat dubla, adica toate val din mutari sunt egale
            	int ok = 1;
            	for (int i = 0; i < mutari.size()-1; i++) {
            		if (mutari.get(i) != mutari.get(i+1))
            			ok = 0;
            	}
            	inCasa = tabla.inCasa(c);
            	if (c == 0) {
            		inCasa += tabla.getScoaseW();
            	}
            	else
            		inCasa += tabla.getScoaseB();
            	
            	//daca nu mai avem piese pe bara
            	int size = 0;
            	if (peBara == 0) {	
            		mutari = correctOrder(mutari, c, tabla);
            		size = mutari.size();
            		System.out.println("Mutari" + mutari);
            		//generam mutarile cu expectiminimax
            		Evaluate ev = new Evaluate();
            		//in tabla aceasta pastram mutarile pe care o sa le facem
            		Tabla rezultat = new Tabla();
            		//se genereaza ce mutari ar trebui sa facem
            		double score = ev.expectiminimax(tabla, 3, mutari, c, rezultat);
            		System.out.println("Scorul "  + score);
            		System.out.println("REZULTAT: " + rezultat.mutari);
                    	//Iterator<Integer> it_mutari = rezultat.mutari.iterator();
                    	//iau ultimele 2*size mutari din ce mi-a returnat minimax
                    	ArrayList<Integer> noi_mutari = new ArrayList<Integer>();
                    	int n = rezultat.mutari.size();
                    	System.out.println("Numar de elemente: " + n);
                    	if (rezultat.mutari.size() != 0) 
                    		for (int j = n - 2*size; j < n; j++) {
                    			noi_mutari.add(rezultat.mutari.get(j));
                    		}
                    	Iterator<Integer> it_mutari = noi_mutari.iterator();
                    	//if (move != null) {
                    	while (it_mutari.hasNext()) {
                    		inCasa = tabla.inCasa(c);
                    		int start = it_mutari.next();
                    		it_mutari.remove();
                    		int dist = it_mutari.next();
                    		it_mutari.remove();
                    		tabla.muta(start, dist, c, inCasa);
                    		response[index] = start;
                    		index++;
                    		response[index] = dist;
                    		index++;
                    		System.out.println("Am mutat de pe pozitia " + start + " distanta " + dist );
                    	}
                    		//}
            		}
            	System.out.println("Tabla dupa mutarea mea");
            	System.out.println(tabla);
            	for (int i = 0; i < 10; i++) {
            		System.out.print(response[i]);
            	}
            	System.out.println();
            	
   
            	System.out.println("Mutarile bune");
            	for (int j = 0; j < index; j++) {
            		System.out.println(response[j]);
            	}
            	
            	byte[] yourResponse = new byte[index];
            	
            	for (int j = 0; j < index; j++) {
            		yourResponse[j] = Byte.parseByte(String.valueOf(response[j]));
            		System.out.println(yourResponse[j]);
            	}
            	ConnectionExample.sendMessage(yourResponse, out);
            }
            socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}
