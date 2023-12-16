#ifndef HEAP_H
#define HEAP_H

#include <stdio.h>
#include <stdlib.h>

// struct pqueue_node {
//     struct pqueue_node *next;
//     double data;
//     int x;
//     int y;
// };

// struct pqueue {
//     struct pqueue_node *front, *back;
//     int size;
// };

// int pqueue_init(struct pqueue *pq);
// int pqueue_destroy(struct pqueue *pq);
// int pqueue_insert(struct pqueue *pq, double i, int x, int y);
// int pqueue_update(struct pqueue *pq, double i, int x, int y);
// int pqueue_remove(struct pqueue *pq, double *i, int x, int y);
// int pqueue_front(struct pqueue *pq, double *i);
// int pqueue_size(struct pqueue *pq);
// int is_empty(struct pqueue *pq);

typedef struct {
    int x, y;
    int cost;
} tile;

typedef struct minHeap {
    int size;
    int cap;
    tile* *arr;
} minHeap;

tile *createTile(int x, int y, int cost);
minHeap *initHeap(int capacity);
void swapNodes(tile** a, tile** b);
void minHeapify(minHeap *heap, int idx);
int insert(minHeap *heap, tile *next);
tile *extractMin(minHeap *heap);
void decreaseKey(minHeap *heap, int idx);
int heapEmpty(minHeap *heap);
void destroyHeap(minHeap *heap);

#endif