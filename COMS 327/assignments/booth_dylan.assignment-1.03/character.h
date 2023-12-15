#ifndef CHARACTER_H
#define CHARACTER_H

#include <stdio.h>
#include <stdlib.h>

#include "pokemon.h"

struct character {
    char type[1]; 
    int trainerType;    //type 0 = pc, 1 = Hiker, 2 = Rival, 3 = Swimmer, 4 = Other
    int cost[80][21];
    int x, y;
};

void findCost(struct character *curr, struct terrain *terr);
void printCost(struct character *curr);
void initChar(struct terrain *terr);
void spawnPC(struct terrain *terr);
void spawnHiker(struct terrain *terr);
void spawnRival(struct terrain *terr);
void spawnSwimmer(struct terrain *terr);
void spawnOther(struct terrain *terr);

#endif