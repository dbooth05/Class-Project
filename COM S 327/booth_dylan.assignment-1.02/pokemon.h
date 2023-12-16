#ifndef POKEMON_H
#define POKEMON_H

/*
    Structure for each space on the map to hold terrain type, 
    and if there is a pokemon
*/
struct terrain {
    char map[80][21];
    int n, s, e, w;             //the gate positions
    int manDistance;
};

#endif