pokemon: pokemon.o queue.o heap.o character.o
	gcc pokemon.o queue.o heap.o character.o -o pokemon -lm

pokemon.o: pokemon.c queue.h pokemon.h character.h
	gcc -Wall -Werror -ggdb pokemon.c -c 

queue.o: queue.c queue.h pokemon.h
	gcc -Wall -Werror -ggdb queue.c -c

heap.o: heap.c heap.h pokemon.h
	gcc -Wall -Werror -ggdb heap.c -c

character.o: character.c character.h pokemon.h
	gcc -Wall -Werror -ggdb character.c -c

clean:
	rm -f pokemon pokemon.o queue.o heap.o character.o