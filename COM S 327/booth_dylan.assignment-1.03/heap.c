#include "heap.h"
#include <stdlib.h>

minHeap *initHeap(int capacity) {
    minHeap *heap = (minHeap*)malloc(sizeof(minHeap));
    
    if (heap == NULL) {
        return NULL;
    }
    
    heap->size = 0;
    heap->cap = capacity;

    heap->arr = (tile**)malloc(capacity * sizeof(tile));

    return heap;
}

tile* createTile(int x, int y, int cost) {
    tile *t = (tile*)malloc(sizeof(tile));
    if (t) {
        t->x = x;
        t->y = y;
        t->cost = cost;
    }
    return t;
}

void swapNodes(tile **a, tile **b) {
    tile *tmp = *a;
    *a = *b;
    *b = tmp;
}

void minHeapify(minHeap *heap, int idx) {

    if (heap->size == 0) {
        return;
    }

    int left = idx * 2 + 1;
    int right = idx * 2 + 2;
    int min = idx;

    if (left < heap->size && heap->arr[left]->cost < heap->arr[min]->cost) {
        min = left;
    }
    if (right < heap->size && heap->arr[right]->cost < heap->arr[min]->cost) {
        min = right;
    }

    if (min != idx) {
        swapNodes(&heap->arr[idx], &heap->arr[min]);
        minHeapify(heap, min);
    }
}

int insert(minHeap *heap, tile *t) {
    if (heap->size == heap->cap) {
        return 0;
    }

    int idx = heap->size;
    heap->arr[idx] = t;
    heap->size++;

    while (idx > 0 && heap->arr[(idx - 1) / 2]->cost > heap->arr[idx]->cost) {
        swapNodes(&heap->arr[idx], &heap->arr[(idx - 1) / 2]);
        idx = (idx - 1) / 2;
    }

    return 1;
}

tile *extractMin(minHeap *heap) {
    if (heap->size == 0) {
        return NULL;
    }

    tile *root = heap->arr[0];
    tile *last = heap->arr[heap->size - 1];

    heap->arr[0] = last;
    heap->size--;

    minHeapify(heap, 0);

    return root;
}

void decreaseKey(minHeap *heap, int idx) {
    
    if (idx > heap->size) {
        return;
    }

    // heap->arr[idx]->key = newKey;

    // while (idx > 0 && heap->arr[(idx - 1) / 2]->key > heap->arr[idx]->key) {
    //     swapNodes(&heap->arr[idx], &heap->arr[(idx - 1) / 2]);
    //     idx = (idx - 1) / 2;
    // }
}

int heapEmpty(minHeap *heap) {
    return (heap->size == 0);
}

void destroyHeap(minHeap *heap) {
    free(heap->arr);
    free(heap);
}

