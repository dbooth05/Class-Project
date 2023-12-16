#include <iostream>
#include <fstream>
#include <sstream>
#include <limits.h>

#include "pokedex.h"

#define pokemon_file "pokemon.csv"
#define moves_file "moves.csv"
#define pokemon_moves_file "pokemon_moves.csv"
#define pokemon_species_file "pokemon_species.csv"
#define experience_file "experience.csv"
#define type_names_file "type_names.csv"
#define pokemon_stats_file "pokemon_stats.csv"
#define stats_file "stats.csv"
#define pokemon_types_file "pokemon_types.csv"

std::vector<pokemon_db> p_list;
std::vector<moves_db> moves_list;
std::vector<pokemon_moves_db> p_moves_list;
std::vector<pokemon_species_db> p_species_list;
std::vector<experience_db> experience_list;
std::vector<type_names_db> type_names_list;
std::vector<pokemon_stats_db> p_stats_list;
std::vector<stats_db> stats_list;
std::vector<pokemon_types_db> p_types_list;

/*
    function for each csv that needs loaded
*/
void load_pokemon() {

    std::string file = location_1;
    file += pokemon_file;

    std::ifstream f(file); // open the file

    if (!(f.is_open())) {
        file = location_2;
        file += pokemon_file;
    
        std::ifstream f(file);
        if (!(f.is_open())) {
            std::cout << "File not found" << std::endl;
            return;
        }
    }
    
    std::string line;
    std::getline(f, line); // removing header line
    p_list.emplace(p_list.begin(), pokemon_db{});
    while(std::getline(f, line)) {
        std::istringstream ls(line);
        std::string part;
        pokemon_db cur;
        for (int i = 0; i < 8; i++) {
            std::getline(ls, part, ',');
            if (part == "") {
                part = std::to_string(INT_MAX);
            }

            switch (i)
            {
                case 0: cur.id = std::stoi(part); break;
                case 1: cur.identifier = part; break;
                case 2: cur.species_id = std::stoi(part); break;
                case 3: cur.height = std::stoi(part); break;
                case 4: cur.weight = std::stoi(part); break;
                case 5: cur.base_experience = std::stoi(part); break;
                case 6: cur.order = std::stoi(part); break;
                case 7: cur.is_default = std::stoi(part); break;
                default:
                    break;
            }
        }

        p_list.push_back(cur);
    }


}

void load_moves () {

    std::string file = location_1;
    file += moves_file;

    std::ifstream f(file); // open the file

    if (!(f.is_open())) {
        file = location_2;
        file += moves_file;
    
        std::ifstream f(file);
        if (!(f.is_open())) {
            std::cout << "File not found" << std::endl;
            return;
        }
    }
    
    std::string line;
    std::getline(f, line); // removing header line
    moves_list.emplace(moves_list.begin(), moves_db{});
    while(std::getline(f, line)) {
        std::istringstream ls(line);
        std::string part;
        moves_db cur;
        for (int i = 0; i < 15; i++) {
            std::getline(ls, part, ',');
            if (part == "") {
                part = std::to_string(INT_MAX);
            }

            switch (i)
            {
            case 0:     cur.id = std::stoi(part); break;
            case 1:     cur.identifier = part; break;
            case 2:     cur.generation_id = std::stoi(part); break;
            case 3:     cur.type_id = std::stoi(part); break;
            case 4:     cur.power = std::stoi(part); break;
            case 5:     cur.pp = std::stoi(part); break;
            case 6:     cur.accuracy = std::stoi(part); break;
            case 7:     cur.priority = std::stoi(part); break;
            case 8:     cur.target_id = std::stoi(part); break;
            case 9:     cur.damage_class_id = std::stoi(part); break;
            case 10:    cur.effect_id = std::stoi(part); break;
            case 11:    cur.effect_chance = std::stoi(part); break;
            case 12:    cur.contest_type_id = std::stoi(part); break;
            case 13:    cur.contest_effect_id = std::stoi(part); break;
            case 14:    cur.super_contest_effect_id = std::stoi(part); break;
            default:
                break;
            }
        }

        moves_list.push_back(cur);
    }
    
}

void load_p_moves() {
    std::string file = location_1;
    file += pokemon_moves_file;

    std::ifstream f(file); // open the file

    if (!(f.is_open())) {
        file = location_2;
        file += pokemon_moves_file;
    
        std::ifstream f(file);
        if (!(f.is_open())) {
            std::cout << "File not found" << std::endl;
            return;
        }
    }
    
    std::string line;
    std::getline(f, line); // removing header line
    p_moves_list.emplace(p_moves_list.begin(), pokemon_moves_db{});
    while(std::getline(f, line)) {
        std::istringstream ls(line);
        std::string part;
        pokemon_moves_db cur;
        for (int i = 0; i < 6; i++) {
            std::getline(ls, part, ',');
            if (part == "") {
                part = std::to_string(INT_MAX);
            }

            switch (i)
            {
                case 0: cur.pokemon_id = std::stoi(part); break;
                case 1: cur.version_group_id = std::stoi(part); break;
                case 2: cur.move_id = std::stoi(part); break;
                case 3: cur.pokemon_move_method_id = std::stoi(part); break;
                case 4: cur.level = std::stoi(part); break;
                case 5: cur.order = std::stoi(part); break;
                default:
                    break;
            }
        }

        p_moves_list.push_back(cur);
    }

}

void load_p_species() {

    std::string file = location_1;
    file += pokemon_species_file;

    std::ifstream f(file);

    if (!(f.is_open())) {
        file = location_2;
        file += pokemon_species_file;

        std::ifstream f(file);
        if (!(f.is_open())) {
            std::cout << "File not found" << std::endl;
            return;
        }
    }

    std::string line;
    std::getline(f, line);
    p_species_list.emplace(p_species_list.begin(), pokemon_species_db{});
    while (std::getline(f, line)) {
        std::istringstream ls(line);
        std::string part;
        pokemon_species_db cur;
        for (int i = 0; i < 15; i++) {
            std::getline(ls, part, ',');
            if (part == "") {
                part = std::to_string(INT_MAX);
            }
            
            switch(i) {
                case 0:     cur.id = std::stoi(part); break;
                case 1:     cur.identifier = part; break;
                case 2:     cur.generation_id = std::stoi(part); break;
                case 3:     cur.evolves_from_species_id = std::stoi(part); break;
                case 4:     cur.evolution_chain_id = std::stoi(part); break;
                case 5:     cur.color_id = std::stoi(part); break;
                case 6:     cur.shape_id = std::stoi(part); break;
                case 7:     cur.habitat_id = std::stoi(part); break;
                case 8:     cur.gender_rate = std::stoi(part); break;
                case 9:     cur.capture_rate = std::stoi(part); break;
                case 10:    cur.base_happiness = std::stoi(part); break;
                case 11:    cur.is_baby = std::stoi(part); break;
                case 12:    cur.hatch_counter = std::stoi(part); break;
                case 13:    cur.has_gender_differeence = std::stoi(part); break;
                case 14:    cur.growth_rate_id = std::stoi(part); break;
                case 15:    cur.forms_switchable = std::stoi(part); break;
                case 16:    cur.is_legendary = std::stoi(part); break;
                case 17:    cur.is_mythical = std::stoi(part); break;
                case 18:    cur.order = std::stoi(part); break;
                case 19:    cur.conquest_order = std::stoi(part); break;
                default:    break;
            }
        }
        
        p_species_list.push_back(cur);
    }

}

void load_experience() {

    std::string file = location_1;
    file += experience_file;

    std::ifstream f(file);

    if (!(f.is_open())) {
        file = location_2;
        file += experience_file;

        std::ifstream f(file);
        if (!(f.is_open())) {
            std::cout << "File not found" << std::endl;
            return;
        }
    }

    std::string line;
    std::getline(f, line);
    experience_list.emplace(experience_list.begin(), experience_db{});
    while (std::getline(f, line)) {
        std::istringstream ls(line);
        std::string part;
        experience_db cur;
        for (int i = 0; i < 3; i++) {
            std::getline(ls, part, ',');
            if (part == "") {
                part = std::to_string(INT_MAX);
            }

            switch(i) {
                case 0: cur.growth_rate_id = std::stoi(part); break;
                case 1: cur.level = std::stoi(part); break;
                case 2: cur.experience = std::stoi(part); break;
                default: break;
            }
        }

        experience_list.push_back(cur);
    }

}

void load_type_names() {
    
    std::string file = location_1;
    file += type_names_file;

    std::ifstream f(file);

    if (!(f.is_open())) {
        file = location_2;
        file += type_names_file;

        std::ifstream f(file);
        if(!(f.is_open())) {
            std::cout << "File not found" << std::endl;
            return;
        }
    }

    std::string line;
    std::getline(f, line);
    type_names_list.emplace(type_names_list.begin(), type_names_db{});
    while (std::getline(f, line)) {
        std::istringstream ls(line);
        std::string part;
        type_names_db cur;
        for (int i = 0; i < 3; i++) {
            std::getline(ls, part, ',');
            if (part == "") {
                part = std::to_string(INT_MAX);
            }

            switch(i) {
                case 0: cur.type_id = std::stoi(part); break;
                case 1: cur.local_language_id = std::stoi(part); break;;
                case 2: cur.name = part; break;
                default: break;
            }
        }

        if (cur.local_language_id == 9) {
            type_names_list.push_back(cur);
        }
    }

}

void load_p_stats() {

    std::string file = location_1;
    file += pokemon_stats_file;

    std::ifstream f(file);

    if (!(f.is_open())) {
        file = location_2;
        file += pokemon_stats_file;

        std::ifstream f(file);
        if (!(f.is_open())) {
            std::cout << "File not found" << std::endl;
            return;
        } 
    }    

    std::string line;
    std::getline(f, line);
    p_stats_list.emplace(p_stats_list.begin(), pokemon_stats_db{});
    while (std::getline(f, line)) {
        std::istringstream ls(line);
        std::string part;
        pokemon_stats_db cur;
        for (int i = 0; i < 4; i++) {
            std::getline(ls, part, ',');
            if (part == "") {
                part = std::to_string(INT_MAX);
            }

            switch(i) {
                case 0: cur.pokemon_id = std::stoi(part); break;
                case 1: cur.stat_id = std::stoi(part); break;
                case 2: cur.base_stat = std::stoi(part); break;
                case 3: cur.effort = std::stoi(part); break;
                default: break;
            }
        }

        p_stats_list.push_back(cur);
    }

}

void load_stats(std::vector<stats_db> stats) {

    std::string file = location_1;
    file += stats_file;

    std::ifstream f(file);

    if (!(f.is_open())) {
        file = location_2;
        file += stats_file;

        std::ifstream f(file);
        if (!(f.is_open())) {
            std::cout << "File not found" << std::endl;
            return;
        }
    }

    std::string line;
    std::getline(f, line);
    stats_list.emplace(stats_list.begin(), stats_db{});
    while (std::getline(f, line)) {
        std::istringstream ls(line);
        std::string part;
        stats_db cur;
        for (int i = 0; i < 5; i++) {
            std::getline(ls, part, ',');
            if (part == "") {
                part = std::to_string(INT_MAX);
            }

            switch(i) {
                case 0: cur.id = std::stoi(part); break;
                case 1: cur.damage_class_id = std::stoi(part); break;
                case 2: cur.identifier = part; break;
                case 3: cur.is_battle_only = std::stoi(part); break;
                case 4: cur.game_index = std::stoi(part); break;
                default: break;
            }
        }

        stats.push_back(cur);
    }

}

void load_p_types() {

    std::string file = location_1;
    file += pokemon_types_file;

    std::ifstream f(file);

    if (!(f.is_open())) {
        file = location_2;
        file += pokemon_types_file;

        std::ifstream f(file);
        if (!(f.is_open())) {
            std::cout << "File not found" << std::endl;
            return;
        }
    }

    std::string line;
    std::getline(f, line);
    p_types_list.emplace(p_types_list.begin(), pokemon_types_db{});
    while (std::getline(f, line)) {
        std::istringstream ls(line);
        std::string part;
        pokemon_types_db cur;
        for (int i = 0; i < 3; i++) {
            std::getline(ls, part, ',');
            if (part == "") {
                part = std::to_string(INT_MAX);
            }

            switch(i) {
                case 0: cur.pokemon_id = std::stoi(part); break;
                case 1: cur.type_id = std::stoi(part); break;
                case 2: cur.slot = std::stoi(part); break;
            }
        }

        p_types_list.push_back(cur);
    }

}


void load_csvs() {

    load_pokemon(); 
    load_moves();
    load_p_moves();
    load_p_species(); 
    load_experience(); 
    load_type_names(); 
    load_p_stats(); 
    // load_stats(); 
    load_p_types(); 

}