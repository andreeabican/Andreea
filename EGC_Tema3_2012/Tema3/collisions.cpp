#pragma once
#include "collisions.h"
#include "cub.h"

Collisions::Collisions() {
}

Collisions::~Collisions() {
}

inline float squared(float v) { return v * v; }

bool Collisions::Collision(std::vector<Cub*> v, float r, Vector3D c) {
/*bool doesCubeIntersectSphere(vec3 C1, vec3 C2, vec3 S, float R)
{*/
	bool ok = false;
	std::vector<Cub*>::iterator it;
	for (it = v.begin(); it < v.end(); it++) {
		Vector3D C1 = (*(*it)).coltSpate;
		Vector3D C2 = (*(*it)).coltFata;
		float dist_squared = r*r;
    /* assume C1 and C2 are element-wise sorted, if not, do that now */
		if (c.x < C1.x) 
			dist_squared -= squared(c.x - C1.x);
		else
			if (c.x > C2.x) 
				dist_squared -= squared(c.x - C2.x);
		/*if (c.y < C1.y) 
			dist_squared -= squared(c.y - C1.y);
		else 
			if (c.y > C2.y) 
				dist_squared -= squared(c.y - C2.y);*/
		if (c.z < C1.z) 
			dist_squared -= squared(c.z - C1.z);
		else 
			if (c.z > C2.z) 
				dist_squared -= squared(c.z - C2.z);
		if (dist_squared > 0) 
			return true;
	}
	return false;
}

bool Collisions::SphereCollision(Vector3D sfera1, float r1, Vector3D sfera2, float r2) {
	float d = (r2 + r1) *(r2 + r1);
	if (d >= (sfera2.x - sfera1.x)*(sfera2.x - sfera1.x) + (sfera2.y - sfera1.y)*(sfera2.y - sfera1.y) + (sfera2.z - sfera1.z)*(sfera2.z - sfera1.z)) {
		return true;
	}
	return false;
}