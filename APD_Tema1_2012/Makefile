all: serial paralel optimserial
serial: serial.c
	gcc -o serial serial.c
	
paralel: paralel1.c
	gcc -fopenmp paralel1.c -o paralel

optimserial: optim1.c
	gcc -o optims optim1.c

clean:
	rm -rf paralel serial optims
