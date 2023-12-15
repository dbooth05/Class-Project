#ifndef POKEMON_H
#define POKEMON_H

#include <vector>
#include <iostream>
#include <cstdlib>
#include <vector>
#include <math.h>

// #include "poke327.h"
#include "pokedex.h"

class pokemon {
    public:
        int id;
        std::string name;
        int level;
        std::vector<pokemon_moves_db> moves;
        std::vector<pokemon_stats_db> stats;
        int hp;
        int attack;
        int defense;
        int speed;
        int s_attack;
        int s_defense;
        int iv_hp;
        int iv_attack;
        int iv_defense;
        int iv_speed;
        int iv_s_attack;
        int iv_s_defense;
        int gender;     //0 is male, 1 is female
        bool shiny;

        pokemon(int loc, int lev) { 
            pokemon_db cur = p_list.at(loc);
            id = cur.id;
            name = cur.identifier;
            level = lev;

            set_ivs();
            set_base_stats();
            set_stats();
            set_moves();
            
            gender = rand() % 2;
            if (rand() % 8192 == 0) { shiny = true; }
        }

        void level_up() {
            level++;
            hp = floor(((stats.at(0).base_stat + iv_hp) * 2 * level) / 100) + level + 10;
            attack = floor(((stats.at(1).base_stat + iv_attack) * 2 * level) / 100) + 5;
            defense = floor(((stats.at(2).base_stat + iv_defense) * 2 * level) / 100) + 5;
            s_attack = floor(((stats.at(3).base_stat + iv_s_attack) * 2 * level) / 100) + 5;
            s_defense = floor(((stats.at(4).base_stat + iv_s_defense) * 2 * level) / 100) + 5;
            speed = floor(((stats.at(5).base_stat + iv_speed) * 2 * level) / 100) + 5;
        }
    private:
        void set_ivs() {
            iv_hp = rand() % 15;
            iv_attack = rand() % 15;
            iv_defense = rand() % 15;
            iv_speed = rand() % 15;
            iv_s_attack = rand() % 15;
            iv_s_defense = rand() % 15;
        }
        void set_base_stats() {
            for (std::vector<pokemon_stats_db>::size_type i = 0; i < p_stats_list.size(); i++) {
                if (p_stats_list.at(i).pokemon_id == id) {
                    stats.push_back(p_stats_list.at(i));
                }
            }            
        }
        void set_stats() {
            hp = iv_hp + stats.at(0).base_stat;
            attack = iv_attack + stats.at(1).base_stat;
            defense = iv_defense + stats.at(2).base_stat;
            s_attack = iv_s_attack + stats.at(3).base_stat;
            s_defense = iv_s_defense + stats.at(4).base_stat;
            speed = iv_speed + stats.at(5).base_stat;
        }
        void set_moves();
};

pokemon *spawnPokemon(int dist);

#endif