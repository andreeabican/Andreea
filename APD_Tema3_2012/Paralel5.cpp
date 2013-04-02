#include <stdio.h>
#include <iostream>
#include <fstream>
#include <math.h>
#include "mpi.h"
using namespace std;

#define NUM_COLORS 256

typedef struct {
	double x, y;
} complex;

double modul(complex z) {
	return sqrt(z.x*z.x + z.y*z.y);
}

complex produs(complex a, complex b) {
	complex z = {a.x*b.x - a.y*b.y, a.x*b.y + a.y*b.x};
	return z;
}

complex suma(complex a, complex b) {
	complex z = {a.x + b.x, a.y + b.y};
	return z;
}

int main(int argc, char* argv[]) {
	double receive[9];
	int nrTasks, rank;
	int tag = 0;
	MPI_Status status;
	FILE *input, *output;
	int i, j, tip, MAX_STEPS, step, width, height, color;
	double rez, param_x, param_y, x_min, x_max, y_min, y_max;
	double x, y;
	
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &nrTasks);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	if (rank == 0) {
		double buffer[9];
		/*for (i = 0; i < argc; i++) {
			cout <<"argv[" << i << "] = " << argv[i];
		}*/
		input = fopen(argv[1], "r");
		//citim informatiile din fis de intrare
		fscanf(input, "%i", &tip);
		buffer[0] = (double)tip;
		//printf("\n%i\n", tip);
		fscanf(input, "%lf %lf %lf %lf", &x_min, &x_max, &y_min, &y_max);
		buffer[1] = x_min;
		buffer[2] = x_max;
		buffer[3] = y_min;
		buffer[4] = y_max;
		//printf("%lf, %lf, %lf, %lf\n", x_min, x_max, y_min, y_max);
		fscanf(input, "%lf", &rez);
		buffer[5] = rez;
		printf("%lf\n", rez);
		fscanf(input, "%i", &MAX_STEPS);
		buffer[6] = (double)MAX_STEPS;
		//printf("%i\n", MAX_STEPS);
		if (tip == 1) {
			fscanf(input, "%lf %lf", &param_x, &param_y);
			buffer[7] = param_x;
			buffer[8] = param_y;
			//printf("%lf %lf\n", param_x, param_y);
		}
		else {
			//oricum nu conteaza
			buffer[7] = 0;
			buffer[8] = 0;
		}
		fclose(input);
		//trimitem datele catre toate celelalte procese
		for (i = 1; i < nrTasks; i++) {
			MPI_Send(buffer, 9, MPI_DOUBLE, i, tag, MPI_COMM_WORLD);
		}
	}
	else {
		//trebuie sa primim datele pe care le-a trimis root-ul
		MPI_Recv(receive, 9, MPI_DOUBLE, 0, tag, MPI_COMM_WORLD, &status);
		//cout << rank << " " << "Am primit\n";
		tip = (int)receive[0];
		x_min = receive[1];
		x_max = receive[2];
		y_min = receive[3];
		y_max = receive[4];
		rez = receive[5];
		MAX_STEPS = (int)receive[6];
		if (tip == 1) {
			param_x = receive[7];
			param_y = receive[8];
		}
	}
	
	width = floor((x_max - x_min)/rez);
	height = floor((y_max - y_min)/rez);
	//avem o matrice de dimensiuni width*height
	i = 0; 
	j = 0;
	int deProcesat;
	int ok = 0, index = 0;
	int count;
	if (rank != nrTasks - 1) {
		//printf("width: %i, height: %i, nrTasks: %i\n", width, height, nrTasks);
		deProcesat = ceil(width * height / (double)nrTasks);
		//printf("deProcesat1: %i\n", deProcesat);
		//linia de pe care incepem sa calculam
		i = floor((rank * deProcesat)/(double)width);
		//coloana de pe care incepem sa calculam
		j = (rank * deProcesat) % width;
		count = deProcesat + 3;
	}

	//daca este ultimul procesa, mai procesam doar restul elementelor
	else {
		int aux = ceil(width * height / (double)nrTasks);
		deProcesat = width*height - rank*aux;
		//printf("deProcesat: %i\n", deProcesat);
		i = floor((rank * aux)/(double)width);
		//coloana de pe care incepem sa calculam
		j = (rank * aux) % width;
		count = aux + 3;
	}
	double x_min1, y_min1, x_min2;
	x_min1 = x_min + j * rez;
	y_min1 = y_min + i * rez;
	//daca ok este 0, inseamna ca incepem de la x_min1
	//bufferul pe care il trimitem
	int *send = new int[count];
	send[0] = i;
	send[1] = j;
	send[2] = deProcesat;
	index = 3;
	//Multime Mandelbrot
	if (tip == 0) {
		for (y = y_min1; y < y_max; y = y + rez) {
			if (i < height && deProcesat > 0) {
				if (ok == 0) {
					x_min2 = x_min1;
					ok = 1;
				}
				else {
					x_min2 = x_min;
				}
				for (x = x_min2; x < x_max; x = x + rez) {
					if (j < width && deProcesat > 0) {
						complex c = {x, y};
						complex z = {0, 0};
						step = 0;
						while (modul(z) < 2 && step < MAX_STEPS) {
							z = suma(produs(z, z), c);
							step++;
						}
						color = step % 256;
						send[index] = color;
						index++;
						deProcesat--;
						j++;
					}
				}
				j = 0;
				i++;
			}
		}
	}
	else {
		if (tip == 1) {
			complex c = {param_x, param_y};
			//cout << x_min1 << " " << y_min1 << "\n";
			for (y = y_min1; y < y_max; y = y + rez) {
					if (i < height && deProcesat > 0) {
						if (ok == 0) {
							x_min2 = x_min1;
							ok = 1;
						}
						else {
							x_min2 = x_min;
						}
					//img[i] = new int[width];
					for (x = x_min2; x < x_max; x = x + rez) {
						if (j < width && deProcesat > 0) {
							complex z = {x, y};
							step = 0;
							while (modul(z) < 2 && step < MAX_STEPS) {
								z = suma(produs(z, z), c);
								step++;
							}
							color = step % 256;
							send[index] = color;
							deProcesat--;
							index++;
							j++;
						}
					}
					j = 0;
					i++;
				}
			}
		}
	}
	
	//Trimitem ceea ce am calculat
	if (rank != 0) {
		//cout << "Am trimis " << rank << "\n";
		MPI_Send(send, count, MPI_INT, 0, tag, MPI_COMM_WORLD);
	}
		
	if (rank == 0) {
			int **img;
			img = new int*[height];
			for (i = 0 ; i < height; i++) {
					img[i] = new int[width];
			}
			ok = 0;
			int start, index = 3, elemente;
		//adaugam elementele pe care le-am procesat in acest thread
		elemente = send[2];
		for (i = send[0]; i < height; i++) {
			if (ok == 0) {
				start = send[1];
				ok = 1;
			}
			else {
				start = 0;
			}
			if (elemente > 0)
				for (j = start; j < width; j++) {
					img[i][j] = send[index];
					elemente--;
					index++;
				}
		}
		
		//asteptam mesajele de la toate celelalte procese si adaugam in matrice tot ce primim
		int k;
		//printf("COUNT: %i\n", count);
		int *receive1 = new int[count];
		//printf("A INITIALIZAT--------------------------------------------\n");
		//cout << "Am inceput sa primesc de la ceilalti\n";
		for (k = 1; k < nrTasks; k++) {
			MPI_Recv(receive1, count, MPI_INT, k, tag, MPI_COMM_WORLD, &status);
			ok = 0;
			index = 3;
			elemente = receive1[2];
			for (i = receive1[0]; i < height; i++) {
			if (ok == 0) {
				start = receive1[1];
				ok = 1;
			}
			else {
				start = 0;
			}
			if (elemente > 0)
				for (j = start; j < width; j++) {
					img[i][j] = receive1[index];
					elemente--;
					index++;
				}
			}
			
		}
		//deschidem fisierul pentru scriere
		output = fopen(argv[2], "w");
		fprintf(output, "P2\n");
		fprintf(output, "%i %i\n", width, height);
		fprintf(output, "%i\n", 255);
		/*for (i = width - 1; i >= 0; i--) {
			for (j = 0; j < height; j++) {
				fprintf(output, "%i ", img[i][j]);
			}
			fprintf(output, "\n");
		}*/
		for (i = height - 1; i >= 0; i--) {
			for (j = 0; j < width; j++) {
				fprintf(output, "%i ", img[i][j]);
			}
			fprintf(output, "\n");
		}
		fclose(output);
	}
	MPI_Finalize();
	return 0;
}

















