#ifndef POKE327_H
#define POKE327_H

#include <stdlib.h>
#include <assert.h>
#include <typeinfo>

#include "heap.h"

#define malloc(size) ({          \
  void *_tmp;                    \
  assert((_tmp = malloc(size))); \
  _tmp;                          \
})

/* Returns true if random float in [0,1] is less than *
 * numerator/denominator.  Uses only integer math.    */
# define rand_under(numerator, denominator) \
  (rand() < ((RAND_MAX / denominator) * numerator))

/* Returns random integer in [min, max]. */
# define rand_range(min, max) ((rand() % (((max) + 1) - (min))) + (min))

# define UNUSED(f) ((void) f)

typedef enum dim {
  dim_x,
  dim_y,
  num_dims
} dim_t;

typedef int16_t pair_t[num_dims];

class character;

#define MAP_X              80
#define MAP_Y              21
#define MIN_TREES          10
#define MIN_BOULDERS       10
#define TREE_PROB          95
#define BOULDER_PROB       95
#define WORLD_SIZE         401

#define MIN_TRAINERS     7
#define ADD_TRAINER_PROB 60

#define MOUNTAIN_SYMBOL       '%'
#define BOULDER_SYMBOL        '0'
#define TREE_SYMBOL           '4'
#define FOREST_SYMBOL         '^'
#define GATE_SYMBOL           '#'
#define PATH_SYMBOL           '#'
#define POKEMART_SYMBOL       'M'
#define POKEMON_CENTER_SYMBOL 'C'
#define TALL_GRASS_SYMBOL     ':'
#define SHORT_GRASS_SYMBOL    '.'
#define WATER_SYMBOL          '~'
#define ERROR_SYMBOL          '&'

#define PC_SYMBOL       '@'
#define HIKER_SYMBOL    'h'
#define RIVAL_SYMBOL    'r'
#define EXPLORER_SYMBOL 'e'
#define SENTRY_SYMBOL   's'
#define PACER_SYMBOL    'p'
#define SWIMMER_SYMBOL  'm'
#define WANDERER_SYMBOL 'w'

#define mappair(pair) (m->mp[pair[dim_y]][pair[dim_x]])
#define mapxy(x, y) (m->mp[y][x])
#define heightpair(pair) (m->height[pair[dim_y]][pair[dim_x]])
#define heightxy(x, y) (m->height[y][x])

typedef enum __attribute__ ((__packed__)) movement_type {
  move_hiker,
  move_rival,
  move_pace,
  move_wander,
  move_sentry,
  move_explore,
  move_swim,
  move_pc,
  num_movement_types
} movement_type_t;

typedef enum __attribute__ ((__packed__)) character_type {
  char_pc,
  char_hiker,
  char_rival,
  char_swimmer,
  char_other,
  num_character_types
} character_type_t;

extern const char *char_type_name[num_character_types];

typedef enum __attribute__ ((__packed__)) terrain_type {
  ter_boulder,
  ter_tree,
  ter_path,
  ter_mart,
  ter_center,
  ter_grass,
  ter_clearing,
  ter_mountain,
  ter_forest,
  ter_water,
  ter_gate,
  num_terrain_types,
  ter_debug
} terrain_type_t;

class map {
  private:
    heap_t turn;
    int32_t num_trainers;
  public:
    int8_t n, s, e, w;
    terrain_type_t mp[MAP_Y][MAP_X];
    uint8_t height[MAP_Y][MAP_X];
    character *cmap[MAP_Y][MAP_X];
    map();
    heap_t *get_turn();
    void set_num_trainers(int n);
    int32_t increment_num_trainers();
    int32_t get_num_trainers();
};

/* 
  this needs to be a abstract, they can only be a pc or an npc, follow inheritance.cpp
  in class examples. s
*/
class character {
  private:
    int next_turn;
    int seq_num;
    char symbol;
    character_type_t ctype;
    movement_type_t mtype;
    bool defeated;
  public:
    bool npc;
    pair_t pos;
    pair_t dir;
    character();
    character(char s);  // set npc false
    character(character_type_t ct, movement_type_t mt); // will set npc true
    virtual ~character() { }
    void set_pos(pair_t new_pos);
    char get_symbol() const;
    void set_symbol(char sym);
    int get_next_turn() const;
    void set_next_turn(int next_turn);
    int get_seq_num() const;
    void set_seq_num(int seq_num);
    character_type_t get_ctype() const;
    movement_type_t get_mtype() const;
    void set_mtype(movement_type_t m);
    bool get_defeated() const;
    void set_defeated();      // change value of defeated to 1
    void set_dir(pair_t d);
};

typedef struct world {
  map *world[WORLD_SIZE][WORLD_SIZE];
  pair_t cur_idx;
  map *cur_map;
  /* Please distance maps in world, not map, since *
   * we only need one pair at any given time.      */
  int hiker_dist[MAP_Y][MAP_X];
  int rival_dist[MAP_Y][MAP_X];
  character pc;
  int quit;
  int add_trainer_prob;
  int char_seq_num;
} world_t;

/* Even unallocated, a WORLD_SIZE x WORLD_SIZE array of pointers is a very *
 * large thing to put on the stack.  To avoid that, world is a global.     */
extern world_t world;

extern pair_t all_dirs[8];

#define rand_dir(dir) {     \
  int _i = rand() & 0x7;    \
  dir[0] = all_dirs[_i][0]; \
  dir[1] = all_dirs[_i][1]; \
}

typedef struct path {
  heap_node_t *hn;
  uint8_t pos[2];
  uint8_t from[2];
  int32_t cost;
} path_t;

int new_map(int teleport);

#endif
