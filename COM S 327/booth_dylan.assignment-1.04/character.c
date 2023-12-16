#include <limits.h>
#include <stdio.h>
#include <math.h>


#include "character.h"
#include "pokemon.h"
#include "heap.h"
#include "queue.h"

/* 
    Returns the value as either INT_MAX or a value thats not negative
*/
int no_neg(int x) {
    return (x < 0 ? INT_MAX : x);
}

const int dimy = 0;
const int dimx = 1;

char charTypes[8] = {'@', 'h', 'r', 'p', 'w', 's', 'e', 'o'};
const char terrTypes[8] = {':', '~', '%', '.', '^', '#', 'M', 'C'};

static int32_t path_cmp(const void *key, const void *with) {
    return ((path_t *)key)->cost - ((path_t *)with)->cost;
}

/*
    Returns the cost for the given terrain type to move to that space
*/
int getCost(int type, int terrain, int costs[11], int x, int y) {
    
    if ((x == 0 || y == 0 || x == 79 || y == 20) && terrain == 2) {          // border
        return costs[0];
    } else if ((x == 0 || y == 0 || x == 79 || y == 20) && terrain == 5) {   // gate
        return costs[10];
    } else if (terrain == 0) {                                          // tall grass
        return costs[5];
    } else if (terrain == 1) {                                          // water
        return costs[9];
    } else if (terrain == 2) {                                          // boulder
        return costs[7];
    } else if (terrain == 3) {                                          // short grass
        return costs[6];
    } else if (terrain == 4) {                                          // forest
        return costs[8];
    } else if (terrain == 5) {                                          // path
        return costs[2];
    } else if (terrain == 6) {                                          // pm
        return costs[3];
    } else if (terrain == 7) {                                          // pc
        return costs[4];
    }

    return INT_MAX;
}

/*
    Creates the cost map using dijkstra's algorithm using
    a fibonacci minheap
*/
void create_cost_map(struct terrain *terr, cost_map_t *costs, pc_t *pc, int terr_costs[11], int type) {

    int x, y;
    heap_t h;
    static path_t paths[MAX_HEIGHT][MAX_WIDTH], *p;
    static int initialized = 0;

    if (!initialized) {
        initialized = 1;
        for (y = 0; y < MAX_HEIGHT; y++) {
            for (x = 0; x < MAX_WIDTH; x++) {
                paths[y][x].cost = INT_MAX;
                paths[y][x].pos[1] = x;
                paths[y][x].pos[0] = y; 
            }
        }
    }

    paths[pc->y][pc->x].cost = 0;

    heap_init(&h, path_cmp, NULL);

    for (y = 1; y < MAX_HEIGHT - 1; y++) {
        for (x = 1; x < MAX_WIDTH - 1; x++) {
            if (getCost(type, terr->map[y][x], terr_costs, x, y)) {
                paths[y][x].hn = heap_insert(&h, &paths[y][x]);
            } else {
                paths[y][x].hn = NULL;
            }
        }
    }

    while ((p = heap_remove_min(&h))) {
        p->hn = NULL;

        int32_t newCost;

        // North
        if ((paths[p->pos[dimy]-1][p->pos[dimx]].hn) &&
            (paths[p->pos[dimy]-1][p->pos[dimx]].cost > 
                (newCost = no_neg(p->cost + getCost(type, terr->map[p->pos[dimy]-1][p->pos[dimx]], terr_costs, p->pos[dimx], p->pos[dimy]-1))))) {

            paths[p->pos[dimy]-1][p->pos[dimx]].cost = fmin(newCost, INT_MAX);

            heap_decrease_key_no_replace(&h, paths[p->pos[dimy]-1][p->pos[dimx]].hn);
        }

        // North West
        if ((paths[p->pos[dimy]-1][p->pos[dimx]-1].hn) &&
            (paths[p->pos[dimy]-1][p->pos[dimx]-1].cost > 
                (newCost = no_neg(p->cost + getCost(type, terr->map[p->pos[dimy]-1][p->pos[dimx]-1], terr_costs, p->pos[dimx]-1, p->pos[dimy]-1))))) {

            paths[p->pos[dimy]-1][p->pos[dimx]-1].cost = (min(newCost, INT_MAX));


            heap_decrease_key_no_replace(&h, paths[p->pos[dimy]-1][p->pos[dimx]-1].hn);
        }

        // North East
        if ((paths[p->pos[dimy]-1][p->pos[dimx]+1].hn) &&
            (paths[p->pos[dimy]-1][p->pos[dimx]+1].cost > 
                (newCost = no_neg(p->cost + getCost(type, terr->map[p->pos[dimy]-1][p->pos[dimx]+1], terr_costs, p->pos[dimx]+1, p->pos[dimy]-1))))) {

            paths[p->pos[dimy]-1][p->pos[dimx]+1].cost = (min(newCost, INT_MAX));

            heap_decrease_key_no_replace(&h, paths[p->pos[dimy]-1][p->pos[dimx]+1].hn);
        }

        // East
        if ((paths[p->pos[dimy]][p->pos[dimx]+1].hn) &&
            (paths[p->pos[dimy]][p->pos[dimx]+1].cost > 
                (newCost = no_neg(p->cost + getCost(type, terr->map[p->pos[dimy]][p->pos[dimx]+1], terr_costs, p->pos[dimx]+1, p->pos[dimy]))))) {

            paths[p->pos[dimy]][p->pos[dimx]+1].cost = (min(newCost, INT_MAX));

            heap_decrease_key_no_replace(&h, paths[p->pos[dimy]][p->pos[dimx]+1].hn);
        }

        // South East
        if ((paths[p->pos[dimy]+1][p->pos[dimx]+1].hn) &&
            (paths[p->pos[dimy]+1][p->pos[dimx]+1].cost > 
                (newCost = no_neg(p->cost + getCost(type, terr->map[p->pos[dimy]+1][p->pos[dimx]+1], terr_costs, p->pos[dimx]+1, p->pos[dimy]+1))))) {

            paths[p->pos[dimy]+1][p->pos[dimx]+1].cost = (min(newCost, INT_MAX));

            heap_decrease_key_no_replace(&h, paths[p->pos[dimy]+1][p->pos[dimx]+1].hn);
        }

        // South
        if ((paths[p->pos[dimy]+1][p->pos[dimx]].hn) &&
            (paths[p->pos[dimy]+1][p->pos[dimx]].cost > 
                (newCost = no_neg(p->cost + getCost(type, terr->map[p->pos[dimy]+1][p->pos[dimx]], terr_costs, p->pos[dimx], p->pos[dimy]+1))))) {

            paths[p->pos[dimy]+1][p->pos[dimx]].cost = (min(newCost, INT_MAX));

            heap_decrease_key_no_replace(&h, paths[p->pos[dimy]+1][p->pos[dimx]].hn);
        }

        // South West
        if ((paths[p->pos[dimy]+1][p->pos[dimx]-1].hn) &&
            (paths[p->pos[dimy]+1][p->pos[dimx]-1].cost > 
                (newCost = no_neg(p->cost + getCost(type, terr->map[p->pos[dimy]+1][p->pos[dimx]-1], terr_costs, p->pos[dimx]-1, p->pos[dimy]+1))))) {

            paths[p->pos[dimy]+1][p->pos[dimx]-1].cost = (min(newCost, INT_MAX));

            heap_decrease_key_no_replace(&h, paths[p->pos[dimy]+1][p->pos[dimx]-1].hn);
        }

        // West
        if ((paths[p->pos[dimy]][p->pos[dimx]-1].hn) &&
            (paths[p->pos[dimy]][p->pos[dimx]-1].cost > 
                (newCost = no_neg(p->cost + getCost(type, terr->map[p->pos[dimy]][p->pos[dimx]-1], terr_costs, p->pos[dimx]-1, p->pos[dimy]))))) {

            paths[p->pos[dimy]][p->pos[dimx]-1].cost = (min(newCost, INT_MAX));

            heap_decrease_key_no_replace(&h, paths[p->pos[dimy]][p->pos[dimx]-1].hn);
        }
                
    }

    for (y = 0; y < MAX_HEIGHT; y++) {
        for (x = 0; x < MAX_WIDTH; x++) {
            costs->map[y][x] = paths[y][x].cost;
        }
    }

    heap_delete(&h);

}

/*
    Initializes the cost map with INT_MAX values.
*/
void init_cost_map(cost_map_t *map) {
    int i, j;
    for (i = 0; i < MAX_HEIGHT; i++) {
        for (j = 0; j < MAX_WIDTH; j++) {
            map->map[i][j] = INT_MAX;
        }
    }
}

/*
    Initializes the 2d map of characters for the terrain.
*/
void initChar(struct terrain *terr) {
    // for (int i = 0; i < 80; i++) {
    //     for (int j = 0; j < 21; j++) {
    //         terr->trainers[j][i] = malloc(sizeof(struct character));
    //         terr->trainers[j][i]->trainerType = -1;
    //         terr->trainers[j][i]->type[0] = ' ';
    //         terr->trainers[j][i]->x = i;
    //         terr->trainers[j][i]->y = j;
    //         terr->trainers[j][i]->move_cost = INT_MAX;
    //     }
    // }

    for (int i = 0; i < MAX_HEIGHT; i++) {
        for (int j = 0; j < MAX_WIDTH; j++) {
            terr->trainers[i][j] = NULL;
        }
    }
}

/*
    Prints the provided cost map.
*/
void print_cost_map(cost_map_t *costs) {
    int x, y;
    for (y = 0; y < MAX_HEIGHT; y++) {
        for (x = 0; x < MAX_WIDTH; x++) {
            if (costs->map[y][x] != INT_MAX) { 
                printf("%2d ", costs->map[y][x] % 100);
            } else {
                printf("   ");
            }
        }
        printf("\n");
    }
    
}

/*
    Creates the player character. Randomly places on map
    only on allowed spots.
*/
character_t *spawnPC(struct terrain *terr) {

    int x, y;
    x = rand() % 70 + 3;
    y = rand() % 15 + 2;

    while (terr->map[y][x] != 5) {
        x = rand() % 70 + 3;
        y = rand() % 15 + 2;
    }

    terr->trainers[y][x] = malloc(sizeof(character_t));

    terr->trainers[y][x]->x = x;
    terr->trainers[y][x]->y = y;
    terr->trainers[y][x]->type[0] = charTypes[0];
    terr->trainers[y][x]->trainerType = 0;

    return terr->trainers[y][x];
}

/*
    Creates the hiker character. Randomly places on map
    only on allowed spots.
*/
character_t *spawnHiker(struct terrain *terr) {
    int x, y;
    x = rand() % 70 + 3;
    y = rand() % 15 + 2;

    while (terr->map[y][x] == 1 || terr->trainers[y][x] != NULL) {
        x = rand() % 70 + 3;
        y = rand() % 15 + 2;
    }

    terr->trainers[y][x] = malloc(sizeof(character_t));

    terr->trainers[y][x]->x = x;
    terr->trainers[y][x]->y = y;
    terr->trainers[y][x]->type[0] = charTypes[1];
    terr->trainers[y][x]->trainerType = 1;

    return terr->trainers[y][x];
}

/*
    Creates the rival character. Randomly places on map
    only on allowed spots.
*/
character_t *spawnRival(struct terrain *terr) {
    int x, y;
    x = rand() % 70 + 3;
    y = rand() % 15 + 2;

    while ((terr->map[y][x] == 2 || terr->map[y][x] == 4 || terr->map[y][x] == 1) || terr->trainers[y][x] != NULL) {
        x = rand() % 70 + 3;
        y = rand() % 15 + 2;
    }

    terr->trainers[y][x] = malloc(sizeof(character_t));

    terr->trainers[y][x]->x = x;
    terr->trainers[y][x]->y = y;
    terr->trainers[y][x]->type[0] = charTypes[2];
    terr->trainers[y][x]->trainerType = 2;

    return terr->trainers[y][x];
}

// /*
//     Creates the swimmer character. Randomly places on map
//     only on allowed spots.
// */
// character_t *spawnSwimmer(struct terrain *terr) {
//     int x, y;
//     x = rand() % 70 + 3;
//     y = rand() % 15 + 2;

//     while (terr->map[y][x] != 1 || terr->trainers[y][x] != NULL) {
//         x = rand() % 70 + 3;
//         y = rand() % 15 + 2;
//     }

//     terr->trainers[y][x] = malloc(sizeof(character_t));

//     terr->trainers[y][x]->x = x;
//     terr->trainers[y][x]->y = y;
//     terr->trainers[y][x]->type[0] = charTypes[3];
//     terr->trainers[y][x]->trainerType = 3;

//     return terr->trainers[y][x];
// }

/*
    Creates the other character. Randomly places on map
    only on allowed spots.
*/
character_t *spawnOther(struct terrain *terr) {
    
    int x, y, type, dir;
    x = rand() % 70 + 3;
    y = rand() % 15 + 2;
    type = rand() % 4 + 3;
    dir = rand() % 4;

    while ((terr->map[y][x] != 0 || terr->map[y][x] != 3) && terr->trainers[y][x] != NULL) {
        x = rand() % 70 + 3;
        y = rand() % 15 + 2;
    }

    terr->trainers[y][x] = malloc(sizeof(character_t));

    terr->trainers[y][x]->x = x;
    terr->trainers[y][x]->y = y;
    terr->trainers[y][x]->type[0] = charTypes[type];
    terr->trainers[y][x]->trainerType = type;
    terr->trainers[y][x]->direction = dir;

    return terr->trainers[y][x];
}