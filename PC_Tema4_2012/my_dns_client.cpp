#pragma pack(push,1)
#include "helpers.h"
#include "header.h"

//ia informatii din bufer si recreeaza numele de domeniu un name
int answerName(char* buffername, char* buffer, char* name, int* len) {
	int lungime = 0;
	int avans = 0;
	int ePointer = 0;
	char* aux = (char*)malloc(256);
	int offset;
	//atata timp cat nu intalnim valoarea 0
	while(*buffername != 0) {
		//daca avem pointer catre o alta adresa, mergem acolo in buffer
		if((unsigned char)*buffername>=192) {
			offset = ((unsigned char)(*buffername))*256 + (unsigned char)(*(buffername+1)) - 49152;
			buffername = buffer + (unsigned char)offset;
			ePointer = 1;
			}
		//daca nu, continuam sa citim in ordine
		else {
			//copiem urmatorul element in aux
			memcpy(aux+lungime, buffername, 1);
			lungime += 1;
			//avansam in buffer
			buffername += 1;
		}
		//daca nu am intalnit inca pointer, marim avans(cu cat o sa avansam in buffer dupa iesirea din functie)
		if (ePointer == 0) {
			avans++;
		}
	}
	//daca am intalnit cel putin un pointer pe parcurs, atunci marim avans cu 1
		if (ePointer == 1) {
			avans++;
		}
	//punem punct in loc de cifre
	int n = *aux; //primul numar
	int poz = 0;
	int i = 0;
	aux += 1;
	while (n != 0) {
		int acolo = 0;
		memcpy(name + poz, aux+poz, n);
		//for (acolo = poz; acolo < poz+n; acolo++)
			//printf("in buffername %d\n", (unsigned short)*(name+acolo));
		poz += n;
		*(name + poz) = '.';
		n = *(aux+poz);
		poz++;
	}
	//stergem 0 de pe ultima pozitie
	*(name + poz) = '\0';
	*len = poz-1;
	return avans;
}

unsigned short tip(char* s) {
	if (strcmp(s, "A") == 0) {
		return A;
		}
	if (strcmp(s, "NS") == 0) {
		return NS;
		}
	if (strcmp(s, "CNAME") == 0) {
		return CNAME;
		}
	if (strcmp(s, "PTR") == 0) {
		return PTR;
		}
	if (strcmp(s, "MX") == 0) {
		return MX;
		}
	printf("!!!Nu s-a dat o comanda valida!!!\n");
	return 0;
}

char* tipSimbol(unsigned short t) {
	char* p = (char*)malloc(20);
	
	if (t == A) {
		return (char*)"A";
		}
	if (t == NS) {
		return (char*)"NS";
		}
	if (t == CNAME) {
		return (char*)"CNAME";
		}
	if (t == PTR) {
		return (char*)"PTR";
		}
	if (t == MX) {
		return (char*)"MX";
		}
	printf("!!!Nu s-a dat o comanda valida!!!\n");
	return 0;
}
//primeste nume si tip si intoarce intreaga cerere pe care trebuie sa o trimitem
char* getHostByName(char* name, char* type) {
	char* final = (char*)malloc(sizeof(dns_header_t) + strlen(name) + 1 + sizeof(dns_question_t));
	
	//bufferul in care adunam informatia pentru header;
	dns_header_t* header = (dns_header_t*)malloc(sizeof(dns_header_t));
	header->id = htons(1234);
	header->rd = 1;
	header->tc = 0;
	header->aa = 0;
	header->opcode = 0;
	header->qr = 0;
	header->rcode = 0;
	header->z = 0;
	header->ra = 0;
	header->qdcount = htons(1);
	header->ancount = 0;
	header->nscount = 0;
	header->arcount = 0;
	memcpy(final, header, sizeof(dns_header_t));
	//setare campuri query======================================================
	
	dns_question_t* question = (dns_question_t*)malloc(sizeof(dns_question_t));
	//formam qname==========================================================
	
	char* namep;
	//daca nu este un mesaj cu tipul PTR
	if (strcmp(type, "PTR") != 0) {
		namep = (char*)malloc(strlen(name) + 1);
		//creeaza o copie a numelui si pastreaza lungimea lui in len
		char* name_copy = (char*)malloc(strlen(name) + 1);
		strcpy(name_copy, name);
		char* tok = strtok(name_copy, ".");
		int i = 0;
		while (tok != NULL) {
				sprintf(namep+i, "%c", (strlen(tok)));
				i++;
				sprintf(namep+i, "%s", tok);
				i += strlen(tok);
				tok = strtok(NULL, ".");	
		}
		sprintf(namep+strlen(namep),"%c", 0);
	}
	else {
		//formam qname pentru tipul PTR
		//face copia numelui
		namep = (char*)malloc(strlen(name) + 13);
		char* name_copy = (char*)malloc(strlen(name) + 1);
		strcpy(name_copy, name);
		//prelucram in functie de caracterul '.'
		char* tok = strtok(name_copy, ".");
		int i = strlen(name);
		int j = 0;
		while (tok != NULL) {
				//printf("token: %s\n", tok);
				//adaugam de la sfarsit la inceput
				memcpy(namep+i-strlen(tok), tok, strlen(tok));
				
				i -= strlen(tok);
				if (i > 0) {
					i--;
					memcpy(namep+i, ".", 1);
					}
				//i--;
				tok = strtok(NULL, ".");	
		}
		memcpy(namep + strlen(namep), ".in-addr.arpa", 13);
	}
	int k = 0;
	/*for (k = 0; k < strlen(namep); k++) {
		printf("pozitia %i: %c\n", k, (*(namep+k)));
	}
		printf("lungimea numelui: %i\n",strlen(namep));
	*/
	//setare question
	question->qtype = htons(tip(type));

	question->qclass = htons(1);
	int dist = sizeof(dns_header_t);
	
	//copiem qname in mesajul final
	if (strcmp("PTR", type) != 0) {
		memcpy(final+dist, namep, strlen(namep) + 1);
		dist += (strlen(namep) + 1);
	}
	else {
		memcpy(final+dist, namep, strlen(namep));
		dist += (strlen(namep));
	}
	//copiem si question in mesajul final
	memcpy(final+dist, question, sizeof(dns_question_t));
	
	return final;
}

//afiseaza rdata in fisierul text
void afisareRData(char* buffer, int rdlength, FILE* file) {
	int i = 0;
	int d = 0;
	fprintf(file, "\t");
	for(i = 0; i < rdlength; i++) {
		if (i < (rdlength-1))
			fprintf(file, "%i.", (unsigned char)(*(buffer+i)));
		else
			if (i == (rdlength-1))
				fprintf(file, "%i", (unsigned char)(*(buffer+i)));
	}
	fprintf(file, "\n");
}

//functie care afiseaza sectiunile=============================================================================================
void afisareSection(int* dist, char* buffer, FILE* out) {
	char* name = (char*)malloc(256); //in name o sa avem numele de domeniu
	int avans;
	int lungime=0;
	//recreem numele de domeniu primit in raspuns
	avans = answerName(buffer + *dist, buffer, name, &lungime);
	//afiseaza numele de domeniu primit in raspuns
	int j = 0;
	for (j = 0; j < lungime; j++) {
		fprintf(out, "%c", (char)(*(name+j)));
	}
	//marim distanta fata de inceputul buffer ului
	*dist += avans+1;
	//luam structura din raspuns
	dns_rr_t* answer = (dns_rr_t*)malloc(sizeof(dns_rr_t));
	memcpy(answer, buffer+ *dist, sizeof(dns_rr_t));
				
	if ((ntohs)(answer->clas) == 1) {
		fprintf(out,"\tIN");
	}
	char* t = tipSimbol((ntohs)(answer->type));
	fprintf(out, "\t%s",t);
	*dist += sizeof(dns_rr_t);
	//afisam si rdata
	char* name1 = (char*)malloc(256);
				
	//preluam si afisam rdata======================================================================================================
	if (ntohs(answer->rdlength) == 4){
		//isneamna ca avem 4 octeti si ii afisam pe fiecare in parte, cu punct intre ei
		afisareRData(buffer + *dist, ntohs(answer->rdlength), out);
		*dist += 4;
	}
	else {
		//apelam aceeasi functie ca atunci cand am aflat numele de domeniu
		lungime = 0;
		avans = answerName(buffer + *dist, buffer, name1, &lungime);
		//afisam in logfile
		fprintf(out,"\t");
		for (j = 0; j < lungime; j++) {
			fprintf(out,"%c", (char)(*(name1+j)));
		}
		fprintf(out,"\n");
		*dist += ntohs(answer->rdlength);
	}
}

//main============================================================================================================================================
int main(int argc, char** argv)
{	
	FILE* out = fopen("logfile", "a");
	if (out == NULL) {
		fprintf(stderr, "nu s-a putut deschide fisierul: logfile");
		exit(0);
	}
	if (argc != 3) {
		fprintf(stderr,"Usage: nume domeniu/adresa ip parametru-tip\n");
		exit(0);
		}
	//SOCKET
	int s = socket(AF_INET, SOCK_DGRAM, 0);
		
	//deschidere fisier
	FILE* f = fopen("dns_servers.conf", "r");
	if (f == NULL) {
		fprintf(stderr, "nu s-a putut deschide fisierul: dns_servers.conf");
		exit(0);
	}
	
	char* name;
	name = argv[1];
	char* type;
	type = argv[2];
	
	
	struct sockaddr_in to_station;
	to_station.sin_family = AF_INET;
	to_station.sin_port = htons(53);
	char line[ 128 ];

	//citire din fisier, linie cu linie si efectuare cereri
	while ( fgets (line, sizeof line, f) != NULL) {
		fputs(line, stdout);
		if (line[0] == '#') {
			//nu conteaza
		}
		else {
			//adresa IP la care trimitem cererea
			to_station.sin_addr.s_addr = inet_addr(line);
			//trimite cerere catre un server DNS===================================================================================================
			char* cerere = getHostByName(name, type);
			int dimensiune;
			//setam dimensiunea diferit, daca avem tipul "PTR"
			if (strcmp("PTR", type) != 0)
				dimensiune = sizeof(dns_header_t) + strlen(name) + 1 + sizeof(dns_question_t);
			else
				dimensiune = sizeof(dns_header_t) + strlen(name) + 13 + sizeof(dns_question_t);
			//se trimite cererea
			int x = sendto(s,cerere, dimensiune+1, 0, (struct sockaddr*) &to_station, sizeof(struct sockaddr));
			//asteapta raspuns====================================================================================================================
			int dim = sizeof(struct sockaddr);
			//in buffer primim cu recvfrom
			char* buffer = (char*)malloc(65536);
			int y = recvfrom(s, buffer, 65536, 0, (struct sockaddr*) &to_station, (socklen_t*)&dim);			
			//printf("y este: %i", y);
			//prelucrare raspuns==================================================================================================================
			dns_header_t* headerr = (dns_header_t*)malloc(sizeof(dns_header_t));
			//luam header-ul din raspuns
			memcpy(headerr, buffer, 12);
			//apoi luam mesajul pe care l-am trimis noi - ne deplasam in vector cu (dim header + dim cerere)
			int dist = dimensiune + 1;
			//printf("Distanta este: %i\n", dist);
			//retinem intr-o variabila numarul de raspunsuri pe care le-am primit
			int nr = ntohs(headerr->ancount);
			//printf("Numar de raspunsuri: %i\n", nr);
			 
			int nr_an = 0;
			//afisam pentru ce am facut cererea si tipul
			fprintf(out, "\n; Trying: %s %s\n", name, type);
			//afisam ANSWER SECTION================================================================================================================
			if (nr > 0)
				fprintf(out, "\n;; ANSWER SECTION:\n");
			for(nr_an = 0; nr_an < nr; nr_an++) {
				afisareSection(&dist, buffer, out);
			}
			//afisam AUTHORITY SECTION=============================================================================================================
			nr = ntohs(headerr->nscount);
			int nr_auth = 0;
			if (nr > 0)
				fprintf(out, "\n;; AUTHORITY SECTION:\n");
			for (nr_auth = 0; nr_auth < nr; nr_auth++) {
				afisareSection(&dist, buffer, out);
			}
			
			//afisam ADDITIONAL SECTION============================================================================================================
			nr = ntohs(headerr->arcount);
			int nr_adit = 0;
			if (nr > 0)
				fprintf(out, "\n;; ADDITIONAL SECTION:\n");
			for (nr_adit = 0; nr_adit < nr; nr_adit++) {
				afisareSection(&dist, buffer, out);
			}
			
			//printf("x este: %i\n", x);
				//daca am primit raspuns si este ok nu mai incercam alta adresa de server DNS
				//daca rcode este 0, inseamna ca am primit raspuns corect si iesim
			if (y > 0 && (headerr->rcode == 0)) {
				exit(0);
			}
		}
	}
	fclose(f);
	fclose(out);
}
