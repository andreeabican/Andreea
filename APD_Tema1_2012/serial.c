#include <stdio.h>
#include <stdlib.h>

int maxim(int x, int y) {
	int m = (x > y)?x:y;
	return m;
}

void main(int argc, char* argv[]) {
	FILE *fp;
	int nrAni = atoi(argv[1]);
	fp = fopen(argv[2], "r");
	if (fp == NULL) {
		puts("Cannot open file!");
		//return;
	}
	int pMin, pMax, n;
	fscanf(fp, "%i %i %i",&pMin, &pMax, &n);
	printf("pMin, pMax, n: %i, %i, %i \n", pMin, pMax, n);
	
	//Avem 3 matrici in care pastram datele necesare problemei
	//tip, pret, buget
	int r[n][n], r1[n][n], p[n][n], p1[n][n], b[n][n], b1[n][n], c[n][n];
	//rezultatele le retinem intr-o matrice cu nrAni linii si 4 coloane
	//pe fiecare linie avem datele pentru anul corespunzator
	int rezultate[nrAni][4];
	int i, j, k ,l;
	int min, max; //le folosim cand calculam resursa cu cost minim
	int cost, costRes;
	int pretMaxA = pMin, pretMaxB = pMin, nrA = 0, nrB = 0;
	//tip resurse
	for (i = 0; i < n; i++) {
		for (j = 0; j < n; j++) {
			fscanf(fp, "%i", &r[i][j]);
			//printf("%i ", r[i][j]);
		}
		//puts(" ");
	}
	//puts("\n");
	for (i = 0; i < n; i++) {
		for (j = 0; j < n; j++) {
			fscanf(fp, "%i", &p[i][j]);
			//printf("%i ", p[i][j]);
		}
		//puts(" ");
	}
	//puts("\n");
	for (i = 0; i < n; i++) {
		for (j = 0; j < n; j++) {
			fscanf(fp, "%i", &b[i][j]);
			//printf("%i ", b[i][j]);
		}
		//puts(" ");
	}
	//puts("\n");
	fclose(fp);
	
	//Start programming
	//Pentru fiecare colonist
	int an = 0;	
	int pret, pret1;
	int ok = -1;
	for (an = 1; an <= nrAni; an++) {
		ok = an % 2;
		nrA = 0; nrB = 0;
		pretMaxA = pMin;
		pretMaxB = pMin;
		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {		
				cost = 3*n + pMax;
				costRes = 3*n + pMax;
				//Determinam pretul minim cu care poate cumpara o resursa
				for (k = 0; k < n; k++) {
					for (l = 0; l < n; l++) {
						if (ok == 1){
							pret = p[k][l] + (abs(i - k) + abs(j - l)); 
							//pentru resursa complementara
							if (r[k][l] == 1 - r[i][j]) {
								if (pret < cost)
									cost = pret; 
							}
							else {
								//pentru resursa proprie
								if (pret < costRes)
									costRes = pret; 
							}
						}
						else {
								pret = p1[k][l] + (abs(i - k) + abs(j - l)); 
								//pentru resursa complementara
								if (r1[k][l] == 1 - r1[i][j]) {
									if (pret < cost)
										cost = pret; 
								}
								else {
									//pentru resursa proprie
									if (pret < costRes)
										costRes = pret; 
								}
							}
					}
				}
				//pentru an impar
				if (ok == 1) {
					r1[i][j] = r[i][j];
					b1[i][j] = cost;
					if (cost > b[i][j]) {
						p1[i][j] = p[i][j] + cost - b[i][j];
					}
					else {
						if (cost < b[i][j])
							p1[i][j] = p[i][j] + (int)((cost - b[i][j])/2);
						else
							p1[i][j] = costRes + 1;
					}
					if (p1[i][j] < pMin)
						p1[i][j] = maxim(p1[i][j], pMin);
					if (p1[i][j] > pMax) {
						p1[i][j] = (int)((pMin + pMax)/2);
						r1[i][j] = 1 - r[i][j];
						b1[i][j] = pMax;
					}
					//numaram colonistii de un anumit tip
					//si det. preturile maxime
					if (r1[i][j] == 0) {
						nrA++;
						if (p1[i][j] > pretMaxA)
							pretMaxA = p1[i][j];
					}
					else {
						nrB++;
						if (p1[i][j] > pretMaxB)
							pretMaxB = p1[i][j];
					}
				}
				//daca este an par
				else {
					r[i][j] = r1[i][j];
					b[i][j] = cost;
					if (cost > b1[i][j])
						p[i][j] = p1[i][j] + cost - b1[i][j];
					else {
						if (cost < b1[i][j])
							p[i][j] = p1[i][j] + (int)((cost - b1[i][j])/2);
						else
							p[i][j] = costRes + 1;
					}
					if (p[i][j] < pMin)
						p[i][j] = maxim(p[i][j], pMin);
					if (p[i][j] > pMax) {
						p[i][j] = (int)((pMin + pMax)/2);
						r[i][j] = 1 - r1[i][j];
						b[i][j] = pMax;
					}
					//numaram colonistii de un anumit tip
					if (r[i][j] == 0) {
						nrA++;
						if (p[i][j] > pretMaxA)
							pretMaxA = p[i][j];	
					}
					else {
						nrB++;
						if (p[i][j] > pretMaxB)
							pretMaxB = p[i][j];	
					}
				}
			}
		}
		//retinem rezultatele in matrice
		rezultate[an-1][0] = nrA;
		rezultate[an-1][1] = pretMaxA;
		rezultate[an-1][2] = nrB;
		rezultate[an-1][3] = pretMaxB;
	}
	//se afiseaza matricea finala si rezultatele pe fiecare an
	FILE *fp1 = fopen(argv[3], "w");
	if (fp1 == NULL) {
		puts("Cannot open file to write!!!");
	}
	for (i = 0; i < nrAni; i++) {
			fprintf(fp1,"%i %i %i %i", rezultate[i][0], rezultate[i][1], rezultate[i][2], rezultate[i][3]);
			fprintf(fp1,"%s", "\n");
		}
	printf("n este: %i\n", n);
	for (i = 0; i < n; i++) {
		for (j = 0; j < n; j++) {
			if (nrAni % 2 == 1)
				fprintf(fp1,"(%i,%i,%i) ", r1[i][j], p1[i][j], b1[i][j]);
			else
				fprintf(fp1,"(%i,%i,%i) ", r[i][j], p[i][j], b[i][j]);
		}
		fprintf(fp1,"%s", "\n");
	}
	fclose(fp1);
}
























