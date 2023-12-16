#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <limits.h>
#include <math.h>

#include "queue.h"
#include "pokemon.h"
#include "heap.h"
#include "character.h"

#define ANSI_COLOR_RED      "\x1b[31m"
#define ANSI_COLOR_RED_BOLD "\x1b[31;1m"
#define ANSI_COLOR_GREEN    "\x1b[32m"
#define ANSI_COLOR_GREEN_BR "\x1b[92;1m"
#define ANSI_COLOR_YELLOW   "\x1b[33m"
#define ANSI_COLOR_BLUE     "\x1b[34m"
#define ANSI_COLOR_MAGENTA  "\x1b[35m"
#define ANSI_COLOR_CYAN     "\x1b[36m"
#define ANSI_COLOR_RESET    "\x1b[0m"

#define WORLD_SIZE 401
#define CENTER 200

struct world world;

/*
    Array to hold the possible terrain types, used for consistency
    across method
*/

const char terrType[8] = {':', '~', '%', '.', '^', '#', 'M', 'C'};
const int costs[5][11] = {{INT_MAX, INT_MAX, 10, 10, 10, 20, 10, INT_MAX, INT_MAX, INT_MAX, 10},
                        {INT_MAX, INT_MAX, 10, 50, 50, 15, 10, 15, 15, INT_MAX, INT_MAX},
                        {INT_MAX, INT_MAX, 10, 50, 50, 20, 10, INT_MAX, INT_MAX, INT_MAX, INT_MAX},
                        {INT_MAX, INT_MAX, INT_MAX, INT_MAX, INT_MAX, INT_MAX, INT_MAX, INT_MAX, INT_MAX, 7, INT_MAX},
                        {INT_MAX, INT_MAX, 10, 50, 50, 20, 10, INT_MAX, INT_MAX, INT_MAX, INT_MAX}};


const int costPath[6] = {INT_MAX, 15, 25, 20, 10, 25};
                        //BLDR Bor Tree Bor Path PMART PCntr Tgras SGras Mtn Forest Water Gate
/*
    Struct to hold the individual segments of the map with the
    gates available for lining them up
*/
struct world {
    struct terrain *worldMap[WORLD_SIZE][WORLD_SIZE];
    struct terrain *map;                            //The pointer to a map of this section
    int currX, currY;                               //Position on world map
};

// typedef struct cost_map {
//     int map[MAP_WId]
// }

/*
    Prints the grid to the terminal
*/
void printGrid(struct terrain *terr) {

    int i, j;
    for (j = 0; j < MAX_HEIGHT; j++) {
        for (i = 0; i < MAX_WIDTH; i++) {
            if (terr->trainers[i][j]->trainerType != -1) {
                if (terr->trainers[i][j]->type[0] == '@') {
                    printf(ANSI_COLOR_RED_BOLD "%c\x1b[0m", terr->trainers[i][j]->type[0]);
                } else {
                    printf(ANSI_COLOR_MAGENTA "%c", terr->trainers[i][j]->type[0]);
                }
            } else {
                if (terr->map[i][j] == terrType[0]) {
                    printf(ANSI_COLOR_GREEN_BR "%c\x1b[0m", terr->map[i][j]);
                } else if (terr->map[i][j] == terrType[1]) {
                    printf(ANSI_COLOR_BLUE "%c", terr->map[i][j]);
                } else if (terr->map[i][j] == terrType[2]) {
                    printf(ANSI_COLOR_RESET "%c", terr->map[i][j]);
                } else if (terr->map[i][j] == terrType[3]) {
                    printf(ANSI_COLOR_GREEN "%c", terr->map[i][j]);
                } else if (terr->map[i][j] == terrType[4]) {
                    printf(ANSI_COLOR_RESET "%c", terr->map[i][j]);
                } else if (terr->map[i][j] == terrType[5]) {
                    printf(ANSI_COLOR_YELLOW "%c", terr->map[i][j]);
                } else {
                    printf(ANSI_COLOR_RED "%c", terr->map[i][j]);
                }
            }
        }
        printf("\n");
    }
}

/*
    Creates the path in the North to South directions
*/
void verticalPath(struct terrain *terr, int startX, int endX) {
    
    int currX = startX;
    int currY = 0;

    if (world.currY > - 200) {
        terr->map[currX][currY] = terrType[5];
        terr->n = currX;
    }

    int steps = rand() % 5 + 5;
    for (int i = 0; i < steps; i++) {
        currY++;
        terr->map[currX][currY] = terrType[5];
    }

    while (currX != endX || currY != MAX_HEIGHT - 1) {
        
        if (currX < endX) {
            currX++;
        } else if (currX > endX) {
            currX--;
        } else if (currY < MAX_HEIGHT -1) {
            currY++;
        } else if (currY > MAX_HEIGHT -1) {
            currY--;
        }
        terr->map[currX][currY] = terrType[5];
    }

    if (world.currY >= 200) {
        terr->map[currX][currY] = terrType[2];
    } else {
        terr->s = endX;
    }

}

/*
    Makes path in the West to East direction
*/
void horizontalPath(struct terrain *terr, int startY, int endY) {
    int currX = 0;
    int currY = startY;

    if (world.currX > -200) {
        terr->map[currX][currY] = terrType[5];
        terr->w = startY;
    }

    int steps = rand() % 15 + 10;
    for (int i = 0; i < steps; i++) {
        currX++;
        terr->map[currX][currY] = terrType[5];
    }
    
    while (currX != MAX_WIDTH - 1 || currY != endY) {
        
        if (currY < endY) {
            currY++;
        } else if (currY > endY) {
            currY--;
        } else if (currX < MAX_WIDTH - 1) {
            currX++;
        } else if (currX > MAX_WIDTH - 1) {
            currX--;
        }
        
        terr->map[currX][currY] = terrType[5];
    }

    if (world.currX >= 200) {
        terr->map[currX][currY] = terrType[2];
    } else {
        terr->e = endY;
    }
}

/*
    Returns boolean value if provided x,y point for the building M/C
    is adjacent to a path in the terr[][] grid
*/
int isAdjacent(struct terrain *terr, int x, int y) {
    // Check if the cell at (x, y) is adjacent to the path
    if (x >= 0 && x < MAX_WIDTH && y >= 0 && y < MAX_HEIGHT) {
        if (terr->map[x][y] == terrType[5] ||
            (x > 0 && terr->map[x - 1][y] == terrType[5]) ||
            (x < MAX_WIDTH - 1 && terr->map[x + 1][y] == terrType[5]) ||
            (y > 0 && terr->map[x][y - 1] == terrType[5]) ||
            (y < MAX_HEIGHT - 1 && terr->map[x][y + 1] == terrType[5])) {
            return 1; // Cell is adjacent to the path
        }
    }
    return 0; // Cell is not adjacent to the path
}

/*
    Returns boolean value if provided x,y point for the building M/C
    is not blocking/on a path in terr[][] grid
*/
int isAvailable(struct terrain *terr, int x, int y) {
    // Check if there's enough space for a 2x2 building without overlap

    if (terr->map[x][y] != terrType[5] && terr->map[x][y+1] != terrType[5] &&
            terr->map[x+1][y] != terrType[5] && terr->map[x+1][y+1] != terrType[5] &&
            terr->map[x][y] != terrType[6] && terr->map[x][y+1] != terrType[6] &&
            terr->map[x+1][y] != terrType[6] && terr->map[x+1][y+1] != terrType[6] &&
            terr->map[x][y] != terrType[7] && terr->map[x][y+1] != terrType[7] &&
            terr->map[x+1][y] != terrType[7] && terr->map[x+1][y+1] != terrType[7]) {

        return 1;
    }

    return 0;
}

/*
    Chooses a random spot to place a 2x2 building on the terr[][] grid. 
    Validates location calling isAjacent() and isAvailable
*/
void placeRandomBuilding(struct terrain *terr, char build) {
        
    int chance = -45 * terr->manDistance;                            // Chance value to generate building
    chance = ceil(fmax(5, ((chance/200) + 50)));                      // Chance value to generate building
    int c = rand() % 100 + 1;

    if (c <= chance || (world.currX == 0 && world.currY == 0)) {
        // Randomly select a location adjacent to the path
        int x, y;
        x = rand() % 70 + 5;
        y = rand() % 15 + 3;

        while (!isAdjacent(terr, x, y) || !isAvailable(terr, x, y)) {
            x = rand() % 70 + 5;
            y = rand() % 15 + 3;
        }
    
        // Place a 2x2 building at the selected location
        terr->map[x][y] = build;
        terr->map[x+1][y] = build;
        terr->map[x][y+1] = build;
        terr->map[x+1][y+1] = build;
    }

}

/*
    Checks to see if there is any generated world segments around its location
    in world map. If so, uses those generated segments gate locations. Otherwise,
    chooses random points to use as gates. Calls functions to generate a vertical and
    horizontal path segments using the start and end points. Then will call function
    to randomly place buildings.
*/
void setPathGate(struct terrain *terr) {

    // Vertical path generation (N->S)
    int start, end;
    struct terrain *curr;

    if (world.currY + CENTER-1 >= 0 && world.worldMap[world.currX + CENTER][world.currY + CENTER-1]) {             // Check north terrain if generated
        curr = world.worldMap[world.currX + CENTER][world.currY + CENTER -1];
        start = curr->s; 
    } else {
        start = rand() % 70 + 5;
    }
    
    if (world.currY + CENTER +1 < WORLD_SIZE && world.worldMap[world.currX + CENTER][world.currY + CENTER +1]) {     // Check south terrain if generated
        curr = world.worldMap[world.currX + CENTER][world.currY + CENTER +1];
        end = curr->n;
    } else {
        end = rand() % 70 + 5;
    }
    terr->s = end;

    verticalPath(terr, start, end);
    // dijkstraPath(terr);

    // Now onto horizontal path generation (W->E)
    
    if (world.currX + CENTER -1 >= 0 && world.worldMap[world.currX + CENTER -1][world.currY + CENTER]) {             // Check west terrain if generated
        curr = world.worldMap[world.currX + CENTER -1][world.currY + CENTER];
        start = curr->e;
    } else {
        start = rand() % 15 + 3;
    }
    terr->w = start;

    if (world.currX + CENTER +1 < WORLD_SIZE && world.worldMap[world.currX + CENTER +1][world.currY + CENTER]) {     // Check east terrain if generated
        curr = world.worldMap[world.currX + CENTER +1][world.currY + CENTER];
        end = curr->w;
    } else {
        end = rand() % 15 + 3;
    }

    horizontalPath(terr, start, end);

    placeRandomBuilding(terr, terrType[6]);
    placeRandomBuilding(terr, terrType[7]);

}

/*
    Set the borders as boulders and changes struct
    terrain edge status to true
*/
void setBorders(struct terrain *terr) {

    /*
        This is where I need to start from
    */

    int i;

    for (i = 0; i < MAX_WIDTH; i++) {
        terr->map[i][0] = terrType[2];
        terr->map[i][20] = terrType[2];
    }

    for (i = 0; i < MAX_HEIGHT; i++) {
        terr->map[0][i] = terrType[2];
        terr->map[79][i] = terrType[2];
    }

}

/*
    Given the map 2D array of terrain structures, this method
    will generate the grid initially by choosing random points 
    and randomly assigning them a terrain type. Then using queues
    will fill the rest of the grid. Calls setBorders() to set borders
    and setPath() to create gates, set path between gates, and place buildings.
*/
struct terrain* generate(struct terrain *terr) {
    
    for (int i = 0; i < MAX_WIDTH; i++) {
        for (int j = 0; j < MAX_HEIGHT; j++) {
            terr->map[i][j] = ' ';
        }
    }

    initChar(terr);

    //setting manhattan distance from 200,200
    terr->manDistance = abs(world.currX - 0) + abs(world.currY - 0);
        
    setBorders(terr);

    struct queue que;
    initQueue(&que);

    int x, y, type;

    for (int i = 0; i < 20; i++) {

        x = rand() % 75 + 1;
        y = rand() % 15 + 1;
        type = rand() % 5;

        struct tile curr;
        curr.x = x;
        curr.y = y;
        curr.type[0] = terrType[type];

        terr->map[x][y] = terrType[type];
        enqueue(&que, curr); //adds to queue to fill rest of grid
        //followed by path
    }
    printf("\n\n");

    /*
        This segment of function goes through queue
        and fills rest of grid growing off other elements
        in the queue.
    */
    while (!empty(&que)) {

        struct tile deque = dequeue(&que); //variable just removed from queue
        int currX = deque.x;
        int currY = deque.y;

        int changePos[] = {-1, 0, 1};
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                int newX = currX + changePos[j];
                int newY = currY + changePos[i];

                if (newX > 0 && newX < MAX_WIDTH && newY > 0 && newY < 20
                    && terr->map[newX][newY] == ' ') {
                    terr->map[newX][newY] = deque.type[0];

                    struct tile next;
                    next.x = newX;
                    next.y = newY;
                    next.type[0] = deque.type[0];

                    enqueue(&que, next);
                }
            }
        }

    }

    delQueue(&que);
    setPathGate(terr);

    spawnPC(terr);
    spawnHiker(terr);
    spawnRival(terr);
    spawnSwimmer(terr);

    return terr;
}

/*
    Initializes the struct world and allocates all memory required.
    sets current position in world at 0, 0
*/
void initWorld() {
    world.currX = 0;
    world.currY = 0;

    struct terrain *terr;

    terr = malloc(sizeof(struct terrain));

    terr = generate(terr);

    world.map = terr;
    world.worldMap[world.currX + CENTER][world.currY + CENTER] = terr;

}

/*
    Free that memory like Willy (1993)
*/
void destroy_world() {
    for (int i = 0; i < 401; i++) {
        for (int j = 0; j < 401; j++) {
            if (world.worldMap[i][j] != NULL) {
                free(world.worldMap[i][j]);
                free(world.worldMap[i][j]->trainers[i][j]);
            }
        }
    }
}

// void dijkstra(int V, int source)

int main(int argc, char *argv[]) {

    //initialize things here
    srand(time(NULL));

    initWorld();

    char choice;

    while (1) {

        if (!world.worldMap[world.currX + CENTER][world.currY + CENTER]) {
            struct terrain *terr;
            terr = malloc(sizeof(struct terrain));
            terr = generate(terr);
            world.map = terr;
            world.worldMap[world.currX + CENTER][world.currY + CENTER] = terr;
        }

        world.map = world.worldMap[world.currX + CENTER][world.currY + CENTER];

        printGrid(world.map);

        printf(ANSI_COLOR_RESET "(%d, %d) Enter command: ", world.currX, world.currY);
        scanf(" %s", &choice);

        if (choice == 'n' && world.currY - 1 >= -200) {
            world.currY--;
        } else if (choice == 's' && world.currY + 1 < 201) {
            world.currY++;
        } else if (choice == 'e' && world.currX + 1 < 201) {
            world.currX++;
        } else if (choice == 'w' && world.currX - 1 >= -200) {
            world.currX--;
        } else if (choice == 'f') {
            printf("Enter x, y: ");
            int x, y;
            scanf("%d, %d", &x, &y);
            if (x >= -200 && x < 201 && y >= -200 && y < 201) {
                world.currX = x;
                world.currY = y;
            } else {
                printf("Invalide choice: %d, %d must be in bounds -200, 200\n", x, y);
            }
        } else if (choice == 'q') {
            destroy_world();
            break;
        } else {
            printf("Invalid choice: %c use n,s,e,w,f,q\n", choice);
        }

    }

}