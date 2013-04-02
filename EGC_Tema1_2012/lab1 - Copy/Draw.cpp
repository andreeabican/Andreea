#include "Draw.h"

Draw::Draw() {
}

Draw::~Draw() {
}
//deseneaza un cerc cu raza 1
void Draw::circle(Object2d** o) {
	float angle = 0;
	int j = 1;
	std::vector<Point2d> points;
	std::vector<int> topology;
	points.push_back(Point2d(0,0));
	points.push_back(Point2d(cosf(0), sinf(0)));
	for (angle = 5; angle < 360; angle += 5) {
		points.push_back(Point2d(cosf(angle), sinf(angle)));
		topology.push_back(0);
		topology.push_back(j);
		j++;
		topology.push_back(j);
	}
	*o = new Object2d(points, topology);
}

//deseneaza un patrat cu latura 1
void Draw::rectangle(Object2d** o) {
	std::vector<Point2d> points;
	std::vector<int> topology;
	points.push_back(Point2d(0.5, -0.5));
	points.push_back(Point2d(-0.5, -0.5));
	points.push_back(Point2d(-0.5, 0.5));
	points.push_back(Point2d(0.5, 0.5));
	topology.push_back(0);
	topology.push_back(1);
	topology.push_back(2);
	topology.push_back(2);
	topology.push_back(3);
	topology.push_back(0);
	*o = new Object2d(points, topology);
}

//deseneaza jucatorii dintr-o echipa, echipa o primeste ca parametru, la fel ca si sistemul de coordonate
void Draw::players(std::vector<Object2d*> *team, CoordinateSystem2d **cs, float r, float g, float b, int type, float w, float h) {
	float pos[] = {0, 16, 8.5f, 13.5f, -6, 4, -0.2f, 1.2f, -6, -7, 8.5, -12};
	int i = 0, k = 0;
	Object2d *o;
	if (type == 1)
		k = 1;
	else
		k = -1;
	for (i = 0; i < 6; i++) {
		circle(&o);
		o->setcolor(r,g,b);
		o->translate(k*pos[i*2],k*pos[i*2+1]);
		(*cs)->objectAdd(o);
		team->push_back(o);
		}
}

void Draw::scoreDelimiter(CoordinateSystem2d **cs) {
	Object2d *o1, *o2;
	rectangle(&o1);
	o1->scale(0.25, 5);
	o1->rotateSelf(40);
	o1->translate(15, 0);
	(*cs)->objectAdd(o1);

	rectangle(&o2);
	o2->scale(0.25, 5);
	o2->rotateSelf(-40);
	o2->translate(15, 0);
	(*cs)->objectAdd(o2);
}

void Draw::score(int score, std::vector<Object2d*> *s, CoordinateSystem2d **cs, float x, float y) {
	Object2d *o;
	switch(score) {
		case 0:
			rectangle(&o); //stanga jos
			o->setcolor(1,1,1);
			o->scale(0.25f, 4);
			o->translate(x, y+2);
			s->push_back(o);
			(*cs)->objectAdd(o);

			rectangle(&o); //stanga sus
			o->setcolor(1,1,1);
			o->scale(0.25f, 4);
			o->translate(x, y+6);
			s->push_back(o);
			(*cs)->objectAdd(o);

			rectangle(&o); //sus
			o->setcolor(1,1,1);
			o->scale(4, 0.25f);
			o->translate(x + 2, y + 8);
			s->push_back(o);
			(*cs)->objectAdd(o);


			rectangle(&o); //dreapta jos
			o->setcolor(1,1,1);
			o->scale(0.25f, 4);
			o->translate(x+4, y+2);
			s->push_back(o);
			(*cs)->objectAdd(o);
			
			rectangle(&o); //dreapta sus
			o->setcolor(1,1,1);
			o->scale(0.25f, 4);
			o->translate(x+4, y+6);
			s->push_back(o);
			(*cs)->objectAdd(o);
			
			rectangle(&o); //jos
			o->setcolor(1,1,1);
			o->scale(4, 0.25f);
			//x + jumatate din lungimea partii de jos a zeroului, y + jumatate din lungimea partii laterale a zeroului
			o->translate(x + 2, y);
			s->push_back(o);
			(*cs)->objectAdd(o);

			rectangle(&o); //mijloc
			o->setcolor(1,1,1);
			o->scale(4, 0.25f);
			o->translate(x + 2, y + 4);
			s->push_back(o);
			(*cs)->objectRemove(o);
			break;
		case 1:
			(*cs)->objectRemove((*s)[2]);
			(*cs)->objectRemove((*s)[0]);
			(*cs)->objectRemove((*s)[1]);
			(*cs)->objectRemove((*s)[5]);
			break;
		case 2:
			(*cs)->objectRemove((*s)[3]);
			(*cs)->objectAdd((*s)[2]);
			(*cs)->objectAdd((*s)[0]);
			(*cs)->objectAdd((*s)[6]);
			(*cs)->objectAdd((*s)[5]);
			break;
		case 3:
			(*cs)->objectRemove((*s)[0]);
			(*cs)->objectAdd((*s)[3]);
			break;
		case 4:
			//4 inseamna reset
			(*cs)->objectRemove((*s)[6]);
			(*cs)->objectRemove((*s)[2]);
			(*cs)->objectRemove((*s)[4]);
			(*cs)->objectRemove((*s)[3]);
			(*cs)->objectRemove((*s)[5]);
			break;
	}
}

void Draw::end(int winner, CoordinateSystem2d **cs) {
	Object2d *o;
	rectangle(&o);
	o->scale(50,50);
	if (winner == 1) {
		o->setcolor(0,0,1);
	}
	else {
		o->setcolor(1,0,0);
	}
	(*cs)->objectAdd(o);
	Sleep(2000);
	(*cs)->objectRemove(o);
	
}
