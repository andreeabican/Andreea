#pragma once
#include "HeadersAndDefines.h"
#include "Support2d.h"
#include <ctime>

//------------------------------------------------------------------------------------------
//Aici sunt implementate toate functiile care deseneaaza: terenul, jucatorii, mingea, scorul
//------------------------------------------------------------------------------------------

class Actions{
	public:
		//implemented in worldDrawer2d_gl .. not for lab1
		Actions();
		~Actions();
		//da mingea unui jucator ales random din echipa team si returneaza numarul jucatorului care are mingea
		static int giveBall(Object2d **ball, std::vector<Object2d*> team1, std::vector<Object2d*> team2, int team, float r1, float r2);
		//schimba jucatorul! jucatorul urmator este jucatorul cu indicele (curent + 1)%7 din echipa team 
		static int changePlayer(std::vector<Object2d*> team1, std::vector<Object2d*> team2, int team, int *curent);
		static void goalKeeper(std::vector<Object2d*> team1, std::vector<Object2d*> team2, int team, int* curent); //jucatorul controlabil devine portarul, pentru cazurile cand trebuie sa aparam :)
};