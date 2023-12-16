#ifndef POKEMON_H
#define POKEMON_H

#include <assert.h>
#include <limits.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "heap.h"

#define MAX_WIDTH 80
#define MAX_HEIGHT 21

#define min(x, y) (x < y ? x : y)
#define make_neg_safe(x) (x < 0 ? INT_MAX : x)

typedef struct character {
    char type[1]; 
    int trainerType;    //type 0 = pc, 1 = Hiker, 2 = Rival, 3 = Swimmer, 4 = Other
    int x, y;
    int move_cost;
    int direction; // 0=north, 1=east, 2=south, 3=west
} character_t;

/*
    Structure for each space on the map to hold terrain type, 
    and if there is a pokemon
*/
struct terrain {
    int map[MAX_HEIGHT][MAX_WIDTH]; // 0=tall grass, 1=water, 2=mtn, 3=short grass, 4=tree, 5=path, 6=PM, 7=PokeC
    character_t *trainers[MAX_HEIGHT][MAX_WIDTH];
    int n, s, e, w;             //the gate positions
    int manDistance;
};

typedef struct pc {
    int x, y;
} pc_t;

typedef struct cost_map {
    int map[MAX_HEIGHT][MAX_WIDTH];
} cost_map_t;

typedef struct path {
  heap_node_t *hn;
  uint8_t pos[2];
  uint8_t from[2];
  int32_t cost;
} path_t;

#endif