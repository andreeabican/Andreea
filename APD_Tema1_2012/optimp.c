#define min(a,b) (((a) < (b)) ? (a) : (b))
#include <stdio.h>
#include <stdlib.h>
#include <omp.h>


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
	int m1[n][n], m2[n][n], m3[n][n], m4[n][n], r2[n][n], r3[n][n], r4[n][n], r5[n][n];
	
	for (an = 1; an <= nrAni; an++) {
		//pentru fiecare an calculam matricile cu cost si costRes pentru fiecare colonist
		//avem nevoie de 4 matrici, care contin preturile din acel an
		//folosim matricile m1, m2, m3, m4 pentru resursa 1 si matricile r5, r2, r3, r4 pentru resursa 0;
		ok = an % 2;
		//initializam toate cele 8 matrici
		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				if (ok == 1) {
					if (r[i][j] == 1) {
						m1[i][j] = p[i][j];
						m2[i][j] = p[i][j];
						m3[i][j] = p[i][j];
						m4[i][j] = p[i][j];
						r5[i][j] = 2*n + pMax;
						r2[i][j] = 2*n + pMax;
						r3[i][j] = 2*n + pMax;
						r4[i][j] = 2*n + pMax;
					}
					else {
						m1[i][j] = 2*n + pMax;
						m2[i][j] = 2*n + pMax;
						m3[i][j] = 2*n + pMax;
						m4[i][j] = 2*n + pMax;
						r5[i][j] = p[i][j];
						r2[i][j] = p[i][j];
						r3[i][j] = p[i][j];
						r4[i][j] = p[i][j];
					}
				}
				else {
					if (r1[i][j] == 1) {
						m1[i][j] = p1[i][j];
						m2[i][j] = p1[i][j];
						m3[i][j] = p1[i][j];
						m4[i][j] = p1[i][j];
						r5[i][j] = 2*n + pMax;
						r2[i][j] = 2*n + pMax;
						r3[i][j] = 2*n + pMax;
						r4[i][j] = 2*n + pMax;
					}
					else {
						m1[i][j] = 2*n + pMax;
						m2[i][j] = 2*n + pMax;
						m3[i][j] = 2*n + pMax;
						m4[i][j] = 2*n + pMax;
						r5[i][j] = p1[i][j];
						r2[i][j] = p1[i][j];
						r3[i][j] = p1[i][j];
						r4[i][j] = p1[i][j];
					}
				}
			}
		}
		
		//parcurgem matricile incepand din coltul stanga sus, dreapta sus, dreapta jos si stanga jos.
		/* Pentru determinarea minimului, procedam astfel:
			Cand pornim din stanga sus:
				comparam mereu cu elementul din stanga si cu cel de sus. Alegem cel mai mic element + 1, pentru a se aduna si drumul la cost
			Cand pornim din dreapta jos
				comparam mereu cu elemntele din dreapta si de jos
			Cand pornim din stanga jos
				comparam cu elementele din stanga si de jos
			Cand pornim din dreapta sus
				comparam mereu cu elementele din dreapta si sus
			Intotdeau a procedam ca la primul caz. Alegem cel mai mic element, daca unul dintre ele este mai mic si inlocuim valoarea de pe pozitia i,j cu valoarea celui mai mi element + 1*/
		int x, y;
		#pragma omp parallel for private(i,j,x,y,k,l)
		for (i = 0; i < n; i++) {
			for ( j = 0; j < n; j++) {
				//pornim din stanga sus
				k = i;
				l = j;
				x = k - 1;
				y = l - 1;
				#pragma omp sections
				{
				#pragma omp section
				if (x >= 0) {
					if (m1[x][l] < m1[k][l])
						m1[k][l] = m1[x][l] + 1;
						
					if (r5[x][l] < r5[k][l])
						r5[k][l] = r5[x][l] + 1;
				}
				#pragma omp section
				if (y >= 0) {
					if (m1[k][y] < m1[k][l])
						m1[k][l] = m1[k][y] + 1;
					
					if (r5[k][y] < r5[k][l])
						r5[k][l] = r5[k][y] + 1;
					
				}
				}
				//pornim din stanga jos
				k = n - i - 1;
				l = j;
				x = k + 1;
				y = l - 1;
				#pragma omp sections
				{
				#pragma omp section
				if (x < n) {
					if (m2[x][l] < m2[k][l])
						m2[k][l] = m2[x][l] + 1;
						
					if (r2[x][l] < r2[k][l])
						r2[k][l] = r2[x][l] + 1;
				}
				#pragma omp section
				if (y >= 0) {
					if (m2[k][y] < m2[k][l])
						m2[k][l] = m2[k][y] + 1;
					
					if (r2[k][y] < r2[k][l])
						r2[k][l] = r2[k][y] + 1;
				}
				}
				
				//dreapta sus
				k = i;
				l = n - j - 1;
				x = k - 1;
				y = l + 1;
				#pragma omp sections
				{
				#pragma omp section
				if (x >= 0) {
					if (m3[x][l] < m3[k][l])
						m3[k][l] = m3[x][l] + 1;
						
					if (r3[x][l] < r3[k][l])
						r3[k][l] = r3[x][l] + 1;
				}
				#pragma omp section
				if (y < n) {
					if (m3[k][y] < m3[k][l])
						m3[k][l] = m3[k][y] + 1;
					
					if (r3[k][y] < r3[k][l])
						r3[k][l] = r3[k][y] + 1;
				}
				}
				//dreapta jos
				k = n - i - 1;
				l = n - j - 1;
				x = k + 1;
				y = l + 1;
				#pragma omp sections
				{
				#pragma omp section
				if (x < n) {
					if (m4[x][l] < m4[k][l])
						m4[k][l] = m4[x][l] + 1;
						
					if (r4[x][l] < r4[k][l])
						r4[k][l] = r4[x][l] + 1;
				}
				#pragma omp section
				if (y < n) {
					if (m4[k][y] < m4[k][l])
						m4[k][l] = m4[k][y] + 1;
					
					if (r4[k][y] < r4[k][l])
						r4[k][l] = r4[k][y] + 1;
				}
				}
			}
		}
		
		//mai parcurgem odata fiecare matrice si determinam minimul dintre toate
		nrA = 0; nrB = 0;
		pretMaxA = pMin;
		pretMaxB = pMin;
		#pragma omp parallel for private(i,j,cost,costRes) reduction(+:nrA,nrB)
		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				if (ok == 1) {
					if (r[i][j] == 0) {
						cost = min(min(m1[i][j], m2[i][j]), min(m3[i][j], m4[i][j]));
						costRes = min(min(r5[i][j], r2[i][j]), min(r3[i][j], r4[i][j]));
					}
					else {
						costRes = min(min(m1[i][j], m2[i][j]), min(m3[i][j], m4[i][j]));
						cost = min(min(r5[i][j], r2[i][j]), min(r3[i][j], r4[i][j]));
					}	
				}
				else {
					if (r1[i][j] == 0) {
						cost = min(min(m1[i][j], m2[i][j]), min(m3[i][j], m4[i][j]));
						costRes = min(min(r5[i][j], r2[i][j]), min(r3[i][j], r4[i][j]));
					}
					else {
						costRes = min(min(m1[i][j], m2[i][j]), min(m3[i][j], m4[i][j]));
						cost = min(min(r5[i][j], r2[i][j]), min(r3[i][j], r4[i][j]));
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
						#pragma omp critical
						if (p[i][j] > pretMaxA)
							pretMaxA = p[i][j];	
					}
					else {
						nrB++;
						#pragma omp critical
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
























