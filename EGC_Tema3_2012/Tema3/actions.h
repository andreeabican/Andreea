//clasa camera
#pragma once
#include "Vector3D.h"
#include <stdlib.h>
#include <string>
#include "cub.h"
#include <vector>

class Actions {
public:
	//in tor avem coordonatele torului, iar in erou, coordonatele eroului
	Vector3D tor, erou;
	std::vector<Vector3D> bonus, inamici;
	std::vector<Vector3D> spatii;
	int indexErou;
public:
	Actions();
	~Actions();

	void desenLabirint(char* fisin, int dim, int lungime, int latime, std::vector<Cub*> *v, int *vectorPereti); // deseneaza labirintul din fisierul de intrare
	void drawTerrain(int lungime, int latime); //deseneaza terenul pe care va fi construit labirintul
};