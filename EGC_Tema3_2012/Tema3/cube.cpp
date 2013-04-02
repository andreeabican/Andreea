#pragma once
#include "cube.h"
#include "camera.h"

Cube::Cube() {
}

Cube::~Cube() {
}

//deseneaza un cub de latura 1 in centrul sistemului de coordonate
void Cube::drawCube(int x) {
	glPushMatrix();
	glBegin(GL_QUADS);
		//Brown color for  every side
		//glColor3f(coord, 0.2, 0.2);
		//face
		float coord = (double)x/2;
		glTexCoord2f(1,0);
		glVertex3f( -coord, -coord, -coord);		 // P1
		glTexCoord2f(1,1);
		glVertex3f( -coord,  coord, -coord);       // P2
		glTexCoord2f(0,1);
		glVertex3f(  coord,  coord, -coord);       // P3
		glTexCoord2f(0,0);
		glVertex3f(  coord, -coord, -coord);       // P4

		//BACK
		glTexCoord2f(1,0);
		glVertex3f(  coord, -coord, coord );
		glTexCoord2f(1,1);
		glVertex3f(  coord,  coord, coord );
		glTexCoord2f(0,1);
		glVertex3f( -coord,  coord, coord );
		glTexCoord2f(0,0);
		glVertex3f( -coord, -coord, coord );

 
		//RIGHT
		glTexCoord2f(1,0);
		glVertex3f( coord, -coord, -coord );
		glTexCoord2f(1,1);
		glVertex3f( coord,  coord, -coord );
		glTexCoord2f(0,1);
		glVertex3f( coord,  coord,  coord );
		glTexCoord2f(0,0);
		glVertex3f( coord, -coord,  coord );
 
		//LEFT
		glTexCoord2f(1,0);
		glVertex3f( -coord, -coord,  coord );
		glTexCoord2f(1,1);
		glVertex3f( -coord,  coord,  coord );
		glTexCoord2f(0,1);
		glVertex3f( -coord,  coord, -coord );
		glTexCoord2f(0,0);
		glVertex3f( -coord, -coord, -coord );
	
		//TOP
		glTexCoord2f(1,0);
		glVertex3f(  coord,  coord,  coord );
		glTexCoord2f(1,1);
		glVertex3f(  coord,  coord, -coord );
		glTexCoord2f(0,1);
		glVertex3f( -coord,  coord, -coord );
		glTexCoord2f(0,0);
		glVertex3f( -coord,  coord,  coord );

		//BOTTOM
		glVertex3f(  coord, -coord, -coord );
		glVertex3f(  coord, -coord,  coord );
		glVertex3f( -coord, -coord,  coord );
		glVertex3f( -coord, -coord, -coord );

		
	glEnd();

	//deseneaza liniile de pe cuburi
	glColor3f(0,0,0);

	glPopMatrix();
	glFlush();
}