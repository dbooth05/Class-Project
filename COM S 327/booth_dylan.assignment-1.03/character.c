#include <limits.h>
#include <math.h>

#include "character.h"
#include "pokemon.h"
#include "heap.h"
#include "queue.h"

char charTypes[5] = {'@', 'h', 'r', 's', 'o'};
const char terrTypes[8] = {':', '~', '%', '.', '^', '#', 'M', 'C'};
const int terrainCosts[5][11] = {{INT_MAX, INT_MAX, 10, 10, 10, 20, 10, INT_MAX, INT_MAX, INT_MAX, 10},
                                 {INT_MAX, INT_MAX, 10, 50, 50, 15, 10, 15, 15, INT_MAX, INT_MAX},
                                 {INT_MAX, INT_MAX, 10, 50, 50, 20, 10, INT_MAX, INT_MAX, INT_MAX, INT_MAX},
                                 {INT_MAX, INT_MAX, INT_MAX, INT_MAX, INT_MAX, INT_MAX, INT_MAX, INT_MAX, INT_MAX, 7, INT_MAX},
                                 {INT_MAX, INT_MAX, 10, 50, 50, 20, 10, INT_MAX, INT_MAX, INT_MAX, INT_MAX}};


/*
    Returns the cost to move to space
*/
int getCost(struct terrain *terr, int type, int x, int y) {

    char curr = terr->map[x][y];
    int cost;

    if (x == 0 || x == 79 || y == 0 || y == 20) {   // border
        if (curr == terrTypes[2]) {          // border wall
            cost = terrainCosts[type][0];
        } else if (curr == terrTypes[5]) {   // gate
            cost = terrainCosts[type][10];
        }
    } else if (curr == terrTypes[0]) {               // tall grass
        cost = terrainCosts[type][5];
    } else if (curr == terrTypes[1]) {               // water
        cost = terrainCosts[type][9];
    } else if (curr == terrTypes[2]) {               // mountain
        cost = terrainCosts[type][7];
    } else if (curr == terrTypes[3]) {               // short grass
        cost = terrainCosts[type][6];
    } else if (curr == terrTypes[4]) {               // forest
        cost = terrainCosts[type][8];
    } else if (curr == terrTypes[5]) {               // path
        cost = terrainCosts[type][2];
    } else if (curr == terrTypes[6]) {               // pmart
        cost = terrainCosts[type][3];
    } else if (curr == terrTypes[7]) {               // pcntr
        cost = terrainCosts[type][4];
    }

    return cost;
}

/*
    Finds the cost map of the provided character
*/
void dijkstra(struct terrain *terr, struct character *c, int srcX, int srcY, int endX, int endY) {

    // for (int i = 0; i < 80; i++) {
    //     for (int j = 0; j < 21; j++) {
    //         c->cost[i][j] = INT_MAX;
    //     }
    // }

    // struct pqueue *min;
    // pqueue_init(min);

    // while (!is_empty(min)) {
    //     pqueue_node *n = front(min);

    // }


    minHeap *heap = initHeap(1680);
    int visited[80][21];
    int cost[80][21];

    for (int i = 0; i < 80; i++) {
        for (int j = 0; j < 21; j++) {
            cost[i][j] = INT_MAX;
            visited[i][j] = 0;
        }
    }

    cost[srcX][srcY] = 0;
    tile *start = createTile(srcX, srcY, 0);
    insert(heap, start);

    while (!heapEmpty(heap)) {
        tile *t = extractMin(heap);
        int x = t->x;
        int y = t->y;

        if (visited[x][y] == 1) {
            continue;
        }

        visited[x][y] = 1;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newX = x + i;
                int newY = y + j;

                if (newX >= 0 && newX < 80 && newY >= 0 && newY < 21 && visited[newX][newY] == 0) {
                    int newCost;

                    if (getCost(terr, c->trainerType, newX, newY) == INT_MAX || cost[x][y] + getCost(terr, c->trainerType, newX, newY) < 0) {
                        newCost = INT_MAX;
                    } else {
                        newCost = cost[x][y] + getCost(terr, c->trainerType, newX, newY); 
                    }

                    if (newCost < cost[newX][newY]) {
                        cost[newX][newY] = newCost;
                        insert(heap, createTile(newX, newY, newCost));
                    }
                }
            }
        }
    }

   for (int i = 0; i < 80; i++) {
    for (int j = 0; j < 21; j++) {
        c->cost[i][j] = cost[i][j];
    }
   }

   destroyHeap(heap);
}

/*
    Prints the cost map of the provided character
*/
void printCost(struct character *curr) {

    for (int i = 0; i < 21; i++) {
        for (int j = 0; j < 80; j++) {
            // if (curr->cost[i][j] == INT_MAX) {
                // printf("   ");
            // } else {
                printf("%02d ", (curr->cost[i][j] % 100));
            // }
        }
        printf("\n");
    }
    printf("\n");

}

void initChar(struct terrain*terr) {
    for (int i = 0; i < 80; i++) {
        for (int j = 0; j < 21; j++) {
            terr->trainers[i][j] = malloc(sizeof(struct character));
            terr->trainers[i][j]->trainerType = -1;
        }
    }
}

/*
    Creates the player character. Randomly places on map
    only on allowed spots. Based on cost if possible to move here.
*/
void spawnPC(struct terrain *terr) {

    int x, y;
    x = rand() % 70 + 3;
    y = rand() % 15 + 2;

    while (terr->map[x][y] != '#') {
        x = rand() % 70 + 3;
        y = rand() % 15 + 2;
    }

    terr->trainers[x][y]->x = x;
    terr->trainers[x][y]->y = y;
    terr->trainers[x][y]->type[0] = charTypes[0];
    terr->trainers[x][y]->trainerType = 0;
    
    terr->pcX = x;
    terr->pcY = y;
}

/*
    Creates the player character. Randomly places on map
    only on allowed spots. Based on cost if possible to move here.
    Then calls findCost to fill the cost map for movement.
*/
void spawnHiker(struct terrain *terr) {
    int x, y;
    x = rand() % 70 + 3;
    y = rand() % 15 + 2;

    while (terr->map[x][y] == '#') {
        x = rand() % 70 + 3;
        y = rand() % 15 + 2;
    }

    terr->trainers[x][y]->x = x;
    terr->trainers[x][y]->y = y;
    terr->trainers[x][y]->type[0] = charTypes[1];
    terr->trainers[x][y]->trainerType = 1;

    dijkstra(terr, terr->trainers[x][y], x, y, terr->pcX, terr->pcY);
    printCost(terr->trainers[x][y]);
}

/*
    Creates the player character. Randomly places on map
    only on allowed spots. Based on cost if possible to move here.
    Then calls findCost to fill the cost map for movement.
*/
void spawnRival(struct terrain *terr) {
    int x, y;
    x = rand() % 70 + 3;
    y = rand() % 15 + 2;

    while (terr->map[x][y] == '%' || terr->map[x][y] == '^' || terr->map[x][y] == '~') {
        x = rand() % 70 + 3;
        y = rand() % 15 + 2;
    }

    terr->trainers[x][y]->x = x;
    terr->trainers[x][y]->y = y;
    terr->trainers[x][y]->type[0] = charTypes[2];
    terr->trainers[x][y]->trainerType = 2;

    // dijkstra(terr, terr->trainers[x][y], x, y, terr->pcX, terr->pcY);
    // printCost(terr->trainers[x][y]);
}

/*
    Creates the player character. Randomly places on map
    only on allowed spots. Based on cost if possible to move here.
    Then calls findCost to fill the cost map for movement.
*/
void spawnSwimmer(struct terrain *terr) {
    int x, y;
    x = rand() % 70 + 3;
    y = rand() % 15 + 2;

    while (terr->map[x][y] != '~') {
        x = rand() % 70 + 3;
        y = rand() % 15 + 2;
    }

    terr->trainers[x][y]->x = x;
    terr->trainers[x][y]->y = y;
    terr->trainers[x][y]->type[0] = charTypes[3];
    terr->trainers[x][y]->trainerType = 3;

}

/*
    Creates the player character. Randomly places on map
    only on allowed spots. Based on cost if possible to move here.
    Then calls findCost to fill the cost map for movement.
*/
void spawnOther(struct terrain *terr) {
    int x, y;
    x = rand() % 70 + 3;
    y = rand() % 15 + 2;

    while (terr->map[x][y] == '%' || terr->map[x][y] == '^' || terr->map[x][y] == '~') {
        x = rand() % 70 + 3;
        y = rand() % 15 + 2;
    }

    terr->trainers[x][y]->x = x;
    terr->trainers[x][y]->y = y;
    terr->trainers[x][y]->type[0] = charTypes[4];
    terr->trainers[x][y]->trainerType = 4;

    // findCost(terr->trainers[x][y], terr);
}