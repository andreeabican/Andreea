#include "Actions.h"

Actions::Actions() {
}

Actions::~Actions() {
}

int Actions::giveBall(Object2d **ball, std::vector<Object2d*> team1, std::vector<Object2d*> team2, int teamNr, float r1, float r2) {
	srand(time(NULL));
	int player = rand()%6; 
	std::cout << player << " jucatorul\n";
	//daca dam mingea echipei 1
	//gaseste coordonatele jucatorului care trebuie sa aiba mingea
	if (teamNr == 1) {
		float x = team1[player]->points[0].x;
		float y = team1[player]->points[0].y;
		//am scazut doar r2/2 adica diametru mingei pe 2 deoarece trebuia sa colizoneze, nu sa fie fix unul langa celalalt. Daca as
		//fi scazut r2, coordonatele ar fi fost una langa cealalta, dar nu ar fi coincis...cel putin asa aparea
		(*ball)->translate(x - (*ball)->points[0].x, y - (*ball)->points[0].y - r1 - r2/2);
		team1[player]->setcolor(0,0,0.5f);
	}
	else {
		float x = team2[player]->points[0].x;
		float y = team2[player]->points[0].y;
		//am scazut doar r2/2 adica diametru mingei pe 2 deoarece trebuia sa colizoneze, nu sa fie fix unul langa celalalt. Daca as
		//fi scazut r2, coordonatele ar fi fost una langa cealalta, dar nu ar fi coincis...cel putin asa aparea
		(*ball)->translate(x - (*ball)->points[0].x, y - (*ball)->points[0].y + r1 + r2/2);
		team2[player]->setcolor(0.5f,0,0);
	}
	return player;
}

//schimba culoarea jucatorului care este controlabil
int Actions::changePlayer(std::vector<Object2d*> team1, std::vector<Object2d*> team2, int team, int *curent) {
	int i = (*curent + 1) % 6;
	if (team == 1) {
		team1[*curent]->setcolor(0,0,1);
		team1[i]->setcolor(0,0,0.5f);
	}
	else {
		team2[*curent]->setcolor(1,0,0);
		team2[i]->setcolor(0.5f, 0,0);
	}
	(*curent) = i;
	return ((*curent + 1)%7);
}

void Actions::goalKeeper(std::vector<Object2d*> team1, std::vector<Object2d*> team2, int team, int* curent) {
	if (team == 1){
		team1[*curent]->setcolor(0,0,1);
		*curent = 0;
		team1[*curent]->setcolor(0,0,0.3f);
	}
	else {
		team2[*curent]->setcolor(1,0,0);
		*curent = 0;
		team2[*curent]->setcolor(0.5f,0,0);
	}
}