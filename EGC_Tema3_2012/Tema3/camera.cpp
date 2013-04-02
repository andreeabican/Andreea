#include "camera.h"

Camera::Camera(){
}
Camera::~Camera(){
}

void Camera::init(){
	position = Vector3D(-1.5, 0, 3);
	//position = Vector3D(0, 2, 3);
	//forward = Vector3D(0,0,-1);
	forward = Vector3D(0, 0, -1);
	up = Vector3D(0,1,0);
	//up = Vector3D(0,1,-1);
	right = Vector3D(1,0,0);
}

void Camera::translate_Forward(float dist){
	position = position + forward * dist;
}
void Camera::translate_Up(float dist){
	position = position + up * dist;
}
void Camera::translate_Right(float dist){
	position = position + right * dist;
}




void Camera::rotateFPS_OY(float angle){
	forward = forward * cos(angle) + right * sin(angle);
	right = forward.CrossProduct(up);
}

void Camera::rotateFPS_OX(float angle){
	right = right * cos(angle) + up * sin(angle);
	up = right.CrossProduct(forward);
}

void Camera::rotateFPS_OZ(float angle){
	up = up * cos(angle) + forward * sin(angle);
	forward = up.CrossProduct(right);
}

void Camera::rotateTPS_OY(float angle, float dist_to_interes){
	translate_Forward(dist_to_interes);
	rotateFPS_OY(angle);
	translate_Forward(-dist_to_interes);
}

void Camera::rotateTPS_OX(float angle, float dist_to_interes){	
	translate_Forward(dist_to_interes);
	rotateFPS_OX(angle);
	translate_Forward(-dist_to_interes);
}

void Camera::rotateTPS_OZ(float angle, float dist_to_interes){
	translate_Forward(dist_to_interes);
	rotateFPS_OZ(angle);
	translate_Forward(-dist_to_interes);
}

void Camera::render(Vector3D pos, Vector3D cen){
	position = pos;
	Vector3D center;
	center = cen;
	gluLookAt(	position.x, position.y, position.z, 
				center.x, center.y, center.z,
				up.x, up.y, up.z);
	//glRotatef(20, 1, 0, 0);
}