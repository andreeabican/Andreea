Am implementat tema incepand cu laboratorul 2.
Dupa 10 mutari se incepe rezolvarea cubului.
Atunci cand incepe rezolvarea cubului, se afiseaza un cub in dreapta sus.
Tastele pe care se misca cubul:
	Q - stanga, fata de sus, W - dreapta, fata de sus
	A - stanga, fata de jos, S - dreapta, fata de jos
	E - sus, fata din stanga, D - jos, fata din stanga
	R - sus, fata din dreapta, F - jos, fata din dreapta
	C - stanga, fata din fata, V - dreapta, fata din fata
	B - stanga, fata din spate, N - dreapta, fata sin spate
	
	Pe x, y si z se roteste cubul pe cele 3 axe ale sistemului de coordonate.
	
Pentru cub, am desenat 27 de cuburi negre si pe fiecare fata a cubului negru am desenat cate un patrat de o anumita culoare.
Am retinut, pentru fiecare cub, intr-un vector de 6 elemente, 1/0, asta seminificand se afla/nu se afla pe acea fata.

Vectorul este de forma: Stanga, Dreapta, Jos, Sus, Spate, Fata

Pentru a determina cand este rezolvat cubul, am salvat "cubul" de la inceput si il compar mereu cu varianta actuala. Daca sunt identice, atunci este rezolvat.

Pentru fereastra de la sfarsit am incercat ceva dar nu afiseaza.