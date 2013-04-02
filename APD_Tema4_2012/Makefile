all: Tema4.cpp
	mpic++ Tema4.cpp -o tema4

run: tema4
	mpirun -np 12 ./tema4 fisier_topologie fisier_mesaje
