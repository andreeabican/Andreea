#pragma once
#include <stdio.h>
#include <stdlib.h>
#include "Vector3D.h"

class Cub {
public:
	Cub();
	Cub(Vector3D coltSpate, Vector3D coltFata);
	~Cub();

	Vector3D coltSpate;
	Vector3D coltFata;
};