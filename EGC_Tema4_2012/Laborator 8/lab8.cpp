// 
// Laborator 8 -- EGC
// 

#include <windows.h>
#include <stdarg.h>
#include <freeglut.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <assert.h>
#include <math.h>
#include <vector>
#include <cstdlib>
#include <iostream>
#include <ctime>

#include "Cube.h"
#include "Ground.h"
#include "Camera.h"
#include "Asteroid.h"
#include "collisions.h"
using namespace std;

// Windows include files 

#ifdef _WIN32
#include <windows.h>
#endif

#define M_PI       3.14159265358979323846


// identificatori pentru stiva de obiecte (necesari pentru selectie)
#define NONE			0
#define CUB1			1
#define CUB2			2
#define GROUND_NAME		3

// tip vedere
#define VEDERE_FATA		0
#define VEDERE_NAVA 	1
#define VEDERE_ASTEROID	2


// initial nici un obiect nu este selectat
int obiect = NONE;

// rotatia initiala a obiectelor
GLfloat spin=0.0;

GLfloat tr=0.0;

GLfloat dir=1;

int win_w = 1, win_h = 1;
GLfloat aspect = 1;

#define WIRE	0
#define SOLID	1

#define PROJECTIVE_SHADOW	0
#define VOLUME_SHADOW		1

// initial umbra este de tipul projective
int shadow_type = PROJECTIVE_SHADOW;
int shadow_enabled = 0;


// modul initial in care sunt desenate poligoanele
int mode = SOLID;

// vederea initiala
int vedere = VEDERE_FATA;

int mainWindow;

// solul
Ground *ground = NULL;

// vectorul suprafetelor 
Cube* objects[2];

// matricea folosita pentru calculul umbrelor proiectate
GLfloat groundshadow[4][4];
GLfloat plan[4];

GLfloat light_position0[] = { 7.0f, 5.0f, 0.0f, 1.0f };				//seteaza pozitia
GLfloat light_position1[] = { -7.0f, 5.0f, 0.0f, 1.0f };

GLfloat light_ambient0 [] = { 1.0, 0.0, 0.1, 1.0 };				//seteaza componenta ambientala
GLfloat light_difusion0[] = { 1.0, 0.0, 0.1, 1.0 };				//seteaza componenta  de difuzie    
GLfloat light_specular0[] = { 1.0, 0.0, 0.1, 1.0 };				//seteaza componenta speculara

GLfloat light_ambient1 [] = { 1.0, 0.0, 0.0, 1.0 };				//seteaza componenta ambientala
GLfloat light_difusion1[] = { 1.0, 0.0, 0.0, 1.0 };				//seteaza componenta  de difuzie    
GLfloat light_specular1[] = { 1.0, 0.0, 0.0, 1.0 };				//seteaza componenta speculara
// camera
Camera *camera_fata, *camera_nava, *camera_asteroid;

//spatiul in care ne incadram
float lungime = 25, latime = 20, inaltime = 20;

//pozitia navei si pozitia camerei dinamice
GLfloat navax = lungime/2, navay = 0, navaz = 0;
GLfloat camx = 0, camy = 0, camz = 0;

//viteza nava
float vitezaNava = 0.2f, transparenta = 0.3;

//vectorul de asteroizi
vector<Asteroid> vectorAsteroizi;
int			nrAsteroizi = 25;
int			i, okScut = 1, scut = 4, okLine = 0, okCameraAsteroid = 0, indexAsteroid;
GLfloat		dim, viteza, x = 0, y = 0, z = 0;
GLfloat xline, yline, zline;

Collisions c;

//pozitiile celor doua spoturi
//GLfloat spot_position0[] = { navax, navay, navaz - 0.01, 1.0 };
//GLfloat spot_position1[] = { navax, navay, navaz + 0.01, 1.0 };
//GLfloat spot_position0[] = { navax + 0.55, navay + 0.2, navaz + 0.7 };
//GLfloat spot_position1[] = { navax + 0.55, navay + 0.2, navaz + 0.7 }; 
GLfloat angle0 = 20, angle1 = 20;
GLfloat direction0    [] = { -1.0, 0.0, 0.0};					//seteaza directia spotului
GLfloat direction1    [] = { -1.0, 0.0, 0.0};
GLfloat color0		  [] = { 0.0, 1.0, 0.0, 1.0 };
GLfloat color1		  [] = { 0.0, 0.0, 1.0, 1.0 };
// nume variabile pentru ecuatiile matematice

#define X 0
#define Y 1
#define Z 2
#define W 3

#define A 0
#define B 1
#define C 2
#define D 3

//variabile pentru off
typedef struct Vertex {
  float x, y, z;
} Vertex;

typedef struct Face {
  Face(void) : nverts(0), verts(0) {};
  int nverts;
  Vertex **verts;
  float normal[3];
} Face;

typedef struct Mesh {
  Mesh(void) : nverts(0), verts(0), nfaces(0), faces(0) {};
  int nverts;
  Vertex *verts;
  int nfaces;
  Face *faces;
} Mesh;





////////////////////////////////////////////////////////////
// GLOBAL VARIABLES
////////////////////////////////////////////////////////////

// Program arguments
static char *filename = 0;
// GLUT variables 

static int GLUTwindow = 0;
static int GLUTwindow_height = 800;
static int GLUTwindow_width = 800;
static int GLUTmouse[2] = { 0, 0 };
static int GLUTbutton[3] = { 0, 0, 0 };
static int GLUTarrows[4] = { 0, 0, 0, 0 };
static int GLUTmodifiers = 0;



// Display variables

static int scaling = 0;
static int translating = 0;
static int rotating = 0;
static float scale = 1.0;
static float center[3] = { 0.0, 0.0, 0.0 };
static float rotation[3] = { 0.0, 0.0, 0.0 };
static float translation[3] = { 0.0, 0.0, -4.0 };



// Mesh variables

static Mesh *mesh = NULL;

////////////////////////////////////////////////////////////
// OFF FILE READING CODE
////////////////////////////////////////////////////////////

Mesh *
ReadOffFile(const char *filename)
{
  int i;

  // Open file
  FILE *fp;
  if (!(fp = fopen(filename, "r"))) {
    fprintf(stderr, "Unable to open file %s\n", filename);
    return 0;
  }

  // Allocate mesh structure
  Mesh *mesh = new Mesh();
  if (!mesh) {
    fprintf(stderr, "Unable to allocate memory for file %s\n", filename);
    fclose(fp);
    return 0;
  }

  // Read file
  int nverts = 0;
  int nfaces = 0;
  int nedges = 0;
  int line_count = 0;
  char buffer[1024];
  while (fgets(buffer, 1023, fp)) {
    // Increment line counter
    line_count++;

    // Skip white space
    char *bufferp = buffer;
    while (isspace(*bufferp)) bufferp++;

    // Skip blank lines and comments
    if (*bufferp == '#') continue;
    if (*bufferp == '\0') continue;

    // Check section
    if (nverts == 0) {
      // Read header 
      if (!strstr(bufferp, "OFF")) {
        // Read mesh counts
        if ((sscanf(bufferp, "%d%d%d", &nverts, &nfaces, &nedges) != 3) || (nverts == 0)) {
          fprintf(stderr, "Syntax error reading header on line %d in file %s\n", line_count, filename);
          fclose(fp);
          return NULL;
        }

        // Allocate memory for mesh
        mesh->verts = new Vertex [nverts];
        assert(mesh->verts);
        mesh->faces = new Face [nfaces];
        assert(mesh->faces);
      }
    }
    else if (mesh->nverts < nverts) {
      // Read vertex coordinates
      Vertex& vert = mesh->verts[mesh->nverts++];
      if (sscanf(bufferp, "%f%f%f", &(vert.x), &(vert.y), &(vert.z)) != 3) {
        fprintf(stderr, "Syntax error with vertex coordinates on line %d in file %s\n", line_count, filename);
        fclose(fp);
        return NULL;
      }
    }
    else if (mesh->nfaces < nfaces) {
      // Get next face
      Face& face = mesh->faces[mesh->nfaces++];

      // Read number of vertices in face 
      bufferp = strtok(bufferp, " \t");
      if (bufferp) face.nverts = atoi(bufferp);
      else {
        fprintf(stderr, "Syntax error with face on line %d in file %s\n", line_count, filename);
        fclose(fp);
        return NULL;
      }

      // Allocate memory for face vertices
      face.verts = new Vertex *[face.nverts];
      assert(face.verts);

      // Read vertex indices for face
      for (i = 0; i < face.nverts; i++) {
        bufferp = strtok(NULL, " \t");
        if (bufferp) face.verts[i] = &(mesh->verts[atoi(bufferp)]);
        else {
          fprintf(stderr, "Syntax error with face on line %d in file %s\n", line_count, filename);
          fclose(fp);
          return NULL;
        }
      }

      // Compute normal for face
      face.normal[0] = face.normal[1] = face.normal[2] = 0;
      Vertex *v1 = face.verts[face.nverts-1];
      for (i = 0; i < face.nverts; i++) {
        Vertex *v2 = face.verts[i];
        face.normal[0] += (v1->y - v2->y) * (v1->z + v2->z);
        face.normal[1] += (v1->z - v2->z) * (v1->x + v2->x);
        face.normal[2] += (v1->x - v2->x) * (v1->y + v2->y);
        v1 = v2;
      }

      // Normalize normal for face
      float squared_normal_length = 0.0;
      squared_normal_length += face.normal[0]*face.normal[0];
      squared_normal_length += face.normal[1]*face.normal[1];
      squared_normal_length += face.normal[2]*face.normal[2];
      float normal_length = sqrt(squared_normal_length);
      if (normal_length > 1.0E-6) {
        face.normal[0] /= normal_length;
        face.normal[1] /= normal_length;
        face.normal[2] /= normal_length;
      }
    }
    else {
      // Should never get here
      fprintf(stderr, "Found extra text starting at line %d in file %s\n", line_count, filename);
      break;
    }
  }

  // Check whether read all faces
  if (nfaces != mesh->nfaces) {
    fprintf(stderr, "Expected %d faces, but read only %d faces in file %s\n", nfaces, mesh->nfaces, filename);
  }

  // Close file
  fclose(fp);

  // Return mesh 
  return mesh;
}



void init(void)
{	
	//initializare camera fata
	camera_fata = new Camera();
	camera_fata->SetPosition(new Vector3D(0, 0, 20));
	//initializare camera nava
	camera_nava = new Camera();
	camera_nava->SetPosition(new Vector3D(navax/* + 0.55*/, navay + 0.2, navaz + 0.7));
	camera_nava->SetForwardVector(new Vector3D(-1,0,0));
	camera_nava->SetRightVector(new Vector3D(0,0,-1));
	camera_nava->SetUpVector(new Vector3D(0,1,0));
	//initializare camera asteroid
	camera_asteroid = new Camera();
	camera_asteroid->SetForwardVector(new Vector3D(1,0,0));
	camera_asteroid->SetRightVector(new Vector3D(0,0,1));
	camera_asteroid->SetUpVector(new Vector3D(0,1,0));

	glClearColor(0, 0, 0, 0.0);
	glEnable(GL_DEPTH_TEST);
	glShadeModel(GL_SMOOTH);			// mod de desenare SMOOTH
	glEnable(GL_LIGHTING);				// activam iluminarea
	glEnable(GL_NORMALIZE);				// activam normalizarea 

		
	glLightfv(GL_LIGHT0, GL_SPECULAR, light_specular0);          
	glLightfv(GL_LIGHT0, GL_AMBIENT , light_ambient0);               
	glLightfv(GL_LIGHT0, GL_POSITION, light_position0);

	glLightfv(GL_LIGHT1, GL_SPECULAR, light_specular1);          
	glLightfv(GL_LIGHT1, GL_AMBIENT , light_ambient1);               
	glLightfv(GL_LIGHT1, GL_POSITION, light_position1);
	
	/*glLightfv(GL_LIGHT2, GL_SPOT_CUTOFF, &angle0);
	glLightfv(GL_LIGHT2, GL_SPOT_DIRECTION, direction0);
	glLightfv(GL_LIGHT2, GL_POSITION, spot_position0);

	glLightfv(GL_LIGHT2, GL_AMBIENT, color0);
	glLightfv(GL_LIGHT2, GL_SPECULAR, color0);
	glLightfv(GL_LIGHT2, GL_DIFFUSE, color0);

	glLightfv(GL_LIGHT3, GL_SPOT_CUTOFF, &angle1);
	glLightfv(GL_LIGHT3, GL_SPOT_DIRECTION, direction1);
	glLightfv(GL_LIGHT3, GL_POSITION, spot_position1);

	glLightfv(GL_LIGHT3, GL_AMBIENT, color1);
	glLightfv(GL_LIGHT3, GL_SPECULAR, color1);
	glLightfv(GL_LIGHT3, GL_DIFFUSE, color1);*/
	// Read file
	 mesh = ReadOffFile("m1380.off");
	 if (!mesh) exit(1);

	  srand((unsigned)time(0)); 

	//deseneaza asteroizii
	for (i = 0; i < nrAsteroizi; i++) {
	glPushName(i+6);
	  static  GLfloat material_ambient1[] = { 0.14f, 0.46f, 0.3f, 1.0f };
	  static  GLfloat material_diffuse1[] = { 0.5f, 0.5f, 0.5f, 1.0f };
	  glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, material_ambient1);
	  glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, material_diffuse1);
	  glMaterialfv(GL_FRONT,GL_SPECULAR,material_ambient1);
	  glPushMatrix();
		x = rand()%((int)lungime);
		if (x < lungime/2)
			x = -x;	
		else
			x = x - lungime/2;
		z = rand()%((int)latime);
		if (z < latime/2)
			z = -z;
		else
			z = z - latime/2;
		y = rand()%((int)inaltime);
		if (y < latime/2)
			y = -y;
		else
			y = y - latime/2;
	//	cout << "pozitia este: (" << x << ", " << y << ", " << z << ") iar viteza: " << viteza << endl ; 
		Asteroid ast = Asteroid(0, y, z);
		viteza = (rand()%100+0.1)*0.002;
		dim = rand()%30/2*0.05;
		ast.viteza = viteza;
		ast.dim = dim;
		ast.draw = 1;
		vectorAsteroizi.push_back(ast);
		glScalef(dim, dim, dim);
		glutSolidDodecahedron();
	  glPopMatrix();
	  glPopName();
	}
}



// functia pentru desenarea unui text 3D
void output(GLfloat x, GLfloat y, GLfloat z,GLfloat rotation[3],char *format,...)
{
	va_list args;

	char buffer[1024],*p;

	va_start(args,format);

	vsprintf(buffer, format, args);

	va_end(args);

	glPushMatrix();
	
	glTranslatef(x,y,z);

	glRotatef(rotation[0],1,0,0);
	glRotatef(rotation[1],0,1,0);
	glRotatef(rotation[2],0,0,1);

	glScalef(0.0045, 0.0045, 0.0); /* 0.1 to 0.001 as required */

	for (p = buffer; *p; p++)
		glutStrokeCharacter(GLUT_STROKE_MONO_ROMAN, *p);

	glPopMatrix();
}


void output2(GLfloat x, GLfloat y, char *format,...)
{
	va_list args;

	char buffer[1024],*p;

	va_start(args,format);

	vsprintf(buffer, format, args);

	va_end(args);

	glPushMatrix();
	
	glTranslatef(x,y,0);

	glRotatef(180,1,0,0);

	glScalef(0.08, 0.08, 0.0); /* 0.1 to 0.001 as required */

	for (p = buffer; *p; p++)
		glutStrokeCharacter(GLUT_STROKE_MONO_ROMAN, *p);

	glPopMatrix();
}
void draw_my_hud(void)
{
	static int frame = 0, last_time = 0;
	static GLfloat fps = 0;
	int time;
	int startY = 13 , startX = 5;
	int pasY = 13;

	frame++;
	time = glutGet(GLUT_ELAPSED_TIME);
	if (time - last_time > 1000) {
		fps = frame * 1000.0 / (time - last_time);
		last_time = time;
		frame = 0;
	}

	/* begin text rendering mode */
	glPushAttrib(GL_ENABLE_BIT);

	glDisable(GL_LIGHTING);
	glDisable(GL_DEPTH_TEST);
	glDisable(GL_TEXTURE_2D);

	glMatrixMode(GL_PROJECTION);
	glPushMatrix();
	glLoadIdentity();
	gluOrtho2D(0, win_w, win_h, 0);

	glMatrixMode(GL_MODELVIEW);
	glPushMatrix();

	glLoadIdentity();
	glColor4f(1, 1, 1, 1);

	output2(win_w-26*4,startY, "FPS: %.1f",fps);

	output2(startX, startY, "Keys Usage  :");
	startY+=pasY;
	output2(startX, startY, "s/w         : Solid/Wireframe");
	startY+=pasY;

	if(shadow_enabled)
	{
		output2(startX, startY, "u           : Toggle shadows( current state ON )");
		startY+=pasY;
	}
	else
	{
		output2(startX, startY, "u           : Toggle shadows( current state OFF )");
		startY+=pasY;
	}

	output2(startX, startY, "q/a         : Creste/Scade diviziuni sol");
	startY+=pasY;
	
	output2(startX, startY, "1           : Privire \"Fata\"");
	startY+=pasY;
	output2(startX, startY, "2           : Privire \"Spate\"");
	startY+=pasY;
	output2(startX, startY, "Click stanga: Animatie/selectie obiecte");
	startY+=pasY;
	output2(startX, startY, "m           : Cycle shadow types");
	startY+=pasY;	

	// in functie de ce este selectat se afiseaza textul corespunzator
	switch (obiect)
	{
		case 2:
			output2(startX, startY, "Obiect      : Nava");
			startY+=pasY;
			break;
		case 4:
			output2(startX, startY, "Obiect      : Nava");
			startY+=pasY;
			break;
		case 5: 
			output2(startX, startY, "Obiect      : Dodecadron");
			startY+=pasY;
			break;
		case GROUND_NAME:
			output2(startX, startY, "Obiect      : Sol");
			startY+=pasY;
			break;
		case NONE:
			output2(startX, startY, "Obiect      : Nici unul");
			startY+=pasY;
			break;
		default:
			output2(startX, startY, "Obiect      : Asteroid(neprocesat in hit list)");
			startY+=pasY;
	}
	if (scut > -1) {
		//cout << "Sunt in viata, iar scutul este " << scut << endl;
		output2(startX,startY,"I am alive");
		startY+=pasY;
	}
	else {
		output2(startX,startY,"!!!!!!!! DEAD !!!!!!!!!");
		startY+=pasY;
		Sleep(4000);
		scut = 4;
		transparenta = 0.3;
	}

	// Restore previous states
	glPopAttrib();

	glMatrixMode(GL_PROJECTION);
	glPopMatrix();
	
	glMatrixMode(GL_MODELVIEW);
	glPopMatrix();

	glEnable(GL_LIGHTING);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_TEXTURE_2D);
	/* end text rendering mode */
}
// functia pentru desenarea scenei 3D
void drawScene()
{
	//glLightfv(GL_LIGHT0, GL_POSITION, light_position0);            
	//glLightfv(GL_LIGHT1, GL_POSITION, light_position1);
	// initilizeaza stiva de nume necesara pentru selectie
	glInitNames();
	if (vedere == VEDERE_FATA) {
		camera_fata->Render();
	}
	else {
		if (vedere == VEDERE_NAVA) {
			camera_nava->SetPosition(new Vector3D(navax/* + 0.55*/, navay + 0.2, navaz + 0.7));
			camera_nava->Render();
		}
		else {
			if (vedere == VEDERE_ASTEROID) {
				//Asteroid a = vectorAsteroizi[indexAsteroid];
				camera_asteroid->SetPosition(new Vector3D(vectorAsteroizi[indexAsteroid].posx + vectorAsteroizi[indexAsteroid].viteza + 1.2, vectorAsteroizi[indexAsteroid].posy, vectorAsteroizi[indexAsteroid].posz));
				camera_asteroid->Render();
			}
		}
	}

	
	glPushMatrix();
		glTranslatef(navax + 0.55, navay + 0.2, navaz + 0.7);
		glutSolidSphere(0.2f, 12, 12);
	glPopMatrix();
	
	//seteaza materialul pentru nava
	static GLfloat material[] = { 1.0, 0.5, 0.5, 1.0 };
	static  GLfloat material_ambient[] = { 0.75f, 0.16f, 0.16f, 1.0f };
	static  GLfloat material_diffuse[] = { 0.5f, 0.5f, 0.5f, 1.0f };
	
	// Draw faces
	glPushName(4);
	glPushMatrix();
	glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, material_ambient);
	glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, material_diffuse);
	glMaterialfv(GL_FRONT,GL_SPECULAR, material_ambient);
	//glColor3f(0, 0, 1);
    glTranslatef(navax, navay, navaz);
	glScalef(1.2, 1.2, 1.2);
	for (int i = 0; i < mesh->nfaces; i++) {
	Face& face = mesh->faces[i];
	glBegin(GL_POLYGON);
		glNormal3fv(face.normal);
		for (int j = 0; j < face.nverts; j++) {
			 Vertex *vert = face.verts[j];
			glVertex3f(vert->x, vert->y, vert->z);
		}
    glEnd();
	}
	glPopMatrix();
	glPopName();

	

	//Deseneaza scutul
	if (okScut == 1) {
		glPushMatrix();
			GLfloat material2[] = { 0.0, 1, 0.0, 0.3 };
			GLfloat material_ambient2[] = { 0.0f, 0.5f, 0.0f, transparenta };
			GLfloat material_diffuse2[] = { 0.0f, 1.0f, 0.0f, transparenta };
			glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, material_ambient2);
			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, material_diffuse2);
			glMaterialfv(GL_FRONT,GL_SPECULAR, material_ambient2);
			glEnable(GL_BLEND);
			glBlendFunc (GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
			glTranslatef(navax + 0.55, navay + 0.2, navaz + 0.7);
			glDisable(GL_DEPTH_TEST);
			glPushName(2);
			glutSolidSphere(1.2, 20, 20);
				glEnable(GL_DEPTH_TEST);
			glDisable(GL_BLEND);
			glPopName();
		glPopMatrix();
	}

	// Deseneaza asteroizii
	for (i = 0; i < nrAsteroizi; i++) {
		if (vectorAsteroizi[i].draw != 0) {
		if (!c.SphereCollision(Vector3D(vectorAsteroizi[i].posx, vectorAsteroizi[i].posy,
										vectorAsteroizi[i].posz), vectorAsteroizi[i].dim/2, 
										Vector3D(navax + 0.55, navay + 0.2, navaz + 0.7), 0.6)) {
			vectorAsteroizi[i].posx = vectorAsteroizi[i].posx + vectorAsteroizi[i].viteza;
			if (vectorAsteroizi[i].posx >= lungime/2) {
				/*x = rand()%((int)lungime);
				if (x < lungime/2)
					x = -x;	
				else
					x = x - lungime/2;*/
				z = rand()%((int)latime);
				if (z < latime/2)
					z = -z;
				else
					z = z - latime/2;
				y = rand()%((int)inaltime);
				if (y < latime/2)
					y = -y;
				else
					y = y - latime/2;
				vectorAsteroizi[i].posx = -lungime/2;
				vectorAsteroizi[i].posy = y;
				vectorAsteroizi[i].posz = z;
				viteza = (rand()%3+1)*0.07;
				dim = rand()%30/2*0.06;
				vectorAsteroizi[i].viteza = viteza;
				vectorAsteroizi[i].dim = dim;
			}
			Asteroid a = vectorAsteroizi[i];
			//cout << "deseneaza asteroid pe poz " << a.posx << " " << a.posy << " " << a.posz;
			glPushName(i+6);
			glPushMatrix();
				static  GLfloat material_ambient1[] = { 1.0f, 0.46f, 0.3f, 1.0f };
				static  GLfloat material_diffuse1[] = { 1.0f, 0.5f, 0.5f, 1.0f };
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, material_ambient1);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, material_diffuse1);
				glMaterialfv(GL_FRONT,GL_SPECULAR,material_ambient1);
				glTranslatef(a.posx , a.posy, a.posz);
				glScalef(a.dim, a.dim, a.dim);
				glutSolidDodecahedron();
			glPopMatrix();
			glPopName();
		}
		else {
			//daca este coliziune, atunci scade scutul si redesenam asteroidul de la inceput
			scut--;
			transparenta -= 0.1;
			cout << scut << endl;
			z = rand()%((int)latime);
			if (z < latime/2)
				z = -z;
			else
				z = z - latime/2;
			y = rand()%((int)inaltime);
			if (y < latime/2)
				y = -y;
			else
				y = y - latime/2;
			vectorAsteroizi[i].posx = -lungime/2;
			vectorAsteroizi[i].posy = y;
			vectorAsteroizi[i].posz = z;
			viteza = (rand()%3+1)*0.07;
			dim = rand()%30/2*0.06;
			vectorAsteroizi[i].viteza = viteza;
			vectorAsteroizi[i].dim = dim;
		}
		}
		else {
			if (okLine > 0) {
				//Deseneaza laserul catre asteroid
					glDisable(GL_LIGHTING);
					glLineWidth(2.5); 
					glColor3f(1.0, 0.0, 0.0);
					glBegin(GL_LINES);
					glVertex3f(navax + 0.55, navay + 0.2, navaz + 0.7);
					glVertex3f(vectorAsteroizi[i].posx + vectorAsteroizi[i].viteza, vectorAsteroizi[i].posy, vectorAsteroizi[i].posz);
					glEnd();
					glEnable(GL_LIGHTING);
					okLine--;
				
				vectorAsteroizi[i].posx = vectorAsteroizi[i].posx + vectorAsteroizi[i].viteza;
				Asteroid a = vectorAsteroizi[i];
			//cout << "deseneaza asteroid pe poz " << a.posx << " " << a.posy << " " << a.posz;
			glPushName(i+6);
			glPushMatrix();
				static  GLfloat material_ambient1[] = { 1.0f, 0.46f, 0.3f, 1.0f };
				static  GLfloat material_diffuse1[] = { 1.0f, 0.5f, 0.5f, 1.0f };
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, material_ambient1);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, material_diffuse1);
				glMaterialfv(GL_FRONT,GL_SPECULAR,material_ambient1);
				glTranslatef(a.posx , a.posy, a.posz);
				glScalef(a.dim, a.dim, a.dim);
				glutSolidDodecahedron();
			glPopMatrix();
			glPopName();
			}
			else {
				//daca am setat draw = 0 => ca nu mai trebuie desenat, asa ca ii regenaram coordonate de start, dimensiune si viteza
				z = rand()%((int)latime);
				if (z < latime/2)
					z = -z;
				else
					z = z - latime/2;
				y = rand()%((int)inaltime);
				if (y < latime/2)
					y = -y;
				else
					y = y - latime/2;
				vectorAsteroizi[i].posx = -lungime/2;
				vectorAsteroizi[i].posy = y;
				vectorAsteroizi[i].posz = z;
				viteza = (rand()%3+1)*0.07;
				dim = rand()%30/2*0.06;
				vectorAsteroizi[i].viteza = viteza;
				vectorAsteroizi[i].dim = dim;
				vectorAsteroizi[i].draw = 1;
			}
		}
	}

	//modifica pozitia spotului
	GLfloat spot_position0[] = { navax + 0.55, navay + 0.2, navaz + 0.7 };
	GLfloat spot_position1[] = { navax + 0.55, navay + 0.2, navaz + 0.7 };
	//cout << "Coord nava "<< navax << endl;
	glLightfv(GL_LIGHT2, GL_SPOT_CUTOFF, &angle0);
	glLightfv(GL_LIGHT2, GL_SPOT_DIRECTION, direction0);
	glLightfv(GL_LIGHT2, GL_POSITION, spot_position0);

	glLightfv(GL_LIGHT2, GL_AMBIENT, color0);
	glLightfv(GL_LIGHT2, GL_SPECULAR, color0);
	glLightfv(GL_LIGHT2, GL_DIFFUSE, color0);

	glLightfv(GL_LIGHT3, GL_SPOT_CUTOFF, &angle1);
	glLightfv(GL_LIGHT3, GL_SPOT_DIRECTION, direction1);
	glLightfv(GL_LIGHT3, GL_POSITION, spot_position1);

	glLightfv(GL_LIGHT3, GL_AMBIENT, color1);
	glLightfv(GL_LIGHT3, GL_SPECULAR, color1);
	glLightfv(GL_LIGHT3, GL_DIFFUSE, color1);
	
	glPushMatrix();
	  glScalef(lungime, latime, inaltime);
	  glutWireCube(1);
	glPopMatrix();
}
// functia de display
void display(void)
{
	glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	glAlphaFunc(GL_LESS, 1);
	glEnable(GL_LIGHT0);
	glEnable(GL_LIGHT1);
	glEnable(GL_LIGHT2);
	glEnable(GL_LIGHT3);
	drawScene();
	draw_my_hud();
	glutSwapBuffers();
}
// functia de animatie ( functia de idle a GLUT-ului -- similara cu un thread separat)
void spinAnimation()
{
	spin=spin+0.5;

	if(spin>360.0)
		spin= spin -360.0;
  	glutPostRedisplay();
}
void vedereDinFata()
{
	int w,h;
	w = glutGet(GLUT_WINDOW_WIDTH);
	h = glutGet(GLUT_WINDOW_HEIGHT);

	aspect = (GLfloat) w / (GLfloat) h;
	win_w = w;
	win_h = h;
	
	glViewport(0,0, (GLsizei) w, (GLsizei) h);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(90, (float)w/h, 0.1, 40.0); 
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

	// vedere din FATA
	/*gluLookAt(0.0, 0.0, -15.0,   //observatorul este in origine departat pe Z
		      0.0, 0.0, 1.0,     //si priveste in directia pozitiva a axei oz
			  0.0, 1.0, 0.0);  
		*/	  
	init();

}
void processhits (GLint hits, GLuint buffer[])
{
   int i;
   GLuint names, *ptr, minZ,*ptrNames, numberOfNames;

   ptr = (GLuint *) buffer;
   minZ = 0xffffffff;
   for (i = 0; i < hits; i++) {	
      names = *ptr;
	  ptr++;
	  if (*ptr < minZ) {
		  numberOfNames = names;
		  minZ = *ptr;
		  ptrNames = ptr+2;
	  }
	  
	  ptr += names+2;
  }

  ptr = ptrNames;
  
  obiect = *ptr;
 
     
}
void pick(int x, int y)
{
	okScut = 0;
	GLuint buffer[1024];
	GLint nhits;

	GLint	viewport[4];

	glGetIntegerv(GL_VIEWPORT, viewport);
	glSelectBuffer(1024, buffer);
	
	// se intra in modul de selectie
	glRenderMode(GL_SELECT);

	// se renderizeaza doar o zona mica din jurul cursorului mouseului
	glMatrixMode(GL_PROJECTION);
	glPushMatrix();
	glLoadIdentity();

	glGetIntegerv(GL_VIEWPORT,viewport);
	gluPickMatrix(x,viewport[3]-y,1.0f,1.0f,viewport);

	gluPerspective(90,(viewport[2]-viewport[0])/(GLfloat) (viewport[3]-viewport[1]),0.1,1000);
	glMatrixMode(GL_MODELVIEW);

	// se va desena scena in modul de selectie (de fapt nimic nu este desenat propriu-zis : "desenarea" se face
	// in bufferul de selectie si nu pe ecran
	drawScene();
	okScut = 1;
	glMatrixMode(GL_PROJECTION);						
	glPopMatrix();								
	glMatrixMode(GL_MODELVIEW);						
	nhits=glRenderMode(GL_RENDER);	
	
	// initial nici un obiect selectat
	obiect = NONE;

	// proceseaza bufferul de selectie si alege obiectul cel mai apropiat (daca exista)
	if(nhits != 0)
		processhits(nhits,buffer);
				
}

// functia pentru procesarea evenimentelor de la mouse
void mouse(int buton, int stare, int x, int y)
{
	switch(buton)
	{
	case GLUT_LEFT_BUTTON:
		if(stare == GLUT_DOWN)
		{
			// se efectueaza selectia unde a avut loc click-ul
			pick(x,y);
			cout << "nu mai deseneaza " << obiect-6 << endl;
			if (obiect >= 6) {
				if(okCameraAsteroid == 1) {
					camera_asteroid->SetPosition(new Vector3D(vectorAsteroizi[obiect-6].posx, vectorAsteroizi[obiect-6].posy, vectorAsteroizi[obiect-6].posz));
					vedere = VEDERE_ASTEROID;
					indexAsteroid = obiect - 6;
				}
				else {
					//cout << "nu mai deseneaza " << obiect-6 << endl;
					vectorAsteroizi[obiect-6].draw = 0;
					i = obiect - 6;
					xline = vectorAsteroizi[obiect-6].posx;
					yline = vectorAsteroizi[obiect-6].posy;
					zline = vectorAsteroizi[obiect-6].posz;
					okLine = 15;
				}
			}
			// se porneste animatia
			glutIdleFunc(spinAnimation);
		}
		break;
	case GLUT_RIGHT_BUTTON:
		if(stare== GLUT_DOWN)
			glutIdleFunc(NULL);
		break;
	}
}
// functia pentru procesarea evenimentelor de la tastatura
void keyboard(unsigned char key, int x, int y)
{
	switch(key)
	{
		case 27:
			exit(0);
			break;
		case '1':
			if (vedere != VEDERE_FATA) {
				camera_fata->Render();
				vedere = VEDERE_FATA;
				okCameraAsteroid = 0;
				glutPostRedisplay();
			}
			break;
		case '2':
			if (vedere != VEDERE_NAVA) {
				camera_nava->SetPosition(new Vector3D(navax/* + 0.55*/, navay + 0.2, navaz + 0.7));
				printf("Switch pe camera nava\n");
				camera_nava->Render();
				vedere = VEDERE_NAVA;
				okCameraAsteroid = 0;
				glutPostRedisplay();
			}
			break;
		case '3':
			/*if (okCameraAsteroid == 1) {
				camera_asteroid->Render();
				vedere = VEDERE_ASTEROID;
				glutPostRedisplay();
			}*/
			break;
		case '4':
			/*if (vedere == VEDERE_NAVA)
					camera_nava->MoveRight(vitezaNava);*/
			if (vedere == VEDERE_FATA) {
				navax -= vitezaNava;
			}
			else {
				if (vedere == VEDERE_NAVA || vedere == VEDERE_ASTEROID) {
					navaz += vitezaNava;
				}
			}
			glutPostRedisplay();
			break;
		case '6':
			if (vedere == VEDERE_FATA) {
				navax += vitezaNava;
			}
			else {
			/*	if (vedere == VEDERE_NAVA)
					camera_nava->MoveLeft(vitezaNava);*/
				if (vedere == VEDERE_NAVA || vedere == VEDERE_ASTEROID) {
					navaz -= vitezaNava;
				}
			}
			glutPostRedisplay();
			break;
		case '8':
			/*if (vedere == VEDERE_NAVA)
					camera_nava->MoveForward(vitezaNava);*/
			if (vedere == VEDERE_FATA) {
				navaz -= vitezaNava;
			}
			else {
				if (vedere == VEDERE_NAVA || vedere == VEDERE_ASTEROID) {
					navax -= vitezaNava;
				}
			}
			glutPostRedisplay();
			break;
		case '5':
		/*	if (vedere == VEDERE_NAVA)
					camera_nava->MoveBackward(vitezaNava);*/
			if (vedere == VEDERE_FATA) {
				navaz += vitezaNava;
			}
			else {
				if (vedere == VEDERE_NAVA || vedere == VEDERE_ASTEROID) {
					navax += vitezaNava;
				}
			}
			glutPostRedisplay();
			break;
		case 'w':
			if (vedere == VEDERE_FATA) {
				camera_fata->MoveForward(vitezaNava);
			}
			else {
				navay += vitezaNava;
			}
			glutPostRedisplay();
			break;
		case 's':
			if (vedere == VEDERE_FATA) {
				camera_fata->MoveBackward(vitezaNava);
			}
			else {
				navay -= vitezaNava;
			}
			glutPostRedisplay();
			break;
		case 'a':
			camera_fata->MoveRight(vitezaNava);
			glutPostRedisplay();
			break;
		case 'd':
			camera_fata->MoveLeft(vitezaNava);
			glutPostRedisplay();
			break;
		case 'q':
			okCameraAsteroid = 1;
			glutPostRedisplay();
			break;
		case 'M':
			if(shadow_type == VOLUME_SHADOW)
				shadow_type = PROJECTIVE_SHADOW;
			else
				shadow_type = VOLUME_SHADOW;
			glutPostRedisplay();
			break;
		case 'u':
			if(shadow_enabled)
				shadow_enabled = 0;
			else
				shadow_enabled = 1;
			glutPostRedisplay();
			break;
		
	
	}
	//glutPostRedisplay();
}


void reshape(int w, int h)
{
	//camera_fata->Render();	
	vedereDinFata();
	/*if(vedere==VEDERE_FATA)
		vedereDinFata();
	if(vedere==VEDERE_SUS)
		vedereDeSus();*/

}

int main(int argc, char** argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGBA | GLUT_DEPTH | GLUT_STENCIL | GLUT_DOUBLE);
	glutInitWindowSize(800,600);
	glutInitWindowPosition(100,100);
	
	mainWindow = glutCreateWindow("Asteroizi");
	
	init();
	glClearColor(0, 0, 0, 1.0);
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutMouseFunc(mouse);
	glutKeyboardFunc(keyboard);

	
	 glutMainLoop();

	return 0;
}