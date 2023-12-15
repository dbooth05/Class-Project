#ifndef POKEDEX_H
#define POKEDEX_H

#include <cstdio>
#include <iostream>
#include <vector>
#include <limits.h>

#define location_1 "/share/cs327/pokedex/pokedex/data/csv/"
#define location_2 "/.poke327/pokedex/data/csv/"

void load_csvs();
void load_stats();

class pokemon_db {
    public:
        int id;
        std::string identifier;
        int species_id;
        int height;
        int weight;
        int base_experience;
        int order;
        int is_default;
        std::ostream &print(std::ostream &o) const {
            o << (id == INT_MAX ? "" : std::to_string(id))  << ", " << identifier << ", " << (species_id == INT_MAX ? "" : std::to_string(species_id)) 
                << ", " << (height == INT_MAX ? "" : std::to_string(height)) << ", " << (weight == INT_MAX ? "" : std::to_string(weight)) << ", " << 
                (base_experience == INT_MAX ? "" : std::to_string(base_experience)) << ", " << (order == INT_MAX ? "" : std::to_string(order)) 
                << ", " << (is_default == INT_MAX ? "" : std::to_string(is_default)) << std::endl;

            return o;
        }
};

class moves_db {
    public:
        int id;
        std::string identifier;
        int generation_id;
        int type_id;
        int power;
        int pp;
        int accuracy;
        int priority;
        int target_id;
        int damage_class_id;
        int effect_id;
        int effect_chance;
        int contest_type_id;
        int contest_effect_id;
        int super_contest_effect_id;
        std::ostream &print(std::ostream &o) const {
            o << (id == INT_MAX ? "" : std::to_string(id)) << ", " << identifier << ", " << (generation_id == INT_MAX ? "" : std::to_string(generation_id)) 
                << ", " << (type_id == INT_MAX ? "" : std::to_string(type_id)) << ", " << (power == INT_MAX ? "" : std::to_string(power)) << ", " << 
                (pp == INT_MAX ? "" : std::to_string(pp)) << ", " << (accuracy == INT_MAX ? "" : std::to_string(accuracy)) << ", " << 
                (priority == INT_MAX ? "" : std::to_string(priority)) << ", " << (target_id == INT_MAX ? "" : std::to_string(target_id)) << ", " << 
                (damage_class_id == INT_MAX ? "" : std::to_string(damage_class_id)) << ", " << (effect_id == INT_MAX ? "" : std::to_string(effect_id)) << ", " << 
                (effect_chance == INT_MAX ? "" : std::to_string(effect_chance)) << ", " << (contest_type_id == INT_MAX ? "" : std::to_string(contest_type_id)) 
                << ", " << (contest_effect_id == INT_MAX ? "" : std::to_string(contest_effect_id)) << ", " << 
                (super_contest_effect_id == INT_MAX ? "" : std::to_string(super_contest_effect_id)) << std::endl;
            return o;
        }
};

class pokemon_moves_db {
    public:
        int pokemon_id;
        int version_group_id;
        int move_id;
        int pokemon_move_method_id;
        int level;
        int order;
        std::ostream &print(std::ostream &o) const {
            o << (pokemon_id == INT_MAX ? "" : std::to_string(pokemon_id)) << ", " << (version_group_id == INT_MAX ? "" : std::to_string(version_group_id)) << ", " << 
                (move_id == INT_MAX ? "" : std::to_string(move_id)) << ", " << (pokemon_move_method_id == INT_MAX ? "" : std::to_string(pokemon_move_method_id)) << ", " << 
                (level == INT_MAX ? "" : std::to_string(level)) << ", " << (order == INT_MAX ? "" : std::to_string(order)) << std::endl;
            return o;
        }
};

class pokemon_species_db {
    public:
        int id;
        std::string identifier;
        int generation_id;
        int evolves_from_species_id;
        int evolution_chain_id;
        int color_id;
        int shape_id;
        int habitat_id;
        int gender_rate;
        int capture_rate;
        int base_happiness;
        int is_baby;
        int hatch_counter;
        int has_gender_differeence;
        int growth_rate_id;
        int forms_switchable;
        int is_legendary;
        int is_mythical;
        int order;
        int conquest_order;
        std::ostream &print(std::ostream &o) const {
            o << (id == INT_MAX ? "" : std::to_string(id)) << ", " << identifier << ", " << (generation_id == INT_MAX ? "" : std::to_string(generation_id)) << ", " << 
                (evolves_from_species_id == INT_MAX ? "" : std::to_string(evolves_from_species_id)) << ", " << (evolution_chain_id == INT_MAX ? "" : std::to_string(evolution_chain_id)) 
                << ", " << (color_id == INT_MAX ? "" : std::to_string(color_id)) << ", " << (shape_id == INT_MAX ? "" : std::to_string(shape_id)) << ", " << 
                (habitat_id == INT_MAX ? "" : std::to_string(habitat_id)) << ", " << (gender_rate == INT_MAX ? "" : std::to_string(gender_rate)) << ", " << 
                (capture_rate == INT_MAX ? "" : std::to_string(capture_rate)) << ", " << (base_happiness == INT_MAX ? "" : std::to_string(base_happiness)) << ", " << 
                (is_baby == INT_MAX ? "" : std::to_string(is_baby)) << ", " << (hatch_counter == INT_MAX ? "" : std::to_string(hatch_counter)) << ", " << 
                (has_gender_differeence == INT_MAX ? "" : std::to_string(has_gender_differeence)) << ", " << (growth_rate_id == INT_MAX ? "" : std::to_string(growth_rate_id)) << ", " <<
                (forms_switchable == INT_MAX ? "" : std::to_string(forms_switchable)) << ", " << (is_legendary == INT_MAX ? "" : std::to_string(is_legendary)) << ", " << 
                (is_mythical == INT_MAX ? "" : std::to_string(is_mythical)) << ", " << (order == INT_MAX ? "" : std::to_string(order)) << ", "
                << ", " << (conquest_order == INT_MAX ? "" : std::to_string(conquest_order)) << std::endl;
            return o;
        }
};

class experience_db {
    public:
        int growth_rate_id;
        int level;
        int experience;
        std::ostream &print(std::ostream &o) const {
            o << (growth_rate_id == INT_MAX ? "" : std::to_string(growth_rate_id)) << ", " << (level == INT_MAX ? "" : std::to_string(level)) << ", " << (experience == INT_MAX ? "" : std::to_string(experience)) << std::endl;
            return o;
        }
};

class type_names_db {
    public:
        int type_id;
        int local_language_id;
        std::string name;
        std::ostream &print(std::ostream &o) const {
            o << (type_id == INT_MAX ? "" : std::to_string(type_id)) << ", " << (local_language_id == INT_MAX ? "" : std::to_string(local_language_id)) << ", " << name << std::endl;
            return o;
        }
};

class pokemon_stats_db {
    public:
        int pokemon_id;
        int stat_id;
        int base_stat;
        int effort;
        std::ostream &print(std::ostream &o) const {
            o << (pokemon_id == INT_MAX ? "" : std::to_string(pokemon_id)) << ", " << (stat_id == INT_MAX ? "" : std::to_string(stat_id)) << ", " << 
                (base_stat == INT_MAX ? "" : std::to_string(base_stat)) << ", " << (effort == INT_MAX ? "" : std::to_string(effort)) << std::endl;
            return o;
        }
};

class stats_db {
    public:
        int id;
        int damage_class_id;
        std::string identifier;
        int is_battle_only;
        int game_index;
        std::ostream &print(std::ostream &o) const {
            o << (id == INT_MAX ? "" : std::to_string(id)) << ", " << (damage_class_id == INT_MAX ? "" : std::to_string(damage_class_id)) << ", " << identifier << ", " << 
                (is_battle_only == INT_MAX ? "" : std::to_string(is_battle_only)) << ", " << (game_index == INT_MAX ? "" : std::to_string(game_index)) << std::endl;
            return o;
        }
};

class pokemon_types_db {
    public:
        int pokemon_id;
        int type_id;
        int slot;
        std::ostream &print(std::ostream &o) const {
            o << (pokemon_id == INT_MAX ? "" : std::to_string(pokemon_id)) << ", " << (type_id == INT_MAX ? "" : std::to_string(type_id)) << ", " << (slot == INT_MAX ? "" : std::to_string(slot)) << std::endl;
            return o;
        }
};

/*
    Access vectors outside of pokedex src and header
*/
extern std::vector<pokemon_db> p_list;
extern std::vector<moves_db> moves_list;
extern std::vector<pokemon_moves_db> p_moves_list;
extern std::vector<pokemon_species_db> p_species_list;
extern std::vector<experience_db> experience_list;
extern std::vector<type_names_db> type_names_list;
extern std::vector<pokemon_stats_db> p_stats_list;
extern std::vector<stats_db> stats_list;
extern std::vector<pokemon_types_db> p_types_list;

#endif