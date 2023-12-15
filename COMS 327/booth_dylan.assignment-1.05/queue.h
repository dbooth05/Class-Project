#ifndef QUEUE_H
#define QUEUE_H

struct queue_node_t {
    struct queue_node_t *next;
    void *data;
};

typedef struct queue {
    struct queue_node_t *head, *tail;
    int size;
} queue_t;

void initQueue(struct queue *q);
int empty(struct queue *q);
void delQueue(struct queue *q);
int enqueue(struct queue *q, void *v);
void *dequeue(struct queue *q);
void printQueue(struct queue *q);

#endif