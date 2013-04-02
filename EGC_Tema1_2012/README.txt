Badoiu Simona Andreea 321CB

Pentru aceasta tema, desenele au fost facute doar din triunghiuri, asa cum s-a cerut in enunt.
Am scris o functie care deseneaza un cerc cu raza 1 si o functie care deseneaza un patrat cu latura 1. Cu ajutorul acestor doua functii am desenat tot ce a fost nevoie.
Coliziunea mingei cu marginile terenului: daca mingea are coordonata centrului mai mica decat (marginea stanga - raza) sau mai mare decat (marginea dreapta - raza). Analog si pentru sus jos.
Pentru gol, m-am folosit tot de dimensiunile portii si de dimensiunile terenului, pentru a determina cand mingea este in poarta si cand nu.

Pentru desenarea scorului am mai scris o functie. Scorul este afisat dupa sistemul cu 7 segmente.

Jucatorii de la fiecare echipa pot fi controlati de pe taste diferite, asa ca jocul chiar se poate juca in doi, cu exceptia faptului ca nu se pot apasa 2 taste in acelasi timp.
Tastele de control sunt:
Echipa albastra:
- w,a,s,d - sus, stanga, jos, dreapta
- q, e - rotire sens trigonometric, rotire in sensul acelor de ceasornic
- c - schimba jucatorul
- f - schimba jucatorul(iese cu portarul)
- ' ' - sut

Echipa in rosu:
- 8,4,2,6 - sus, stanga, jos, dreapta
- 7,9 - rotire sens trigonometric, rotire in sensul acelor de ceasornic
- + - schimba jucatorul
- 3 - schimba jucatorul(iese cu portarul)
- 0 - sut

Coliziunile dintre minge si jucatori le-am facut cu ajutorul formulei
(x2 - x1)^2 + (y2 - y1)^2 <= (r1 + r2)^2
Aceeasi formula a fost folosita si pentru coliziunile cu bara.

La reflexia mingei in marginile terenului, in functie de marginea in care se lovea, am schimbat semnul valorii cu care se translata pe x sau pe y. De exemplu, daca se loveste in marginile laterale, se schimba semnul pentru x, iar y ramane neschimbat, iar daca se loveste in marginile de sus sau de jos, se schimba semnul lui y, iar x ramane neschimbat.

Directia pe care pleaca mingea din jucator este data de (x2 - x1) si (y2 - y1), unde x1, y1, x2, y2 sunt coordonatele centrelor jucatorului si mingei.

Se poate schimba jucatorul atata timp cat nimeni din echipa nu are mingea.

Coliziunile dintre minge si jucatori se detecteaza atata timp cat niciun jucator nu are mingea.
Daca o echipa da 3 goluri, jocul reincepe. La sfarsitul meciului, se afiseaza un patrat colorat in culoarea echipei castigatoare.
