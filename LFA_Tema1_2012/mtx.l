%{

#include <math.h>
#include <vector>
#include <string>
#include <iostream>
#include <map>

struct masina {
	//are valoarea 1 daca avem tranzitii si zero daca nu
	int okTranzitii;
	std::string c;
	std::string name;
	//chestiile astea pentru loop nu au niciun rost in celelalte masini, in afara de cea initiala
	std::vector<std::string> loops;
	std::vector<masina*> adreseloops;
	//daca nu avem tranzitii trecem la urmatoarea pereche
	masina* next;
	//daca avem tranzitii, verificam pe care element din vector ne aflam si continuam de acolo
	std::vector<std::string> tranzitii;
	std::vector<masina*> tranzitie;
};

/* aici avem toate masinile pe care le-am citit din fisierul de intrare*/
std::vector<masina*> masini;
std::vector<char> alfabet;
std::vector<masina*> vectorTranzitii;
int n = 0;
int okTranzitii = 0, okLoop = 0, okt = 0;
masina* masinaCurenta;

/*===========================================================================
===================== Masinile elementare ===================================
===========================================================================*/

//muta cursorul cu o pozitie la stanga(primeste pozitia cursorului si sirul pe care trebuie sa il modifice)
//se intoarce sirul modifica si pozitia cursorului actualizata
void Left(int* p, std::string* s) {
	int poz = *p;
	if (poz == 0) {
		(*s)[poz] = '#';
		*s = ">" + *s;
	}
	if (poz > 0) {
		char aux = (*s)[poz];
		(*s)[poz] = (*s)[poz - 1];
		(*s)[poz - 1] = aux;
	}
	if (poz > 0)
		*p = poz - 1; 
}

void Right(int *p, std::string* s) {
	int poz = *p;
	int l = (*s).length();
	if (poz == l-2) {
		(*s)[poz] = (*s)[poz+1];
		(*s)[poz+1] = '>';
		*s = *s + "#";
	}
	if (poz < l - 2) {
		char aux = (*s)[poz];
		(*s)[poz] = (*s)[poz + 1];
		(*s)[poz+1] = aux;
	}
	*p = poz + 1;
}

void LeftToChar(int *p, std::string* s, char c) {
	int i, l, poz, pozc;
	poz = *p;
	pozc = -1;
	l = (*s).length();
	//determinam pozitia carcaterului c
	for (i = poz-1; i >= 0; i--) {
		if ((*s)[i] == c) {
			pozc = i;
			break;
		}	
	}
	if (pozc != -1) {
		if (pozc == 0) {
			(*s).erase(poz, 1);
			*s = ">" + *s;
			*p = 0;
		}
		else {
			(*s).erase(poz, 1);
			(*s).insert(pozc, ">");
			*p = pozc;
		}
	}
	else {
		if (c == '#') {
			(*s).erase(poz, 1);
			*s = ">#" + *s;
			*p = 0;
		}
	}
}


void LeftToNotChar(int *p, std::string* s, char c) {
	int i, l, poz, pozc;
	poz = *p;
	pozc = -1;
	l = (*s).length();
	//determinam pozitia carcaterului c
	for (i = poz-1; i >= 0; i--) {
		if ((*s)[i] != c) {
			pozc = i;
			break;
		}	
	}
	if (pozc != -1) {
		if (pozc == 0) {
			(*s).erase(poz, 1);
			*s = ">" + *s;
			*p = 0;
		}
		else {
			(*s).erase(poz, 1);
			(*s).insert(pozc, ">");
			*p = pozc;
		}
	}
	else {
		if (c == '#') {
			(*s).erase(poz, 1);
			*s = ">#" + *s;
			*p = 0;
		}
	}
}



void RightToChar(int *p, std::string* s, char c) {
	int i, l, poz, pozc;
	poz = *p;
	pozc = -1;
	l = (*s).length();
	//determinam pozitia carcaterului c
	for (i = poz+2; i < l; i++) {
		if ((*s)[i] == c) {
			pozc = i;
			break;
		}	
	}
	if (pozc != -1) {
		(*s).erase(poz, 1);
		pozc--;
		(*s).insert(pozc, ">");
		*p = pozc;
	}
	else {
		if (c == '#') {
			(*s).erase(poz, 1);
			*s = *s + ">#";
			*p = l-1;
		}
	}
}

void RightToNotChar(int *p, std::string* s, char c) {
	int i, l, poz, pozc;
	poz = *p;
	pozc = -1;
	l = (*s).length();
	//determinam pozitia carcaterului c
	for (i = poz+2; i < l; i++) {
		if ((*s)[i] != c) {
			pozc = i;
			break;
		}	
	}
	if (pozc != -1) {
		(*s).erase(poz, 1);
		pozc--;
		(*s).insert(pozc, ">");
		*p = pozc;
	}
	else {
		if (c == '#') {
			(*s).erase(poz, 1);
			*s = *s + ">#";
			*p = l-1;
		}
	}
}


void Replace(int* p, std::string* s, char c) {
	(*s)[(*p) + 1] = c;
}

//sterge toate # - urile de la inceput si de la sfarsit si adauga cate unul in fiecare parte pentru a avea la sfarsit un sir de forma #orice# indiferent de situatie, deoarece pe parcurs se strang mai multe # uri la un capat si la celalalt in functie de operatiile pe care le executam
void BandaInfinita(std::string* s) {
	int found;
	found = (*s).find_first_not_of("#");
	//sterge tot pana acolo
	if (found > 0) {
		(*s) = (*s).substr(found);
	}
	found = (*s).find_last_not_of("#");
	if (found < (*s).length() - 1 ) {
		(*s) = (*s).substr(0, found+1);
	}
	(*s) = "#" + *s;
	*s = *s + "#";
}

//executa o masina(neelementara) - executa toti pasii pe care aceasta ii descrie
void Execute(int* p, std::string* s, std::string mas) {
	int l, i;
	masina *m, *next; 
	l = masini.size();	
	//cauta in vectorul de masini, masina care trebuie executata
	for (i = 0; i < l; i++) {
		if (masini[i]->name == mas) {
			m = masini[i];
			break;
		}
	}
	next = m->next;
	//executie masina complexa
	while (next) {
		std::string str = next->name;
		if (str == "litera") {
			Replace(p, s, (next->c)[0]);
			//next = next->next;
		}
		else {
			if (str == "bucla") {
				l = m->loops.size();
				//cauta numele buclei in loops
				for (i = 0; i < l; i++) {
					if ((m->loops)[i] == next->c)
						break;
				}
				next = (m->adreseloops)[i];
				continue;
			}
			else {
				//verificam daca e o masina elementara
				if (str == "L") {
					if (next->c == "") {
						//executam left simplu
						Left(p, s);
					}
					else {
						if ((next->c)[0] == '!') {
							LeftToNotChar(p,s,(next->c)[1]);
						}
						else {
							LeftToChar(p, s, (next->c)[0]);
						}
					}
				}
				else {
					if (str == "R") {
						if (next->c == "") {
							Right(p,s);
						}
						else {
							if ((next->c)[0] == '!') {
								RightToNotChar(p,s,(next->c)[1]);
							}
							else {
								RightToChar(p,s,(next->c)[0]);
							}
						}
					}
					else {
						//trebuie sa se execute o alta masina complexa => apelam Execute din nou
						Execute(p, s, str);
					}
				}
			}
		}
		if (next->okTranzitii == 1) {
			char c = (*s)[(*p) + 1];
			int j;
			l = (next->tranzitii).size();
			int ok = 0;
			for (i = 0 ; i < l; i++) {
				std::string aux = (next->tranzitii)[i];
				for (j = 0; j < aux.length(); j++) {
					if (c == aux[j]) {
						ok = 1;
						break;
					}
				}
				if (ok == 1)
					break;
			}
			if (ok == 1) {
				next = (next->tranzitie)[i];
			}
			else
				break;
		}
		else {
			next = next->next;
		}
	}
}


%}

DIGIT	[0-9]
ID	[a-z][a-z0-9]*
ACTION [A-Za-z0-9_]*
space	[ ]
%s ALFA
%s JUMP
%s MASINA
%s INMASINA

%%
	using namespace std;
alphabet{space}:: { BEGIN(ALFA); }
<ALFA>\n
<ALFA>[^; ]	{ 
	alfabet.push_back(yytext[0]);	
	}

<ALFA>;	{ BEGIN(INITIAL); }
	

[\n\t ]		
^;	{ BEGIN(JUMP); }
<JUMP>.
<JUMP>\n	{
	BEGIN(INITIAL); 
	}	
^\n
{ACTION}{space}::= {
	string s = yytext;
	int found = s.find(" ");
	s = s.substr(0, found);
	//cout << s << ", " << endl;
	masina* m = new masina;
	m->name = s; 
	masini.push_back(m);
	masinaCurenta = m;
	n++;
	BEGIN(MASINA);
	}
	
<MASINA>{ID}@ {
	//adauga numele structurii repetitive in vectorul destinat acesteia
	string s = yytext;
	s = s.substr(0, s.size() - 1);
	masini[n-1]->loops.push_back(s);
	okLoop = 1;
	}
<MASINA>"["[a-zA-Z0-9#$_@*()<>!]*"]" {
		//daca okLoop este 1, atunci masina pe care o avem descrisa aici va fi adaugata pe pozitia corespunzatoare loop ului pe care tocmai l-am citit	
		masina* m = new masina;
		m->next = NULL;
		m->okTranzitii = 0;
		string s = yytext;
		int found = s.find("(");	
		//daca exista paranteze rotunde, adica masina turing apelata cauta un caracter sau il ocoleste
		if (found != -1) {
			string str1 = s.substr(1, found-1);
			s = s.substr(found+1);
			found = s.find(")");
			s = s.substr(0, found);
			m->c = s;
			m->name = str1;
			}
			else {
				//tot sirul de caractere(cu exceptia parantezelor patrate) reprezinta numele masinii apelate	
				s = s.substr(1, s.size()-2);
				int i, aux = 0;
				for (i = 0; i < alfabet.size(); i++) {
					//daca acest caracter exista in alfabet, inseamna ca aici avem apelata masina turing care inlocuieste un caracter cu acest caracter
					if (s[0] == alfabet[i]) {
						m->c = alfabet[i];
						m->name = "litera";
						aux = 1;
						break;
					}
				}
				if (aux != 1) {
					m->name = s;
					m->c = "";
					}
				else {
					aux = 0;
				}
			}
			if (okt == 0) {
				m->okTranzitii = 0;
				masinaCurenta->next = m;
				masinaCurenta = m;
			}
			else {
				okt = 0;
				//daca am gasit paranteze rotunde, care inseamna inceput de tranzitii, adaugam pe pozitia pe care trebuie sa adaugam
				masinaCurenta->tranzitie.push_back(m);
				masinaCurenta = m;
			}
			
			if (okLoop == 1) {
				okLoop = 0;
				masini[n-1]->adreseloops.push_back(m);
			}
	}
<MASINA>"(" { 
		okTranzitii++;
		masinaCurenta->okTranzitii = 1;
		vectorTranzitii.push_back(masinaCurenta);	
	}
<MASINA>"{"[a-zA-Z0-9#$_@*()<>, ]*"}" {
	okt = 1;
	if (okTranzitii != 0)
		masinaCurenta = vectorTranzitii.back();
	int i;
	string s1 = "";
	string s = yytext;
	s=s.substr(1, s.size() - 2);
	for (i = 0; i < s.size(); i++) {
		if (s[i] != ' ' && s[i] != ',') {
			s1 = s1 + s[i];
		}
	}
	masinaCurenta->tranzitii.push_back(s);
	}
<MASINA>")" {
	okTranzitii--;
	vectorTranzitii.pop_back();
	}
<MASINA>;; {BEGIN(INITIAL);}
<MASINA>; {
	masinaCurenta = vectorTranzitii.back();
	}
<MASINA>&{ID} {
	string s = yytext;
	masina *m = new masina;
	m->name = "bucla";
	m->c = s.substr(1);
	m->okTranzitii = 0;
	masinaCurenta->next = m;
	masinaCurenta = m;
	}
<MASINA>[\n\t-> ]
<MASINA>^\n
<MASINA>^\t
<MASINA>\n$
%%
int main(int argc, char** argv)
{
	/* sarim peste numele programului*/
	++argv;
	--argc;
	if (argc > 0)
		yyin = fopen( argv[0], "r");
	else
		printf("Nu s-au primit date de intrare suficiente\n");
	std::string s = argv[2];
	std::string masinaTuring = argv[1];
	yylex();
	int i, index;
	//gasim pozitia initiala a capului de citire
	for (i = 0; i < s.length(); i++) {
		if (s[i] == '>') {
			index = i;
			break;
		}
	}
	Execute(&index, &s, masinaTuring);
	BandaInfinita(&s);
	std::cout << s << std::endl;
}

























