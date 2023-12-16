#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <limits.h>

const int MAX_SIZE = 1500;
const int ROWS = 21;
const int COLUMNS = 80;

/*
    Array to hold the possible terrain types, used for consistency
    across method
*/
const char terrType[8] = {':', '~', '%', '.', '^', '#', 'M', 'C'};

/*
    Structure for each space on the map to hold terrain type, 
    and if there is a pokemon
*/
struct terrain {
    char terrainType[1];
    int x, y;
    char pokemon[20];
    int edge;
};

/*
    Queue structure for discovering and setting the rest of the map
    for terrainType
*/
struct queue {
    int head, tail, inputNum, size;
    struct terrain *terr;
};

/*
    Initializs the queue
*/
void initQueue(struct queue *q) {
    q -> size = MAX_SIZE;
    q -> inputNum = 0;
    q -> head = 0;
    q -> tail = 0;
    q -> terr = (struct terrain *)malloc(sizeof(struct terrain) * q -> size);
}

/*
    Returns if the queue is empty
*/
int empty(struct queue *q) {
    return (q -> inputNum == 0);
}

/* 
    Frees the data storage of the queue
*/
void delQueue(struct queue *q) {
    free(q -> terr);
}

/*
    Adds a new terrain struct to back of queue
*/
int enqueue(struct queue *q, struct terrain *add) {

    q -> terr[q -> tail] = *add;
    q -> tail = (q -> tail + 1) % q -> size;
    q -> inputNum++;
    return 1;
}

/*
    Removes the first terrain struct in the queue
*/
struct terrain* dequeue(struct queue *q) {
    struct terrain *terr;

    if (empty(q)) {
        return NULL;
    }

    terr = &q -> terr[q -> head];
    q -> head = (q -> head + 1) % q -> size;
    q -> inputNum--;

    return terr;
}

/*
    Prints the grid to the terminal
*/
void printGrid(struct terrain terr[COLUMNS][ROWS]) {

    int i, j;
    for (j = 0; j < ROWS; j++) {
        // char line[COLUMNS];
        for (i = 0; i < COLUMNS; i++) {
            struct terrain curr = terr[i][j];
            if (curr.terrainType[0]) {
                // line[i] = curr.terrainType[0];
                printf("%c", terr[i][j].terrainType[0]);
            } else {
                // line[i] = '.';
                printf("%c", ',');
            }
        }
        // printf("%c\n", line);
        printf("\n");
    }

}

/*
    Creates the path in the North to South directions
*/
void verticalPath(struct terrain terr[COLUMNS][ROWS], struct terrain *start, struct terrain *end) {
    
    int currX = start->x;
    int currY = start->y;

    int steps = rand() % 5 + 5;
    for (int i = 0; i < steps; i++) {
        currY++;
        terr[currX][currY].terrainType[0] = terrType[5];
    }

    while (currX != end->x || currY != end->y) {
        
        if (terr[currX][currY].edge) {
            continue;
        } else if (currX < end->x) {
            currX++;
        } else if (currX > end->x) {
            currX--;
        } else if (currY < end->y) {
            currY++;
        } else if (currY > end->y) {
            currY--;
        }
        terr[currX][currY].terrainType[0] = terrType[5];
    }

}

/*
    Makes path in the West to East direction
*/
void horizontalPath(struct terrain terr[COLUMNS][ROWS], struct terrain *start, struct terrain *end) {
    int currX = start->x;
    int currY = start->y;

    int steps = rand() % 15 + 10;
    for (int i = 0; i < steps; i++) {
        currX++;
        terr[currX][currY].terrainType[0] = terrType[5];
    }
    
    while (currX != end->x || currY != end->y) {
        
        if (terr[currX][currY].edge) {
            continue;
        } else if (currY < end->y) {
            currY++;
        } else if (currY > end->y) {
            currY--;
        } else if (currX < end->x) {
            currX++;
        } else if (currX > end->x) {
            currX--;
        }
        
        terr[currX][currY].terrainType[0] = terrType[5];
    }
}

/*
    Returns boolean value if provided x,y point for the building M/C
    is adjacent to a path in the terr[][] grid
*/
int isAdjacent(struct terrain terr[COLUMNS][ROWS], int x, int y) {
    // Check if the cell at (x, y) is adjacent to the path
    if (x >= 0 && x < COLUMNS && y >= 0 && y < ROWS) {
        if (terr[x][y].terrainType[0] == terrType[5] ||
            (x > 0 && terr[x - 1][y].terrainType[0] == terrType[5]) ||
            (x < COLUMNS - 1 && terr[x + 1][y].terrainType[0] == terrType[5]) ||
            (y > 0 && terr[x][y - 1].terrainType[0] == terrType[5]) ||
            (y < ROWS - 1 && terr[x][y + 1].terrainType[0] == terrType[5])) {
            return 1; // Cell is adjacent to the path
        }
    }
    return 0; // Cell is not adjacent to the path
}

/*
    Returns boolean value if provided x,y point for the building M/C
    is not blocking/on a path in terr[][] grid
*/
int isAvailable(struct terrain terr[COLUMNS][ROWS], int x, int y) {
    // Check if there's enough space for a 2x2 building without overlap

    if (terr[x][y].terrainType[0] != terrType[5] && terr[x][y+1].terrainType[0] != terrType[5] &&
            terr[x+1][y].terrainType[0] != terrType[5] && terr[x+1][y+1].terrainType[0] != terrType[5] &&
            terr[x][y].terrainType[0] != terrType[6] && terr[x][y+1].terrainType[0] != terrType[6] &&
            terr[x+1][y].terrainType[0] != terrType[6] && terr[x+1][y+1].terrainType[0] != terrType[6] &&
            terr[x][y].terrainType[0] != terrType[7] && terr[x][y+1].terrainType[0] != terrType[7] &&
            terr[x+1][y].terrainType[0] != terrType[7] && terr[x+1][y+1].terrainType[0] != terrType[7]) {

        return 1;
    }

    return 0;
}

/*
    Chooses a random spot to place a 2x2 building on the terr[][] grid. 
    Validates location calling isAjacent() and isAvailable
*/
void placeRandomBuilding(struct terrain terr[COLUMNS][ROWS], char build) {
    
    // Randomly select a location adjacent to the path
    int x, y;
    x = rand() % 70 + 5;
    y = rand() % 15 + 3;

    while (!isAdjacent(terr, x, y) || !isAvailable(terr, x, y)) {
        x = rand() % 70 + 5;
        y = rand() % 15 + 3;
    }
    
    // Place a 2x2 building at the selected location
    terr[x][y].terrainType[0] = build;
    terr[x+1][y].terrainType[0] = build;
    terr[x][y+1].terrainType[0] = build;
    terr[x+1][y+1].terrainType[0] = build;
}

/*
    Chooses 1 spot on each border wall to make path, then will 
    create path using a simple path generation. After path generation
    is complete, calls placeRandomBuilding() with building type to add 
    building to terr[][] grid
*/
void setPath(struct terrain terr[COLUMNS][ROWS]) {
    
    struct terrain *start, *end;
    int startCoord, endCoord;

    //setting gates on x-axis (north south)
    startCoord = rand() % 70 + 5;
    endCoord = rand() % 70 + 5;

    terr[startCoord][0].terrainType[0] = terrType[5];
    terr[endCoord][20].terrainType[0] = terrType[5];

    start = &terr[startCoord][0];
    end = &terr[endCoord][20];
    verticalPath(terr, start, end);

    //setting gates on y-axis (east west)
    startCoord = rand() % 15 + 3;
    endCoord = rand() % 15 + 3;

    terr[0][startCoord].terrainType[0] = terrType[5];
    terr[79][endCoord].terrainType[0] = terrType[5];

    start = &terr[0][startCoord];
    end = &terr[79][endCoord];
    horizontalPath(terr, start, end);

    placeRandomBuilding(terr, terrType[6]);
    placeRandomBuilding(terr, terrType[7]);

}

/*
    Set the borders as boulders and changes struct
    terrain edge status to true
*/
void setBorders(struct terrain terr[COLUMNS][ROWS]) {

    int i;
    struct terrain curr;

    for (i = 0; i < COLUMNS; i++) {
        curr.terrainType[0] = terrType[2];
        curr.edge = 1;
        terr[i][0] = curr;
        terr[i][20] = curr;
    }

    for (i = 0; i < ROWS; i++) {
        curr.terrainType[0] = terrType[2];
        terr[0][i] = curr;
        terr[79][i] = curr;
    }

}

/*
    Given the map 2D array of terrain structures, this method
    will generate the grid initially by choosing random points 
    and randomly assigning them a terrain type. Then using queues
    will fill the rest of the grid. Calls setBorders() to set borders
    and setPath() to create gates, set path between gates, and place buildings.
*/
int generate(struct terrain terr[COLUMNS][ROWS]) {

    setBorders(terr);

    struct queue que;
    initQueue(&que);

    int x, y, type;

    for (int j = 0; j < ROWS; j++) {
        for (int i = 0; i < COLUMNS; i++) {
            terr[i][j].x = i;
            terr[i][j].y = j;
        }
    }


    for (int i = 0; i < 20; i++) {

        struct terrain curr;

        x = rand() % 75 + 1;
        y = rand() % 15 + 1;
        type = rand() % 5;

        curr.terrainType[0] = terrType[type];

        curr.x = x;
        curr.y = y;

        terr[x][y] = curr;
        enqueue(&que, &curr); //adds to queue to fill rest of grid

        //followed by path
    }

    /*
        This segment of function goes through queue
        and fills rest of grid growing off other elements
        in the queue.
    */
    while (!empty(&que)) {

        struct terrain *deque = dequeue(&que); //variable just removed from queue
        int currX = deque -> x;
        int currY = deque -> y;

        int changePos[] = {-1, 0, 1};
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                int newX = currX + changePos[j];
                int newY = currY + changePos[i];

                if (newX > 0 && newX < COLUMNS && newY > 0 && newY < 20
                    && !terr[newX][newY].terrainType[0]) {
                    struct terrain newTerr;
                    newTerr.terrainType[0] = deque -> terrainType[0];
                    newTerr.x = newX;
                    newTerr.y = newY;
                    terr[newX][newY] = newTerr;
                    enqueue(&que, &newTerr);
                }
            }
        }

    }

    delQueue(&que);
    setPath(terr);

    return 0;
}

int main(int argc, char *argv[]) {

    //initialize things here
    srand(time(NULL));

    struct terrain terr[80][21] = {};
    generate(terr);

    printGrid(terr);

}