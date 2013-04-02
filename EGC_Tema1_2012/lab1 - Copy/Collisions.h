#pragma once
#include "HeadersAndDefines.h"
#include "Support2d.h"

//------------------------------------------------------------------------------------------
//Aici sunt implementate toate functiile care trateaza coliziunile
//------------------------------------------------------------------------------------------

class Collisions{
	public:
		Collisions();
		~Collisions();
		//daca se depasesc limitele terenului, atunci inseamna ca are loc o coliziune intre minge si marginea terenului
		static int borderCollision(Object2d *ball, float width, float height, float radius);
		static int ballPlayerCollision(Object2d *ball, std::vector<Object2d*> team1, std::vector<Object2d*> team2, float r1, float r2, int *theTeam);
		static bool circleCircle(Object2d *o1, Object2d *o2, float r1, float r2);
		//returneaza 1 daca se loveste de bara de la echipa 1 si 2 daca se loveste de bara de la echipa 2
		static int baraCollision(Object2d *ball, Object2d *o1, Object2d *o2, Object2d *o3, Object2d *o4, float r1, float r2);
		static bool playerPlayer(std::vector<Object2d*> team1, std::vector<Object2d*> team2, float r1, float r2);
	
};