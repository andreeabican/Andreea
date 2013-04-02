#include "WorldDrawer2d.h"
#include "Draw.h"
#include "Collisions.h"
#include "Actions.h"
#include <time.h>
#include <stdio.h>
#include <conio.h>
#include <ctime>
#include <windows.h>
bool WorldDrawer2d::animation= true;


//used global vars
CoordinateSystem2d *cs1, *cs2,*cs3;
Object2d *o, *o1, *o2, *mstanga, *mdreapta, *msus1, *msus2, *mjos1, *mjos2, *ball, *p11, *p12, *p13, *p21, *p22, *p23, *mijloc, *teren;
Object2d *poarta1, *poarta2, *bara11, *bara12, *bara21, *bara22;
std::vector<Object2d*> team1, team2;
std::vector<Object2d*> bara1;
std::vector<Object2d*> bara2;
std::vector<Object2d*> border; //aici retinem obiectele folosite pentru a delimita marginea terenului; Mingea nu iese niciodata din teren...deocamdata
//Obiecte folosite pentru desenarea scorului:
std::vector<Object2d*> s1, s2; // in s1 se pastreaza obiectele care afiseaza scorul echipei 1
							  // in s2 se pastreaza obiectele care afiseaza scorul echipei 2
Draw draw;
Collisions collisions;
Actions actions;
float r1 = 1, r2 = 0.5f; //r1 - raza jucatorului, r2 - raza mingei
float speedx = 0.3f, speedy = 0.2f; // translatia pe x si pe y per frame a balonului
float width = 23, height = 35;
int theTeam; //numarul echipei care are acum mingea 1 sau 2; se initializeaza in init
int score1 = 0, score2 = 0; //scorul echipei 1, respectiv scorul echipei 2
bool ok = false, ok1 = false, ok2 = false; //true daca un jucator are mingea si false daca mingea nu este la niciun jucator
int curent1, curent2; // jucatorul curent de la echipa 1, respectiv jucatorul curent de la echipa 2
float tx = 0.1f, ty = 0.1f, r = 0.05f; //translatia pe x si pe y la apasarea tastelor de miscare, respectiva valoare unghiului de rotatie
int nr = 0; // il folosesc atunci cand afisez ecranul colorat de la sfarsitul jocului
Object2d *end; //acesta este obiectul pentru patratul colorat de la sfarsit


void WorldDrawer2d::init(){
	draw = Draw();
	collisions = Collisions();
	actions = Actions();	
	nr = 0;
	cs3 = new CoordinateSystem2d();
	cs_used.push_back(cs3);
	cs2 = new CoordinateSystem2d();
	cs_used.push_back(cs2);

	//teren
	draw.rectangle(&teren);
	teren->setcolor(0,0.5,0);
	teren->scale(width, height);
	
	//margine stanga
	draw.rectangle(&mstanga);
	mstanga->setcolor(1,1,1);
	mstanga->scale(0.25, 35.5);
	mstanga->translate(-11.625, 0);
	//margine dreapta
	draw.rectangle(&mdreapta);
	mdreapta->setcolor(1,1,1);
	mdreapta->scale(0.25, 35.5);
	mdreapta->translate(11.625,0);
	//margine sus
	draw.rectangle(&msus1);
	draw.rectangle(&msus2);
	msus2->setcolor(1,1,1);
	msus1->setcolor(1,1,1);
	msus1->scale(8.2f, 0.25f);
	msus2->scale(8.2f, 0.25f);
	msus1->translate(-7.4f, 17.625f);
	msus2->translate(7.4f, 17.625f);
	//poarta de sus
	draw.rectangle(&p11);
	p11->setcolor(1,1,1);
	p11->scale(6.6f, 0.25f);
	p11->translate(0, 19.5f);

	draw.rectangle(&p12);
	p12->setcolor(1,1,1);
	p12->scale(0.25f, 2);
	p12->translate(3.3f, 18.5f);

	draw.rectangle(&p13);
	p13->setcolor(1,1,1);
	p13->scale(0.25f, 2);
	p13->translate(-3.3f, 18.5f);

	//marginea de jos
	draw.rectangle(&mjos1);
	draw.rectangle(&mjos2);
	mjos1->setcolor(1,1,1);
	mjos2->setcolor(1,1,1);
	mjos1->scale(8.2f, 0.25f);
	mjos2->scale(8.2f, 0.25f);
	mjos1->translate(-7.4f, -17.625f);
	mjos2->translate(7.4f, -17.625f);

	//poarta de jos
	draw.rectangle(&p21);
	p21->setcolor(1,1,1);
	p21->scale(6.6f, 0.25f);
	p21->translate(0, -19.5f);

	draw.rectangle(&p22);
	p22->setcolor(1,1,1);
	p22->scale(0.25f, 2);
	p22->translate(3.3f, -18.5f);

	draw.rectangle(&p23);
	p23->setcolor(1,1,1);
	p23->scale(0.25f, 2);
	p23->translate(-3.3f, -18.5f);

	//marcaj mijloc
	draw.rectangle(&mijloc);
	mijloc->setcolor(1,1,1);
	mijloc->scale(23, 0.25f);

	//marcaje pentru porti; daca mingea trece de acest marcaj, atunci este gol
	draw.rectangle(&poarta1);
	poarta1->setcolor(1,1,1);
	poarta1->scale(6.6f, 0.25f);
	poarta1->translate(0, 17.625f);

	draw.rectangle(&poarta2);
	poarta2->setcolor(1,1,1);
	poarta2->scale(6.6f, 0.25f);
	poarta2->translate(0, -17.625f);

	//bara poarta
	draw.circle(&bara11);
	bara11->setcolor(1,1,1);
	bara11->scale(0.6f, 0.6f);
	bara11->translate(-3.3f, 17.5f);
	bara1.push_back(bara11);
	cs3->objectAdd(bara11);

	draw.circle(&bara12);
	bara12->setcolor(1,1,1);
	bara12->scale(0.6f, 0.6f);
	bara12->translate(3.3f, 17.5f);
	bara1.push_back(bara12);
	cs3->objectAdd(bara12);

	draw.circle(&bara21);
	bara21->setcolor(1,1,1);
	bara21->scale(0.6f, 0.6f);
	bara21->translate(-3.3f, -17.5f);
	bara2.push_back(bara21);
	cs3->objectAdd(bara21);

	draw.circle(&bara22);
	bara22->setcolor(1,1,1);
	bara22->scale(0.6f, 0.6f);
	bara22->translate(3.3f, -17.5f);
	bara2.push_back(bara22);
	cs3->objectAdd(bara22);
	//jucatorii
	draw.players(&team1, &cs3, 0, 0, 1, 1, width, height);
	draw.players(&team2, &cs3, 1, 0, 0, 2, width, height);
	
	//mingea
	srand(time(NULL));
	int teamNr = rand()%2; 
	theTeam = teamNr+1;
	std::cout << teamNr << " echipa\n";

	draw.circle(&ball);
	ball->setcolor(1,0.8f,0.85f);
	ball->scale(r2, r2);
	int team = teamNr+1;
	int p = actions.giveBall(&ball, team1, team2, teamNr+1, r1, r2);
	//o - o sa reprezinte jucatorul curent, adica cel care are mingea
	if (team == 1) {
		o = team1[p];
		ok1 = true;
		o1 = team1[p];
		o2 = team2[p];
	}
	else {
		o = team2[p];
		ok2 = true;
		o2 = team2[p];
		o1 = team1[p];
	}
	//initializam jucatorii curenti
	curent1 = p;
	curent2 = p;
	//la inceput mingea sigur este la cineva, deci trebuie sa nu se miste, chiar daca nu s-a detectat inca nicio coliziune
	ok = true;
	animation = !animation;

	//deseneaza scorul initial: 0 x 0
	//inaltimea unei cifre din scor este de 8, cifrele se gasesc la distanta 4 de axa ox si la distanta 13 de axa oy
	draw.score(0, &s1, &cs3, 13, 4);
	draw.scoreDelimiter(&cs3);
	draw.score(0, &s2, &cs3, 13, -12);

	//adauga obiectele la sistemul de coordonate
	cs3->objectAdd(poarta2);
	cs3->objectAdd(poarta1);
	cs3->objectAdd(mijloc);
	cs3->objectAdd(mstanga);
	cs3->objectAdd(ball);
	cs3->objectAdd(mdreapta);
	cs3->objectAdd(msus1);
	cs3->objectAdd(msus2);
	cs3->objectAdd(mjos1);
	cs3->objectAdd(mjos2);
	cs3->objectAdd(p11);
	cs3->objectAdd(p12);
	cs3->objectAdd(p13);
	cs3->objectAdd(p21);
	cs3->objectAdd(p22);
	cs3->objectAdd(p23);
	cs3->objectAdd(teren);
}
void WorldDrawer2d::onIdle(){	//per frame
	static int iteration=1;
	static bool o1dir=true;
	static bool o2dir=true;
	static bool o3dir=true;
	static bool o3dir2=true;
	if(animation){
		if (collisions.playerPlayer(team1, team2, 1,1)) {
			//sa seteze chestiile care sa spuna in ce directie nu paote sa se miste un jucator
		}
		//verifica daca mingea s-a lovit de o bara
		int barac = collisions.baraCollision(ball, bara11, bara12, bara21, bara22, 0.5f, 0.6f);
		if (barac == 1) {
			std::cout << "Bara\n";
			speedy = -speedy;
			ball->translate(2*speedx, 2*speedy);
		}
		else
			if (barac == 2) {
				std::cout << "Bara2\n";
				speedy = -speedy;
				ball->translate(2*speedx, 2*speedy);
			}
		//ball-player collision, in bp avem jucatorul de care s-a lovit mingea
		//verifica coliziunea cu jucatorii doar daca mingea nu este la niciun jucator. Daca mingea este deja la cineva, nu mai conteaza coliziunea
		//altui jucator cu mingea
		if (ok == false) {
			int bp = collisions.ballPlayerCollision(ball, team1, team2, 0.5f, 1, &theTeam);
			if (bp != -1) {
				animation = !animation;
				ok = true;
				//schimba jucatorul curent
				if (theTeam == 1) {
					o = team1[bp];
					o1->setcolor(0,0,1);
					o1 = team1[bp];
					curent1 = bp;
					team1[curent1]->setcolor(0,0,0.3f);
					ok1 = true;
					ok2 = false;
				}
				else {
					o = team2[bp];
					o2->setcolor(1,0,0);
					o2 = team2[bp];
					curent2 = bp;
					team2[curent2]->setcolor(0.5f,0,0);
					ok2 = true;
					ok1 = false;
				}
			}
		}
		int bc = collisions.borderCollision(ball, width, height, 1);
		if (bc == 1) {
			speedx = -speedx;
			ball->translate(speedx, speedy);
		}
		else
			if (bc == 2) {
				speedy = -speedy;
				ball->translate(speedx, speedy);
			}
			else
				if  (bc == 3) {
					std::cout << "Inceput de gol!!!\n";
					ball->translate(speedx, speedy);
					//mareste scorul echipei care a dat gol
					//da mingea unui jucator ales aleator din echipa care a primit gol
				}
				else
					if (bc == 4) {
						//echipa 1 a primit gol
						animation = !animation;
						std::cout << "goooool!!!\n";
						score2++;
						draw.score(score2, &s2, &cs3, 13, -12);
						o1->setcolor(0,0,1);
						int p = actions.giveBall(&ball, team1, team2, 1, r1, r2);
						o = team1[p];
						o1 = team1[p];
						ok = true;
						ok1 = true;
						ok2 = false;
					}
					else
						if (bc == 5) {
							//echipa 2 a primit gol
							animation = !animation;
							std::cout << "goooool!!!\n";
							score1++;
							draw.score(score1, &s1, &cs3, 13, 4);
							o2->setcolor(1,0,0);
							int p = actions.giveBall(&ball, team1, team2, 2, r1, r2);
							o = team2[p];
							o2 = team2[p];
							ok = true;
							ok2 = true;
							ok1 = false;
						}
		else
			ball->translate(speedx, speedy);
	}
	if (score1 == 3 || score2 == 3) {
		float r = 1, g = 1, b = 1;
		while (!cs3->objects.empty()) {
			cs3->objects.pop_back();
		}
		if (score1 == 3) {
			r = 0; g = 0; b = 1;
		}
		else {
			r = 1; g = 0; b = 0;
		}
		team1.clear();
		team2.clear();
		s1.clear();
		s2.clear();
		if (nr == 0) {
			draw.rectangle(&end);
			end->scale(50,50);
			end->setcolor(r,g,b);
			cs2->objectAdd(end);
		}
		nr++;
		if (nr == 70) {
			nr = 0;
			cs2->objectRemove(end);
			score1 = 0; 
			score2 = 0;
			init();
			animation = !animation;
		}
	}
}

void WorldDrawer2d::onKey(unsigned char key){
	switch(key){
		//start and stop the movement
		case ' ':
			if (ok1 && ok) {
				o->setcolor(0,0,1);
				ok = false;
				ok1 = false;
				speedx = (ball->points[0].x - o->points[0].x)/5;
				speedy = (ball->points[0].y - o->points[0].y)/5;
				ball->translate(3*speedx, 3*speedy);
				animation = !animation;
			}
			break;
		case 'e':
			if (ok1 && ok) {
				o->rotateRelativeToPoint(o->points[0], -r);
				ball->rotateRelativeToPoint(o->points[0], -r);
			}
			break;
		case 'q':
			if (ok1 && ok) {
				o->rotateRelativeToPoint(o->points[0], r);
				ball->rotateRelativeToPoint(o->points[0], r);
			}
			break;
		case 's':
			if (ok1 && ok) {
				ball->translate(0,-ty);
				o->translate(0,-ty);
			}
			else
				o1->translate(0,-ty);
			break;
		case 'a':
			if (ok1 && ok) {
				ball->translate(-tx,0);
				o->translate(-tx,0);
			}
			else
				o1->translate(-tx, 0);
			break;
		case 'd':
			if (ok1 && ok) {
				ball->translate(tx,0);
				o->translate(tx,0);
			}
			else
			o1->translate(tx,0);
			break;
		case 'w':
			if (ok1 && ok) {
				ball->translate(0,ty);
				o->translate(0,ty);
			}
			else
			o1->translate(0,ty);
			break;
		case 'c':
			//daca niciun jucator de la echipa 1 nu are mingea atunci se pot face schimbari pe jucatori
			if (ok1 == false) {
				actions.changePlayer(team1, team2,1,&curent1);
				o1 = team1[curent1];
			}
			break;
		case 'f':
			if(ok1 == false) {
				actions.goalKeeper(team1, team2, 1, &curent1);
				o1 = team1[curent1];
			}
			break;
		//Controls pentru a 2-a echipa
		case '0':
			if (ok2 && ok) {
				o->setcolor(1,0,0);
				ok = false;
				ok2 = false;
				speedx = (ball->points[0].x - o->points[0].x)/5;
				speedy = (ball->points[0].y - o->points[0].y)/5;
				ball->translate(3*speedx, 3*speedy);
				animation = !animation;
			}
			break;
		case '9':
			if (ok2 && ok) {
				o->rotateRelativeToPoint(o->points[0], -r);
				ball->rotateRelativeToPoint(o->points[0], -r);
			}
			break;
		case '7':
			if (ok2 && ok) {
				o->rotateRelativeToPoint(o->points[0], r);
				ball->rotateRelativeToPoint(o->points[0], r);
			}
			break;
		case '2':
			if (ok2 && ok) {
				ball->translate(0,-ty);
				o->translate(0,-ty);
			}
			else
			o2->translate(0,-ty);
			break;
		case '4':
			if (ok2 && ok) {
				ball->translate(-tx,0);
				o->translate(-tx,0);
			}
			else
				o2->translate(-tx,0);
			break;
		case '6':
			if (ok2 && ok) {
				ball->translate(tx,0);
				o->translate(tx,0);
			}
			else
				o2->translate(tx,0);
			break;
		case '8':
			if (ok2 && ok) {
				ball->translate(0,ty);
				o->translate(0,ty);
			}
			else
				o2->translate(0,ty);
			break;
		case '+':
			//daca niciun jucator de la echipa 1 nu are mingea atunci se pot face schimbari pe jucatori
			if (ok2 == false) {
				actions.changePlayer(team1, team2,2,&curent2);
				o2 = team2[curent2];
			}
			break;
		case '3':
			if (ok2 == false) {
				actions.goalKeeper(team1, team2, 2, &curent2);
				o2 = team2[curent2];
			}
			break;
		default:
			break;
	}
}


int main(int argc, char** argv){
	WorldDrawer2d wd2d(argc,argv,600,600,200,100,std::string("Lab 1"));
	wd2d.init();
	wd2d.run();
	return 0;
}