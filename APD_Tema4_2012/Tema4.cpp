#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <fstream>
#include <string>
#include <unistd.h>
#include "mpi.h"
using namespace std;

void afisareTabela(int* tabela, int n, int rank) {
	int i;
	cout << "Pentru nodul " << rank << ":\n";
	for (i = 0; i < n; i++) {
		cout << i << " - " << tabela[i] <<  "\n";
	}
}

//verifica daca vectorul contine aceasta valoare
int contine(int* vector, int valoare, int n) {
	int i;
	for (i = 0; i < n; i++) {
		if (vector[i] == valoare)
			return 1;
	}
	return 0;	
}

int allone(int* vector, int n) {
	int i;
	for (i = 0; i < n; i++) {
		if (vector[i] != 1)
			return 0;
	}
	return 1;
}

int main(int argc, char *argv[])
{
	int noTasks, rank, sursa;
	char* dest;
	string mesaj;
	int recv, sync = 0;
	int tag = 0, tag1 = 1, tag2 = 2, tag3 = 3, i, j;
	MPI_Status status;
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &noTasks);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	
	//citeste linia corespunzatoare din fisier, pentru a afla ce vecini are
	//in tabela de rutare, pe pozitia i valoarea j, semnifica: daca vrei sa ajungi la i, trimite mai departe la j
	char* b = new char[2]; // il folosim atunci cand verificam daca trebuie sa facem broadcast
	b[0] ='B';
	b[1] = '\0';
	int* tabela = new int[noTasks];
	MPI_Request request;
	string line;
	ifstream myfile, mesaje;
	myfile.open(argv[1]);
	int nrLinie = 0, nrVecini = 0;
	char* pch;
	char* vecini = new char[noTasks];
	
	int *dinCiclu = new int[noTasks];
	int k = 0, parinte, nrElemCiclu = 0;
	
	//citirea liniei corespunzatoare din fisier
	if (myfile.is_open()) {
		getline(myfile, line);
		while (nrLinie != rank) {
			getline(myfile, line);
			nrLinie++;
		}
		char* a = new char[line.size() + 1];
		a[line.size()] = 0;
		memcpy(a, line.c_str(), line.size());
		pch = strtok(a, " :");
		//cout << "rank:" << rank << "\n";
		pch =strtok(NULL, " :");
		while (pch != NULL) {
			//cout << pch << "\n";
			int vecin = atoi(pch);
			//cout << "vecin " << vecin << "\n";
			vecini[nrVecini] = vecin;
			nrVecini++;
			pch =strtok(NULL, " :");
		}
		
		myfile.close();
		
		//daca este radacina arborelui, atunci trimite primul mesaj de sondaj
		if (rank == 0) {
			int* tabela1 = new int[noTasks];
			int parinte1;
			for (i = 0; i < noTasks; i++) {
				tabela[i] = -1;
			}
			tabela[rank] = rank;
			int sondaj = 0;
			for (i = 0; i < nrVecini; i++) {
				MPI_Send(&sondaj, 1, MPI_INT, vecini[i], tag, MPI_COMM_WORLD);
			}
			for (i = 0; i < nrVecini; i++) {
				MPI_Recv(tabela1, noTasks, MPI_INT, vecini[i], tag1, MPI_COMM_WORLD, &status);
				parinte1 = status.MPI_SOURCE;
				//cout << "nodul 0 a primit ecou de la nodul " << parinte1 << "\n";
				if (tabela1[0] != -2) {
					tabela[parinte1] = parinte1;
					for (j = 0; j < noTasks; j++) {
						if (tabela1[j] != rank) {
							tabela[j] = parinte1;
						}
					}
				}
				else {
					//cout << "A primit o tabela invalida de la nodul " << parinte1;
				}
			}
			//afisareTabela(tabela, noTasks, rank);
		}
		else {
			//int *dinCiclu = new int[noTasks];
			//int k = 0, parinte, nrElemCiclu = 0;
			int flag = 0, recv1;
			//primeste mesajul de tip sondaj(primul sondaj primit este singurul care conteaza)
			MPI_Recv(&recv,1,MPI_INT,MPI_ANY_SOURCE,tag,MPI_COMM_WORLD,&status);
			parinte = status.MPI_SOURCE;
			//actualizeaza tabela de rutare - oricare ar fi destinatia, next hop este parintele
			for (i = 0; i < noTasks; i++) {
				tabela[i] = parinte;
			}
			tabela[rank] = rank;
			
			//trimie sondajul mai departe
			for (i = 0; i < nrVecini; i++) {
				if (vecini[i] != parinte) {
					k++;
					//cout << "k este " << k << "\n";
					MPI_Send(&recv, 1, MPI_INT, vecini[i], tag, MPI_COMM_WORLD);
				}
			}
			
			int index = -1;
			int count = 1;
			MPI_Request requests[nrVecini]; //sau nrVecini - 1
			for (i = 0; i < nrVecini; i++) {
				//if (vecini[i] != parinte) {
					//flag = 0;
					//daca mai primeste mesaje de tip sondaj de la alti vecini
					MPI_Irecv(&recv1, 1, MPI_INT, vecini[i], tag, MPI_COMM_WORLD, &requests[i]);
					sleep(2);
					MPI_Testany(i+1, requests, &index, &flag, &status);
					//cout << "FLAGUL " << flag << "\n";
					if (index != MPI_UNDEFINED) {
						//cout << "A trecut de mpi_Testany si are flagul " << flag << " si parintele " << status.MPI_SOURCE << " index " << index << "\n";
						int* tabela1 = new int[noTasks];
						for (j = 0; j < noTasks; j++) {
							tabela1[j] = -2;
						}
						//cout << "----------------------- am intrat aici -------------------------\n";
						int parinteaux = status.MPI_SOURCE;
						//retinem nodurile care au trimis mesaje de sondaj duplicat
						dinCiclu[nrElemCiclu] = parinteaux;
						nrElemCiclu++;
						//trimite un mesaj de tip ecou, nul
						//cout << "===== Nodul " << rank << " A trimis ecou invalid catre nodul " << parinteaux << " ==========\n";
						MPI_Send(tabela1, noTasks, MPI_INT, parinteaux, tag1, MPI_COMM_WORLD);
					}
				//}
			}
			//daca nu este frunza, asteapta mesaje de tip ecou

			if (k != 0) {
				int* tabela1 = new int[noTasks];
				int parinte1;
				MPI_Status status1;
				for (i = 0; i < nrVecini; i++) {
					if (vecini[i] != parinte) {			
						//cout << "rankul: " << rank << " asteapta mesaje de tip ecou\n";
						MPI_Recv(tabela1, noTasks, MPI_INT, vecini[i], tag1, MPI_COMM_WORLD, &status);
						//cout << "rankul: " << rank << " a primit mesaj de tip ecou\n";
						parinte1 = status.MPI_SOURCE;
						//actualizeaza tabela
						if (tabela1[0] != -2) {
							tabela[parinte1] = parinte1;
							//daca tabela anterioara nu trimitea catre acest nod pentru destinatia respectiva, atunci nodul acesta trebuie sa trimita catre nodul de la care a primit tabela acum, pentru acea destinatie - cam incurcat, dar ideea e simpla
							for (j = 0; j < noTasks; j++) {
								if (tabela1[j] != rank) {
									tabela[j] = parinte1;
								}
							}
						}
						else {
							//cout << "A primit o tabela care nu este valida de la nodul " << parinte1 << "\n";
						}
					}
				}
				//trimite tabela mai departe, catre parinte
				MPI_Send(tabela, noTasks, MPI_INT, parinte, tag1, MPI_COMM_WORLD);
			}
			else
				//daca am ajuns la o frunza(nu mai are alti vecini in afara de parinte)
				if (k == 0) {
					//incepem sa trimitem mesaje de tip ecou
					MPI_Send(tabela, noTasks, MPI_INT, parinte, tag1, MPI_COMM_WORLD);
					//cout << "frunza " << rank << " a trimis un ecou\n";
				}
			//afisareTabela(tabela, noTasks, rank);
		}
		afisareTabela(tabela, noTasks, rank);
	}	
	
	//==========================================================================
	//=============== Etapa 2 - se trimit mesaje intre noduri ==================
	//==========================================================================
	mesaje.open(argv[2]);
	if (mesaje.is_open()) {
		char* a;
		getline(mesaje, line);
		a = new char[line.size() + 1];
		a[line.size()] = 0;
		memcpy(a, line.c_str(), line.size());
		int n = atoi(a);
		free(a);
		//fiecare proces citeste fiecare linie din fisier si daca sursa este egala cu rankul procesului, se incepe procesul de trimitere a mesajului
		for ( i = 0; i < n; i++) {
			getline(mesaje, line);
			a = new char[line.size() + 1];
			a[line.size()] = 0;
			memcpy(a, line.c_str(), line.size());
			pch = strtok(a, " ");
			sursa = atoi(pch);
			//cout << "sursa: " << sursa << "\n";
			if (sursa == rank) {
				//citim destinatia si mesajul pe care trebuie sa il trimitem
				pch = strtok(NULL, " ");
				dest = pch;
				
				//======================================== Broadcast ==================================
				if (strcmp(dest, b) == 0) {
					for (j = 0; j < nrVecini; j++) {
						int size = line.size() + 1;
						char* msg = new char[size];
						msg[size-1] = 0;
						memcpy(msg, line.c_str(), size);
						cout << "Trimitem: " << line << "\n";
						MPI_Send(msg, size, MPI_CHAR, vecini[j], tag2, MPI_COMM_WORLD);
					}
					//trimite mesaje catre toti vecinii
					cout << "Nodul " << rank << " trimite mesaj de broadcast\n";
				}
				//====================================== Mesaj normal ===============================
				else {
					//trimite mesajul cu destinatia citita din fisier
					int destinatie = atoi(dest);
					size_t found;
					//vom determina cu ajutorul tabelei de rutare care este next-hop si trimitem catre acesta
					int size = line.size() + 1;
					char* msg = new char[size];
					msg[size-1] = 0;
					memcpy(msg, line.c_str(), size);
					cout << " Nodul " << rank << " trimite: " << line << " catre nodul " << destinatie << " Next-hop: " << tabela[destinatie] << "\n";
					MPI_Send(msg, size, MPI_CHAR, tabela[destinatie], tag2, MPI_COMM_WORLD);
					free(msg);
				}
			}
		}
	}
	mesaje.close();
			
			//trebuie sa stergem vecinii care fac parte din cicluri
			/*for (i = 0; i < nrElemCiclu; i++) {
				cout << dinCiclu[i] << ", ";
			}
			cout << endl;*/
			int ind = 0;
			int* newVecini = new int[nrVecini - nrElemCiclu];
			for (i = 0; i < nrVecini; i++) {
				if (!contine(dinCiclu, vecini[i], nrElemCiclu)) {
					newVecini[ind] = vecini[i];
					ind++;
				}
			}
			for (i = 0; i < ind; i++) {
				vecini[i] = newVecini[i];
			}
			nrVecini = ind;
			//dupa ce a trimis toate mesajele, trimite un mesaj de broadcast, care anunta toate procesele ca a terminat de trimis
			//trimitem catre toti vecinii un mesaj care contine rankul nostru. Se termina atunci cand ajungem la frunze.
			for (i = 0; i < nrVecini; i++) {
					char exit[50] = "exit ";
					char nr[33];
					sprintf(nr, "%d", rank);
					strcat(exit, nr);
					int size = strlen(exit) + 1;
					MPI_Send(exit, size, MPI_CHAR, vecini[i], tag2, MPI_COMM_WORLD);
			}
			//indiferent daca procesul trimite sau nu mesaje, acesta trebuie sa astepte mesaje
			//in continutul mesajului vom avea destinatia si mesajul(deci trimitem linia pe care o citim, din care stergem sursa)
			char primit[200];
			char primit1[200];
			char exit[50] = "exit";
			int terminare, desti, source, deTerminat = nrVecini - 1, terminat = 0;
			char *d, *s;
			int* terminate = new int[noTasks];
			for (i = 0; i < noTasks; i++) {
				terminate[i] = 0;
			}
			terminate[rank] = 1;
			int flag = 0, count;
			while (!allone(terminate, noTasks)) {
				MPI_Recv(primit, 200, MPI_CHAR, MPI_ANY_SOURCE, tag2, MPI_COMM_WORLD, &status);
				//a primit un mesaj
				MPI_Get_count(&status, MPI_CHAR, &count);
				memcpy(primit1, primit, count);
				s = strtok(primit1, " ");
				if (strcmp(s, exit) == 0) {
					//am primit mesaj de terminare
					//determinam rankul care a terminat
					char* r = strtok(NULL, " ");
					int ran = atoi(r);
					terminate[ran] = 1;
					for (i = 0; i < nrVecini; i++) {
						if (vecini[i] != status.MPI_SOURCE) {
							//trimite mai departe mesajul de terminare
							MPI_Send(primit, count, MPI_CHAR, vecini[i], tag2, MPI_COMM_WORLD);
						}
					}
				}
				else {
					d = strtok(NULL, " ");
					if (strcmp(d, b) == 0) {
						cout << "\n<<<<<<<<<<<<<<<<<<<<<< rank " << rank << " a primit mesajul de broadcast >>>>>>>>>>>>\n\n";
						for (i = 0; i < nrVecini; i++) {
							if (vecini[i] != status.MPI_SOURCE) {
								//trimite mai departe mesajul de terminare
								MPI_Send(primit, count, MPI_CHAR, vecini[i], tag2, MPI_COMM_WORLD);
							}
						}
					}
					else {
						desti = atoi(d);
						source = atoi(s);
						cout << "procesul " << rank << " a primit de la " << status.MPI_SOURCE << " mesajul: " << primit << " cu destinatia " << desti <<  "(expeditorul mesajului este: " << source << " NEXT-HOP: " << tabela[desti] <<  ")\n";
						//daca aceasta este destinatia, ne oprim aici, altfel trimitem mesajul mai departe
						if (rank != desti) {
							MPI_Send(primit, count, MPI_CHAR, tabela[desti], tag2, MPI_COMM_WORLD);
						}
						else {
							cout << "\n!!!!rank: " << rank << " mesajul " << primit << " a ajuns la destinatie!!!!\n\n";
						}
					}
				}
			}
			
	//=============================================================================================================
	//========================================= Etapa 3 ===========================================================
	//=============================================================================================================
	/*char vot[70];
	if (rank == 0) {
		for (i = 0; i < )
		MPI_Recv(vot, 50, MPI_CHAR, MPI_ANY_SOURCE, tag2, MPI_COMM_WORLD, &status);
	}*/
	
	MPI_Finalize();	
	return 0;
}





















