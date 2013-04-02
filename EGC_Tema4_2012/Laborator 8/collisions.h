#pragma once
#include <stdio.h>
#include <stdlib.h>
#include "Vector3D.h"
#include "Asteroid.h"
#include <vector>

class Collisions {
public:
	Collisions();
	~Collisions();
	static bool SphereCollision(Vector3D sfera1, float r1, Vector3D sfera2, float r2);

	Vector3D coltSpate;
	Vector3D coltFata;
};