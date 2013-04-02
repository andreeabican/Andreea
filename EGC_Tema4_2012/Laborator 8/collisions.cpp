#pragma once
#include "collisions.h"
#include "Asteroid.h"

Collisions::Collisions() {
}

Collisions::~Collisions() {
}

bool Collisions::SphereCollision(Vector3D sfera1, float r1, Vector3D sfera2, float r2) {
	float d = (r2 + r1) *(r2 + r1);
	if (d >= (sfera2.x - sfera1.x)*(sfera2.x - sfera1.x) + (sfera2.y - sfera1.y)*(sfera2.y - sfera1.y) + (sfera2.z - sfera1.z)*(sfera2.z - sfera1.z)) {
		return true;
	}
	return false;
}