#include "Cubulet.h"

Cubulet::Cubulet() {
	int i;
	for (i = 0; i < 6; i++)
		layers[i] = 0;
}

Cubulet::~Cubulet() {
}

void Cubulet::DrawCube(int size, CoordinateSystem3d **cs, int x, int y, int z) {
	float p = 0.9;
	//deseneaza fata din spate
	std::vector<Point3d> points1;
	std::vector<int> topology1;
	points1.push_back(Point3d(0.5, -0.5, -0.5));
	points1.push_back(Point3d(0.5, 0.5, -0.5));
	points1.push_back(Point3d(-0.5, 0.5, -0.5));
	points1.push_back(Point3d(-0.5, -0.5, -0.5));
	topology1.push_back(0); topology1.push_back(1); topology1.push_back(2);
	topology1.push_back(2); topology1.push_back(3); topology1.push_back(0);
	fata = new Object3d(points1, topology1);
	fata->setcolor(1,0,0);
	fata->scale(p*size, p*size, p*size);
	fata->translate(x*size, y*size, z*size - 0.3);
	//fata->translate(0,0,0);
	(*cs)->objectAdd(fata);

	//deseneaza fata din fata
	std::vector<Point3d> points2;
	std::vector<int> topology2;
	points2.push_back(Point3d(0.5, -0.5, 0.5));
	points2.push_back(Point3d(0.5, 0.5, 0.5));
	points2.push_back(Point3d(-0.5, 0.5, 0.5));
	points2.push_back(Point3d(-0.5, -0.5, 0.5));
	topology2.push_back(0); topology2.push_back(1); topology2.push_back(2);
	topology2.push_back(2); topology2.push_back(3); topology2.push_back(0);
	spate = new Object3d(points2, topology2);
	spate->setcolor(0,1,0);
	spate->scale(p*size, p*size, p*size);
	spate->translate(x*size, y*size, z*size + 0.3);
	//spate->translate(0,0,z*0.1);
	(*cs)->objectAdd(spate);

	//deseneaza fata din dreapta
	std::vector<Point3d> points3;
	std::vector<int> topology3;
	points3.push_back(Point3d(0.5, 0.5, 0.5));
	points3.push_back(Point3d(0.5, -0.5, 0.5));
	points3.push_back(Point3d(0.5, -0.5, -0.5));
	points3.push_back(Point3d(0.5, 0.5, -0.5));
	topology3.push_back(0); topology3.push_back(1); topology3.push_back(2);
	topology3.push_back(2); topology3.push_back(3); topology3.push_back(0);
	dreapta = new Object3d(points3, topology3);
	dreapta->setcolor(0,0,1);
	dreapta->scale(p*size, p*size, p*size);
	dreapta->translate(x*size + 0.3, y*size, z*size);
	dreapta->translate(0,0,0);
	(*cs)->objectAdd(dreapta);

	//deseneaza fata din stanga
	std::vector<Point3d> points4;
	std::vector<int> topology4;
	points4.push_back(Point3d(-0.5, 0.5, 0.5));
	points4.push_back(Point3d(-0.5, -0.5, 0.5));
	points4.push_back(Point3d(-0.5, -0.5, -0.5));
	points4.push_back(Point3d(-0.5, 0.5, -0.5));
	topology4.push_back(0); topology4.push_back(1); topology4.push_back(2);
	topology4.push_back(2); topology4.push_back(3); topology4.push_back(0);
	stanga = new Object3d(points4, topology4);
	stanga->setcolor(1,1,0);
	stanga->scale(p*size, p*size, p*size);
	stanga->translate(x*size - 0.3, y*size, z*size);
	//stanga->translate(1,1,0);
	(*cs)->objectAdd(stanga);

	//deseneaza fata de sus
	std::vector<Point3d> points5;
	std::vector<int> topology5;
	points5.push_back(Point3d(0.5, 0.5, 0.5));
	points5.push_back(Point3d(0.5, 0.5, -0.5));
	points5.push_back(Point3d(-0.5, 0.5, -0.5));
	points5.push_back(Point3d(-0.5, 0.5, 0.5));
	topology5.push_back(0); topology5.push_back(1); topology5.push_back(2);
	topology5.push_back(2); topology5.push_back(3); topology5.push_back(0);
	sus = new Object3d(points5, topology5);
	sus->setcolor(0,1,1);
	sus->scale(p*size, p*size, p*size);
	sus->translate(x*size, y*size + 0.3, z*size);
	//stanga->translate(1,1,0);
	(*cs)->objectAdd(sus);

	//deseneaza fata de jos
	std::vector<Point3d> points6;
	std::vector<int> topology6;
	points6.push_back(Point3d(0.5, -0.5, 0.5));
	points6.push_back(Point3d(0.5, -0.5, -0.5));
	points6.push_back(Point3d(-0.5, -0.5, -0.5));
	points6.push_back(Point3d(-0.5, -0.5, 0.5));
	topology6.push_back(0); topology6.push_back(1); topology6.push_back(2);
	topology6.push_back(2); topology6.push_back(3); topology6.push_back(0);
	jos = new Object3d(points6, topology6);
	jos->setcolor(1,0,1);
	jos->scale(p*size, p*size, p*size);
	jos->translate(x*size, y*size - 0.3, z*size);
	//stanga->translate(1,1,0);
	(*cs)->objectAdd(jos);

	//deseneaza un cub cu latura 1
	std::vector<Point3d> points;
	std::vector<int> topology;
	points.push_back(Point3d(0.5,0.5,0.5));
	points.push_back(Point3d(0.5,-0.5,0.5));
	points.push_back(Point3d(0.5,-0.5,-0.5));
	points.push_back(Point3d(0.5,0.5,-0.5));
	points.push_back(Point3d(-0.5,0.5,0.5));
	points.push_back(Point3d(-0.5,-0.5,0.5));
	points.push_back(Point3d(-0.5,-0.5,-0.5));
	points.push_back(Point3d(-0.5,0.5,-0.5));
	topology.push_back(0);topology.push_back(1);topology.push_back(2);	//right
	topology.push_back(2);topology.push_back(3);topology.push_back(0);
	topology.push_back(6);topology.push_back(5);topology.push_back(2);	//bottom
	topology.push_back(2);topology.push_back(1);topology.push_back(5);
	topology.push_back(6);topology.push_back(5);topology.push_back(7);	//left
	topology.push_back(7);topology.push_back(4);topology.push_back(5);
	topology.push_back(7);topology.push_back(3);topology.push_back(4);	//top
	topology.push_back(4);topology.push_back(0);topology.push_back(3);
	topology.push_back(7);topology.push_back(3);topology.push_back(2);	//front
	topology.push_back(2);topology.push_back(6);topology.push_back(7);
	topology.push_back(4);topology.push_back(0);topology.push_back(5);	//back
	topology.push_back(5);topology.push_back(1);topology.push_back(0);
	cub = new Object3d(points, topology);
	cub->setcolor(0,0,0);
	cub->scale(size, size, size);
	cub->translate(x*size, y*size, z*size);
	(*cs)->objectAdd(cub);
}

void Cubulet::rotateXrelative(float angles, float x, float y, float z) {
	cub->rotateXRelativeToPoint(Point3d(x,y,z), angles);
	fata->rotateXRelativeToPoint(Point3d(x,y,z), angles);
	spate->rotateXRelativeToPoint(Point3d(x,y,z), angles);
	dreapta->rotateXRelativeToPoint(Point3d(x,y,z), angles);
	stanga->rotateXRelativeToPoint(Point3d(x,y,z), angles);
	sus->rotateXRelativeToPoint(Point3d(x,y,z), angles);
	jos->rotateXRelativeToPoint(Point3d(x,y,z), angles);
}

void Cubulet::rotateYrelative(float angles, float x, float y, float z) {
	cub->rotateYRelativeToPoint(Point3d(x,y,z), angles);
	fata->rotateYRelativeToPoint(Point3d(x,y,z), angles);
	spate->rotateYRelativeToPoint(Point3d(x,y,z), angles);
	dreapta->rotateYRelativeToPoint(Point3d(x,y,z), angles);
	stanga->rotateYRelativeToPoint(Point3d(x,y,z), angles);
	sus->rotateYRelativeToPoint(Point3d(x,y,z), angles);
	jos->rotateYRelativeToPoint(Point3d(x,y,z), angles);
}

void Cubulet::rotateZrelative(float angles, float x, float y, float z) {
	cub->rotateZRelativeToPoint(Point3d(x,y,z), angles);
	fata->rotateZRelativeToPoint(Point3d(x,y,z), angles);
	spate->rotateZRelativeToPoint(Point3d(x,y,z), angles);
	dreapta->rotateZRelativeToPoint(Point3d(x,y,z), angles);
	stanga->rotateZRelativeToPoint(Point3d(x,y,z), angles);
	sus->rotateZRelativeToPoint(Point3d(x,y,z), angles);
	jos->rotateZRelativeToPoint(Point3d(x,y,z), angles);
}