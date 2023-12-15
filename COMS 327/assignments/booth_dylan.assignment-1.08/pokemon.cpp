#include <iostream>
#include <cstdlib>
#include <vector>

#include "pokemon.h"

// start of class func stuff

void pokemon::set_moves() {

    using namespace std;
    vector<pokemon_moves_db>::size_type i;
    vector<pokemon_moves_db> possible_moves;

    // select random move if any are possible for level up
    while (pokemon::moves.size() < 1 || rand() % 2 == 0) {

        // Adds possible moves to a vector based on pokemon id
        for (i = 0; i < p_moves_list.size(); i++) {
            if (p_moves_list.at(i).pokemon_id == pokemon::id) {
                possible_moves.push_back(p_moves_list.at(i));
            }
        }

        while (possible_moves.size() > 0) {
            int ran = rand() % possible_moves.size();
            if (possible_moves.at(ran).level <= pokemon::level) {
                pokemon::moves.push_back(possible_moves.at(ran));
                break;
            }
            possible_moves.erase(possible_moves.begin() + ran);
        }
        if (pokemon::moves.size() < 1) {
            pokemon::level_up();
        }
    }

}

// end of class func stuff

pokemon *spawnPokemon(int dist) {
    int select = rand() % p_list.size() + 1;
    int lev;

    int min, max;
    if (dist == 0) {
        lev = 1;
    } else if (dist <= 200) {
        min = 1;
        max = dist / 2;
        lev = rand() % (max - min + 1) + min;
    } else {
        min = (dist - 200) / 2;
        max = 100;
        lev = rand() % (max - min + 1) + min;
    }

    pokemon *cur = new pokemon(select, lev);

    return cur;
}