#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include "helpers.h"
#include <queue>
using namespace std;


#define DRUMAX 10000
//afisare topologie
void afis_topol(int topologie[KIDS][KIDS]) {
	FILE* f = fopen("andreea", "at");
	fprintf(f,"\nAfisare\n");
	for (int i = 0; i < KIDS; i++) {
		for (int j = 0; j < KIDS; j++) {
			fprintf(f,"%i, ", topologie[i][j]);
		}
		fprintf(f,"\n");
	}
	fclose(f);
}

//verifica - adauga mesajul sau nu in LSADatabase si daca l-a adaugat intoarce 1, iar daca nu l-a adaugat, intoarce 0
int verifica(msg LSADatabase[], msg m, int* index) {
	int i = 0;
	int ok = 0;
	for ( i = 0; i < *index; i++) {
		msg x = LSADatabase[i];
		if (x.creator == m.creator) {
			ok = 1;
			if (x.timp < m.timp) {
				LSADatabase[i] = m;
				return 1;
			}
		}
	}
	if (!ok) {
		LSADatabase[*index] = m;
		(*index)++;
		FILE* f = fopen("andreea", "at");
		fprintf(f, "indexul in database este %i\n", *index);
		fclose(f);
		return 1;
		}
	return 0;
}
//in nod primeste nodul pentru care trebuie sa faca tabela de rutare
void floyd(int topol[KIDS][KIDS], int tab_rutare[KIDS][2], int nod) {
	int i, j, k;
	int topologie[KIDS][KIDS];
	int A[KIDS][KIDS];
	int V[KIDS][KIDS];
	//initializare V cu 0
	for (i = 0; i < KIDS; i++)
		for ( j = 0; j < KIDS; j++) {
		V[i][j] = 0;
		}
	//construim o topologie in conformitate cu ce ne trebuie pentru Floyd
	for (i = 0; i < KIDS; i++)
		for ( j = 0; j < KIDS; j++) {
			if (i == j)
				topologie[i][j] = 0;
			else
				if (topol[i][j] == -1)
					topologie[i][j] = 10000;
				else
					topologie[i][j] = topol[i][j];
		}
	//Algoritmul Floyd	
	for (i = 0; i < KIDS; i++)
		for (j = 0; j < KIDS; j++) {
			A[i][j] = topologie[i][j];
			if (A[i][j] < 10000) {
				V[i][j] = j;
			}
		}
	for (k = 0; k < KIDS; k++)
		for (i = 0; i < KIDS; i++)
			for ( j = 0; j < KIDS; j++) {
				if (A[i][j] > A[i][k] + A[k][j]) {
					A[i][j] = A[i][k] + A[k][j];
					V[i][j] = V[i][k];
				}
			}
	//actualizam tabela de rutare
	for ( i = 0; i < KIDS; i++) {
		if (A[nod][i] < 10000) {
		tab_rutare[i][0] = A[nod][i];
		tab_rutare[i][1] = V[nod][i];
		}
	}
}



int main (int argc, char ** argv)
{			
	int vecini[2 * KIDS];
	//file *f = fopen("andreea", "a");
	int pipeout = atoi(argv[1]);
	int pipein = atoi(argv[2]);
	int nod_id = atoi(argv[3]); //procesul curent participa la simulare numai dupa ce nodul cu id-ul lui este adaugat in topologie 
	int timp =-1 ;
	int gata = FALSE;
	msg mesaj;
	int cit, k;
	int topologie[KIDS][KIDS];
	int id_unic = 0;
	msg LSADatabase[KIDS];
	int index = 0;; //retine cate elemente avem in LSADatabase
	queue<msg> coada_new;
	queue<msg> coada_old;

	
	//initializare topologie cu valori negative, pentru a putea face diferenta intre nodurile intre care exista drum si cele intre care nu exista
	int i = 0;
	int j = 0;
	for ( i = 0; i < KIDS; i++)
		for (j = 0; j < KIDS; j++)
			topologie[i][j] = -1;
	
	//nu modificati numele, modalitatea de alocare si initializare a tabelei de rutare - se foloseste la mesajele de tip 8/10, deja implementate si la logare
	int tab_rutare [KIDS][2]; //tab_rutare[k][0] reprezinta costul drumului minim de la ruterul curent (nod_id) la ruterul k 
								//tab_rutare[k][1] reprezinta next_hop pe drumul minim de la ruterul curent (nod_id) la ruterul k 
								
	for (k = 0; k < KIDS; k++) {
		tab_rutare[k][0] = 10000;  // drum =DRUMAX daca ruterul k nu e in retea sau informatiile despre el nu au ajuns la ruterul curent
		tab_rutare[k][1] = -1; //in cadrul protocolului(pe care il veti implementa), next_hop =-1 inseamna ca ruterul k nu e (inca) cunoscut de ruterul nod_id (vezi mai sus)
	}
																
	printf ("Nod %d, pid %u alive & kicking\n", nod_id, getpid());

	if (nod_id == 0) { //sunt deja in topologie
		timp = -1; //la momentul 0 are loc primul eveniment
		mesaj.type = 5; //finish procesare mesaje timp -1
		mesaj.sender = nod_id;
		write (pipeout, &mesaj, sizeof(msg)); 
		printf ("TRIMIS Timp %d, Nod %d, msg tip 5 - terminare procesare mesaje vechi din coada\n", timp, nod_id);
	
	}

	while (!gata) {
		cit = read(pipein, &mesaj, sizeof(msg));
		
		if (cit <= 0) {
			printf ("Adio, lume cruda. Timp %d, Nod %d, msg tip %d cit %d\n", timp, nod_id, mesaj.type, cit);
			exit (-1);
		}
		
		switch (mesaj.type) {
			
			//1,2,3,4 sunt mesaje din protocolul link state; 
			//actiunea imediata corecta la primirea unui pachet de tip 1,2,3,4 este buffer-area (punerea in coada /coada new daca sunt 2 cozi - vezi enunt)
			//mesajele din coada new se vor procesa atunci cand ea devine coada old (cand am intrat in urmatorul pas de timp)
			case 1:
				{
				coada_new.push(mesaj);
				}
				//printf ("Timp %d, Nod %d, msg tip 1 - LSA\n", timp, nod_id);
				break;
				
			case 2:
				{
				coada_new.push(mesaj);
				}
				break;
				
			case 3:
				{
				coada_new.push(mesaj);
				}
				break;
				
			case 4:
				{
				coada_new.push(mesaj);
				} 
				break; 
			
			case 6:
				{
				timp++;
				//printf ("Timp %d, Nod %d, msg tip 6 - incepe procesarea mesajelor puse din coada la timpul anterior (%d)\n", timp, nod_id, timp-1);
				//coada noua devine coada veche;
				queue<msg> coada_aux;
				coada_old = coada_new;
				
				coada_new = coada_aux;
				//golim coada noua
				while (!coada_new.empty()) {
					coada_new.pop();
				}
				
				
				//daca NU mai am de procesat mesaje venite la timpul anterior
				//(dar mai pot fi mesaje venite in acest moment de timp, pe care le procesez la t+1) 
				//trimit mesaj terminare procesare pentru acest pas (tip 5)
				//altfel, procesez mesajele venite la timpul anterior si apoi trimit mesaj de tip 5
				while (!coada_old.empty()) {
					msg mesaj1 = coada_old.front();
					switch (mesaj1.type) {
						//1,2,3,4 sunt mesaje din protocolul link state; 
						//actiunea imediata corecta la primirea unui pachet de tip 1,2,3,4 este buffer-area (punerea in coada /coada new daca sunt 2 cozi - vezi enunt)
						//mesajele din coada new se vor procesa atunci cand ea devine coada old (cand am intrat in urmatorul pas de timp)
						case 1:
						{
						//verificam daca exista in LSADatabase un mesaj mai nou care are acelasi creator
						int ok;
						ok = verifica(LSADatabase, mesaj1, &index);
						//luam vectorul din payload
						int *vecini = (int*)(mesaj1.payload);
						//creator
						int creator = mesaj1.creator;
						//parcurgem vecinii si adaugam in topologie
						//update topologie
						if (ok)
							for (int i = 0; i < mesaj1.len; i+=2) {
								topologie[*(vecini+i)][creator] = *(vecini+i+1);
								topologie[creator][*(vecini+i)] = *(vecini+i+1);
							}
						int sender = mesaj1.sender;
						mesaj1.sender = nod_id;
						//trimitere la vecini
						if (ok)
							for (int i = 0; i < KIDS; i++) {
								if (topologie[nod_id][i] > 0 && sender != i) {
									mesaj1.next_hop = i;
									write(pipeout, &mesaj1, sizeof(msg));
								}
							}
						}
						//printf ("Timp %d, Nod %d, msg tip 1 - LSA\n", timp, nod_id);
						break;
					
						case 2:
						{
						
						//vecinii isi actualizeaza structurile de date
						//topologie
						topologie[nod_id][mesaj1.creator] = mesaj1.len;
						topologie[mesaj1.creator][nod_id] = mesaj1.len;
						//se raspunde cu mesaje database reply
						int i = 0;
						msg LSA;
						//trimitem tot ce avem in LSADatabase catre noul ruter
						for (i = 0; i < index; i++) {
							msg message = LSADatabase[i];
							message.sender = nod_id;
							message.timp = timp;
							message.type = 3;
							message.next_hop = mesaj1.sender;
							//message.creator = nod_id; //???? nu stiu daca trebuie sa pun asta NU TREBUIE
							write(pipeout, &message, sizeof(msg));
						}		
				
						//trimitem catre toti vecinii acestui nod, mesaj de tip LSA
						LSA.creator = nod_id;
						LSA.type = 1;
						LSA.sender = nod_id;
						LSA.timp = timp;
						LSA.nr_secv = id_unic;
						//actualizam id_unic de fiecare data cand il folosim
						id_unic++;
				
						//am creat lista de vecini si costuri
						//pe pozitiile pare am retinut vecinii si pe pozitiile impare imediat urmatoare am retinut costul pana la acel vecin
						//in v avem 2*nr_vecini
						int v = 0;
						for (i = 0; i < KIDS; i++) {
							if (topologie[nod_id][i] > 0) {
								vecini[v] = i;
								v++;
								vecini[v] = topologie[nod_id][i];
								//printf("vecini nodul %i la timpul %i: vecin %i si cost %i\n", nod_id, timp, vecini[v-1], vecini[v]);
								v++;
							}
						}
						//trimitem in len, lungimea vectorului de vecini si costuri
						LSA.len = v;
						//copiem in payload acest vector
						memcpy(LSA.payload, &vecini, sizeof(vecini));
				
						//parcurgem iar vecinii si le trimitem lista cu toti vecinii nodului curent si costuri
						for (i = 0; i < KIDS; i++) {
							if (topologie[nod_id][i] > 0) {
								//tot ce mai modificam la mesaj, este catre cine il trimitem
								LSA.next_hop = i;
								write(pipeout, &LSA, sizeof(msg));
							}
						}
					
						}
						//printf ("Timp %d, Nod %d, msg tip 2 - Database Request\n", timp, nod_id);
						break;
				
						case 3:
						{
						//aici noul ruter primeste mesaje database reply 
						//verificam daca exista in LSADatabase un mesaj mai nou care are acelasi creator
						int ok;
						ok = verifica(LSADatabase, mesaj1, &index);						
						//trimitem mesajul catre toti vecinii cu exceptia noului ruter
						int creator = mesaj1.creator;
						int *vecini = (int*)(mesaj1.payload);
						//update topologie
						if (ok)
							for (int i = 0; i < mesaj1.len; i+=2) {
								//printf("timpul %i pentru nodul %i: vecin %i si cost %i\n", timp, creator, *(vecini+i), *(vecini + i + 1));
								topologie[*(vecini+i)][creator] = *(vecini+i+1);
								topologie[creator][*(vecini+i)] = *(vecini+i+1);
							}
							
						//calculeaza tabela de rutare
						floyd(topologie, tab_rutare, nod_id);
						//printf("\n");
						}
					
						//printf ("Timp %d, Nod %d, msg tip 3 - Database Reply\n", timp, nod_id);
						break;
				
						case 4:
						{
						//cand primim un pachet de tip 4, pur si simplu il trimitem la next_hop, daca nu cumva nod_id este chiar destinatia
						if (mesaj1.len != nod_id) {
							mesaj1.next_hop = tab_rutare[mesaj1.len][1];
							mesaj1.timp = timp;
							//printf("trimitem la %i((((((((((((((((((((((\n", tab_rutare[mesaj1.len][1]);
							write(pipeout, &mesaj1, sizeof(msg));
						}
						else
							printf("Am primit pachetul Uraaaaaaaaaaaaaaaaaaaaaaaaaa!!!!\n");
						} 
						//printf ("Timp %d, Nod %d, msg tip 4 - pachet de date (de rutat)\n", timp, nod_id);
						break; 
					}
					//	procesez tote mesajele din coada old 
					//	(sau toate mesajele primite inainte de inceperea timpului curent - marcata de mesaj de tip 6)
					//	la acest pas/timp NU se vor procesa mesaje venite DUPA inceperea timpului curent
					//cand trimiteti mesaje de tip 4 nu uitati sa setati (inclusiv) campurile, necesare pt logare:  mesaj.timp, mesaj.creator, mesaj.nr_secv, mesaj.sender, mesaj.next_hop
					//la tip 4 - creator este sursa initiala a pachetului rutat
					coada_old.pop();
				}
				//actualizare tabela de rutare
				floyd(topologie, tab_rutare, nod_id);
				
			//acum coada_old e goala, trimit mesaj de tip 5
				mesaj.type = 5; 
				mesaj.sender = nod_id;
				write (pipeout, &mesaj, sizeof(msg)); 
			}
			break;
			
			case 7: //complet in ceea ce priveste partea cu mesajele de control
					//aveti de implementat tratarea evenimentelor si trimiterea mesajelor ce tin de protocolul de rutare
					//in campul payload al mesajului de tip 7 e linia de fisier (%s) corespunzatoare respectivului eveniment
					//vezi multiproc.c, liniile 88-115 (trimitere mes tip 7) si liniile 184-194 (parsare fisiere evenimente)
					 
					//rutere direct implicate in evenimente, care vor primi mesaje de tip 7 de la simulatorul central:
					//eveniment tip 1: ruterul nou adaugat la retea  (ev.d1  - vezi liniile indicate)
					//eveniment tip 2: capetele noului link (ev.d1 si ev.d2)
					//eveniment tip 3: capetele linkului suprimat (ev.d1 si ev.d2)
					//evenimet tip 4:  ruterul sursa al pachetului (ev.d1)
				{
				
				//parsare eveniment
				char *p;
				//printf ("Split \"%s\" in tokens:\n", mesaj.payload);
				p = strtok (mesaj.payload, " ");
				//determinam tipul evenimentului
				int tip_event = atoi(p);
				//printf ("Tipul evenimentului: %i\n", tip_event);
				p = strtok (NULL, " ,");
				switch(tip_event) {
					case 1:
					//aparitia unui nou ruter
					{
					p = strtok(NULL, " ");
					//aflam numarul de vecini
					int nr_vecini = atoi(p);
					//printf ("Numar vecini: %i\n", nr_vecini);
					p = strtok(NULL, " ");
					int i;
					for (i = 0; i < nr_vecini; i++) {
						//mesajul pe care il vom trimite dupa ce adaugam toate datele in el
						msg trimite;
						int vecin = atoi(p);
						//printf ("Vecin%i: %i\n",i, vecin);
						p = strtok(NULL, " ");
						//retinem costul in len
						//pentru mesaje de tip2, trebuie sa trimitem doar costul de la noul ruter la vecin
						trimite.len = atoi(p);
						int cost = atoi(p);
						//printf("Cost%i: %i\n", i, cost);
						p = strtok(NULL, " ");
						topologie[nod_id][vecin] = cost;
						topologie[vecin][nod_id] = cost;
						//trimitem mesaje de tip DatabaseRequest(tip 2) catre fiecare vecin
						trimite.type = 2;
						trimite.nr_secv = id_unic;
						id_unic++;
						trimite.sender = nod_id;
						trimite.next_hop = vecin;
						trimite.creator = nod_id;
						write(pipeout, &trimite, sizeof(msg));
						//deocamdata am trimis Database request. Nu cred ca trebuie sa mai fac altceva in cazul asta
					}
					}
					break;
					
					//aparitia unui nou link
					case 2:
					{
					//ruter1
					int rut1 = atoi(p);
					
					p = strtok(NULL, " ");
					int rut2 = atoi(p);
					
					p = strtok(NULL, " ");
					int cost = atoi(p);
					p = strtok(NULL," ");
					//capetele linkului creeaza si isi trimit unul altuia mesaje database request(tip2)
					msg trimite;
					trimite.len = cost;
					//actualizare topologie
					topologie[rut1][rut2] = cost;
					topologie[rut2][rut1] = cost;
					//Construiesc pachete de tip LSA si le trimit tuturor vecinilor directi, inclusiv noului vecin=============
					msg LSA;
					LSA.creator = nod_id;
					LSA.type = 1;
					LSA.sender = nod_id;
					LSA.timp = timp;
					LSA.nr_secv = id_unic;
					//actualizam id_unic de fiecare data cand il folosim
					id_unic++;
					
					//construim vectorul pentru payload
					int v = 0;
						for (i = 0; i < KIDS; i++) {
							if (topologie[nod_id][i] > 0) {
								vecini[v] = i;
								v++;
								vecini[v] = topologie[nod_id][i];
								//printf("vecini nodul %i la timpul %i: vecin %i si cost %i\n", nod_id, timp, vecini[v-1], vecini[v]);
								v++;
							}
						}
					LSA.len = v;
					//adaugam vectorul v in payload
					memcpy(LSA.payload, &vecini, sizeof(vecini));
					
					
							//actualizam LSADatabase pe baza LSA ului propriu
					//int ok = verifica(LSADatabase, LSA, &index);
				
						//parcurgem iar vecinii si le trimitem lista cu toti vecinii nodului curent si costuri
					//if (ok)
						for (i = 0; i < KIDS; i++) {
							if (topologie[nod_id][i] > 0) {
								//tot ce mai modificam la mesaj, este catre cine il trimitem
								LSA.next_hop = i;
								write(pipeout, &LSA, sizeof(msg));
							}
						}
					
					//Se termina LSA========================
					trimite.type = 2;
					trimite.nr_secv = id_unic;
					id_unic++;
					trimite.sender = nod_id;
					if (nod_id == rut1)
						trimite.next_hop = rut2;
						else
							if (nod_id == rut2)
								trimite.next_hop = rut1;
					trimite.creator = nod_id;
					write(pipeout, &trimite, sizeof(msg));
					}
					break;
					case 3:
					{
					int rut1 = atoi(p);
					p = strtok(NULL, " ");
					int rut2 = atoi(p);
					p = strtok(NULL, " ");
					//actualizare topologie
					topologie[rut1][rut2] = -1;
					topologie[rut2][rut1] = -1;
					//construiesc pachet LSA si il trimit tuturor vecinilor directi, cu exceptia capatului opus al link ului
					msg LSA;
					LSA.creator = nod_id;
					LSA.type = 1;
					LSA.sender = nod_id;
					LSA.timp = timp;
					LSA.nr_secv = id_unic;
					//actualizam id_unic de fiecare data cand il folosim
					id_unic++;
					
					//construim vectorul pentru payload
					int v = 0;
						for (i = 0; i < KIDS; i++) {
							if (topologie[nod_id][i] > 0) {
								vecini[v] = i;
								v++;
								vecini[v] = topologie[nod_id][i];
								//printf("vecini nodul %i la timpul %i: vecin %i si cost %i\n", nod_id, timp, vecini[v-1], vecini[v]);
								v++;
							}
						}
					LSA.len = v;
					//adaugam vectorul v in payload
					memcpy(LSA.payload, &vecini, sizeof(vecini));
					
					
							//actualizam LSADatabase pe baza LSA ului propriu
					int ok = verifica(LSADatabase, LSA, &index);
				
						//parcurgem iar vecinii si le trimitem lista cu toti vecinii nodului curent si costuri
					if (ok)
						for (i = 0; i < KIDS; i++) {
							if (topologie[nod_id][i] > 0) {
								//tot ce mai modificam la mesaj, este catre cine il trimitem
								LSA.next_hop = i;
								write(pipeout, &LSA, sizeof(msg));
							}
						}
					}
					break;
					
					case 4:
					{
					int sursa = atoi(p);
					//printf ("^^^^^^^^^^^6Sursa in case 4: %i\n", sursa);
					p = strtok(NULL, " ");
					int dest = atoi(p);
					//printf ("^^^^^^^^^^^^6Dest in case 4: %i\n", dest);
					p = strtok(NULL, " ");
					//construim pachet de date
					msg pachet;
					pachet.type = 4;
					pachet.creator = nod_id;
					pachet.nr_secv = id_unic;
					id_unic++;
					pachet.sender = sursa;
					pachet.timp = timp;
					//retinem destinatia in len
					pachet.len = dest;
					//printf("trimitem la %i((((((((((((((((((((((\n", tab_rutare[dest][1]);
					pachet.next_hop = tab_rutare[dest][1];
					//trimitem la next_hop conform tabelei de rutare
					if (tab_rutare[dest][1] != -1)
						write(pipeout, &pachet, sizeof(msg));
					}
					break;
				}
				//se termina parsarea
				
				if (mesaj.join == TRUE) {
					timp = mesaj.timp;
					printf ("Nod %d, msg tip eveniment - voi adera la topologie la pasul %d\n", nod_id, timp+1); 
				}
				else
					printf ("Timp %d, Nod %d, msg tip 7 - eveniment\n", timp+1, nod_id);
				//acest tip de mesaj (7) se proceseaza imediat - nu se pune in nicio coada (vezi enunt)
				}
				break;
			
			case 8: //complet implementat - nu modificati! (exceptie afisari on/off)
				{
				//printf ("Timp %d, Nod %d, msg tip 8 - cerere tabela de rutare\n", timp+1, nod_id);
				mesaj.type = 10;  //trimitere tabela de rutare
				mesaj.sender = nod_id;
				memcpy (mesaj.payload, &tab_rutare, sizeof (tab_rutare));
				//Observati ca acest tip de mesaj (8) se proceseaza imediat - nu se pune in nicio coada (vezi enunt)
				write (pipeout, &mesaj, sizeof(msg)); 
				}
				break;
				
			case 9: //complet implementat - nu modificati! (exceptie afisari on/off)
				{
				//Aici poate sa apara timp -1 la unele "noduri"
				//E ok, e vorba de procesele care nu reprezentau rutere in retea, deci nu au de unde sa ia valoarea corecta de timp
				//Alternativa ar fi fost ca procesele neparticipante la simularea propriu-zisa sa ramana blocate intr-un apel de read()
				printf ("Timp %d, Nod %d, msg tip 9 - terminare simulare\n", timp, nod_id);
				gata = TRUE;
				}
				break;
				

			default:
				printf ("\nEROARE: Timp %d, Nod %d, msg tip %d - NU PROCESEZ ACEST TIP DE MESAJ\n", timp, nod_id, mesaj.type);
				exit (-1);
		}			
	}

return 0;

}
