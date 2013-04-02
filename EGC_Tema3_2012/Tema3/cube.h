//clasa cube
#pragma once
#include "Vector3D.h"
#include "glut.h"
#include <vector>
#include "Cub.h"

class Cube {
public:
	Cube();
	~Cube();

	void drawCube(int x); // x - dimensiunea cubului
};