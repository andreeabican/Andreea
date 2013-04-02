#pragma once
#include <cmath>
#include <iostream>
#include <vector>
#include "Support3d.h"
#include "Transform3d.h"

class Cubulet {
	public:
		Object3d *cub, *fata, *spate, *stanga, *dreapta, *sus, *jos;
		int layerJos, layerSus, layerStanga, layerDreapta, layerFata, layerSpate;
		int layers[6];
	public:
		Cubulet();
		~Cubulet();
		//Deseneaza un cub din cubul rubick, cu fiecare fata colorata diferit
		void DrawCube(int size, CoordinateSystem3d **cs, int x, int y, int z);
		void rotateXrelative(float angles, float x, float y, float z);
		void rotateYrelative(float angles, float x, float y, float z);
		void rotateZrelative(float angles, float x, float y, float z);
};