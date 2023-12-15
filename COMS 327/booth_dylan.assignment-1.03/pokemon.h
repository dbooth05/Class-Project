#ifndef POKEMON_H
#define POKEMON_H

#include <limits.h>

#define MAX_WIDTH 80
#define MAX_HEIGHT 21

/*
    Structure for each space on the map to hold terrain type, 
    and if there is a pokemon
*/
struct terrain {
    char map[MAX_HEIGHT][MAX_WIDTH];
    struct character *trainers[MAX_HEIGHT][MAX_WIDTH];
    int n, s, e, w;             //the gate positions
    int manDistance;
    int pcX, pcY;
};

#endif