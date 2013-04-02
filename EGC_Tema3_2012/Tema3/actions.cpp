#pragma once
#include <string>
#include <stdio.h>
#include <fstream>
#include <iostream>
#include "actions.h"
#include "cube.h"
#include <vector>
#include <ctime>

Actions::Actions() {
}

Actions::~Actions() {
}

void Actions::desenLabirint(char* fisin, int dim, int lungime, int latime, std::vector<Cub*> *v, int *vectorPereti) {
	Cube cube;
	//in acest vector adaugam toate coordonatele libere si alegem la intamplare pozitii din acest vector pentru celelalte obiecte din labirint
	/*std::vector<Vector3D> spatii;*/
	float i, j;
	float lung = (float)lungime/2;
	float lat = (float)latime/2;
	i = -lung;
	j = -lat;
	std::string str(fisin);
	std::ifstream infile(fisin);
	std::string line;
	std::srand((unsigned)time(0));
	while (getline(infile, line)) {
		//deseneaza labirintul
		std::string::iterator it;
		for (it = line.begin(); it < line.end(); it++) {
			if ((*it) == '*') {
				if (*vectorPereti == 0) {
					//Cub* cub = new Cub(Vector3D(dim*i+(double)dim/2, 0, dim*j + (double)dim/2), Vector3D(0.66, 0.27, 0.07));
					//in cub pastram coordonatele a doua colturi opuse din cub
					Cub* cub = new Cub(Vector3D(dim*i, -(double)dim/2, dim*j), Vector3D(dim*(i+1), (double)dim/2, dim*(j+1)));
					(*v).push_back(cub);
				}
				glPushMatrix();
					glColor3f(0.54, 0.53, 0.47);
					glTranslatef(dim*i + (double)dim/2, 0, dim*j + (double)dim/2);
					cube.drawCube(dim);
				glPopMatrix();
			}
			else {
				if (*vectorPereti == 0)
					spatii.push_back(Vector3D(dim*i + (double)dim/2, 0, dim*j + (double)dim/2));
			}
			//std::cout << *it;	
			i++;
		}
		i = -lung;
		j++;
		std::cout << std::endl;
	}
	if (*vectorPereti == 0) {
		int dim = spatii.size();
		int random_int = std::rand() % dim;
		std::cout << random_int;
		tor = spatii[random_int];
		int random_int1 = std::rand() % dim;
		while (random_int1 == random_int) {
			random_int1 = std::rand() % dim;
		}
		erou = spatii[random_int1];
		indexErou = random_int1;
	}
	(*vectorPereti) = 1;
}

void Actions::drawTerrain(int lungime, int latime) {
	Cube cube;
	float i, j;
	float lung = (float)lungime/2;
	float lat = (float)latime/2;
	glColor3f(0, 0, 0.7);
	for (i = -lung; i < lung; i++) {
		for (j = -lat; j < lat; j++) {
			glPushMatrix();
				glColor3f(0, 0, 0.7);
				glTranslatef(i + 0.5, -0.5, j + 0.5);
				cube.drawCube(3);
			glPopMatrix();
		}
	}
}