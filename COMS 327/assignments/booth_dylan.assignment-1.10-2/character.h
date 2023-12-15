#ifndef CHARACTER_H
# define CHARACTER_H

# include <cstdint>
# include <vector>

# include "pair.h"
# include "pokemon.h"

#define DIJKSTRA_PATH_MAX (INT_MAX / 2)

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

class bag {
    public:
        int heal_pot_cnt;
        int super_heal_cnt;
        int hyper_heal_cnt;
        int max_heal_cnt;

        int revive_cnt;
        int max_revive_cnt;

        int pokeball_cnt;
        int great_ball_cnt;
        int master_ball_cnt;
        
        int size;
        int max_size;

        void count_size() {
          size = heal_pot_cnt + super_heal_cnt + hyper_heal_cnt + max_heal_cnt + revive_cnt 
                  + max_revive_cnt + pokeball_cnt + great_ball_cnt + master_ball_cnt;
        }
};

class badge {
  public:
    std::string b_name;
    std::string t_name;
    int difficulty; // man distance
    std::vector<std::string> g_p_names;
};

class character {
 public:
  virtual ~character() {}
  pair_t pos;
  char symbol;
  int next_turn;
  int seq_num;
  std::vector<pokemon*> poke;
  std::vector<badge*> badges;
  pokemon *cur_pokemon;
  bag item_bag;
  void replenish();
};

class npc : public character {
 public:
  character_type_t ctype;
  movement_type_t mtype;
  int defeated;
  pair_t dir;
};

class pc : public character {
  public:
    int pokecoins;
};

/* character is defined in poke327.h to allow an instance of character
 * in world without including character.h in poke327.h                 */

int32_t cmp_char_turns(const void *key, const void *with);
void delete_character(void *v);

extern void (*move_func[num_movement_types])(character *, pair_t);

int pc_move(char);
bool is_pc(character *c);

#endif
