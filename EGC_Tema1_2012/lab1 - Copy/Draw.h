#pragma once
#include "HeadersAndDefines.h"
#include "Support2d.h"

//------------------------------------------------------------------------------------------
//Aici sunt implementate toate functiile care deseneaaza: terenul, jucatorii, mingea, scorul
//------------------------------------------------------------------------------------------

class Draw{
	public:
		//implemented in worldDrawer2d_gl .. not for lab1
		Draw();
		~Draw();
		static void circle(Object2d** o); //deseneaza un cerc de raza 1 in centrul sist. de coordonate
		static void rectangle(Object2d** o); // deseneaza un patrat de latura 1
		static void border(std::vector<Object2d> *b); //deseneaza marginea terenului si poarta;
		static void players(std::vector<Object2d*> *team, CoordinateSystem2d **cs, float r, float g, float b, int type, float w, float h); //adauga jucatorii in echipa si ii aranjeaza pe pozitiile corespunzatoare
		static void scoreDelimiter(CoordinateSystem2d **cs); //deseneaza x-ul dintre scorul echipei 1 si scorul echipei 2
		static void score(int score, std::vector<Object2d*> *s, CoordinateSystem2d **cs, float x, float y); //afiseaza scorul "score" in sistemul de coordonate cs	
		static void end(int winner, CoordinateSystem2d **cs);
};