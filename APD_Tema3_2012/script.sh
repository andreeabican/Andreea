#! /bin/bash

FISIN=$1
FISOUT=$2

module load libraries/openmpi-1.6-gcc-4.4.6
time mpirun -np 8 ./paralel $FISIN $FISOUT

