#include <unistd.h>
#include <ncurses.h>
#include <cctype>
#include <cstdlib>
#include <climits>
#include <vector>

#include "io.h"
#include "character.h"
#include "poke327.h"
#include "pokemon.h"

void io_pokemon();
void io_display_bag();

typedef struct io_message {
  /* Will print " --more-- " at end of line when another message follows. *
   * Leave 10 extra spaces for that.                                      */
  char msg[71];
  struct io_message *next;
} io_message_t;

static io_message_t *io_head, *io_tail;

void io_init_terminal(void)
{
  initscr();
  raw();
  noecho();
  curs_set(0);
  keypad(stdscr, TRUE);
  start_color();
  resize_term(24, 80);
  init_pair(COLOR_RED, COLOR_RED, COLOR_BLACK);
  init_pair(COLOR_GREEN, COLOR_GREEN, COLOR_BLACK);
  init_pair(COLOR_YELLOW, COLOR_YELLOW, COLOR_BLACK);
  init_pair(COLOR_BLUE, COLOR_BLUE, COLOR_BLACK);
  init_pair(COLOR_MAGENTA, COLOR_MAGENTA, COLOR_BLACK);
  init_pair(COLOR_CYAN, COLOR_CYAN, COLOR_BLACK);
  init_pair(COLOR_WHITE, COLOR_WHITE, COLOR_BLACK);
}

void io_reset_terminal(void)
{
  endwin();

  while (io_head) {
    io_tail = io_head;
    io_head = io_head->next;
    free(io_tail);
  }
  io_tail = NULL;
}

void io_queue_message(const char *format, ...)
{
  io_message_t *tmp;
  va_list ap;

  if (!(tmp = (io_message_t *) malloc(sizeof (*tmp)))) {
    perror("malloc");
    exit(1);
  }

  tmp->next = NULL;

  va_start(ap, format);

  vsnprintf(tmp->msg, sizeof (tmp->msg), format, ap);

  va_end(ap);

  if (!io_head) {
    io_head = io_tail = tmp;
  } else {
    io_tail->next = tmp;
    io_tail = tmp;
  }
}

static void io_print_message_queue(uint32_t y, uint32_t x)
{
  while (io_head) {
    io_tail = io_head;
    attron(COLOR_PAIR(COLOR_CYAN));
    mvprintw(y, x, "%-80s", io_head->msg);
    attroff(COLOR_PAIR(COLOR_CYAN));
    io_head = io_head->next;
    if (io_head) {
      attron(COLOR_PAIR(COLOR_CYAN));
      mvprintw(y, x + 70, "%10s", " --more-- ");
      attroff(COLOR_PAIR(COLOR_CYAN));
      refresh();
      getch();
    }
    free(io_tail);
  }
  io_tail = NULL;
}

/**************************************************************************
 * Compares trainer distances from the PC according to the rival distance *
 * map.  This gives the approximate distance that the PC must travel to   *
 * get to the trainer (doesn't account for crossing buildings).  This is  *
 * not the distance from the NPC to the PC unless the NPC is a rival.     *
 *                                                                        *
 * Not a bug.                                                             *
 **************************************************************************/
static int compare_trainer_distance(const void *v1, const void *v2)
{
  const character *const *c1 = (const character * const *) v1;
  const character *const *c2 = (const character * const *) v2;

  return (world.rival_dist[(*c1)->pos[dim_y]][(*c1)->pos[dim_x]] -
          world.rival_dist[(*c2)->pos[dim_y]][(*c2)->pos[dim_x]]);
}

static character *io_nearest_visible_trainer()
{
  character **c, *n;
  uint32_t x, y, count;

  c = (character **) malloc(world.cur_map->num_trainers * sizeof (*c));

  /* Get a linear list of trainers */
  for (count = 0, y = 1; y < MAP_Y - 1; y++) {
    for (x = 1; x < MAP_X - 1; x++) {
      if (world.cur_map->cmap[y][x] && world.cur_map->cmap[y][x] !=
          &world.pc) {
        c[count++] = world.cur_map->cmap[y][x];
      }
    }
  }

  /* Sort it by distance from PC */
  qsort(c, count, sizeof (*c), compare_trainer_distance);

  n = c[0];

  free(c);

  return n;
}

void io_display()
{
  uint32_t y, x;
  character *c;

  clear();
  for (y = 0; y < MAP_Y; y++) {
    for (x = 0; x < MAP_X; x++) {
      if (world.cur_map->cmap[y][x]) {
        mvaddch(y + 1, x, world.cur_map->cmap[y][x]->symbol);
      } else {
        switch (world.cur_map->map[y][x]) {
        case ter_boulder:
          attron(COLOR_PAIR(COLOR_MAGENTA));
          mvaddch(y + 1, x, BOULDER_SYMBOL);
          attroff(COLOR_PAIR(COLOR_MAGENTA));
          break;
        case ter_mountain:
          attron(COLOR_PAIR(COLOR_MAGENTA));
          mvaddch(y + 1, x, MOUNTAIN_SYMBOL);
          attroff(COLOR_PAIR(COLOR_MAGENTA));
          break;
        case ter_tree:
          attron(COLOR_PAIR(COLOR_GREEN));
          mvaddch(y + 1, x, TREE_SYMBOL);
          attroff(COLOR_PAIR(COLOR_GREEN));
          break;
        case ter_forest:
          attron(COLOR_PAIR(COLOR_GREEN));
          mvaddch(y + 1, x, FOREST_SYMBOL);
          attroff(COLOR_PAIR(COLOR_GREEN));
          break;
        case ter_path:
          attron(COLOR_PAIR(COLOR_YELLOW));
          mvaddch(y + 1, x, PATH_SYMBOL);
          attroff(COLOR_PAIR(COLOR_YELLOW));
          break;
        case ter_gate:
          attron(COLOR_PAIR(COLOR_YELLOW));
          mvaddch(y + 1, x, GATE_SYMBOL);
          attroff(COLOR_PAIR(COLOR_YELLOW));
          break;
        case ter_bailey:
          attron(COLOR_PAIR(COLOR_YELLOW));
          mvaddch(y + 1, x, BAILEY_SYMBOL);
          attroff(COLOR_PAIR(COLOR_YELLOW));
          break;
        case ter_mart:
          attron(COLOR_PAIR(COLOR_BLUE));
          mvaddch(y + 1, x, POKEMART_SYMBOL);
          attroff(COLOR_PAIR(COLOR_BLUE));
          break;
        case ter_center:
          attron(COLOR_PAIR(COLOR_RED));
          mvaddch(y + 1, x, POKEMON_CENTER_SYMBOL);
          attroff(COLOR_PAIR(COLOR_RED));
          break;
        case ter_grass:
          attron(COLOR_PAIR(COLOR_GREEN));
          mvaddch(y + 1, x, TALL_GRASS_SYMBOL);
          attroff(COLOR_PAIR(COLOR_GREEN));
          break;
        case ter_clearing:
          attron(COLOR_PAIR(COLOR_GREEN));
          mvaddch(y + 1, x, SHORT_GRASS_SYMBOL);
          attroff(COLOR_PAIR(COLOR_GREEN));
          break;
        case ter_water:
          attron(COLOR_PAIR(COLOR_CYAN));
          mvaddch(y + 1, x, WATER_SYMBOL);
          attroff(COLOR_PAIR(COLOR_CYAN));
          break;
        default:
          attron(COLOR_PAIR(COLOR_CYAN));
          mvaddch(y + 1, x, ERROR_SYMBOL);
          attroff(COLOR_PAIR(COLOR_CYAN)); 
       }
      }
    }
  }

  mvprintw(23, 1, "PC position is (%2d,%2d) on map %d%cx%d%c.",
           world.pc.pos[dim_x],
           world.pc.pos[dim_y],
           abs(world.cur_idx[dim_x] - (WORLD_SIZE / 2)),
           world.cur_idx[dim_x] - (WORLD_SIZE / 2) >= 0 ? 'E' : 'W',
           abs(world.cur_idx[dim_y] - (WORLD_SIZE / 2)),
           world.cur_idx[dim_y] - (WORLD_SIZE / 2) <= 0 ? 'N' : 'S');
  mvprintw(22, 1, "%d known %s.", world.cur_map->num_trainers,
           world.cur_map->num_trainers > 1 ? "trainers" : "trainer");
  mvprintw(22, 30, "Nearest visible trainer: ");
  if ((c = io_nearest_visible_trainer())) {
    attron(COLOR_PAIR(COLOR_RED));
    mvprintw(22, 55, "%c at vector %d%cx%d%c.",
             c->symbol,
             abs(c->pos[dim_y] - world.pc.pos[dim_y]),
             ((c->pos[dim_y] - world.pc.pos[dim_y]) <= 0 ?
              'N' : 'S'),
             abs(c->pos[dim_x] - world.pc.pos[dim_x]),
             ((c->pos[dim_x] - world.pc.pos[dim_x]) <= 0 ?
              'W' : 'E'));
    attroff(COLOR_PAIR(COLOR_RED));
  } else {
    attron(COLOR_PAIR(COLOR_BLUE));
    mvprintw(22, 55, "NONE.");
    attroff(COLOR_PAIR(COLOR_BLUE));
  }

  io_print_message_queue(0, 0);

  refresh();
}

uint32_t io_teleport_pc(pair_t dest)
{
  /* Just for fun. And debugging.  Mostly debugging. */

  do {
    dest[dim_x] = rand_range(1, MAP_X - 2);
    dest[dim_y] = rand_range(1, MAP_Y - 2);
  } while (world.cur_map->cmap[dest[dim_y]][dest[dim_x]]                  ||
           move_cost[char_pc][world.cur_map->map[dest[dim_y]]
                                                [dest[dim_x]]] ==
             DIJKSTRA_PATH_MAX                                            ||
           world.rival_dist[dest[dim_y]][dest[dim_x]] < 0);

  return 0;
}

static void io_scroll_trainer_list(char (*s)[40], uint32_t count)
{
  uint32_t offset;
  uint32_t i;

  offset = 0;

  while (1) {
    for (i = 0; i < 13; i++) {
      mvprintw(i + 6, 19, " %-40s ", s[i + offset]);
    }
    switch (getch()) {
    case KEY_UP:
      if (offset) {
        offset--;
      }
      break;
    case KEY_DOWN:
      if (offset < (count - 13)) {
        offset++;
      }
      break;
    case 27:
      return;
    }

  }
}

static void io_list_trainers_display(npc **c, uint32_t count)
{
  uint32_t i;
  char (*s)[40]; /* pointer to array of 40 char */

  s = (char (*)[40]) malloc(count * sizeof (*s));

  mvprintw(3, 19, " %-40s ", "");
  /* Borrow the first element of our array for this string: */
  snprintf(s[0], 40, "You know of %d trainers:", count);
  mvprintw(4, 19, " %-40s ", *s);
  mvprintw(5, 19, " %-40s ", "");

  for (i = 0; i < count; i++) {
    snprintf(s[i], 40, "%16s %c: %2d %s by %2d %s",
             char_type_name[c[i]->ctype],
             c[i]->symbol,
             abs(c[i]->pos[dim_y] - world.pc.pos[dim_y]),
             ((c[i]->pos[dim_y] - world.pc.pos[dim_y]) <= 0 ?
              "North" : "South"),
             abs(c[i]->pos[dim_x] - world.pc.pos[dim_x]),
             ((c[i]->pos[dim_x] - world.pc.pos[dim_x]) <= 0 ?
              "West" : "East"));
    if (count <= 13) {
      /* Handle the non-scrolling case right here. *
       * Scrolling in another function.            */
      mvprintw(i + 6, 19, " %-40s ", s[i]);
    }
  }

  if (count <= 13) {
    mvprintw(count + 6, 19, " %-40s ", "");
    mvprintw(count + 7, 19, " %-40s ", "Hit escape to continue.");
    while (getch() != 27 /* escape */)
      ;
  } else {
    mvprintw(19, 19, " %-40s ", "");
    mvprintw(20, 19, " %-40s ",
             "Arrows to scroll, escape to continue.");
    io_scroll_trainer_list(s, count);
  }

  free(s);
}

static void io_list_trainers()
{
  npc **c;
  uint32_t x, y, count;

  c = (npc **) malloc(world.cur_map->num_trainers * sizeof (*c));

  /* Get a linear list of trainers */
  for (count = 0, y = 1; y < MAP_Y - 1; y++) {
    for (x = 1; x < MAP_X - 1; x++) {
      if (world.cur_map->cmap[y][x] && world.cur_map->cmap[y][x] !=
          &world.pc) {
        c[count++] = dynamic_cast<npc *> (world.cur_map->cmap[y][x]);
      }
    }
  }

  /* Sort it by distance from PC */
  qsort(c, count, sizeof (*c), compare_trainer_distance);

  /* Display it */
  io_list_trainers_display(c, count);
  free(c);

  /* And redraw the map */
  io_display();
}

void io_pokemart()
{
  mvprintw(0, 0, "Welcome to the Pokemart.  Could I interest you in some Pokeballs?");
  refresh();
  getch();

  world.pc.replenish();

}

void io_pokemon_center()
{
  mvprintw(0, 0, "Welcome to the Pokemon Center.  How can Nurse Joy assist you?");
  refresh();
  getch();

  for (int i = 0; i < (int) world.pc.poke.size(); i++) {
    world.pc.poke.at(i)->heal_full();
  }
}

int damage_amount(pokemon *pa, pokemon *pd, moves_db *m) {

  int crit_ran = rand() % 256;
  int critical = (crit_ran < floor(pa->speed/2) ? 1.5 : 1);

  // mvprintw(11, 5, "%s", (critical == 1.5 ? "CRIT" : "regular hit"));

  double random = (rand() % 16 + 85);
  int stab = (m->type_id == pd->type_id ? 1.5 : 1);
  // int type = 1; // can leave this for reduced complexity

  double damage_frac = (((((2*pa->level)/5)+2)*m->power*(pa->attack/pd->defense))/50);
  int damage = ceil(((damage_frac + 2) * critical * random * stab * 1)/20);
  // mvprintw(12, 5, "damage_frac:%.2f, crit:%d, rand:%.2f, stab:%d", damage_frac, critical, random, stab);

  return damage;
}

void attack(pokemon *pa, pokemon *pd, moves_db *m) {        
  
  int damage;

  if (pa->dead) {
    return;
  }

  if (rand() % 100 < m->accuracy) {
    damage = damage_amount(pa, pd, m);
    pd->hp = (pd->hp - damage > 0 ? pd->hp - damage : 0);
    if (pd->hp == 0) { pd->dead = true; }
    mvprintw(10, 3, "%s attacked and dealt %d damage", pa->name.c_str(), damage);
  } else {
    mvprintw(10, 3, "%s attacked and missed", pa->name.c_str());
  }

  getch();

  for (int i = 0; i < 15; i++) {
    mvprintw(10, 3+i, " ");
  }
}

void io_battle_switch() {
  
  int ch;
  clear();
  box(stdscr, 0, 0);

  mvprintw(3, 5, "Select number of pokemon you want to use");
  
  for (int i = 0; i < (int) world.pc.poke.size(); i++) {
    mvprintw(5+i, 5, "%d. %s hp:%d/%d", i+1, world.pc.poke.at(i)->name.c_str(), world.pc.poke.at(i)->hp, world.pc.poke.at(i)->hp_max);
  }

  while (1) {
    ch = getch();

    switch (ch) {
      case 27:
        return;
        break;
      default:
        if (ch-49 < (int) world.pc.poke.size()) {
          world.pc.cur_pokemon = world.pc.poke.at(ch-49);
          return;
        }
        break;
    }
  }

}

bool check_pc_poke() {

  for (int i = 0; i < (int) world.pc.poke.size(); i++) {
    if (!world.pc.poke.at(i)->dead) { return false; }
  }

  return true;
}

void io_battle(character *aggressor, character *defender)
{
  npc *n = (npc *) ((aggressor == &world.pc) ? defender : aggressor);

  int ch;
  int selected_move;
  int def = 0;

  while (1) {

    selected_move = -1;

    clear();
    box(stdscr, 0, 0);
    mvprintw(5, 40, "Trainer %c", n->symbol);
    mvprintw(6, 40, "Pokemon %s hp:%d/%d", n->cur_pokemon->name.c_str(), n->cur_pokemon->hp, n->cur_pokemon->hp_max);
    mvprintw(18, 40, "Pokemon %s hp:%d/%d", world.pc.cur_pokemon->name.c_str(), world.pc.cur_pokemon->hp, world.pc.cur_pokemon->hp_max);

    if (n->cur_pokemon->dead && (int) n->poke.size() > 1) {
      for (int i = 0; i < (int) n->poke.size(); i++) {
        if (!n->poke.at(i)->dead) { n->cur_pokemon = n->poke.at(i); break; }
      }
    }

    if (n->cur_pokemon->dead) {
      def = 1;
    }

    if (world.pc.cur_pokemon->dead) {
      mvprintw(2, 5, "Your pokemon %s was knocked out, switch pokemon (4)", world.pc.cur_pokemon->name.c_str());
    } 

    ch = getch();

    switch(ch) {
      case '1':
        for (int i = 0; i < (int) world.pc.cur_pokemon->moves.size(); i++) {
          moves_db move = moves_list.at(world.pc.cur_pokemon->moves.at(i).move_id);
          mvprintw(16+i, 40, "%d. %s power:%d accuracy:%d", i+1, move.identifier.c_str(), (move.power != INT_MAX ? move.power : 0), (move.accuracy != INT_MAX ? move.accuracy : 0));
        }

        while (1) {
          selected_move = getch();

          switch(selected_move) {
            case 27:
              selected_move = -1;
              goto exit_move_select;
              break;
            default:
              if (selected_move-49 > -1 && selected_move-49 < (int) world.pc.cur_pokemon->moves.size()) {
                goto exit_move_select;
              }
              break;
          }
        }

        exit_move_select: // exit previous loop
        break;
      case '2':
        mvprintw(10, 5, "entered bag");
        io_display_bag();
        clear();
        box(stdscr, 0, 0);
        mvprintw(5, 50, "Trainer %c", n->symbol);
        mvprintw(6, 50, "Pokemon %s hp:%d/%d", n->cur_pokemon->name.c_str(), n->cur_pokemon->hp, n->cur_pokemon->hp_max);
        mvprintw(18, 40, "Pokemon %s hp:%d/%d", world.pc.cur_pokemon->name.c_str(), world.pc.cur_pokemon->hp, world.pc.cur_pokemon->hp_max);
        break;
      case '3':
        // implement prevent leaving battle last, for sakes of getting other things first
        if ((rand() % 100 < 50 && n->poke.at(0)->dead) || check_pc_poke()) {
          mvprintw(10, 5, "how are you going to be the best if you cant beat trainers");
          getch();
          goto exit_loop; 
        } else {
          mvprintw(10, 5, "failed to leave the battle");
        }
        break;
      case '4':
        mvprintw(10, 5, "switch pokemon");
        io_battle_switch();
        clear();
        box(stdscr, 0, 0);
        mvprintw(5, 50, "Trainer %c", n->symbol);
        mvprintw(6, 50, "Pokemon %s hp:%d/%d", n->cur_pokemon->name.c_str(), n->cur_pokemon->hp, n->cur_pokemon->hp_max);
        mvprintw(18, 40, "Pokemon %s hp:%d/%d", world.pc.cur_pokemon->name.c_str(), world.pc.cur_pokemon->hp, world.pc.cur_pokemon->hp_max);
        break;
      default:
        ch = -1;
        break;
    }

    if (ch != -1) {
      int trainer_move = (n->cur_pokemon->moves.size() > 1 ? rand() % n->cur_pokemon->moves.size() : 0);

      if (selected_move != -1) {
        int pc_priority = moves_list.at(world.pc.cur_pokemon->moves.at(selected_move-49).move_id).priority;
        int npc_priority = moves_list.at(n->cur_pokemon->moves.at(trainer_move).move_id).priority;

        if (pc_priority > npc_priority) { // pc goes first
          attack(world.pc.cur_pokemon, n->cur_pokemon, &moves_list.at(world.pc.cur_pokemon->moves.at(selected_move-49).move_id));
          attack(n->cur_pokemon, world.pc.cur_pokemon, &moves_list.at(n->cur_pokemon->moves.at(trainer_move).move_id));  
        } else if (pc_priority < npc_priority) { // pc goes second
          attack(n->cur_pokemon, world.pc.cur_pokemon, &moves_list.at(n->cur_pokemon->moves.at(trainer_move).move_id));
          attack(world.pc.cur_pokemon, n->cur_pokemon, &moves_list.at(world.pc.cur_pokemon->moves.at(selected_move-49).move_id));
        } else { // same priority, compare with speed
          if (world.pc.cur_pokemon->speed >= n->cur_pokemon->speed) { // pc goes first 
            attack(world.pc.cur_pokemon, n->cur_pokemon, &moves_list.at(world.pc.cur_pokemon->moves.at(selected_move-49).move_id));
            attack(n->cur_pokemon, world.pc.cur_pokemon, &moves_list.at(n->cur_pokemon->moves.at(trainer_move).move_id));
          } else { // pc goes second
            attack(n->cur_pokemon, world.pc.cur_pokemon, &moves_list.at(n->cur_pokemon->moves.at(trainer_move).move_id));
            attack(world.pc.cur_pokemon, n->cur_pokemon, &moves_list.at(world.pc.cur_pokemon->moves.at(selected_move-49).move_id));
          }
        }
      } else {
        attack(n->cur_pokemon, world.pc.cur_pokemon, &moves_list.at(n->cur_pokemon->moves.at(trainer_move).move_id));
      }
    }

  } 

  exit_loop: // label to leave loop

  /* DO NOT FORGET TO SET THESE VALUES */
  if (def) {
    n->defeated = 1;
    if (n->ctype == char_hiker || n->ctype == char_rival) {
      n->mtype = move_wander;
    }
  }

  io_display();
}

void io_capture_pokemon(pokemon *p) {
        // mvprintw(10, 5, "you captured %s", p->name.c_str());
        // world.pc.poke.push_back(p);
        // world.pc.item_bag.pokeball_cnt--;
        // getch();
  // could add a chance structure for capturing pokemon, not guranteed on first pokeball...
  clear();
  box(stdscr, 0, 0);
  int ch;

  mvprintw(3, 5, "you captured %s", p->name.c_str());
  if ((int) world.pc.poke.size() >= 6) {
    mvprintw(4, 5, "You have max amount of pokemon, esc to leave or");
    mvprintw(5, 5, "enter number of pokemon you would like to replace.");

    for (int i = 0; i < (int) world.pc.poke.size(); i++) {
      mvprintw(7+i, 5, "%d. %s hp:%d/%d", i+1, world.pc.poke.at(i)->name.c_str(), world.pc.poke.at(i)->hp, world.pc.poke.at(i)->hp_max);
    }

    while (1) {
      ch = getch();

      switch(ch) {
        case 27:
          return;
          break;
        default:
          if (ch-49 > -1 && ch-49 < (int) world.pc.poke.size()) {
            world.pc.poke.erase(world.pc.poke.begin() + ch-49);
            world.pc.poke.push_back(p);
            world.pc.item_bag.pokeball_cnt--;
            mvprintw(15, 3, "You captured %s", p->name.c_str());
            getch();
            return;
          }
          break;
      }
    }
  } else {
    world.pc.poke.push_back(p);
  }
}

void io_battle_pokemon(pokemon *p) {

  int ch;
  int selected_move;

  while (1) {

    selected_move = -1;
    
    clear();
    box(stdscr, 0, 0);
    mvprintw(5, 40, "Wild Pokemon %s hp:%d/%d", p->name.c_str(), p->hp, p->hp_max);
    mvprintw(18, 45, "Pokemon %s hp:%d/%d", world.pc.cur_pokemon->name.c_str(), world.pc.cur_pokemon->hp, world.pc.cur_pokemon->hp_max);

    if (p->dead) {
      mvprintw(2, 5, "Enter 5 to capture with pokeball, %d pokeballs remaining", world.pc.item_bag.pokeball_cnt);
      ch = getch();
    } else if (world.pc.cur_pokemon->dead) {
      mvprintw(2, 5, "Your pokemon %s was knocked out, switch pokemon (4)", world.pc.cur_pokemon->name.c_str());
      ch = getch();
    } else {
      ch = getch();
    }

    switch(ch) {
      case '1':
        for (int i = 0; i < (int) world.pc.cur_pokemon->moves.size(); i++) {
          moves_db move = moves_list.at(world.pc.cur_pokemon->moves.at(i).move_id);
          mvprintw(16+i, 40, "%d. %s power:%d accuracy:%d", i+1, move.identifier.c_str(), (move.power != INT_MAX ? move.power : 0), (move.accuracy != INT_MAX ? move.accuracy : 0));
        }

        while (1) {
          selected_move = getch();

          switch(selected_move) {
            case 27:
              selected_move = -1;
              goto exit_move_select;
              break;
            default:
              if (selected_move-49 > -1 && selected_move-49 < (int) world.pc.cur_pokemon->moves.size()) {
                goto exit_move_select;
              }
              break;
          }
        }

        exit_move_select: // exit previous loop
        break;
      case '2':
        mvprintw(10, 5, "entered bag");
        io_display_bag();
        clear();
        box(stdscr, 0, 0);
        mvprintw(5, 40, "Wild Pokemon %s hp:%d/%d", p->name.c_str(), p->hp, p->hp_max);
        mvprintw(18, 45, "Pokemon %s hp:%d/%d", world.pc.cur_pokemon->name.c_str(), world.pc.cur_pokemon->hp, world.pc.cur_pokemon->hp_max);
        break;
      case '3':
        mvprintw(10, 5, "how are you going to be the best if you cant get pokemon");
        delete(p);
        getch();
        goto exit_loop;
        break;
      case '4':
        mvprintw(10, 5, "switch pokemon");
        io_battle_switch();
        clear();
        box(stdscr, 0, 0);
        mvprintw(5, 40, "Wild Pokemon %s hp:%d/%d", p->name.c_str(), p->hp, p->hp_max);
        mvprintw(18, 45, "Pokemon %s hp:%d/%d", world.pc.cur_pokemon->name.c_str(), world.pc.cur_pokemon->hp, world.pc.cur_pokemon->hp_max);
        break;
      case '5':
        if (!p->dead) { break; } // make sure pokemon dead to capture
        io_capture_pokemon(p);
        goto exit_loop;
      default:
        ch = -1;
        break;
    }

    if (ch != -1) {
      int w_poke_move = (p->moves.size() > 1 ? rand() % p->moves.size() : 0);

      if (selected_move != -1) { // pc goes first
        int pc_priority = moves_list.at(world.pc.cur_pokemon->moves.at(selected_move-49).move_id).priority;
        int npc_priority = moves_list.at(p->moves.at(w_poke_move).move_id).priority;
      
        if (pc_priority > npc_priority) {
          attack(world.pc.cur_pokemon, p, &moves_list.at(world.pc.cur_pokemon->moves.at(selected_move-49).move_id));
          attack(p, world.pc.cur_pokemon, &moves_list.at(p->moves.at(w_poke_move).move_id));
        } else if (pc_priority < npc_priority) { // pc goes second
          attack(p, world.pc.cur_pokemon, &moves_list.at(p->moves.at(w_poke_move).move_id));
          attack(world.pc.cur_pokemon, p, &moves_list.at(world.pc.cur_pokemon->moves.at(selected_move-49).move_id));
        } else { // same priority, compare with speed
          if (world.pc.cur_pokemon->speed >= p->speed) { // pc goes first
            attack(world.pc.cur_pokemon, p, &moves_list.at(world.pc.cur_pokemon->moves.at(selected_move-49).move_id));
            attack(p, world.pc.cur_pokemon, &moves_list.at(p->moves.at(w_poke_move).move_id));    
          } else { // pc goes second
            attack(world.pc.cur_pokemon, p, &moves_list.at(world.pc.cur_pokemon->moves.at(selected_move-49).move_id));
            attack(p, world.pc.cur_pokemon, &moves_list.at(p->moves.at(w_poke_move).move_id));
          }
        }
      } else { // pc made other move, only npc makes move
        attack(p, world.pc.cur_pokemon, &moves_list.at(p->moves.at(w_poke_move).move_id));
      }
    }
    
  } 

  exit_loop: // label to leave loop
    io_display();

}

pokemon *io_choose_pokemon() {

  int ch = -1;

  clear();
  box(stdscr, 0, 0);
  mvprintw(3, 5, "Time to choose your starting pokemon! Enter number choice");

  //choosing pokemon to display
  pokemon *op1 = spawnPokemon(0);
  mvprintw(5, 5, "1. %s", op1->name.c_str());
  pokemon *op2 = spawnPokemon(0);
  mvprintw(7, 5, "2. %s", op2->name.c_str());
  pokemon *op3 = spawnPokemon(0);
  mvprintw(9, 5, "3. %s", op3->name.c_str());

  refresh();

  ch = getch();
  while (ch < 49 || ch > 51) {
    mvprintw(2, 5, "Enter a number 1, 2, or 3!");
    ch = getch();
  }

  if (ch == 49) {
    mvprintw(13, 5, "You selected 1. %s", op1->name.c_str());
    getch();
    world.pc.cur_pokemon = op1;
    return op1;
  } else if (ch == 50) {
    mvprintw(13, 5, "You selected 2. %s", op2->name.c_str());
    getch();
    world.pc.cur_pokemon = op2;
    return op2;
  } else {
    mvprintw(13, 5, "You selected 3. %s", op3->name.c_str());
    getch();
    world.pc.cur_pokemon = op3;
    return op3;
  }

}

void io_display_pokemon_info(int ch) {

    clear();
    box(stdscr, 0, 0);

    pokemon *cur = world.pc.poke.at(ch);

    mvprintw(3, 5, "Info about %s, enter esc to leave", cur->name.c_str());
    mvprintw(5, 5, "hp:%d\t\tattack:%d\t\tdefense:%d", cur->hp, cur->attack, cur->defense);
    mvprintw(6, 5, "special attack:%d\tspecial defense:%d\tspeed:%d", cur->s_attack, cur->s_defense, cur->speed);
    mvprintw(7, 5, "gender:%s\tlevel:%d", (cur->gender == 0 ? "male" : "female"), cur->level);

    for (int i = 0; i < (int) cur->moves.size(); i++) {
      mvprintw(9+i, 5, "%d. %s, power: %d, accuracy: %d, priority: %d", i+1, 
        moves_list.at(cur->moves.at(i).move_id).identifier.c_str(), 
        (moves_list.at(cur->moves.at(i).move_id).power == INT_MAX ? 0 : moves_list.at(cur->moves.at(i).move_id).power), 
        (moves_list.at(cur->moves.at(i).move_id).accuracy == INT_MAX ? 0 : moves_list.at(cur->moves.at(i).move_id).accuracy),
        (moves_list.at(cur->moves.at(i).move_id).priority == INT_MAX ? 0 : moves_list.at(cur->moves.at(i).move_id).priority));
    }

    int c = getch();
    while (c != 27) {
      c = getch();
    }
    io_pokemon();
}

void io_pokemon() {

  std::vector<pokemon*>::size_type i;

  clear();
  box(stdscr, 0, 0);

  mvprintw(3, 5, "List of pokemon, you have %d pokemon", world.pc.poke.size());
  mvprintw(4, 5, "Enter number to view more info about pokemon");
  mvprintw(5, 5, "Press esc to leave menu");

  for (i = 0; i < world.pc.poke.size(); i++) {
    pokemon *cur = world.pc.poke.at(i);
    mvprintw(7 + i, 5, "%d. %s hp:%d/%d", i+1, cur->name.c_str(), cur->hp, cur->hp_max);
  }

  while (1) {
    int ch = getch();
    switch(ch) {
      case 27:
        goto exit_loop;
        break;
      default:
        if (ch > 0 && ch - 49 < (int) world.pc.poke.size()) {
          io_display_pokemon_info(ch-49);
        }
        break;
    }
  }

  exit_loop: // label to leave loop
    io_display();
}

void io_display_revive() {
  
  clear();
  box(stdscr, 0, 0);

  refresh();

  mvprintw(3, 5, "Enter number of pokemon to revive or enter esc to leave");

  int ch;
  while(1) {
    mvprintw(4, 5, "You have %d revives", world.pc.item_bag.revive_cnt);
    for (int i = 0; i < (int) world.pc.poke.size(); i++) {
      mvprintw(6+i, 5, "%d. %s  status:%s", i+1, world.pc.poke.at(i)->name.c_str(), (world.pc.poke.at(i)->dead ? "dead" : "alive"));
    }
    
    ch = getch();

    switch(ch) {
      case 27:
        goto exit;
        break;
      default:
        // do stuff here
        if (world.pc.item_bag.revive_cnt > 0) {
          if (world.pc.poke.at(ch-49)->revive()) {
            world.pc.item_bag.revive_cnt--;
            world.pc.item_bag.size--;
          }
        }
        break;
    }
  }

  exit: // exit label for loop
    // io_display_bag();
    return;
  
}

void io_display_heal() {
  
  clear();
  box(stdscr, 0, 0);

  mvprintw(3, 5, "Enter number of pokemon to heal or enter esc to leave");

  int ch;

  while (1) {
    mvprintw(4, 5, "You have %d healing potions", world.pc.item_bag.heal_pot_cnt);
    int i;
    for (i = 0; i < (int) world.pc.poke.size(); i++) {
      mvprintw(6+i, 5, "%d. %s hp:%d/%d", i+1, world.pc.poke.at(i)->name.c_str(), world.pc.poke.at(i)->hp, world.pc.poke.at(i)->hp_max);
    }
    
    ch = getch();

    switch(ch) {
      case 27:
        goto exit;
        break;
      default:
        if (world.pc.item_bag.heal_pot_cnt > 0) {
          if (ch - 49 >= 0 && ch - 49 < (int) world.pc.poke.size()) {
            if (world.pc.poke.at(ch-49)->hp < world.pc.poke.at(ch-49)->hp_max) {
              world.pc.poke.at(ch-49)->hp = (world.pc.poke.at(ch-49)->hp + 20 <= world.pc.poke.at(ch-49)->hp_max ? world.pc.poke.at(ch-49)->hp+20 : world.pc.poke.at(ch-49)->hp_max);
              world.pc.item_bag.heal_pot_cnt--;
              world.pc.item_bag.size--;
            }
          }
        }
        break;
    }
    ch = getch();
    mvprintw(2, 5, "                                                             ");
  }

  exit: // label to exit loop
    return;

}

void io_display_bag() {

  clear();
  box(stdscr, 0, 0);

  mvprintw(3, 5, "Item bag, you have %d items", world.pc.item_bag.size);
  mvprintw(4, 5, "Press esc to leave menu or enter 1-3 to use item");

  mvprintw(6, 5, "1. Revives: %d", world.pc.item_bag.revive_cnt);
  mvprintw(7, 5, "2. Healing potions: %d", world.pc.item_bag.heal_pot_cnt);
  mvprintw(8, 5, "3. Pokeballs: %d", world.pc.item_bag.pokeball_cnt);

  int ch;
  ch = getch();
  while (1) {

    switch(ch) {
      case '1':
        io_display_revive();
        break;
      case '2':
        io_display_heal();
        break;
      case '3':
        mvprintw(10, 5, "You can't use pokeballs here, assault pokemon with them!");
        break;
      case 27:
        return;
        break;
      default:
        break;
    }

    ch = getch();
  }

}

uint32_t move_pc_dir(uint32_t input, pair_t dest)
{
  dest[dim_y] = world.pc.pos[dim_y];
  dest[dim_x] = world.pc.pos[dim_x];

  switch (input) {
  case 1:
  case 2:
  case 3:
    dest[dim_y]++;
    break;
  case 4:
  case 5:
  case 6:
    break;
  case 7:
  case 8:
  case 9:
    dest[dim_y]--;
    break;
  }
  switch (input) {
  case 1:
  case 4:
  case 7:
    dest[dim_x]--;
    break;
  case 2:
  case 5:
  case 8:
    break;
  case 3:
  case 6:
  case 9:
    dest[dim_x]++;
    break;
  case '>':
    if (world.cur_map->map[world.pc.pos[dim_y]][world.pc.pos[dim_x]] ==
        ter_mart) {
      io_pokemart();
    }
    if (world.cur_map->map[world.pc.pos[dim_y]][world.pc.pos[dim_x]] ==
        ter_center) {
      io_pokemon_center();
    }
    break;
  }

  if (world.cur_map->cmap[dest[dim_y]][dest[dim_x]]) {
    if (dynamic_cast<npc *> (world.cur_map->cmap[dest[dim_y]][dest[dim_x]]) &&
        ((npc *) world.cur_map->cmap[dest[dim_y]][dest[dim_x]])->defeated) {
      // Some kind of greeting here would be nice
      return 1;
    } else if ((dynamic_cast<npc *>
                (world.cur_map->cmap[dest[dim_y]][dest[dim_x]]))) {
      io_battle(&world.pc, world.cur_map->cmap[dest[dim_y]][dest[dim_x]]);
      // Not actually moving, so set dest back to PC position
      dest[dim_x] = world.pc.pos[dim_x];
      dest[dim_y] = world.pc.pos[dim_y];
    }
  }
  
  if (move_cost[char_pc][world.cur_map->map[dest[dim_y]][dest[dim_x]]] ==
      DIJKSTRA_PATH_MAX) {
    return 1;
  }

  if (world.cur_map->map[dest[dim_y]][dest[dim_x]] == ter_gate &&
      dest[dim_y] != world.pc.pos[dim_y]                       &&
      dest[dim_x] != world.pc.pos[dim_x]) {
    return 1;
  }

  if (world.cur_map->map[dest[dim_y]][dest[dim_x]] == ter_grass &&
      world.cur_map->map[world.pc.pos[dim_y]][world.pc.pos[dim_x]] == ter_grass) {
    
    if (rand() % 10 >= 9) {
      int man_dist = (world.cur_idx[dim_y] - 200) + (world.cur_idx[dim_x] - 200);
      io_battle_pokemon(spawnPokemon(man_dist));
      // world.pc.poke.push_back(spawnPokemon(man_dist));
      // mvprintw(0, 0, "You encountered a wild %s and captured it.", world.pc.poke.at(world.pc.poke.size()-1)->name.c_str());
      getch();
    }
  }

  return 0;
}

void io_teleport_world(pair_t dest)
{
  /* mvscanw documentation is unclear about return values.  I believe *
   * that the return value works the same way as scanf, but instead   *
   * of counting on that, we'll initialize x and y to out of bounds   *
   * values and accept their updates only if in range.                */
  int x = INT_MAX, y = INT_MAX;
  
  world.cur_map->cmap[world.pc.pos[dim_y]][world.pc.pos[dim_x]] = NULL;

  echo();
  curs_set(1);
  do {
    mvprintw(0, 0, "Enter x [-200, 200]:           ");
    refresh();
    mvscanw(0, 21, "%d", &x);
  } while (x < -200 || x > 200);
  do {
    mvprintw(0, 0, "Enter y [-200, 200]:          ");
    refresh();
    mvscanw(0, 21, "%d", &y);
  } while (y < -200 || y > 200);

  refresh();
  noecho();
  curs_set(0);

  x += 200;
  y += 200;

  world.cur_idx[dim_x] = x;
  world.cur_idx[dim_y] = y;

  new_map(1);
  io_teleport_pc(dest);
}

void io_handle_input(pair_t dest)
{
  uint32_t turn_not_consumed;
  int key;

  do {
    switch (key = getch()) {
    case '7':
    case 'y':
    case KEY_HOME:
      turn_not_consumed = move_pc_dir(7, dest);
      break;
    case '8':
    case 'k':
    case KEY_UP:
      turn_not_consumed = move_pc_dir(8, dest);
      break;
    case '9':
    case 'u':
    case KEY_PPAGE:
      turn_not_consumed = move_pc_dir(9, dest);
      break;
    case '6':
    case 'l':
    case KEY_RIGHT:
      turn_not_consumed = move_pc_dir(6, dest);
      break;
    case '3':
    case 'n':
    case KEY_NPAGE:
      turn_not_consumed = move_pc_dir(3, dest);
      break;
    case '2':
    case 'j':
    case KEY_DOWN:
      turn_not_consumed = move_pc_dir(2, dest);
      break;
    case '1':
    case 'b':
    case KEY_END:
      turn_not_consumed = move_pc_dir(1, dest);
      break;
    case '4':
    case 'h':
    case KEY_LEFT:
      turn_not_consumed = move_pc_dir(4, dest);
      break;
    case '5':
    case ' ':
    case '.':
    case KEY_B2:
      dest[dim_y] = world.pc.pos[dim_y];
      dest[dim_x] = world.pc.pos[dim_x];
      turn_not_consumed = 0;
      break;
    case '>':
      turn_not_consumed = move_pc_dir('>', dest);
      break;
    case 'Q':
      dest[dim_y] = world.pc.pos[dim_y];
      dest[dim_x] = world.pc.pos[dim_x];
      world.quit = 1;
      turn_not_consumed = 0;
      break;
      break;
    case 't':
      io_list_trainers();
      turn_not_consumed = 1;
      break;
    case 'g':
      io_pokemon();
      turn_not_consumed = 1;
      break;
    case 'B':
      io_display_bag();
      io_display();
      break;
    case 'p':
      /* Teleport the PC to a random place in the map.              */
      io_teleport_pc(dest);
      turn_not_consumed = 0;
      break;
    case 'f':
      /* Fly to any map in the world.                                */
      io_teleport_world(dest);
      turn_not_consumed = 0;
      break;    
    case 'q':
      /* Demonstrate use of the message queue.  You can use this for *
       * printf()-style debugging (though gdb is probably a better   *
       * option.  Not that it matters, but using this command will   *
       * waste a turn.  Set turn_not_consumed to 1 and you should be *
       * able to figure out why I did it that way.                   */
      io_queue_message("This is the first message.");
      io_queue_message("Since there are multiple messages, "
                       "you will see \"more\" prompts.");
      io_queue_message("You can use any key to advance through messages.");
      io_queue_message("Normal gameplay will not resume until the queue "
                       "is empty.");
      io_queue_message("Long lines will be truncated, not wrapped.");
      io_queue_message("io_queue_message() is variadic and handles "
                       "all printf() conversion specifiers.");
      io_queue_message("Did you see %s?", "what I did there");
      io_queue_message("When the last message is displayed, there will "
                       "be no \"more\" prompt.");
      io_queue_message("Have fun!  And happy printing!");
      io_queue_message("Oh!  And use 'Q' to quit!");

      dest[dim_y] = world.pc.pos[dim_y];
      dest[dim_x] = world.pc.pos[dim_x];
      turn_not_consumed = 0;
      break;
    default:
      /* Also not in the spec.  It's not always easy to figure out what *
       * key code corresponds with a given keystroke.  Print out any    *
       * unhandled key here.  Not only does it give a visual error      *
       * indicator, but it also gives an integer value that can be used *
       * for that key in this (or other) switch statements.  Printed in *
       * octal, with the leading zero, because ncurses.h lists codes in *
       * octal, thus allowing us to do reverse lookups.  If a key has a *
       * name defined in the header, you can use the name here, else    *
       * you can directly use the octal value.                          */
      mvprintw(0, 0, "Unbound key: %#o ", key);
      turn_not_consumed = 1;
    }
    refresh();
  } while (turn_not_consumed);
}
