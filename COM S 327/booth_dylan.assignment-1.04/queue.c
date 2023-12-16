#include <stdlib.h>
#include <stdio.h>

#include "queue.h"
#include "pokemon.h"

/*
    Initializs the queue
*/
void initQueue(struct queue *q) {
    q -> head = NULL;
    q -> tail = NULL;
    q -> size = 0;
}

/*
    Returns if the queue is empty
*/
int empty(struct queue *q) {
    return !q -> size;
}

/* 
    Frees the data storage of the queue
*/
void delQueue(struct queue *q) {

    struct queue_node *n;
    for (n = q -> head; n; n = q -> head) {
        q -> head = n -> next;
        free(n);
    }

    q->tail = NULL;
    q->size = 0;

}

/*
    Adds a new terrain struct to tail of queue
*/
int enqueue(struct queue *q, struct tile add) {

    struct queue_node *n;

    if (!(n = malloc(sizeof(struct queue_node)))) {
        return -1;
    }

    n->data = add;
    n->next = NULL;

    if (q -> tail == NULL) {
        q->head = q->tail = n;
    } else {
        q->tail->next = n;
        q->tail = n;
    }

    q -> size++;

    return 0;
}

/*
    Removes the first terrain struct in the queue
*/
struct tile dequeue(struct queue *q) {

    struct queue_node *n = q -> head;
    struct tile value = n->data;

    if (!(q -> head = n -> next)) {
        q -> tail = NULL;
    }

    q -> head = n -> next;
    q -> size--;

    free(n);

    return value;
}

void printQueue(struct queue *q) {

    struct queue_node *n = q->head;
    printf("Queue:");
    while (n -> next != NULL) {
        printf("%c", n->data.type);
        n = n -> next;
    }
}