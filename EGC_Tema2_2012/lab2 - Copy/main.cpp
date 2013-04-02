//-----------------------------------------------------------------------------------------------
//					LAB 2
//
//	Fisiere de interes: Transform3d.cpp main.cpp
//
//	Functii WorldDrawer3d:
//	-init e apelat o singura data, la inceput.
//	-idle e apelat per frame
//	-onKey la apasarea unei taste.
//
//	Obiecte:
//	- un obiect este reprezentat prin punct si topologia punctelor (cum sunt legate pctele)
//	- obiectele sunt compuse din triunghiuri! de exemplu cu 4 puncte si 6 indici de topologie
//	pot crea 2 triunghiuri adiacente ce impreuna formeaza un dreptunghi.
//
//	Sisteme de coordonate:
//	- sunt 2 tipuri de sisteme de coordonate (fix - bleu&magenta) si dinamic(rosu&albastru)
//	- ca un obiect sa fie desenat trebuie sa fie atasat unui sistem de coordonate
//	- cand un obiect e atasat la un sistem de coordonate urmeaza transformarile sistemului.
//
//	Control obiecte:
//	- daca translatez/rotesc un obiect/punct direct (ex: o->translate(1,1)) o fac in coordonate globale
//	- daca translatez/rotesc un obiect printr-un sistem de coordonate o fac in coordonate locale
//	- pentru simplitate toate coordonatele mentinute in obiecte(de c++) sunt globale.
//
//	Happy coding.
//----------------------------------------------------------------------------------------------

#pragma once
#include "WorldDrawer3d.h"
#include "Cubulet.h"
bool WorldDrawer3d::animation=false;


//used global vars
CoordinateSystem3d *cs1, *cs2;
Object3d *o1, *o2, *o3, *o4,*o5, *o6;
int n = 3;
int nr = 27;
std::vector<Cubulet> c, cCopy;
int vrot = 5;
int spinx= 0, spiny = 0, spinz = 0;
bool animationx = false, animationy = false, animationz = false;
bool movex = false, movey = false, movez = false;
int rotx = 0, roty = 0, rotz = 0;
int size = 5;
//d specifica directia spre care mutam adica 1 sau -1: -1 -stanga/jos/spate, iar 1 - dreapta/sus/fata
int i, d, layer;
//daca ok este true atunci se poate face o miscare, iara daca nu, inseamna ca o alta miscare este in desfasurare si nu se mai
//poate incepe o noua actiune
bool ok = true, rezolvat = false;
//contorizeaza cate mutari s-au facut
int nMoves = 0;
int startMoves = 10;
int newWindow, window;
int argc1;
char **argv1;

/* layer: 0 - Stanga
		  1 - Dreapta
		  2 - jos
		  3 - sus
		  4 - spate
		  5 - fata*/
/* d = -1/1 - directia spre care se misca, stanga/spate/jos - dreapta/fata/sus*/
void cube(Object3d **o1) {
	std::vector<Point3d> points;
	std::vector<int> topology;
	points.push_back(Point3d(1,1,1));
	points.push_back(Point3d(1,1,-1));
	points.push_back(Point3d(-1,1,-1));
	points.push_back(Point3d(-1,1,1));
	points.push_back(Point3d(1,-1,1));
	points.push_back(Point3d(1,-1,-1));
	points.push_back(Point3d(-1,-1,-1));
	points.push_back(Point3d(-1,-1,1));
	topology.push_back(0);topology.push_back(1);topology.push_back(2);	//top
	topology.push_back(2);topology.push_back(3);topology.push_back(0);
	topology.push_back(6);topology.push_back(5);topology.push_back(4);	//bottom
	topology.push_back(7);topology.push_back(4);topology.push_back(6);
	topology.push_back(2);topology.push_back(3);topology.push_back(6);	//left
	topology.push_back(7);topology.push_back(3);topology.push_back(6);
	topology.push_back(0);topology.push_back(1);topology.push_back(5);	//right
	topology.push_back(0);topology.push_back(5);topology.push_back(4);
	topology.push_back(0);topology.push_back(3);topology.push_back(4);	//front
	topology.push_back(7);topology.push_back(3);topology.push_back(4);
	topology.push_back(5);topology.push_back(1);topology.push_back(2);	//back
	topology.push_back(6);topology.push_back(2);topology.push_back(5);
	(*o1) = new Object3d(points,topology);
}

void WorldDrawer3d::scor() {
	//glutDestroyWindow(window);
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();
	gluLookAt(0,0,20, 0, 0, 0, 0, 1, 0);
	cs2 = new CoordinateSystem3d();
	cs_used.push_back(cs2);
	glPushMatrix();
	Object3d *o1;
	cube(&o1);
	o1->setcolor(1,0,0);
	cs2->objectAdd(o1);
	/*int i;
	int x = -5, y = 5, z = 0;
	std::vector<Object3d*> o;
	for (i = 0; i < nMoves; i++){
		cube(&(o[i]));
		o[i]->scale(1/2, 1/2, 1/2);
	}
	//afisam cuburile
	while (nMoves > 0) {
		cs2->objectAdd(o[i]);
		o[i]->setcolor(1,0,0);
		o[i] ->translate(x,y,z);
		x++;
		if (x > 5) {
			y--;
		}
		if (y == 0)
			z++;
		nMoves--;
	}*/
}

void onKey1(unsigned char key, int x, int y) {
	//exit this window
	if (key == 'e') {
		WorldDrawer3d wd3d(argc1,argv1,600,600,200,100,std::string("Lab 2"), &window);
		wd3d.init();
		wd3d.run();
		glutSetWindow(window);
		//glutSetWindow(window);
		glutDestroyWindow(newWindow);
		
	}
}

void reshape(int w, int h) {
	glViewport(0,0, w, h);

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	float aspect = (float)w/(float)h;
	gluPerspective(90.0f, aspect, 0.1f, 3000.0f);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

	gluLookAt(20.0, 20.0, 20.0,0.0, 0.0, 0.0, 0.0, 1.0, 0.0);  //looking at xoy
}

void moveCubeX() {
	int i;
	//cand rotim cubul pe axa x
	for (i = 0; i < nr; i++) {
		//tot ce e sus si fata devine jos si fata
		if (c[i].layers[5] == 1 && c[i].layers[3] == 1) {
			c[i].layers[3] = 0;
			c[i].layers[2] = 1;
		}
		else
			//ce e sus si spate devine sus si fata
			if (c[i].layers[3] == 1 && c[i].layers[4] == 1) {
				c[i].layers[4] = 0;
				c[i].layers[5] = 1;
			}
			else
				//daca este jos si fata devine jos si spate
				if (c[i].layers[2] == 1 && c[i].layers[5] == 1) {
					c[i].layers[5] = 0;
					c[i].layers[4] = 1;
				}
				else
					//daca este jos si spate devine sus si spate
					if (c[i].layers[2] == 1 && c[i].layers[4] == 1) {
						c[i].layers[2] = 0;
						c[i].layers[3] = 1;
					}
					else
						if (c[i].layers[3] == 1) {
							c[i].layers[3] = 0;
							c[i].layers[5] = 1;
						}
						else
							if (c[i].layers[5] == 1) {
								c[i].layers[5] = 0;
								c[i].layers[2] = 1;
							}
							else
								if (c[i].layers[2] == 1) {
									c[i].layers[2] = 0;
									c[i].layers[4] = 1;
								}
								else
									if (c[i].layers[4] == 1) {
										c[i].layers[4] = 0;
										c[i].layers[3] = 1;
									}
						

	}
}

void moveCubeY() {
	int i;
	//cand rotim cubul pe axa y
	for (i = 0; i < nr; i++) {
		//tot ce e dreapta si fata devine stanga si fata
		if (c[i].layers[5] == 1 && c[i].layers[1] == 1) {
			c[i].layers[1] = 0;
			c[i].layers[0] = 1;
		}
		else
			//ce e stanga si fata devine stanga si spate
			if (c[i].layers[0] == 1 && c[i].layers[5] == 1) {
				c[i].layers[5] = 0;
				c[i].layers[4] = 1;
			}
			else
				//daca este stanga-spate, devine dreapta spate
				if (c[i].layers[0] == 1 && c[i].layers[4] == 1) {
					c[i].layers[0] = 0;
					c[i].layers[1] = 1;
				}
				else
					//daca este dreapta-spate devine dreapta fata
					if (c[i].layers[1] == 1 && c[i].layers[4] == 1) {
						c[i].layers[4] = 0;
						c[i].layers[5] = 1;
					}
					else
						if (c[i].layers[5] == 1) {
							c[i].layers[5] = 0;
							c[i].layers[0] = 1;
						}
						else
							if (c[i].layers[0] == 1) {
								c[i].layers[0] = 0;
								c[i].layers[4] = 1;
							}
							else
								if (c[i].layers[4] == 1) {
									c[i].layers[4] = 0;
									c[i].layers[1] = 1;
								}
								else
									if (c[i].layers[1] == 1) {
										c[i].layers[1] = 0;
										c[i].layers[5] = 1;
									}
						

	}
}

void moveCubeZ() {
	int i;
	//cand rotim cubul pe axa z
	for (i = 0; i < nr; i++) {
		//tot ce e sus-dreapta -> sus-stanga
		if (c[i].layers[3] == 1 && c[i].layers[1] == 1) {
			c[i].layers[1] = 0;
			c[i].layers[0] = 1;
		}
		else
			//ce e sus-stanga -> jos-stanga
			if (c[i].layers[3] == 1 && c[i].layers[0] == 1) {
				c[i].layers[3] = 0;
				c[i].layers[2] = 1;
			}
			else
				//daca este jos-stanga -> jos-dreapta
				if (c[i].layers[2] == 1 && c[i].layers[0] == 1) {
					c[i].layers[0] = 0;
					c[i].layers[1] = 1;
				}
				else
					//daca este jos-dreapta -> sus-dreapta
					if (c[i].layers[2] == 1 && c[i].layers[1] == 1) {
						c[i].layers[2] = 0;
						c[i].layers[3] = 1;
					}
					else
						if (c[i].layers[1] == 1) {
							c[i].layers[1] = 0;
							c[i].layers[3] = 1;
						}
						else
							if (c[i].layers[3] == 1) {
								c[i].layers[3] = 0;
								c[i].layers[0] = 1;
							}
							else
								if (c[i].layers[0] == 1) {
									c[i].layers[0] = 0;
									c[i].layers[2] = 1;
								}
								else
									if (c[i].layers[2] == 1) {
										c[i].layers[2] = 0;
										c[i].layers[1] = 1;
									}
						

	}
}

void move(int layer, int d) {
	int k;
	int aux1, aux2, aux3, aux4;
	//pentru fata de jos sau pentru fata de sus
	if (layer == 2 || layer == 3) {
		for (k = 0; k < nr; k++) {
			//daca apartine stratului "layer"
			if (c[k].layers[layer] == 1) {
				aux1 = c[k].layers[5];
				aux2 = c[k].layers[4];
				aux3 = c[k].layers[1];
				aux4 = c[k].layers[0];
				//schimbam layerurile
				//daca este pe colt
				if (d == 1) {
					if (aux1 == 1) {
						if (aux3 == 1) {
							c[k].layers[1] = 0;
							c[k].layers[0] = 1;
						}
						else
							if (aux4 == 1) {
								c[k].layers[5] = 0;
								c[k].layers[4] = 1;
							}
							else {
								c[k].layers[5] = 0;
								c[k].layers[0] = 1;
							}
					}
					else
						if (aux2 == 1) {
							if (aux3 == 1) {
								c[k].layers[4] = 0;
								c[k].layers[5] = 1;
							}
							else
								if (aux4 == 1) {
									c[k].layers[0] = 0;
									c[k].layers[1] = 1;
								}
								else {
									c[k].layers[4] = 0;
									c[k].layers[1] = 1;
								}
						}
						else {
							if (aux4 == 1) {
								c[k].layers[0] = 0;
								c[k].layers[4] = 1;
							}
							else
								if (aux3 == 1) {
									c[k].layers[1] = 0;
									c[k].layers[5] = 1;
								}
						}
				}
				else
					if (d == -1) {
					if (aux1 == 1) {
						if (aux3 == 1) {
							c[k].layers[5] = 0;
							c[k].layers[4] = 1;
						}
						else
							if (aux4 == 1) {
								c[k].layers[0] = 0;
								c[k].layers[1] = 1;
							}
							else {
								c[k].layers[5] = 0;
								c[k].layers[1] = 1;
							}
					}
					else
						if (aux2 == 1) {
							if (aux3 == 1) {
								c[k].layers[1] = 0;
								c[k].layers[0] = 1;
							}
							else
								if (aux4 == 1) {
									c[k].layers[4] = 0;
									c[k].layers[5] = 1;
								}
								else {
									c[k].layers[4] = 0;
									c[k].layers[0] = 1;
								}
						}
						else {
							if (aux4 == 1) {
								c[k].layers[0] = 0;
								c[k].layers[5] = 1;
							}
							else
								if (aux3 == 1) {
									c[k].layers[1] = 0;
									c[k].layers[4] = 1;
								}
						}
					}
			}
		}
	}

	//daca este layer stanga sau layer dreapta
	if (layer == 0 || layer == 1) {
		for (k = 0; k < nr; k++) {
			//daca apartine stratului "layer"
			if (c[k].layers[layer] == 1) {
				aux1 = c[k].layers[5]; //fata
				aux2 = c[k].layers[4]; //spate
				aux3 = c[k].layers[2]; //jos
				aux4 = c[k].layers[3]; //sus
				//schimbam layerurile
				//daca este pe colt
				if (d == 1) {
					if (aux1 == 1) {
						if (aux3 == 1) {
							c[k].layers[5] = 0;
							c[k].layers[4] = 1;
						}
						else
							if (aux4 == 1) {
								c[k].layers[3] = 0;
								c[k].layers[2] = 1;
							}
							else {
								c[k].layers[5] = 0;
								c[k].layers[2] = 1;
							}
					}
					else
						if (aux2 == 1) {
							if (aux3 == 1) {
								c[k].layers[2] = 0;
								c[k].layers[3] = 1;
							}
							else
								if (aux4 == 1) {
									c[k].layers[4] = 0;
									c[k].layers[5] = 1;
								}
								else {
									c[k].layers[4] = 0;
									c[k].layers[3] = 1;
								}
						}
						else {
							if (aux4 == 1) {
								c[k].layers[3] = 0;
								c[k].layers[5] = 1;
							}
							else
								if (aux3 == 1) {
									c[k].layers[2] = 0;
									c[k].layers[4] = 1;
								}
						}
				}
				else
					if (d == -1) {
					if (aux1 == 1) {
						if (aux3 == 1) {
							c[k].layers[2] = 0;
							c[k].layers[3] = 1;
						}
						else
							if (aux4 == 1) {
								c[k].layers[5] = 0;
								c[k].layers[4] = 1;
							}
							else {
								c[k].layers[5] = 0;
								c[k].layers[3] = 1;
							}
					}
					else
						if (aux2 == 1) {
							if (aux3 == 1) {
								c[k].layers[4] = 0;
								c[k].layers[5] = 1;
							}
							else
								if (aux4 == 1) {
									c[k].layers[3] = 0;
									c[k].layers[2] = 1;
								}
								else {
									c[k].layers[4] = 0;
									c[k].layers[2] = 1;
								}
						}
						else {
							if (aux4 == 1) {
								c[k].layers[3] = 0;
								c[k].layers[4] = 1;
							}
							else
								if (aux3 == 1) {
									c[k].layers[2] = 0;
									c[k].layers[5] = 1;
								}
						}
					}
			}
		}
	}
	else
		//daca este fata din fata sau fata din spate
		if (layer == 4 || layer == 5) {
		for (k = 0; k < nr; k++) {
			//daca apartine stratului "layer"
			if (c[k].layers[layer] == 1) {
				aux1 = c[k].layers[3]; //sus
				aux2 = c[k].layers[2]; //jos
				aux3 = c[k].layers[0]; //stanga
				aux4 = c[k].layers[1]; //dreapta
				//schimbam layerurile
				//daca este pe colt si intoarcem spre dreapta
				if (d == 1) {
					if (aux1 == 1) {
						if (aux3 == 1) {
							c[k].layers[3] = 0;
							c[k].layers[2] = 1;
						}
						else
							if (aux4 == 1) {
								c[k].layers[1] = 0;
								c[k].layers[0] = 1;
							}
							else {
								c[k].layers[3] = 0;
								c[k].layers[0] = 1;
							}
					}
					else
						if (aux2 == 1) {
							if (aux3 == 1) {
								c[k].layers[0] = 0;
								c[k].layers[1] = 1;
							}
							else
								if (aux4 == 1) {
									c[k].layers[2] = 0;
									c[k].layers[3] = 1;
								}
								else {
									c[k].layers[2] = 0;
									c[k].layers[1] = 1;
								}
						}
						else {
							if (aux4 == 1) {
								c[k].layers[1] = 0;
								c[k].layers[3] = 1;
							}
							else
								if (aux3 == 1) {
									c[k].layers[0] = 0;
									c[k].layers[2] = 1;
								}
						}
				}
				else
					if (d == -1) {
					if (aux1 == 1) {
						if (aux3 == 1) {
							c[k].layers[0] = 0;
							c[k].layers[1] = 1;
						}
						else
							if (aux4 == 1) {
								c[k].layers[3] = 0;
								c[k].layers[2] = 1;
							}
							else {
								c[k].layers[3] = 0;
								c[k].layers[1] = 1;
							}
					}
					else
						if (aux2 == 1) {
							if (aux3 == 1) {
								c[k].layers[2] = 0;
								c[k].layers[3] = 1;
							}
							else
								if (aux4 == 1) {
									c[k].layers[1] = 0;
									c[k].layers[0] = 1;
								}
								else {
									c[k].layers[2] = 0;
									c[k].layers[0] = 1;
								}
						}
						else {
							if (aux4 == 1) {
								c[k].layers[1] = 0;
								c[k].layers[2] = 1;
							}
							else
								if (aux3 == 1) {
									c[k].layers[0] = 0;
									c[k].layers[3] = 1;
								}
						}
					}
			}
		}
	}
}

void DeseneazaRubick() {
	int x, y, z;
	int k = 0;
	for (x = -1; x < n-1; x++) {
		for (y = -1; y < n-1; y++) {
			for (z = -1; z < n-1; z++) {
				c.push_back(Cubulet());
				cCopy.push_back(Cubulet());
				c[k].DrawCube(size, &cs1, x, y, z);
				if (x == -1) {
					c[k].layers[0] = 1;
					cCopy[k].layers[0] = 1;
				}
				else
					if (x == n-2) {
						c[k].layers[1] = 1;
						cCopy[k].layers[1] = 1;
					}
				if (y == -1) {
					c[k].layers[2] = 1;
					cCopy[k].layers[2] = 1;
				}
				else
					if (y == n-2) {
						c[k].layers[3] = 1;
						cCopy[k].layers[3] = 1;
					}
				if (z == -1) {
					c[k].layers[4] = 1;
					cCopy[k].layers[4] = 1;
				}
				else
					if (z == n-2) {
						c[k].layers[5] = 1;
						cCopy[k].layers[5] = 1;
					}
				k++;
			}
		}
	}
}

//add
void WorldDrawer3d::init(){
	//creeaza 2 sistem de coordonate client
	cs1 = new CoordinateSystem3d();
	cs_used.push_back(cs1);
	DeseneazaRubick();
}
void WorldDrawer3d::onIdle(){	//per frame
	if (rezolvat == true) {
		printf("A fost rezolvat, felicitari!!");
		rezolvat = false;
	}
	else {
		int i, j;
		if (startMoves <= 0) {
			bool okaici = false;
			for (i = 0; i < nr; i++) {
				if (okaici == false) {
					rezolvat = true;
					for (j = 0; j < 6; j++) {
						if (c[i].layers[j] != cCopy[i].layers[j]) {
							rezolvat = false;
							okaici = false;
							break;
						}
					}
				}
				else
					break;
			}
		}
	}
	if (startMoves == 0) {
		Object3d *o1;
		cube(&o1);
		o1->setcolor(1,0.8f,0.9f);
		o1->translate(17, 19, 0);
		cs1->objectAdd(o1);
		startMoves--;
	}

	if (animationx) {
			float angle = vrot*3.1415/180;
			for (i = 0; i < nr; i++) {
				Cubulet c1 = c[i];
				c1.rotateXrelative(angle, 0, 0, 0);
			}
			//c[20].rotateXrelative(angle, 0, 0, 0);
			rotx += vrot;
			if (rotx == 90) {
				animationx = false;
				rotx = 0;
				ok = true;
			}	
	}
	else
	if (animationy) {
			float angle = vrot*3.1415/180;
			for (i = 0; i < nr; i++) {
				Cubulet c1 = c[i];
				c1.rotateYrelative(angle, 0, 0, 0);
			}
			roty += vrot;
			if (roty == 90) {
				animationy = false;
				roty = 0;
				ok = true;
			}	
	}
	else
	if (animationz) {
		float angle = vrot*3.1415/180;
			for (i = 0; i < nr; i++) {
				Cubulet c1 = c[i];
				c1.rotateZrelative(angle, 0, 0, 0);
			}
			//c[20].rotateXrelative(angle, 0, 0, 0);
			rotz += vrot;
			if (rotz == 90) {
				animationz = false;
				rotz = 0;
				ok = true;
			}	
	}
	else
		if (movey) {
			float angle = vrot*3.14/180;
			for (i = 0; i < nr; i++) {
				if (c[i].layers[layer] == 1)
					c[i].rotateYrelative(d*angle,  0, 0, 0);
			}
			roty += vrot;
			if (roty == 90) {
				movey = false;
				roty = 0;
				ok = true;
			}
		}
		else
			if (movex) {
				float angle = vrot*3.14/180;
				for (i = 0; i < nr; i++) {
					if (c[i].layers[layer] == 1)
						c[i].rotateXrelative(d*angle,  0, 0, 0);
				}
				rotx += vrot;
				if (rotx == 90) {
					movex = false;
					rotx = 0;
					ok = true;
				}
		}
			else
				if (movez) {
					float angle = vrot*3.14/180;
					for (i = 0; i < nr; i++) {
						if (c[i].layers[layer] == 1)
							c[i].rotateZrelative(d*angle,  0, 0, 0);
					}
					rotz += vrot;
					if (rotz == 90) {
						movez = false;
						rotz = 0;
						ok = true;
					}
			}
	
}

void WorldDrawer3d::onKey(unsigned char key){
	if (ok == true)
		switch(key){
			case 'x':
				ok = false;
				//Rotire pe axa x a intregului cub
				animationx = true;
				moveCubeX();
				break;
			case 'y':
				ok = false;
				//rotire pe axa y a intregului cub
				animationy = true;
				moveCubeY();
				break;
			case 'z':
				ok = false;
				//rotire pe axa z a intregului cub
				animationz = true;
				moveCubeZ();
				break;
			case 'a':
				if (startMoves > 0) {
					startMoves--;
				}
				else {
					nMoves++;
				}
				ok = false;
				movey = true;
				d = 1;
				layer = 2;
				nMoves++;
				move(layer,d);
				//invarte fata de jos spre stanga
				break;
			case 's':
				if (startMoves > 0) {
					startMoves--;
				}
				else {
					nMoves++;
				}
				ok = false;
				movey = true;
				layer = 2;
				d = -1;
				nMoves++;
				move(layer,d);
				break;
			case 'q':
				if (startMoves > 0) {
					startMoves--;
				}
				else {
					nMoves++;
				}
				ok = false;
				movey =true;
				layer = 3;
				d = 1;
				nMoves++;
				move(layer,d);
				break;
			case 'w':
				if (startMoves > 0) {
					startMoves--;
				}
				else {
					nMoves++;
				}
				ok = false;
				movey = true;
				layer = 3;
				d = -1;
				nMoves++;
				move(layer, d);
				break;
			case 'e':
				if (startMoves > 0) {
					startMoves--;
				}
				else {
					nMoves++;
				}
				ok = false;
				movex = true;
				layer = 0;
				d = -1;
				nMoves++;
				move(layer, d);
				break;
			case 'd':
				if (startMoves > 0) {
					startMoves--;
				}
				else {
					nMoves++;
				}
				ok = false;
				movex = true;
				layer = 0;
				d = 1;
				nMoves++;
				move(layer, d);
				break;
			case 'r':
				if (startMoves > 0) {
					startMoves--;
				}
				else {
					nMoves++;
				}
				ok = false;
				movex = true;
				layer = 1;
				d = -1;
				nMoves++;
				move(layer, d);
				break;
			case 'f':
				if (startMoves > 0) {
					startMoves--;
				}
				else {
					nMoves++;
				}
				ok = false;
				movex = true;
				layer = 1;
				d = 1;
				nMoves++;
				move(layer, d);
				break;
			case 'c':
				if (startMoves > 0) {
					startMoves--;
				}
				else {
					nMoves++;
				}
				ok = false;
				movez = true;
				layer = 5;
				d = 1;
				nMoves++;
				move(layer, d);
				break;
			case 'v':
				if (startMoves > 0) {
					startMoves--;
				}
				else {
					nMoves++;
				}
				ok = false;
				movez = true;
				layer = 5;
				d = -1;
				nMoves++;
				move(layer, d);
				break;
			case 'b':
				if (startMoves > 0) {
					startMoves--;
				}
				else {
					nMoves++;
				}
				ok = false;
				movez = true;
				layer = 4;
				d = 1;
				nMoves++;
				move(layer, d);
				break;
			case 'n':
				if (startMoves > 0) {
					startMoves--;
				}
				else {
					nMoves++;
				}
				ok = false;
				movez = true;
				layer = 4;
				d = -1;
				nMoves++;
				move(layer, d);
				break;
			case 'p':
				glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGB|GLUT_DEPTH);
				glutInitWindowSize(640, 480);
				glutInitWindowPosition(100,100);
				newWindow = glutCreateWindow("Scor");
				glutSetWindow(newWindow);
				glutDisplayFunc(scor);
				glutReshapeFunc(reshape);
				glutKeyboardFunc(onKey1);
				glutMainLoop();
				break;
			default:
				break;
		}
}


int main(int argc, char** argv){
	argc1 = argc;
	argv1 = argv;
	WorldDrawer3d wd3d(argc,argv,600,600,200,100,std::string("Lab 2"), &window);
	wd3d.init();
	wd3d.run();
	return 0;
}