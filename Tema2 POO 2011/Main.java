import java.util.*;
public class Main {
	public enum comenzi{
		read, write, create, mkdir, delete, rmdir, execute, ls, groupadd, groupdel, useradd, usermod, chmod;
	}
	public enum tipuriFis{
		sursa, html, mp3, tex, zip, txt, obiect, executabil;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] aux = null; //se retine in aux vectorul obtinut din comanda.split la grupuri
		String nume_grup, nume_utiliz; // numele grupului primit de la tastatura + numele utiliz.
										// pentru comenzile care vizeaza grupuririle si pentru chmod
		String grupg = ""; //grupul in care se afla userul care a dat comanda
		String comanda, cale, continut="", user; // le folosesc pe parcurs, in aproape fiecare comanda
		//numele ultimului element din calea catre fisier
		String ultim;
		AFisier f; //un fisier/director la care am ajuns dupa ce am parcurs calea primita de la tastatura
		
		//Am creat o multime in care se retin toate comenzile din enum. Pe baza acestui set voi verifica daca s-a dat
		//o comanda valida sau nu
		HashSet<String> values = new HashSet<String>();
		for (comenzi c : comenzi.values()) {
		      values.add(c.name());
		  }
		
		// multime in care pastrez toate tipurile de fisiere din enum. Pe baza acestui set voi verifica daca
		// tipul fisierului exista sau nu
		HashSet<String> tipuriF = new HashSet<String>();
		for (tipuriFis t : tipuriFis.values()) {
			tipuriF.add(t.name());
		}
		
		//Citire comanda de la System.in
		Scanner sc = new Scanner(System.in);
		String str;
		str = sc.nextLine();
		//am creat o instanta a clasei comanda... in aceasta am creat mai multe functii care prelucreaza comanda
		//primita de la tastatura
		Comanda com = new Comanda();
		//Crearea radacinii/directorului root
		ContentDir c = new ContentDir();
		Director root = new Director("root", "Director", "", "", c, null);
		//la root are toata lumea acces
		root.dall = "rwx";
		//In acest Map, cheile sunt utilizatorii care au creat cel putin un grup,
		// iar valorile din Map reprezinta vectori care contin numele grupurilor pe care le-a creat
		//pana acum utilizatorul al carui nume il gasim in cheie
		Map<String, ArrayList<String>> utilizatori = new HashMap<String, ArrayList<String>>();
		//In acest map cheile sunt grupurile, iar in ArrayList gasim toti utilizatorii care apartin grupului
		//respectiv(primul utilizator din vector este owner)
		Map<String, ArrayList<String>> grupuri = new HashMap<String, ArrayList<String>>();
		
		//Inceput bucla care se termina odata cu comanda quit
		while (!str.equals("quit")) {
			comanda = com.actiune(str);
			// in co pastram comanda care s-a dat de la tastatura si pe care am determinat-o mai sus
			if (values.contains(comanda)) {
				comenzi co = comenzi.valueOf(comanda);
				//parcurgere arbore SGF si executare comanda
				switch (co) {
				case read:
					//determinare user, cale, parcurgere arbore fisiere pana la penultimul din cale
					user = com.user(str);
					cale = com.cale(str);
					if (cale == null) {
						System.err.println("Eroare");
						break;
					}
					if (cale.equals("/")) {
						System.err.println("Eroare");
						break;
					}
					f = com.fisDest(root, cale);
					ultim = com.ultim(cale);
					// daca toate directoarele din cale(pana la penultimul) exista, atunci verificam daca exista
					//si ultimul element din cale, daca este fisier si nu este executabil... daca da atunci afisam continutul
					if (f != null) {
					if (((Director)f).content.c.containsKey(ultim)) {
						f = ((Director)f).content.c.get(ultim);
						// Daca f nu este director si f nu este executabil
						if (f.type != null)
							//verific drepturile
							if ((f.dall.contains("r")) || user.equals("root") || (user.equals(f.owner_user) && f.duser.contains("r")) || (grupuri.containsKey(f.owner_group) && grupuri.get(f.owner_group).contains(user) && f.dgrup.contains("r")))
								if (!f.type.equals("") && !f.type.equals("executabil")) {
									System.out.println(f.type + " " + ((Fisier)f).content);
									}
								else
									System.err.println("Eroare"); //este director sau este executabil
							else System.err.println("Eroare"); //Eroare drepturi
						else System.err.println("Eroare"); //Nu exista un fisier/director in cale
						}
					else {
						System.err.println("Eroare"); //Nu exista
						break;
						}
					}
					else {
						System.err.println("Eroare"); //nu exista directorul
						break;
					}
					break;
				case write:
					user = com.user(str);
					cale = com.cale(str);
					if (cale == null) {
						System.err.println("Eroare"); //Eroare nu s-a precizat calea
						break;
					}
					if (cale.equals("/")) {
						System.err.println("Eroare"); //Eroare este "/"
						break;
					}
					// f - penultimul fisier din cale din cale(trebuie sa fie director)
					f = com.fisDest(root, cale);
					// ultimul element din cale - reprezinta fisierul in care trebuie sa scriem
					ultim = com.ultim(cale);
					// daca ultimul exista in directorul f, atunci avansam, f = fisierului cu numele din ultim
					if (f != null) {
						if (((Director)f).content.c.containsKey(ultim)) {
						f = ((Director)f).content.c.get(ultim);
						//if (f.type != null)
							if ((f.dall.contains("w")) || user.equals("root") || (user.equals(f.owner_user) && f.duser.contains("w")) || (grupuri.containsKey(f.owner_group) && grupuri.get(f.owner_group).contains(user) && f.dgrup.contains("w")))
								// Daca f NU este director si f NU este executabil atunci putem scrie in acesta
								if (!f.type.equals("") && !f.type.equals("executabil")) {
									if (com.continut(str) != null)
										((Fisier)f).content = com.continut(str);
									else
										System.err.println("Eroare"); //Eroare continut
									}
								else
									System.err.println("Eroare"); //este director sau este executabil
							else System.err.println("Eroare"); //Eroare drepturi
						}
						else {
							System.err.println("Eroare"); //nu exista
							break;
						}
					}
					else System.err.println("Eroare"); //Eroare - nu exista unul dintre fisierele din cale
					break;
				case create:
					user = com.user(str); // utilizatorul care a dat comanda
					if (com.type(str) == null || user == null) {
						System.err.println("Eroare");
						break;
					}
					cale = com.cale(str);
					if (cale == null) {
						System.err.println("Eroare"); //nu s-a precizat calea
						break;
					}
					if (cale.equals("/")) {
						System.err.println("Eroare"); //Este vorba de directorul /
						break;
					}
					// avansam pana la penultimul fisier din cale(trebuie sa fie director)
					f = com.fisDest(root, cale);
					ultim = com.ultim(cale);
					//daca tipul fisierului nu exista in cele pe care le asteptam se intoarce eroare
					if (!tipuriF.contains(com.type(str))) {
						System.err.println("Eroare"); //eroare tip fisier
						break;
					}
					if (com.continut(str) != null && !com.type(str).equals("executabil")) 
						continut = com.continut(str);
					else
						if (com.continut(str) == null && !com.type(str).equals("executabil"))
							continut = "";
					// daca fisierul este executabil si s-a precizat continutul in comanda => Eroare
					if (com.type(str).equals("executabil") && com.continut(str) != null) {
							System.err.println("Eroare"); //Eroare continut
							break;
						}
					else
						if (com.type(str).equals("executabil") && com.continut(str) == null) 
							continut = null;

					grupg = "";
					//daca utilizatorul este owner pentru cel putin un grup, in grupg retinem numele acestui grup
					if (utilizatori.containsKey(user) && !utilizatori.get(user).isEmpty())
						grupg = utilizatori.get(user).get(0);
					// daca toate directoarele pana la penultimul din cale exista
					if (f != null) {
						// verificam daca utilizatorul are drepturi asupra directorului in care vrea sa creeze
						if ((f.dall.contains("w")) || user.equals("root") || (user.equals(f.owner_user) && f.duser.contains("w")) || (grupuri.containsKey(f.owner_group) && grupuri.get(f.owner_group).contains(user) && f.dgrup.contains("w")))  {
							if (!((Director)f).content.c.containsKey(ultim))
								((Director)f).content.c.put(ultim, new Fisier(ultim, com.type(str), user, grupg, continut, f));
							else {
									System.err.println("Eroare"); //deja exista
									break;
								}
						}
						else System.err.println("Eroare"); //eroare permisiuni
					}
					else System.err.println("Eroare"); //eroare cale
					break;
				case mkdir:
					user = com.user(str); // utilizatorul care a dat comanda
					cale = com.cale(str); // calea catre director
					if (cale == null) {
						System.err.println("Eroare"); //nu s-a precizat calea
						break;
					}
					if (cale.equals("/")) {
						System.err.println("Eroare"); // nu se poate crea directorul /
						break;
					}
					// f = penultimul director din cale
					f = com.fisDest(root, cale);
					// numele ultimului director din cale
					ultim = com.ultim(cale);
					// stabilim grupul pentru care user este owner(daca este owner)
					grupg = "";
					if (utilizatori.containsKey(user) && !utilizatori.get(user).isEmpty())
						grupg = utilizatori.get(user).get(0);
					if (f != null) {
						// verificare drepturi
						if ((f.dall.contains("w")) || user.equals("root") || (user.equals(f.owner_user) && f.duser.contains("w")) || (grupuri.containsKey(f.owner_group) && grupuri.get(f.owner_group).contains(user) && f.dgrup.contains("w"))) {
							// daca fisierul nu exista deja
							if (!((Director)f).content.c.containsKey(ultim)) { 
								((Director)f).content.c.put(ultim, new Director(ultim, "", com.user(str), grupg, new ContentDir(), f));
							}
							else {
								System.err.println("Eroare"); //Exista deja
								break;
							}
						}
						else {
							System.err.println("Eroare"); //Eroare permisiuni
						}
					}
					else
					System.err.println("Eroare"); //comanda mkdir nu a putut fi executata - eroare cale
					break;
				case delete:
					user = com.user(str);
					cale = com.cale(str);
					// Daca nu s-a precizat calea catre fisier, se intoarce eroare
					if (cale == null) {
						System.err.println("Eroare"); // nu s-a precizat calea
						break;
					}
					if (cale.equals("/")) {
						System.err.println("Eroare"); // nu se poate sterge directorul /
						break;
					}
					// f = penultimul fisier din cale(trebuie sa fie director)
					f = com.fisDest(root, cale);
					// numele fisierului care urmeaza sa fie sters
					ultim = com.ultim(cale);
					if (f != null) {
						// verificam daca utilizatorul are drepturi asupra directorului in care vrea sa creeze
						if ((f.dall.contains("w")) || user.equals("root") || (user.equals(f.owner_user) && f.duser.contains("w")) || (grupuri.containsKey(f.owner_group) && grupuri.get(f.owner_group).contains(user) && f.dgrup.contains("w")))  {
							if (((Director)f).content.c.containsKey(ultim)) {
								// f = fiserului cu numele continut in ultim
								f = ((Director)f).content.c.get(ultim);
								// daca user are drepturi de scriere asupra acestui fisier, atunci il stergem
								// din contentul parintelui sau f.parinte
								if ((f.dall.contains("w")) || user.equals("root") || (user.equals(f.owner_user) && f.duser.contains("w")) || (grupuri.containsKey(f.owner_group) && grupuri.get(f.owner_group).contains(user) && f.dgrup.contains("w")))  {
									// stergerea are loc doar daca este fisier, pentru director exista rmdir
									if (!f.type.equals("")) {
										((Director)f.parinte).content.c.remove(f.name);
									}
									else System.err.println("Eroare"); //nu este fisier
								}
								else System.err.println("Eroare"); //permisiuni
							}
							else System.err.println("Eroare"); //nu exista fisierul
						}
						else System.err.println("Eroare"); //permisiuni director
					}
					else System.err.println("Eroare"); //eroare parcurgere cale
					break;
				case rmdir:
					// la fel ca la delete - singura diferenta este ca il stergem doar daca este director
					user = com.user(str);
					cale = com.cale(str);
					if (cale == null) {
						System.err.println("Eroare"); //nu s-a precizat calea
						break;
					}
					if (cale.equals("/")) {
						System.err.println("Eroare");
						break;
					}
					f = com.fisDest(root, cale);
					ultim = com.ultim(cale);
					if (f != null) {
						// verificam daca utilizatorul are drepturi asupra directorului in care vrea sa creeze
						if ((f.dall.contains("w")) || user.equals("root") || (user.equals(f.owner_user) && f.duser.contains("w")) || (grupuri.containsKey(f.owner_group) && grupuri.get(f.owner_group).contains(user) && f.dgrup.contains("w")))  {
							if (((Director)f).content.c.containsKey(ultim)) {
								f = ((Director)f).content.c.get(ultim);
								if ((f.dall.contains("w")) || user.equals("root") || (user.equals(f.owner_user) && f.duser.contains("w")) || ( grupuri.containsKey(f.owner_group) && grupuri.get(f.owner_group).contains(user) && f.dgrup.contains("w")))  {
									if (f.type.equals("")) {
										((Director)f.parinte).content.c.remove(f.name);
									}
									else System.err.println("Eroare"); // nu este fisier
								}
								else System.err.println("Eroare"); //permisiuni fisier
							}
							else System.err.println("Eroare"); // nu exista fisierul
						}
						else System.err.println("Eroare"); //permisiuni director
					}
					else System.err.println("Eroare"); //eroare cale
					break;
				case execute:
					// asemanator cu read, doar ca in loc sa se afiseze continutul, se afiseaza "Fisierul a fost
					// executat cu succes" - asta doar daca fisierul este executabil
					user = com.user(str);
					cale = com.cale(str);
					if (cale == null) {
						System.err.println("Eroare"); //nu s-a precizat calea
						break;
					}
					if (cale.equals("/")) {
						System.err.println("Eroare");
						break;
					}
					f = com.fisDest(root, cale);
					ultim = com.ultim(cale);
					if (f != null) {
						if (((Director)f).content.c.containsKey(ultim)) {
							f = ((Director)f).content.c.get(ultim);
							if ((f.dall.contains("x")) || user.equals("root") || (user.equals(f.owner_user) && f.duser.contains("x")) || (grupuri.containsKey(f.owner_group) && grupuri.get(f.owner_group).contains(user) && f.dgrup.contains("x"))) {
								if (f.type.equals("executabil")) {
									System.out.println("Fisierul a fost executat cu succes");
								}
								else
									System.err.println("Eroare"); //eroare executie
							}
							else
								System.err.println("Eroare"); //eraore drepturi
						}
						else
							System.err.println("Eroare"); //fisierul nu exista
					}
					else
						System.err.println("Eroare"); //eraore cale
					break;
				case ls:
					user = com.user(str);
					cale = com.cale(str);
					if (cale == null) {
						System.err.println("Eroare"); //nu s-a precizat calea
						break;
					}
					if (cale.equals("/")) {
						f = root;
						if (!((Director)f).content.c.isEmpty()) {
							System.out.println(((Director)f).content);
						}
						break;
					}
					f = com.fisDest(root, cale);
					ultim = com.ultim(cale);
					if (f != null) {
						if (((Director)f).content.c.containsKey(ultim)) {
							f = ((Director)f).content.c.get(ultim);
							if (f.type == "") {
								if ((f.dall.contains("x")) || user.equals("root") || (user.equals(f.owner_user) && f.duser.contains("x")) || (grupuri.containsKey(f.owner_group) && grupuri.get(f.owner_group).contains(user) && f.dgrup.contains("x"))) {
									if (!((Director)f).content.c.isEmpty()) {
										System.out.println(((Director)f).content);
									}
								}
								else System.err.println("Eroare"); //Eroare permisiuni
							}
							else
								System.err.println("Eroare"); //nu este director
						}
						else System.err.println("Eroare"); //nu exista directorul
					}
					else 
						System.err.println("Eroare"); //Eroare cale
					break;
				case groupadd:
					user = com.user(str);
					aux = str.split(" ");
					if (aux.length == 3) {
						nume_grup = aux[2]; // numele grupului care urmeaza sa fie adaugat
						if (grupuri.containsKey(nume_grup)) {
							System.err.println("Eroare"); //grupul deja exista
							break;
								}
						else {
							// primul utilizator din vectorul de utilizatori este owner
							ArrayList<String> users = new ArrayList<String>(); 
							users.add(user);
							grupuri.put(nume_grup, users);
							// modificare map utilizatori - adaugam grupul pe care l-a creat utilizatorul
							//in ArrayList ul de grupuri al acestuia
							if (utilizatori.containsKey(user)) {
								utilizatori.get(user).add(nume_grup);
							}
							else {
								// daca userul nu exista inca in map, se adauga un nou user in mapul utilizatori
								ArrayList<String> groups = new ArrayList<String>();
								groups.add(nume_grup);
								utilizatori.put(user, groups);
							}
						}
					}
					break;
				case groupdel:
					user = com.user(str);
					aux = str.split(" ");
					if (aux.length == 3) {
						nume_grup = aux[2]; // numele grupului pe care vrem sa il stergem
						if (!grupuri.containsKey(nume_grup)) {
							System.err.println("Eroare"); //grupul nu exista
							break;
						}
						else
							// daca grupul are cel putin un user care il detine, sau daca utilizatorul este root
							if (utilizatori.containsKey(user) || user.equals("root"))
								if (user.equals("root") || utilizatori.get(user).contains(nume_grup)) {
									nume_utiliz = user; // initial utilizatorul din al carui array trebuie sa
														// stergem grupul este user, dar daca cel care da comanda
														// este root, trebuie sa determinam utilizatorul
														// care detine grupul
									if (user.equals("root"))
										nume_utiliz = grupuri.get(nume_grup).get(0);
									utilizatori.get(nume_utiliz).remove(0); //Stergem primul grup din lista de grupuri
																			//la care a fost adaugat pana acum utiliz. user
									//daca dupa ce am sters primul grup, inca mai avem grupuri in vector, grupg = numele grupului cu care
									// inlocuim numele grupului pe care l-am sters
									if (utilizatori.get(nume_utiliz).size() != 0) 
										grupg = utilizatori.get(nume_utiliz).get(0);
									// daca nu mai exista grupuri pe care nume_utiliz le-a creat
									else grupg = "";
									// functie care parcurge arborele si schimba grupurile cand este cazul
									com.schimbaGrup(root, user, grupg);
									grupuri.remove(nume_grup); //am sters grupul si din mapul care contine 
																// numele grupurilor
								}
								else {
									System.err.println("Eroare"); // nu este ownerul grupului si nici root
									break;
								}
							else {
								System.err.println("Eroare");
							}
					}
					break;
				case useradd:
					user = com.user(str);
					aux = str.split(" ");
					if (aux.length == 4) {
						nume_grup = aux[2];
						nume_utiliz = aux[3];
						//System.out.println(nume_grup + ", " + nume_utiliz);
						if (grupuri.containsKey(nume_grup))
							if (utilizatori.get(user).contains(nume_grup) || user.equals("root")) {
								// daca userul este ownerul grupului
								//if (user.equals(grupuri.get(nume_grup).get(0)) || user.equals("root")) {
									if (!grupuri.get(nume_grup).contains(nume_utiliz))
										grupuri.get(nume_grup).add(nume_utiliz);
								else {
									System.err.println("Eroare"); //nu este owner
									break;
								}
							}
							else {
								System.err.println("Eroare"); // grupul nu are owner si nu esti root
								break;
							}
						else
							System.err.println("Eroare"); //grupul nu exista
							break;
						}
					System.err.println("Eroare"); //comanda gresita
					break;
				case usermod:
					user = com.user(str);
					aux = str.split(" ");
					if (aux.length >=4) {
						nume_utiliz = aux[aux.length-1];
						if (nume_utiliz.equals(user)) {
							System.err.println("Eroare");
							break;
						}
						aux = com.um(str);
						for (int i=0; i<aux.length; i++) {
							if (grupuri.containsKey(aux[i])) {
									//daca user este ownerul grupului din care vrea sa stearga
									if (user.equals("root") || utilizatori.get(user).contains(aux[i])) {
										// daca userul este ownerul grupului
											if (grupuri.get(aux[i]).contains(nume_utiliz))
												grupuri.get(aux[i]).remove(nume_utiliz);
										}
										else {
											System.err.println("Eroare"); //nu este owner
											break;
										}
								}
							}
						}
					else {
						System.err.println("Eroare"); //s-a dat o comanda gresita
						break;
					}
					break;
				case chmod:
					user = com.user(str);
					aux = str.split(" ");
					cale = com.caleChmod(str);
					if (cale == null) {
						System.err.println("Eroare"); //nu s-a precizat calea
						break;
					}
					if (cale.equals("/")) {
						System.err.println("Eroare"); // nu se pot face modificari de permisiuni asupra /
						break;
					}
					f = com.fisDest(root, cale);
					ultim = com.ultim(cale);
					if (f != null) {
						if (((Director)f).content.c.containsKey(ultim)) {
							f = ((Director)f).content.c.get(ultim);
						}
						else {
							System.err.println("Eroare"); //Nu exista fisierul asupra caruia vrei sa faci modificarile
							break;
						}
					}
					else {
						System.err.println("Eroare"); //Eroare cale
						break;
					}
					if (com.permisiuni(str) == null || com.operator(str) == null || com.proprietar(str) == null) {
						System.err.println("Eroare"); //comanda gresita
						break;
					}
					// modificari user
					String permisiuni = com.permisiuni(str);
					String proprietar = com.proprietar(str);
					String operator = com.operator(str);
					if (f != null) {
						// verificam daca utilizatorul are drepturi asupra directorului in care vrea sa creeze
						if ((f.dall.contains("w")) || user.equals("root") || (user.equals(f.owner_user) && f.duser.contains("w")) || (grupuri.containsKey(f.owner_group) && grupuri.get(f.owner_group).contains(user) && f.dgrup.contains("w"))) {
							//scgimbare drepturi user
							if (proprietar.equals("u")) {
								if (operator.equals("+")) {
									for (int i=0; i<permisiuni.length(); i++) {
										String p = permisiuni.substring(i, i+1);
										if (!f.duser.contains(p)) {
											f.duser += p;
										}
									}
								}
								if (operator.equals("-")) {
									for (int i=0; i<permisiuni.length(); i++) {
										String p = permisiuni.substring(i, i+1);
										if (f.duser.contains(p)) {
											f.duser = f.duser.replace(p, "");
										}
									}
								}
								if (operator.equals("=")) {
									f.duser = permisiuni;
								}
							}
							//schimbare drepturi all
							if (proprietar.equals("a")) {
								if (operator.equals("+")) {
									for (int i=0; i<permisiuni.length(); i++) {
										String p = permisiuni.substring(i, i+1);
										if (!f.dall.contains(p)) {
											f.dall += p;
										}
									}
								}
								if (operator.equals("-")) {
									for (int i=0; i<permisiuni.length(); i++) {
										String p = permisiuni.substring(i, i+1);
										if (f.dall.contains(p)) {
											f.dall = f.dall.replace(p, "");
										}
									}
								}
								if (operator.equals("=")) {
									f.dall = permisiuni;
								}
							}	
							//schimbare drepturi grup
							if (proprietar.equals("g")) {
								if (operator.equals("+")) {
									for (int i=0; i<permisiuni.length(); i++) {
										String p = permisiuni.substring(i, i+1);
										if (!f.dgrup.contains(p)) {
											f.dgrup += p;
										}
									}
								}
								if (operator.equals("-")) {
									for (int i=0; i<permisiuni.length(); i++) {
										String p = permisiuni.substring(i, i+1);
										//System.out.println(p + " substring");
										if (f.dgrup.contains(p)) {
											f.dgrup = f.dgrup.replace(p, "");
										}
									}
								}
								if (operator.equals("=")) {
									f.dgrup = permisiuni;
								}
							}
						}
					}
							break;
				default:
					System.err.println("Eroare"); //nu s-a dat nicio comanda din cele asteptate
					break;
				}
				}
			else {
				System.err.println("Eroare"); //nu s-a dat o comanda valida
			}
			
			//Citire comanda de la System.in
			str = sc.nextLine();
		}
	}
}
