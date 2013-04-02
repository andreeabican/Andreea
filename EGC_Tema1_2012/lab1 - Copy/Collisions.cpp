#include "Collisions.h"

Collisions::Collisions() {
}

Collisions::~Collisions() {
}

int Collisions::borderCollision(Object2d *ball, float width, float height, float radius) {
	float rightLimit = width/2 - radius/2;
	float leftLimit = -width/2 + radius/2;
	float topLimit = height/2 - radius/2;
	float bottomLimit = -height/2 + radius/2;
	float x = ball->points[0].x;
	float y = ball->points[0].y;
	if ((x > -3.3+radius/2) && (x < 3.3-radius/2) && (((y >= topLimit) && (y < topLimit + 1.5f)) || ((y <= bottomLimit) && (y > bottomLimit - 1.5f)))) {
		//Gooooolllll!!!
		return 3;
	}
	else
		if (y > topLimit + 1.5f) {
			//A ajuns la bara din spate a portii si este coliziune, dar nu trebuie sa se intoarca, asa ca daca este gol, mingea este imediat
			//data echipei care a primit gol
			return 4; // echipa 1 a primit gol
		}
		else
			if (y < bottomLimit - 1.5f) {
				return 5; // echipa 2 a primit gol
			}
	if ((x < rightLimit) && (x > leftLimit) && (y < topLimit) && (y > bottomLimit)) {
		//Mingea este in teren
		return 0;
	}
	//Are loc o coliziune cu una din marginile terenului
	if (x >= rightLimit || x <= leftLimit)
		return 1;
	else
		if (y >= topLimit || y <= bottomLimit)
			return 2;
}

bool Collisions::circleCircle(Object2d *o1, Object2d *o2, float r1, float r2) {
	float x1 = o1->points[0].x, x2 = o2->points[0].x;
	float y1 = o1->points[0].y, y2 = o2->points[0].y;
	float dx = x2-x1;
	float dy = y2-y1;
	float r = r1 + r2;

	if (dx*dx + dy*dy < r*r)
		return true;
	return false;
}

//r1 raza mingei, r2 raza jucatorului, theTeam - echipa care are mingea
int Collisions::ballPlayerCollision(Object2d *ball, std::vector<Object2d*> team1, std::vector<Object2d*> team2, float r1, float r2, int *theTeam) {
	int n = team1.size(), i;
	for (i = 0; i < n; i++) {
		if (circleCircle(ball, team1[i], r1, r2)) {
			(*theTeam) = 1;
			return i;
		}
		if (circleCircle(ball, team2[i], r1, r2)) {
			(*theTeam) = 2;
			return i;
		}
	}
	return -1;
}

int Collisions::baraCollision(Object2d *ball, Object2d *o1, Object2d *o2, Object2d *o3, Object2d *o4,  float r1, float r2) {
	int i = 0;
		if (circleCircle(ball, o1, r1, r2) || circleCircle(ball, o2, r1, r2)) {
			std::cout << "In fct\n";
			return 1;
		}
		else
			if (circleCircle(ball, o3, r1, r2) || circleCircle(ball, o4, r1, r2)) {
				std::cout << "In fct\n";
				return 2;
			}
		return -1;
}

bool Collisions::playerPlayer(std::vector<Object2d*> team1, std::vector<Object2d*> team2, float r1, float r2) {
	int n = team1.size(), i,j;
	for (i = 0; i < n; i++) {
		for (j = 0; j < n; j++) {
			if (circleCircle(team1[i], team2[j], r1, r2)) {
				return true;
			}
		}
	}
	return false;
}