//include librarii de opengl, glut si glu
#pragma comment(lib, "opengl32.lib")
#pragma comment(lib, "glu32.lib")
#pragma comment(lib, "glut32.lib")

//includes
#include <stdio.h>
#include <iostream>
#include <stdlib.h>
#include <sstream>
#include <ctime>

//actions
#include "actions.h"
#include "Vector3D.h"
#include "collisions.h"

//cube
#include "cube.h"
#include "cub.h"

//glut and glew
#include "glut.h"

//ground
#include "ground.h"

//camera
#include "camera.h"

#include "Texture.h"

//cam
Collisions collisions;
Camera camera;
//o folosim pentru a ne ajuta sa determinam coordonatalele la care se gaseste jucatorul(aceasta o sa fie asezata putin mai in fata decat camera)
Camera firstCamera;
Actions actions;
Cube cube;
Cub cub;

//cuburi
float angle = 0, myAngle = 0.1, sumAngle = 0, myDist = 1, sndDist = 1, sndsum = 0, sum = 0, finalangle = 1, rotAngle = 0, unghi = 0;
//bool gor = true, gorb = false, gol = false, golb = false, gof = true, turn = false, gofb = false;
bool terminat = false;
int ok = 1, dimCub = 3;
std::vector<Cub*> pereti;
int k = 0, trage = 0, frames = 0;
int vectorPereti = 0;
//Vector3D position = Vector3D(camera.position.x - 1.5, camera.position.y - 1, camera.position.z - 3);
Vector3D diff = Vector3D(0, 0, 0);
//coordonatele initiale ale bilei
Vector3D coordBila = Vector3D(-1.5, -1, 0), coordTor;
Vector3D coordCamera;
//vrem sa mergem intotdeauna in fata sau in spate, nu pe diagonala(orientarea se va schimba prin rotiri)
float distOz = 0.3;
float distOx = 0;
float x, z, cosinusx, sinusx, cosinusy, sinusy, cosinusz, sinusz, nextx, nextz;
//folosim pentru rotirea camerei la TPV
float camAngleX = 0, camAngleY = 0, camAngleZ = 0;
float distCamX = 0, distCamY = 5, distCamZ = 10;
//vrem sa avem mereu cel putin 4 inamici in labirint, asa ca atunci cand unul moare, mai desenam unul random, iar la inceput desenam 4 inamici
int nrInamici = 0;
std::vector<Vector3D> inamici;
std::vector<int> viataInamici;
float razaErou = 0.2, razaInamic = 0.5, razaTori = 0.5, razaTore = 1, razaCon = 0.3, lungCon = 1;
//cu cat s-a deplasat glontul
float distZ = 0, distX = 0;

// componente material
	GLfloat diffuse4f [] = {0.5, 0.5, 0.5, 1.0};
	GLfloat ambient4fTeren [] = {1, 0.97, 0.862, 1.0};
	GLfloat specular4f [] = {1.0, 1.0, 1.0, 1.0};
	GLfloat shininess = 64.0;
	GLfloat ambient4fErou [] = {0, 0, 1, 1.0};
	GLfloat ambient4fCon [] = {1, 0, 0, 1.0};
	GLfloat ambient4fLabirint [] = {0.54, 0.53, 0.47, 1.0};
	GLfloat ambient4fTor [] = {0, 1, 0, 1.0};
	GLfloat ambient4fInamici [] = {0.3, 0.2, 0.5, 1.0};
	GLfloat ambient4fGlont [] = {0.0, 1.0, 0.0, 1.0};
	
	// componente lumina
	// position = x,y,z,w -> w = 0 lumina omnidirectionala pozitionata la infinit; w = 1 lumina directionala la care poate fi setat spotul
	GLfloat position4f [] = {1.0, 1.0, 0.5, 0.0};
	GLfloat color4f [] = {1.0, 1.0, 1.0, 1.0};

GLuint tex;
Texture wallsTexture;

bool LoadWallsTextures() {
	const GLfloat globalAmbientColor4f [] = {0.2, 0.2, 0.2, 1.0};
	glLightModelfv(GL_LIGHT_MODEL_AMBIENT, globalAmbientColor4f);
	
	glEnable(GL_DEPTH_TEST);	// pornire depth test

	glShadeModel(GL_SMOOTH);	// model iluminare
	glEnable(GL_LIGHTING);
	// setare parametrii lumina 0
	glLightfv(GL_LIGHT0, GL_AMBIENT, color4f);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, color4f);
	glLightfv(GL_LIGHT0, GL_POSITION, position4f);

	glEnable(GL_LIGHT0); // pornire lumina 0
	if (LoadTGA(&wallsTexture, "bricks.tga")) {
		glGenTextures(1, &wallsTexture.texID);
		glBindTexture(GL_TEXTURE_2D, wallsTexture.texID);
		glTexImage2D(GL_TEXTURE_2D, 0, wallsTexture.bpp / 8, wallsTexture.width, wallsTexture.height, 0, wallsTexture.type, GL_UNSIGNED_BYTE, wallsTexture.imageData);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glEnable(GL_TEXTURE_2D);

		if (wallsTexture.imageData) {
			free(wallsTexture.imageData);
		}
		return true;
	}
	else return false;
}

//functie afisare
void display(){
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	//setup view
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	//glTranslatef(0,0,-85);

	//ne uitam de sus in jos
	if (ok == 1) {
		//camera.render();
		gluLookAt(0, 65, 0, 0, 1, 0, 0, 0, -1);
		//glColor3f(1, 0.97, 0.862);
		//ground
		// aplicare material
		glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diffuse4f);
		glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fTeren);
		glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, specular4f);
		glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shininess);
		draw_ground(dimCub*14, dimCub*18, 2, 2, -(float)dimCub/2);
		//labirint
		if (vectorPereti == 0) {
			glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fLabirint);
			actions.desenLabirint("labirint1.txt", dimCub, 14, 18, &pereti, &vectorPereti);
			coordBila = actions.erou;
			coordTor = actions.tor;
			while (nrInamici < 7) {
				//std::cout << "intra aici\n";
				int randomNr = std::rand() % actions.spatii.size();
				while (randomNr == actions.indexErou) {
					randomNr = std::rand() % actions.spatii.size();
				}
				inamici.push_back(actions.spatii[randomNr]);
				viataInamici.push_back(0);
				nrInamici++;
			}
		}
		else {
			glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fLabirint);
			actions.desenLabirint("labirint1.txt", dimCub, 14, 18, &pereti, &vectorPereti);
		}
		//Deseneaza  bila cu care mergem prin labirint
		if (!terminat) {
			glPushMatrix(); 
				glTranslatef(coordBila.x, coordBila.y, coordBila.z);
				glRotatef(rotAngle*(180/3.145), 0, 1, 0);
				glColor3f(0,0,1);
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fErou);
				glutSolidSphere(razaErou, 20, 20);
				glTranslatef(0, 0, -0.3);
				glRotatef(180, 0, 1, 0);
				//glColor3f(1, 0, 0);
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fCon);
				glutSolidCone(razaCon, lungCon, 20 ,20);
			glPopMatrix();
		}
			//deseneaza torul
			glPushMatrix(); 
				glTranslatef(coordTor.x, coordTor.y, coordTor.z);
				glRotatef(angle*(180/3.145), 0, 1, 0);
				glColor3f(0,1,0);
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fTor);
				glutSolidTorus(razaTori, razaTore, 20, 20);
			glPopMatrix();
	}
	else
		if (ok == 2) {
			cosinusy = std::cos(/*-rotAngle */-camAngleY);
			sinusy = std::sin(/*-rotAngle*/ -camAngleY);
			cosinusx = std::cos(/*-rotAngle */-camAngleX);
			sinusx = std::sin(/*-rotAngle*/ -camAngleX);
			float pex, pey, pez;
			pex = coordBila.x + (distCamX*cosinusy - distCamZ*sinusy)/* + (distCamX*cosinusz - distCamY*sinusz)*/;
			pey = coordBila.y + (distCamY*cosinusx - distCamZ*sinusx) /*+ (distCamX*sinusz + distCamY*cosinusz)*/;
			pez =  coordBila.z + (distCamX*sinusy + distCamZ*cosinusy) /*+ (distCamY*sinusx + distCamZ*cosinusx)*/;
			camera.render(Vector3D(pex, pey, pez), Vector3D(coordBila.x, coordBila.y, coordBila.z));
			//camera.render(Vector3D(coordBila.x + (distCamX*cosinus - distCamZ*sinus), coordBila.y + distCamY, coordBila.z + (distCamX*sinus + distCamZ*cosinus)), Vector3D(coordBila.x, coordBila.y, coordBila.z));
			//glRotatef(-20, 1, 0, 0);
			//glColor3f(1, 0.97, 0.862);
			//ground
			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diffuse4f);
			glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fTeren);
			glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, specular4f);
			glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shininess);
			draw_ground(dimCub*14, dimCub*18, 2, 2, -(float)dimCub/2);
			//labirint
			glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fLabirint);
			actions.desenLabirint("labirint1.txt", dimCub, 14, 18, &pereti, &vectorPereti);
			//Deseneaza  bila cu care mergem prin labirint
			if (!terminat) {
			glPushMatrix();
				glTranslatef(coordBila.x, coordBila.y, coordBila.z);
				/*glTranslatef(x, auxCam.position.y, z);*/
				glRotatef(rotAngle*(180/3.145), 0, 1, 0);		
				//glColor3f(0,0,1);
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fErou);
				glutSolidSphere(razaErou, 20, 20);
				glTranslatef(0, 0, -0.3);
				glRotatef(180, 0, 1, 0);
				//glColor3f(1, 0, 0);
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fCon);
				glutSolidCone(razaCon, lungCon, 20 ,20);
			glPopMatrix();
			}
			glPushMatrix(); 
				glTranslatef(coordTor.x, coordTor.y, coordTor.z);
				glRotatef(angle*(180/3.145), 0, 1, 0);
				glColor3f(0,1,0);
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fTor);
				glutSolidTorus(razaTori, razaTore, 20, 20);
			glPopMatrix();
		}
		else
			if (ok == 3) {
				cosinusy = std::cos(-rotAngle);
				sinusy = std::sin(-rotAngle);
				firstCamera.render(Vector3D(coordBila.x, coordBila.y, coordBila.z), firstCamera.position + firstCamera.forward);
				glColor3f(1, 0.97, 0.862);
				//ground
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diffuse4f);
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fTeren);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, specular4f);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shininess);
				draw_ground(dimCub*14, dimCub*18, 2, 2, -(float)dimCub/2);
				//labirint
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fLabirint);
				actions.desenLabirint("labirint1.txt", dimCub, 14, 18, &pereti, &vectorPereti);
				glPushMatrix(); 
				glTranslatef(coordTor.x, coordTor.y, coordTor.z);
				glRotatef(angle*(180/3.145), 0, 1, 0);
				glColor3f(0,1,0);
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fTor);
				glutSolidTorus(razaTori, razaTore, 20, 20);
			glPopMatrix();
			}
			int i;
			std::srand((unsigned)time(0));
			glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fInamici);
			for (i = 0; i < nrInamici; i++) {
				float deplX = 0, deplZ = 0.04;
					int random = std::rand() % 4;
					if (random == 1) {
						deplZ = -0.04;
						deplX = 0;
					}
					else
						if (random == 2) {
							deplX = 0.04;
							deplZ = 0;
						}
						else
							if (random == 3) {
								deplX = -0.04;
								deplZ = 0;
							}
				glPushMatrix();
					cosinusy = std::cos(-rotAngle);
					sinusy = std::sin(-rotAngle);
					nextx = inamici[i].x - /*(deplX*cosinusy - deplZ*sinusy)*/deplX;
					nextz = inamici[i].z - /*(deplX*sinusy + deplZ*cosinusy)*/deplZ;
					if (nextx < dimCub*14/2 - razaInamic && nextx > -dimCub*14/2 + razaInamic && nextz < dimCub*18/2 - razaInamic && nextz > -dimCub*18/2 + razaInamic) {
						if (!collisions.Collision(pereti, razaInamic, Vector3D(nextx, inamici[i].y, nextz))) {
							inamici[i] = Vector3D(nextx, inamici[i].y, nextz);
						}
						else {
							nextx = inamici[i].x + /*(deplX*cosinusy - deplZ*sinusy)*/deplX;
							nextz = inamici[i].z + /*(deplX*sinusy + deplZ*cosinusy)*/deplZ;
							if (nextx < dimCub*14/2 - razaInamic && nextx > -dimCub*14/2 + razaInamic && nextz < dimCub*18/2 - razaInamic && nextz > -dimCub*18/2 + razaInamic) {
								if (!collisions.Collision(pereti, razaInamic, Vector3D(nextx, inamici[i].y, nextz))) {
									inamici[i] = Vector3D(nextx, inamici[i].y, nextz);
								}
								else {
									float aux = deplX;
									deplX = deplZ;
									deplZ = aux;
									nextx = inamici[i].x + /*(deplX*cosinusy - deplZ*sinusy)*/deplX;
									nextz = inamici[i].z + /*(deplX*sinusy + deplZ*cosinusy)*/deplZ;
									if (nextx < dimCub*14/2 - razaInamic && nextx > -dimCub*14/2 + razaInamic && nextz < dimCub*18/2 - razaInamic && nextz > -dimCub*18/2 + razaInamic) {
										if (!collisions.Collision(pereti, razaInamic, Vector3D(nextx, inamici[i].y, nextz))) {
											inamici[i] = Vector3D(nextx, inamici[i].y, nextz);
										}
										else {
											nextx = inamici[i].x - /*(deplX*cosinusy - deplZ*sinusy)*/deplX;
											nextz = inamici[i].z - /*(deplX*sinusy + deplZ*cosinusy)*/deplZ;
											if (nextx < dimCub*14/2 - razaInamic && nextx > -dimCub*14/2 + razaInamic && nextz < dimCub*18/2 - razaInamic && nextz > -dimCub*18/2 + razaInamic) {
												if (!collisions.Collision(pereti, razaInamic, Vector3D(nextx, inamici[i].y, nextz))) {
												inamici[i] = Vector3D(nextx, inamici[i].y, nextz);
												}
											}
										}
									}
								}
							}
						}
					}
					glTranslatef(inamici[i].x, inamici[i].y, inamici[i].z);
					glColor3f(1, 0, 0);
					glutSolidSphere(razaInamic, 20, 20);
				glPopMatrix();
			}

			//deseneaza glontul
			if (trage == 1) {
				glPushMatrix();
				std::vector<Vector3D>::iterator it;
				for (i = 0; i < nrInamici; i++) {
					if (collisions.SphereCollision(inamici[i], razaInamic, Vector3D(distX, 0, distZ), 0.2)) {
						trage = 0;
						viataInamici[i]++;
						if (viataInamici[i] >= 3) {
							inamici.erase(inamici.begin() + i);
							viataInamici.erase(viataInamici.begin() + i);
							i--;
							nrInamici--;
						}
					}
				}
					glTranslatef(distX, 0, distZ);
					glColor3f(1,1,0);
					glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4fGlont);
					glutSolidSphere(0.2, 10, 10);
				glPopMatrix();
			}

	//swap buffers
	glutSwapBuffers();
}

void reshape(int width, int height){
	//set viewport
	glViewport(0,0,width,height);

	//set proiectie
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(45, (float)width/(float)height, 0.2, 100);	
	//gluLookAt(0, 50,60,0,-1,0,0,0,-1);
}

void idle();

void keyboard(unsigned char ch, int x, int y){
	switch(ch){
		case 27:
			exit(0);
			break;
		case '1':
			if (ok != 1) {
				ok = 1;
			}
			break;
		case '2':
			if (ok != 2) {
				camAngleY = rotAngle;
				camAngleZ = 0;
				k = 0;
				ok = 2;
			}
			break;
		case '3':
			if (ok != 3)
				ok = 3;
			break;
		case 'w':
			if (!terminat) {
			cosinusy = std::cos(-rotAngle);
			sinusy = std::sin(-rotAngle);
			nextx = coordBila.x - (distOx*cosinusy - distOz*sinusy);
			nextz = coordBila.z - (distOx*sinusy + distOz*cosinusy);
			int i;
			for (i = 0; i < nrInamici; i++) {
				if (collisions.SphereCollision(inamici[i], 0.5, Vector3D(nextx, coordBila.y, nextz), 0.2)) {
					//jucatorul moare
					terminat = true;
				}
			}
			if (collisions.SphereCollision(coordTor, 1, Vector3D(nextx, coordBila.y, nextz), 0.2)) {
				terminat = true;
			}
			if (!collisions.Collision(pereti, 0.2, Vector3D(nextx, coordBila.y, nextz))) {
				/*coordBila.x = coordBila.x - (distOx*cosinus - distOz*sinus);
				coordBila.z = coordBila.z - (distOx*sinus + distOz*cosinus);*/
				coordBila.x = nextx;
				coordBila.z = nextz;
			}
			}
			break;
		case 's' :
			if (!terminat) {
			cosinusy = std::cos(-rotAngle);
			sinusy = std::sin(-rotAngle);
			nextx = coordBila.x + (distOx*cosinusy - distOz*sinusy);
			nextz = coordBila.z + (distOx*sinusy + distOz*cosinusy);
			int i;
			for (i = 0; i < nrInamici; i++) {
				if (collisions.SphereCollision(inamici[i], 0.5, Vector3D(nextx, coordBila.y, nextz), 0.2)) {
					//jucatorul moare
					terminat = true;
					break;
				}
			}
			if (collisions.SphereCollision(coordTor, 1, Vector3D(nextx, coordBila.y, nextz), razaErou)) {
				terminat = true;
			}
			if (!collisions.Collision(pereti, 0.2, Vector3D(nextx, coordBila.y, nextz))) {
			/*	coordBila.x = coordBila.x + (distOx*cosinus - distOz*sinus);
				coordBila.z = coordBila.z + (distOx*sinus + distOz*cosinus);*/
				coordBila.x = nextx;
				coordBila.z = nextz;
			}
			}
			break;
		case 'a' :
			if (!terminat) {
			rotAngle += myAngle;
			/*if (ok == 3) {
				camera.rotateFPS_OY(-myAngle);
			}*/
			firstCamera.rotateFPS_OY(-myAngle);
			if (rotAngle >= 2*3.14) {
				rotAngle = rotAngle - 2*3.14;
			}
			}
			break;
		case 'd' : 
			if (!terminat) {
			rotAngle -= myAngle;
			/*if (ok == 3) {
				camera.rotateFPS_OY(myAngle);
			}*/
			firstCamera.rotateFPS_OY(myAngle);
			if (rotAngle <= 0) {
				rotAngle = rotAngle + 2*3.14;
			}
			}
			break;
		case '4' : 
			if (!terminat) {
			camAngleY += myAngle;
			camera.rotateFPS_OY(camAngleY);
			}
			break;
		case '6':
			if (!terminat) {
			camAngleY -= myAngle;
			camera.rotateFPS_OY(-camAngleY);
			}
			break;
		case ' ':
			trage = 1;
			unghi = rotAngle;
			distZ = coordBila.z;
			distX = coordBila.x;
			break;
		default:
			break;
	}

}


//idle
void idle(){
	if (trage == 1) {
		float cosin = std::cos(-unghi);
		float sin = std::sin(-unghi);
		frames++;
		distX -= (0*cosin - 0.05*sin);
		distZ -= (0*sin + 0.05*cosin);
		if (frames == 500) {
			trage = 0;
			frames = 0;
		}
	}
	angle = angle + 0.01;
	if(angle > 360) 
		angle = angle - 360;
	glutPostRedisplay();
}



int main(int argc, char *argv[]){

	//init glut
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);

	//init window
	glutInitWindowSize(800,600);
	glutInitWindowPosition(200,200);
	glutCreateWindow("lab transformari si camera");

	//callbacks
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutKeyboardFunc(keyboard);
	glutIdleFunc(idle);


	//z test on
	glEnable(GL_DEPTH_TEST);

	//set background
	glClearColor(0.2,0.2,0.2,1.0);

	//init camera
	camera.init();
	firstCamera.init();

	//incarca texturi
	LoadWallsTextures();
	glShadeModel (GL_SMOOTH);
	std::vector<Cub*> v;

	//loop
	glutMainLoop();

	return 0;
}