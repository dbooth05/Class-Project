#ifndef QUEUE_H
#define QUEUE_H

#include "pokemon.h"

struct tile {  //this is new
    int x, y;
    int type;
    int cost;
    character_t *character;
};

struct queue_node {
    struct queue_node *next;
    struct tile data;
};

typedef struct queue {
    struct queue_node *head, *tail;
    int size;
} queue_t;

void initQueue(struct queue *q);
int empty(struct queue *q);
void delQueue(struct queue *q);
int enqueue(struct queue *q, struct tile add);
struct tile dequeue(struct queue *q);
void printQueue(struct queue *q);

#endif