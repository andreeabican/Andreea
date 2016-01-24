% Rezolva problema
go(problema(Coords, Pachete, Poarta), Solutie) :-
	rezolva(Coords, Pachete, Poarta, Solutie).

rezolva(Coords, pachete(SemnalE, ValE), poarta(SemnalP, EnergP), [Init | Solutie]) :-
	startlocation(Coords, Init),
	afisareFormat1(Init, 0, Coords, SemnalE, SemnalP),
	createNumarVizite(Coords, NumarVizite),
	cresteVizite(Init, Coords, NumarVizite, NumarNouVizite),
	% gaseste locatiile la o distanta mai mica decat 3*SemnalE
	filtruLocatii(Init, SemnalE, Coords, LFiltrate),
	length(LFiltrate, LengthLFiltrate),
	LengthFiltr is LengthLFiltrate + 1,
	random(1, LengthFiltr, Rand),
	getNRandElems(Rand, LFiltrate, LRand, []),
	% In newAdjList se pastreaza lista de adiacenta. La inceput lista de adiacenta este vida.
	insertMoreAdjList(Init, LRand, [], NewAdjList),
	% ia lista cu toate nodurile adiacente pentru locatia curenta(Init aici)
	succesor(Init, NewAdjList, Adiacente),
	% adauga nodul Init in lista de noduri expandate(pentru a nu-l expandata inca odata daca o sa mai trecem prin el)
	% ca la lista de adiacenta, lista de noduri expandate este vida initial
	adaugaElem(Init, [], Expandate),
	decizie(Adiacente, NumarNouVizite, Coords, [], 0, ValE, SemnalE, EnergP, SemnalP, NodDecis),
	getEnergy(NodDecis, Coords, ValE, 0, E1),
	% updateaza lista de adiacente astfel incat NodDecis sa fie adaugat ultimul in lista de adiacenta a locatiei curente
	reAdaugaNodAdiacent(Init, NodDecis, NewAdjList, UpdatedAdjList),
	rezolvarecursiv(NodDecis, NumarNouVizite, UpdatedAdjList, Expandate, Coords, E1, pachete(SemnalE, ValE), poarta(SemnalP, EnergP), Init, Solutie).

% daca nu se poate alege decat poarta, dar nu avem suficienta energie
rezolvarecursiv([-1, -1], _, _, _, _, _, _, _, _, []) :-
	write('Agentul a murit! Nu exista solutie!'), nl.

% daca a ajuns la poarta si are energie suficienta
rezolvarecursiv(Locatie, _, _, _, _, Energ, pachete(_, _), poarta(_, EP), _, [Locatie]) :-
	isGate(Locatie),
	Energ >= EP,
	write('Done!'), nl, !.

% daca a ajuns la poarta dar nu are energie suficienta
rezolvarecursiv(Locatie, _, _, _, _, Energ, pachete(_, _), poarta(_, EP), _, [Locatie]) :-
	isGate(Locatie),
	Energ < EP,
	write('Nu s-a gasit solutie!'), nl, !.


% daca am ajuns la orice alta locatie in afara de poarta si avem un nod
% ce trebuie sa fie expandat
rezolvarecursiv(Locatie, NumarVizite, AdjList, Expandate, Coords, Energ, pachete(SE, VE), poarta(SP, EP), Predec, [Locatie | Solutie]) :-
	not(contine(Locatie, Expandate)),
	% adauga locatia in lista nodurilor expandate
	adaugaElem(Locatie, Expandate, Expandate1),
	% creste numarul de vizite ale acestei locatii
	cresteVizite(Locatie, Coords, NumarVizite, NumarNouVizite),
	updateCoords(Predec, Coords, Coords1),
	afisareFormat1(Locatie, Energ, Coords1, SE, SP),
	% gaseste locatiile la o distanta mai mica decat 3*SemnalE
	filtruLocatii(Locatie, SE, Coords1, LFiltrate),
	% din locatiile obtinute, sterge locatiie catre care deja exista portal de la Locatie
	succesor(Locatie, AdjList, Adiacente),
	stergeLista(Adiacente, LFiltrate, LocsToChoose),
	% din locatiile ramase, alege un numar random de locatii pentru care sa se creeze portale
	length(LocsToChoose, LengthLocsToChoose),
	LengthToChoose is LengthLocsToChoose + 1,
	random(1, LengthToChoose, Rand),
	getNRandElems(Rand, LocsToChoose, LRand, []),
	% adaugam locatiile din LRand in lista de adiacenta
	insertMoreAdjList(Locatie, LRand, AdjList, NewAdjList),
	% ia toate locatiile adiacente acestei locatii(cele care existau inainte + cele generate acum)
	succesor(Locatie, NewAdjList, Adiacente1),
	decizie(Adiacente1, NumarNouVizite, Coords1, Expandate, Energ, VE, SE, EP, SP, NodDecis),
	getEnergy(NodDecis, Coords1, VE, Energ, E1),
	reAdaugaNodAdiacent(Locatie, NodDecis, NewAdjList, UpdatedAdjList),
	rezolvarecursiv(NodDecis, NumarNouVizite, UpdatedAdjList, Expandate1, Coords1, E1, pachete(SE, VE), poarta(SP, EP), Locatie, Solutie).


% daca am ajuns la orice alta locatie in afara de poarta si avem un nod
% care deja a fost expandat - nu mai generam alte portale de la acest
% nod
rezolvarecursiv(Locatie, NumarVizite, AdjList, Expandate, Coords, Energ, pachete(SE, VE), poarta(SP, EP), Predec, [Locatie | Solutie]) :-
	contine(Locatie, Expandate),
	cresteVizite(Locatie, Coords, NumarVizite, NumarNouVizite),
	updateCoords(Predec, Coords, Coords1),
	afisareFormat1(Locatie, Energ, Coords1, SE, SP),
	succesor(Locatie, AdjList, Adiacente),
	decizie(Adiacente, NumarNouVizite, Coords1, Expandate, Energ, VE, SE, EP, SP, NodDecis),
	getEnergy(NodDecis, Coords1, VE, Energ, E1),
	reAdaugaNodAdiacent(Locatie, NodDecis, AdjList, UpdatedAdjList),
	rezolvarecursiv(NodDecis, NumarNouVizite, UpdatedAdjList, Expandate, Coords1, E1, pachete(SE, VE), poarta(SP, EP), Locatie, Solutie).


% Decide catre ce locatie adiacenta sa mearga. Atata timp cat nu avem
% energie suficienta pentru a intra in poarta, evitam poarta si ne
% indreptam catre locatia care primeste cel mai mare semnal de energie.
% Dupa ce am adunat energie suficienta, analizam semnalul primit de la
% poarta si ne indreptam mereu catre nodul care receptioneaza cel mai
% mare semnal de la poarta.

% intram aici atat timp cat nu avem energie suficienta pentru a trece
% prin poarta
decizie(Adiacente, NumarVizite, Coords, Expandate, EnergieAgent, _, SemnalE, EnergiePoarta, SemnalP, NodDecis) :-
	EnergieAgent < EnergiePoarta,
	listaMaxScoreEnergie(Adiacente, NumarVizite, Coords, SemnalE, Expandate, Scores),
	%calculeaza semnalele de la pachetele de energie si de la poarta pentru a le afisa
	listaMaxEnergie(Adiacente, Coords, SemnalE, SemnaleEnergie1),
	listaSemnalPoarta(Adiacente, Coords, SemnalP, SemnalePoarta),
	afisareFormat2(Adiacente, SemnaleEnergie1, SemnalePoarta),
	selecteazaNodSemnalMaxim(Adiacente, Scores, [[-1, -1], -1200], [NodDecis, _]).


% intram aici atunci cand am adunat energie suficienta pentru a trece
% prin poarta. Acum cautam semnal cat mai mare de la poarta.
decizie(Adiacente, NumarVizite, Coords, Expandate, EnergieAgent, _, SemnalE, EnergiePoarta, SemnalP, NodDecis) :-
	EnergieAgent >= EnergiePoarta,
	listaMaxScorePoarta(Adiacente, NumarVizite, Coords, SemnalP, Expandate, Scores),
	listaMaxEnergie(Adiacente, Coords, SemnalE, SemnaleEnergie),
	listaSemnalPoarta(Adiacente, Coords, SemnalP, SemnalePoarta1),
	afisareFormat2(Adiacente, SemnaleEnergie, SemnalePoarta1),
	selecteazaNodSemnalMaxim(Adiacente, Scores, [[-1, -1], -1200], [NodDecis, _]).


% Selecteaza nodul care are cel mai mare scor. Scorurile sunt calculate
% in functie de ce cautam(poarta sau energie si sunt trimise ca
% parametru la aceasta functie)
selecteazaNodSemnalMaxim([], [], [Nod, SemnalMax], [Nod, SemnalMax]).
selecteazaNodSemnalMaxim([A | Adiacente], [S | Semnale], [_, Max], [Nod, Semnal]) :-
	S > Max,
	%write(S), write(', '), write(Max), nl,
	selecteazaNodSemnalMaxim(Adiacente, Semnale, [A, S], [Nod, Semnal]).
selecteazaNodSemnalMaxim([_ | Adiacente], [S | Semnale], [Dummy, Max], [Nod, Semnal]) :-
	S =< Max,
	%write('Al doilea'), write(S), write(Max), nl,
	selecteazaNodSemnalMaxim(Adiacente, Semnale, [Dummy, Max], [Nod, Semnal]).


% calculeaza semnalul maxim de energie pentru fiecare nod din lista de
% adiacenta a locatiei curente
listaMaxEnergie([], _, _, []).
listaMaxEnergie([Node | Rest], Coords, EnergyVal, [Max | Energii]) :-
	maxEnergie(Node, EnergyVal, Coords, 0, Max),
	listaMaxEnergie(Rest, Coords, EnergyVal, Energii).

% calculeaza scorul pentru toate locatiile primite, in functie de
% semnalul primit de la pachetele de energie si numarul de vizite
listaMaxScoreEnergie([], _, _, _, _, []).
listaMaxScoreEnergie([Node | Rest], NumarVizite, Coords, EnergyVal, Expandate, [Max | Scores]) :-
	maxScoreEnergie(Node, NumarVizite, Coords, EnergyVal, Coords, -1200, Max),
	listaMaxScoreEnergie(Rest, NumarVizite, Coords, EnergyVal, Expandate, Scores).


% Calculeaza semnalul maxim de energie primit de nodul Node
% maxenergie(+Node, +EnergyVal, +Coords, -Max)
%maxEnergie(_, _, [], Max, Max).
maxEnergie(_, _, [], Max, Max) :-
	Max >= 0.
maxEnergie(_, _, [], Max, 0) :-
	Max < 0.
maxEnergie(N, EnergyVal, [[X, Y, energy] | RestCoords], Max, Max1) :-
	getXY(N, N1),
	manhattan(N1, [X, Y], Manh),
	Semnal is (EnergyVal - Manh),
	Semnal > Max,
	maxEnergie(N, EnergyVal, RestCoords, Semnal, Max1).

maxEnergie(N, EnergyVal, [[X, Y, energy] | RestCoords], Max, Max1) :-
	getXY(N, N1),
	manhattan(N1, [X, Y], Manh),
	Semnal is (EnergyVal - Manh),
	Semnal =< Max,
	maxEnergie(N, EnergyVal, RestCoords, Max, Max1).

maxEnergie(Node, EnergyVal, [_ | RestCoords], Max, Max1) :-
	maxEnergie(Node, EnergyVal, RestCoords, Max, Max1).


% calculeaza semnalul maxim primit de la poarta pentru fiecare nod din
% lista de adiacenta a locatiei curente
listaSemnalPoarta([], _, _, []).
listaSemnalPoarta([Node | Rest], Coords, SemnalPoarta, [Semnal | Semnale]) :-
	gateLocation(Coords, GateLoc),
	semnalPoarta(Node, SemnalPoarta, GateLoc, Semnal),
	listaSemnalPoarta(Rest, Coords, SemnalPoarta, Semnale).


% listaMaxScorePoarta(+[Node | Rest], +NumarVizite, +Coords, +SemnalP,
% +Expandate, -[Max | Scores])
% calculeaza scorul pentru toate locatiile primite in functie de
% semnalul primit de la poarta si numarul de vizite
listaMaxScorePoarta([], _, _, _, _, []).
listaMaxScorePoarta([Node | Rest], NumarVizite, Coords, SemnalP, Expandate, [Max | Scores]) :-
	gateLocation(Coords, [X, Y, gate]),
	%write('Locatia portii: '), write([X, Y, gate]), nl,
	maxScorePoarta(Node, [X, Y, gate], NumarVizite, Coords, SemnalP, -1200, Max),
	%write('Scor fata de poarta: ' ), write(Max), nl,
	listaMaxScorePoarta(Rest, NumarVizite, Coords, SemnalP, Expandate, Scores).



% SemnalPoarta
% calculeaza semnalul primit de la poarta de catre nodul Node
semnalPoarta(Node, SemnalMaxPoarta, GateLoc, Semnal) :-
	getXY(Node, N),
	getXY(GateLoc, G),
	manhattan(N, G, Manh),
	Semnal1 is (SemnalMaxPoarta - Manh),
	(Semnal1 >= 0,
	Semnal is Semnal1;
	Semnal1 < 0,
	Semnal is 0).

% avem nevoie de o lista cu atatea elemente cat locatiile din coords.
% Vom folosi aceasta lista pentru a numara de cate ori s-a trecut
% printr-un anumit loc - ne va ajuta in cadrul euristicii. Elementele
% listei vor fi initializate cu 0
createNumarVizite([], []).
createNumarVizite([_ | Coords], [0 | NumarVizite]) :-
	createNumarVizite(Coords, NumarVizite).


% mareste numarul de vizite pentru o anumita locatie
cresteVizite(_, [], [], []).
cresteVizite(Locatie, [C | _], [N | NumarVizite], [N1 | NumarVizite]) :-
	getXY(Locatie, [XL, YL]),
	getXY(C, [XC, YC]),
	XL =:= XC,
	YL =:= YC,
	N1 is N + 1.
cresteVizite(L, [_ | Coords], [N | NumarVizite], [N | NumarNouVizite]) :-
	cresteVizite(L, Coords, NumarVizite, NumarNouVizite).

%gasesteVizite(+Locatie, +Coords, +NumarVizite, -NrViziteLocatie)
gasesteVizite(Locatie, [C | _], [N | _], N) :-
	getXY(Locatie, [XL, YL]),
	getXY(C, [XC, YC]),
	XC =:= XL,
	YC =:= YL.
gasesteVizite(Locatie, [_ | Coords], [_ | NumarVizite], N1) :-
	gasesteVizite(Locatie, Coords, NumarVizite, N1).


% afiseaza nodul curent, energia si semnalele de la poarta si de la
% pachetele de energie
afisareFormat1(Nod, Energie, Coords, PachetEnergyVal, SemnalMaxPoarta) :-
	maxEnergie(Nod, PachetEnergyVal, Coords, -1200, SemnalE),
	gateLocation(Coords, GateLoc),
	semnalPoarta(Nod, SemnalMaxPoarta, GateLoc, SemnalP),
	getXY(Nod, [X, Y]),
	write('['), write(X), write(','), write(Y), write('|'), write(Energie), write('|'), write(SemnalP), write(','), write(SemnalE), write(']: ').

% afiseaza toate nodurile adiacente, alaturi de semnalele pe care
% acestea le primesc de la poarta si de la pachetele de energie
afisareFormat2([], [], []) :-
	nl.
	%getXY(A, [X, Y]),
	%write('['), write(X), write(', '), write(Y), write(' | ') , write(SP), write(', '), write(SE), write(']'), nl.
afisareFormat2([A | Adiacente], [SE | SemnaleE], [SP | SemnaleP]) :-
	getXY(A, [X, Y]),
	write('['), write(X), write(', '), write(Y), write(' | ') , write(SP), write(', '), write(SE), write(']'), write(', '),
	afisareFormat2(Adiacente, SemnaleE, SemnaleP).


% calculeaza un scor, in functie de lista Expandate - care ne spune daca
% o locatie a mai fost vizitata
maxScoreEnergie([_, _, gate], _, _, _, _, _, Max1) :-
	Max1 is -1100, !.
maxScoreEnergie(_, _, _, _, [], Max, Max).
maxScoreEnergie(N, NumarVizite, Coords1, EnergyVal, [[X, Y, energy] | Coords], Max, Max1) :-
	getXY(N, N1),
	manhattan(N1, [X, Y], Manh),
	gasesteVizite(N, Coords1, NumarVizite, Vizite),
	Semnal is (EnergyVal - Manh),
	Score is Semnal - Vizite * EnergyVal,
	(Score > Max,
	 maxScoreEnergie(N, NumarVizite, Coords1, EnergyVal, Coords, Score, Max1);
	Score =< Max,
	 maxScoreEnergie(N, NumarVizite, Coords1, EnergyVal, Coords, Max, Max1)).

maxScoreEnergie(Node, NumarVizite, Coords1, EnergyVal, [_ | RestCoords], Max, Max1) :-
	maxScoreEnergie(Node, NumarVizite, Coords1, EnergyVal, RestCoords, Max, Max1).


% calculeaza scorul pentru o locatie atunci cand vrem sa ajungem la
% poarta
maxScorePoarta(N, [X, Y, gate], NumarVizite, Coords, SemnalP, Max, Max1) :-
	getXY(N, N1),
	manhattan(N1, [X, Y], Manh),
	Semnal is (SemnalP - Manh),
	gasesteVizite(N, Coords, NumarVizite, Vizite),
	Score is Semnal - SemnalP * Vizite,
	(Score > Max,
	Max1 is Score;
	 Score =< Max,
	Max1 is Max).

% Returneaza coordonatele
getXY([X, Y, _], [X, Y]).
getXY([X, Y], [X, Y]).

% updateCoords(+Locatie, +Coords, -NewCoords)
% daca locatia continea energie, inseamna ca energia a fost consumata,
% asa ca vom updata aceasta informatie si in lista de coordonate...adica
% vom inlatura tag-ul "energy"
updateCoords(_, [], []).
updateCoords([X, Y, energy], [[X, Y, energy] | Coords], [[X, Y] | Coords]).
updateCoords(L, [H | Coords], [H | NewCoords]) :-
	updateCoords(L, Coords, NewCoords).

% Verifica daca ne aflam pe o locatie cu energie. Daca da, atunci aduna
% energia pachetului la energia curenta. Daca nu, atunci valoarea
% energiei ramane aceeasi.
getEnergy(_, [], _, EnergCurenta, EnergCurenta).
getEnergy([X, Y, energy], [[X, Y, energy] | _], VE, EnergCurenta, SumaEnergie) :-
	SumaEnergie is EnergCurenta + VE.
getEnergy([_, _], _, _, EnergCurenta, EnergCurenta).
getEnergy([X, Y, Tag], [[X, Y, Tag] | _], _, EnergCurenta, EnergCurenta).
getEnergy(Nod, [_ | Coords], VE, EnergCurenta, SumaEnergie) :-
	getEnergy(Nod, Coords, VE, EnergCurenta, SumaEnergie).

% verifica daca nodul reprezinta poarta
isGate([_, _, gate]).


% Returneaza o lista cu locatiile aflate la distanta mai mica decat 3*Se
% fata de locatia N
filtruLocatii(_, _, [], []).
filtruLocatii(N, Se, [H | RestLoc], [H | LFiltrate]) :-
	getXY(N, N1),
	getXY(H, H1),
	manhattan(N1, H1, Dist),
	MaxDist is 3*Se,
	Dist =< MaxDist,
	Dist =\= 0,
	filtruLocatii(N, Se, RestLoc, LFiltrate).
filtruLocatii(N, Se, [H | RestLoc], LFiltrate) :-
	getXY(N, N1),
	getXY(H, H1),
	manhattan(N1, H1, Dist),
	MaxDist is 3*Se,
	(   Dist > MaxDist;
	Dist =:= 0),
	filtruLocatii(N, Se, RestLoc, LFiltrate).

% Gaseste locatia portii
gateLocation([[X, Y, gate] | _], [X, Y, gate]).
gateLocation([_ | Rest], Gate) :-
	gateLocation(Rest, Gate).

% Gaseste locatia initiala
startlocation([[X, Y, initial] | _], [X, Y, initial]).
startlocation([[_, _, _] | Rest], Init) :-
	startlocation(Rest, Init).
startlocation([[_, _] | Rest], Init) :-
	startlocation(Rest, Init).

% insereaza mai multe elemente in lista de adiacenta
insertMoreAdjList(_, [], AdjList, AdjList).
insertMoreAdjList(P, [A | AdjNodes], AdjList, NewAdjList) :-
	insertAdjList(P, A, AdjList, NewAdjList1),
	insertAdjList(A, P, NewAdjList1, NewAdjList2),
	insertMoreAdjList(P, AdjNodes, NewAdjList2, NewAdjList).

% Lista de adiacenta pentru locatii
% insertAdjList(+P, +A, -Lista)
% P si A sunt de forma (x, y)
insertAdjList(P, A, [], [[P, A]]).
insertAdjList(P, A, [[P | Tail] | Rest], [[P, A | Tail] | Rest]) :-
	%not(member(A, Tail)), !.
	not(contine(A, Tail)), !.
insertAdjList(P, A, [[H | Tail] | Rest], [[H | Tail] | Lista1]) :-
	insertAdjList(P, A, Rest, Lista1).

% sterge nodul ca NodDecis pentru Locatia curenta si il adauga din nou
% la sfarsit(Pentru a nu mai fi ales si data viitoare tot acest nod)
reAdaugaNodAdiacent([X, Y, Tag], NodAdiacent, [[[X, Y, Tag] | Rest] | AdjList], [[[X, Y, Tag] | NewRest] | AdjList]) :-
	stergeElem(NodAdiacent, Rest, Rest1),
	adaugaLaSfarsit(NodAdiacent, Rest1, NewRest).
reAdaugaNodAdiacent([X, Y, _], NodAdiacent, [[[X, Y] | Rest] | AdjList], [[[X, Y] | NewRest] | AdjList]) :-
	stergeElem(NodAdiacent, Rest, Rest1),
	adaugaLaSfarsit(NodAdiacent, Rest1, NewRest).
reAdaugaNodAdiacent([X, Y], NodAdiacent, [[[X, Y] | Rest] | AdjList], [[[X, Y] | NewRest] | AdjList]) :-
	stergeElem(NodAdiacent, Rest, Rest1),
	adaugaLaSfarsit(NodAdiacent, Rest1, NewRest).
reAdaugaNodAdiacent([X, Y], NodAdiacent, [[[X, Y, Tag] | Rest] | AdjList], [[[X, Y, Tag] | NewRest] | AdjList]) :-
	stergeElem(NodAdiacent, Rest, Rest1),
	adaugaLaSfarsit(NodAdiacent, Rest1, NewRest).
reAdaugaNodAdiacent(N1, NodAdiacent, [[N2 | Rest] | AdjList], [[N2 | Rest] | NewAdjList]) :-
	reAdaugaNodAdiacent(N1, NodAdiacent, AdjList, NewAdjList).


% Gaseste toti succesorii nodului N
% N - nod, ListaAdj - Lista de adiacenta
succesor(_, [], []).
succesor(N, [[N | Tail] | _], Tail):- !.
succesor([X, Y, _], [[[X, Y] | Tail] | _], Tail) :- !.
succesor([X, Y], [[[X, Y, _] | Tail] | _], Tail) :- !.
succesor(N, [[_ | _] | Rest], Succ) :-
	succesor(N, Rest, Succ).

% adauga in lista(la inceput)
adaugaElem(X, [], [X]).
adaugaElem(X, L, [X | L]).

% adauga un element la sfarsit
adaugaLaSfarsit(X, [], [X]).
adaugaLaSfarsit(X, [H | L], [H | Z]) :-
	adaugaLaSfarsit(X, L, Z).

% Sterge dintr-o lista data, toate elementele din alta lista
stergeLista(_, [], []).
stergeLista([], LR, LR).
stergeLista([H | L1], L, LNew) :-
	stergeElem(H, L, LR),
	stergeLista(L1, LR, LNew).

% Sterge nodul N din lista L
% folosit pentru a sterge nodul predecesor din lista de succesori
stergeElem(_, [], []).
stergeElem([X, Y, _], [[X, Y, _] | T], T) :- !.
stergeElem([X, Y], [[X, Y, _] | T], T) :- !.
stergeElem([X, Y], [[X, Y] | T], T) :- !.
stergeElem([X, Y, _], [[X, Y] | T], T) :- !.
stergeElem(N, [H | T], [H | Rest]):-
	stergeElem(N, T, Rest).


% verifica daca o lista contine elementul dat
contine([X, Y, _], [[X, Y, _] | _]).
contine([X, Y, _], [[X, Y] | _]).
contine([X, Y], [[X, Y, _] | _]).
contine([X, Y], [[X, Y] | _]).
contine(X, [_ | Rest]) :-
	contine(X, Rest).

% ia N elemente random din lista List si
% le returneaza in lista RandL
getNRandElems(_, [], [], _).
getNRandElems(0, _, Acc, Acc).
getNRandElems(N, List, RandL, Acc) :-
	length(List, Len),
	Len1 is Len + 1,
	random(1, Len1, R),
	nth1(R, List, Elem),
	(N1 is N - 1,
	%not(member(Elem, Acc)),
	 not(contine(Elem, Acc)),
	getNRandElems(N1, List, RandL, [Elem | Acc]);
	getNRandElems(N, List, RandL, Acc)).

% calculeaza distanta Manhattan dintre doua puncte
manhattan([X1, Y1], [X2, Y2], D) :-
	  D is (abs(X2 - X1) + abs(Y2 - Y1)).


% cere solutia pentru problema
%go(problema([
%[15, 15], [43, 5], [9, 25, initial], [25, 25, gate],
%[50, 14, energy], [13, 31], [29, 36, energy], [40, 31],
%[51, 33, energy]
%], pachete(17, 10), poarta(20, 25)), Solutie).

% go(problema([[15, 15], [43, 5], [9, 25, initial], [25, 25, gate], [50,
% 14, energy], [13, 31], [29, 36, energy], [40, 31], [51, 33, energy]],
% pachete(17, 10), poarta(20, 25)), Solutie).



% succesor([9, 25], [15, 15]).
% succesor([9, 25], [13, 31]).
% succesor([9, 25], [43, 5]).
% succesor([15, 15], [9, 25]).
% succesor([13, 31], [9, 25]).
% succesor([43, 5], [9, 25]).

% succesor([15, 15], [43, 5]).
% succesor([43, 5], [15, 15]).
% succesor([50, 14], [43, 5]).
% succesor([50, 14], [13, 31]).
% succesor([13, 31], [50, 14]).
% succesor([43, 5], [50, 14]).

% succesor([25, 25], [50, 14]).
% succesor([25, 25], [29, 36]).
% succesor([25, 25], [40, 31]).
% succesor([40, 31], [25, 25]).
% succesor([50, 14], [25, 25]).
% succesor([29, 36], [25, 25]).

% succesor([29, 36], [51, 33]).
% succesor([51, 33], [29, 36]).

% succesor([40, 31], [51, 33]).
% succesor([51, 33], [40, 31]).

% succesor([40, 31], [50, 14]).
% succesor([50, 14], [40, 31]).
