#ifndef CHARACTER_H
#define CHARACTER_H

#include <stdio.h>
#include <stdlib.h>

#include "pokemon.h"

void create_cost_map(struct terrain *terr, cost_map_t *costs, pc_t *pc, int terr_costs[11], int type);
void init_cost_map(cost_map_t *map);
void print_cost_map(cost_map_t *costs);
void initChar(struct terrain *terr);
character_t *spawnPC(struct terrain *terr);
character_t *spawnHiker(struct terrain *terr);
character_t *spawnRival(struct terrain *terr);
// character_t *spawnSwimmer(struct terrain *terr);
character_t *spawnOther(struct terrain *terr);

#endif