#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <limits.h>
#include <math.h>
#include <unistd.h>
#include <getopt.h>

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

const int default_num_trainers = 10;

struct option long_options[] = {
    {"numtrainers", required_argument, 0, 'n'},
};

int num_trainers = 0;

struct world world;
pc_t *pc;
heap_t pq;

cost_map_t hiker_cost_map;
cost_map_t rival_cost_map;
cost_map_t swimmer_cost_map;
cost_map_t other_cost_map;

void followMinPath(struct terrain *terr, character_t *c, cost_map_t move_cost, queue_t *que);

/*
    Array to hold the possible terrain types, used for consistency
    across method
*/
const char terrType[8] = {':', '~', '%', '.', '^', '#', 'M', 'C'};

#define IM INT_MAX
int costs[4][11] = {
//   BM  BT  PA  PM  PC  TG  SG  MT  FT  WA  GA
    {IM, IM, 10, 10, 10, 20, 10, IM, IM, IM, 10}, // PC
    {IM, IM, 10, 50, 50, 15, 10, 15, 15, IM, IM}, // Hiker
    {IM, IM, 10, 50, 50, 20, 10, IM, IM, IM, IM}, // Rival
    // {IM, IM, IM, IM, IM, IM, IM, IM, IM,  7, IM}, // Swimmer
    {IM, IM, 10, 50, 50, 20, 10, IM, IM, IM, IM}, // Other
};
#undef IM


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

int priority_cmp(const void *p1, const void *p2) {
    return ((character_t *)p1)->move_cost - ((character_t *)p2)->move_cost;
}

/*
    Prints the grid to the terminal
*/
void printGrid(struct terrain *terr) {

    int i, j;
    for (i = 0; i < MAX_HEIGHT; i++) {
        for (j = 0; j < MAX_WIDTH; j++) {
            if (terr->trainers[i][j] != NULL) {
                if (terr->trainers[i][j]->type[0] == '@') {
                    printf(ANSI_COLOR_RED_BOLD "%c\x1b[0m", terr->trainers[i][j]->type[0]);
                } else {
                    printf(ANSI_COLOR_RED_BOLD "%c\x1b[0m", terr->trainers[i][j]->type[0]);
                }
            } else {
                if (terr->map[i][j] == 0) {
                    printf(ANSI_COLOR_GREEN_BR "%c\x1b[0m", terrType[0]);
                } else if (terr->map[i][j] == 1) {
                    printf(ANSI_COLOR_BLUE "%c", terrType[1]);
                } else if (terr->map[i][j] == 2) {
                    printf(ANSI_COLOR_MAGENTA "%c", terrType[2]);
                } else if (terr->map[i][j] == 3) {
                    printf(ANSI_COLOR_GREEN "%c", terrType[3]);
                } else if (terr->map[i][j] == 4) {
                    printf(ANSI_COLOR_RESET "%c", terrType[4]);
                } else if (terr->map[i][j] == 5) {
                    printf(ANSI_COLOR_YELLOW "%c", terrType[5]);
                } else if (terr->map[i][j] == 6) {
                    printf(ANSI_COLOR_RED "%c", terrType[6]);
                } else if (terr->map[i][j] == 7) {
                    printf(ANSI_COLOR_RED "%c", terrType[7]);
                }
            }
        }
        printf(ANSI_COLOR_RESET "\n");
    }
}

void printGridAsInts(struct terrain *terr) {
    for (int i = 0; i < MAX_HEIGHT; i++) {
        for (int j = 0; j < MAX_WIDTH; j++) {
            printf("%d ", terr->map[i][j]);
        }
    }
}

/*
    Creates the path in the North to South directions
*/
void verticalPath(struct terrain *terr, int startX, int endX) {
    
    int currX = startX;
    int currY = 0;

    if (world.currY > - 200) {
        terr->map[currY][currX] = 5;
        terr->n = currX;
    }

    int steps = rand() % 5 + 5;
    for (int i = 0; i < steps; i++) {
        currY++;
        terr->map[currY][currX] = 5;
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
        terr->map[currY][currX] = 5;
    }

    if (world.currY >= 200) {
        terr->map[currY][currX] = 2;
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
        terr->map[currY][currX] = 5;
        terr->w = startY;
    }

    int steps = rand() % 15 + 10;
    for (int i = 0; i < steps; i++) {
        currX++;
        terr->map[currY][currX] = 5;
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
        
        terr->map[currY][currX] = 5;
    }

    if (world.currX >= 200) {
        terr->map[currY][currX] = 2;
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
        if (terr->map[y][x] == 5 ||
            (x > 0 && terr->map[y][x-1] == 5) ||
            (x < MAX_WIDTH - 1 && terr->map[y][x+1] == 5) ||
            (y > 0 && terr->map[y-1][x] == 5) ||
            (y < MAX_HEIGHT - 1 && terr->map[y+1][x] == 5)) {
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

    if (terr->map[y][x] != 5 && terr->map[y+1][x] != 5 &&
            terr->map[y][x+1] != 5 && terr->map[y+1][x+1] != 5 &&
            terr->map[y][x] != 6 && terr->map[y+1][x] != 6 &&
            terr->map[y][x+1] != 6 && terr->map[y+1][x+1] != 6 &&
            terr->map[y][x] != 7 && terr->map[y+1][x] != 7 &&
            terr->map[y][x+1] != 7 && terr->map[y+1][x+1] != 7) {

        return 1;
    }

    return 0;
}

/*
    Chooses a random spot to place a 2x2 building on the terr[][] grid. 
    Validates location calling isAjacent() and isAvailable
*/
void placeRandomBuilding(struct terrain *terr, int build) {
        
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
        terr->map[y][x] = build;
        terr->map[y][x+1] = build;
        terr->map[y+1][x] = build;
        terr->map[y+1][x+1] = build;
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

    placeRandomBuilding(terr, 6);
    placeRandomBuilding(terr, 7);

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
        terr->map[0][i] = 2;
        terr->map[20][i] = 2;
    }

    for (i = 0; i < MAX_HEIGHT; i++) {
        terr->map[i][0] = 2;
        terr->map[i][79] = 2;
    }

}

/*
    Spawns an amount of trainers of random types based on
    either a default number or a provided amount of
    trainers via a switch --numtrainers
*/
void spawnTrainers(struct terrain *terr, int howMany) {

    character_t *n;

    for (int i = 0; i < howMany; i++) {
        if (i == 0 && howMany > 2) {
            n = spawnHiker(terr);
        } else if (i == 1 && howMany > 2) {
            n = spawnRival(terr);
        } else {
            int type = rand() % 4 + 1;
            if (type == 1) {
                n = spawnHiker(terr);
            } else if (type == 2) {
                n = spawnRival(terr);
            } else {
                n = spawnOther(terr);
            } 
        }
            
        //     // else if (type == 3) {
        //     //     n = spawnSwimmer(terr);
        //     // } 
        
        n->move_cost = 0;

        heap_insert(&pq, n);
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
            terr->map[j][i] = -1;
        }
    }

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
        curr.type = type;

        terr->map[y][x] = type;

        enqueue(&que, curr); //adds to queue to fill rest of grid

    }

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
                    && terr->map[newY][newX] == -1) {
                    terr->map[newY][newX] = deque.type;

                    struct tile next;
                    next.x = newX;
                    next.y = newY;
                    next.type = deque.type;

                    enqueue(&que, next);
                }
            }
        }

    }

    delQueue(&que);
    setPathGate(terr);

    initChar(terr);

    if (num_trainers == 0) {
        spawnTrainers(terr, default_num_trainers);
    } else {
        spawnTrainers(terr, num_trainers);
    }

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
    
    character_t *p = spawnPC(terr);
    pc = malloc(sizeof(pc_t));
    pc->x = p->x;
    pc->y = p->y;

    // pqueue_enqueue(pq, terr->trainers[pc->y][pc->x]);

    world.map = terr;
    world.worldMap[world.currY + CENTER][world.currX + CENTER] = terr;
}

/*
    Free that memory like Willy (1993)
*/
void destroy_world() {

    free(pc);

    for (int i = 0; i < 401; i++) {
        for (int j = 0; j < 401; j++) {
            if (world.worldMap[i][j] != NULL) {
                for (int k = 0; k < MAX_HEIGHT; k++) {
                    for (int l = 0; l < MAX_WIDTH; l++) {
                        free(world.worldMap[i][j]->trainers[k][l]);
                    }
                }
                free(world.worldMap[i][j]);
            }
        }
    }
}

/*
    fills the cost maps for trainer types
*/
void fill_cost_maps(struct terrain *terr) {
    // always do swimmer then rival then hiker
    // init_cost_map(&swimmer_cost_map);
    init_cost_map(&other_cost_map);
    init_cost_map(&rival_cost_map);
    init_cost_map(&hiker_cost_map);
    // create_cost_map(terr, &swimmer_cost_map, pc, costs[3], 3);
    create_cost_map(terr, &other_cost_map, pc, costs[3], 3);
    create_cost_map(terr, &rival_cost_map, pc, costs[2], 2);
    create_cost_map(terr, &hiker_cost_map, pc, costs[1], 1);
    // printf("Rival cost map:\n");
    // print_cost_map(rival_cost_map);
    // printf("\nHiker cost map:\n");
    // print_cost_map(hiker_cost_map);
    // printf("\nSwimmer cost map:\n");
    // print_cost_map(swimmer_cost_map);
    // printf("\nOther cost map:\n");
    // print_cost_map(other_cost_map);
}

/*
    Time to make some moves.
*/
void followMinPath(struct terrain *terr, character_t *c, cost_map_t move_cost, queue_t *que) {
    int move_x = c->x;
    int move_y = c->y;
    int move_c = 0;

    for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            if ((move_cost.map[c->y + i][c->x + j] < move_c || move_c == 0) && terr->trainers[c->y + i][c->x + j] == NULL && move_cost.map[c->y + i][c->x + j] != INT_MAX) {
                move_x = c->x + j;
                move_y = c->y + i;
                move_c = move_cost.map[move_y + i][move_x + j];
            }
        }
    }

    terr->trainers[move_y][move_x] = c;
    terr->trainers[c->y][c->x] = NULL;
    terr->trainers[move_y][move_x]->x = move_x;
    terr->trainers[move_y][move_x]->y = move_y;
    terr->trainers[move_y][move_x]->move_cost = terr->trainers[move_y][move_x]->move_cost - move_c;

    // heap_insert(&pq, terr->trainers[move_y][move_x]);

    struct tile n;
    n.character = terr->trainers[move_y][move_x];
    enqueue(que, n);

}

void moveNPC(struct terrain *terr, character_t *c, cost_map_t move_cost, queue_t *que) {

    int newX = c->x, newY = c->y;

    if (c->trainerType == 3) {                          // pacer
        if (c->direction == 0) {        // north
            if (move_cost.map[c->y-1][c->x] != INT_MAX) {
                newY = newY - 1;
            } else {
                newY = newY + 1;
                c->direction = 2;
            }
        } else if (c->direction == 1) { // east
            if (move_cost.map[c->y][c->x+1] != INT_MAX) {
                newX = newX + 1;
            } else {
                newX = newX - 1;
                c->direction = 3;
            }
        } else if (c->direction == 2) { // south
            if (move_cost.map[c->y+1][c->x] != INT_MAX) {
                newY = newY + 1;
            } else {
                newY = newY - 1;
                c->direction = 0;
            }
        } else if (c->direction == 3) { // west
            if (move_cost.map[c->y][c->x-1] != INT_MAX) {
                newX = newX - 1;
            } else {
                newX = newX + 1;
                c->direction = 1;
            }
        }

        if (terr->trainers[newY][newX] != NULL) {
            newX = c->x;
            newY = c->y;
        } else {
            c->move_cost = abs(move_cost.map[newY][newX] - c->move_cost);
        }

    } else if (c->trainerType == 4) {                   // wanderer
        if (c->direction == 0) {
            if (terr->map[c->y-1][c->x] == terr->map[c->y][c->x]) {
                newY = newY - 1;
            } else {
                c->direction = rand() % 4;
            }
        } else if (c->direction == 1) {
            if (terr->map[c->y][c->x+1] == terr->map[c->y][c->x]){ 
                newX = newX + 1;
            } else {
                c->direction = rand() % 4;
            }
        } else if (c->direction == 2) {
            if (terr->map[c->y+1][c->x] == terr->map[c->y][c->x]) {
                newY = newY + 1;
            } else {
                c->direction = rand() % 4;
            }
        } else if (c->direction == 3) {
            if (terr->map[c->y][c->x-1]) {
                newX = newX - 1;
            } else {
                c->direction = rand() % 4;
            }
        }

        if (terr->trainers[newY][newX] != NULL) {
            newX = c->x;
            newY = c->y;
        } else {
            c->move_cost = abs(move_cost.map[newY][newX] - c->move_cost);
        }

    } else if (c->trainerType == 5) {                   // sentry
        //do nothing. this doesn't move
    } else if (c->trainerType == 6) {                   // explorer
        if (c->direction == 0) {        // north
            if (move_cost.map[c->y-1][c->x] != INT_MAX) {
                newY = newY - 1;
            } else {
                c->direction = rand() % 4;
            }
        } else if (c->direction == 1) { // east
            if (move_cost.map[c->y][c->x+1] != INT_MAX) {
                newX = newX + 1;
            } else {
                c->direction = rand() % 4;
            }
        } else if (c->direction == 2) { // south
            if (move_cost.map[c->y+1][c->x] != INT_MAX) {
                newY = newY + 1;
            } else {
                c->direction = rand() % 4;
            }
        } else if (c->direction == 3) { // west
            if (move_cost.map[c->y][c->x-1] != INT_MAX) {
                newX = newX - 1;
            } else {
                c->direction = rand() % 4;
            }
        }

        if (terr->trainers[newY][newX] != NULL) {
            newX = c->x;
            newY = c->y;
        } else {
            c->move_cost = abs(move_cost.map[newY][newX] - c->move_cost);
        }
        
    }

    if (c->trainerType != 5 && (newY != c->y || newX != c->x)) {
        terr->trainers[newY][newX] = c;
        terr->trainers[c->y][c->x] = NULL;
        terr->trainers[newY][newX]->x = newX;
        terr->trainers[newY][newX]->y = newY;
    }

    struct tile n;
    n.character = c;
    enqueue(que, n);

    // heap_insert(&pq, terr->trainers[newY][newX]);

}


/*
    Where the running of game is done
*/
// int main(int argc, char *argv[]) {

//     int opt, idx = 0;
//     while ((opt = getopt_long(argc, argv, "n:", long_options, &idx)) != -1) {
//         switch (opt) {
//             case 'n':
//                 num_trainers = atoi(optarg);
//                 if (num_trainers < 10) {
//                     printf("Num_trainers value set to minimum 10.");
//                     num_trainers = 10;
//                 }
//                 break;
//             default:
//                 break;
//         }
//     }

//     //initialize things here
//     srand(time(NULL));

//     pq = pqueue_init(priority_cmp, num_trainers + 1);

//     initWorld();

//     char choice;

//     while (1) {

//         if (!world.worldMap[world.currY + CENTER][world.currX + CENTER]) {
//             struct terrain *terr;
//             terr = malloc(sizeof(struct terrain));
//             terr = generate(terr);
//             world.map = terr;
//             world.worldMap[world.currY + CENTER][world.currX + CENTER] = terr;
//         }

//         world.map = world.worldMap[world.currY + CENTER][world.currX + CENTER];

//         fill_cost_maps(world.map);

//         printGrid(world.map);

//         printf(ANSI_COLOR_RESET "(%d, %d) Enter command: ", world.currX, world.currY);
//         scanf(" %s", &choice);

//         if (choice == 'n' && world.currY - 1 >= -200) {
//             world.currY--;
//         } else if (choice == 's' && world.currY + 1 < 201) {
//             world.currY++;
//         } else if (choice == 'e' && world.currX + 1 < 201) {
//             world.currX++;
//         } else if (choice == 'w' && world.currX - 1 >= -200) {
//             world.currX--;
//         } else if (choice == 'f') {
//             printf("Enter x, y: ");
//             int x, y;
//             scanf("%d, %d", &x, &y);
//             if (x >= -200 && x < 201 && y >= -200 && y < 201) {
//                 world.currX = x;
//                 world.currY = y;
//             } else {
//                 printf("Invalide choice: %d, %d must be in bounds -200, 200\n", x, y);
//             }
//         } else if (choice == 'q') {
//             destroy_world();
//             break;
//         } else {
//             printf("Invalid choice: %c use n,s,e,w,f,q\n", choice);
//         }

//     }

// }


/*
    Main for assignment1-04, just loops movement of NPCs
*/
int main(int argc, char *argv[]) {
    //initialize things here
    srand(time(NULL));

    int opt, idx = 0;
    while ((opt = getopt_long(argc, argv, "n:", long_options, &idx)) != -1) {
        switch (opt) {
            case 'n':
                num_trainers = atoi(optarg);
                break;
            default:
                break;
        }
    }

    heap_init(&pq, priority_cmp, NULL);

    initWorld();
    
    fill_cost_maps(world.map);

    // print_cost_map(&rival_cost_map);
    // print_cost_map(&hiker_cost_map);
    // print_cost_map(&other_cost_map);

    printGrid(world.map);

    printf("\n\n");

    while (1) {

        queue_t que;
        initQueue(&que);

        while (pq.size != 0) {
            // character_t *c = pqueue_dequeue(pq);
            character_t *c = heap_remove_min(&pq);

            if (c->trainerType == 1) {
                followMinPath(world.map, c, hiker_cost_map, &que);
            } else if (c->trainerType == 2) {
                followMinPath(world.map, c, rival_cost_map, &que);
            } else if (c->trainerType >= 3) {
                moveNPC(world.map, c, other_cost_map, &que);
            }
            
            // else if (c->trainerType == 3) {
            //     moveNPC(world.map, c, swimmer_cost_map, &que);
            // } 
            usleep(250000);
            // sleep(1);

            printGrid(world.map);

            printf("\n\n");
        }

        while (!empty(&que)) {
            struct tile n = dequeue(&que);
            heap_insert(&pq, n.character);
        }

        delQueue(&que);

    }
}