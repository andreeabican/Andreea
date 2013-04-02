#ifndef ASTEROID_H
#define ASTEROID_H

#include <freeglut.h>		
#include "Vector3D.h"

/*
	Asteroid
	Clasa pentru initializarea si localizarea unui asteroid
	Aici pastram inclusiv viteza cu care se misca
*/

class Asteroid
{
	
// VARIABILE
//-------------------------------------------------
public:
	int draw;
	float posx, posy, posz;
	float viteza, dim;
	Vector3D material_color;

// FUNCTII
//-------------------------------------------------
public:
	// constructor fara parametri
	Asteroid(float x, float y, float z);				

	// plaseaza observatorul in scena
	void Render ( void );	

	void MoveForward(float viteza);
};

#endif